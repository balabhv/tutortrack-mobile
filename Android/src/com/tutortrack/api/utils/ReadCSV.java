package com.tutortrack.api.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import au.com.bytecode.opencsv.CSVReader;

import com.tutortrack.api.API.Location;
import com.tutortrack.api.Subject;
import com.tutortrack.api.User;
import com.tutortrack.api.student.TutorBlock;

public class ReadCSV {

	public static List<TutorBlock> read(Activity parent, String filepath) {

		List<TutorBlock> list = new LinkedList<TutorBlock>();
		
		try {
			CSVReader reader = new CSVReader(new InputStreamReader(parent.getAssets().open(filepath)));
			List<String[]> elements = reader.readAll();
			
			for (String[] tutor : elements) {
				TutorBlock block = new TutorBlock();
				User u = new User();
				u.setName(tutor[0]);
				block.setTutor(u);
				block.setSubjects(Subject.parse(tutor[1]));
				block.setStartDate(parseDate(tutor[2]));
				block.setEndDate(parseDate(tutor[3]));
				block.setStartTime(parseTime(tutor[4]));
				block.setEndTime(parseTime(tutor[5]));
				block.setWhere(locationFromString(tutor[6]));
				list.add(block);
			}
			
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		};

		System.out.println("Done");

		return list;
	}

	private static Calendar parseDate(String dateString) {
		Calendar c = new GregorianCalendar();

		String[] parts = dateString.split("/");
		int month = Integer.parseInt(parts[0]);
		int day = Integer.parseInt(parts[1]);
		int year = Integer.parseInt(parts[2]);

		switch (month) {
		case 1:
			c.set(year, Calendar.JANUARY, day);
			break;
		case 2:
			c.set(year, Calendar.FEBRUARY, day);
			break;
		case 3:
			c.set(year, Calendar.MARCH, day);
			break;
		case 4:
			c.set(year, Calendar.APRIL, day);
			break;
		case 5:
			c.set(year, Calendar.MAY, day);
			break;
		case 6:
			c.set(year, Calendar.JUNE, day);
			break;
		case 7:
			c.set(year, Calendar.JULY, day);
			break;
		case 8:
			c.set(year, Calendar.AUGUST, day);
			break;
		case 9:
			c.set(year, Calendar.SEPTEMBER, day);
			break;
		case 10:
			c.set(year, Calendar.OCTOBER, day);
			break;
		case 11:
			c.set(year, Calendar.NOVEMBER, day);
			break;
		case 12:
			c.set(year, Calendar.DECEMBER, day);
			break;
		}

		return c;

	}

	private static Calendar parseTime(String time) {
		Calendar c = new GregorianCalendar();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm a", Locale.US);
		try {
			c.setTime(sdf.parse(time));
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}

		return c;
	}

	private static Location locationFromString(String loc) {
		if (loc.toLowerCase(Locale.US).equals("north")) {
			return Location.NORTH;
		} else if (loc.toLowerCase(Locale.US).equals("south")) {
			return Location.SOUTH;
		} else if (loc.toLowerCase(Locale.US).equals("east")) {
			return Location.EAST;
		} else {
			return Location.ICC;
		}
	}

}
