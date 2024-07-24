package org.petmeet.http.api.domain.jwt.service;

import java.sql.Ref;
import java.time.LocalDateTime;
import java.util.Date;

import org.petmeet.http.db.jwt.RefreshToken;
import org.petmeet.http.db.jwt.RefreshRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class JwtService {
	private final JwtUtils jwtUtils;
	private final RefreshRepository refreshRepository;
	private final RedisService redisService;

	@Transactional
	public ResponseEntity<?> issueRefreshToken (HttpServletRequest request, HttpServletResponse response) {
		// 여기서 refresh_Token 이 안 넘어 오는듯.
		String refresh_token = null;
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("Refresh_Token")) {
				refresh_token = cookie.getValue();
			}
		}

		if(refresh_token == null) {
			throw new RuntimeException("Refresh Token is Null");
		}

		try {
			jwtUtils.isTokenExpired(refresh_token);
		} catch (ExpiredJwtException e) {
			throw new RuntimeException("Refresh Token is Expired");
		}

		String category = jwtUtils.getCategory(refresh_token);

		if(!category.equals("Refresh_Token")) {
			throw new RuntimeException("This is not refresh Token");
		}

		// DB 에 저장되어 있는지 확인
		Boolean isExist = refreshRepository.existsByRefreshToken(refresh_token);
		if(!isExist) {
			log.debug("Invalid Refresh Token is exist : {}", refresh_token);
		}


		String username = jwtUtils.getUsername(refresh_token);
		String role = jwtUtils.getRole(refresh_token);

		String newAccessToken = jwtUtils.createToken("Access_Token",username,role,600000L);
		String newRefreshToken = jwtUtils.createToken("Refresh_Token",username,role,86400000L); //24시간

		// 이 메소드가 안도는 듯?
		// TODO: TODO: 리프레쉬 토큰 재 발급시 redis 기존 토큰 삭제하기
		refreshRepository.deleteByRefreshToken(refresh_token); // 기존 refreshToken 삭제
		log.info("Refresh Token is Delete ? : {}", refresh_token);
		addNewRefreshToken(newRefreshToken, username, 86400000L);

		//response.addHeader("Authorization", "Bearer " + newAccessToken);
		response.addCookie(jwtUtils.createCookie("Refresh_Token", newRefreshToken));
		response.setHeader("Access_Token",newAccessToken);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	private void addNewRefreshToken(String refresh, String username, Long expiredMs) {
		Date date = new Date(System.currentTimeMillis() + expiredMs);

		refreshRepository.save(RefreshToken.builder()
			.refreshToken(refresh)
			.username(username)
			.expiredAt(expiredMs.toString())
			.category("This is NewRefresh-Token")
			.createdAt(LocalDateTime.now())
			.build()
		);
	}

}
