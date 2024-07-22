package org.petmeet.http.api.domain.member.ui;

import org.petmeet.http.api.domain.member.application.dto.req.MemberRegisterRequest;
import org.petmeet.http.api.domain.member.application.dto.res.MemberRegisterResponse;
import org.petmeet.http.api.domain.member.application.service.MemberRegisterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api/v1")
@RequiredArgsConstructor
@RestController
public class MemberRegisterController {
	private final MemberRegisterService memberRegisterService;

	@PostMapping("/register")
	public ResponseEntity<MemberRegisterResponse> register(@RequestBody MemberRegisterRequest request) {
		MemberRegisterResponse response = memberRegisterService.register(request);
		return ResponseEntity.ok(response);
	}
}
