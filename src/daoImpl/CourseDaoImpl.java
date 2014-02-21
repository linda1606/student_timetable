package daoImpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import util.ConnectionFactory;
import model.Course;
import dao.CourseDao;

public class CourseDaoImpl implements CourseDao {
	Connection conn = null;
	Statement statement  = null;

	@Override
	public Course create(String name, int credits) throws Exception {
		try {
			conn = ConnectionFactory.getInstance().getConnection();
			statement = conn.createStatement();
			statement.execute("DELETE FROM course WHERE name = '" + name + "'");
			statement.execute("INSERT INTO course VALUES ('" + name + "', '" + credits + "');");
			return new Course(name, credits);
		} catch(Exception e){
			e.printStackTrace();
		}
		finally {
			ConnectionFactory.getInstance().close(null, statement, conn);
			}
		return null;
	}

	@Override
	public Course find(String name) throws Exception {
		try {
			conn = ConnectionFactory.getInstance().getConnection();
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
			ConnectionFactory.getInstance().close(null, statement, conn);
		}
	}

	@Override
	public void update(Course course) throws Exception {
		try {
			conn = ConnectionFactory.getInstance().getConnection();
			statement = conn.createStatement();
			statement.executeUpdate("DELETE FROM COURSE WHERE name = '" + course.getName() + "';");
			statement.executeUpdate("INSERT INTO course VALUES('" + course.getName() + "','" + course.getCredits() + "');");
		} 
		finally {
			ConnectionFactory.getInstance().close(null, statement, conn);
		}
		
	}

}
