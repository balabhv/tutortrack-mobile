package com.tutortrack.dialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tutortrack.R;
import com.tutortrack.activity.MainActivity;
import com.tutortrack.api.API;

public class LoginDialog extends Activity {

	/* Temporary hardcoded usernames and passwords until Madhu finishes API */
	public static final String STUDENT_EMAIL = "mobile.fake@example.com";
	public static final String STUDENT_PASS = "mobile";
	public static final String TUTOR_EMAIL = "tutor.fake@example.com";
	public static final String TUTOR_PASS = "tutor";
	public static final String ADMIN_EMAIL = "admin.fake@example.com";
	public static final String ADMIN_PASS = "admin";

	private String key;
	private Button login, cancel;
	private EditText emailField, passField;

	public void onCreate(Bundle b) {
		super.onCreate(b);
		setContentView(R.layout.login_dialog);

		Bundle b2 = this.getIntent().getExtras();
		key = b2.getString(MainActivity.KEY_LOGIN);

		login = (Button) findViewById(R.id.button_ok);
		cancel = (Button) findViewById(R.id.button_cancel);

		emailField = (EditText) findViewById(R.id.edittext_username);
		passField = (EditText) findViewById(R.id.edittext_password);

		login.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				login(emailField.getText().toString(), passField.getText()
						.toString());
			}
		});

		cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();

			}
		});
	}

	public void login(String user, String pass) {
		if (key.equals(MainActivity.KEY_STUDENT)) {
			new StudentLoginTask(this).execute(user,pass);
		} else if (key.equals(MainActivity.KEY_TUTOR)) {
			new TutorLoginTask(this).execute(user,pass);
		} else if (key.equals(MainActivity.KEY_ADMIN)) {
			new AdminLoginTask(this).execute(user,pass);
		} else {
			// shouldn't get here
		}
	}

	public class StudentLoginTask extends AsyncTask<String, Void, Boolean> {

		ProgressDialog p;
		Context _context;

		public StudentLoginTask(Context c) {
			super();
			p = ProgressDialog.show(c, "", "Logging in...", true, false);
			_context = c;

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
				e.printStackTrace();
			}
			if (api.createStudentSession(user, pass) == null)
				return false;
			else
				return true;
		}

		public void onPostExecute(Boolean b) {
			p.cancel();
			if (b) {
				Toast.makeText(_context, "Login successful!",
						Toast.LENGTH_SHORT).show();
				Intent data = new Intent();
				data.putExtra(MainActivity.KEY_LOGIN, key);
				setResult(RESULT_OK, data);
			} else {
				Toast.makeText(_context, "Login failed!", Toast.LENGTH_SHORT)
						.show();
				setResult(RESULT_CANCELED);
			}
			finish();
		}
	}

	public class TutorLoginTask extends AsyncTask<String, Void, Boolean> {

		ProgressDialog p;
		private Context _context;

		public TutorLoginTask(Context c) {
			super();
			p = ProgressDialog.show(c, "", "Logging in...", true, false);
			_context = c;

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
				e.printStackTrace();
			}
			if (api.createTutorSession(user, pass) == null)
				return false;
			else
				return true;
		}

		public void onPostExecute(Boolean b) {
			p.cancel();
			if (b) {
				Toast.makeText(_context, "Login successful!",
						Toast.LENGTH_SHORT).show();
				Intent data = new Intent();
				data.putExtra(MainActivity.KEY_LOGIN, key);
				setResult(RESULT_OK, data);
			} else {
				Toast.makeText(_context, "Login failed!", Toast.LENGTH_SHORT)
						.show();
				setResult(RESULT_CANCELED);
			}
			finish();
		}
	}

	public class AdminLoginTask extends AsyncTask<String, Void, Boolean> {

		ProgressDialog p;
		private Context _context;

		public AdminLoginTask(Context c) {
			super();
			p = ProgressDialog.show(c, "", "Logging in...", true, false);
			_context = c;

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
				e.printStackTrace();
			}
			if (api.createAdminSession(user, pass) == null)
				return false;
			else
				return true;
		}

		public void onPostExecute(Boolean b) {
			p.cancel();
			if (b) {
				Toast.makeText(_context, "Login successful!",
						Toast.LENGTH_SHORT).show();
				Intent data = new Intent();
				data.putExtra(MainActivity.KEY_LOGIN, key);
				setResult(RESULT_OK, data);
			} else {
				Toast.makeText(_context, "Login failed!", Toast.LENGTH_SHORT)
						.show();
				setResult(RESULT_CANCELED);
			}
			finish();
		}
	}

}
