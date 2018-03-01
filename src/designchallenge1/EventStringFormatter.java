package designchallenge1;

import java.util.List;

public interface EventStringFormatter {
	public String formatEvent(CalendarEvent evt);
	public String formatEvents(List<CalendarEvent> evts);
}
