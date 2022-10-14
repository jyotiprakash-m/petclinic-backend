package com.demo.spring.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "owners")
public class Owner {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int ownerId;
	@NotEmpty(message = "Name is required")
	private String name;
	@NotEmpty(message = "Address is required")
	private String address;
	private String city;
	@Pattern(regexp = "^\\d{10}$", message = "Enter 10 digit number")
	private String telephone;
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "owner_id", referencedColumnName = "id")
	private List<Pet> pets;

	public int getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
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

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	
	public List<Pet> getPets() {
		return pets;
	}
	
	public void setPets(List<Pet> pets) {
		this.pets = pets;
	}

	public Owner() {

	}

	public Owner(int ownerId, String name, String address, String city, String telephone, List<Pet> pets) {
		super();
		this.ownerId = ownerId;
		this.name = name;
		this.address = address;
		this.city = city;
		this.telephone = telephone;
		this.pets = pets;
	}
	public Owner(String name, String address, String city, String telephone, List<Pet> pets) {
		this.name = name;
		this.address = address;
		this.city = city;
		this.telephone = telephone;
		this.pets = pets;
	}
	


	

}
