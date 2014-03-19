package com.tutortrack.api.tutor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;

import com.tutortrack.activity.MainActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

public class TutorAppointmentQueue implements Serializable {

	/**
	 * DO NOT EDIT THIS
	 */
	private static final long serialVersionUID = -4374433334100683336L;
	
	public Queue<TutorAppointment> aptmtQueue;
	public Queue<TutorAppointment> mirrorQueue;
	private static Context mContext;
	
	public TutorAppointmentQueue(Context context) {
		mContext = context;
		this.aptmtQueue = new LinkedList<TutorAppointment>();
		this.mirrorQueue = new LinkedList<TutorAppointment>();
	}
	
	public void addDataSetToQueue(TutorAppointment ds) {
		aptmtQueue.add(ds);
		mirrorQueue.add(ds);
		storeAndReRetrieveQueue(true);
	}

	// Saves Q_COUNT and aptmtQueue into memory for later use
	protected void storeAndReRetrieveQueue(boolean rebuildMirrorQueue) {

		Queue<TutorAppointment> backupQueue = new LinkedList<TutorAppointment>();
		backupQueue.addAll(aptmtQueue);
		
		// save Q_COUNT in SharedPrefs
		final SharedPreferences mPrefs = mContext.getSharedPreferences(
				MainActivity.PREFS_TAG, Context.MODE_PRIVATE);
		final SharedPreferences.Editor mPrefsEditor = mPrefs.edit();
		int Q_COUNT = backupQueue.size();
		int Q_COUNT_BACKUP = Q_COUNT;
		mPrefsEditor.putInt("Q_COUNT", Q_COUNT);
		mPrefsEditor.commit();
		
		// obtain storage directory and file for the aptmtQueue
		File folder = new File(Environment.getExternalStorageDirectory()
				+ "/TutorTrack");
		
		if (!folder.exists()) {
			folder.mkdir();
		}

		File AppointmentQueueFile = new File(folder.getAbsolutePath() + "/"
				 + "Appointments.ser");

		// writes the queue to a serializable file
		ObjectOutput out = null;
		try {
			out = new ObjectOutputStream(new FileOutputStream(AppointmentQueueFile));
			
			// serializes DataSets
			while (Q_COUNT > 0) {
				TutorAppointment ds = backupQueue.remove();
				out.writeObject(ds);
				Q_COUNT--;
			}

			out.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		// re-retrieve the queue
		aptmtQueue = new LinkedList<TutorAppointment>();
		Q_COUNT = Q_COUNT_BACKUP;
		
		try {
			// Deserialize the file as a whole
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(
					AppointmentQueueFile));
			
			// Deserialize the objects one by one
			for (int i = 0; i < Q_COUNT; i++) {
				TutorAppointment dataSet = (TutorAppointment) in.readObject();
				aptmtQueue.add(dataSet);
			}
			in.close();

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (rebuildMirrorQueue) {
			mirrorQueue.clear();
			mirrorQueue.addAll(aptmtQueue);
		}
		
		
		try {
			if (out != null)
				out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// removes the dataset with the associated key: true if removed, false if not found
	public boolean removeItemWithKey(long keyVal) {
		LinkedList<TutorAppointment> backup = new LinkedList<TutorAppointment>();
		backup.addAll(aptmtQueue);
		for (TutorAppointment ds : backup) {
			if (ds.APTMT_ID == keyVal) {
				aptmtQueue.remove(ds);
				mirrorQueue.remove(ds);
				storeAndReRetrieveQueue(true);
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Rebuilds the aptmtQueue from the serializable file it
	 * is saved on.
	 * 
	 * @return 
	 * 			@true if the aptmtQueue is rebuilt successfully
	 * 			@false if the rebuilding fails 
	 */
	public boolean buildQueueFromFile() {
		
		// reset the queues but save a backup
		Queue<TutorAppointment> backupQueue = new LinkedList<TutorAppointment>();
		backupQueue.addAll(aptmtQueue);
		aptmtQueue       = new LinkedList<TutorAppointment>();
		mirrorQueue = new LinkedList<TutorAppointment>();
		
		// get Q_COUNT from the SharedPrefs
		final SharedPreferences mPrefs = mContext.getSharedPreferences(
				MainActivity.PREFS_TAG, Context.MODE_PRIVATE);
		int Q_COUNT = mPrefs.getInt("Q_COUNT", -1);
		if (Q_COUNT == -1) {
			aptmtQueue.addAll(backupQueue);
			mirrorQueue.addAll(backupQueue);
			return false;
		}

		// obtain storage directory and file for the aptmtQueue
		File folder = new File(Environment.getExternalStorageDirectory()
				+ "/ExcaliburApps");
		if (!folder.exists()) {
			aptmtQueue.addAll(backupQueue);
			mirrorQueue.addAll(backupQueue);
			return false;
		}
		
		File AppointmentQueueFile = new File(folder.getAbsolutePath() + "/"
				+ "Appointments.ser");
		
		try {
			// Deserialize the file as a whole
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(
					AppointmentQueueFile));
			
			// Deserialize the objects one by one
			for (int i = 0; i < Q_COUNT; i++) {
				TutorAppointment dataSet = (TutorAppointment) in.readObject();
				aptmtQueue.add(dataSet);
			}
			in.close();

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			aptmtQueue.addAll(backupQueue);
			mirrorQueue.addAll(backupQueue);
			return false;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			aptmtQueue.addAll(backupQueue);
			mirrorQueue.addAll(backupQueue);
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			aptmtQueue.addAll(backupQueue);
			mirrorQueue.addAll(backupQueue);
			return false;
		}
		
		mirrorQueue.addAll(aptmtQueue);
		return true;
	}


}
