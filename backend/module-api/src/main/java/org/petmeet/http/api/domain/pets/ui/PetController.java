package org.petmeet.http.api.domain.pets.ui;

import java.util.List;
import java.util.Map;

import org.petmeet.http.api.domain.pets.application.dto.dtos.PetDTO;
import org.petmeet.http.api.domain.pets.application.dto.request.PetRequestDTO;
import org.petmeet.http.api.domain.pets.application.dto.response.PetRegisterResponse;
import org.petmeet.http.api.domain.pets.application.service.PetService;
import org.petmeet.http.db.pets.Pet;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/pet")
@RestController
public class PetController {
	private final PetService petService;

	@PostMapping("/auth2register")
	public ResponseEntity<?> secureRegister(
		@RequestHeader HttpHeaders httpHeaders,
		@RequestBody PetRequestDTO.PetRegisterRequest request) {

		if(httpHeaders.containsKey(HttpHeaders.AUTHORIZATION)) {
			PetRegisterResponse petRegisterResponse = petService.registerPet(request);
			String auth = httpHeaders.getFirst(HttpHeaders.AUTHORIZATION);
			log.info("Auth Token : {}", auth );

			if(auth.startsWith("Bearer ")) {
				return new ResponseEntity<>(petRegisterResponse, HttpStatus.OK);
			}
		}
		return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
	}

	@PostMapping("/authRegister")
	public ResponseEntity<PetRegisterResponse> authRegister(@RequestBody PetRequestDTO.PetRegisterRequest request, HttpServletRequest httpServletRequest) {
		String bearerToken = httpServletRequest.getHeader("Authorization");
		log.info("Auth Token : {}", bearerToken);

		if(bearerToken != null && bearerToken.startsWith("Bearer ")) {
			petService.registerPet(request);
		}
		return ResponseEntity.ok().build();

	}

	//펫 정보 수정
	@PostMapping("/update/{id}")
	public ResponseEntity<?> updatePet(@RequestBody PetRequestDTO.PetRegisterRequest request, @PathVariable Long id) {
		PetRegisterResponse petRegisterResponse = petService.updatePet(request, id);
		return ResponseEntity.ok(petRegisterResponse);
	}

	// 펫 삭제
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deletePet(@PathVariable Long id) {
		petService.deletePet(id);
		return ResponseEntity.ok(Map.of(
			"status","success"
		));
	}

	@GetMapping("/search/{keyword}")
	public ResponseEntity<?> searchPets(@PathVariable String keyword) {
		List<PetDTO.PetListDTO> searchResult = petService.getSearchResult(keyword);
		return ResponseEntity.ok(searchResult);
	}



}
