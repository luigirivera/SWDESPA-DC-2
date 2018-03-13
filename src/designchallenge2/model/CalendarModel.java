package designchallenge2.model;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import designchallenge2.item.CalendarItem;
import designchallenge2.item.CalendarTask;
import designchallenge2.item.ItemGetFlags;
import designchallenge2.view.CalendarObserver;

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
