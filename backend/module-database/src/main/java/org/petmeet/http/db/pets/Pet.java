package org.petmeet.http.db.pets;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import org.hibernate.annotations.Comment;
import org.petmeet.http.db.member.MemberEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
@Entity
public class Pet implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="pet_id")
	private Long id;

	@Column(nullable = false, length = 10)
	private String name;

	@Column(nullable = false, length = 5)
	private String gender;

	@Column(nullable = false, length = 5)
	private Integer age;

	@Comment("중성화 수술 여부")
	private boolean neutered;

	@Column(length = 10)
	private String birth;

	@Comment("견종")
	@Column(nullable = false, length = 20)
	private String breed;

	private Integer peopleAge;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="member_id")
	private MemberEntity member;

	private LocalDateTime createdAt;

	public Pet () {
	}

	@PrePersist
	public void onCreateAt() {
		this.createdAt = LocalDateTime.now();
		calculatePeopleAge();
	}

	private void calculatePeopleAge() {
		this.peopleAge = this.age * 7;
	}

	public Integer updatePeopleAge() {
		return this.peopleAge = this.age * 7;
	}

	public void onUpdate (String name, Integer age, String gender, String breed, boolean neutered, String birth) {
		this.name = name;
		this.age =age;
		this.gender = gender;
		this.breed = breed;
		this.neutered = neutered;
		this.birth =birth;
	}
}
