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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
		Pet pet = PetRequestDTO.PetRegisterRequest.toEntity(request);
		pet.getPeopleAge();

		// 방법1
		// 현재 로그인된 발급 된 토큰에서 Username 값만 뽑아 온다.
		// 쿼리를 만들어 username 에 해당하는 Member 객체를 가져온다.
		// Member 에서 Id 값을 뽑아서 Pet 등록할 때 같이 등록 시킨다.
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		log.info("UserId : {}" , username); // ID 가져옴

		LoginEntity login = loginRepository.findByUsername(username).get();
		if(login == null) {
			throw new RuntimeException("Login not found");
		}

		MemberEntity member = memberRepository.getReferenceById(login.getId());

		petRepository.save(Pet.builder()
			.birth(request.getBirth())
			.neutered(request.isNeutered())
			.gender(pet.getGender())
			.name(request.getName())
			.breed(request.getBreed())
			.birth(request.getBirth())
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
