package org.petmeet.http.api.domain.pets.application.dto.dtos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PetDTO {

	@AllArgsConstructor
	@NoArgsConstructor
	@Getter
	public static class PetListDTO {
		private String name;
		private String gender;
		private Integer age;
		private boolean neutered;
		private String birth;
		private String breed;
		private Integer peopleAge;
		private LocalDateTime createdAt;

	}

}
