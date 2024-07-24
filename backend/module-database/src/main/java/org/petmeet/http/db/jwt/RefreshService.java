package org.petmeet.http.db.jwt;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RefreshService {
	private final RedisTemplate redisTemplate;

	public void refresh() {
		ValueOperations<String, String> ops = redisTemplate.opsForValue();
		ops.set("refresh", "true");
	}
}
