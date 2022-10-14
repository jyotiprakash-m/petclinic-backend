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

import com.demo.spring.entity.Vet;
import com.demo.spring.repository.VetServiceVetRepository;
import com.demo.spring.util.Message;

@RestController
@RequestMapping("/vet")
public class VetServiceVetRestController {

	@Autowired
	VetServiceVetRepository repo;
	Message message = new Message();
	Logger logger = LoggerFactory.getLogger(VetServiceVetRestController.class);

//	Add vet (Post)
	@PostMapping(path = "/save")
	public ResponseEntity<Message> addVet(@Valid @RequestBody Vet v) {
		repo.save(v);
		logger.info("Vet is Saved");
		message.setStatus("Vet is Saved");
		return new ResponseEntity<>(message, HttpStatus.OK);

	}

//	List all vets (Get)
	@GetMapping(path = "/get")
	public ResponseEntity<List<Vet>> getAllVet() {
		return new ResponseEntity<>(repo.findAll(), HttpStatus.OK);
	}

//	Search a vet based on specialty (Get) - Tough 
	@GetMapping(path = "/searchBySpecialty/{specialty}")
	public ResponseEntity<List<Vet>> searchVetBySpecialty(@PathVariable String specialty) {
		return new ResponseEntity<>(repo.searchBySpecialtyLike(specialty), HttpStatus.OK);

	}

//	List all vet by name (Get) - Tough
	@GetMapping(path = "/searchByName/{name}")
	public ResponseEntity<List<Vet>> searchVetByName(@PathVariable String name) {
		return new ResponseEntity<>(repo.searchByNameLike(name), HttpStatus.OK);

	}

//	List all vet for a specialty (Get) - Tough
	@GetMapping(path = "/find/{specialty}")
	public ResponseEntity<List<Vet>> findVetBySpecialty(@PathVariable String specialty) {
		return new ResponseEntity<>(repo.findAllBySpecialty(specialty), HttpStatus.OK);
	}

//	Remove vet (Delete)
	@DeleteMapping(path = "/delete/{id}")
	public ResponseEntity<Message> deleteVetById(@PathVariable("id") int id) {
		Optional<Vet> vet = repo.findById(id);

		if (vet.isEmpty()) {
			logger.error("No Vet found with id: {}", id);
			message.setStatus("No Vet found with id: "+ id);
			return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
		} else {
			repo.deleteById(id);
			logger.info("Vet is deleted with id {}", id);
			message.setStatus("Vet with id " + id + " is deleted");
			return new ResponseEntity<>(message, HttpStatus.OK);
		}
	}

//	Update vet (Update)
	@PutMapping(path = "/update/{id}")
	public ResponseEntity<Message> updateVet(@PathVariable("id") int id, @Valid @RequestBody Vet v) {
		if (repo.findById(id).isEmpty()) {
			logger.error("No Vet information found with id: {}", id);
			message.setStatus("No specialty found with id: " + id);
			return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
		} else {
			repo.save(v);
			logger.info("Vet Updated");
			message.setStatus("Vet Updated");
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
