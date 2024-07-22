package org.petmeet.http.api.domain.member.application.service;

import java.time.LocalDateTime;

import org.petmeet.http.db.login.LoginEntity;
import org.petmeet.http.db.member.MemberEntity;
import org.petmeet.http.db.login.LoginRepository;
import org.petmeet.http.db.member.MemberRepository;
import org.petmeet.http.api.domain.login.application.dto.LoginDTO;
import org.petmeet.http.api.domain.member.application.dto.req.MemberRegisterRequest;
import org.petmeet.http.api.domain.member.application.dto.res.MemberRegisterResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberRegisterService {
	private final MemberRepository memberRepository;
	private final LoginRepository loginRepository;
	private final BCryptPasswordEncoder passwordEncoder;

	@Transactional
	public MemberRegisterResponse register(MemberRegisterRequest request) {
		if(request == null) {
			throw new RuntimeException("Null");
		}

		LoginEntity login = LoginDTO.toEntity(request.getLogin());
		String encodedPassword = passwordEncoder.encode(request.getLogin().getPassword());
		String userId = request.getLogin().getUsername();

		Boolean isExist = loginRepository.existsByUsername(userId);

		if(isExist) {
			throw new RuntimeException("UserId already exists");
		}

		LoginEntity encryptedLogin = new LoginEntity(userId, encodedPassword);

		MemberEntity member = MemberRegisterRequest.toEntity(request);
		member.setRole();

		encryptedLogin.associateMember(member);

		memberRepository.save(member);
		loginRepository.save(encryptedLogin);

		return MemberRegisterResponse.builder()
			.name(member.getName())
			.email(member.getEmail())
			.phoneNumber(member.getPhoneNumber())
			.gender(member.getGender())
			.address(request.getAddress())
			.login(request.getLogin())
			.createdAt(LocalDateTime.now())
			.build();
	}

}
