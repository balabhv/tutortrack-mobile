package com.tutortrack.api;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.tutortrack.api.student.StudentAppointment;
import com.tutortrack.api.student.TutorBlock;

public class API {

	public enum Location {
		NORTH, SOUTH, EAST, ICC, NONE;
	}

	private static API instance = null;
	private static User currentUser;
	public static Activity mainActivity;
	private static boolean useDev = true;

	public final static String DEV_BASE_URL = "http://192.168.1.7:8888/_ah/api/tutortrack/v1";
	public final static String PROD_BASE_URL = "https://1-dot-tutor-track-api.appspot.com/_ah/api/tutortrack/v1";
	private static String baseUrl;

	public static API getInstance() {
		if (instance == null) {
			instance = new API();
		}
		return instance;
	}

	public static void setUseDev(boolean yes) {
		useDev = yes;
		if (useDev()) {
			baseUrl = DEV_BASE_URL;
		} else {
			baseUrl = PROD_BASE_URL;
		}
	}

	public static boolean useDev() {
		return useDev;
	}

	public static boolean hasConnectivity() {
		ConnectivityManager connectivityManager = (ConnectivityManager) mainActivity
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null;
	}

	public static Location locationFromString(String s) {
		if (s.equalsIgnoreCase("North")) {
			return Location.NORTH;
		} else if (s.equalsIgnoreCase("East")) {
			return Location.EAST;
		} else if (s.equalsIgnoreCase("South")) {
			return Location.SOUTH;
		} else if (s.equalsIgnoreCase("ICC")) {
			return Location.ICC;
		} else {
			return Location.NONE;
		}
	}

