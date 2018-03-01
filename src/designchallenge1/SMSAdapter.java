package designchallenge1;

import sms.SMS;
import sms.SMSView;

public class SMSAdapter implements CalendarObserver {
	private SMSView view;
	
	public SMSAdapter(SMSView view) {
		this.view = view;
	}
	
	@Override
	public void update(CalendarEvent evt) {
		SMS sms = new SMS(evt.getName(), evt.getDate(), evt.getColor().toColor());
		view.sendSMS(sms);
	}
}
