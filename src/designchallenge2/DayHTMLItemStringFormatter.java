package designchallenge2;

public class DayHTMLItemStringFormatter implements ItemStringFormatter {
	private static final String START_TAG = "<html>";
	private static final String END_TAG = "</html>";

	@Override
	public String format(CalendarItem item) {
		String tmp = item.getName();
		if(item instanceof CalendarTask && ((CalendarTask)item).isDone())
			tmp = "<strike>" + ((CalendarTask)item).getName() + "</strike>";
		return START_TAG + tmp + END_TAG;
	}

}