	public static String stringFromLocation(Location l) {
		switch (l) {
		case NORTH:
			return "NORTH";
		case SOUTH:
			return "SOUTH";
		case EAST:
			return "EAST";
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
			res = makeRequest(baseUrl, "students/myInfo",
					"email=" + URLEncoder.encode(email, "UTF-8") + "&password="
							+ URLEncoder.encode(password, "UTF-8"), "GET", null);
			JSONObject result = new JSONObject(res);

			if (result.getString("name") != null) {
				currentUser = User.studentFromJSON(result);
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
			res = makeRequest(baseUrl, "tutors/myInfo",
					"email=" + URLEncoder.encode(email, "UTF-8") + "&password="
							+ URLEncoder.encode(password, "UTF-8"), "GET", null);
			JSONObject result = new JSONObject(res);

			if (result.getString("name") != null) {
				currentUser = User.tutorFromJSON(result);
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
			res = makeRequest(baseUrl, "admins/myInfo",
					"email=" + URLEncoder.encode(email, "UTF-8") + "&password="
							+ URLEncoder.encode(password, "UTF-8"), "GET", null);
			JSONObject result = new JSONObject(res);

			if (result.getString("name") != null) {
				currentUser = User.adminFromJSON(result);
				return currentUser;
			}
		} catch (Exception e) {
			return null;
		}

		return null;
	}
	
	public void deleteSession() {
		currentUser = null;
	}

	public static User getCurrentUser() {
		return currentUser;
	}

	@SuppressLint("SimpleDateFormat")
	public ArrayList<TutorBlock> searchTutors(Location loc, String subject) {
		ArrayList<TutorBlock> results = new ArrayList<TutorBlock>();

		SimpleDateFormat dateformat = new SimpleDateFormat("MM/dd/yy");
		SimpleDateFormat timeformat = new SimpleDateFormat("h a");

		String locSearch = API.stringFromLocation(loc);

		try {
			String parameters = "location="
					+ URLEncoder.encode(locSearch, "UTF-8") + "&subject="
					+ URLEncoder.encode(subject, "UTF-8");

			if (subject.equalsIgnoreCase("")) {
				if (locSearch.equalsIgnoreCase(""))
					parameters = "";
				else
					parameters = parameters.substring(0,
							parameters.indexOf("&"));
			} else {
				if (locSearch.equalsIgnoreCase(""))
					parameters = parameters
							.substring(parameters.indexOf("&") + 1);
			}

			String req = makeRequest(baseUrl, "tutors/search", parameters,
					"GET", null);
			JSONObject reqRes = new JSONObject(req);
			JSONArray res = reqRes.getJSONArray("items");

			for (int i = 0; i < res.length(); i++) {

				JSONObject obj = res.getJSONObject(i);
				JSONObject tutor = obj.getJSONObject("tutor");
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

				User u = User.tutorFromJSON(tutor);
				TutorBlock block = new TutorBlock(u, subjects, startDate,
						endDate, startTime, endTime, tutorLoc);
				results.add(block);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return results;
	}

	public User getTutor(String name) {
		try {
			String res = makeRequest(baseUrl, "tutors/searchByName",
					"tutor_name=" + URLEncoder.encode(name, "UTF-8"), "GET",
					null);
			JSONObject respJSON = new JSONObject(res);
			if (respJSON.getString("name") != null) {
				return User.tutorFromJSON(respJSON);
			}
		} catch (Exception e) {
			return null;
		}

		return null;
	}
	
	public StudentAppointment makeAppointmentWithTutor(User tutor, Calendar date, Calendar time, Location loc, ArrayList<Subject> subjects) {
		Calendar when = new GregorianCalendar();
		when.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DATE), time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), time.get(Calendar.SECOND));
		StudentAppointment appt = new StudentAppointment(when, subjects, getTutor(tutor.getName()), loc);
		JSONObject obj = new JSONObject();
		try {
			obj.put("studentEmail", currentUser.getEmail());
			obj.put("studentPassword", currentUser.getPassword());
			obj.put("date", appt.getDate());
			obj.put("time", appt.getTime());
			obj.put("location", API.stringFromLocation(loc));
			obj.put("subjects", appt.getSubjectString());
			JSONObject tutorJSON = User.JSONFromTutorUser(tutor);
			obj.put("tutor", tutorJSON);
			makeRequest(baseUrl, "appointments/makeAppointmentWithTutor", "", "POST", obj);
			return appt;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public ArrayList<StudentAppointment> getAppointmentsForStudent(User student) {
		ArrayList<StudentAppointment> results = new ArrayList<StudentAppointment>();
		
		try {
			String res = this.makeRequest(baseUrl, "appointments/getStudentAppointments", "email=" + URLEncoder.encode(student.getEmail(), "UTF-8") + "&password="
					+ URLEncoder.encode(student.getPassword(), "UTF-8"), "GET", null);
			JSONObject ret = new JSONObject(res);
			JSONArray r = ret.getJSONArray("items");
			
			for (int i = 0 ; i < r.length() ; ++i) {
				JSONObject apptJSON = r.getJSONObject(i);
				StudentAppointment appt = StudentAppointment.AppointmentFromJSON(apptJSON);
				results.add(appt);
			}
			
			return results;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return new ArrayList<StudentAppointment>();
		} catch (JSONException e) {
			e.printStackTrace();
			return new ArrayList<StudentAppointment>();
		}
	}
	
	public boolean editStudentAppointment(StudentAppointment orig, StudentAppointment appt) {
		JSONObject obj = new JSONObject();
		try {
			JSONObject original = StudentAppointment.JSONFromAppointment(orig);
			JSONObject edited = StudentAppointment.JSONFromAppointment(appt);
			obj.put("orig", original);
			obj.put("edited", edited);
			String httpresp = makeRequest(baseUrl, "appointments/editAppointment", "", "POST", obj);
			JSONObject res = new JSONObject(httpresp);
			String success = res.getString("message");
			if (success.equalsIgnoreCase("Operation Succeeded!")) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	public void cancelStudentAppointment(StudentAppointment appt) {
		JSONObject obj = new JSONObject();
		try {
			obj.put("studentEmail", currentUser.getEmail());
			obj.put("studentPassword", currentUser.getPassword());
			obj.put("date", appt.getDate());
			obj.put("time", appt.getTime());
			obj.put("location", API.stringFromLocation(appt.getWhere()));
			obj.put("subjects", appt.getSubjectString());
			JSONObject tutorJSON = User.JSONFromTutorUser(appt.getWithWho());
			obj.put("tutor", tutorJSON);
			String httpresp = makeRequest(baseUrl, "appointments/cancelStudentAppointment", "", "POST", obj);
			JSONObject res = new JSONObject(httpresp);
			String success = res.getString("message");
			if (success.equalsIgnoreCase("Operation Succeeded!")) {
				//return appt;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
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
