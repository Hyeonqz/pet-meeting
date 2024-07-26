package org.petmeet.http.api.filter;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LoggerFilter implements Filter{

	@Override
	public void doFilter (ServletRequest req, ServletResponse res, FilterChain chain) throws
		IOException,
		ServletException {
		var request = new ContentCachingRequestWrapper((HttpServletRequest)req);
		var response = new ContentCachingResponseWrapper((HttpServletResponse)res);

		// 실행전 메소드
		chain.doFilter(request, response);

		// 실행 후 메소드
		var uri = request.getRequestURI();

		// Request
		StringBuilder requestSb = new StringBuilder();
		var headers = request.getHeaderNames();

		headers.asIterator().forEachRemaining(headerKey -> {
			var headerValue = request.getHeader(headerKey);
			requestSb.append(headerKey).append(" : ").append(headerValue).append("\n");
		});

		var requestBody = new String(request.getContentAsByteArray());
		log.info("[RequestURI] : [{}]", uri);
		log.info("[RequestBody] : [{}]", requestBody);

		// Response
		StringBuilder responseSb = new StringBuilder();
		response.getHeaderNames().forEach(headerkey -> {
			var headerValue = response.getHeader(headerkey);

			responseSb.append(headerkey).append(" : ").append(headerValue).append("\n");
		});

		var responseBody = new String(response.getContentAsByteArray());
		log.info("[ResponseBody] : [{}]", responseBody);

		// 사용안할시 responseBody 비워져서 나옴.
		response.copyBodyToResponse();

	}

}