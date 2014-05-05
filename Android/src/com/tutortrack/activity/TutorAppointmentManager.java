package com.tutortrack.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tutortrack.R;
import com.tutortrack.api.tutor.TutorAppointment;
import com.tutortrack.api.tutor.TutorAppointmentQueue;

public class TutorAppointmentManager extends Activity {

	public static TutorAppointmentQueue queue;

	private static LinearLayout scrollQueue;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manage_appointments_view);

		scrollQueue = (LinearLayout) findViewById(R.id.scrollqueue);

		queue = new TutorAppointmentQueue(this.getApplicationContext());

		boolean success = queue.buildQueueFromFile();

		if (!success) {
			Log.e("ERROR", "Error building queue from file");
		}

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

		for (final TutorAppointment ds : queue.mirrorQueue)
			addViewToScrollQueue(ds);

	}

	// Fills the text fields in the list element blocks
	private void makeBlock(View view, TutorAppointment ds) {
		TextView tv = (TextView) view.findViewById(R.id.student_name);
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

	private void addViewToScrollQueue(final TutorAppointment ds) {

		String previous = "";
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		layoutParams.setMargins(5, 5, 5, 5);

		final View data = View.inflate(getApplicationContext(),
				R.layout.tutor_appointment_block, null);

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
