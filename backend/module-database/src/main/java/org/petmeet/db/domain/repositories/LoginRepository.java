package org.petmeet.db.domain.repositories;

import java.util.Optional;

import org.petmeet.db.domain.entities.login.LoginEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginRepository extends JpaRepository<LoginEntity, Long> {
	Boolean existsByUsername(String username);
	Optional<LoginEntity> findByUsername(String username);
}
