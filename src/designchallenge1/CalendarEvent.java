package designchallenge1;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class CalendarEvent implements Cloneable{
	private String name;
	private CalendarColor color;
	private Calendar date;
	private boolean repeating;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CalendarColor getColor() {
		return color;
	}

	public void setColor(CalendarColor color) {
		this.color = color;
	}

	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}

	public void setDate(int year, int month, int dayOfMonth) {
		this.date = new GregorianCalendar.Builder().setLenient(false).setDate(year, month, dayOfMonth).build();
	}

	public boolean isRepeating() {
		return repeating;
	}

	public void setRepeating(boolean repeating) {
		this.repeating = repeating;
	}

	public boolean isAt(Calendar calendar) {
		return (date.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)
				|| calendar.get(Calendar.YEAR) >= date.get(Calendar.YEAR) && repeating)
				&& date.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)
				&& date.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH);
	}

	public boolean isAt(int year, int month, int dayOfMonth) {
		Calendar calendar = new GregorianCalendar.Builder().setLenient(false).setDate(year, month, dayOfMonth).build();
		return isAt(calendar);
	}

	public boolean isToday() {
		Calendar today = GregorianCalendar.getInstance();
		return isAt(today);
	}
	
	@Override
	public Object clone() {
		CalendarEvent clone = new CalendarEvent();
		clone.setName(this.name);
		clone.setColor(this.color);
		clone.setDate((Calendar)this.date.clone());
		clone.setRepeating(this.repeating);
		return clone;
	}

	@Override
	public String toString() {
		return name + " " + color.toString() + " " + (date.get(Calendar.MONTH) + 1) + "/"
				+ date.get(Calendar.DAY_OF_MONTH) + "/" + date.get(Calendar.YEAR) + " " + repeating;
	}

}
