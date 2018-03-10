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
	private final String createPlaceholderStartDate = "Start Date";
	private final String createPlaceholderEndDate = "End Date";
	private final String createPlaceholderStartTime = "Start Time";
	private final String createPlaceholderEndTime = "End Time";
	
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
		view.addCreateStartDateListener(new createStartDateFocusListener(), new createStartDateKeyListener());
		view.addCreateEndDateListener(new createEndDateFocusListener(), new createEndDateKeyListener());
		view.addCreateStartTimeListener(new createStartTimeFocusListener(), new createStartTimeKeyListener());
		view.addCreateEndTimeListener(new createEndTimeFocusListener(), new createEndTimeKeyListener());
		view.addEventCheckBoxListener(new eventCheckBoxListener());
		view.addTaskCheckBoxListener(new taskCheckBoxListener());
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
	
	class createStartDateKeyListener implements KeyListener{

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
	
	class createEndDateKeyListener implements KeyListener{

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
			if(view.getEventRB().isSelected())
				eventRBenable();
			else
			{
				view.getEventRB().setSelected(false);
				view.getTaskRB().setSelected(true);
				taskRBenable();
			}
		}
		
	}
	
	class taskRadioBtnListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(view.getTaskRB().isSelected())
				taskRBenable();
			else
			{
				view.getEventRB().setSelected(true);
				view.getTaskRB().setSelected(false);
				eventRBenable();
			}
			
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
	
	class eventCheckBoxListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(view.getEvent().isSelected())
			{
				if(view.getTask().isSelected())
				{
					//TODO: view both
				}
				else
				{
					//TODO: view only events
				}
			}
			
			else
			{
				if(view.getTask().isSelected())
				{
					//TODO: view only tasks
				}
				
				else
				{
					//TODO: view both
				}
			}
			
		}
		
	}
	
	class taskCheckBoxListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(view.getTask().isSelected())
			{
				if(view.getEvent().isSelected())
				{
					//TODO: view both
				}
				else
				{
					//TODO: view only tasks
				}
			}
			
			else
			{
				if(view.getEvent().isSelected())
				{
					//TODO: view only events
				}
				
				else
				{
					//TODO: view both
				}
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
	
	class createStartDateFocusListener implements FocusListener{

		@Override
		public void focusGained(FocusEvent arg0) {
			if(view.getStartDate().getText().equals(createPlaceholderStartDate))
			{
				view.getStartDate().setText("");
				view.getStartDate().setForeground(Color.BLACK);
			}
			
		}

		@Override
		public void focusLost(FocusEvent arg0) {
			if(view.getStartDate().getText().equals(""))
			{
				view.getStartDate().setText(createPlaceholderStartDate);
				view.getStartDate().setForeground(Color.GRAY);
			}
			
		}
		
	}
	
	class createEndDateFocusListener implements FocusListener{

		@Override
		public void focusGained(FocusEvent arg0) {
			if(view.getEndDate().getText().equals(createPlaceholderEndDate))
			{
				view.getEndDate().setText("");
				view.getEndDate().setForeground(Color.BLACK);
			}
			
		}

		@Override
		public void focusLost(FocusEvent arg0) {
			if(view.getEndDate().getText().equals(""))
			{
				view.getEndDate().setText(createPlaceholderEndDate);;
				view.getEndDate().setForeground(Color.GRAY);
			}
			
		}
		
	}
	
	class createStartTimeFocusListener implements FocusListener{

		@Override
		public void focusGained(FocusEvent arg0) {
			if(view.getStartTime().getText().equals(createPlaceholderStartTime))
			{
				view.getStartTime().setText("");
				view.getStartTime().setForeground(Color.BLACK);
			}
			
		}

		@Override
		public void focusLost(FocusEvent arg0) {
			if(view.getStartTime().getText().equals(""))
			{
				view.getStartTime().setText(createPlaceholderStartTime);
				view.getStartTime().setForeground(Color.GRAY);
			}
			
		}
		
	}
	
	class createEndTimeFocusListener implements FocusListener{

		@Override
		public void focusGained(FocusEvent arg0) {
			if(view.getEndTime().getText().equals(createPlaceholderEndTime))
			{
				view.getEndTime().setText("");
				view.getEndTime().setForeground(Color.BLACK);
			}
			
		}

		@Override
		public void focusLost(FocusEvent arg0) {
			if(view.getEndTime().getText().equals(""))
			{
				view.getEndTime().setText(createPlaceholderEndTime);
				view.getEndTime().setForeground(Color.GRAY);
			}
			
		}
		
	}
	
	private void eventRBenable()
	{
		view.getTaskRB().setSelected(false);
		view.getCreateTOLabelDate().setVisible(true);
		view.getCreateTOLabelTime().setVisible(true);
		view.getEndTime().setVisible(true);
		view.getEndTime().setEnabled(true);
		view.getEndDate().setEnabled(true);
		view.getEndDate().setVisible(true);
		
		view.getStartDate().setBounds(10, 120, 120, 40);
		view.getStartTime().setBounds(10, 160, 120, 40);
		view.getSave().setBounds(300, 120, 90, 40);
		view.getDiscard().setBounds(300, 160, 90, 40);
	}
	
	private void taskRBenable()
	{
		view.getEventRB().setSelected(false);
		view.getCreateTOLabelDate().setVisible(false);
		view.getCreateTOLabelTime().setVisible(false);
		view.getEndTime().setVisible(false);
		view.getEndTime().setEnabled(false);
		view.getEndTime().setText(createPlaceholderEndTime);
		view.getEndTime().setForeground(Color.GRAY);
		view.getEndDate().setEnabled(false);
		view.getEndDate().setVisible(false);
		view.getEndDate().setText(createPlaceholderEndDate);
		view.getEndDate().setForeground(Color.GRAY);
		
		view.getStartDate().setBounds(70, 120, 120, 40);
		view.getStartTime().setBounds(70, 160, 120, 40);
		view.getSave().setBounds(230, 120, 90, 40);
		view.getDiscard().setBounds(230, 160, 90, 40);
	}
	
	private void clearCreatePanel() {
		view.getCreateName().setText(createPlaceholderName);
		view.getCreateName().setForeground(Color.GRAY);
		view.getStartDate().setText(createPlaceholderStartDate);
		view.getStartDate().setForeground(Color.GRAY);
		view.getStartTime().setText(createPlaceholderStartTime);
		view.getStartTime().setForeground(Color.GRAY);
		view.getEndTime().setText(createPlaceholderEndTime);
		view.getEndTime().setForeground(Color.GRAY);
		view.getEndDate().setText(createPlaceholderEndDate);
		view.getEndDate().setForeground(Color.GRAY);
		view.getTaskRB().setSelected(false);
		view.getEventRB().setSelected(true);
		view.getCreateTOLabelDate().setVisible(true);
		view.getCreateTOLabelTime().setVisible(true);
		view.getEndTime().setVisible(true);
		view.getEndTime().setEnabled(true);
		view.getEndDate().setVisible(true);
		view.getEndDate().setEnabled(true);
		
		view.getStartDate().setBounds(10, 120, 120, 40);
		view.getStartTime().setBounds(10, 160, 120, 40);
		view.getSave().setBounds(300, 120, 90, 40);
		view.getDiscard().setBounds(300, 160, 90, 40);
	}
	
	private void saveCreation() {
		String[] startDate, endDate, startTime, endTime;
		boolean pass = true;
		
		if(view.getCreateName().getText().equals(createPlaceholderName) || view.getCreateName().getText().isEmpty())
		{
			JOptionPane.showMessageDialog(null, "Please enter a name", "Error", JOptionPane.ERROR_MESSAGE);
			pass = false;
		}
		
		if(view.getStartDate().getText().equals(createPlaceholderStartDate) || view.getStartDate().getText().isEmpty())
		{
			JOptionPane.showMessageDialog(null, "Please enter a starting date", "Error", JOptionPane.ERROR_MESSAGE);
			pass = false;
		}
		
		if((view.getEndDate().getText().equals(createPlaceholderEndDate) || view.getEndDate().getText().isEmpty()) && view.getEventRB().isSelected())
		{
			JOptionPane.showMessageDialog(null, "Please enter an ending date", "Error", JOptionPane.ERROR_MESSAGE);
			pass = false;
		}
		
		if(view.getStartTime().getText().equals(createPlaceholderStartTime) || view.getStartTime().getText().isEmpty())
		{
			JOptionPane.showMessageDialog(null, "Please enter a starting time", "Error", JOptionPane.ERROR_MESSAGE);
			pass = false;
		}
		
		if((view.getEndTime().getText().equals(createPlaceholderEndTime) || view.getEndTime().getText().isEmpty()) && view.getEventRB().isSelected())
		{
			JOptionPane.showMessageDialog(null, "Please enter an ending time", "Error", JOptionPane.ERROR_MESSAGE);
			pass = false;
		}
		
		// TODO: check for validity of time and date and do the same above if invalid
		// TODO: check if conflicting
		
		if(pass)
		{
			startDate = view.getStartDate().getText().split("/");
			startTime = view.getStartTime().getText().split(":");
				
			if(view.getEventRB().isSelected())
			{
				endTime = view.getEndTime().getText().split(":");
				endDate = view.getEndDate().getText().split("/");
			}
				
			
			// TODO: add the task/event for that day to both JTables

		}
		clearCreatePanel();
		view.getCreatePanel().setVisible(false);
		view.getCreate().setEnabled(true);
	}
}
