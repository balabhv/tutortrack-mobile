package com.tutortrack.api;

public class User {
	
	public enum UserType {
		STUDENT, TUTOR, ADMINISTRATOR;
	}
	
	private UserType type;
	private String name;
	private String email;

	public UserType getType() {
		return type;
	}

	public void setType(UserType type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public static User createUser(UserType type, String name, String email) {
		User u = new User();
		u.setType(type);
		u.setName(name);
		u.setEmail(email);
		return u;
	}
	
	

}
