package com.tutortrack.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tutortrack.R;
import com.tutortrack.dialog.LoginDialog;

public class MainActivity extends Activity {

	public static final String KEY_LOGIN = "LOGIN_TYPE";
	public static final String KEY_STUDENT = "STUDENT";
	public static final String KEY_TUTOR = "TUTOR";
	public static final String KEY_ADMIN = "ADMIN";
	public static final String PREFS_TAG = "PREFS";

	public static final int LOGIN_REQUESTED = 1;

	private Button studentLogin, tutorLogin, adminLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		studentLogin = (Button) findViewById(R.id.student_login_button);
		tutorLogin = (Button) findViewById(R.id.tutor_login_button);
		adminLogin = (Button) findViewById(R.id.admin_login_button);

		studentLogin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(),
						LoginDialog.class);
				i.putExtra(KEY_LOGIN, KEY_STUDENT);
				startActivityForResult(i, LOGIN_REQUESTED);

			}
		});

		tutorLogin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(),
						LoginDialog.class);
				i.putExtra(KEY_LOGIN, KEY_TUTOR);
				startActivityForResult(i, LOGIN_REQUESTED);

			}
		});

		adminLogin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(),
						LoginDialog.class);
				i.putExtra(KEY_LOGIN, KEY_ADMIN);
				startActivityForResult(i, LOGIN_REQUESTED);

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == LOGIN_REQUESTED && resultCode == RESULT_OK) {
			String key = data.getStringExtra(KEY_LOGIN);
			if (key.equals(KEY_STUDENT)) {
				startActivity(new Intent(getApplicationContext(),
						StudentMain.class));
			} else if (key.equals(KEY_TUTOR)) {
				startActivity(new Intent(getApplicationContext(),
						TutorMain.class));
			} else if (key.equals(KEY_ADMIN)) {

			} else {
				// shouldn't get here - invalid key
				Toast.makeText(getApplicationContext(), "Login error.",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

}
