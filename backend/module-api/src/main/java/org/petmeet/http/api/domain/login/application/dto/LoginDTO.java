package org.petmeet.http.api.domain.login.application.dto;

import org.petmeet.http.db.login.LoginEntity;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class LoginDTO {

	@NotBlank
	private String username;
	@NotBlank
	private String password;

	public static LoginEntity toEntity(LoginDTO dto) {
		if(dto == null) {
			throw new RuntimeException("Login DTO is null");
		}
		return LoginEntity.builder()
			.username(dto.getUsername())
			.password(dto.getPassword())
			.build();
	}

}
