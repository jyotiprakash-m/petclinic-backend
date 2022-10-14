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

import com.demo.spring.entity.Vet;
import com.demo.spring.repository.VetServiceVetRepository;
import com.demo.spring.rest.VetServiceVetRestController;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class VetServiceVetRestControllerTest {
	@Autowired
	MockMvc mvc;
	@MockBean
	VetServiceVetRepository repo;
	@InjectMocks
	VetServiceVetRestController vrc;
	
//	Add Vet test
	@Test
//	@Disabled
	public void testAddVet() throws Exception {
		Vet vet = new Vet("xyz","baripada","9876543290","surgery");
		when(repo.save(vet)).thenReturn(vet);
		ObjectMapper om = new ObjectMapper();
		String value = om.writeValueAsString(vet);
		mvc.perform(post("/vet/save").contentType(MediaType.APPLICATION_JSON).content(value))
		.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}
	
//	****************** Data validation test *****************
	@Test
//	@Disabled
	public void testEmptyNameAddVet() throws Exception {
		Vet vet = new Vet("","baripada","9876543290","surgery");
		when(repo.save(vet)).thenReturn(vet);
		ObjectMapper om = new ObjectMapper();
		String value = om.writeValueAsString(vet);
		mvc.perform(post("/vet/save").contentType(MediaType.APPLICATION_JSON).content(value))
		.andExpect(status().isBadRequest()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.name").value("Name is required"));
	}
	@Test
//	@Disabled
	public void testEmptyAddressAddVet() throws Exception {
		Vet vet = new Vet("xyz","","9876543290","surgery");
		when(repo.save(vet)).thenReturn(vet);
		ObjectMapper om = new ObjectMapper();
		String value = om.writeValueAsString(vet);
		mvc.perform(post("/vet/save").contentType(MediaType.APPLICATION_JSON).content(value))
		.andExpect(status().isBadRequest()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.address").value("Address is required"));
	}
	@Test
//	@Disabled
	public void testEmptyTelephoneAddVet() throws Exception {
		Vet vet = new Vet("xyz","baripada","","surgery");
		when(repo.save(vet)).thenReturn(vet);
		ObjectMapper om = new ObjectMapper();
		String value = om.writeValueAsString(vet);
		mvc.perform(post("/vet/save").contentType(MediaType.APPLICATION_JSON).content(value))
		.andExpect(status().isBadRequest()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.telephone").value("Enter 10 digit number"));
	}
	@Test
//	@Disabled
	public void testEmptySpecialtyAddVet() throws Exception {
		Vet vet = new Vet("xyz","baripada","9876543290","");
		when(repo.save(vet)).thenReturn(vet);
		ObjectMapper om = new ObjectMapper();
		String value = om.writeValueAsString(vet);
		mvc.perform(post("/vet/save").contentType(MediaType.APPLICATION_JSON).content(value))
		.andExpect(status().isBadRequest()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.specialty").value("Speciality is required"));
	}
	
	
//	List all vets test
	@Test
//	@Disabled	
	public void testGetAllVet() throws Exception {
		Vet vet = new Vet("xyz","baripada","9876543290","surgery");
		List<Vet> vets = new ArrayList<>();
		vets.add(vet);
		Mockito.when(repo.findAll()).thenReturn(vets);
		mvc.perform(get("/vet/get").accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.[0].name").value("xyz"));
	}
//	Search all vets based on specialty test
	@Test
//	@Disabled	
	public void testSearchVetBySpecialty() throws Exception {
		String specialty ="surg";
		Vet vet = new Vet("xyz","baripada","9876543290","surgery");
		List<Vet> vets = new ArrayList<>();
		vets.add(vet);
		Mockito.when(repo.searchBySpecialtyLike(specialty)).thenReturn(vets);
		mvc.perform(get("/vet/searchBySpecialty/"+specialty).accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.[0].name").value("xyz"));
	}
//	List all vets by name test
	@Test
//	@Disabled	
	public void testSearchVetByName() throws Exception {
		String name ="xy";
		Vet vet = new Vet("xyz","baripada","9876543290","surgery");
		List<Vet> vets = new ArrayList<>();
		vets.add(vet);
		Mockito.when(repo.searchByNameLike(name)).thenReturn(vets);
		mvc.perform(get("/vet/searchByName/"+name).accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.[0].name").value("xyz"));
	}
//	List all vets for a specialty test
	@Test
//	@Disabled	
	public void testFindVetBySpecialty() throws Exception {
		String specialty ="surgery";
		Vet vet = new Vet("xyz","baripada","9876543290","surgery");
		List<Vet> vets = new ArrayList<>();
		vets.add(vet);
		Mockito.when(repo.findAllBySpecialty(specialty)).thenReturn(vets);
		mvc.perform(get("/vet/find/"+specialty).accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.[0].name").value("xyz"));
	}
	
//	Update vet test (Positive and negative)
	@Test
//	@Disabled
	public void testUpdateVet() throws Exception {
		int id=1;
		Vet vet = new Vet(1,"xyz","baripada","9876543290","surgery");
		Optional<Vet> optVet = Optional.of(vet);
		Mockito.when(repo.findById(1)).thenReturn(optVet);
		ObjectMapper om = new ObjectMapper();
		String value = om.writeValueAsString(vet);
		mvc.perform(put("/vet/update/" + id).contentType(MediaType.APPLICATION_JSON).content(value))
		.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.status").value("Vet Updated"));
	}
	@Test
//	@Disabled
	public void negativeTestUpdateVet() throws Exception {
		int id=2;
		Vet vet = new Vet(1,"xyz","baripada","9876543290","surgery");
		Optional<Vet> optVet = Optional.of(vet);
		Mockito.when(repo.findById(1)).thenReturn(optVet);
		ObjectMapper om = new ObjectMapper();
		String value = om.writeValueAsString(vet);
		mvc.perform(put("/vet/update/" + id).contentType(MediaType.APPLICATION_JSON).content(value))
		.andExpect(status().isBadRequest()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.status").value("No specialty found with id: " + id));
	}
//	Remove vet test (positive and negative)
	@Test
//	@Disabled	
	public void testDeleteSpecialtyById() throws Exception {
		int id=1;
		Vet vet = new Vet(1,"xyz","baripada","9876543290","surgery");
		Optional<Vet> optVet = Optional.of(vet);
		Mockito.when(repo.findById(1)).thenReturn(optVet);
		mvc.perform(delete("/vet/delete/" + id).contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.status").value("Vet with id " + id + " is deleted"));
	}
	@Test
//	@Disabled	
	public void negativeTestDeleteSpecialtyById() throws Exception {
		int id=2;
		Vet vet = new Vet(1,"xyz","baripada","9876543290","surgery");
		Optional<Vet> optVet = Optional.of(vet);
		Mockito.when(repo.findById(1)).thenReturn(optVet);
		mvc.perform(delete("/vet/delete/" + id).contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.status").value("No Vet found with id: "+ id));
	}
	
}
