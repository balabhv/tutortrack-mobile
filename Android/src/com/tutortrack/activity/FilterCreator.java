package com.tutortrack.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tutortrack.R;

public class FilterCreator extends Activity {
	
	private Spinner filterType;
	private Button addFilter, ok, cancel;
	private LinearLayout filterScroll;
	private ScrollView filterScrollHolder;
	
	private static final int FILTER_TYPE_LOCATION = 0;
	private static final int FILTER_TYPE_SUBJECT = 1;
	private int locationCount = 0;
	private int subjectCount = 0;
	
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
				
			}
		});
		
		
				
	}
	
	protected void applyFilters() {
		// TODO Auto-generated method stub
		
	}

	protected void addFilter() {
		int pos = filterType.getSelectedItemPosition();
		
		if (pos == FilterCreator.FILTER_TYPE_LOCATION) {
			if (this.locationCount == 0)
				locationCount++;
			else
				Toast.makeText(getApplicationContext(), "One Location Filter Only", Toast.LENGTH_SHORT).show();
		} else if (pos == FilterCreator.FILTER_TYPE_SUBJECT) {
			if (this.subjectCount == 0)
				this.subjectCount++;
			else
				Toast.makeText(getApplicationContext(), "One Subject Filter Only", Toast.LENGTH_SHORT).show();
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
			String[] myResArray = getResources().getStringArray(R.array.location_array);
			ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, myResArray);
			s.setAdapter(spinnerArrayAdapter);
		} else {
			t.setText("Subject is:");
			String[] myResArray = getResources().getStringArray(R.array.test_subject_array);
			ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, myResArray);
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
