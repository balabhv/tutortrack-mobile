package com.tutortrack.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.tutortrack.R;
import com.tutortrack.api.student.TutorBlockAdapter;

public class TutorBrowser extends Activity {
	
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
				startActivity(new Intent(getApplicationContext(), FilterCreator.class));
				
			}
		});
	}

}
