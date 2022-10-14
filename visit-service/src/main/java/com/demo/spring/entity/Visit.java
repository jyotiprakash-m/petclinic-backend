package com.demo.spring.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "visits")
public class Visit {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int visitorId;
	@NotEmpty(message = "Name is required")
	private String name;
	@NotEmpty(message = "Address is required")
	private String address;
	private String city;
//	 @Size(min = 10, max = 10,message = "Enter 10 digit numeber")
	@Pattern(regexp = "^\\d{10}$", message = "Enter 10 digit number")
	private String telephone;
	@NotNull(message = "Pet id is required")
	private int petId;

	public int getVisitorId() {
		return visitorId;
	}

	public void setVisitorId(int visitorId) {
		this.visitorId = visitorId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPetId() {
		return petId;
	}

	public void setPetId(int petId) {
		this.petId = petId;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Visit() {

	}

	public Visit(int visitorId, String name, String address, String city, String telephone, int petId) {
		this.visitorId = visitorId;
		this.name = name;
		this.address = address;
		this.city = city;
		this.telephone = telephone;
		this.petId = petId;
	}
	public Visit(String name, String address, String city, String telephone, int petId) {
		this.name = name;
		this.address = address;
		this.city = city;
		this.telephone = telephone;
		this.petId = petId;
	}

}
