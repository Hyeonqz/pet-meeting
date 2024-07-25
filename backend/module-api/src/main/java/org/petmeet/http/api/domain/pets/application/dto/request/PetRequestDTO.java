package org.petmeet.http.api.domain.pets.application.dto.request;

import org.petmeet.http.db.member.MemberEntity;
import org.petmeet.http.db.pets.Pet;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;

public class PetRequestDTO {

	@Getter
	public static class PetRegisterRequest {
		private String name;
		private Integer age;
		private String gender;
		private String breed;
		private boolean neutered;
		private String birth;

		@JsonIgnore
		private MemberEntity member;

		public PetRegisterRequest () {
		}

		public PetRegisterRequest (String name, Integer age, String gender, String breed, boolean neutered,
			String birth,
			MemberEntity member) {
			this.name = name;
			this.age = age;
			this.gender = gender;
			this.breed = breed;
			this.neutered = neutered;
			this.birth = birth;
			this.member = member;
		}

		public static Pet toEntity(PetRegisterRequest request) {
			return Pet.builder()
				.birth(request.getBirth())
				.gender(request.getGender())
				.name(request.getName())
				.breed(request.getBreed())
				.neutered(request.isNeutered())
				.age(request.getAge())
				.birth(request.getBirth())
				.member(request.getMember())
				.build();
		}
	}





}
