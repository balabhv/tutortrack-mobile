package com.tutortrack.api;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class User implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1329461383328008769L;

	public enum UserType {
		STUDENT, TUTOR, ADMINISTRATOR;
	}
	
	private UserType type;
	private String name;
	private String email;
	private String password;
	private String umsid;

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
	
	public static User createUser(UserType type, String name, String email, String password) {
		User u = new User();
		u.setType(type);
		u.setName(name);
		u.setEmail(email);
		u.setPassword(password);
		return u;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUmsid() {
		return umsid;
	}

	public void setUmsid(String umsid) {
		this.umsid = umsid;
	}

	public static User tutorFromJSON(JSONObject tutor) {
		User u = new User();
		u.setType(UserType.TUTOR);
		try {
			u.setName(tutor.getString("name"));
			u.setEmail(tutor.getString("email"));
			u.setPassword(tutor.getString("password"));
			u.setUmsid(tutor.getString("umsid"));
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		
		return u;
	}

	public static JSONObject JSONFromTutorUser(User tutor) {
		JSONObject obj = null;
		try {
			obj = new JSONObject();
			obj.put("name", tutor.name);
			obj.put("email", tutor.email);
			obj.put("password", tutor.password);
			obj.put("umsid", tutor.umsid);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return obj;
	}

	public static User studentFromJSON(JSONObject result) {
		User u = new User();
		u.setType(UserType.STUDENT);
		try {
			u.setName(result.getString("name"));
			u.setEmail(result.getString("email"));
			u.setPassword(result.getString("password"));
			u.setUmsid(result.getString("umsid"));
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		
		return u;
	}

	public static User adminFromJSON(JSONObject result) {
		User u = new User();
		u.setType(UserType.ADMINISTRATOR);
		try {
			u.setName(result.getString("name"));
			u.setEmail(result.getString("email"));
			u.setPassword(result.getString("password"));
			u.setUmsid(result.getString("umsid"));
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		
		return u;
	}
	
	

}
