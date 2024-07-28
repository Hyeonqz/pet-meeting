package org.petmeet.http.db.pets;

import java.util.List;

public interface PetRepositoryCustom {
	List<Pet> searchByKeyword(String keyword);
}
