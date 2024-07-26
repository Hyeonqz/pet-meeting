package org.petmeet.api.domain.member.ui;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.petmeet.http.api.domain.member.application.service.MemberService;
import org.petmeet.http.db.login.LoginEntity;
import org.petmeet.http.db.member.Address;
import org.petmeet.http.db.member.MemberEntity;
import org.petmeet.http.db.member.Role;
import org.petmeet.http.db.member.MemberRepository;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MemberControllerTest {
	private final MemberService memberService;
	private final MemberRepository memberRepository;

	MemberControllerTest (MemberService memberService, MemberRepository memberRepository) {
		this.memberService = memberService;
		this.memberRepository = memberRepository;
	}

	@Test
	@DisplayName("회원가입 테스트")
	void MemberRegisterControllerTest () {
		// given
		MemberEntity member = new MemberEntity();
		LoginEntity login = LoginEntity.builder()
			.username("jin")
			.password("1234")
			.lastLoginTime(LocalDateTime.now())
			.build();

		Address address = Address.builder()
			.address("서울시청")
			.zipCode("13213")
			.detail_Address("언주로213길")
			.build();

		// when
		memberRepository.save(MemberEntity.builder()
			.name("jin")
			.email("wlsgusrb78@naver.com")
			.address(address)
			.phoneNumber("123412312")
			.gender("남자")
			.role(Role.USER)
			.login(login)
			.build()
		);

		// then
	}

}