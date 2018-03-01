package designchallenge1;

import java.util.List;

public abstract class EventWriter {
	protected List<CalendarEvent> calendarEvents;
	
	public void writeEvents(List<CalendarEvent> calendarEvents) {
		this.calendarEvents = calendarEvents;
		getData();
		prepareData();
		outputData();
	}
	
	protected abstract void getData();
	protected abstract void prepareData();
	protected abstract void outputData();
}
