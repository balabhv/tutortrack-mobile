package com.tutortrack.api;

import android.app.Dialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tutortrack.R;
import com.tutortrack.api.User.UserType;

public class LoginManager {

	private static boolean success;

	public static boolean login(String email, String password, UserType type) {

		switch (type) {
		case STUDENT:
			try {
				Log.d("LoginManager", "loginattempt");
				success = new StudentLoginTask().execute(email, password).get();
			} catch (Exception e) {
				success = false;
			}
			break;
		case TUTOR:
			try {
				new TutorLoginTask().execute(email, password);
			} catch (Exception e) {
				success = false;
			}
			break;
		case ADMINISTRATOR:
			try {
				new AdminLoginTask().execute(email, password);
			} catch (Exception e) {
				success = false;
			}
			break;
		}
		return success;
	}

	public static class StudentLoginTask extends
			AsyncTask<String, Void, Boolean> {

		Dialog p;

		public StudentLoginTask() {
			super();
			p = new Dialog(API.mainActivity);
			p.setContentView(R.layout.progress_dialog);
			TextView text = (TextView) p.findViewById(R.id.descripTextField);
			text.setText("Logging in...");
			ProgressBar bar = (ProgressBar) p.findViewById(R.id.progressBar);
			bar.setIndeterminate(true);
			p.setCanceledOnTouchOutside(false);

		}

		public void onPreExecute() {
			Log.d("LoginManager", "onPreExecute");

			p.show();
		}

		@Override
		protected Boolean doInBackground(String... params) {
			Log.d("LoginManager", "doInBackground");
			String user = params[0];
			String pass = params[1];
			API api = API.getInstance();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (api.createStudentSession(user, pass) == null)
				return false;
			else
				return true;
		}

		public void onPostExecute(Boolean b) {
			if (API.getCurrentUser() != null) {
				p.cancel();
				success = b;
			}

		}
	}

	public static class TutorLoginTask extends AsyncTask<String, Void, Boolean> {

		Dialog p;

		public TutorLoginTask() {
			super();
			p = new Dialog(API.mainActivity);
			p.setContentView(R.layout.progress_dialog);
			TextView text = (TextView) p.findViewById(R.id.descripTextField);
			text.setText("Logging in...");
			ProgressBar bar = (ProgressBar) p.findViewById(R.id.progressBar);
			bar.setIndeterminate(true);
			p.setCanceledOnTouchOutside(false);

		}

		public void onPreExecute() {
			Log.d("LoginManager", "onPreExecute");

			p.show();
		}

		@Override
		protected Boolean doInBackground(String... params) {
			Log.d("LoginManager", "doInBackground");
			String user = params[0];
			String pass = params[1];
			API api = API.getInstance();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (api.createTutorSession(user, pass) == null)
				return false;
			else
				return true;
		}

		public void onPostExecute(Boolean b) {
			if (API.getCurrentUser() != null) {
				p.cancel();
				success = b;
			}

		}
	}

	public static class AdminLoginTask extends AsyncTask<String, Void, Boolean> {

		Dialog p;

		public AdminLoginTask() {
			super();
			p = new Dialog(API.mainActivity);
			p.setContentView(R.layout.progress_dialog);
			TextView text = (TextView) p.findViewById(R.id.descripTextField);
			text.setText("Logging in...");
			ProgressBar bar = (ProgressBar) p.findViewById(R.id.progressBar);
			bar.setIndeterminate(true);
			p.setCanceledOnTouchOutside(false);

		}

		public void onPreExecute() {
			Log.d("LoginManager", "onPreExecute");

			p.show();
		}

		@Override
		protected Boolean doInBackground(String... params) {
			Log.d("LoginManager", "doInBackground");
			String user = params[0];
			String pass = params[1];
			API api = API.getInstance();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (api.createAdminSession(user, pass) == null)
				return false;
			else
				return true;
		}

		public void onPostExecute(Boolean b) {
			if (API.getCurrentUser() != null) {
				p.cancel();
				success = b;
			}

		}
	}

}
