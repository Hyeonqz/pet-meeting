package org.petmeet.http.api.config;

import java.util.Collections;

import org.petmeet.http.db.jwt.RefreshRepository;
import org.petmeet.http.api.domain.jwt.filter.CustomLogoutFilter;
import org.petmeet.http.api.domain.jwt.filter.JwtFilter;
import org.petmeet.http.api.domain.jwt.filter.LoginFilter;
import org.petmeet.http.api.domain.jwt.service.JwtUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@EnableWebSecurity
@Configuration
public class WebSecurityConfig {
	private final AuthenticationConfiguration configuration;
	private final JwtUtils jwtUtils;
	private final RefreshRepository refreshRepository;

	@Bean
	protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http
			.authorizeHttpRequests((auth) -> auth
				.requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
				.requestMatchers("/api/v1/**", "/").permitAll()
				.requestMatchers("/api/admin/**").hasRole("ADMIN")
				.anyRequest().authenticated()
			)

			.csrf(AbstractHttpConfigurer::disable)

			.cors((cors) -> cors
			.configurationSource(new CorsConfigurationSource() {
				@Override
				public CorsConfiguration getCorsConfiguration (HttpServletRequest request) {
					CorsConfiguration corsConfiguration = new CorsConfiguration();
					corsConfiguration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
					corsConfiguration.setAllowedMethods(Collections.singletonList("*"));
					corsConfiguration.setAllowCredentials(true);
					corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
					corsConfiguration.setMaxAge(3600L);

					return corsConfiguration;
				}
			}))

			.formLogin(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)

			.addFilterBefore(new JwtFilter(jwtUtils), LoginFilter.class)

			.addFilterAt(
				new LoginFilter(authenticationManager(configuration), jwtUtils, refreshRepository),
				UsernamePasswordAuthenticationFilter.class)

			.addFilterBefore(new CustomLogoutFilter(jwtUtils, refreshRepository), LogoutFilter.class) // 로그아웃 필터 전에 다 등록을 하겠다는 뜻

			.sessionManagement((session) -> session
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

		;

		return http.build();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

}
