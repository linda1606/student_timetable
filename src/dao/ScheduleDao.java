package dao;

import java.util.Collection;

import model.Offering;
import model.Schedule;

public interface ScheduleDao {
	public void deleteAll() throws Exception;
	public Schedule create(String name) throws Exception;
	public Schedule find(String name);
	public Collection<Schedule> all() throws Exception;
	public void update(Schedule schedule) throws Exception;
}
