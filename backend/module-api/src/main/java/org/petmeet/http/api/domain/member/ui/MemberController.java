package org.petmeet.http.api.domain.member.ui;

import java.util.List;
import java.util.Map;

import org.petmeet.http.api.domain.member.application.dto.req.MemberRegisterRequest;
import org.petmeet.http.api.domain.member.application.dto.req.MemberUpdateRequest;
import org.petmeet.http.api.domain.member.application.dto.res.MemberRegisterResponse;
import org.petmeet.http.api.domain.member.application.dto.res.MemberUpdateResponse;
import org.petmeet.http.api.domain.member.application.service.MemberService;
import org.petmeet.http.api.domain.pets.application.dto.dtos.PetDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api/member")
@RequiredArgsConstructor
@RestController
public class MemberController {
	private final MemberService memberService;

	@PostMapping("/register")
	public ResponseEntity<MemberRegisterResponse> register(@RequestBody MemberRegisterRequest request) {
		MemberRegisterResponse response = memberService.register(request);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/update")
	public ResponseEntity<MemberUpdateResponse> update(@RequestBody MemberUpdateRequest request) {
		MemberUpdateResponse memberUpdateResponse = memberService.memberUpdate(request);
		return ResponseEntity.ok(memberUpdateResponse);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable  Long id) {
		memberService.memberDelete(id);
		return ResponseEntity.ok(Map.of("Status", "Success"));
	}

	@GetMapping("/petList/{id}")
	public ResponseEntity<?> getPetsList(@PathVariable Long id) {
		List<PetDTO.PetListDTO> petList = memberService.getPetList(id);
		return ResponseEntity.ok(petList);
	}

}
