package designchallenge1;

import java.util.Calendar;

import facebook.FBView;

public class FBAdapter implements CalendarObserver {
	private FBView view;

	public FBAdapter(FBView view) {
		this.view = view;
	}

	@Override
	public void update(CalendarEvent evt) {
		view.showNewEvent(evt.getName(), evt.getDate().get(Calendar.MONTH)+1,
				evt.getDate().get(Calendar.DAY_OF_MONTH), evt.getDate().get(Calendar.YEAR),
				evt.getColor().toColor());
	}

}
