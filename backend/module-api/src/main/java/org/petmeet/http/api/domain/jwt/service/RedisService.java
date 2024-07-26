package org.petmeet.http.api.domain.jwt.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RedisService {
	private final RedisTemplate<String,Object> redisTemplate;

	// 복잡한 Redis 작업 및 성능 최적화가 필요할 때 주로 사용, 간단한 작업일 경우는 repository 방식 사용
	public void saveRefreshToken(String refreshToken, String username) {
		redisTemplate.opsForValue().set(refreshToken,username);
	}

	public boolean existsByRefreshToken(String refreshToken) {
		return Boolean.TRUE.equals(redisTemplate.hasKey(refreshToken));
	}

	public Object getRefreshToken(String refreshToken) {
		return redisTemplate.opsForValue().get(refreshToken);
	}

	public void deleteRefreshToken(String refreshToken) {
		redisTemplate.delete(refreshToken);
	}
}
