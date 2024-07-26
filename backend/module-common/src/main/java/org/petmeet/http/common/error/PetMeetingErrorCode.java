package org.petmeet.http.common.error;

import static org.petmeet.http.common.error.ApiResponseStatus.*;

import java.util.Arrays;

import org.petmeet.http.common.exception.PetMeetException;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PetMeetingErrorCode implements ErrorCode{

	INVALID_ACCESS("IA01","유효하지 않은 접근 입니다.",ERROR),
	UNKNOWN_ERROR("ER01", "알 수 없는 에러", ERROR)
	;

	private final String code;
	private final String description;
	private final ApiResponseStatus status;

	public static PetMeetingErrorCode fromCode(String code) {
		return Arrays.stream(PetMeetingErrorCode.values())
			.filter(pet -> pet.getCode().equals(code))
			.findFirst()
			.orElse(PetMeetingErrorCode.UNKNOWN_ERROR)
			;
	}
}
