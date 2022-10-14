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

import com.demo.spring.entity.ValidPet;
import com.demo.spring.repository.OwnerServiceValidPetRepository;
import com.demo.spring.util.Message;

@RestController
@RequestMapping("/validPet")
public class OwnerServiceValidPetRestController {
	@Autowired
	OwnerServiceValidPetRepository repo;
	Message message = new Message();
	Logger logger = LoggerFactory.getLogger(OwnerServiceValidPetRestController.class);

//	Create Valid pet
	@PostMapping(path = "/save")
	public ResponseEntity<Message> addValidPet(@Valid @RequestBody ValidPet vp) {
		repo.save(vp);
		message.setStatus("Pet is Saved");
		return new ResponseEntity<>(message, HttpStatus.OK);

	}

//	Update Valid Pet
	@PutMapping(path = "/update/{id}")
	public ResponseEntity<Message> updateValidPetById(@PathVariable("id") int id, @Valid @RequestBody ValidPet vp) {
		if (repo.findById(id).isEmpty()) {
			logger.error("No Pet found with id: {}" , id);
			message.setStatus("No Pet found with id: " + id);
			return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
		} else {
			repo.save(vp);
			logger.info("Pet updated successfully");
			message.setStatus("Pet updated successfully");
			return new ResponseEntity<>(message, HttpStatus.OK);
		}

	}

//	Get All valid pet
	@GetMapping(path = "/get")
	public ResponseEntity<List<ValidPet>> getAllValidPets() {
		return new ResponseEntity<>(repo.findAll(), HttpStatus.OK);
	}

//	Delete valid pet
	@DeleteMapping(path = "/delete/{id}")
	public ResponseEntity<Message> deleteValidPetById(@PathVariable("id") int id) {

		if (repo.findById(id).isEmpty()) {
			logger.error("No Pet found with id: {}" , id);
			message.setStatus("No data found with pet id: " + id);
			return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
		} else {
			repo.deleteById(id);
			logger.info("Pet with is deleted");
			message.setStatus("Pet with " + id + " is deleted");
			return new ResponseEntity<>(message, HttpStatus.OK);
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
