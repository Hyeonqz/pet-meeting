package org.petmeet.http.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public enum ApiResponseStatus {

	SUCCESS("success"),
	FAIL("fail"),
	ERROR("error"),
	;

	ApiResponseStatus (String code) {
		this.code = code;
	}

	private final String code;
}
