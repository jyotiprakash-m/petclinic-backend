package com.demo.spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.demo.spring.entity.Visit;

@Repository
public interface VisitServiceVisitRepository extends JpaRepository<Visit, Integer> {
//	Find visits all by pet id
	public List<Visit> findAllByPetId(int petId);
	
//	List all visits for given pet id
	@Query(name="SELECT v FROM Visit p WHERE p.petId IN :ids")
	public List<Visit> searchByPetIdIn(@Param("ids") List<Integer> list);
	
//	List all visits by visitor name
	@Query("SELECT o FROM Visit o WHERE o.name LIKE %:name%")
	public List<Visit> searchByNameLike(@Param("name") String name);
}
