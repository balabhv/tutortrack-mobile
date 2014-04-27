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
import com.tutortrack.api.student.StudentAppointment;
import com.tutortrack.api.student.TutorBlock;

public class AppointmentEditor extends Activity {

	public class EditAppointmentTask extends AsyncTask<Void, Void, Void> {

		private Context _context;
		private ProgressDialog d;
		private boolean success;

		public EditAppointmentTask(Context c) {
			_context = c;
			d = ProgressDialog.show(_context, "", "Editing appointment...",
					true, false);
		}

		public void onPreExecute() {
			d.show();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			success = API.getInstance().editStudentAppointment(orig, edited);
			return null;
		}

		public void onPostExecute(Void result) {
			d.cancel();
			if (success) {
				Intent i = new Intent();
				i.putExtra("StudentAppointment", edited);
				setResult(RESULT_OK, i);
			} else {
				setResult(RESULT_CANCELED);
			}
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
	private ArrayList<Subject> subj;
	private StudentAppointment orig, edited;

	private Calendar apptDate;
	private Calendar apptTime;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.make_appointment_dialog);

		orig = (StudentAppointment) getIntent()
				.getSerializableExtra("original");
		block = (TutorBlock) getIntent().getSerializableExtra("block");
		tutorNameLbl = (TextView) findViewById(R.id.tutorNameLbl);
		tutorNameLbl.setText(orig.getWithWho().getName());

		subjectCheckBoxLayout = (LinearLayout) findViewById(R.id.subjectCheckBoxLayout);
		dateCalendar = (DatePicker) findViewById(R.id.datePicker);
		dateCalendar.setMinDate(block.getStartDate().getTimeInMillis());
		dateCalendar.setMaxDate(block.getEndDate().getTimeInMillis());
		dateCalendar.setCalendarViewShown(true);
		dateCalendar.updateDate(orig.getWhen().get(Calendar.YEAR), orig
				.getWhen().get(Calendar.MONTH),
				orig.getWhen().get(Calendar.DATE));

		timePicker = (TimePicker) findViewById(R.id.timePicker);
		timePicker.setIs24HourView(false);
		timePicker.setCurrentHour(orig.getWhen().get(Calendar.HOUR_OF_DAY));
		timePicker.setCurrentMinute(orig.getWhen().get(Calendar.MINUTE));
		

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
				apptDate.set(dateCalendar.getYear(), dateCalendar.getMonth()+1,
						dateCalendar.getDayOfMonth());
				apptTime.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
				apptTime.set(Calendar.MINUTE, timePicker.getCurrentMinute());
				subj = new ArrayList<Subject>();

				for (int i = 0; i < subjectCheckBoxes.size(); ++i) {
					CheckBox b = subjectCheckBoxes.get(i);
					if (b.isChecked()) {
						subj.add(block.getSubjects().get(i));
					}
				}
				/*
				 * if (apptTime.before(block.getStartTime()) ||
				 * apptTime.after(block.getEndTime())) {
				 * Toast.makeText(AppointmentCreator.this,
				 * "Selected time is outside of the selected timeslot",
				 * Toast.LENGTH_SHORT).show(); return; }
				 */
				Calendar when = new GregorianCalendar();
				when.set(apptDate.get(Calendar.YEAR), apptDate.get(Calendar.MONTH), apptDate.get(Calendar.DATE), apptTime.get(Calendar.HOUR_OF_DAY), apptTime.get(Calendar.MINUTE), apptTime.get(Calendar.SECOND));
				edited = new StudentAppointment(when, subj, orig.getWithWho(), orig.getWhere());
				 new EditAppointmentTask(AppointmentEditor.this).execute();

			}
		});

	}

	private ArrayList<Subject> getCheckedSubjects() {

		ArrayList<Subject> subjects = new ArrayList<Subject>();

		for (CheckBox c : this.subjectCheckBoxes) {
			if (c.isChecked()) {
				subjects.add(new Subject((String) c.getText()));
			}
		}

		return subjects;
	}

}
