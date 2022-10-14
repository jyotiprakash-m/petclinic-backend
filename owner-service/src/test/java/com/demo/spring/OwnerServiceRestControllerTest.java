package com.demo.spring;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.demo.spring.entity.Owner;
import com.demo.spring.entity.Pet;
import com.demo.spring.repository.OwnerServiceOwnerRepository;
import com.demo.spring.repository.OwnerServicePetRepository;
import com.demo.spring.rest.OwnerServiceRestController;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class OwnerServiceRestControllerTest {
	@Autowired
	MockMvc mvc;

	@MockBean
	OwnerServiceOwnerRepository ownerRepo;

	@MockBean
	OwnerServicePetRepository petRepo;

	@InjectMocks
	OwnerServiceRestController orc;

//	*****************Owner related APIs test ***************

// Create owner test
	@Test
//	@Disabled
	public void testAddOwner() throws Exception {
		List<Pet> pets = new ArrayList<Pet>();
		pets.add(new Pet("Raja", "Dog"));
		Owner owner = new Owner("Jyoti", "At-Baripada", "Baripada", "7890675432", pets);

		when(ownerRepo.save(owner)).thenReturn(owner);
		ObjectMapper om = new ObjectMapper();
		String value = om.writeValueAsString(owner);
		mvc.perform(post("/owner/save").contentType(MediaType.APPLICATION_JSON).content(value))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}

//	*************Data validation tests**************** 
	@Test
//	@Disabled
	public void testEmptyNameAddOwner() throws Exception {
		List<Pet> pets = new ArrayList<Pet>();
		pets.add(new Pet("Raja", "Dog"));
		Owner owner = new Owner("", "At-Baripada", "Baripada", "7890675432", pets);

		when(ownerRepo.save(owner)).thenReturn(owner);
		ObjectMapper om = new ObjectMapper();
		String value = om.writeValueAsString(owner);
		mvc.perform(post("/owner/save").contentType(MediaType.APPLICATION_JSON).content(value))
				.andExpect(status().isBadRequest()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.name").value("Name is required"));
	}
	@Test
//	@Disabled
	public void testEmptyAddressAddOwner() throws Exception {
		List<Pet> pets = new ArrayList<Pet>();
		pets.add(new Pet("Raja", "Dog"));
		Owner owner = new Owner("Jyoti", "", "Baripada", "7890675432", pets);
		
		when(ownerRepo.save(owner)).thenReturn(owner);
		ObjectMapper om = new ObjectMapper();
		String value = om.writeValueAsString(owner);
		mvc.perform(post("/owner/save").contentType(MediaType.APPLICATION_JSON).content(value))
		.andExpect(status().isBadRequest()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.address").value("Address is required"));
	}
	@Test
//	@Disabled
	public void testEmptyTelephoneAddOwner() throws Exception {
		List<Pet> pets = new ArrayList<Pet>();
		pets.add(new Pet("Raja", "Dog"));
		Owner owner = new Owner("Jyoti", "At-Baripada", "Baripada", "78906754a32", pets);
		
		when(ownerRepo.save(owner)).thenReturn(owner);
		ObjectMapper om = new ObjectMapper();
		String value = om.writeValueAsString(owner);
		mvc.perform(post("/owner/save").contentType(MediaType.APPLICATION_JSON).content(value))
		.andExpect(status().isBadRequest()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.telephone").value("Enter 10 digit number"));
	}
	
	
// Get All owner test
	@Test
//	@Disabled	
	public void testgetAllOwner() throws Exception {
		List<Pet> pets = new ArrayList<Pet>();
		pets.add(new Pet("Raja", "Dog"));
		List<Owner> owners = new ArrayList<>();
		owners.add(new Owner("Jyoti", "At-Baripada", "Baripada", "7890675432", pets));

		Mockito.when(ownerRepo.findAll()).thenReturn(owners);

		mvc.perform(get("/owner/get").accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.[0].name").value("Jyoti"));
	}

// Get Owner By id test - positive and negative
	@Test
//	@Disabled
	public void positiveTestgetOwnerById() throws Exception {
		int id = 1;
		List<Pet> pets = new ArrayList<Pet>();
		pets.add(new Pet("Raja", "Dog"));
		Owner owner = new Owner(1, "Jyoti", "At-Baripada", "Baripada", "7890675432", pets);
		Optional<Owner> optOwner = Optional.of(owner);
		Mockito.when(ownerRepo.findById(id)).thenReturn(optOwner);

//		Positive
		mvc.perform(get("/owner/get/" + id).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(jsonPath("$.name").value("Jyoti"));

	}

// Get Owner By id test - Negative
	@Test
//	@Disabled
	public void negativeTestgetOwnerById() throws Exception {
		int id = 999;
		List<Pet> pets = new ArrayList<Pet>();
		pets.add(new Pet("Raja", "Dog"));
		Owner owner = new Owner(4, "Jyoti", "At-Baripada", "Baripada", "7890675432", pets);
		Optional<Owner> optOwner = Optional.of(owner);
		Mockito.when(ownerRepo.findById(4)).thenReturn(optOwner);

		mvc.perform(get("/owner/get/" + id).accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andDo(print());
//		.andExpect(jsonPath("$.name").value(null));
	}

// Get Owner By name test
	@Test
//	@Disabled
	public void testGetOwnerByName() throws Exception {
		String searchName = "Jyo";
		List<Pet> pets = new ArrayList<Pet>();
		pets.add(new Pet("Raja", "Dog"));
		List<Owner> owners = new ArrayList<>();
		owners.add(new Owner("Jyoti", "At-Baripada", "Baripada", "7890675432", pets));

		Mockito.when(ownerRepo.searchByNameLike(searchName)).thenReturn(owners);
		mvc.perform(get("/owner/search/" + searchName).accept(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.[0].name").value("Jyoti"));

	}

//	Update owner test - Positive	
	@Test
//	@Disabled
	public void positiveTestUpdateUserById() throws Exception {
		int id = 1;
		List<Pet> pets = new ArrayList<Pet>();
		pets.add(new Pet(1, "Raja", "Dog"));
		Owner owner = new Owner(1, "Jyoti", "At-Baripada", "Baripada", "7890675432", pets);
		Optional<Owner> optOwner = Optional.of(owner);
		Mockito.when(ownerRepo.findById(1)).thenReturn(optOwner);
		when(ownerRepo.save(owner)).thenReturn(owner);
		ObjectMapper om = new ObjectMapper();
		String value = om.writeValueAsString(owner);
		mvc.perform(put("/owner/update/" + id).contentType(MediaType.APPLICATION_JSON).content(value))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.status").value("Owner is Updated"));

	}

//	Update owner test - Negative
	@Test
//	@Disabled
	public void negativeTestUpdateUserById() throws Exception {
		int id = 2;
		List<Pet> pets = new ArrayList<Pet>();
		pets.add(new Pet(1, "Raja", "Dog"));
		Owner owner = new Owner(1, "Jyoti", "At-Baripada", "Baripada", "7890675432", pets);
		Optional<Owner> optOwner = Optional.of(owner);
		Mockito.when(ownerRepo.findById(1)).thenReturn(optOwner);
		when(ownerRepo.save(owner)).thenReturn(owner);
		ObjectMapper om = new ObjectMapper();
		String value = om.writeValueAsString(owner);

		mvc.perform(put("/owner/update/" + id).contentType(MediaType.APPLICATION_JSON).content(value))
				.andExpect(status().isBadRequest()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.status").value("No data avaliable for the owner id: 2"));
	}

//	Delete owner test - positive
	@Test
//	@Disabled
	public void testPositiveDeleteOwnerById() throws Exception {
		int id = 1;

		List<Pet> pets = new ArrayList<Pet>();
		pets.add(new Pet(5, "Kalu", "Dog"));
		Owner owner = new Owner(2, "Jyoti", "At-Baripada", "Baripada", "7890675432", pets);
		Optional<Owner> optOwner = Optional.of(owner);
		Mockito.when(ownerRepo.findById(1)).thenReturn(optOwner);
		mvc.perform(delete("/owner/delete/" + id)).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("Owner with id 1 is deleted"));

	}

//	Delete owner test - negative
	@Test
//	@Disabled
	public void testNegativeDeleteOwnerById() throws Exception {
		int id = 6;

		List<Pet> pets = new ArrayList<Pet>();
		pets.add(new Pet(5, "Kalu", "Dog"));
		Owner owner = new Owner(2, "Jyoti", "At-Baripada", "Baripada", "7890675432", pets);
		Optional<Owner> optOwner = Optional.of(owner);
		Mockito.when(ownerRepo.findById(1)).thenReturn(optOwner);

		mvc.perform(delete("/owner/delete/" + id)).andDo(print()).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value("No owner found with id: " + id));
	}

//	*********************Pet Related APIs***********************

//	Search pet by id test - positive
	@Test
//	@Disabled
	public void positiveTestGetPetById() throws Exception {
		int id = 1;
		Pet pet = new Pet(1, "Raja", "Dog");
		Optional<Pet> optPet = Optional.of(pet);
		Mockito.when(petRepo.findById(id)).thenReturn(optPet);
		mvc.perform(get("/owner/pet/" + id).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(jsonPath("$.petName").value("Raja"));

	}

//	Search pet by id test - negative
	@Test
//	@Disabled
	public void negativeTestGetPetById() throws Exception {
		int id = 2;
		Pet pet = new Pet(1, "Raja", "Dog");
		Optional<Pet> optPet = Optional.of(pet);
		Mockito.when(petRepo.findById(1)).thenReturn(optPet);
		mvc.perform(get("/owner/pet/" + id).accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andDo(print());
//		.andExpect(jsonPath("$.petName").value(null));

	}

//	Update a pet test - negative 1
	@Test
//	@Disabled
	public void testNegative1UpdatePet() throws Exception {
		int ownerId = 1;
		int petId = 3;
		Pet pet = new Pet(1, "Raja", "Dog");
		List<Pet> pets = new ArrayList<Pet>();
		pets.add(new Pet(2, "Raja", "Dog"));
		Owner owner = new Owner(1, "Jyoti", "At-Baripada", "Baripada", "7890675432", pets);
		Optional<Owner> optOwner = Optional.of(owner);
		Mockito.when(ownerRepo.findById(1)).thenReturn(optOwner);
		when(petRepo.save(pet)).thenReturn(pet);

		ObjectMapper om = new ObjectMapper();
		String value = om.writeValueAsString(pet);

		mvc.perform(put("/owner/" + ownerId + "/update/pet/" + petId).contentType(MediaType.APPLICATION_JSON)
				.content(value)).andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.status").value("No information with pet id: " + petId));

	}

//	Update a pet test - negative 2
	@Test
//	@Disabled
	public void testNegative2UpdatePet() throws Exception {
		int ownerId = 2;
		int petId = 1;
		Pet pet = new Pet(1, "Raja", "Dog");
		List<Pet> pets = new ArrayList<Pet>();
		pets.add(new Pet(2, "Raja", "Dog"));
		Owner owner = new Owner(1, "Jyoti", "At-Baripada", "Baripada", "7890675432", pets);
		Optional<Owner> optOwner = Optional.of(owner);
		Mockito.when(ownerRepo.findById(1)).thenReturn(optOwner);
		when(petRepo.save(pet)).thenReturn(pet);

		ObjectMapper om = new ObjectMapper();
		String value = om.writeValueAsString(pet);

		mvc.perform(put("/owner/" + ownerId + "/update/pet/" + petId).contentType(MediaType.APPLICATION_JSON)
				.content(value)).andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.status").value("No information with owner id: " + ownerId));

	}

//	Update a pet test - Positive
//	@Disabled
	public void testPositiveUpdatePet() throws Exception {
		int ownerId = 1;
		int petId = 1;
		Pet pet = new Pet(1, "Raja", "Dog");
		List<Pet> pets = new ArrayList<Pet>();
		pets.add(new Pet(1, "Raja", "Dog"));
		Owner owner = new Owner(1, "Jyoti", "At-Baripada", "Baripada", "7890675432", pets);
		Optional<Owner> optOwner = Optional.of(owner);
		Mockito.when(ownerRepo.findById(1)).thenReturn(optOwner);
		when(petRepo.save(pet)).thenReturn(pet);

		ObjectMapper om = new ObjectMapper();
		String value = om.writeValueAsString(pet);
		if (petId == 1)
			mvc.perform(put("/owner/" + ownerId + "/update/pet/" + petId).contentType(MediaType.APPLICATION_JSON)
					.content(value)).andExpect(status().isOk())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON))
					.andExpect(jsonPath("$.status").value("Pet updated successfully"));

	}

//	Add pet test -positive
	@Test
	@Disabled
	public void testPositiveAddPet() throws Exception {
		int ownerId = 1;
		Pet pet = new Pet("Maya", "Cat");
		List<Pet> pets = new ArrayList<Pet>();
		pets.add(new Pet(2, "Raja", "Dog"));
		Owner owner = new Owner(1, "Jyoti", "At-Baripada", "Baripada", "7890675432", pets);
		Optional<Owner> optOwner = Optional.of(owner);
		Mockito.when(ownerRepo.findById(1)).thenReturn(optOwner);
		ObjectMapper om = new ObjectMapper();
		String value = om.writeValueAsString(pet);
		mvc.perform(delete("/owner/" + ownerId + "/add/pet").contentType(MediaType.APPLICATION_JSON).content(value))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.status").value("Pet is added successfully"));

	}

//	Add pet test -negative
//	@Disabled
	public void testNegativeAddPet() throws Exception {
		int ownerId = 2;
		Pet pet = new Pet("Maya", "Cat");
		List<Pet> pets = new ArrayList<Pet>();
		pets.add(new Pet(2, "Raja", "Dog"));
		Owner owner = new Owner(1, "Jyoti", "At-Baripada", "Baripada", "7890675432", pets);
		Optional<Owner> optOwner = Optional.of(owner);
		Mockito.when(ownerRepo.findById(1)).thenReturn(optOwner);
		ObjectMapper om = new ObjectMapper();
		String value = om.writeValueAsString(pet);
		mvc.perform(delete("/owner/" + ownerId + "/add/pet").contentType(MediaType.APPLICATION_JSON).content(value))
				.andExpect(status().isBadRequest()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.status").value("Data not found for pet id: " + ownerId));

	}

//	Delete a pet test
	@Test
//	@Disabled
	public void testDeletePet() throws Exception {
		int ownerId = 1;
		int petId = 1;
		Pet pet = new Pet(1, "Raja", "Dog");
		List<Pet> pets = new ArrayList<Pet>();
		pets.add(pet);
		Owner owner = new Owner(1, "Jyoti", "At-Baripada", "Baripada", "7890675432", pets);
		Optional<Owner> optOwner = Optional.of(owner);
		Optional<Pet> optPet = Optional.of(pet);
		Mockito.when(ownerRepo.findById(1)).thenReturn(optOwner);
		Mockito.when(petRepo.findById(1)).thenReturn(optPet);

		
			mvc.perform(delete("/owner/" + ownerId + "/delete/pet/" + petId)).andExpect(status().isOk())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON))
					.andExpect(jsonPath("$.status").value("Pet is deleted successfully"));

	}
	@Test
//	@Disabled
	public void negativeTest1DeletePet() throws Exception {
		int ownerId = 2;
		int petId = 1;
		Pet pet = new Pet(1, "Raja", "Dog");
		List<Pet> pets = new ArrayList<Pet>();
		pets.add(pet);
		Owner owner = new Owner(1, "Jyoti", "At-Baripada", "Baripada", "7890675432", pets);
		Optional<Owner> optOwner = Optional.of(owner);
		Optional<Pet> optPet = Optional.of(pet);
		Mockito.when(ownerRepo.findById(1)).thenReturn(optOwner);
		Mockito.when(petRepo.findById(1)).thenReturn(optPet);
		mvc.perform(delete("/owner/" + ownerId + "/delete/pet/" + petId)).andExpect(status().isBadRequest())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.status").value("Data not found for owner id: " + ownerId));
	}
	@Test
	@Disabled
	public void negativeTest2DeletePet() throws Exception {
		int ownerId = 1;
		int petId = 2;
		Pet pet = new Pet(1, "Raja", "Dog");
		List<Pet> pets = new ArrayList<Pet>();
		pets.add(pet);
		Owner owner = new Owner(1, "Jyoti", "At-Baripada", "Baripada", "7890675432", pets);
		Optional<Owner> optOwner = Optional.of(owner);
		Optional<Pet> optPet = Optional.of(pet);
		Mockito.when(ownerRepo.findById(1)).thenReturn(optOwner);
		Mockito.when(petRepo.findById(1)).thenReturn(optPet);
		mvc.perform(delete("/owner/" + ownerId + "/delete/pet/" + petId)).andExpect(status().isBadRequest())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.status").value("Data not found for pet id: " + petId));
	}
}

