package org.petmeet.api.domain.jwt.filter;

import java.io.IOException;

import org.petmeet.api.domain.jwt.service.JwtUtils;
import org.petmeet.db.domain.repositories.RefreshRepository;
import org.springframework.web.filter.GenericFilterBean;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilterBean {
	private final JwtUtils jwtUtils;
	private final RefreshRepository refreshRepository;

	@Override
	public void doFilter (ServletRequest servletRequest, ServletResponse servletResponse,
		FilterChain filterChain) throws IOException, ServletException {

		doFilter((HttpServletRequest)servletRequest, (HttpServletResponse)servletResponse, filterChain);
	}

	private void doFilter (HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws IOException, ServletException {

		String requestURI = request.getRequestURI();
		if(!requestURI.matches("^\\/logout$")) {
			filterChain.doFilter(request, response);
			return ;
		}
		String requestMethod = request.getMethod();
		if(!requestMethod.equals("POST")) {
			filterChain.doFilter(request, response);
			return ;
		}

		String refreshToken  = null;
		Cookie[] cookies = request.getCookies();

		for(Cookie cookie : cookies) {
			if(cookie.getName().equals("Refresh_Token")) {
				refreshToken = cookie.getValue();
			}
		}

		if(refreshToken == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return ;
		}

		try {
			jwtUtils.isTokenExpired(refreshToken);
		} catch (ExpiredJwtException e) {
			//response status code
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return ;
		}

		String category = jwtUtils.getCategory(refreshToken);
		if (!category.equals("Refresh_Token")) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return ;
		}

		Boolean isExist = refreshRepository.existsByRefreshToken(refreshToken);
		if (!isExist) {
			//response status code
			log.info("이미 로그아웃이 되었습니다.");
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		refreshRepository.deleteByRefreshToken(refreshToken);

		//Refresh 토큰 Cookie 값 null 로 설정
		Cookie cookie = new Cookie("Refresh_Token", null);
		cookie.setMaxAge(0);
		cookie.setPath("/");

		response.addCookie(cookie);
		response.setStatus(HttpServletResponse.SC_OK);

	}

}
