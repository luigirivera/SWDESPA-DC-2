package designchallenge1;

public class HTMLEventMarkerFormatter extends HTMLEventStringFormatter {
	@Override
	public String formatEvent(CalendarEvent evt) {
		return "<font color=" + evt.getColor().toHex() + ">"
		+ "◉" + "</font>";
	}
}
