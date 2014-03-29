package com.tutortrack.api.utils;

import java.lang.reflect.Type;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class SharedPreferencesExecutor<T> {
	 
	private Context context;
	private String where;
 
	public SharedPreferencesExecutor(Context context, String where) {
		this.context = context;
		this.where = where;
	}
 
	public void save(String key, T sharedPreferencesEntry) {
 
		  SharedPreferences appSharedPrefs = context.getSharedPreferences(where, 0);
		  Editor prefsEditor = appSharedPrefs.edit();
		  Gson gson = new Gson();
		  String json = gson.toJson(sharedPreferencesEntry);
		  prefsEditor.putString(key, json);
		  prefsEditor.commit();
 
	}
 
	@SuppressWarnings("unchecked")
	public T retreive(String key) {
 
		SharedPreferences appSharedPrefs = context.getSharedPreferences(where, 0);

		 Gson gson = new Gson();
		 Type collectionType = new TypeToken<T>(){}.getType();
		 String json = appSharedPrefs.getString(key, "");
		 return (T) gson.fromJson(json, collectionType);
	}
	
	public String retreiveJSONString(String key) {
		 
		SharedPreferences appSharedPrefs = context.getSharedPreferences(where, 0);

		 return appSharedPrefs.getString(key, "");
		 
	}
}
