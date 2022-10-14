package com.demo.spring.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name="pets")
public class Pet {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int petId;
	@NotEmpty(message = "Pet name is required")
	private String petName;
	@NotEmpty(message = "Pet type is required")
	private String type;
	public int getPetId() {
		return petId;
	}
	public void setPetId(int petId) {
		this.petId = petId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String getPetName() {
		return petName;
	}
	public void setPetName(String petName) {
		this.petName = petName;
	}
	public Pet() {
		
	}
	public Pet(int petId, String petName, String type) {
		super();
		this.petId = petId;
		this.petName = petName;
		this.type = type;
	}
	public Pet( String petName, String type) {
		
		this.petName = petName;
		this.type = type;
	}
	
	
	

}
