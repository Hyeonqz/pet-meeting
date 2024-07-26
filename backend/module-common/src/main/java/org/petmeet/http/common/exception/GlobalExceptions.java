package org.petmeet.http.common.exception;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
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

/*	@ExceptionHandler
	public ResponseEntity<?> GlobalExceptionHandler(Exception ex, WebRequest request) {
		log.error("[Error Response] : {}", ex.getMessage());
		log.error("[Error Where?] : {}", (Object)ex.getStackTrace());

		log.debug("[Exception] : {}", ex.getLocalizedMessage());

		//TODO: 뭘 리턴 시킬지 고민좀 해보자
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}*/
}
