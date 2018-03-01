package designchallenge1;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CalendarModel {
	private List<CalendarEvent> events;
	private List<CalendarEvent> pendingEvents;
	private List<CalendarObserver> observers;
	private NotificationTracker notificationTracker;
	private CalendarProgram view;
	
	public CalendarModel() {
		events = new ArrayList<CalendarEvent>();
		pendingEvents = new ArrayList<CalendarEvent>();
		observers = new ArrayList<CalendarObserver>();
		notificationTracker = new NotificationTracker();
		notificationTracker.start();
	}
	
	public void setView(CalendarProgram view) {
		this.view = view;
	}
	
	class NotificationTracker{
		private ScheduledExecutorService ses;
		private int currYear;
		
		NotificationTracker() {
			ses = Executors.newScheduledThreadPool(1);
			currYear = GregorianCalendar.getInstance().get(Calendar.YEAR);
			this.start();
		}
		
		public void start() {
			ses.scheduleAtFixedRate(new Runnable() {
				public void run() {
					if(Calendar.getInstance().get(Calendar.YEAR)!=currYear) {
						currYear = Calendar.getInstance().get(Calendar.YEAR);
						for (CalendarEvent evt : events) {
							if (evt.isRepeating() && currYear>=evt.getDate().get(Calendar.YEAR)) {
								CalendarEvent clone = (CalendarEvent)evt.clone();
								clone.getDate().set(Calendar.YEAR, currYear);
								scheduleEvent(clone);
							}
						}
					}
					List<CalendarEvent> toRemove = new ArrayList<CalendarEvent>();
					for (CalendarEvent evt : pendingEvents) {
						if (evt.isToday()) {
							updateObservers(evt);
							toRemove.add(evt);
						}
					}
					for (CalendarEvent evt : toRemove)
						finishEvent(evt);
				}
			}, 0	, 1, TimeUnit.SECONDS);
		}
	}
	
	public void addEvent(CalendarEvent evt) {
		events.add(evt);
		scheduleEvent(evt);
		updateView();
	}
	
	public void addEvents(List<CalendarEvent> evts) {
		for(CalendarEvent evt : evts) {
			addEvent(evt);
		}
	}
	
	private void scheduleEvent(CalendarEvent evt) {
		if(!pendingEvents.contains(evt))
			pendingEvents.add(evt);
	}
	
	private void finishEvent(CalendarEvent evt) {
		pendingEvents.remove(evt);
	}
	
	public void attach(CalendarObserver obs) {
		observers.add(obs);
	}
	
	public void updateView() {
		if (view!=null)
			view.refreshCurrentPage();
	}
	
	public void updateObservers(CalendarEvent evt) {
		for (CalendarObserver co : observers) {
			co.update(evt);
		}
	}
	
	public List<CalendarEvent> getEventsAt(Calendar calendar) {
		List<CalendarEvent> eventsAt = new ArrayList<CalendarEvent>();
		for (CalendarEvent ce : events) {
			if(ce.isAt(calendar))
				eventsAt.add(ce);
		}
		return eventsAt;
	}
	
	public List<CalendarEvent> getEventsAt(int year, int month, int dayOfMonth){
		Calendar calendar = new Calendar.Builder().setLenient(false).setDate(year, month, dayOfMonth).build();
		return getEventsAt(calendar);
	}
	
	//debug method
	public void printEvents() {
		for (CalendarEvent ce : events) {
			System.out.println(ce);
		}
	}
	
	public void initEvents() {
		EventReader er = new CSVEventReader("res/Philippine Holidays.csv");
		this.addEvents(er.readEvents());
		er = new PSVEventReader("res/DLSU Unicalendar.psv");
		this.addEvents(er.readEvents());
	}
	
	public void outputEvents() {
		EventWriter ew = new CSVEventWriter("res/Philippine Holidays.csv");
		ew.writeEvents(events);
		ew = new PSVEventWriter("res/DLSU Unicalendar.psv");
		ew.writeEvents(events);
	}
}
