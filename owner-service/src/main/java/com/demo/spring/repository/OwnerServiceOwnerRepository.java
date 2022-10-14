package com.demo.spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.demo.spring.entity.Owner;

@Repository
public interface OwnerServiceOwnerRepository extends JpaRepository<Owner, Integer> {
	
	@Query("SELECT o FROM Owner o WHERE o.name LIKE %:name%")
	public List<Owner> searchByNameLike(@Param("name") String name);

}
