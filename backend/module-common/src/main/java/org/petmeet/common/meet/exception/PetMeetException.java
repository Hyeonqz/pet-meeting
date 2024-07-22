package org.petmeet.common.meet.exception;

public class PetMeetException extends RuntimeException{

	public PetMeetException () {
		super();
	}

	public PetMeetException (String message) {
		super(message);
	}

	public PetMeetException (String message, Throwable cause) {
		super(message, cause);
	}

	public PetMeetException (Throwable cause) {
		super(cause);
	}

	protected PetMeetException (String message, Throwable cause, boolean enableSuppression,
		boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
