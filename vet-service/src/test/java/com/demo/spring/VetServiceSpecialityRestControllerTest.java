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

import com.demo.spring.entity.Specialty;
import com.demo.spring.repository.VetServiceSpecialityRepository;
import com.demo.spring.rest.VetServiceSpecialityRestController;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class VetServiceSpecialityRestControllerTest {
	@Autowired
	MockMvc mvc;
	@MockBean
	VetServiceSpecialityRepository repo;
	
	@InjectMocks
	VetServiceSpecialityRestController vsc;
	
//	Add specialty test
	@Test
//	@Disabled
	public void testAddSpecialty() throws Exception {
		Specialty specialty = new Specialty("surgery");
		when(repo.save(specialty)).thenReturn(specialty);
		ObjectMapper om = new ObjectMapper();
		String value = om.writeValueAsString(specialty);
		mvc.perform(post("/speciality/save").contentType(MediaType.APPLICATION_JSON).content(value))
		.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}
	
//	***************** Data Validation *******************
	@Test
//	@Disabled
	public void testEmptyNameAddSpecialty() throws Exception {
		Specialty specialty = new Specialty("");
		when(repo.save(specialty)).thenReturn(specialty);
		ObjectMapper om = new ObjectMapper();
		String value = om.writeValueAsString(specialty);
		mvc.perform(post("/speciality/save").contentType(MediaType.APPLICATION_JSON).content(value))
		.andExpect(status().isBadRequest()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.name").value("Name is required"));
	}
	
//	List all specialty test
	@Test
//	@Disabled	
	public void testgetAllSpecialty() throws Exception {
		Specialty specialty = new Specialty("surgery");
		List<Specialty> specialties = new ArrayList<>();
		specialties.add(specialty);
		Mockito.when(repo.findAll()).thenReturn(specialties);
		mvc.perform(get("/speciality/get").accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.[0].name").value("surgery"));
	}
//	Update the specialty test
	@Test
//	@Disabled
	public void testUpdateSpecialty() throws Exception {
		int id=1;
		Specialty specialty = new Specialty(1,"surgery");
		Optional<Specialty> optSpecialty = Optional.of(specialty);
		Mockito.when(repo.findById(1)).thenReturn(optSpecialty);
		ObjectMapper om = new ObjectMapper();
		String value = om.writeValueAsString(specialty);
		mvc.perform(put("/speciality/update/" + id).contentType(MediaType.APPLICATION_JSON).content(value))
		.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.status").value("Specialty with id " + id + " is updated"));
	}
	
	@Test
//	@Disabled
	public void negativeTestUpdateSpecialty() throws Exception {
		int id=2;
		Specialty specialty = new Specialty(1,"surgery");
		Optional<Specialty> optSpecialty = Optional.of(specialty);
		Mockito.when(repo.findById(1)).thenReturn(optSpecialty);
		ObjectMapper om = new ObjectMapper();
		String value = om.writeValueAsString(specialty);
		mvc.perform(put("/speciality/update/" + id).contentType(MediaType.APPLICATION_JSON).content(value))
		.andExpect(status().isBadRequest()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.status").value("No specialty found with id: " + id));
	}
//	Remove Specialty test
	@Test
//	@Disabled	
	public void testDeleteSpecialtyById() throws Exception {
		int id=1;
		Specialty specialty = new Specialty(1,"surgery");
		Optional<Specialty> optSpecialty = Optional.of(specialty);
		Mockito.when(repo.findById(1)).thenReturn(optSpecialty);
		mvc.perform(delete("/speciality/delete/" + id).contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.status").value("Specialty having id " + id + " is deleted"));
	}
	
	@Test
//	@Disabled	
	public void testNegativeDeleteSpecialtyById() throws Exception {
		int id=2;
		Specialty specialty = new Specialty(1,"surgery");
		Optional<Specialty> optSpecialty = Optional.of(specialty);
		Mockito.when(repo.findById(1)).thenReturn(optSpecialty);
		mvc.perform(delete("/speciality/delete/" + id).contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.status").value("No Information of specialty found with id: " + id));
	}
	
	
}
