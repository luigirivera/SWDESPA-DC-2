package designchallenge2.controller;

import java.time.LocalDateTime;

import designchallenge2.item.CalendarItem;
import designchallenge2.item.CalendarTask;

public interface CalendarController {
	public void addEvent(String name, LocalDateTime start, LocalDateTime end);
	public void addTask(String name, LocalDateTime start);
	public void markTask(CalendarTask task, boolean done);
	public void deleteItem(CalendarItem item);
}
