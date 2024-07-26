package org.petmeet.http.api.domain.member.application.dto.req;

import org.petmeet.http.api.domain.login.application.dto.LoginDTO;
import org.petmeet.http.db.member.Address;
import org.petmeet.http.db.member.MemberEntity;

import lombok.Getter;

@Getter
public class MemberUpdateRequest {
	private String name;
	private Address address;
	private String email;
	private String phoneNumber;
	private String gender;

	public MemberEntity toEntity(MemberUpdateRequest request) {
		return MemberEntity.builder()
			.address(request.getAddress())
			.email(request.getEmail())
			.name(request.getName())
			.gender(request.getGender())
			.phoneNumber(request.getPhoneNumber())
			.build();
	}

}
