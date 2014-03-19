package com.tutortrack.api.student;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tutortrack.R;
import com.tutortrack.api.utils.ReadCSV;

public class TutorBlockAdapter extends BaseAdapter {

	private final List<TutorBlock> mItems = new ArrayList<TutorBlock>();

	private final Context mContext;

	public TutorBlockAdapter(Context context, Activity a) {

		mContext = context;
		this.loadData("test.csv", a);

	}

	private void loadData(final String filepath, final Activity parent) {
		mItems.addAll(ReadCSV.read(parent, filepath));
		
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
		
		return itemLayout;
	}

}
