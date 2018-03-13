package designchallenge2;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class DefaultCalendarModel implements CalendarModel {
	private ItemService itemService;
	private List<CalendarObserver> observers;

	public DefaultCalendarModel(CalendarDB calendarDB) {
		this.itemService = new ItemService(calendarDB);
		this.observers = new ArrayList<CalendarObserver>();
	}

	@Override
	public void attach(CalendarObserver obs) {
		observers.add(obs);
	}

	@Override
	public void detach(CalendarObserver obs) {
		observers.remove(obs);
	}

	@Override
	public void notifyObs() {
		for (CalendarObserver obs : observers)
			obs.update();
	}

	@Override
	public List<CalendarItem> getItemsOn(ItemGetFlags flags, LocalDate date) {
		return itemService.getOn(flags, date);
	}
	
	@Override
	public List<CalendarItem> getItemsOn(ItemGetFlags flags, YearMonth yearMonth) {
		return itemService.getOn(flags, yearMonth);
	}

	@Override
	public int getTaskCount() {
		return itemService.getTaskCount();
	}

	@Override
	public boolean addItem(CalendarItem item) {
		return itemService.add(item);
	}

	@Override
	public boolean updateItem(CalendarItem item) {
		return itemService.update(item);
	}

	@Override
	public boolean deleteItem(CalendarItem item) {
		return itemService.delete(item);
	}
	
	@Override
	public boolean markTask(CalendarTask task, boolean done) {
		return itemService.markTask(task, done);
	}

}
