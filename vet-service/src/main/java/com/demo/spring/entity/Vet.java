package com.demo.spring.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Entity
@Table(name="vets")
public class Vet {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int vetId;
	@NotEmpty(message = "Name is required")
	private String name;
	@NotEmpty(message = "Address is required")
	private String address;
	@Pattern(regexp = "^\\d{10}$", message = "Enter 10 digit number")
	private String telephone;
	@NotEmpty(message = "Speciality is required")
	private String specialty;

	public int getVetId() {
		return vetId;
	}

	public void setVetId(int vetId) {
		this.vetId = vetId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	
	public String getSpecialty() {
		return specialty;
	}

	public void setSpecialty(String specialty) {
		this.specialty = specialty;
	}

	public Vet() {

	}

	public Vet(int vetId, String name, String address, String telephone, String specialty) {
		this.vetId = vetId;
		this.name = name;
		this.address = address;
		this.telephone = telephone;
		this.specialty = specialty;
	}
	public Vet( String name, String address, String telephone, String specialty) {
		this.name = name;
		this.address = address;
		this.telephone = telephone;
		this.specialty = specialty;
	}
	

	
}
