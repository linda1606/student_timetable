package test;
import junit.framework.TestCase;
import model.Course;
import model.Offering;
import model.Schedule;
import student_timetable.Report;
import dao.CourseDao;
import dao.OfferingDao;
import dao.ScheduleDao;
import daoImpl.CourseDaoImpl;
import daoImpl.OfferingDaoImpl;
import daoImpl.ScheduleDaoImpl;

public class TestReport extends TestCase {
	private CourseDao courseDao = new CourseDaoImpl();
	private ScheduleDao scheduleDao = new ScheduleDaoImpl();
	private OfferingDao offeringDao = new OfferingDaoImpl();

	public TestReport(String name) { 
		super(name); 
	}
	
	public void testEmptyReport() throws Exception {
		scheduleDao.deleteAll();
		Report report = new Report();
		StringBuffer buffer = new StringBuffer();
		report.write(buffer);
		assertEquals("Number of scheduled offerings: 0\n", buffer.toString());
	}
	
	public void testReport() throws Exception {
		scheduleDao.deleteAll();
		Course cs101 = courseDao.create("CS101", 3);
		courseDao.update(cs101);
		Offering off1 = offeringDao.create(cs101, "M10");
		offeringDao.update(off1);
		Offering off2 = offeringDao.create(cs101, "T9");
		offeringDao.update(off1);
		Schedule s = scheduleDao.create("Bob");
		s.add(off1);
		s.add(off2);
		scheduleDao.update(s);
		Schedule s2 = scheduleDao.create("Alice");
		s2.add(off1);
		scheduleDao.update(s);
		Report report = new Report();
		StringBuffer buffer = new StringBuffer();
		report.write(buffer);
		String result = buffer.toString();
		String valid1 = "CS101 M10\n\tAlice\n\tBob\n" + "CS101 T9\n\tBob\n" + "Number of scheduled offerings: 2\n";
		String valid2 = "CS101 T9\n\tBob\n" + "CS101 M10\n\tAlice\n\tBob\n" + "Number of scheduled offerings: 2\n";
		assertTrue(result.equals(valid1) || result.equals(valid2));
	}
}
