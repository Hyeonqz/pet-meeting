package org.petmeet.http.api.domain.member.application.dto.req;

import org.petmeet.http.db.member.MemberEntity;
import org.petmeet.http.db.member.Role;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberDTO {
	private Role role;

	public static MemberDTO from(MemberEntity member) {
		return MemberDTO.builder()
			.role(member.getRole())
			.build();
	}
}
