package org.petmeet.api.domain.member.applicatoin.dto.req;

import org.petmeet.api.domain.login.application.dto.LoginDTO;
import org.petmeet.db.domain.entities.member.Address;
import org.petmeet.db.domain.entities.member.MemberEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor @NoArgsConstructor
@Getter
public class MemberRegisterRequest {

	private String name;
	private Address address;
	private String email;
	private String phoneNumber;
	private String gender;
	private LoginDTO login;

	public static MemberEntity toEntity(MemberRegisterRequest request) {

		//LoginEntity login = LoginDTO.toEntity(request.getLogin());

		return MemberEntity.builder()
			//.login(login)
			.address(request.getAddress())
			.email(request.getEmail())
			.name(request.getName())
			.gender(request.getGender())
			.phoneNumber(request.getPhoneNumber())
			.build();
	}

}
