package org.petmeet.http.db.pets;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public class PetRepositoryImpl implements PetRepositoryCustom{

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<Pet> searchByKeyword(String keyword) {
		String jpql = "SELECT p FROM Pet p WHERE p.name LIKE :keyword " +
			"OR p.breed LIKE :keyword " +
			"OR p.gender LIKE :keyword " +
			"OR p.birth LIKE :keyword";
		return entityManager.createQuery(jpql, Pet.class)
			.setParameter("keyword", "%" + keyword + "%")
			.getResultList();
	}

}
