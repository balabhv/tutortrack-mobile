package com.tutortrack.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tutortrack.R;
import com.tutortrack.api.API.Location;
import com.tutortrack.api.Subject;
import com.tutortrack.api.User;
import com.tutortrack.api.User.UserType;
import com.tutortrack.api.student.StudentAppointment;
import com.tutortrack.api.student.StudentAppointmentQueue;

public class StudentAppointmentManager extends Activity {

	public static StudentAppointmentQueue queue;

	private static LinearLayout scrollQueue;
	private Button add;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manage_appointments_view);

		scrollQueue = (LinearLayout) findViewById(R.id.scrollqueue);

		queue = new StudentAppointmentQueue(this.getApplicationContext());

		boolean success = queue.buildQueueFromFile();

		if (!success) {
			Log.e("ERROR", "Error building queue from file");
		}

		fillScrollQueue();

		add = (Button) findViewById(R.id.button_add);

		add.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				User tempU = new User();
				tempU.setName("Bob Jones");
				tempU.setEmail("bob_jones@student.uml.edu");
				tempU.setType(UserType.STUDENT);
				Calendar t = new GregorianCalendar(TimeZone.getDefault());
				t.set(2014, 5, 21, 19, 30, 0);
				Subject a = new Subject("Circuit Theory I");
				Subject b = new Subject("Circuit Theory II");
				Subject c = new Subject("Computing I");
				Subject d = new Subject("Computing II");
				Subject e = new Subject("Computing III");
				Subject f = new Subject("Computing IV");
				ArrayList<Subject> s = new ArrayList<Subject>();
				s.add(a);
				s.add(b);
				s.add(c);
				s.add(d);
				s.add(e);
				s.add(f);
				StudentAppointment tempAppt = new StudentAppointment(t, s, tempU,
						Location.EAST);
				queue.addDataSetToQueue(tempAppt);
				fillScrollQueue();
			}
		});

	}

	private void fillScrollQueue() {

		if (scrollQueue.getChildCount() > 0)
			scrollQueue.removeAllViews();

		for (final StudentAppointment ds : queue.mirrorQueue)
			addViewToScrollQueue(ds);

	}

	// Fills the text fields in the list element blocks
	private void makeBlock(View view, StudentAppointment ds) {
		TextView tv = (TextView) view.findViewById(R.id.tutor_name);
		tv.setText(ds.getWithWho().getName());

		TextView desc = (TextView) view.findViewById(R.id.subjects);
		desc.setText(ds.getSubjectString());

		TextView on = (TextView) view.findViewById(R.id.when);
		on.setText(ds.getWhenWhereString());
	}

	private String checkPrevious(String previous, LinearLayout scrollQueue,
			String ds) {

		LinearLayout space = new LinearLayout(getApplicationContext());
		space.setPadding(0, 10, 0, 10);

		if ((!previous.equals(ds)) && (!previous.equals("")))
			scrollQueue.addView(space);

		return ds;
	}

	private void addViewToScrollQueue(final StudentAppointment ds) {

		String previous = "";
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		layoutParams.setMargins(5, 5, 5, 5);

		final View data = View.inflate(getApplicationContext(),
				R.layout.student_appointment_block, null);

		this.registerForContextMenu(data);

		makeBlock(data, ds);
		previous = checkPrevious(previous, scrollQueue, ds.getWithWho()
				.getName());

		scrollQueue.addView(data, layoutParams);
		data.setContentDescription("" + ds.APTMT_ID);

		data.setOnLongClickListener(new OnLongClickListener() {

			public boolean onLongClick(View v) {

				return false;
			}

		});

	}

	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.appointment_context_menu, menu);
	}

}
