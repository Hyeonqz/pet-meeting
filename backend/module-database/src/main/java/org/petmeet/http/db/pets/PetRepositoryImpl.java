package org.petmeet.http.db.pets;

import java.util.List;

import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public class PetRepositoryImpl implements PetRepositoryCustom{

	@PersistenceContext
	private EntityManager entityManager;

	private final JPAQueryFactory jpaQueryFactory;

	public PetRepositoryImpl (JPAQueryFactory jpaQueryFactory) {
		this.jpaQueryFactory = jpaQueryFactory;
	}

/*	@Override
	public List<Pet> searchByKeyword (String keyword) {
		return jpaQueryFactory
			.selectFrom()
			.where()
			.and()
			.fetch()
		return List.of();
	}*/

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
