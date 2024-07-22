package org.petmeet.http.api.domain.jwt.filter;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

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

import jakarta.servlet.FilterChain;
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
	protected String obtainUsername(HttpServletRequest request) {
		String username = request.getParameter("username");
		log.info("Username from request: {}", username);
		return (username != null) ? username.trim() : "";
	}

	@Override
	protected String obtainPassword(HttpServletRequest request) {
		String password = request.getParameter("password");
		log.info("Password from request: {}", password);
		return (password != null) ? password : "";
	}

	// 여기서 만든 filter 들은 SecurityConfig 에 등록을 해줘야 한다.
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws
		AuthenticationException {

		String username = this.obtainUsername(request);
		String password = this.obtainPassword(request);

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
		addRefreshTokenEntity(username,refreshToken, 86400000L);

		// HTTP 인증 방식은 RFC7235 정의에 따라 아래 인증 헤더 형태를 가져야 한다.
		response.addHeader("Authorization", "Bearer " + accessToken);
		response.setHeader("Access_Token", accessToken);
		response.addCookie(jwtUtils.createCookie("Refresh_Token",refreshToken));
		response.setStatus(HttpStatus.OK.value());

		log.info("[Authorization Access_Token] : [{}]", accessToken);
		log.info("[Authorization Refresh_Token] : [{}]", refreshToken);

		log.info("Success Login");
	}

	//로그인 실패시 실행하는 메소드
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
		response.setStatus(401);
		log.info("Unsuccessful Login");
	}

	// Refresh_Token DB 에 저장
	private void addRefreshTokenEntity(String username, String refresh, Long expiredMs) {
		Date date = new Date(System.currentTimeMillis() + expiredMs);

		RefreshToken refreshEntity = new RefreshToken(username,refresh,date);

		refreshRepository.save(refreshEntity);
	}
}
