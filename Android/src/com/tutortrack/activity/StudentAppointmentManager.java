package com.tutortrack.activity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tutortrack.R;
import com.tutortrack.api.API;
import com.tutortrack.api.student.StudentAppointment;
import com.tutortrack.api.student.StudentAppointmentQueue;
import com.tutortrack.api.student.TutorBlock;
import com.tutortrack.dialog.AppointmentEditor;

public class StudentAppointmentManager extends Activity {

	public static StudentAppointmentQueue queue;

	private static LinearLayout scrollQueue;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manage_appointments_view);

		scrollQueue = (LinearLayout) findViewById(R.id.scrollqueue);

		queue = new StudentAppointmentQueue(this.getApplicationContext());

		fillScrollQueue();

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

		this.fillQueueFromDatastore();

	}

	public void fillQueueFromDatastore() {
		new loadAppointmentsFromWebTask(this).execute();
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

	private void fillScrollQueue() {

		if (scrollQueue.getChildCount() > 0)
			scrollQueue.removeAllViews();

		for (final StudentAppointment ds : queue.mirrorQueue)
			addViewToScrollQueue(ds);

	}

	@Override
	public void onBackPressed() {
		Intent setIntent = new Intent(this, StudentMain.class);
		setIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		setIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(setIntent);
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
		data.setId(ds.APTMT_ID);
		data.setContentDescription("" + ds.APTMT_ID);

		data.setOnLongClickListener(new OnLongClickListener() {

			public boolean onLongClick(View v) {

				return false;
			}

		});

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle("Context Menu");
		menu.add(0, v.getId(), 0, "Edit Appointment");
		menu.add(0, v.getId(), 0, "Cancel Appointment");
	}

	public void deleteViewFromQueue(int id) {
		StudentAppointment ds = queue.removeItemWithKey(id);
		new cancelAppointmentTask(this).execute(ds);

	}

	public boolean onContextItemSelected(MenuItem item) {

		if (item.getTitle().equals("Edit Appointment")) {
			this.editAppointment(item.getItemId());
		} else {
			this.deleteViewFromQueue(item.getItemId());
		}

		return false;

	}

	private void editAppointment(int itemId) {
		LinkedList<StudentAppointment> backup = new LinkedList<StudentAppointment>();
		StudentAppointment apptToEdit = null;
		backup.addAll(queue.aptmtQueue);
		for (StudentAppointment ds : backup) {
			if (ds.APTMT_ID == itemId) {
				apptToEdit = ds;
				break;
			}
		}
		new getTutorBlockTask(this).execute(apptToEdit);
	}

	public class loadAppointmentsFromWebTask extends
			AsyncTask<String, Void, Void> {

		private ArrayList<StudentAppointment> tempList;
		Context _context;
		ProgressDialog p;

		public loadAppointmentsFromWebTask(Context c) {
			super();
			p = ProgressDialog.show(c, "", "Loading...", true, false);
			tempList = new ArrayList<StudentAppointment>();
			_context = c;
		}

		public void onPreExecute() {
			p.show();
		}

		@Override
		protected Void doInBackground(String... arg0) {

			List<StudentAppointment> res = API.getInstance()
					.getAppointmentsForStudent(API.getCurrentUser());
			System.out.println("res.size() = " + res.size());
			tempList.addAll(res);

			return null;

		}

		public void onPostExecute(Void res) {
			queue.aptmtQueue.clear();
			queue.mirrorQueue.clear();
			for (StudentAppointment tempAppt : tempList) {
				queue.addDataSetToQueue(tempAppt);
			}
			fillScrollQueue();
			p.cancel();

		}

	}

	public class cancelAppointmentTask extends
			AsyncTask<StudentAppointment, Void, Void> {

		Context _context;
		ProgressDialog p;

		public cancelAppointmentTask(Context c) {
			super();
			p = ProgressDialog.show(c, "", "Canceling Appointment...", true,
					false);
			_context = c;
		}

		public void onPreExecute() {
			p.show();
		}

		@Override
		protected Void doInBackground(StudentAppointment... arg0) {

			API.getInstance().cancelStudentAppointment(arg0[0]);

			return null;

		}

		public void onPostExecute(Void res) {
			p.cancel();
			fillScrollQueue();

		}

	}

	public class getTutorBlockTask extends
			AsyncTask<StudentAppointment, Void, Void> {

		Context _context;
		ProgressDialog p;
		TutorBlock block;
		StudentAppointment orig;

		public getTutorBlockTask(Context c) {
			super();
			p = ProgressDialog.show(c, "", "Opening Appointment Editor...",
					true, false);
			_context = c;
		}

		public void onPreExecute() {
			p.show();
		}

		@Override
		protected Void doInBackground(StudentAppointment... arg0) {
			orig = arg0[0];
			block = API.getInstance().getTutorBlockForTutor(orig.getWithWho());

			return null;

		}

		public void onPostExecute(Void res) {
			p.cancel();
			Intent i = new Intent(_context, AppointmentEditor.class);
			i.putExtra("original", orig);
			i.putExtra("block", block);
			startActivity(i);

		}

	}

}
