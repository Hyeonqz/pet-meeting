package org.petmeet.http.api.domain.jwt.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.petmeet.http.api.domain.member.application.dto.req.MemberDTO;
import org.petmeet.http.db.login.LoginEntity;
import org.petmeet.http.db.member.MemberEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {
	private final LoginEntity login;
	private final MemberDTO memberDTO;


	@Override
	public Collection<? extends GrantedAuthority> getAuthorities () {
		Collection<GrantedAuthority> collection = new ArrayList<>();

		return Collections.singletonList(new SimpleGrantedAuthority("ROLE_"+"USER"));
/*		collection.add(new GrantedAuthority() {
			@Override
			public String getAuthority () {
				return memberDTO.getRole().getCode();
			}
		});*/

		//return collection;
	}

	@Override
	public String getPassword () {
		return login.getPassword();
	}

	@Override
	public String getUsername () {
		return login.getUsername();
	}

	@Override
	public boolean isAccountNonExpired () {
		return UserDetails.super.isAccountNonExpired();
	}

	@Override
	public boolean isAccountNonLocked () {
		return UserDetails.super.isAccountNonLocked();
	}

	@Override
	public boolean isCredentialsNonExpired () {
		return UserDetails.super.isCredentialsNonExpired();
	}

	@Override
	public boolean isEnabled () {
		return UserDetails.super.isEnabled();
	}

}
