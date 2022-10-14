package com.demo.spring.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.demo.spring.entity.Pet;
import com.demo.spring.repository.VisitorServicePetRepository;
import com.demo.spring.util.Message;

@RestController
@RequestMapping("/pet")
public class VisitServicePetRestController {
	@Autowired
	VisitorServicePetRepository repo;
	Message message = new Message();
	Logger logger = LoggerFactory.getLogger(VisitServicePetRestController.class);

//	 Create
	@PostMapping(path = "/save")
	public ResponseEntity<Message> addPet(@Valid @RequestBody Pet p) {
		repo.save(p);
		logger.info("Pet is Saved");
		message.setStatus("Pet is Saved");
		return new ResponseEntity<>(message, HttpStatus.OK);

	}

//	 Update
	@PutMapping(path = "/update/{id}")
	public ResponseEntity<Message> updateUserById(@PathVariable("id") int id, @Valid @RequestBody Pet p) {
		if (repo.findById(id).isEmpty()) {
			logger.error("No Pet found with id: {}" , id);
			message.setStatus("No Pet found with id: " + id);
			return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
		} else {
			repo.save(p);
			logger.info("Pet updated successfully");
			message.setStatus("Pet updated successfully");
			return new ResponseEntity<>(message, HttpStatus.OK);
		}

	}

//	 Get all
	@GetMapping(path = "/get")
	public ResponseEntity<List<Pet>> getAllPets() {
		return new ResponseEntity<>(repo.findAll(), HttpStatus.OK);

	}

//	 Get by id
	@GetMapping(path = "/get/{id}")
	public ResponseEntity<Pet> getPetById(@PathVariable int id) {
		Pet emptyPet = new Pet();
		Optional<Pet> petOp = repo.findById(id);
		if (petOp.isPresent()) {
			return new ResponseEntity<>(petOp.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(emptyPet, HttpStatus.BAD_REQUEST);
		}
		
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
