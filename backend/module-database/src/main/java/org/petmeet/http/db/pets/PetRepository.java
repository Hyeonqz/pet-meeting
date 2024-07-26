package org.petmeet.http.db.pets;

import java.util.List;
import java.util.Optional;

import org.petmeet.http.db.member.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PetRepository extends JpaRepository<Pet, Long> {
	Optional<Pet> findByName(String name);
	Optional<Pet> findByMemberId(Long id);
	void deleteByMember(MemberEntity member);

	@Query("SELECT p FROM Pet p WHERE p.member.id = :memberId")
	List<Pet> findAllByMemberId(@Param("memberId") Long memberId);
}
