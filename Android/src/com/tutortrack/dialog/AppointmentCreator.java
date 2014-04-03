package com.tutortrack.dialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.tutortrack.R;
import com.tutortrack.api.API;
import com.tutortrack.api.Subject;
import com.tutortrack.api.User;
import com.tutortrack.api.student.TutorBlock;

public class AppointmentCreator extends Activity {

	public class MakeAppointmentTask extends AsyncTask<Void, Void, Void> {
		
		private Context _context;
		private ProgressDialog d;

		public MakeAppointmentTask(Context c) {
			_context = c;
			d = ProgressDialog.show(_context, "", "Making appointment...", true, false);
		}
		
		public void onPreExecute() {
			d.show();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			User tutor = API.getInstance().getTutor(block.getTutor().getName());
			if (tutor == null) {
				this.cancel(true);
				return null;
			} else {
				
			}
			return null;
		}
		
		public void onPostExecute(Void result) {
			d.cancel();
			Intent i = new Intent();
			i.putExtra("appointment_date", apptDate);
			i.putExtra("appointment_time", apptTime);
			setResult(RESULT_OK, i);
			finish();
		}

	}

	private TutorBlock block;
	private TextView tutorNameLbl;
	private LinearLayout subjectCheckBoxLayout;
	private DatePicker dateCalendar;
	private TimePicker timePicker;
	private Button ok, cancel;
	private ArrayList<CheckBox> subjectCheckBoxes = new ArrayList<CheckBox>();
	
	private Calendar apptDate;
	private Calendar apptTime;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.make_appointment_dialog);
		
		block = (TutorBlock) getIntent().getSerializableExtra("data");
		tutorNameLbl = (TextView) findViewById(R.id.tutorNameLbl);
		tutorNameLbl.setText(block.getTutor().getName());
		
		subjectCheckBoxLayout = (LinearLayout) findViewById(R.id.subjectCheckBoxLayout);
		dateCalendar = (DatePicker) findViewById(R.id.datePicker);
		
		dateCalendar.setMinDate(block.getStartDate().getTimeInMillis());
		dateCalendar.setMaxDate(block.getEndDate().getTimeInMillis());
		dateCalendar.setCalendarViewShown(true);
		
		timePicker = (TimePicker) findViewById(R.id.timePicker);
		timePicker.setIs24HourView(false);
		
		apptDate = new GregorianCalendar();
		apptTime = new GregorianCalendar();
		
		for (Subject s : block.getSubjects()) {
			CheckBox feature1 = new CheckBox(this);
			feature1.setText(s.toString());
			subjectCheckBoxes.add(feature1);
			subjectCheckBoxLayout.addView(feature1);
		}
		
		cancel = (Button) findViewById(R.id.button_cancel);
		ok = (Button) findViewById(R.id.button_create);
		
		cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
				
			}
		});
		
		ok.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new MakeAppointmentTask(AppointmentCreator.this).execute();
				
			}
		});
		
		
		
		
	}

}
