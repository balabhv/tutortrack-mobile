package com.tutortrack.api;

import java.io.Serializable;


public class Filter {
	
	public enum FilterType {
		LOCATION, SUBJECT;
	}
	
	private FilterType type;
	private Serializable value;

	/**
	 * @param type
	 * @param value
	 * @param filterView
	 */
	public Filter(FilterType type, Serializable value) {
		this.type = type;
		this.value = value;
	}
	public Filter() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * @return the type
	 */
	public FilterType getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(FilterType type) {
		this.type = type;
	}
	/**
	 * @return the value
	 */
	public Serializable getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(Serializable value) {
		this.value = value;
	}
}
