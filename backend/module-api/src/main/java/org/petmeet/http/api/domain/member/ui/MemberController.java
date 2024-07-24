package org.petmeet.http.api.domain.member.ui;

import java.util.Map;

import org.petmeet.http.api.domain.login.application.dto.LoginDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/apis/member")
@RestController
public class MemberController {

	// 위 방식으로도 개발을 많이 함.
	@GetMapping
	public ResponseEntity<?> get1(HttpServletRequest request, @RequestBody LoginDTO loginDTO) {
		return ResponseEntity.ok(Map.of(
			"status","success"
			)
		);
	}

}
