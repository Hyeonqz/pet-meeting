package org.petmeet.http.api.domain.pets.application.service;

import java.util.Optional;

import org.petmeet.http.api.domain.jwt.service.CustomUserDetails;
import org.petmeet.http.api.domain.pets.application.dto.request.PetRequestDTO;
import org.petmeet.http.api.domain.pets.application.dto.response.PetRegisterResponse;
import org.petmeet.http.common.error.ErrorCode;
import org.petmeet.http.common.exception.PetMeetException;
import org.petmeet.http.db.login.LoginEntity;
import org.petmeet.http.db.login.LoginRepository;
import org.petmeet.http.db.member.MemberEntity;
import org.petmeet.http.db.member.MemberRepository;
import org.petmeet.http.db.pets.Pet;
import org.petmeet.http.db.pets.PetRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class PetService {
	private final PetRepository petRepository;
	private final LoginRepository loginRepository;
	private final MemberRepository memberRepository;

	@Transactional
	public PetRegisterResponse registerPet(PetRequestDTO.PetRegisterRequest request) {
		return getPetRegisterResponse(request);
	}

	@Transactional
	public PetRegisterResponse updatePet(PetRequestDTO.PetRegisterRequest request, Long id) {
		return getPetUpdateResponse(request,id);
	}

	@Transactional
	public void deletePet(Long id) {
		petRepository.deleteById(id);
	}

	private PetRegisterResponse getPetUpdateResponse (PetRequestDTO.PetRegisterRequest request, Long id) {
		if(id == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다");

		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		LoginEntity login = loginRepository.findByUsername(username)
			.orElseThrow(() -> new RuntimeException("User not found"));

		log.info("Member Id : {}" , login.getMember().getId());

		// pet 이긴 pet 인데 Pet 에 있는 member_id 가 member 랑 같은 Id 를 찾아야 한다.
		// 한 User 에게 등록된 pet 이 여러개 이기 때문에 pet_id 를 받아야 한다.
		// 지금은 pet_id 를 찾는데 member_id 를 찾아야 한다.

		Pet pet = petRepository.getReferenceById(id);
		if(pet == null)
			throw new RuntimeException("Pet Id 랑 Matching 되는 Pet 이 없다");

		pet.updatePeopleAge();

		pet.onUpdate(
			request.getName(),
			request.getAge(),
			request.getGender(),
			request.getBreed(),
			request.isNeutered(),
			request.getBirth()
		);

		petRepository.save(pet);

		return PetRegisterResponse.builder()
			.neutered(request.isNeutered())
			.gender(pet.getGender())
			.name(request.getName())
			.breed(request.getBreed())
			.birth(request.getBirth())
			.age(request.getAge())
			.peopleAge(pet.updatePeopleAge())
			.build();
	}

	private PetRegisterResponse getPetRegisterResponse (PetRequestDTO.PetRegisterRequest request) {
		Pet pet = PetRequestDTO.PetRegisterRequest.toEntity(request);
		pet.getPeopleAge();

		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		log.info("UserId : {}" , username); // ID 가져옴

		LoginEntity login = loginRepository.findByUsername(username).get();
		if(login == null) {
			throw new RuntimeException("Login not found");
		}

		MemberEntity member = memberRepository.getReferenceById(login.getMember().getId());

		petRepository.save(Pet.builder()
			.birth(request.getBirth())
			.neutered(request.isNeutered())
			.gender(pet.getGender())
			.name(request.getName())
			.breed(request.getBreed())
			.age(request.getAge())
			.member(member)
			.build()
		);

		return PetRegisterResponse.builder()
			.age(pet.getAge())
			.birth(pet.getBirth())
			.name(pet.getName())
			.breed(pet.getBreed())
			.neutered(pet.isNeutered())
			.createdAt(pet.getCreatedAt())
			.peopleAge(pet.getPeopleAge())
			.gender(pet.getGender())
			.build();
	}

}
