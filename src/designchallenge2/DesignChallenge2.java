package designchallenge2;

import designchallenge2.controller.CalendarController;
import designchallenge2.controller.DefaultCalendarController;
import designchallenge2.model.CalendarDB;
import designchallenge2.model.CalendarModel;
import designchallenge2.model.DefaultCalendarModel;
import designchallenge2.view.CalendarObserver;
import designchallenge2.view.DefaultCalendarView;

public class DesignChallenge2 {
	public static void main(String[] args) {
		//new DefaultCalendarController(new DefaultCalendarView()).init();
		CalendarModel model = new DefaultCalendarModel(new CalendarDB());
		CalendarController controller = new DefaultCalendarController(model);
		CalendarObserver view = new DefaultCalendarView(model, controller);
		model.attach(view);
	}
}
