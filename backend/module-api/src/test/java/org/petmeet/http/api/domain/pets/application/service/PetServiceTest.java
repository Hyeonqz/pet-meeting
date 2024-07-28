package org.petmeet.http.api.domain.pets.application.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.petmeet.http.api.domain.pets.application.dto.dtos.PetDTO;
import org.petmeet.http.db.member.MemberEntity;
import org.petmeet.http.db.member.MemberRepository;
import org.petmeet.http.db.pets.Pet;
import org.petmeet.http.db.pets.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PetServiceTest {

	@Autowired
	private PetRepository petRepository;

	@Autowired
	private PetService petService;

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

	@Test
	@DisplayName("Pet DB 에 데이터 넣기")
	void Insert_Test() {

	    // given
		Pet pet = new Pet();
		MemberEntity member = MemberEntity.builder()
			.id(5L)
			.build();

		for (int i = 1; i <= 10000 ; i++) {
			petRepository.save(Pet.builder()
				.age(i)
					.name("밍"+i)
					.breed("말티즈"+i)
					.peopleAge(i*7)
					.birth("1999-05-10")
					.gender("암컷")
					.neutered(false)
					.member(member)
					.createdAt(LocalDateTime.now())
				.build()
			);
		}
	}

	@Test
	@DisplayName("keyword 에 따른 통합검색")
	public void testSearchPets() {
		// given
		List<PetDTO.PetListDTO> results = petService.getSearchResult("말티즈");
		// when
		// then
		assertFalse(results.isEmpty());
		for (PetDTO.PetListDTO pet : results) {
			System.out.print(
				pet.getAge() + ", " + pet.getPeopleAge() + ", " + pet.getGender() + ", " +
				pet.getName() + ", " + pet.getCreatedAt() + ", " + ", " + pet.getBirth() + ", " +
					pet.getBreed() + ", " + pet.isNeutered()
			);
		}
	}

}