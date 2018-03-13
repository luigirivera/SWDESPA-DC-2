package designchallenge2;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.LocalDateTime;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class DefaultCalendarController implements CalendarController{
	private static final String createPlaceholderName = "Name";
	private static final String createPlaceholderStartDate = "Start Date";
	private static final String createPlaceholderEndDate = "End Date";
	private static final String createPlaceholderStartTime = "Start Time";
	private static final String createPlaceholderEndTime = "End Time";
	
	private CalendarModel model;
	
	public DefaultCalendarController(CalendarModel model) {
		this.model = model;
	}
	
	// OVERRIDE METHODS
	
	@Override
	public void addEvent(String name, LocalDateTime start, LocalDateTime end) {
		CalendarEvent event = new CalendarEvent();
		event.setName(name);
		event.setStart(start);
		event.setEnd(end);
		model.addItem(event);
	}

	@Override
	public void addTask(String name, LocalDateTime start) {
		CalendarTask task = new CalendarTask();
		task.setName(name);
		task.setStart(start);
		task.setEnd(start.plus(CalendarTask.DURATION));
		task.setDone(false);
		model.addItem(task);
	}

	@Override
	public void markTask(CalendarTask task, boolean done) {
		model.markTask(task, done);
	}

	@Override
	public void deleteItem(CalendarItem item) {
		model.deleteItem(item);
	}
	
	
}
