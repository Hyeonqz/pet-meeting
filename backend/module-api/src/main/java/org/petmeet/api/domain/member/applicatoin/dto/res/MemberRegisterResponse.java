package org.petmeet.api.domain.member.applicatoin.dto.res;

import java.time.LocalDateTime;

import org.petmeet.api.domain.login.application.dto.LoginDTO;
import org.petmeet.db.entities.member.Address;
import org.petmeet.db.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class MemberRegisterResponse {

	private String name;
	private String email;
	private String phoneNumber;
	private String gender;
	private Role role;
	private Address address;
	private LoginDTO login;
	private LocalDateTime createdAt;

}
