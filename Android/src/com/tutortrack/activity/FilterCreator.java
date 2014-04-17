package com.tutortrack.activity;

import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
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
import com.tutortrack.api.Filter;
import com.tutortrack.api.Filter.FilterType;
import com.tutortrack.api.utils.SharedPreferencesExecutor;

public class FilterCreator extends Activity {

	private Spinner filterType;
	private Button addFilter, ok, cancel;
	private LinearLayout filterScroll;
	private ScrollView filterScrollHolder;
	private ArrayList<Filter> filters = new ArrayList<Filter>();
	private SharedPreferencesExecutor<ArrayList<Filter>> saver;
	private SharedPreferences prefs;

	private static final int FILTER_TYPE_LOCATION = 0;
	private static final int FILTER_TYPE_SUBJECT = 1;
	private int locationCount;
	private int subjectCount;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filter_create);

		filterType = (Spinner) this.findViewById(R.id.filter_type_spinner);
		addFilter = (Button) findViewById(R.id.add_filter_button);
		ok = (Button) findViewById(R.id.filters_ok);
		cancel = (Button) findViewById(R.id.filters_cancel);
		filterScroll = (LinearLayout) findViewById(R.id.filters_view);
		filterScrollHolder = (ScrollView) findViewById(R.id.filters_scroll);
		saver = new SharedPreferencesExecutor<ArrayList<Filter>>(
				API.mainActivity.getApplicationContext(), "filters");

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

	}

	public void onResume() {
		super.onResume();
		prefs = this.getSharedPreferences("filter_creator_settings", 0);
		locationCount = prefs.getInt("location_count", 0);
		subjectCount = prefs.getInt("subject_count", 0);
		String json = saver.retreiveJSONString("filters");
		System.out.println(json);
		
		if (!json.equalsIgnoreCase("")) {
			filters = FilterCreator.deserializeJSONString(json);
			this.loadFiltersFromPreferences();
		}
	}

	public static ArrayList<Filter> deserializeJSONString(String json) {
		ArrayList<Filter> temp = new ArrayList<Filter>();
		try {
			JSONArray arr = new JSONArray(json);
			for (int i = 0; i < arr.length(); ++i) {
				Filter f = new Filter();
				JSONObject obj = arr.getJSONObject(i);

				if (obj.getString("type").equalsIgnoreCase("LOCATION")) {
					f.setType(FilterType.LOCATION);
					f.setValue(API.locationFromString(obj.getString("value")));
				} else {
					f.setType(FilterType.SUBJECT);
					f.setValue(obj.getString("value"));
				}

				temp.add(f);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return temp;
	}

	private void addFilterOfType(int pos) {
		final View v = View.inflate(this.getApplicationContext(),
				R.layout.filter_element, null);

		Filter f = new Filter();

		v.setTag(pos);

		TextView t = (TextView) v.findViewById(R.id.filter_name);
		Spinner s = (Spinner) v.findViewById(R.id.filter_criteria);
		ImageView x = (ImageView) v.findViewById(R.id.filter_x);

		x.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if (((Integer) v.getTag()).intValue() == FILTER_TYPE_LOCATION) {
					locationCount = 0;
					SharedPreferences.Editor editor = prefs.edit();
					editor.putInt("location_count", locationCount);
					editor.commit();
				} else if (((Integer) v.getTag()).intValue() == FILTER_TYPE_SUBJECT) {
					subjectCount = 0;
					SharedPreferences.Editor editor = prefs.edit();
					editor.putInt("subject_count", subjectCount);
					editor.commit();
				}

				filterScroll.removeView(v);
				filters.remove(v.getId());

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
			f.setType(FilterType.LOCATION);
			f.setValue(API.locationFromString(myResArray[0]));
		} else {
			t.setText("Subject is:");
			String[] myResArray = getResources().getStringArray(
					R.array.test_subject_array);
			ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
					this, android.R.layout.simple_spinner_dropdown_item,
					myResArray);
			s.setAdapter(spinnerArrayAdapter);
			f.setType(FilterType.SUBJECT);
			f.setValue(myResArray[0]);
		}

		s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				TextView selectedText = (TextView) arg0.getChildAt(0);
				Filter f = filters.get(v.getId());
				if (f.getType() == FilterType.LOCATION) {
					f.setValue(API.locationFromString(selectedText.getText()
							.toString()));
				} else {
					f.setValue(selectedText.getText().toString());
				}

				filters.set(v.getId(), f);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		layoutParams.setMargins(1, 1, 1, 1);

		filterScroll.addView(v, layoutParams);
		scrollDown();

		filters.add(f);
		int id = filters.indexOf(f);
		v.setId(id);

	}

	protected void applyFilters() {

		saver.save("filters", filters);

	}

	protected void addFilter() {
		int pos = filterType.getSelectedItemPosition();

		if (pos == FilterCreator.FILTER_TYPE_LOCATION) {
			if (this.locationCount == 0) {
				locationCount = 1;
				SharedPreferences.Editor editor = prefs.edit();
				editor.putInt("location_count", locationCount);
				editor.commit();

			} else
				Toast.makeText(getApplicationContext(),
						"One Location Filter Only", Toast.LENGTH_SHORT).show();
			return;
		} else if (pos == FilterCreator.FILTER_TYPE_SUBJECT) {
			if (this.subjectCount == 0) {
				subjectCount = 1;
				SharedPreferences.Editor editor = prefs.edit();
				editor.putInt("subject_count", subjectCount);
				editor.commit();
			} else
				Toast.makeText(getApplicationContext(),
						"One Subject Filter Only", Toast.LENGTH_SHORT).show();
			return;
		}

		addFilterOfType(pos);

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

	private void loadFiltersFromPreferences() {

		if (filters == null)
			filters = new ArrayList<Filter>();
		for (int i = 0; i < filters.size(); i++) {
			addFilterOfTypeFromPrefs(i);
		}
	}

	private void addFilterOfTypeFromPrefs(final int pos) {
		final View v = View.inflate(this.getApplicationContext(),
				R.layout.filter_element, null);

		Filter f = filters.get(pos);

		v.setTag((f.getType() == FilterType.LOCATION) ? FILTER_TYPE_LOCATION
				: FILTER_TYPE_SUBJECT);
		v.setId(pos);

		TextView t = (TextView) v.findViewById(R.id.filter_name);
		Spinner s = (Spinner) v.findViewById(R.id.filter_criteria);
		ImageView x = (ImageView) v.findViewById(R.id.filter_x);

		x.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if (((Integer) v.getTag()).intValue() == FILTER_TYPE_LOCATION) {
					locationCount--;
					SharedPreferences.Editor editor = prefs.edit();
					editor.putInt("location_count", locationCount);
					editor.commit();
				} else if (((Integer) v.getTag()).intValue() == FILTER_TYPE_SUBJECT) {
					subjectCount--;
					SharedPreferences.Editor editor = prefs.edit();
					editor.putInt("subject_count", subjectCount);
					editor.commit();
				}

				filterScroll.removeView(v);
				filters.remove(pos);

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
			Location l = (Location) f.getValue();
			s.setSelection(Arrays.asList(myResArray).indexOf(
					API.stringFromLocation(l)));
		} else {
			t.setText("Subject is:");
			String[] myResArray = getResources().getStringArray(
					R.array.test_subject_array);
			ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
					this, android.R.layout.simple_spinner_dropdown_item,
					myResArray);
			s.setAdapter(spinnerArrayAdapter);
			String str = (String) f.getValue();
			s.setSelection(Arrays.asList(myResArray).indexOf(str));
		}

		s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				TextView selectedText = (TextView) arg0.getChildAt(0);
				Filter f = filters.get(v.getId());
				if (f.getType() == FilterType.LOCATION) {
					f.setValue(API.locationFromString(selectedText.getText()
							.toString()));
				} else {
					f.setValue(selectedText.getText().toString());
				}

				filters.set(pos, f);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		layoutParams.setMargins(1, 1, 1, 1);

		filterScroll.addView(v, layoutParams);
		scrollDown();

	}

}
