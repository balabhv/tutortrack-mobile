package com.tutortrack.control;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

public class RangeTimePicker extends TimePicker implements OnTimeChangedListener{
	
	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public RangeTimePicker(Context context, AttributeSet attrs, int defStyle) {
		this(context, attrs);
	}
	/**
	 * @param context
	 * @param attrs
	 */
	public RangeTimePicker(Context context, AttributeSet attrs) {
		this(context, attrs, 1, 24, 0, 59);
	}

	private int minHour, maxHour;
	private int minMinute, maxMinute;
	
	private int currentHour = 0;
	private int currentMinute = 0;
	
	

	public RangeTimePicker(Context context, AttributeSet attrs, int minH, int maxH, int minM, int maxM) {
		super(context, attrs);
		this.setOnTimeChangedListener(this);
		minHour = minH;
		maxHour = maxH;
		minMinute = minM;
		maxMinute = maxM;
	}

	public void setMin(int hour, int minute) {
	    minHour = hour;
	    minMinute = minute;
	}

	public void setMax(int hour, int minute) {
	    maxHour = hour;
	    maxMinute = minute;
	}

	@Override
	public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

	    boolean validTime = true;
	    if (hourOfDay < minHour || (hourOfDay == minHour && minute < minMinute)){
	        validTime = false;
	    }

	    if (hourOfDay  > maxHour || (hourOfDay == maxHour && minute > maxMinute)){
	        validTime = false;
	    }

	    if (validTime) {
	        currentHour = hourOfDay;
	        currentMinute = minute;
	    }

	    this.setCurrentHour(currentHour);
	    this.setCurrentMinute(currentMinute);
	}

}
