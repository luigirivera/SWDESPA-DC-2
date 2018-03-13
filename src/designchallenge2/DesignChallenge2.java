package designchallenge2;

public class DesignChallenge2 {
	public static void main(String[] args) {
		//new DefaultCalendarController(new DefaultCalendarView()).init();
		CalendarModel model = new DefaultCalendarModel(new CalendarDB());
		CalendarController controller = new DefaultCalendarController(model);
		CalendarObserver view = new DefaultCalendarView(model, controller);
		model.attach(view);
	}
}
