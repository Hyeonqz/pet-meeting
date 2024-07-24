package org.petmeet.http.db.jwt;

import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
@RedisHash(value = "refresh_token", timeToLive = 14440L)
public class RefreshToken {

	@Id
	private String refreshToken;

	private String username;

	private String expiredAt;
	private String category;
	private LocalDateTime createdAt;

	@TimeToLive
	private Long timeToLive;

}
