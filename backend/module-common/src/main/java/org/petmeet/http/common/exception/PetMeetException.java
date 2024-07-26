package org.petmeet.http.common.exception;

import org.petmeet.http.common.error.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PetMeetException extends RuntimeException{

	private ErrorCode errorCode;
	private Errors errors;
	private Throwable errorCause;

	public PetMeetException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}

	public PetMeetException(ErrorCode errorCode, Errors errors) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
		this.errors = errors;
	}

	public PetMeetException(ErrorCode errorCode, Throwable errorCause) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
		this.errorCause = errorCause;
	}

	public PetMeetException (ErrorCode errorCode, Errors errors, Throwable errorCause) {
		super(errorCode.getMessage(), errorCause);
		this.errorCode = errorCode;
		this.errors = errors;
		this.errorCause = errorCause;
	}


}
