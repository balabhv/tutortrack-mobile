package com.tutortrack.api.student;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tutortrack.R;
import com.tutortrack.activity.TutorBrowser;
import com.tutortrack.api.API;
import com.tutortrack.dialog.MakeAppointmentLongpressConfirmationDialog;

public class TutorBlockAdapter extends BaseAdapter {

	private final List<TutorBlock> mItems = new ArrayList<TutorBlock>();

	private final Context mContext;

	public TutorBlockAdapter(Context context) {

		mContext = context;

	}


	@Override
	public int getCount() {
		return mItems.size();
	}

	@Override
	public TutorBlock getItem(int arg0) {
		return mItems.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {

		final TutorBlock block = getItem(arg0);
		
		LayoutInflater Li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View Cv = Li.inflate(R.layout.tutor_block,null);
		final LinearLayout itemLayout = (LinearLayout) Cv;
		
		final TextView nameView = (TextView) itemLayout.findViewById(R.id.tutor_block_name);
		nameView.setText(block.getTutor().getName());
		
		final TextView subjectView = (TextView) itemLayout.findViewById(R.id.block_subjects);
		subjectView.setText(block.getSubjectString());
		
		final TextView dateTimeLocView = (TextView) itemLayout.findViewById(R.id.block_when);
		dateTimeLocView.setText(block.getWhenWhereString());
		
		Cv.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				Intent i = new Intent(mContext, MakeAppointmentLongpressConfirmationDialog.class);
				i.putExtra("data", block);
				((Activity) mContext).startActivityForResult(i, TutorBrowser.APPOINTMENT_REQUESTED);
				return true;
			}
		});
		
		return itemLayout;
	}
	
	public void loadTutors(String loc, String sub) {
		mItems.clear();
		new loadTutorsFromWebTask(this.mContext).execute(loc,sub);
	}
	
	public class loadTutorsFromWebTask extends AsyncTask<String,Void,Void> {
		
		private ArrayList<TutorBlock> tempList;
		Context _context;
		ProgressDialog p;
		
		public loadTutorsFromWebTask(Context c) {
			super();
			p = ProgressDialog.show(c, "", "Loading...", true, false);
			tempList = new ArrayList<TutorBlock>();
			_context = c;
		}
		
		public void onPreExecute() {
			p.show();
		}

		@Override
		protected Void doInBackground(String... arg0) {
			
			String loc = "";
			String sub = "";
			
			if (arg0.length == 2) {
				loc = arg0[0];
				sub = arg0[1];
			}
			
			List<TutorBlock> res = API.getInstance().searchTutors(API.locationFromString(loc), sub);
			tempList.addAll(res);
			
			return null;
			
		}
		
		public void onPostExecute(Void res) {
			mItems.addAll(tempList);
			TutorBlockAdapter.this.notifyDataSetChanged();
			p.cancel();
			
		}
		
		
	}

}
