package org.petmeet.http.api.domain.jwt.service;

import org.petmeet.http.api.domain.member.application.dto.req.MemberDTO;
import org.petmeet.http.db.login.LoginEntity;
import org.petmeet.http.db.login.LoginRepository;
import org.petmeet.http.db.member.MemberEntity;
import org.petmeet.http.db.member.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {
	private final MemberRepository memberRepository;
	private final LoginRepository loginRepository;

	@Transactional
	@Override
	public UserDetails loadUserByUsername (String username) throws UsernameNotFoundException {
		LoginEntity login = loginRepository.findByUsername(username)
			.orElseThrow( () -> new UsernameNotFoundException(username + "Not Found"));
		MemberEntity member = login.getMember();
		MemberDTO memberDTO = MemberDTO.from(member);

		return new CustomUserDetails(login, memberDTO);

	}

}
