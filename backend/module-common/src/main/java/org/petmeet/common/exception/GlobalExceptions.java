package org.petmeet.common.exception;

import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
@Order(value = Integer.MAX_VALUE)
public class GlobalExceptions {

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> GlobalExceptionHandler(Exception ex, WebRequest request) {
		log.error("[Request] : [{}]",request);
		log.error("[Error Response] : [{}]", ex.getMessage());
		log.error("[Error Location] : [{}]", (Object)ex.getStackTrace());

		//TODO: 뭘 리턴 시킬지 고민좀 해보자
		return null;
	}
}
