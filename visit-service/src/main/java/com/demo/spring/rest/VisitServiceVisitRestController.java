package com.demo.spring.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.demo.spring.entity.Visit;
import com.demo.spring.repository.VisitServiceVisitRepository;
import com.demo.spring.util.Message;

@RestController
@RequestMapping("/visit")
public class VisitServiceVisitRestController {
	@Autowired
	VisitServiceVisitRepository repo;
	Message message = new Message();
	Logger logger = LoggerFactory.getLogger(VisitServiceVisitRestController.class);

//	Create visit (Post)
	@PostMapping(path = "/save")
	public ResponseEntity<Message> addVisit(@Valid @RequestBody Visit v) {
		repo.save(v);
		message.setStatus("Visit is Saved");
		return new ResponseEntity<>(message, HttpStatus.OK);

	}

//	Update visit
	@PutMapping(path = "/update/{id}")
	public ResponseEntity<Message> updateVisitById(@PathVariable("id") int id, @Valid @RequestBody Visit v) {
		if (repo.findById(id).isEmpty()) {
			logger.error("No Visit found with id: {}", id);
			message.setStatus("No visit found with id: " + id);
			return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
		} else {
			repo.save(v);
			logger.info("Visit updated successfully");
			message.setStatus("Visit updated successfully");
			return new ResponseEntity<>(message, HttpStatus.OK);
		}
	}

//	Delete visit
	@DeleteMapping(path = "/delete/{id}")
	public ResponseEntity<Message> deleteOwnerById(@PathVariable("id") int id) {

		if (repo.findById(id).isEmpty()) {
			logger.error("No visit found with id: {}", id);
			message.setStatus("No visit found with id: " + id);
			return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
		} else {
			repo.deleteById(id);
			logger.info("Visit is deleted with id {}", id);
			message.setStatus("Visit with id" + id + " is deleted");
			return new ResponseEntity<>(message, HttpStatus.OK);
		}
	}

//	Get all visits
	@GetMapping(path = "/get")
	public ResponseEntity<List<Visit>> getAllVisits() {
		return new ResponseEntity<>(repo.findAll(), HttpStatus.OK);
	}

//	Find visits all by pet id
	@GetMapping(path = "/get/{petId}")
	public ResponseEntity<List<Visit>> getAllVisitsByPetId(@PathVariable("petId") int petId) {
		return new ResponseEntity<>(repo.findAllByPetId(petId), HttpStatus.OK);
	}

	// Get Visits By name
	@GetMapping(path = "/search/{name}")
	public ResponseEntity<List<Visit>> getVisitByName(@PathVariable String name) {

		return new ResponseEntity<>(repo.searchByNameLike(name), HttpStatus.OK);
	}

//	List all visitors for given pet id
	@PostMapping(path = "/filter/petIdList")
	public ResponseEntity<List<Visit>> getAllVisitsByPetList(@RequestBody List<Integer> petIdList) {
		return new ResponseEntity<>(repo.searchByPetIdIn(petIdList), HttpStatus.OK);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return errors;
	}

}
