package designchallenge1;

import java.util.ArrayList;
import java.util.List;

public abstract class EventReader {
	protected List<CalendarEvent> events;
	
	public List<CalendarEvent> readEvents() {
		events = new ArrayList<CalendarEvent>();
		getInput();
		parseInput();
		return events;
	}
	
	protected abstract void getInput();
	protected abstract void parseInput();
}
