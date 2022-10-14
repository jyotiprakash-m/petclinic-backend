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

import com.demo.spring.entity.ValidPet;
import com.demo.spring.repository.OwnerServiceValidPetRepository;
import com.demo.spring.rest.OwnerServiceValidPetRestController;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class OwnerServiceValidPetRestControllerTest {
	@Autowired
	MockMvc mvc;
	@MockBean
	OwnerServiceValidPetRepository repo;
	@InjectMocks
	OwnerServiceValidPetRestController ovc;

//	Create valid pet test
	@Test
//	@Disabled
	public void testAddValidPet() throws Exception {
		ValidPet pet = new ValidPet("Dog");
		when(repo.save(pet)).thenReturn(pet);
		ObjectMapper om = new ObjectMapper();
		String value = om.writeValueAsString(pet);
		mvc.perform(post("/validPet/save").contentType(MediaType.APPLICATION_JSON).content(value))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}
	
//	*************Data validation tests**************** 
	@Test
//	@Disabled
	public void testEmptyTypeAddValidPet() throws Exception {
		ValidPet pet = new ValidPet("");
		when(repo.save(pet)).thenReturn(pet);
		ObjectMapper om = new ObjectMapper();
		String value = om.writeValueAsString(pet);
		mvc.perform(post("/validPet/save").contentType(MediaType.APPLICATION_JSON).content(value))
				.andExpect(status().isBadRequest()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.type").value("Pet type is required"));
	}
//	Update valid pet test
	@Test
//	@Disabled
	public void testUpdateValidPetById() throws Exception {
		int id = 1;
		ValidPet pet = new ValidPet(1, "Dog");
		Optional<ValidPet> optValidPet = Optional.of(pet);
		Mockito.when(repo.findById(1)).thenReturn(optValidPet);
		ObjectMapper om = new ObjectMapper();
		String value = om.writeValueAsString(pet);
		mvc.perform(put("/validPet/update/" + id).contentType(MediaType.APPLICATION_JSON).content(value))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.status").value("Pet updated successfully"));
	}

//	Update valid pet test
	@Test
//	@Disabled
	public void negativeTestUpdateValidPetById() throws Exception {
		int id = 2;
		ValidPet pet = new ValidPet(1, "Dog");
		Optional<ValidPet> optValidPet = Optional.of(pet);
		Mockito.when(repo.findById(1)).thenReturn(optValidPet);
		ObjectMapper om = new ObjectMapper();
		String value = om.writeValueAsString(pet);
		mvc.perform(put("/validPet/update/" + id).contentType(MediaType.APPLICATION_JSON).content(value))
				.andExpect(status().isBadRequest()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.status").value("No Pet found with id: " + id));
	}

//	Get all valid pet test
	@Test
//		@Disabled	
	public void testgetAllValidPets() throws Exception {
		ValidPet pet = new ValidPet(1, "Dog");
		List<ValidPet> pets = new ArrayList<>();
		pets.add(pet);
		Mockito.when(repo.findAll()).thenReturn(pets);
		mvc.perform(get("/validPet/get").accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.[0].type").value("Dog"));
	}

//		Delete valid pet test
	@Test
//		@Disabled	
	public void testDeleteValidPetById() throws Exception {
		int id = 1;
		ValidPet pet = new ValidPet(1, "Dog");
		Optional<ValidPet> optValidPet = Optional.of(pet);
		Mockito.when(repo.findById(1)).thenReturn(optValidPet);
		mvc.perform(delete("/validPet/delete/" + id).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.status").value("Pet with " + id + " is deleted"));
	}

	@Test
//		@Disabled	
	public void testNegativeDeleteValidPetById() throws Exception {
		int id = 2;
		ValidPet pet = new ValidPet(1, "Dog");
		Optional<ValidPet> optValidPet = Optional.of(pet);
		Mockito.when(repo.findById(1)).thenReturn(optValidPet);
		mvc.perform(delete("/validPet/delete/" + id).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.status").value("No data found with pet id: " + id));
	}

}
