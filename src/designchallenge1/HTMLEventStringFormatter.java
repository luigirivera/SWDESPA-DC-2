package designchallenge1;

import java.util.List;

public class HTMLEventStringFormatter implements EventStringFormatter {
	public final static String HTMLSTART = "<html>";
	public final static String HTMLEND = "</html>";

	@Override
	public String formatEvent(CalendarEvent evt) {
		return "<font color=" + evt.getColor().toHex() + ">"
		+ evt.getName() + "</font>";
	}

	@Override
	public String formatEvents(List<CalendarEvent> evts) {
		String tmp = "";
		for (CalendarEvent evt : evts) {
			tmp += formatEvent(evt);
			tmp += "<br>";
		}
		return tmp;
	}

}
