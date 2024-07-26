package org.petmeet.http.api.domain.jwt.filter;

import java.io.IOException;
import java.io.PrintWriter;

import org.petmeet.http.api.domain.member.application.dto.req.MemberDTO;
import org.petmeet.http.db.login.LoginEntity;
import org.petmeet.http.db.member.MemberEntity;
import org.petmeet.http.db.member.Role;
import org.petmeet.http.api.domain.jwt.service.CustomUserDetails;
import org.petmeet.http.api.domain.jwt.service.JwtUtils;
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

		String accessToken = request.getHeader("Access_Token");

		if (accessToken == null) {
			filterChain.doFilter(request, response); // 다음 필터로 넘긴다.
			return ;
		}

		try {
			jwtUtils.isTokenExpired(accessToken);
		} catch (ExpiredJwtException e) {

			PrintWriter writer = response.getWriter();
			writer.print("Access token is expired");

			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return ;
		}

		// 토큰이 access 인지 refresh 인지
		String category = jwtUtils.getCategory(accessToken);

		if (!category.equals("Access_Token")) {

			PrintWriter writer = response.getWriter();
			writer.print("invalid Access_Token");

			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		String username = jwtUtils.getUsername(accessToken);
		String role = jwtUtils.getRole(accessToken);

		LoginEntity login = new LoginEntity(username,"password");
		login.onUpdate();
		MemberEntity member = new MemberEntity(Role.USER, login);
		MemberDTO memberDTO = MemberDTO.from(member);

		CustomUserDetails customUserDetails = new CustomUserDetails(login,memberDTO);

		Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authToken);

		filterChain.doFilter(request, response);
	}

}
