package dao;

import model.Course;
import model.Offering;

public interface OfferingDao {
	public Offering create(Course course, String daysTimesCsv) throws Exception;
	public Offering find(int id) throws Exception;
	public void update(Offering offering) throws Exception;

}
