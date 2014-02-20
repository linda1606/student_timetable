package student_timetable;
import java.util.*;
import java.sql.*;

import util.DB;

public class Schedule {
	
	String name;
	int credits = 0;
	static final int minCredits = 12;
	static final int maxCredits = 18;
	boolean permission = false;
	
	ArrayList<Offering> schedule = new ArrayList<Offering>();
	
//	static String url = "jdbc:mysql://localhost:3306/student_timetable";
//	static { 
//		try { 
//			Class.forName("com.mysql.jdbc.Drive"); 
//		}
//		catch (Exception ignored) {} 
//	}

	public static void deleteAll() throws Exception {
		Connection conn = null;
		Statement statement = null; 
		try {
			conn = DB.getInstance().getConnection();
			statement = conn.createStatement();
			statement.executeUpdate("DELETE  FROM schedule");
		} 
		finally {
			DB.getInstance().free(null, statement, conn);
		}
	}
	
	public static Schedule create(String name) throws Exception {
		Connection conn = null;
		Statement statement = null;
		try {
			conn = DB.getInstance().getConnection();
			statement = conn.createStatement();
			statement.executeUpdate("DELETE FROM schedule WHERE name = '" + name + "';");
			return new Schedule(name);
		} 
		finally {
			DB.getInstance().free(null, statement, conn);
		}
	}
	
	public static Schedule find(String name) {
		Connection conn = null;
		Statement statement = null;
		ResultSet result = null;
		try {
			conn = DB.getInstance().getConnection();
			 statement = conn.createStatement();
			 result = statement.executeQuery("SELECT * FROM schedule WHERE Name= '" + name + "';");
			Schedule schedule = new Schedule(name);
			while (result.next()) {
				int offeringId = result.getInt("OfferingId");
				Offering offering = Offering.find(offeringId);
				schedule.add(offering);
			}
			return schedule;
		} 
		catch (Exception ex) {
			return null;
		} 
		finally {
			DB.getInstance().free(result, statement, conn);
		}
	}

	public static Collection<Schedule> all() throws Exception {
		ArrayList<Schedule> result = new ArrayList<Schedule>();
		Connection conn = null;
		Statement statement = null;
		ResultSet results = null;
		try {
			conn = DB.getInstance().getConnection();
			statement = conn.createStatement();
			results = statement.executeQuery("SELECT DISTINCT Name FROM schedule;");
			while (results.next())
			result.add(Schedule.find(results.getString("Name")));
		} 
		finally {
			DB.getInstance().free(results, statement, conn);
		}
		return result;
	}

	public void update() throws Exception {
		Connection conn = null;
		Statement statement = null;
		try {
			conn = DB.getInstance().getConnection();
			statement = conn.createStatement();
			statement.executeUpdate("DELETE FROM schedule WHERE name = '" + name + "';");
			for (int i = 0; i < schedule.size(); i++) {
				Offering offering = (Offering) schedule.get(i);
				statement.executeUpdate("INSERT INTO schedule VALUES('" + name + "','" + offering.getId() + "');");
			}
		} 
		finally {
			DB.getInstance().free(null, statement, conn);
		}
	}

	public Schedule(String name) {
		this.name = name;
	}

	public void add(Offering offering) {
		credits += offering.getCourse().getCredits();
		schedule.add(offering);
	}

	public void authorizeOverload(boolean authorized) {
		permission = authorized;
	}

	public List<String> analysis() {
		ArrayList<String> result = new ArrayList<String>();
		if (credits < minCredits)
			result.add("Too few credits");
		if (credits > maxCredits && !permission)
			result.add("Too many credits");
		checkDuplicateCourses(result);
		checkOverlap(result);
		return result;
	}

	public void checkDuplicateCourses(ArrayList<String> analysis) {
		HashSet<Course> courses = new HashSet<Course>();
		for (int i = 0; i < schedule.size(); i++) {
			Course course = ((Offering) schedule.get(i)).getCourse();
			if (courses.contains(course))
				analysis.add("Same course twice - " + course.getName());
			courses.add(course);
		}
	}

	public void checkOverlap(ArrayList<String> analysis) {
		HashSet<String> times = new HashSet<String>();
		for (Iterator<Offering> iterator = schedule.iterator(); iterator.hasNext();) {
			Offering offering = (Offering) iterator.next();
			String daysTimes = offering.getDaysTimes();
			StringTokenizer tokens = new StringTokenizer(daysTimes, ",");
			while (tokens.hasMoreTokens()) {
				String dayTime = tokens.nextToken();
				if (times.contains(dayTime))
					analysis.add("Course overlap - " + dayTime);
				times.add(dayTime);
			}
		}
	}

	public String toString() {
		return "Schedule " + name + ": " + schedule;
	}
}
