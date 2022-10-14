package com.demo.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demo.spring.entity.Specialty;

@Repository
public interface VetServiceSpecialityRepository extends JpaRepository<Specialty, Integer> {

}
