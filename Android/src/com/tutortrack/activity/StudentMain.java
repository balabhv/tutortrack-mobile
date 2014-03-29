package com.tutortrack.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
	
	public void onResume() {
		super.onResume();

		// Initialize action bar customization for API >= 11
		if (android.os.Build.VERSION.SDK_INT >= 11) {
			ActionBar bar = getActionBar();

			// make the actionbar clickable
			Drawable logo = this.getResources().getDrawable(R.drawable.logo);
			bar.setLogo(this.resize(logo));
			bar.setDisplayUseLogoEnabled(true);
			bar.setDisplayShowTitleEnabled(false);
		}
		
	}

	private Drawable resize(Drawable image) {

		final TypedArray styledAttributes = getBaseContext().getTheme()
				.obtainStyledAttributes(
						new int[] { android.R.attr.actionBarSize });
		int mActionBarSize = (int) styledAttributes.getDimension(0, 0);
		styledAttributes.recycle();

		Bitmap b = ((BitmapDrawable) image).getBitmap();
		Bitmap bitmapResized = Bitmap.createScaledBitmap(b, b.getWidth(),
				mActionBarSize, false);
		return new BitmapDrawable(getResources(), bitmapResized);
	}

}
