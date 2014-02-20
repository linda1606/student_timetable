package student_timetable;
import java.sql.*;

import util.DB;

public class Course {
	private String name;
	private int credits;
//	static String url = "jdbc:mysql://localhost:3306/student_timetable";
//	static { 
//		try { 
//			Class.forName("com.mysql.jdbc.Drive"); 
//			}
//		catch (Exception ignored) {} 
//	}

	public static Course create(String name, int credits)  {
		Connection conn = null;
		Statement statement  = null;
		try {
			conn = DB.getInstance().getConnection();
			statement = conn.createStatement();
			statement.execute("DELETE FROM course WHERE name = '" + name + "'");
			statement.execute("INSERT INTO course VALUES ('" + name + "', '" + credits + "');");
			return new Course(name, credits);
		} catch(Exception e){
			e.printStackTrace();
		}
		finally {
//			try { 
//				conn.close(); 
//			} 
//			catch (Exception ignored) {}
			DB.getInstance().free(null, statement, conn);
		}
		return null;
	}

	public static Course find(String name) {
		Connection conn = null;
		Statement statement = null;
		try {
			conn = DB.getInstance().getConnection();
			statement = conn.createStatement();
			ResultSet result = statement.executeQuery("SELECT * FROM course WHERE Name = '" + name + "';");
			if (!result.next()) return null;
			int credits = result.getInt("Credits");
			return new Course(name, credits);
		} 
		catch (Exception ex) {
			return null;
		} 
		finally {
			DB.getInstance().free(null, statement, conn);
		}
	}
	
	public void update() throws Exception {
		Connection conn = null;
		Statement statement= null;
		try {
			conn = DB.getInstance().getConnection();
			statement = conn.createStatement();
			statement.executeUpdate("DELETE FROM COURSE WHERE name = '" + name + "';");
			statement.executeUpdate("INSERT INTO course VALUES('" + name + "','" + credits + "');");
		} 
		finally {
			DB.getInstance().free(null, statement, conn);
		}
	}

	Course(String name, int credits) {
		this.name = name;
		this.credits = credits;
	}
	
	public int getCredits() {
		return credits;
	}

	public String getName() {
		return name;
	}
}
