package daoImpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import util.ConnectionFactory;
import model.Course;
import model.Offering;
import dao.CourseDao;
import dao.OfferingDao;

public class OfferingDaoImpl implements OfferingDao {
	private Course course;
	private CourseDao courseDao = new CourseDaoImpl();
	Connection conn = null;
	Statement statement = null;
	ResultSet result = null;

	@Override
	public Offering create(Course course, String daysTimesCsv) throws Exception {
		
		try {
			conn = ConnectionFactory.getInstance().getConnection();
			statement = conn.createStatement();
			result = statement.executeQuery("SELECT MAX(ID) FROM offering;");
			result.next();
			int newId = 1 + result.getInt(1);
			statement.executeUpdate("INSERT INTO offering VALUES ('"+ newId + "','" + course.getName() + "','" + daysTimesCsv + "');");
			return new Offering(newId, course, daysTimesCsv);
		} 
		finally {
			ConnectionFactory.getInstance().close(null, statement, conn);
		}
	}

	@Override
	public Offering find(int id) throws Exception {
		try {
			conn = ConnectionFactory.getInstance().getConnection();
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
			ConnectionFactory.getInstance().close(null, statement, conn);
			return null;
		}
	}

	@Override
	public void update(Offering offering) throws Exception {
		try {
			conn = ConnectionFactory.getInstance().getConnection();
			statement = conn.createStatement();
			statement.executeUpdate("DELETE FROM Offering WHERE ID=" + offering.getId() + ";");
			statement.executeUpdate("INSERT INTO Offering VALUES('" + offering.getId() + "','" + course.getName() + "','" + offering.getDaysTimes() + "');");
		} 
		finally {
			ConnectionFactory.getInstance().close(null, statement, conn);
		}

	}

}
