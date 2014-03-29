package com.tutortrack.dialog;

import com.tutortrack.R;
import com.tutortrack.api.student.TutorBlock;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MakeAppointmentDialog extends Activity {
	
	private Button ok, cancel;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.make_appointment_longpress_dialog);
		
		ok = (Button) findViewById(R.id.button_ok);
		cancel = (Button) findViewById(R.id.button_cancel);
		
		
		
		ok.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent data = new Intent();
				data.putExtra("data", (TutorBlock) getIntent().getSerializableExtra("data"));
				setResult(RESULT_OK, data);
				finish();
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

}
