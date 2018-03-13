package designchallenge2;

import java.time.LocalDateTime;

public interface CalendarController {
	public void addEvent(String name, LocalDateTime start, LocalDateTime end);
	public void addTask(String name, LocalDateTime start);
	public void markTask(CalendarTask task, boolean done);
	public void deleteItem(CalendarItem item);
}
