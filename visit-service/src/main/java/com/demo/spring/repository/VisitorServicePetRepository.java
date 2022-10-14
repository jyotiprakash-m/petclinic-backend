package com.demo.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demo.spring.entity.Pet;

@Repository
public interface VisitorServicePetRepository extends JpaRepository<Pet, Integer> {

}
