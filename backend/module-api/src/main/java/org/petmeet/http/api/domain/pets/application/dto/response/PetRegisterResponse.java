package org.petmeet.http.api.domain.pets.application.dto.response;

import java.time.LocalDateTime;

import org.hibernate.annotations.Comment;
import org.petmeet.http.db.member.MemberEntity;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class PetRegisterResponse {

	private String name;
	private String gender;
	private Integer age;
	private Boolean neutered;
	private String birth;
	private String breed;
	private Integer peopleAge;
	private LocalDateTime createdAt;

	public PetRegisterResponse () {
	}

	@Builder
	public PetRegisterResponse (String name, String gender, Integer age, Boolean neutered, String birth, String breed,
		Integer peopleAge, LocalDateTime createdAt) {
		this.name = name;
		this.gender = gender;
		this.age = age;
		this.neutered = neutered;
		this.birth = birth;
		this.breed = breed;
		this.peopleAge = peopleAge;
		this.createdAt = createdAt;
	}

}
