package com.tutortrack.activity;

import java.util.Arrays;

import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tutortrack.R;
import com.tutortrack.api.API;
import com.tutortrack.api.API.Location;

public class FilterCreator extends Activity {

	private Spinner filterType;
	private Button addFilter, ok, cancel;
	private LinearLayout filterScroll;
	private ScrollView filterScrollHolder;

	private static final int FILTER_TYPE_LOCATION = 0;
	private static final int FILTER_TYPE_SUBJECT = 1;
	public static final String SHARED_PREFERENCE_FILTERS = "filters";
	private int locationCount = 0;
	private int subjectCount = 0;
	private SharedPreferences prefs = getSharedPreferences(
			SHARED_PREFERENCE_FILTERS, 0);

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filter_create);

		filterType = (Spinner) this.findViewById(R.id.filter_type_spinner);
		addFilter = (Button) findViewById(R.id.add_filter_button);
		ok = (Button) findViewById(R.id.filters_ok);
		cancel = (Button) findViewById(R.id.filters_cancel);
		filterScroll = (LinearLayout) findViewById(R.id.filters_view);
		filterScrollHolder = (ScrollView) findViewById(R.id.filters_scroll);

		cancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();

			}
		});

		addFilter.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				addFilter();

			}
		});

		ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				applyFilters();
				setResult(RESULT_OK);
				finish();
			}
		});

		loadFiltersFromPrefs();

	}

	private void loadFiltersFromPrefs() {
		int filter_count = prefs.getInt("filter_count", 0);
		String filter_dump = prefs.getString("filters",
				(new JSONObject()).toString());

		try {
			JSONObject filterObj = new JSONObject(filter_dump);
			switch (filter_count) {
			case 0:
				return;
			case 1:
				if ((filterObj.has("location"))
						&& (filterObj.getString("location") != null)) {
					String loc = filterObj.getString("location");
					Location l = API.locationFromString(loc);
					this.locationCount = 1;
					this.addFilterOfType(FILTER_TYPE_LOCATION, l);
				} else if ((filterObj.has("subject"))
						&& (filterObj.getString("subject") != null)) {
					String sub = filterObj.getString("subject");
					this.subjectCount = 1;
					this.addFilterOfType(FILTER_TYPE_SUBJECT, sub);
				}
				break;
			case 2:
				String loc = filterObj.getString("location");
				Location l = API.locationFromString(loc);
				this.locationCount = 1;
				this.addFilterOfType(FILTER_TYPE_LOCATION, l);
				String sub = filterObj.getString("subject");
				this.subjectCount = 1;
				this.addFilterOfType(FILTER_TYPE_SUBJECT, sub);
				break;
			default:
				return;
			}
		} catch (Exception e) {

		}

	}

	private void addFilterOfType(int pos, Object l) {
		final View v = View.inflate(this.getApplicationContext(),
				R.layout.filter_element, null);

		v.setTag(pos);

		TextView t = (TextView) v.findViewById(R.id.filter_name);
		Spinner s = (Spinner) v.findViewById(R.id.filter_criteria);
		ImageView x = (ImageView) v.findViewById(R.id.filter_x);

		x.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if (((Integer) v.getTag()).intValue() == FILTER_TYPE_LOCATION) {
					locationCount--;
				} else if (((Integer) v.getTag()).intValue() == FILTER_TYPE_SUBJECT) {
					subjectCount--;
				}

				filterScroll.removeView(v);

			}
		});

		if (pos == FILTER_TYPE_LOCATION) {
			t.setText("Location is:");
			String[] myResArray = getResources().getStringArray(
					R.array.location_array);
			ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
					this, android.R.layout.simple_spinner_dropdown_item,
					myResArray);
			s.setAdapter(spinnerArrayAdapter);
			Location loc = (Location) l;
			s.setSelection(Arrays.asList(myResArray).indexOf(
					API.stringFromLocation(loc)));
		} else {
			t.setText("Subject is:");
			String[] myResArray = getResources().getStringArray(
					R.array.test_subject_array);
			ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
					this, android.R.layout.simple_spinner_dropdown_item,
					myResArray);
			s.setAdapter(spinnerArrayAdapter);
			s.setSelection(Arrays.asList(myResArray).indexOf((String) l));
		}

		s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				TextView selectedText = (TextView) arg0.getChildAt(0);
				if (selectedText != null) {
					selectedText.setTextColor(Color.BLACK);
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				TextView selectedText = (TextView) arg0.getChildAt(0);
				if (selectedText != null) {
					selectedText.setTextColor(Color.BLACK);
				}

			}
		});

		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		layoutParams.setMargins(1, 1, 1, 1);

		filterScroll.addView(v, layoutParams);
		scrollDown();

	}

	protected void applyFilters() {

		int filtercount = filterScroll.getChildCount();
		switch (filtercount) {
		case 0:
			noFilters();
			break;
		case 1:
			oneFilter();
			break;
		case 2:
			twoFilters();
			break;
		default:
			break;
		}

	}

	private void twoFilters() {
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt("filter_count", 2);
		JSONObject obj = new JSONObject();
		for (int i = 0; i < 2; i++) {
			View v = filterScroll.getChildAt(i);
			try {
				if ((Integer) v.getTag() == FILTER_TYPE_LOCATION) {
					Spinner s = (Spinner) v.findViewById(R.id.filter_criteria);
					String str = (String) s.getSelectedItem();
					obj.put("location", str);
					editor.putString("filters", obj.toString());
				} else {
					Spinner s = (Spinner) v.findViewById(R.id.filter_criteria);
					String str = (String) s.getSelectedItem();
					obj.put("subject", str);
					editor.putString("filters", obj.toString());
				}
			} catch (Exception e) {

			}
		}
		editor.commit();
	}

	private void oneFilter() {
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt("filter_count", 1);

		View v = filterScroll.getChildAt(0);
		try {
			if ((Integer) v.getTag() == FILTER_TYPE_LOCATION) {
				Spinner s = (Spinner) v.findViewById(R.id.filter_criteria);
				String str = (String) s.getSelectedItem();
				JSONObject obj = new JSONObject();
				obj.put("location", str);
				editor.putString("filters", obj.toString());
			} else {
				Spinner s = (Spinner) v.findViewById(R.id.filter_criteria);
				String str = (String) s.getSelectedItem();
				JSONObject obj = new JSONObject();
				obj.put("subject", str);
				editor.putString("filters", obj.toString());
			}
		} catch (Exception e) {

		}
		editor.commit();

	}

	private void noFilters() {
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt("filter_count", 0);
		editor.commit();

	}

	protected void addFilter() {
		int pos = filterType.getSelectedItemPosition();

		if (pos == FilterCreator.FILTER_TYPE_LOCATION) {
			if (this.locationCount == 0)
				locationCount++;
			else
				Toast.makeText(getApplicationContext(),
						"One Location Filter Only", Toast.LENGTH_SHORT).show();
		} else if (pos == FilterCreator.FILTER_TYPE_SUBJECT) {
			if (this.subjectCount == 0)
				this.subjectCount++;
			else
				Toast.makeText(getApplicationContext(),
						"One Subject Filter Only", Toast.LENGTH_SHORT).show();
		}

		addFilterOfType(pos);

	}

	private void addFilterOfType(int pos) {
		final View v = View.inflate(this.getApplicationContext(),
				R.layout.filter_element, null);

		v.setTag(pos);

		TextView t = (TextView) v.findViewById(R.id.filter_name);
		Spinner s = (Spinner) v.findViewById(R.id.filter_criteria);
		ImageView x = (ImageView) v.findViewById(R.id.filter_x);

		x.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if (((Integer) v.getTag()).intValue() == FILTER_TYPE_LOCATION) {
					locationCount--;
				} else if (((Integer) v.getTag()).intValue() == FILTER_TYPE_SUBJECT) {
					subjectCount--;
				}

				filterScroll.removeView(v);

			}
		});

		if (pos == FILTER_TYPE_LOCATION) {
			t.setText("Location is:");
			String[] myResArray = getResources().getStringArray(
					R.array.location_array);
			ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
					this, android.R.layout.simple_spinner_dropdown_item,
					myResArray);
			s.setAdapter(spinnerArrayAdapter);
		} else {
			t.setText("Subject is:");
			String[] myResArray = getResources().getStringArray(
					R.array.test_subject_array);
			ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
					this, android.R.layout.simple_spinner_dropdown_item,
					myResArray);
			s.setAdapter(spinnerArrayAdapter);
		}

		s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				TextView selectedText = (TextView) arg0.getChildAt(0);
				if (selectedText != null) {
					selectedText.setTextColor(Color.BLACK);
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				TextView selectedText = (TextView) arg0.getChildAt(0);
				if (selectedText != null) {
					selectedText.setTextColor(Color.BLACK);
				}

			}
		});

		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		layoutParams.setMargins(1, 1, 1, 1);

		filterScroll.addView(v, layoutParams);
		scrollDown();
	}

	private void scrollDown() {
		Thread scrollThread = new Thread() {
			public void run() {
				try {
					sleep(200);
					FilterCreator.this.runOnUiThread(new Runnable() {
						public void run() {
							filterScrollHolder.fullScroll(View.FOCUS_DOWN);
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		scrollThread.start();
	}

}
