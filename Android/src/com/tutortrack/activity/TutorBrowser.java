package com.tutortrack.activity;

import java.util.ArrayList;

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
import android.widget.ListView;

import com.tutortrack.R;
import com.tutortrack.api.API;
import com.tutortrack.api.API.Location;
import com.tutortrack.api.Filter;
import com.tutortrack.api.Filter.FilterType;
import com.tutortrack.api.student.TutorBlock;
import com.tutortrack.api.student.TutorBlockAdapter;
import com.tutortrack.api.utils.SharedPreferencesExecutor;
import com.tutortrack.dialog.AppointmentCreator;

public class TutorBrowser extends Activity {

	private static final int FILTERS_REQUESTED = 1;
	public static final int APPOINTMENT_REQUESTED = 2;
	public static final int APPOINTMENT_CREATION_REQUESTED = 3;
	private ListView list;
	private TutorBlockAdapter adapter;
	private Button filterButton;
	private SharedPreferencesExecutor<ArrayList<Filter>> saver;

	public void onCreate(Bundle b) {
		super.onCreate(b);
		setContentView(R.layout.tutor_browser);

		adapter = new TutorBlockAdapter(this);
		list = (ListView) findViewById(R.id.listView1);
		filterButton = (Button) findViewById(R.id.filterButton);
		saver = new SharedPreferencesExecutor<ArrayList<Filter>>(API.mainActivity.getApplicationContext(), "filters");
		

		filterButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(getApplicationContext(),
						FilterCreator.class), FILTERS_REQUESTED);

			}
		});

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
		list.setAdapter(adapter);
		loadFilters();
	}

	private void loadFilters() {
		String json = saver.retreiveJSONString("filters");
		System.out.println(json);
		
		String loc, sub;
		loc = sub = "";
		
		if (!json.equalsIgnoreCase("")) {
			ArrayList<Filter> filters = FilterCreator.deserializeJSONString(json);
			
			if (filters == null)
				filters = new ArrayList<Filter>();
			
			for (int i = 0 ; i < filters.size() ; i++) {
				if (filters.get(i).getType() == FilterType.LOCATION) {
					loc = API.stringFromLocation((Location) filters.get(i).getValue());
				} else {
					sub = (String) filters.get(i).getValue();
				}
			}
		}
		
		adapter.loadTutors(loc, sub);
		
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

	public void onActivityResult(int reqCode, int resCode, Intent data) {
		if (reqCode == FILTERS_REQUESTED && resCode == RESULT_OK) {
			
		} else if (reqCode == APPOINTMENT_REQUESTED && resCode == RESULT_OK) {
			TutorBlock block = (TutorBlock) data.getSerializableExtra("data");
			Intent i = new Intent(this, AppointmentCreator.class);
			i.putExtra("data", block);
			startActivityForResult(i, APPOINTMENT_CREATION_REQUESTED);
		} else if (reqCode == APPOINTMENT_CREATION_REQUESTED && resCode == RESULT_OK) {
			startActivity(new Intent(this, StudentAppointmentManager.class));
		}
	}

	

}
