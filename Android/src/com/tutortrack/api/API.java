package com.tutortrack.api;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.tutortrack.api.User.UserType;
import com.tutortrack.api.student.TutorBlock;

public class API {

	public enum Location {
		NORTH, SOUTH, EAST, ICC, NONE;
	}
	
	public static boolean hasConnectivity() {
	     ConnectivityManager connectivityManager = (ConnectivityManager) mainActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
	     NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	     return activeNetworkInfo != null; 
	}

	private static API instance = null;
	private static User currentUser;
	public static Activity mainActivity;

	private String DEV_BASE_URL = "http://10.253.93.115:8080/_ah/api/tutortrack/v1";

	public static API getInstance() {
		if (instance == null) {
			instance = new API();
		}
		return instance;
	}
	
	public static Location locationFromString(String s) {
		if (s.equalsIgnoreCase("North")) {
			return Location.NORTH;
		} else if (s.equalsIgnoreCase("East")) {
			return Location.EAST;
		} else if (s.equalsIgnoreCase("South")) {
			return Location.SOUTH;
		} else {
			return Location.ICC;
		}
	}
	
	public static String stringFromLocation(Location l) {
		switch (l) {
		case NORTH:
			return "North";
		case SOUTH:
			return "South";
		case EAST:
			return "East";
		case ICC:
			return "ICC";
		case NONE:
		default:
			return "";
		}
	}

	public User createStudentSession(String email, String password) {

		Log.d("api", "Inside createSession");
		String res;
		try {
			System.out.println("Inside try");
			res = makeRequest(DEV_BASE_URL, "students/myInfo", "email="
					+ URLEncoder.encode(email, "UTF-8") + "&password="
					+ URLEncoder.encode(password, "UTF-8"), "GET", null);
			JSONObject result = new JSONObject(res);

			if (result.getString("name") != null) {
				currentUser = new User();
				currentUser.setName(result.getString("name"));
				currentUser.setEmail(result.getString("email"));
				currentUser.setType(UserType.STUDENT);
				return currentUser;
			}
		} catch (Exception e) {
			return null;
		}

		return null;
	}

	public User createTutorSession(String email, String password) {

		Log.d("api", "Inside createSession");
		String res;
		try {
			System.out.println("Inside try");
			res = makeRequest(DEV_BASE_URL, "tutors/myInfo", "email="
					+ URLEncoder.encode(email, "UTF-8") + "&password="
					+ URLEncoder.encode(password, "UTF-8"), "GET", null);
			JSONObject result = new JSONObject(res);

			if (result.getString("name") != null) {
				currentUser = new User();
				currentUser.setName(result.getString("name"));
				currentUser.setEmail(result.getString("email"));
				currentUser.setType(UserType.TUTOR);
				return currentUser;
			}
		} catch (Exception e) {
			return null;
		}

		return null;
	}

	public User createAdminSession(String email, String password) {

		Log.d("api", "Inside createSession");
		String res;
		try {
			System.out.println("Inside try");
			res = makeRequest(DEV_BASE_URL, "admins/myInfo", "email="
					+ URLEncoder.encode(email, "UTF-8") + "&password="
					+ URLEncoder.encode(password, "UTF-8"), "GET", null);
			JSONObject result = new JSONObject(res);

			if (result.getString("name") != null) {
				currentUser = new User();
				currentUser.setName(result.getString("name"));
				currentUser.setEmail(result.getString("email"));
				currentUser.setType(UserType.ADMINISTRATOR);
				return currentUser;
			}
		} catch (Exception e) {
			return null;
		}

		return null;
	}

	public static User getCurrentUser() {
		return currentUser;
	}

	@SuppressLint("SimpleDateFormat")
	public ArrayList<TutorBlock> searchTutors(Location loc, String subject) {
		ArrayList<TutorBlock> results = new ArrayList<TutorBlock>();
		
		SimpleDateFormat dateformat = new SimpleDateFormat("MM/dd/yyyy");
		SimpleDateFormat timeformat = new SimpleDateFormat("h:mm a");

		String locSearch = API.stringFromLocation(loc);
		

		try {
			String parameters = "location="
					+ URLEncoder.encode(locSearch, "UTF-8") + "&subject="
					+ URLEncoder.encode(subject, "UTF-8");
			String req = makeRequest(DEV_BASE_URL, "tutors/search", parameters, "GET", null);
			JSONObject reqRes = new JSONObject(req);
			JSONArray res = reqRes.getJSONArray("items");
			
			for (int i = 0 ; i < res.length() ; i++) {
				
				JSONObject obj = res.getJSONObject(i);
				String name = obj.getString("name");
				String startD = obj.getString("startDate");
				String endD = obj.getString("endDate");
				String startT = obj.getString("startTime");
				String endT = obj.getString("endTime");
				String location = obj.getString("location");
				String tempsub = obj.getString("subjects");
				
				Calendar startDate = Calendar.getInstance();
				startDate.setTime(dateformat.parse(startD));
				Calendar endDate = Calendar.getInstance();
				endDate.setTime(dateformat.parse(endD));
				Calendar startTime = Calendar.getInstance();
				startTime.setTime(timeformat.parse(startT));
				Calendar endTime = Calendar.getInstance();
				endTime.setTime(timeformat.parse(endT));
				Location tutorLoc = API.locationFromString(location);
				String[] parts = tempsub.split(", ");
				ArrayList<Subject> subjects = new ArrayList<Subject>();
				for (String sub : parts) {
					subjects.add(new Subject(sub));
				}
				
				User u = new User();
				u.setName(name);
				u.setType(UserType.TUTOR);
				
				TutorBlock block = new TutorBlock(u, subjects, startDate, endDate, startTime, endTime, tutorLoc);
				results.add(block);
			}
		} catch (Exception e) {

		}
		
		return results;
	}

	private String makeRequest(String baseURL, String path, String parameters,
			String reqType, JSONObject postData) {

		Log.d("API", "makeRequest");

		byte[] mPostData = null;

		int mstat = 0;
		try {
			URL url = new URL(baseURL + "/" + path + "?" + parameters);
			System.out.println("Connect to: " + url);

			HttpURLConnection urlConnection = (HttpURLConnection) url
					.openConnection();
			urlConnection.setRequestMethod(reqType);
			urlConnection.setRequestProperty("Accept", "application/json");
			// urlConnection.setDoOutput(true);
			if (postData != null) {
				System.out.println("Post data: " + postData);
				mPostData = postData.toString().getBytes();
				urlConnection.setRequestProperty("Content-Length",
						Integer.toString(mPostData.length));
				urlConnection.setRequestProperty("Content-Type",
						"application/json");
				OutputStream out = urlConnection.getOutputStream();
				out.write(mPostData);
				out.close();
			}

			mstat = urlConnection.getResponseCode();
			InputStream in;
			System.out.println("Status: " + mstat);
			if (mstat >= 200 && mstat < 300) {
				in = new BufferedInputStream(urlConnection.getInputStream());
			} else {
				in = new BufferedInputStream(urlConnection.getErrorStream());
			}
			try {
				ByteArrayOutputStream bo = new ByteArrayOutputStream();
				int i = in.read();
				while (i != -1) {
					bo.write(i);
					i = in.read();
				}
				return bo.toString();
			} catch (IOException e) {
				return "";
			} finally {
				in.close();
			}
		} catch (ConnectException ce) {
			System.err
					.println("Connection failed: ENETUNREACH (network not reachable)");
			ce.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "Error: status " + mstat;
	}

}
