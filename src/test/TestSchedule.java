package test;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

import junit.framework.TestCase;
import model.Course;
import model.Offering;
import model.Schedule;
import dao.CourseDao;
import dao.OfferingDao;
import dao.ScheduleDao;
import daoImpl.CourseDaoImpl;
import daoImpl.OfferingDaoImpl;
import daoImpl.ScheduleDaoImpl;

public class TestSchedule extends TestCase {
	
	private CourseDao courseDao = new CourseDaoImpl();
	private ScheduleDao scheduleDao = new ScheduleDaoImpl();
	private OfferingDao offeringDao = new OfferingDaoImpl();
	
	public TestSchedule(String name) {
		super(name);
	}
	@Test
	public void testMinCredits() {
		Schedule schedule = new Schedule("name");
		Collection<String> analysis = schedule.analysis();
		assertEquals(1, analysis.size());
		assertTrue(analysis.contains("Too few credits"));
	}
	@Test
	public void testJustEnoughCredits() {
		Course cs110 = new Course("CS110", 11);
		Offering mwf10 = new Offering(1, cs110, "M10,W10,F10");
		Schedule schedule = new Schedule("name");
		schedule.add(mwf10);
		List<String> analysis = schedule.analysis();
		assertEquals(1, analysis.size());
		assertTrue(analysis.contains("Too few credits"));
		schedule = new Schedule("name");
		Course cs101 = new Course("CS101", 12);
		Offering th11 = new Offering(1, cs101, "T11,H11");
		schedule.add(th11);
		analysis = schedule.analysis();
		assertEquals(0, analysis.size());
	}
	@Test
	public void testMaxCredits() {
		Course cs110 = new Course("CS110", 20);
		Offering mwf10 = new Offering(1, cs110, "M10,W10,F10");
		Schedule schedule = new Schedule("name");
		schedule.add(mwf10);
		List<String> analysis = schedule.analysis();
		assertEquals(1, analysis.size());
		assertTrue(analysis.contains("Too many credits"));
		schedule.authorizeOverload(true);
		analysis = schedule.analysis();
		assertEquals(0, analysis.size());
	}
	@Test
	public void testJustBelowMax() {
		Course cs110 = new Course("CS110", 19);
		Offering mwf10 = new Offering(1, cs110, "M10,W10,F10");
		Schedule schedule = new Schedule("name");
		schedule.add(mwf10);
		List<String> analysis = schedule.analysis();
		assertEquals(1, analysis.size());
		assertTrue(analysis.contains("Too many credits"));
		schedule = new Schedule("name");
		Course cs101 = new Course("CS101", 18);
		Offering th11 = new Offering(1, cs101, "T11,H11");
		schedule.add(th11);
		analysis = schedule.analysis();
		assertEquals(0, analysis.size());
	}
	@Test
	public void testDupCourses() {
		Course cs110 = new Course("CS110", 6);
		Offering mwf10 = new Offering(1, cs110, "M10,W10,F10");
		Offering th11 = new Offering(1, cs110, "T11,H11");
		Schedule schedule = new Schedule("name");
		schedule.add(mwf10);
		schedule.add(th11);
		List<String> analysis = schedule.analysis();
		assertEquals(1, analysis.size());
		assertTrue(analysis.contains("Same course twice - CS110"));
	}
	@Test
	public void testOverlap() {
		Schedule schedule = new Schedule("name");
		Course cs110 = new Course("CS110", 6);
		Offering mwf10 = new Offering(1, cs110, "M10,W10,F10");
		schedule.add(mwf10);
		Course cs101 = new Course("CS101", 6);
		Offering mixed = new Offering(1, cs101, "M10,W11,F11");
		schedule.add(mixed);
		List<String> analysis = schedule.analysis();
		assertEquals(1, analysis.size());
		assertTrue(analysis.contains("Course overlap - M10"));
		Course cs102 = new Course("CS102", 1);
		Offering mixed2 = new Offering(1, cs102, "M9,W10,F11");
		schedule.add(mixed2);
		analysis = schedule.analysis();
		assertEquals(3, analysis.size());
		assertTrue(analysis.contains("Course overlap - M10"));
		assertTrue(analysis.contains("Course overlap - W10"));
		assertTrue(analysis.contains("Course overlap - F11"));
	}
	@Test
	public void testCourseCreate() throws Exception {
		Course c = courseDao.create("CS202", 1);
		Course c2 = courseDao.find("CS202");
		assertEquals("CS202", c2.getName());
		Course c3 = courseDao.find("Nonexistent");
		assertNull(c3);
	}
	@Test
	public void testOfferingCreate() throws Exception {
		Course c = courseDao.create("CS202", 2);
		Offering offering = offeringDao.create(c, "M10");
		assertNotNull(offering);
	}
	@Test
	public void testPersistentSchedule() throws Exception {
		Schedule s = scheduleDao.create("Bob");
		assertNotNull(s);
	}
	@Test
	public void testScheduleUpdate() throws Exception {
		Course cs101 = courseDao.create("CS101", 3);
		courseDao.update(cs101);
		Offering off1 = offeringDao.create(cs101, "M10");
		
		offeringDao.update(off1);
		Offering off2 = offeringDao.create(cs101, "T9");
		offeringDao.update(off1);
		Schedule s = scheduleDao.create("a");
		s.add(off1);
		s.add(off2);
		scheduleDao.update(s);
		Schedule s2 = scheduleDao.create("Alice");
		s2.add(off1);
		scheduleDao.update(s2);
		Schedule s3 = scheduleDao.find("Xiu");
		assertEquals(2, s3.schedule.size());
		Schedule s4 = scheduleDao.find("Alice");
		assertEquals(1, s4.schedule.size());
	}
	
}
