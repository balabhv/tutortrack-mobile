package com.tutortrack.api.student;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

import com.tutortrack.api.Subject;
import com.tutortrack.api.User;
import com.tutortrack.api.API.Location;

public class StudentAppointment implements Serializable {


	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5492815171513465138L;
	public int APTMT_ID;
	private Calendar when;
	private User withWho;
	private Location where;
	private ArrayList<Subject> subjects;
	
	public static int ID_COUNT = 0;
	
	public static final String id_key = "APTMT_ID";
	public static final String when_key = "TIMESTAMP";
	public static final String with_who_key = "WITH_WHO";
	public static final String where_key = "LOCATION";
	public static final String what_key = "SUBJECTS";
	
public StudentAppointment(Calendar mDate, ArrayList<Subject> subjList, User withWho, Location place) {
		
		when = mDate;
		
		APTMT_ID = ID_COUNT++;
		
		subjects = new ArrayList<Subject>();
		
		for (Subject s: subjList) {
			subjects.add(s);
		}
		
		this.withWho = withWho;
		
		where = place;
			
	}

	/**
	 * @return the when
	 */
	public Calendar getWhen() {
		return when;
	}
	/**
	 * @param when the when to set
	 */
	public void setWhen(Calendar when) {
		this.when = when;
	}
	/**
	 * @return the withWho
	 */
	public User getWithWho() {
		return withWho;
	}
	/**
	 * @param withWho the withWho to set
	 */
	public void setWithWho(User withWho) {
		this.withWho = withWho;
	}
	public ArrayList<Subject> getSubjects() {
		return subjects;
	}
	public void setSubjects(ArrayList<Subject> subjects) {
		this.subjects = subjects;
	}
	public String getSubjectString() {
		
		String s = "";
		for (Subject sub : subjects) {
			s += sub.toString() + ", ";
		}
		
		return s.substring(0, s.lastIndexOf(", "));
	}
	
	public String getWhenWhereString() {
		String s = "";
		
		s = "Date: " + this.getDate() + "\nTime: " + this.getTime() + "\nLocation: ";
		switch(this.where) {
		case NORTH:
			s += "North Campus, Southwick Hall";
			break;
		case SOUTH:
			s += "South Campus, O'Leary Library";
			break;
		case EAST:
			s += "East Campus, Fox Hall";
			break;
		case ICC:
			s += "ICC";
			break;
		default:
			break;
		}
		
		return s;
	}
	
	public String getTime() {
		String hour = ":";
		if (when.get(Calendar.HOUR) < 10) {
			hour = "0" + when.get(Calendar.HOUR) + hour;
		} else {
			hour = when.get(Calendar.HOUR) + hour;
		}
		String min = "";
		if (when.get(Calendar.MINUTE) < 10) {
			min = "0" + when.get(Calendar.MINUTE);
		} else {
			min = when.get(Calendar.MINUTE) + "";
		}
		String suffix = " ";
		if (when.get(Calendar.AM_PM) == Calendar.AM) {
			suffix += "AM";
		} else {
			suffix += "PM";
		}
		
		return hour + min + suffix;
	}
	
	public String getDate() {
		String month = "/";
		if (when.get(Calendar.MONTH) < 10) {
			month = "0" + when.get(Calendar.MONTH) + month;
		} else {
			month = when.get(Calendar.MONTH) + month;
		}
		
		String day = "/";
		if (when.get(Calendar.DAY_OF_MONTH) < 10) {
			day = "0" + when.get(Calendar.DAY_OF_MONTH) + day;
		} else {
			day = when.get(Calendar.DAY_OF_MONTH) + day;
		}
		
		String year = "";
		if (when.get(Calendar.YEAR) < 1000) {
			year = "0" + when.get(Calendar.YEAR);
		} else {
			year = when.get(Calendar.YEAR) + "";
		}
		
		return month + day + year;
	}

}
