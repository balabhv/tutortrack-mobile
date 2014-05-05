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
				// TODO Auto-generated method stub refer tutor management make object Serializable
				// View existing timeslot, create timeslot, reschedule/delete timesslot on longpress
	startActivity(new Intent(getApplicationContext(), TimeslotViewer.class));
	
				
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
