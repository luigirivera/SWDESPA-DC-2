package designchallenge2.model;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import designchallenge2.item.CalendarItem;
import designchallenge2.item.CalendarTask;
import designchallenge2.item.ItemGetFlags;

public interface ItemService {
	public List<CalendarItem> getOn(ItemGetFlags flags, LocalDate date);
	public List<CalendarItem> getOn(ItemGetFlags flags, YearMonth yearMonth);
	public int getTaskCount();
	public boolean add(CalendarItem item);
	public boolean update(CalendarItem item);
	public boolean delete(CalendarItem item);
	public boolean markTask(CalendarTask task, boolean done);
}
