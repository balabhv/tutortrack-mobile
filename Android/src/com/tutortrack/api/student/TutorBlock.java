package com.tutortrack.api.student;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

import com.tutortrack.api.API.Location;
import com.tutortrack.api.Subject;
import com.tutortrack.api.User;

public class TutorBlock implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4689150249867551075L;
	private User tutor;
	private ArrayList<Subject> subjects;
	private Calendar startDate, endDate, startTime, endTime;
	private Location where;

	/**
	 * @return the tutor
	 */
	public User getTutor(){
		return tutor;
	}

	/**
	 * @param tutor
	 *            the tutor to set
	 */
	public void setTutor(User tutor) {
		this.tutor = tutor;
	}

	/**
	 * @return the subjects
	 */
	public ArrayList<Subject> getSubjects() {
		return subjects;
	}

	/**
	 * @param subjects
	 *            the subjects to set
	 */
	public void setSubjects(ArrayList<Subject> subjects) {
		this.subjects = subjects;
	}

	/**
	 * @return the where
	 */
	public Location getWhere() {
		return where;
	}

	/**
	 * @param where
	 *            the where to set
	 */
	public void setWhere(Location where) {
		this.where = where;
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

		s = this.getStartDateString() + " - " + this.getEndDateString() + "\n"
				+ this.getStartTimeString() + " - " + this.getEndTimeString()
				+ "\nLocation: ";
		switch (this.where) {
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

	private String getEndTimeString() {
		return getTimeString(endTime);
	}

	private String getStartTimeString() {
		return getTimeString(startTime);
	}

	private String getEndDateString() {
		return getDateString(endDate);
	}

	private String getStartDateString() {
		return getDateString(startDate);
	}
	
	/**
	 * @return the startDate
	 */
	public Calendar getStartDate() {
		return startDate;
	}

	/**
	 * @return the endDate
	 */
	public Calendar getEndDate() {
		return endDate;
	}

	/**
	 * @return the startTime
	 */
	public Calendar getStartTime() {
		return startTime;
	}

	/**
	 * @return the endTime
	 */
	public Calendar getEndTime() {
		return endTime;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
	}

	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(Calendar startTime) {
		this.startTime = startTime;
	}

	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(Calendar endTime) {
		this.endTime = endTime;
	}

	private String getTimeString(Calendar when) {
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

	private String getDateString(Calendar when) {
		String month = "/";
		if (when.get(Calendar.MONTH) < 10) {
			month = "0" + (when.get(Calendar.MONTH) + 1) + month;
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

	/**
	 * @param tutor
	 * @param subjects
	 * @param startDate
	 * @param endDate
	 * @param startTime
	 * @param endTime
	 * @param where
	 */
	public TutorBlock(User tutor, ArrayList<Subject> subjects,
			Calendar startDate, Calendar endDate, Calendar startTime,
			Calendar endTime, Location where) {
		this.tutor = tutor;
		this.subjects = subjects;
		this.startDate = startDate;
		this.endDate = endDate;
		this.startTime = startTime;
		this.endTime = endTime;
		this.where = where;
	}
	
	public TutorBlock() {
	}

}
