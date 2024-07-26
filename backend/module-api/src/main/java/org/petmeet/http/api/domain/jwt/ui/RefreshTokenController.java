package org.petmeet.http.api.domain.jwt.ui;

import org.petmeet.http.api.domain.jwt.service.JwtService;
import org.petmeet.http.db.jwt.RefreshToken;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequestMapping("/api/v1")
@RequiredArgsConstructor
@RestController
public class RefreshTokenController {
	private final JwtService jwtService;

	@PostMapping("/refresh")
	public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
		ResponseEntity<?> responseEntity = jwtService.issueRefreshToken(request, response);
		return ResponseEntity.ok(responseEntity);
	}

}
