package org.petmeet.api.domain.jwt.service;

import java.util.ArrayList;
import java.util.Collection;

import org.petmeet.db.domain.entities.member.MemberEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {
	private final MemberEntity member;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities () {
		Collection<GrantedAuthority> collection = new ArrayList<>();

		collection.add(new GrantedAuthority() {
			@Override
			public String getAuthority () {
				return member.getRole().getCode();
			}
		});

		return collection;
	}

	@Override
	public String getPassword () {
		return member.getLogin().getPassword();
	}

	@Override
	public String getUsername () {
		return member.getLogin().getUsername();
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
