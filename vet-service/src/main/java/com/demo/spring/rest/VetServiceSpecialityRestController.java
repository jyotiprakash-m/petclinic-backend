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

import com.demo.spring.entity.Specialty;
import com.demo.spring.repository.VetServiceSpecialityRepository;
import com.demo.spring.util.Message;

@RestController
@RequestMapping("/speciality")
public class VetServiceSpecialityRestController {
	@Autowired
	VetServiceSpecialityRepository repo;

	Message message = new Message();
	Logger logger = LoggerFactory.getLogger(VetServiceSpecialityRestController.class);

//	Add specialist (Post)
	@PostMapping(path = "/save")
	public ResponseEntity<Message> addSpecialty(@Valid @RequestBody Specialty s) {
		repo.save(s);
		logger.info("Specialty is Saved");
		message.setStatus("Specialty id Saved");
		return new ResponseEntity<>(message, HttpStatus.OK);
	}

//	List All Specialist (Get)
	@GetMapping(path = "/get")
	public ResponseEntity<List<Specialty>> getAllSpecialty() {
		return new ResponseEntity<>(repo.findAll(), HttpStatus.OK);
	}

//	Update the Specialist
	@PutMapping(path = "/update/{id}")
	public ResponseEntity<Message> updateSpecialty(@PathVariable("id") int id, @Valid @RequestBody Specialty s) {
		if (repo.findById(id).isEmpty()) {
			logger.error("No informatio found with id: {}", id);
			message.setStatus("No specialty found with id: " + id);
			return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
		} else {
			repo.save(s);
			logger.info("Specialty is updated with id: {}", id);
			message.setStatus("Specialty with id " + id + " is updated");
			return new ResponseEntity<>(message, HttpStatus.OK);
		}
	}

//	Remove Specialty (Delete)
	@DeleteMapping(path = "/delete/{id}")
	public ResponseEntity<Message> deleteSpecialtyById(@PathVariable("id") int id) {

		if (repo.findById(id).isEmpty()) {
			logger.error("No data found with id: {}", id);
			message.setStatus("No Information of specialty found with id: " + id);
			return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
		} else {
			repo.deleteById(id);
			logger.info("Specialty is deleted with id {}", id);
			message.setStatus("Specialty having id " + id + " is deleted");
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
