package org.petmeet.http.api.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	private List<String> SWAGGER = List.of(
		"/swagger-ui.html",
		"/swagger-ui/**"
	);

	//TODO: 나중에 다시 설정하기
	@Override
	public void addInterceptors (InterceptorRegistry registry) {
		WebMvcConfigurer.super.addInterceptors(registry);
	}

	// Spring Security 에서 swagger 를 불러오기 위한 Config 및 Cors 설정
	@Override
	public void addCorsMappings (CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedOrigins("*")
			.allowedOrigins("http://localhost:3000")
			.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
			.allowedHeaders("*")
			.allowCredentials(false)
			.maxAge(3600L);
	}

}
