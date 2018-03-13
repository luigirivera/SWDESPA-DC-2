package designchallenge2.item;

import java.util.ArrayList;
import java.util.List;

import designchallenge1.CalendarColor;
import designchallenge1.CalendarEvent;

public class LegacyEventConverter {
	public static designchallenge1.CalendarEvent convert(CalendarItem item){
		designchallenge1.CalendarEvent legacyEvent = new CalendarEvent();
		legacyEvent.setName(item.getName());
		if(item instanceof designchallenge2.item.CalendarEvent)
			legacyEvent.setColor(CalendarColor.BLUE);
		else
			legacyEvent.setColor(CalendarColor.GREEN);
		return legacyEvent;
	}
	
	public static List<designchallenge1.CalendarEvent> convert(List<CalendarItem> items){
		List<designchallenge1.CalendarEvent> legacyEvents = new ArrayList<designchallenge1.CalendarEvent>();
		for (CalendarItem item : items) {
			legacyEvents.add(LegacyEventConverter.convert(item));
		}
		return legacyEvents;
	}
}
