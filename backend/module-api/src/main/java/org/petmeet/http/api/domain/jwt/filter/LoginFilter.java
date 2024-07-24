package org.petmeet.http.api.domain.jwt.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.petmeet.http.api.domain.login.application.dto.LoginDTO;
import org.petmeet.http.db.jwt.RefreshToken;
import org.petmeet.http.db.jwt.RefreshRepository;
import org.petmeet.http.api.domain.jwt.service.CustomUserDetails;
import org.petmeet.http.api.domain.jwt.service.JwtUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
	private final AuthenticationManager authenticationManager;
	private final JwtUtils jwtUtils;
	private final RefreshRepository refreshRepository;

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws
		AuthenticationException {

		LoginDTO loginDTO = new LoginDTO();

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			ServletInputStream inputStream = request.getInputStream();
			String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
			loginDTO = objectMapper.readValue(messageBody, LoginDTO.class);

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		System.out.println(loginDTO.getUsername());

		String username = loginDTO.getUsername();
		String password = loginDTO.getPassword();

		log.info("username: {}", username);
		log.info("password: {}", password);

		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);

		return authenticationManager.authenticate(authToken);
	}

	//로그인 성공시 실행 메소드
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {

		CustomUserDetails userDetails = (CustomUserDetails)authentication.getPrincipal();
		String username =  userDetails.getUsername();

		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
		GrantedAuthority auth = iterator.next();
		String role = auth.getAuthority();

		// 토큰 생성
		String accessToken = jwtUtils.createToken("Access_Token",username,role,600000L);
		String refreshToken = jwtUtils.createToken("Refresh_Token",username,role,86400000L);

		// refresh Token 저장
		addRefreshTokenEntity(refreshToken,username, 86400000L);

		// HTTP 인증 방식은 RFC7235 정의에 따라 아래 인증 헤더 형태를 가져야 한다.
		response.addHeader("Authorization", "Bearer " + accessToken);
		response.setHeader("Access_Token", accessToken);
		response.addCookie(jwtUtils.createCookie("Refresh_Token",refreshToken));
		response.setStatus(HttpStatus.OK.value());

		log.info("[Authorization] : Bearer {}", accessToken);
		log.info("[Authorization Refresh_Token] : [{}]", refreshToken);

		log.info("Success Login");
	}

	//로그인 실패시 실행하는 메소드
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
		response.setStatus(401);
		log.info("Unsuccessful Login");
	}

	private void addRefreshTokenEntity(String refresh, String username, Long expiredMs) {
		Date date = new Date(System.currentTimeMillis() + expiredMs);
		RefreshToken refreshEntity = RefreshToken.builder()
			.refreshToken(refresh)
			.username(username)
			.expiredAt(expiredMs.toString())
			.build();
		refreshRepository.save(refreshEntity);
	}
}
