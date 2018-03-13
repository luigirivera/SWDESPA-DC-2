package designchallenge2;

public class AgendaHTMLItemStringFormatter implements ItemStringFormatter{
	private static final String START_TAG = "<html>";
	private static final String END_TAG = "</html>";

	@Override
	public String format(CalendarItem item) {
		String tmp = item.getName();
		if(item instanceof CalendarEvent)
			tmp = String.format("<font color=%s>%s</font>", CalendarEvent.DEFAULT_COLOR, item.getName());
		else if(item instanceof CalendarTask) {
			tmp = item.getName();
			if(((CalendarTask)item).isDone())
				tmp = String.format("<strike>%s</strike>", item.getName());
			tmp = String.format("<font color=%s>%s</font>", CalendarTask.DEFAULT_COLOR, tmp);
		}
		return START_TAG + tmp + END_TAG;
	}

}
