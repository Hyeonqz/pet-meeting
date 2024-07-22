package org.petmeet.api.domain.member.ui;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.petmeet.api.domain.member.applicatoin.service.MemberRegisterService;
import org.petmeet.db.entities.login.LoginEntity;
import org.petmeet.db.entities.member.Address;
import org.petmeet.db.entities.member.MemberEntity;
import org.petmeet.db.enums.Role;
import org.petmeet.db.repositories.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MemberRegisterControllerTest {
	private final MemberRegisterService memberRegisterService;
	private final MemberRepository memberRepository;

	MemberRegisterControllerTest (MemberRegisterService memberRegisterService, MemberRepository memberRepository) {
		this.memberRegisterService = memberRegisterService;
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