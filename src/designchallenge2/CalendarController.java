package designchallenge2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import designchallenge1.CalendarEvent;
import designchallenge1.EventReader;
import designchallenge1.IOEventReader;

public class CalendarController {
	private CalendarView view;
	
	public CalendarController(CalendarView view) {
		this.view = view;
	}
	
	public void init() {
		view.addbtnNextListener(new btnNext_Action());
		view.addbtnPrevListener(new btnPrev_Action());
		view.addcmbYearListener(new cmbYear_Action());
	}
	
	class btnPrev_Action implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (view.getMonthToday() == 0) {
				view.setMonthToday(11);
				view.setYearToday(view.getYearToday() - 1);
			} else {
				view.setMonthToday(view.getMonthToday()-1);
			}
			view.refreshCalendar(view.getMonthToday(), view.getYearToday());
		}
	}

	class btnNext_Action implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (view.getMonthToday() == 11) {
				view.setMonthToday(0);
				view.setYearToday(view.getYearToday() - 1);
			} else {
				view.setMonthToday(view.getMonthToday()+1);
			}
			view.refreshCalendar(view.getMonthToday(), view.getYearToday());
		}
	}

	class cmbYear_Action implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (view.getCmbYear().getSelectedItem() != null) {
				String b = view.getCmbYear().getSelectedItem().toString();
				view.setYearToday(Integer.parseInt(b));
				view.refreshCalendar(view.getMonthToday(), view.getYearToday());
			}
		}
	}
	
	class calendarTableMouseListener implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent evt) {
			int col = view.getCalendarTable().getSelectedColumn();
			int row = view.getCalendarTable().getSelectedRow();
			try {
				int day = view.getValidCells().getDayAtCell(row, col);
				EventReader er = new IOEventReader(view.getMonthToday(), day, view.getYearToday());
				List<CalendarEvent> cevts = er.readEvents();
				if (cevts.size()>0) {
					view.getCalendarModel().addEvents(cevts);
					view.getCalendarModel().outputEvents();
				}
			} catch (IllegalArgumentException e) {
				System.out.println(e.getMessage());
			}
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {}

		@Override
		public void mouseExited(MouseEvent arg0) {}

		@Override
		public void mousePressed(MouseEvent arg0) {}

		@Override
		public void mouseReleased(MouseEvent arg0) {}
		
	}
}
