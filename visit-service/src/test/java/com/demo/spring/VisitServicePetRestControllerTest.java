package com.demo.spring;

import static org.mockito.Mockito.when;
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

import com.demo.spring.entity.Pet;
import com.demo.spring.repository.VisitorServicePetRepository;
import com.demo.spring.rest.VisitServicePetRestController;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class VisitServicePetRestControllerTest {
	@Autowired
	MockMvc mvc;
	@MockBean
	VisitorServicePetRepository repo;
	@InjectMocks
	VisitServicePetRestController vpc;
	
//	Create pet test
	@Test
//	@Disabled
	public void testAddPet() throws Exception {
		Pet pet = new Pet("Dog");
		when(repo.save(pet)).thenReturn(pet);
		ObjectMapper om = new ObjectMapper();
		String value = om.writeValueAsString(pet);
		mvc.perform(post("/pet/save").contentType(MediaType.APPLICATION_JSON).content(value))
		.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.status").value("Pet is Saved"));
	}
	
//	************* Data Validation Test ****************
	@Test
//	@Disabled
	public void testEmptytypeAddPet() throws Exception {
		Pet pet = new Pet("");
		when(repo.save(pet)).thenReturn(pet);
		ObjectMapper om = new ObjectMapper();
		String value = om.writeValueAsString(pet);
		mvc.perform(post("/pet/save").contentType(MediaType.APPLICATION_JSON).content(value))
		.andExpect(status().isBadRequest()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.type").value("Pet type is required"));
	}
	
//	Update pet test(Positive and negative)
	@Test
//	@Disabled
	public void testUpdatePetById() throws Exception {
		int id=1;
		Pet pet = new Pet(1,"Dog");
		Optional<Pet> optPet = Optional.of(pet);
		Mockito.when(repo.findById(1)).thenReturn(optPet);
		ObjectMapper om = new ObjectMapper();
		String value = om.writeValueAsString(pet);
		mvc.perform(put("/pet/update/" + id).contentType(MediaType.APPLICATION_JSON).content(value))
		.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.status").value("Pet updated successfully"));
	}
	@Test
//	@Disabled
	public void negativeTestUpdatePetById() throws Exception {
		int id=2;
		Pet pet = new Pet(1,"Dog");
		Optional<Pet> optPet = Optional.of(pet);
		Mockito.when(repo.findById(1)).thenReturn(optPet);
		ObjectMapper om = new ObjectMapper();
		String value = om.writeValueAsString(pet);
		mvc.perform(put("/pet/update/" + id).contentType(MediaType.APPLICATION_JSON).content(value))
		.andExpect(status().isBadRequest()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.status").value("No Pet found with id: "+id));
	}
	

	
//	Get all pet test
	@Test
//	@Disabled	
	public void testGetAllPets() throws Exception {
		Pet pet = new Pet(1,"Dog");
		List<Pet> pets = new ArrayList<>();
		pets.add(pet);
		Mockito.when(repo.findAll()).thenReturn(pets);
		mvc.perform(get("/pet/get").accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.[0].type").value("Dog"));
	}
	
//	Get pet by id test
	@Test
//	@Disabled	
	public void testGetPetById() throws Exception {
		int id =1;
		Pet pet = new Pet(1,"Dog");
		Optional<Pet> opPet = Optional.of(pet);
		Mockito.when(repo.findById(1)).thenReturn(opPet);
		mvc.perform(get("/pet/get/"+id).accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.type").value("Dog"));
	}
	@Test
//	@Disabled	
	public void negativeTestGetPetById() throws Exception {
		int id =2;
		Pet pet = new Pet(1,"Dog");
		Optional<Pet> opPet = Optional.of(pet);
		Mockito.when(repo.findById(1)).thenReturn(opPet);
		mvc.perform(get("/pet/get/"+id).accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isBadRequest())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON));
//		.andExpect(jsonPath("$.type").value(null));
	}
	
}
