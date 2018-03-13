package designchallenge2;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public interface ItemService {
	public List<CalendarItem> getOn(ItemGetFlags flags, LocalDate date);
	public List<CalendarItem> getOn(ItemGetFlags flags, YearMonth yearMonth);
	public int getTaskCount();
	public boolean add(CalendarItem item);
	public boolean update(CalendarItem item);
	public boolean delete(CalendarItem item);
	public boolean markTask(CalendarTask task, boolean done);
}
