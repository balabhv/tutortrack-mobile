package com.tutortrack.api;

import java.io.Serializable;
import java.util.ArrayList;

public class Subject implements Serializable{
	
	// put list of subjects here
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3780434738490185433L;
	private String subjName;
	
	public Subject(String name) {
		subjName = name;
	}
	
	public String toString() {
		return subjName;
	}
	
	public void setSubject(String name) {
		subjName = name;
	}

	public static ArrayList<Subject> parse(String string) {
		String[] tutor = string.split(",");
		ArrayList<Subject> subj = new ArrayList<Subject>();
		
		for (int i = 0 ; i < tutor.length ; ++i) {
			subj.add(new Subject(tutor[i]));
		}
		return subj;
	}

}
