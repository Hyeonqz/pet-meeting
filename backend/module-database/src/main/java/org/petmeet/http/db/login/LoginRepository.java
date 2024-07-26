package org.petmeet.http.db.login;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface LoginRepository extends JpaRepository<LoginEntity, Long> {
	Boolean existsByUsername(String username);
	Optional<LoginEntity> findByUsername(String username);

}
