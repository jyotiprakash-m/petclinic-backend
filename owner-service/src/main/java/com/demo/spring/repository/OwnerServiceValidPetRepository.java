package com.demo.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.spring.entity.ValidPet;

public interface OwnerServiceValidPetRepository extends JpaRepository<ValidPet, Integer> {

}
