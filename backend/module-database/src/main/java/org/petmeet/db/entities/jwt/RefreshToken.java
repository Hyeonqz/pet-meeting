package org.petmeet.db.entities.jwt;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import lombok.Getter;

@Getter
@RedisHash(value = "refresh_token", timeToLive = 14440L)
public class RefreshToken {

	@Id
	private String refreshToken;
	private String username;
	private String expiredAt;

	@TimeToLive
	private Long timeToLive;
}
