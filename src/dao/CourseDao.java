package dao;

import model.Course;

public interface CourseDao {
	public Course create(String name, int credits) throws Exception;
	public Course find(String name) throws Exception;
	public void update(Course course) throws Exception;

}
