package org.petmeet.http.api.domain.pets.application.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.petmeet.http.db.pets.Pet;
import org.petmeet.http.db.pets.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PetServiceTest {

	@Autowired
	private final PetRepository petRepository;

	public PetServiceTest (PetRepository petRepository) {
		this.petRepository = petRepository;
	}

	@Test
	@DisplayName("로그인 후 펫 등록 테스트")
	void PetServiceTest() {
	    // given
		Pet pet = new Pet();

	    // when
		petRepository.save(Pet.builder()
			.age(14)
			.birth("1999-05-10")
			.name("밍키")
			.gender("암컷")
			.neutered(false)
			.build()
		);

	    // then
	}

}