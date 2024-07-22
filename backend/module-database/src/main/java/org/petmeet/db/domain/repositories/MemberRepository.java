package org.petmeet.db.domain.repositories;

import java.util.Optional;

import org.petmeet.db.domain.entities.member.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
	Optional<MemberEntity> findByLogin_Username(String username);
}
