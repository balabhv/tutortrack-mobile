package com.tutortrack.api;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

public class API {
	
	public enum Location {
		NORTH, SOUTH, ICC;
	}

	private static API instance = null;
	private User currentUser;
	
	public static API getInstance() {
		if (instance == null) {
			instance = new API();
		}
		return instance;
	}
	
	public boolean createStudentSession(String email, String password) {
		// TODO insert call to Web API
		return false;
	}
	
	public boolean createTutorSession(String email, String password) {
		// TODO insert call to Web API
		return false;
	}
	
	public boolean createAdminSession(String email, String password) {
		// TODO insert call to Web API
		return false;
	}

	public User getCurrentUser() {
		return currentUser;
	}
	
	/**
	 * Makes an HTTP request and for JSON-formatted data. This call is blocking,
	 * and so functions that call this function must not be run on the UI
	 * thread.
	 * 
	 * @param baseURL
	 *            The base of the URL to which the request will be made
	 * @param path
	 *            The path to append to the request URL
	 * @param parameters
	 *            Parameters separated by ampersands (&)
	 * @param reqType
	 *            The request type as a string (i.e. GET or POST)
	 * @return A String dump of a JSONObject representing the requested data
	 */
	private String makeRequest(String baseURL, String path, String parameters,
			String reqType, JSONObject postData) {

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