package com.demo.spring.util;

public class Message {
	private String status;
	private String state;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Message() {
	}

	public Message(String status, String state) {
		this.status = status;
		this.state = state;
	}

}
