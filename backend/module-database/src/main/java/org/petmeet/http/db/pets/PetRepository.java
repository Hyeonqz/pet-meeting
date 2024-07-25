package org.petmeet.http.db.pets;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {
	Optional<Pet> findByName(String name);

}
