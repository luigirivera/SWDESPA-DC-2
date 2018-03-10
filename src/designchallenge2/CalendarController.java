package designchallenge2;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JOptionPane;

public class CalendarController {
	private CalendarView view;
	
	private final String createPlaceholderName = "Name";
	private final String createPlaceholderDate = "Date";
	private final String createPlaceholderStart = "Start Time";
	private final String createPlaceholderEnd = "End Time";
	
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
		view.addDayToggleButtonListener(new dayToggleBtnListener());
		view.addAgendaToggleButtonListener(new agendaToggleBtnListener());
		view.addCreateNameListener(new createNameFocusListener(), new createNameKeyListener());
		view.addCreateDateListener(new createDateFocusListener(), new createDateKeyListener());
		view.addCreateStartTimeListener(new createStartTimeFocusListener(), new createStartTimeKeyListener());
		view.addCreateEndTimeListener(new createEndTimeFocusListener(), new createEndTimeKeyListener());
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
				view.setYearToday(view.getYearToday() + 1);
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
	
	class createNameKeyListener implements KeyListener{

		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_ENTER)
				saveCreation();
		}

		@Override
		public void keyReleased(KeyEvent arg0) {}

		@Override
		public void keyTyped(KeyEvent arg0) {}
	}
	
	class createDateKeyListener implements KeyListener{

		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_ENTER)
				saveCreation();
		}

		@Override
		public void keyReleased(KeyEvent arg0) {}

		@Override
		public void keyTyped(KeyEvent arg0) {}
	}
	
	class createStartTimeKeyListener implements KeyListener{

		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_ENTER)
				saveCreation();
		}

		@Override
		public void keyReleased(KeyEvent arg0) {}

		@Override
		public void keyTyped(KeyEvent arg0) {}
	}
	
	class createEndTimeKeyListener implements KeyListener{

		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_ENTER)
				saveCreation();
		}

		@Override
		public void keyReleased(KeyEvent arg0) {}

		@Override
		public void keyTyped(KeyEvent arg0) {}
	}
	
	class saveCreateBtnListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			saveCreation();
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
			view.getTaskRB().setSelected(false);
			view.getCreateTOLabel().setVisible(true);
			view.getEndTime().setVisible(true);
			view.getEndTime().setEnabled(true);
		}
		
	}
	
	class taskRadioBtnListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			view.getCreateTOLabel().setVisible(false);
			view.getEndTime().setVisible(false);
			view.getEventRB().setSelected(false);
			view.getEndTime().setEnabled(false);
			view.getEndTime().setText(createPlaceholderEnd);
				
			
		}
		
	}
	
	class dayToggleBtnListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(view.getDay().isSelected())
			{
				view.getDayPanel().setVisible(true);
				view.getAgendaPanel().setVisible(false);
				view.getAgenda().setSelected(false);
			}
			
			else
			{
				view.getAgenda().setSelected(true);
				view.getAgendaPanel().setVisible(true);
				view.getDayPanel().setVisible(false);
				view.getDay().setSelected(false);
			}

			
		}
	}
	
	class agendaToggleBtnListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (view.getAgenda().isSelected()) {
				view.getAgendaPanel().setVisible(true);
				view.getDayPanel().setVisible(false);
				view.getDay().setSelected(false);
			}
			
			else
			{
				view.getDay().setSelected(true);
				view.getDayPanel().setVisible(true);
				view.getAgendaPanel().setVisible(false);
				view.getAgenda().setSelected(false);
			}
		}
		
	}
	
	class createNameFocusListener implements FocusListener{

		@Override
		public void focusGained(FocusEvent arg0) {
			if(view.getCreateName().getText().equals(createPlaceholderName))
			{
				view.getCreateName().setText("");
				view.getCreateName().setForeground(Color.BLACK);
			}
			
		}

		@Override
		public void focusLost(FocusEvent arg0) {
			if(view.getCreateName().getText().equals(""))
			{
				view.getCreateName().setText(createPlaceholderName);
				view.getCreateName().setForeground(Color.GRAY);
			}
			
		}
		
	}
	
	class createDateFocusListener implements FocusListener{

		@Override
		public void focusGained(FocusEvent arg0) {
			if(view.getDate().getText().equals(createPlaceholderDate))
			{
				view.getDate().setText("");
				view.getDate().setForeground(Color.BLACK);
			}
			
		}

		@Override
		public void focusLost(FocusEvent arg0) {
			if(view.getDate().getText().equals(""))
			{
				view.getDate().setText(createPlaceholderDate);
				view.getDate().setForeground(Color.GRAY);
			}
			
		}
		
	}
	
	class createStartTimeFocusListener implements FocusListener{

		@Override
		public void focusGained(FocusEvent arg0) {
			if(view.getStartTime().getText().equals(createPlaceholderStart))
			{
				view.getStartTime().setText("");
				view.getStartTime().setForeground(Color.BLACK);
			}
			
		}

		@Override
		public void focusLost(FocusEvent arg0) {
			if(view.getStartTime().getText().equals(""))
			{
				view.getStartTime().setText(createPlaceholderStart);
				view.getStartTime().setForeground(Color.GRAY);
			}
			
		}
		
	}
	
	class createEndTimeFocusListener implements FocusListener{

		@Override
		public void focusGained(FocusEvent arg0) {
			if(view.getEndTime().getText().equals(createPlaceholderEnd))
			{
				view.getEndTime().setText("");
				view.getEndTime().setForeground(Color.BLACK);
			}
			
		}

		@Override
		public void focusLost(FocusEvent arg0) {
			if(view.getEndTime().getText().equals(""))
			{
				view.getEndTime().setText(createPlaceholderEnd);
				view.getEndTime().setForeground(Color.GRAY);
			}
			
		}
		
	}
	
	private void clearCreatePanel() {
		view.getCreateName().setText(createPlaceholderName);
		view.getCreateName().setForeground(Color.GRAY);
		view.getDate().setText(createPlaceholderDate);
		view.getDate().setForeground(Color.GRAY);
		view.getStartTime().setText(createPlaceholderStart);
		view.getStartTime().setForeground(Color.GRAY);
		view.getEndTime().setText(createPlaceholderEnd);
		view.getEndTime().setForeground(Color.GRAY);
		view.getTaskRB().setSelected(false);
		view.getEventRB().setSelected(true);
		view.getCreateTOLabel().setVisible(true);
		view.getEndTime().setVisible(true);
		view.getEndTime().setEnabled(true);
	}
	
	private void saveCreation() {
		String[] date;
		String[] startTime;
		String[] endTime;
		boolean pass = true;
		
		if(view.getCreateName().getText().equals(createPlaceholderName) || view.getCreateName().getText().isEmpty())
		{
			JOptionPane.showMessageDialog(null, "Please enter a name", "Error", JOptionPane.ERROR_MESSAGE);
			pass = false;
		}
		
		if(view.getDate().getText().equals(createPlaceholderDate) || view.getDate().getText().isEmpty())
		{
			JOptionPane.showMessageDialog(null, "Please enter a date", "Error", JOptionPane.ERROR_MESSAGE);
			pass = false;
		}
		
		if(view.getStartTime().getText().equals(createPlaceholderStart) || view.getStartTime().getText().isEmpty())
		{
			JOptionPane.showMessageDialog(null, "Please enter a start time", "Error", JOptionPane.ERROR_MESSAGE);
			pass = false;
		}
		
		if((view.getEndTime().getText().equals(createPlaceholderEnd) || view.getEndTime().getText().isEmpty()) && view.getEventRB().isSelected())
		{
			JOptionPane.showMessageDialog(null, "Please enter an end time", "Error", JOptionPane.ERROR_MESSAGE);
			pass = false;
		}
		
		// TODO: check for validity of time and date and do the same above if invalid
		// TODO: check if conflicting
		
		if(pass)
		{
			date = view.getDate().getText().split("/");
			startTime = view.getStartTime().getText().split(":");
				
			if(view.getEventRB().isSelected())
				endTime = view.getEndTime().getText().split(":");
			
			// TODO: add the day/agenda for that day

		}
		clearCreatePanel();
		view.getCreatePanel().setVisible(false);
		view.getCreate().setEnabled(true);
	}
}
