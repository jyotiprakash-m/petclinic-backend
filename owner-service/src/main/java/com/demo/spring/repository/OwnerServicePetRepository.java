package com.demo.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.spring.entity.Pet;

public interface OwnerServicePetRepository extends JpaRepository<Pet, Integer> {

}
