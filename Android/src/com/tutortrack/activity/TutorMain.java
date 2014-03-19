package com.tutortrack.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tutortrack.R;
import com.tutortrack.api.API;

public class TutorMain extends Activity {
	
	private Button editTimeStampsButton, manageAppointmentsButton;
	private TextView logged_in_as;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tutor_main);
		
		logged_in_as = (TextView) findViewById(R.id.tutor_login_status);
		
		editTimeStampsButton = (Button) findViewById(R.id.edit_timeslot_button);
		manageAppointmentsButton = (Button) findViewById(R.id.manage_appointments_button);
		
		editTimeStampsButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		manageAppointmentsButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), TutorAppointmentManager.class));
				
			}
		});
		
		logged_in_as.setText("Logged in as: " + API.getCurrentUser().getName());
		
		
		
		
	}

}
