package designchallenge2;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public interface CalendarModel {
	public void attach(CalendarObserver obs);
	public void detach(CalendarObserver obs);
	public void notifyObs();
	public List<CalendarItem> getItemsOn(ItemGetFlags flags, LocalDate date);
	public List<CalendarItem> getItemsOn(ItemGetFlags flags, YearMonth yearMonth);
	public int getTaskCount();
	public boolean addItem(CalendarItem item);
	public boolean updateItem(CalendarItem item);
	public boolean deleteItem(CalendarItem item);
	public boolean markTask(CalendarTask task, boolean done);
}
