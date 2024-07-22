package org.petmeet.http.db.jwt;

import java.util.Date;

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
	private Date expiredAt;

	@TimeToLive
	private Long timeToLive;

	public RefreshToken (String refreshToken, String username, Date expiredAt) {
		this.refreshToken = refreshToken;
		this.username = username;
		this.expiredAt = expiredAt;
	}

}
