package daoImpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import util.DB;
import model.Course;
import model.Offering;
import dao.CourseDao;
import dao.OfferingDao;

public class OfferingDaoImpl implements OfferingDao {
	private Course course;
	private CourseDao courseDao = new CourseDaoImpl(); 

	@Override
	public Offering create(Course course, String daysTimesCsv) throws Exception {
		Connection conn = null;
		Statement statement = null;
		ResultSet result = null;
		try {
			conn = DB.getInstance().getConnection();
			statement = conn.createStatement();
			result = statement.executeQuery("SELECT MAX(ID) FROM offering;");
			result.next();
			int newId = 1 + result.getInt(1);
			statement.executeUpdate("INSERT INTO offering VALUES ('"+ newId + "','" + course.getName() + "','" + daysTimesCsv + "');");
			return new Offering(newId, course, daysTimesCsv);
		} 
		finally {
			DB.getInstance().free(result, statement, conn);
		}
	}

	@Override
	public Offering find(int id) throws Exception {
		Connection conn = null;
		Statement statement = null;
		ResultSet result = null;
		try {
			conn = DB.getInstance().getConnection();
			statement = conn.createStatement();
			 result = statement.executeQuery("SELECT * FROM offering WHERE ID =" + id + ";");
			if (result.next() == false)
				return null;
			String courseName = result.getString("Course");
			Course course = courseDao.find(courseName);
			String dateTime = result.getString("DateTime");
			conn.close();
			return new Offering(id, course, dateTime);
		} 
		catch (Exception ex) {
			DB.getInstance().free(result, statement, conn);
			return null;
		}
	}

	@Override
	public void update(Offering offering) throws Exception {
		Connection conn = null;
		Statement statement = null;
		try {
			conn = DB.getInstance().getConnection();
			statement = conn.createStatement();
			statement.executeUpdate("DELETE FROM Offering WHERE ID=" + offering.getId() + ";");
			statement.executeUpdate("INSERT INTO Offering VALUES('" + offering.getId() + "','" + course.getName() + "','" + offering.getDaysTimes() + "');");
		} 
		finally {
			DB.getInstance().free(null, statement, conn);
		}

	}

}
