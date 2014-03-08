package com.tutortrack.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tutortrack.R;
import com.tutortrack.activity.MainActivity;

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
				if (login(emailField.getText().toString(), passField.getText().toString())) {
					Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(), "Login failed!", Toast.LENGTH_SHORT).show();
				}
				
				finish();
				
			}
		});
		
		cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
	}
	
	public boolean login(String user, String pass) {
		if (key.equals(MainActivity.KEY_STUDENT)) {
			if ((user.equals(STUDENT_EMAIL)) && (pass.equals(STUDENT_PASS)))
				return true;
			return false;
		} else if (key.equals(MainActivity.KEY_TUTOR)) {
			if ((user.equals(TUTOR_EMAIL)) && (pass.equals(TUTOR_PASS)))
				return true;
			return false;
		} else if (key.equals(MainActivity.KEY_ADMIN)) {
			if ((user.equals(ADMIN_EMAIL)) && (pass.equals(ADMIN_PASS)))
				return true;
			return false;
		} else {
			// shouldn't get here
			return false;
		}
	}

}
