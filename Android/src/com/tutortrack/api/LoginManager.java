package com.tutortrack.api;

import android.os.AsyncTask;

import com.tutortrack.api.User.UserType;

public class LoginManager {

	public static boolean login(String email, String password, UserType type) {
		
		boolean success = false;;
		switch (type) {
		case STUDENT:
			try {
				success = new StudentLoginTask().execute(email, password).get();
			} catch (Exception e) {
				success = false;
			}
			break;
		case TUTOR:
			try {
				success = new TutorLoginTask().execute(email, password).get();
			} catch (Exception e) {
				success = false;
			}
			break;
		case ADMINISTRATOR:
			try {
				success = new AdminLoginTask().execute(email, password).get();
			} catch (Exception e) {
				success = false;
			}
			break;
		}
		return success;
	}
	
	public static class StudentLoginTask extends AsyncTask<String, Void, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			String user = params[0];
			String pass = params[1];
			API api = API.getInstance();
			return api.createStudentSession(user, pass);
		}
		
	}
	
	public static class TutorLoginTask extends AsyncTask<String, Void, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			String user = params[0];
			String pass = params[1];
			API api = API.getInstance();
			return api.createTutorSession(user, pass);
		}
		
	}
	
	public static class AdminLoginTask extends AsyncTask<String, Void, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			String user = params[0];
			String pass = params[1];
			API api = API.getInstance();
			return api.createAdminSession(user, pass);
		}
		
	}
	
}
