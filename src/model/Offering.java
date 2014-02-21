package model;

public class Offering {
	private int id;
	private Course course;
	private String daysTimes;

	public void setId(int id) {
		this.id = id;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public void setDaysTimes(String daysTimes) {
		this.daysTimes = daysTimes;
	}

	public Offering(int id, Course course, String daysTimesCsv) {
		this.id = id;
		this.course = course;
		this.daysTimes = daysTimesCsv;
	}

	public int getId() {
		return id;
	}

	public Course getCourse() {
		return course;
	}

	public String getDaysTimes() {
		return daysTimes;
	}

	public String toString() {
		return "Offering " + getId() + ": " + getCourse() + " meeting " + getDaysTimes();
	}
	
}
