package com.tutortrack.api;

import java.util.ArrayList;
import java.util.Calendar;

public class TimeSlot {
	
	private Calendar startDate, endDate, startTime, endTime;
	private User tutor;
	private ArrayList<Subject> subjects;
	private API.Location location;
	/**
	 * @return the startDate
	 */
	public Calendar getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}
	/**
	 * @return the endDate
	 */
	public Calendar getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return the startTime
	 */
	public Calendar getStartTime() {
		return startTime;
	}
	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(Calendar startTime) {
		this.startTime = startTime;
	}
	/**
	 * @return the endTime
	 */
	public Calendar getEndTime() {
		return endTime;
	}
	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(Calendar endTime) {
		this.endTime = endTime;
	}
	/**
	 * @return the tutor
	 */
	public User getTutor() {
		return tutor;
	}
	/**
	 * @param tutor the tutor to set
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
	 * @param subjects the subjects to set
	 */
	public void setSubjects(ArrayList<Subject> subjects) {
		this.subjects = subjects;
	}
	/**
	 * @return the location
	 */
	public API.Location getLocation() {
		return location;
	}
	/**
	 * @param location the location to set
	 */
	public void setLocation(API.Location location) {
		this.location = location;
	}

}
