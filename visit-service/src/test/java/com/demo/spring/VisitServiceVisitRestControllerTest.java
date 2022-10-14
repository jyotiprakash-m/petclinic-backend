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

import com.demo.spring.entity.Visit;
import com.demo.spring.repository.VisitServiceVisitRepository;
import com.demo.spring.rest.VisitServiceVisitRestController;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class VisitServiceVisitRestControllerTest {
	@Autowired
	MockMvc mvc;
	@MockBean
	VisitServiceVisitRepository repo;
	@InjectMocks
	VisitServiceVisitRestController vvc;
	
//	Create visit test
	@Test
//	@Disabled
	public void testAddVisit() throws Exception {
		Visit visit = new Visit("Jyoti","At-Baripada","Baripada","8976543298",1);
		when(repo.save(visit)).thenReturn(visit);
		ObjectMapper om = new ObjectMapper();
		String value = om.writeValueAsString(visit);
		mvc.perform(post("/visit/save").contentType(MediaType.APPLICATION_JSON).content(value))
		.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.status").value("Visit is Saved"));
	}
	
//	**************** Data Validation test *******************
//	Create visit test
	@Test
//	@Disabled
	public void testEmptyNameAddVisit() throws Exception {
		Visit visit = new Visit("","At-Baripada","Baripada","8976543298",1);
		when(repo.save(visit)).thenReturn(visit);
		ObjectMapper om = new ObjectMapper();
		String value = om.writeValueAsString(visit);
		mvc.perform(post("/visit/save").contentType(MediaType.APPLICATION_JSON).content(value))
		.andExpect(status().isBadRequest()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.name").value("Name is required"));
	}
	
//	Create visit test
	@Test
//	@Disabled
	public void testEmptyAddressAddVisit() throws Exception {
		Visit visit = new Visit("Jyoti","","Baripada","8976543298",1);
		when(repo.save(visit)).thenReturn(visit);
		ObjectMapper om = new ObjectMapper();
		String value = om.writeValueAsString(visit);
		mvc.perform(post("/visit/save").contentType(MediaType.APPLICATION_JSON).content(value))
		.andExpect(status().isBadRequest()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.address").value("Address is required"));
	}
//	Create visit test
	@Test
//	@Disabled
	public void testEmptyTelephoneAddVisit() throws Exception {
		Visit visit = new Visit("Jyoti","At-Baripada","Baripada","",1);
		when(repo.save(visit)).thenReturn(visit);
		ObjectMapper om = new ObjectMapper();
		String value = om.writeValueAsString(visit);
		mvc.perform(post("/visit/save").contentType(MediaType.APPLICATION_JSON).content(value))
		.andExpect(status().isBadRequest()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.telephone").value("Enter 10 digit number"));
	}

	
//	Update visit test (Positive and negative)
	@Test
//	@Disabled
	public void testUpdateVisitById() throws Exception {
		int id=1;
		Visit visit = new Visit(1,"Jyoti","At-Baripada","Baripada","8976543298",1);
		Optional<Visit> optVisit = Optional.of(visit);
		Mockito.when(repo.findById(1)).thenReturn(optVisit);
		ObjectMapper om = new ObjectMapper();
		String value = om.writeValueAsString(visit);
		mvc.perform(put("/visit/update/" + id).contentType(MediaType.APPLICATION_JSON).content(value))
		.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.status").value("Visit updated successfully"));
	}
	@Test
//	@Disabled
	public void negativeTestUpdateVisitById() throws Exception {
		int id=2;
		Visit visit = new Visit(1,"Jyoti","At-Baripada","Baripada","8976543298",1);
		Optional<Visit> optVisit = Optional.of(visit);
		Mockito.when(repo.findById(1)).thenReturn(optVisit);
		ObjectMapper om = new ObjectMapper();
		String value = om.writeValueAsString(visit);
		mvc.perform(put("/visit/update/" + id).contentType(MediaType.APPLICATION_JSON).content(value))
		.andExpect(status().isBadRequest()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.status").value("No visit found with id: " + id));
	}
//	Delete visit test (Positive and negative)
	@Test
//	@Disabled	
	public void testDeleteVisitById() throws Exception {
		int id=1;
		Visit visit = new Visit(1,"Jyoti","At-Baripada","Baripada","8976543298",1);
		Optional<Visit> optVisit = Optional.of(visit);
		Mockito.when(repo.findById(1)).thenReturn(optVisit);
		mvc.perform(delete("/visit/delete/" + id).contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.status").value("Visit with id" + id + " is deleted"));
	}
	@Test
//	@Disabled	
	public void testNegativeDeleteVisitById() throws Exception {
		int id=2;
		Visit visit = new Visit(1,"Jyoti","At-Baripada","Baripada","8976543298",1);
		Optional<Visit> optVisit = Optional.of(visit);
		Mockito.when(repo.findById(1)).thenReturn(optVisit);
		mvc.perform(delete("/visit/delete/" + id).contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.status").value("No visit found with id: " + id));
	}
//	Get all visits test
	@Test
//	@Disabled	
	public void testGetAllVisits() throws Exception {
		Visit visit = new Visit(1,"Jyoti","At-Baripada","Baripada","8976543298",1);
		List<Visit> visits = new ArrayList<>();
		visits.add(visit);
		Mockito.when(repo.findAll()).thenReturn(visits);
		mvc.perform(get("/visit/get").accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.[0].name").value("Jyoti"));
	}
//  Find visits all by pet id test
	@Test
//	@Disabled	
	public void testGetAllVisitsByPetId() throws Exception {
		int petId =1;
		Visit visit = new Visit(1,"Jyoti","At-Baripada","Baripada","8976543298",1);
		List<Visit> visits = new ArrayList<>();
		visits.add(visit);
		Mockito.when(repo.findAllByPetId(petId)).thenReturn(visits);
		mvc.perform(get("/visit/get/"+petId).accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.[0].name").value("Jyoti"));
	}
//	Get Visits By name test
	@Test
//	@Disabled	
	public void testGetVisitByName() throws Exception {
		String name ="Jyoti";
		Visit visit = new Visit(1,"Jyoti","At-Baripada","Baripada","8976543298",1);
		List<Visit> visits = new ArrayList<>();
		visits.add(visit);
		Mockito.when(repo.searchByNameLike(name)).thenReturn(visits);
		mvc.perform(get("/visit/search/"+name).accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.[0].name").value("Jyoti"));
	}
//	List all visitors for given pet id test
	@Test
//	@Disabled	
	public void testGetAllVisitsByPetList() throws Exception {
		List<Integer> petIdList =new ArrayList<Integer>();
		petIdList.add(1);
		petIdList.add(2);
		Visit visit = new Visit(1,"Jyoti","At-Baripada","Baripada","8976543298",1);
		List<Visit> visits = new ArrayList<>();
		visits.add(visit);
		Mockito.when(repo.searchByPetIdIn(petIdList)).thenReturn(visits);
		ObjectMapper om = new ObjectMapper();
		String value = om.writeValueAsString(petIdList);
		mvc.perform(post("/visit/filter/petIdList").contentType(MediaType.APPLICATION_JSON).content(value)).andDo(print()).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.[0].name").value("Jyoti"));
	}
}
