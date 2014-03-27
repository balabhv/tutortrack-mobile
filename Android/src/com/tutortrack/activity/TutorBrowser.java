package com.tutortrack.activity;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.tutortrack.R;
import com.tutortrack.api.student.TutorBlockAdapter;

public class TutorBrowser extends Activity {
	
	private static final int FILTERS_REQUESTED = 1;
	private ListView list;
	private TutorBlockAdapter adapter;
	private Button filterButton;
	
	public void onCreate(Bundle b) {
		super.onCreate(b);
		setContentView(R.layout.tutor_browser);
		
		adapter = new TutorBlockAdapter(getApplicationContext(), this);
		list = (ListView) findViewById(R.id.listView1);
		filterButton = (Button) findViewById(R.id.filterButton);
		
		list.setAdapter(adapter);
		
		filterButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(getApplicationContext(), FilterCreator.class), FILTERS_REQUESTED);
				
			}
		});
		
		loadPrefs();
	}
	
	public void onActivityResult(int reqCode, int resCode, Intent data) {
		if (reqCode == FILTERS_REQUESTED && resCode == RESULT_OK) {
			loadPrefs();
		}
	}

	private void loadPrefs() {
		SharedPreferences prefs = getSharedPreferences(
				FilterCreator.SHARED_PREFERENCE_FILTERS, 0);
		int filter_count = prefs.getInt("filter_count", 0);
		String filter_dump = prefs.getString("filters",
				(new JSONObject()).toString());

		try {
			JSONObject filterObj = new JSONObject(filter_dump);
			switch (filter_count) {
			case 0:
				adapter.loadTutors("", "");
			case 1:
				if ((filterObj.has("location"))
						&& (filterObj.getString("location") != null)) {
					String loc = filterObj.getString("location");
					adapter.loadTutors(loc, "");
				} else if ((filterObj.has("subject"))
						&& (filterObj.getString("subject") != null)) {
					String sub = filterObj.getString("subject");
					adapter.loadTutors("", sub);
				}
				break;
			case 2:
				String loc = filterObj.getString("location");
				String sub = filterObj.getString("subject");
				adapter.loadTutors(loc, sub);
				break;
			default:
				adapter.loadTutors("", "");
			}
		} catch (Exception e) {

		}

		
	}

}
