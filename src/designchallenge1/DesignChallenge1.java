/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package designchallenge1;

import facebook.FBView;
import sms.SMSView;

/**
 *
 * @author Arturo III
 */
public class DesignChallenge1 {

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		// TODO code application logic here
		CalendarModel cm = new CalendarModel();
		cm.attach(new FBAdapter(new FBView()));
		cm.attach(new SMSAdapter(new SMSView()));
		CalendarProgram cp = new CalendarProgram();
		cm.setView(cp);
		cp.setCalendarModel(cm);
		cm.initEvents();
	}
}
