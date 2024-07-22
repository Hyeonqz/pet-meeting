package org.petmeet.db.repositories;

import org.petmeet.db.entities.login.LoginEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginRepository extends JpaRepository<LoginEntity, Long> {
	Boolean existsByUsername(String username);
}
