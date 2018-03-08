package designchallenge2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class CalendarController {
	private CalendarView view;
	
	public CalendarController(CalendarView view) {
		this.view = view;
	}
	
	public void init() {
		view.addbtnNextListener(new btnNext_Action());
		view.addbtnPrevListener(new btnPrev_Action());
		view.addcalendarListener(new calendarTableMouseListener());
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
	
	class calendarTableMouseListener implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent evt) {
			int col = view.getCalendarTable().getSelectedColumn();
			int row = view.getCalendarTable().getSelectedRow();
			
			try {
				int day = view.getValidCells().getDayAtCell(row, col);
				view.getDayLabel().setText(view.getMonths()[view.getMonthToday()] + " " + day + ", " + view.getYearToday());
				// TODO: show the day/agenda for that day
				
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
