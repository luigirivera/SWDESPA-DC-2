package designchallenge2.model;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import designchallenge2.item.CalendarItem;
import designchallenge2.item.CalendarTask;
import designchallenge2.item.ItemGetFlags;
import designchallenge2.view.CalendarObserver;

public class DefaultCalendarModel implements CalendarModel {
	private DBItemService itemService;
	private List<CalendarObserver> observers;

	public DefaultCalendarModel(CalendarDB calendarDB) {
		this.itemService = new DBItemService(calendarDB);
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
