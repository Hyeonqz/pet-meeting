package org.petmeet.http.db.jwt;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface RefreshRepository extends CrudRepository<RefreshToken,String> {
	Boolean existsByRefreshToken(String refreshToken);

	@Transactional
	void deleteByRefreshToken(String refreshToken);
}
