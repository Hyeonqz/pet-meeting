package org.petmeet.http.api.domain.jwt.service;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtUtils {

	private final SecretKey secretKey;

	public JwtUtils (@Value("${spring.jwt.secret.key}") String secretKey) {
		this.secretKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
	}

	public String getUsername(String token) {
		return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("username", String.class);
	}

	public String getRole(String token) {
		return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
	}

	public String getCategory(String token) {
		return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("category", String.class);
	}

	public Boolean isTokenExpired(String token) {
		return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
	}

	public Cookie createCookie (String key, String value) {
		Cookie cookie = new Cookie(key,value);
		cookie.setMaxAge(24*60*60);
		// cookie.setSecure(true); // https 설정 할 경우 넣어줘야
		// cookie.setPath("/"); cookie 경로 설정
		cookie.setHttpOnly(true);
		return cookie;
	}

	public String createToken(String category, String username, String role, Long expiredAt) {
		long now = System.currentTimeMillis();
		long expirationTime = now + expiredAt * 2000;

		return Jwts.builder()
			.claim("category", category) // access 인지, refresh 인지 체크
			.claim("username", username)
			.claim("role", role)
			.issuedAt(new Date(now))
			.expiration(new Date(expirationTime))
			.signWith(secretKey)
			.compact();
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(secretKey).build().parseSignedClaims(token);
			//log.info("JWT Request: {}", Jwts.parser().setSigningKey(secretKey).build().parseSignedClaims(token));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}
}
