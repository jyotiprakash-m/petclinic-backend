package com.demo.spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.demo.spring.entity.Vet;

@Repository
public interface VetServiceVetRepository extends JpaRepository<Vet, Integer> {

//	Search a vet based on specialty (Get) - Tough 
	public List<Vet> findAllBySpecialty(String specialty);
	
//	List all vet for a specialty (Get) - Tough
	@Query("SELECT v FROM Vet v WHERE v.specialty LIKE %:specialty%")
	List<Vet> searchBySpecialtyLike(@Param("specialty") String specialty);
	
//	List all vet by name (Get) - Tough
	@Query("SELECT v FROM Vet v WHERE v.name LIKE %:name%")
	List<Vet> searchByNameLike(@Param("name") String name);
}
