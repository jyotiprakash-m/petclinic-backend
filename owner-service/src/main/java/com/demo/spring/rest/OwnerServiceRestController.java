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

import com.demo.spring.entity.Owner;
import com.demo.spring.entity.Pet;
import com.demo.spring.repository.OwnerServiceOwnerRepository;
import com.demo.spring.repository.OwnerServicePetRepository;
import com.demo.spring.util.Message;

//@CrossOrigin(origins = "http://localhost:3333")
@RestController
@RequestMapping("/owner")
public class OwnerServiceRestController {
	@Autowired
	OwnerServiceOwnerRepository ownerRepo;

	@Autowired
	OwnerServicePetRepository petRepo;
	Message message = new Message();
	Logger logger = LoggerFactory.getLogger(OwnerServiceRestController.class);

//	Owner related APIs

//	Create owner (Post)
	@PostMapping(path = "/save")
	public ResponseEntity<Message> addOwner(@Valid @RequestBody Owner o) {
		ownerRepo.save(o);
		message.setStatus("Owner is Saved");
		return new ResponseEntity<>(message, HttpStatus.OK);

	}

// Get All owner
	@GetMapping(path = "/get")
	public ResponseEntity<List<Owner>> getAllOwner() {
		return new ResponseEntity<>(ownerRepo.findAll(), HttpStatus.OK);
	}

// Get Owner By id
	@GetMapping(path = "/get/{id}")
	public ResponseEntity<Owner> getOwnerById(@PathVariable int id) {
		Owner emptyOwner = new Owner();
		Optional<Owner> ownerOp = ownerRepo.findById(id);
		if (ownerOp.isPresent()) {
			return new ResponseEntity<>(ownerOp.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(emptyOwner, HttpStatus.BAD_REQUEST);
		}

	}

// Get Owner By name
	@GetMapping(path = "/search/{name}")
	public ResponseEntity<List<Owner>> getOwnerByName(@PathVariable String name) {
		return new ResponseEntity<>(ownerRepo.searchByNameLike(name), HttpStatus.OK);
	}

//	Update owner (Update)
	@PutMapping(path = "/update/{id}")
	public ResponseEntity<Message> updateUserById(@PathVariable("id") int id, @Valid @RequestBody Owner o) {
		if (ownerRepo.findById(id).isEmpty()) {
			logger.error("No Owner found with id: {}", id);
			message.setStatus("No data avaliable for the owner id: " + id);
			return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
		} else {
			ownerRepo.save(o);
			message.setStatus("Owner is Updated");
			return new ResponseEntity<>(message, HttpStatus.OK);
		}

	}

//	Delete owner (Delete)
	@DeleteMapping(path = "/delete/{id}")
	public ResponseEntity<Message> deleteOwnerById(@PathVariable("id") int id) {

		if (ownerRepo.findById(id).isEmpty()) {
			logger.error("No owner found with id: {}", id);
			message.setStatus("No owner found with id: " + id);
			return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
		} else {
			ownerRepo.deleteById(id);
			logger.info("Owner is deleted");
			message.setStatus("Owner with id " + id + " is deleted");
			return new ResponseEntity<>(message, HttpStatus.OK);
		}
	}

//	Pet Related APIs

//	Search pet by id (Get)
	@GetMapping(path = "/pet/{id}")
	public ResponseEntity<Pet> getPetById(@PathVariable("id") int id) {
		Pet emptyPet = new Pet();
		Optional<Pet> petOp = petRepo.findById(id);
		if (petOp.isPresent()) {
			return new ResponseEntity<>(petOp.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(emptyPet, HttpStatus.BAD_REQUEST);
		}
	}

//	Update a pet
	@PutMapping(path = "/{ownerId}/update/pet/{petId}")
	public ResponseEntity<Message> updatePet(@PathVariable("ownerId") int ownerId, @PathVariable("petId") int petId,
			@Valid @RequestBody Pet p) {

		if (ownerRepo.findById(ownerId).isEmpty() || petRepo.findById(petId).isEmpty()) {
			if (ownerRepo.findById(ownerId).isEmpty()) {
				logger.error("No information with owner id:: {}", ownerId);
				message.setStatus("No information with owner id: " + ownerId);
			} else {
				logger.error("No information with pet id: {}", petId);
				message.setStatus("No information with pet id: " + petId);
			}
			return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
		} else {
			petRepo.save(p);
			logger.info("Pet updated successfully");
			message.setStatus("Pet updated successfully");
			return new ResponseEntity<>(message, HttpStatus.OK);
		}
	}

//	Add a pet
	@PostMapping(path = "/{id}/add/pet")
	public ResponseEntity<Message> addPet(@PathVariable int id, @Valid @RequestBody Pet p) {
		Optional<Owner> owner = ownerRepo.findById(id);

		if (owner.isEmpty()) {
			logger.error("No owner with id: {}", id);
			message.setStatus("No owner with id: " + id);
			return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
		} else {
			owner.get().getPets().add(p);
			ownerRepo.save(owner.get());
			logger.info("Pet is added successfully");
			message.setStatus("Pet is added successfully");
			return new ResponseEntity<>(message, HttpStatus.OK);
		}
	}

//	Delete a pet
	@DeleteMapping(path = "/{ownerId}/delete/pet/{petId}")
	public ResponseEntity<Message> deletePet(@PathVariable("ownerId") int ownerId, @PathVariable("petId") int petId) {

		if (ownerRepo.findById(ownerId).isEmpty() || petRepo.findById(petId).isEmpty()) {
			if (ownerRepo.findById(ownerId).isEmpty()) {
				logger.error("Data not found for owner id: {}", ownerId);
				message.setStatus("Data not found for owner id: " + ownerId);
			} else {
				logger.error("Data not found for pet id: {}", petId);
				message.setStatus("Data not found for pet id: " + petId);
			}
			return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
		} else {
			petRepo.deleteById(petId);
			logger.info("Pet is deleted successfully");
			message.setStatus("Pet is deleted successfully");
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
