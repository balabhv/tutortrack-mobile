package com.tutortrack.api.student;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;

import android.content.Context;

public class StudentAppointmentQueue implements Serializable {

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4632176905088549674L;
	public Queue<StudentAppointment> aptmtQueue;
	public Queue<StudentAppointment> mirrorQueue;
	private static Context mContext;
	
	public StudentAppointmentQueue(Context context) {
		mContext = context;
		this.aptmtQueue = new LinkedList<StudentAppointment>();
		this.mirrorQueue = new LinkedList<StudentAppointment>();
	}
	
	public void addDataSetToQueue(StudentAppointment ds) {
		aptmtQueue.add(ds);
		mirrorQueue.add(ds);
	}
	
	// removes the dataset with the associated key: true if removed, false if not found
	public StudentAppointment removeItemWithKey(long keyVal) {
		LinkedList<StudentAppointment> backup = new LinkedList<StudentAppointment>();
		backup.addAll(aptmtQueue);
		for (StudentAppointment ds : backup) {
			if (ds.APTMT_ID == keyVal) {
				aptmtQueue.remove(ds);
				mirrorQueue.remove(ds);
				return ds;
			}
		}
		
		return null;
	}


}
