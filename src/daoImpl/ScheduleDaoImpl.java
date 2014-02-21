package daoImpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import util.ConnectionFactory;
import model.Offering;
import model.Schedule;
import dao.OfferingDao;
import dao.ScheduleDao;

public class ScheduleDaoImpl implements ScheduleDao{
	ArrayList<Offering> schedules = new ArrayList<Offering>();
	private OfferingDao offeringDao = new OfferingDaoImpl();
	
	@Override
	public void deleteAll() throws Exception {
		Connection conn = null;
		Statement statement = null; 
		try {
			conn = ConnectionFactory.getInstance().getConnection();
			statement = conn.createStatement();
			statement.executeUpdate("DELETE  FROM schedule");
		} 
		finally {
			ConnectionFactory.getInstance().close(null, statement, conn);
		}	
	}

	@Override
	public Schedule create(String name) throws Exception {
		Connection conn = null;
		Statement statement = null;
		try {
			conn = ConnectionFactory.getInstance().getConnection();
			statement = conn.createStatement();
			statement.executeUpdate("DELETE FROM schedule WHERE name = '" + name + "';");
			return new Schedule(name);
		} 
		finally {
			ConnectionFactory.getInstance().close(null, statement, conn);
		}
	}

	@Override
	public Schedule find(String name) {
		Connection conn = null;
		Statement statement = null;
		ResultSet result = null;
		try {
			conn = ConnectionFactory.getInstance().getConnection();
			 statement = conn.createStatement();
			 result = statement.executeQuery("SELECT * FROM schedule WHERE Name= '" + name + "';");
			Schedule schedule = new Schedule(name);
			while (result.next()) {
				int offeringId = result.getInt("OfferingId");
				Offering offering =  offeringDao.find(offeringId);
				schedule.add(offering);
			}
			return schedule;
		} 
		catch (Exception ex) {
			return null;
		} 
		finally {
			ConnectionFactory.getInstance().close(null, statement, conn);
		}
	}

	@Override
	public Collection<Schedule> all() throws Exception {
		ArrayList<Schedule> result = new ArrayList<Schedule>();
		Connection conn = null;
		Statement statement = null;
		ResultSet results = null;
		try {
			conn = ConnectionFactory.getInstance().getConnection();
			statement = conn.createStatement();
			results = statement.executeQuery("SELECT DISTINCT Name FROM schedule;");
			while (results.next())
			result.add(find(results.getString("Name")));
		} 
		finally {
			ConnectionFactory.getInstance().close(null, statement, conn);
		}
		return result;
	}

	@Override
	public void update(Schedule schedule) throws Exception {
		Connection conn = null;
		Statement statement = null;
		try {
			conn = ConnectionFactory.getInstance().getConnection();
			statement = conn.createStatement();
			statement.executeUpdate("DELETE FROM schedule WHERE name = '" + schedule.getName() + "';");
			for (int i = 0; i < schedules.size(); i++) {
				Offering offering = (Offering) schedules.get(i);
				statement.executeUpdate("INSERT INTO schedule VALUES('" + schedule.getName() + "','" + offering.getId() + "');");
			}
		} 
		finally {
			ConnectionFactory.getInstance().close(null, statement, conn);
		}
		
	}

}
