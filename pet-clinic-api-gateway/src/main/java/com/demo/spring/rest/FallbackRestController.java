package com.demo.spring.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.spring.util.Message;

@RestController
public class FallbackRestController {

	Message message = new Message();
	Logger logger = LoggerFactory.getLogger(FallbackRestController.class);

	@GetMapping("/ownerServiceFallback")
	public ResponseEntity<Message> ownerServiceFallback() {
		logger.error("Owner service is down");
		message.setStatus("Owner service is down");
		message.setState("error");
		return new ResponseEntity<>(message, HttpStatus.SERVICE_UNAVAILABLE);
	}

	@GetMapping("/vetServiceFallback")
	public ResponseEntity<Message> vetServiceFallback() {
		logger.error("Vet service is down");
		message.setStatus("Vet service is down");
		message.setState("error");
		return new ResponseEntity<>(message, HttpStatus.SERVICE_UNAVAILABLE);
	}

	@GetMapping("/visitServiceFallback")
	public ResponseEntity<Message> visitServiceFallback() {
		logger.error("Visit service is down");
		message.setStatus("Visit service is down");
		message.setState("error");
		return new ResponseEntity<>(message, HttpStatus.SERVICE_UNAVAILABLE);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Message> handleError(Exception ex) {
		logger.error(ex.getMessage());
		message.setStatus("Something went wrong");
		message.setState("error");
		return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
