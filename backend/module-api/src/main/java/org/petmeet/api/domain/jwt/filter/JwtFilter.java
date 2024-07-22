package org.petmeet.api.domain.jwt.filter;

import java.io.IOException;
import java.io.PrintWriter;

import org.petmeet.api.domain.jwt.service.CustomUserDetails;
import org.petmeet.api.domain.jwt.service.JwtUtils;
import org.petmeet.db.domain.entities.login.LoginEntity;
import org.petmeet.db.domain.entities.member.MemberEntity;
import org.petmeet.db.domain.enums.Role;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
	private final JwtUtils jwtUtils;

	@Override
	protected void doFilterInternal (HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		// 헤더에서 access키에 담긴 토큰을 꺼냄
		String accessToken = request.getHeader("Access_Token");

		// 토큰이 없다면 다음 필터로 넘김
		if (accessToken == null) {
			filterChain.doFilter(request, response); // 다음 필터로 넘긴다.
			return ;
		}

		// 토큰 만료 여부 확인, 만료시 다음 필터로 넘기지 않음
		try {
			jwtUtils.isTokenExpired(accessToken);
		} catch (ExpiredJwtException e) {

			//response body
			PrintWriter writer = response.getWriter();
			writer.print("Access token expired");

			//response status code
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return ;
		}

		// 토큰이 Access_Token 인지 확인 (발급시 페이로드에 명시)
		String category = jwtUtils.getCategory(accessToken);

		if (!category.equals("Access_Token")) {

			//response body
			PrintWriter writer = response.getWriter();
			writer.print("invalid Access_Token");

			//response status code
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		// username, role 값을 획득
		String username = jwtUtils.getUsername(accessToken);
		String role = jwtUtils.getRole(accessToken);

		// TODO : Login 객체, User 객체 가져오기.
		LoginEntity login = new LoginEntity(username,"password");
		// TODO: 오류 예상?
		MemberEntity member = new MemberEntity(Role.USER, login);

		CustomUserDetails customUserDetails = new CustomUserDetails(member);

		Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authToken);

		filterChain.doFilter(request, response);
	}

}
