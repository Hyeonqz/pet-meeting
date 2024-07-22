package org.petmeet.http.api.domain.jwt.service;

import org.petmeet.http.db.member.MemberEntity;
import org.petmeet.http.db.member.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {
	private final MemberRepository memberRepository;

	@Override
	public UserDetails loadUserByUsername (String username) throws UsernameNotFoundException {
		log.info("[loadUserByUsername] :  [{}]", username);

		MemberEntity member = memberRepository.findByLogin_Username(username)
			.orElseThrow(() -> new UsernameNotFoundException("UserName not found"));
		log.info("User found: {}", member);

		return new CustomUserDetails(member);

	}

}
