package org.petmeet.db.domain.enums;

import java.util.Arrays;

import lombok.Getter;

@Getter
public enum Role {

	USER("USER","유저 권한"),
	ADMIN("ADMIN","관리자 권한"),
	UNKNOWN("UNKNOWN","알 수 없는 권한")
	;

	private String code;
	private String description;

	Role (String code, String description) {
		this.code = code;
		this.description = description;
	}

}
