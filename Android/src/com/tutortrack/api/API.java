package com.tutortrack.api;

public class API {
	
	private static API instance = null;
	private User currentUser;
	
	public static API getInstance() {
		if (instance == null) {
			instance = new API();
		}
		return instance;
	}
	
	public boolean createStudentSession(String email, String password) {
		// TODO insert call to Web API
		return false;
	}
	
	public boolean createTutorSession(String email, String password) {
		// TODO insert call to Web API
		return false;
	}
	
	public boolean createAdminSession(String email, String password) {
		// TODO insert call to Web API
		return false;
	}

	public User getCurrentUser() {
		return currentUser;
	}

}
