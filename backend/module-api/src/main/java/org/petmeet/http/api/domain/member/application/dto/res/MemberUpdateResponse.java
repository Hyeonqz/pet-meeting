package org.petmeet.http.api.domain.member.application.dto.res;

import java.time.LocalDateTime;

import org.petmeet.http.api.domain.login.application.dto.LoginDTO;
import org.petmeet.http.db.member.Address;
import org.petmeet.http.db.member.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class MemberUpdateResponse {
	private String name;
	private String email;
	private String phoneNumber;
	private String gender;
	private Role role;
	private Address address;
	private LocalDateTime updatedAt;
}
