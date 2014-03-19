package com.tutortrack.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tutortrack.R;
import com.tutortrack.api.API;

public class StudentMain extends Activity {
	
	private Button viewTutorsButton, manageAppointmentsButton;
	private TextView logged_in_as;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.student_main);
		
		logged_in_as = (TextView) findViewById(R.id.student_login_status);
		
		viewTutorsButton = (Button) findViewById(R.id.view_tutors_button);
		manageAppointmentsButton = (Button) findViewById(R.id.manage_appointments_button2);
		
		viewTutorsButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), TutorBrowser.class));
				
			}
		});
		
		manageAppointmentsButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), StudentAppointmentManager.class));
				
			}
		});
		
		logged_in_as.setText("Logged in as: " + API.getCurrentUser().getName());
		
		
		
		
	}

}
