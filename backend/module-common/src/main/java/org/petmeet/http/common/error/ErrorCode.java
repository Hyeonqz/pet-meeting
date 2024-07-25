package org.petmeet.http.common.error;

public interface ErrorCode {
	String getCode();
	String getDescription();
	default String getMessage() {
		return String.format("[%s] %s", getCode(), getDescription());
	}
}
