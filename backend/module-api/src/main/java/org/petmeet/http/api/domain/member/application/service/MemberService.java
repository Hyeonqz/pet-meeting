package org.petmeet.http.api.domain.member.application.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.petmeet.http.api.domain.member.application.dto.req.MemberUpdateRequest;
import org.petmeet.http.api.domain.member.application.dto.res.MemberUpdateResponse;
import org.petmeet.http.api.domain.pets.application.dto.dtos.PetDTO;
import org.petmeet.http.db.login.LoginEntity;
import org.petmeet.http.db.member.MemberEntity;
import org.petmeet.http.db.login.LoginRepository;
import org.petmeet.http.db.member.MemberRepository;
import org.petmeet.http.api.domain.login.application.dto.LoginDTO;
import org.petmeet.http.api.domain.member.application.dto.req.MemberRegisterRequest;
import org.petmeet.http.api.domain.member.application.dto.res.MemberRegisterResponse;
import org.petmeet.http.db.pets.Pet;
import org.petmeet.http.db.pets.PetRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberService {
	private final MemberRepository memberRepository;
	private final LoginRepository loginRepository;
	private final PetRepository petRepository;
	private final BCryptPasswordEncoder passwordEncoder;

	@Transactional
	public MemberRegisterResponse register(MemberRegisterRequest request) {
		return getMemberRegisterResponse(request);
	}

	@Transactional
	public MemberUpdateResponse memberUpdate (MemberUpdateRequest request) {
		return getMemberUpdateResponse(request);
	}


	@Transactional
	public void memberDelete (Long id) {
		deleteMember(id);
	}

	public List<PetDTO.PetListDTO> getPet(Long memberId) {
		return getPetList(memberId);
	}

	// memberId 에 해당하는 Pet 들의 다 조회를 할거임.
	// 몇 마리를 등록했는지 List 로 볼 수 있어야 함.
	// 조회될 Pet DTO 를 만들어야 한다.
	public List<PetDTO.PetListDTO> getPetList(Long memberId) {
		// 로그인된 username 에 해당하는 Pet 들
		List<Pet> petList = petRepository.findAllByMemberId(memberId);

		return petList.stream()
			.map(pet -> new PetDTO.PetListDTO(
				pet.getName(),
				pet.getGender(),
				pet.getAge(),
				pet.isNeutered(),
				pet.getBirth(),
				pet.getBreed(),
				pet.getPeopleAge(),
				pet.getCreatedAt()))
			.collect(Collectors.toList());
		}

	private void deleteMember (Long id) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		LoginEntity login = loginRepository.findByUsername(username)
			.orElseThrow(() -> new RuntimeException("User not found"));

		MemberEntity referenceById = memberRepository.getReferenceById(login.getMember().getId());
		log.info("Deleting member with id {}", id);

		var commonId = login.getMember().getId();
		log.info("common Id : {}", commonId);

		if(commonId == referenceById.getId())
			throw new RuntimeException("ID 값이 다른데???");

		petRepository.deleteByMember(referenceById);
		loginRepository.deleteById(commonId);
		memberRepository.deleteById(commonId);
	}

	private MemberUpdateResponse getMemberUpdateResponse(MemberUpdateRequest request) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		LoginEntity login = loginRepository.findByUsername(username)
			.orElseThrow(() -> new RuntimeException("User not found"));

/*		MemberEntity member = memberRepository.findById(login.getMember().getId())
			.orElseThrow(() -> new RuntimeException("Member not found"));*/

		MemberEntity referenceById = memberRepository.getReferenceById(login.getMember().getId());
		log.info("Login ID : {}", login.getId());
		log.info("Member Id : {}", login.getMember().getId());

		// 파라미터 넣어야할듯 생성자 방식으로?
		referenceById.onUpdate(
			request.getName(),
			request.getAddress(),
			request.getEmail(),
			request.getPhoneNumber(),
			request.getGender()
			);

		memberRepository.save(referenceById);

		return MemberUpdateResponse.builder()
			.name(referenceById.getName())
			.email(referenceById.getEmail())
			.phoneNumber(referenceById.getPhoneNumber())
			.gender(referenceById.getGender())
			.address(referenceById.getAddress())
			.updatedAt(LocalDateTime.now())
			.build();
	}

	private MemberRegisterResponse getMemberRegisterResponse (MemberRegisterRequest request) {
		if(request == null) {
			throw new RuntimeException("Member Request is Null");
		}

		LoginEntity login = LoginDTO.toEntity(request.getLogin());
		String encodedPassword = passwordEncoder.encode(request.getLogin().getPassword());
		String userId = request.getLogin().getUsername();

		Boolean isExist = loginRepository.existsByUsername(userId);

		if(isExist) {
			throw new RuntimeException("UserId already exists");
		}

		LoginEntity encryptedLogin = new LoginEntity(userId, encodedPassword);

		MemberEntity member = MemberRegisterRequest.toEntity(request);
		member.setRole();

		encryptedLogin.associateMember(member);

		memberRepository.save(member);
		loginRepository.save(encryptedLogin);

		return MemberRegisterResponse.builder()
			.name(member.getName())
			.email(member.getEmail())
			.phoneNumber(member.getPhoneNumber())
			.gender(member.getGender())
			.address(request.getAddress())
			.login(request.getLogin())
			.createdAt(LocalDateTime.now())
			.build();
	}


}
