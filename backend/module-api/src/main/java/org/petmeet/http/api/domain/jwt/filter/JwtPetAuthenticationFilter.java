package org.petmeet.http.api.domain.jwt.filter;

import java.io.IOException;

import org.petmeet.http.api.domain.jwt.service.CustomUserDetailService;
import org.petmeet.http.api.domain.jwt.service.CustomUserDetails;
import org.petmeet.http.api.domain.jwt.service.JwtUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtPetAuthenticationFilter extends OncePerRequestFilter {
	private final JwtUtils jwtUtils;
	private final CustomUserDetailService customUserDetailService;

	@Override
	protected void doFilterInternal (HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		String token = jwtUtils.resolveToken(request);
		if (token != null && jwtUtils.validateToken(token)) {
			String username = jwtUtils.getUsername(token);
			CustomUserDetails userDetails = (CustomUserDetails)customUserDetailService.loadUserByUsername(username);

			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

		filterChain.doFilter(request, response);

	}

	/** 구현 목표*/
	// 로그인을 했다 -> JWT 토큰을 발급한다. 그리고 그 토큰을 header 에 박아둔다.
	// 현재 로그인 했으니 로그인 한 사람을 ROLE 이 있다.
	// 그 로그인 한 사람이 API 를 요청할 떄 TOKEN 을 통해 권한이 USER 인지 검증한다
	// 현재 Header 에 있는 Token 에 담긴 정보가 USER 라면 api 를 호출하고, 아니라면 거부 되게한다.



}
