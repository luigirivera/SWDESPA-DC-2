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
		view.addCreateButtonListener(new createbtnListener());
		view.addCreateSaveButtonListener(new saveCreateBtnListener());
		view.addCreateDiscardButtonListener(new discardCreateBtnListener());
		view.addEventRadioButtonListener(new eventRadioBtnListener());
		view.addTaskRadioButtonListener(new taskRadioBtnListener());
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
	
	class createbtnListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			view.getCreatePanel().setVisible(true);
			view.getCreate().setEnabled(false);
		}
		
	}
	
	class saveCreateBtnListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			view.getCreatePanel().setVisible(false);
			view.getCreate().setEnabled(true);
			
			// TODO: add the day/agenda for that day
			clearCreatePanel();
		}
	}
	
	class discardCreateBtnListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			view.getCreatePanel().setVisible(false);
			view.getCreate().setEnabled(true);
			clearCreatePanel();
		}
	}
	
	class eventRadioBtnListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(view.getEventRB().isSelected())
			{
				view.getTaskRB().setSelected(false);
				view.getCreateTOLabel().setVisible(true);
				view.getEndTime().setVisible(true);
				view.getEndTime().setEnabled(true);
			}
		}
		
	}
	
	class taskRadioBtnListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(view.getTaskRB().isSelected())
			{
				view.getCreateTOLabel().setVisible(false);
				view.getEndTime().setVisible(false);
				view.getEventRB().setSelected(false);
				view.getEndTime().setEnabled(false);
			}
				
			
		}
		
	}
	
	private void clearCreatePanel() {
		view.getCreateName().setText("");
		view.getDate().setText("");
		view.getStartTime().setText("");
		view.getEndTime().setText("");
		view.getTaskRB().setSelected(false);
		view.getEventRB().setSelected(false);
	}
}
