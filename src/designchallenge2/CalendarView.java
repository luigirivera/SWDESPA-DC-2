package designchallenge2;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import designchallenge1.CalendarEvent;
import designchallenge1.CalendarModel;
import designchallenge1.CellStringFormatter;
import designchallenge1.EventReader;
import designchallenge1.EventStringFormatter;
import designchallenge1.HTMLCellStringFormatter;
import designchallenge1.HTMLEventStringFormatter;
import designchallenge1.IOEventReader;

public class CalendarView extends JFrame{
	/**** Day Components ****/
	private int yearBound, monthBound, dayBound, yearToday, monthToday;
	private String[] months = { "January", "February", "March", "April", "May", "June", "July", "August", "September",
			"October", "November", "December" };

	/**** Swing Components ****/
	private JLabel monthLabel, titleLabel, dayLabel, filter, createTOLabel;
	private JTextField createName, date, startTime, endTime;
	
	private JButton btnPrev, btnNext, create, today, save, discard;
	private JToggleButton day, agenda;
	private JRadioButton eventRB, taskRB;
	private Container pane;
	private JScrollPane scrollCalendarTable;
	private JPanel calendarPanel, topPanel, createPanel, dayPanel, agendaPanel;
	private JCheckBox event, task;

	/**** Calendar Table Components ***/
	private JTable calendarTable;
	private DefaultTableModel modelCalendarTable;

	/**** Added during the project ****/
	private CalendarModel calendarModel;
	private CellDataHolder validCells;
	private final String createPlaceholderName = "Name";
	private final String createPlaceholderDate = "Date";
	private final String createPlaceholderStart = "Start Time";
	private final String createPlaceholderEnd = "End Time";
	private JTable dayTable;
	private DefaultTableModel modelDayTable;
	private JScrollPane scrollDayTable;

	public CalendarView() {
		super("Calendar Application");
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}
		
		setLayout(null);
		setSize(950, 700);
		
		instantiate();
		init();
		generateCalendar();
		generateDayTable();
		
		setResizable(false);
		setVisible(true);	
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	private void instantiate() {
		calendarPanel = new JPanel();
		topPanel = new JPanel();
		createPanel = new JPanel();
		dayPanel = new JPanel();
		
		monthLabel = new JLabel("January");
		dayLabel = new JLabel("");
		titleLabel = new JLabel("My Productivity Tool");
		filter = new JLabel("Filter");
		createTOLabel = new JLabel("to");
		
		btnPrev = new JButton("<");
		btnNext = new JButton(">");
		create = new JButton("Create");
		today = new JButton("Today");
		save = new JButton("Save");
		discard = new JButton("Discard");
		
		day = new JToggleButton("Day");
		agenda = new JToggleButton("Agenda");
		
		eventRB = new JRadioButton("Event");
		taskRB = new JRadioButton("Task");
		
		event = new JCheckBox("Event");
		task = new JCheckBox("Task");
		
		createName = new JTextField();
		startTime = new JTextField();
		endTime = new JTextField();
		date = new JTextField();

		
		modelCalendarTable = new DefaultTableModel() {
			public boolean isCellEditable(int rowIndex, int mColIndex) {
				return false;
			}
		};
		
		modelDayTable = new DefaultTableModel() {
			public boolean isCellEditable(int rowIndex, int mColIndex) {
				return false;
			}
		};
		
		validCells = new CellDataHolder();
		calendarTable = new JTable(modelCalendarTable);
		dayTable = new JTable(modelDayTable);
		scrollCalendarTable = new JScrollPane(calendarTable);
		scrollDayTable = new JScrollPane(dayTable);
	}
	
	private void init() {
		topPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		dayPanel.setLayout(null);
		calendarPanel.setLayout(null);
		topPanel.setLayout(null);
		createPanel.setLayout(null);
		
		titleLabel.setFont(new Font("Arial", Font.BOLD, 25));
		dayLabel.setFont(new Font("Arial", Font.BOLD, 25));
		monthLabel.setFont(new Font("Arial", Font.PLAIN, 15));
		filter.setFont(new Font("Arial", Font.BOLD, 15));
		createTOLabel.setFont(new Font("Arial", Font.BOLD, 15));
		
		date.setHorizontalAlignment(JTextField.CENTER);
		startTime.setHorizontalAlignment(JTextField.CENTER);
		endTime.setHorizontalAlignment(JTextField.CENTER);
		
		date.setText(createPlaceholderDate);
		startTime.setText(createPlaceholderStart);
		endTime.setText(createPlaceholderEnd);
		createName.setText(createPlaceholderName);
		
		eventRB.setSelected(true);
		today.setEnabled(false);
		
		date.setForeground(Color.GRAY);
		startTime.setForeground(Color.GRAY);
		endTime.setForeground(Color.GRAY);
		createName.setForeground(Color.GRAY);
		
		btnPrev.setMargin(new Insets(0,0,0,0));
		btnNext.setMargin(new Insets(0,0,0,0));
		
		add(calendarPanel);
		calendarPanel.add(monthLabel);
		calendarPanel.add(btnPrev);
		calendarPanel.add(btnNext);
		calendarPanel.add(scrollCalendarTable);
		calendarPanel.add(create);
		calendarPanel.add(filter);
		calendarPanel.add(event);
		calendarPanel.add(task);
		
		add(topPanel);
		topPanel.add(titleLabel);
		topPanel.add(today);
		topPanel.add(day);
		topPanel.add(agenda);
		topPanel.add(dayLabel);
		
		add(createPanel);
		createPanel.add(createName);
		createPanel.add(date);
		createPanel.add(startTime);
		createPanel.add(endTime);
		createPanel.add(eventRB);
		createPanel.add(taskRB);
		createPanel.add(save);
		createPanel.add(discard);
		createPanel.add(createTOLabel);
		createPanel.setVisible(false);
		
		add(dayPanel);
		dayPanel.add(scrollDayTable);
		dayPanel.setVisible(false);
		
		topPanel.setBounds(0,0,this.getWidth(), 70);
		titleLabel.setBounds(10, 10, 250, 50);
		today.setBounds(280, 15, 100, 40);
		dayLabel.setBounds(450, 10, 250, 50);
		day.setBounds(785, 15, 70, 40);
		agenda.setBounds(850, 15, 70, 40);
		
		calendarPanel.setBounds(0, 70, 270, 610);
		create.setBounds(10, 10, 250, 40);
		monthLabel.setBounds(10, 50, 200, 50);
		btnPrev.setBounds(180, 60, 40, 30);
		btnNext.setBounds(220, 60, 40, 30);
		scrollCalendarTable.setBounds(10, 100, 250, 390);
		filter.setBounds(10, 500, 50,50);
		event.setBounds(30, 520, 70, 50);
		task.setBounds(120, 520, 70, 50);
		
		createPanel.setBounds(270, 70, this.getWidth() - 270, 610);
		createName.setBounds(10, 30, 400, 40);
		eventRB.setBounds(40, 70, 70, 50);
		taskRB.setBounds(150, 70, 70, 50);
		date.setBounds(10, 120, 120, 40);
		startTime.setBounds(140, 120, 120, 40);
		createTOLabel.setBounds(270, 120, 20, 40);
		endTime.setBounds(290, 120, 120, 40);
		save.setBounds(230, 170, 90, 40);
		discard.setBounds(320, 170, 90, 40);
		
		dayPanel.setBounds(270, 70, this.getWidth() - 270, 610);
		scrollDayTable.setBounds(20, 20, dayPanel.getWidth()-50, dayPanel.getHeight()-50);
	}
	
	private void generateDayTable() {
		DefaultTableCellRenderer rightRender = new DefaultTableCellRenderer();
		rightRender.setHorizontalAlignment(SwingConstants.RIGHT);
		
		modelDayTable.addColumn("Time");
		modelDayTable.addColumn("Event/Task");
			
		modelDayTable.setColumnCount(2);
		modelDayTable.setRowCount(48);
		
		dayTable.setShowVerticalLines(true);
		dayTable.setGridColor(Color.BLACK);
		dayTable.setRowHeight(75);
		dayTable.getColumnModel().getColumn(0).setCellRenderer(rightRender);
		dayTable.getColumnModel().getColumn(0).setPreferredWidth(100);
		dayTable.getColumnModel().getColumn(1).setPreferredWidth(scrollDayTable.getWidth() - dayTable.getColumnModel().getColumn(0).getWidth() - 45);
		dayTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		dayTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		dayTable.getTableHeader().setReorderingAllowed(false);
		
		for(int i = 0; i < 48; i++)
			if(i%2==0)
				dayTable.setValueAt(i/2 + ":00", i, 0);
				
	}
	
	private void generateCalendar() {
		GregorianCalendar cal = new GregorianCalendar();
		dayBound = cal.get(GregorianCalendar.DAY_OF_MONTH);
		monthBound = cal.get(GregorianCalendar.MONTH);
		yearBound = cal.get(GregorianCalendar.YEAR);
		monthToday = monthBound;
		yearToday = yearBound;

		String[] headers = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" }; // All headers
		for (int i = 0; i < 7; i++) {
			modelCalendarTable.addColumn(headers[i]);
		}

		calendarTable.getParent().setBackground(calendarTable.getBackground()); // Set background

		calendarTable.getTableHeader().setResizingAllowed(false);
		calendarTable.getTableHeader().setReorderingAllowed(false);

		calendarTable.setColumnSelectionAllowed(true);
		calendarTable.setRowSelectionAllowed(true);
		calendarTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		calendarTable.setRowHeight(60);
		modelCalendarTable.setColumnCount(7);
		modelCalendarTable.setRowCount(6);
		
		refreshCalendar(monthBound, yearBound); // Refresh calendar
	
	}
	
	public void refreshCalendar(int month, int year) {
		int nod, som, i, j;

		btnPrev.setEnabled(true);
		btnNext.setEnabled(true);
		if (month == 0 && year <= yearBound - 10)
			btnPrev.setEnabled(false);
		if (month == 11 && year >= yearBound + 100)
			btnNext.setEnabled(false);

		monthLabel.setText(months[month] + " " + yearToday);
		monthLabel.setBounds(10, 50, 360, 50);

		for (i = 0; i < 6; i++)
			for (j = 0; j < 7; j++)
				modelCalendarTable.setValueAt(null, i, j);

		GregorianCalendar cal = new GregorianCalendar(year, month, 1);
		nod = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
		som = cal.get(GregorianCalendar.DAY_OF_WEEK);

		// Added this
		validCells.getList().clear();

		for (i = 1; i <= nod; i++) {
			int row = new Integer((i + som - 2) / 7);
			int column = (i + som - 2) % 7;
			modelCalendarTable.setValueAt(i, row, column);
			// Added lines below
			validCells.getList().add(new CellData(i, row, column));
			try {
				refreshTileEvents(i, row, column);
			} catch (NullPointerException e) {
				System.out.println("No CalendarModel yet");
			}
		}

		calendarTable.setDefaultRenderer(calendarTable.getColumnClass(0), new TableRenderer());
	}
	
	public void refreshCurrentPage() {
		this.refreshCalendar(monthToday, yearToday);
	}

	/* Added this */
	public void refreshTileEvents(int day, int row, int column) throws NullPointerException {
		EventStringFormatter esformatter = new HTMLEventStringFormatter();
		CellStringFormatter csformatter = new HTMLCellStringFormatter();

		modelCalendarTable.setValueAt(csformatter.format(day,
				esformatter.formatEvents(calendarModel.getEventsAt(yearToday, monthToday, day))), row, column);
	}
	
	// ------------LISTENERS------------//
	public void addbtnNextListener(ActionListener e) {
		btnNext.addActionListener(e);
	}
	
	public void addbtnPrevListener(ActionListener e) {
		btnPrev.addActionListener(e);
	}
	
	public void addcalendarListener(MouseListener e) {
		calendarTable.addMouseListener(e);
	}
	
	public void addCreateButtonListener(ActionListener e) {
		create.addActionListener(e);
	}
	
	public void addCreateSaveButtonListener(ActionListener e) {
		save.addActionListener(e);
	}
	
	public void addCreateDiscardButtonListener(ActionListener e) {
		discard.addActionListener(e);
	}
	
	public void addEventRadioButtonListener(ActionListener e) {
		eventRB.addActionListener(e);
	}
	
	public void addTaskRadioButtonListener(ActionListener e) {
		taskRB.addActionListener(e);
	}
	
	public void addDayToggleButtonListener(ActionListener e) {
		day.addActionListener(e);
	}
	
	public void addAgendaToggleButtonListener(ActionListener e) {
		agenda.addActionListener(e);
	}
	
	public void addCreateNameListener(FocusListener f, KeyListener k) {
		createName.addFocusListener(f);
		createName.addKeyListener(k);
	}

	public void addCreateDateListener(FocusListener f, KeyListener k) {
		date.addFocusListener(f);
		date.addKeyListener(k);
	}

	public void addCreateStartTimeListener(FocusListener f, KeyListener k) {
		startTime.addFocusListener(f);
		startTime.addKeyListener(k);
	}

	public void addCreateEndTimeListener(FocusListener f, KeyListener k) {
		endTime.addFocusListener(f);
		endTime.addKeyListener(k);
	}
	
	// ------------GETTERS AND SETTERS------------//
	public int getYearToday() {
		return yearToday;
	}

	public void setYearToday(int yearToday) {
		this.yearToday = yearToday;
	}

	public int getMonthToday() {
		return monthToday;
	}

	public void setMonthToday(int monthToday) {
		this.monthToday = monthToday;
	}

	public JButton getBtnPrev() {
		return btnPrev;
	}

	public void setBtnPrev(JButton btnPrev) {
		this.btnPrev = btnPrev;
	}

	public JButton getBtnNext() {
		return btnNext;
	}

	public void setBtnNext(JButton btnNext) {
		this.btnNext = btnNext;
	}

	public JTable getCalendarTable() {
		return calendarTable;
	}

	public void setCalendarTable(JTable calendarTable) {
		this.calendarTable = calendarTable;
	}

	public CalendarModel getCalendarModel() {
		return calendarModel;
	}

	public void setCalendarModel(CalendarModel calendarModel) {
		this.calendarModel = calendarModel;
	}

	public CellDataHolder getValidCells() {
		return validCells;
	}

	public void setValidCells(CellDataHolder validCells) {
		this.validCells = validCells;
	}

	public JLabel getDayLabel() {
		return dayLabel;
	}

	public void setDayLabel(JLabel dayLabel) {
		this.dayLabel = dayLabel;
	}

	public String[] getMonths() {
		return months;
	}

	public void setMonths(String[] months) {
		this.months = months;
	}

	public JTextField getCreateName() {
		return createName;
	}

	public void setCreateName(JTextField createName) {
		this.createName = createName;
	}

	public JTextField getDate() {
		return date;
	}

	public void setDate(JTextField date) {
		this.date = date;
	}

	public JTextField getStartTime() {
		return startTime;
	}

	public void setStartTime(JTextField startTime) {
		this.startTime = startTime;
	}

	public JTextField getEndTime() {
		return endTime;
	}

	public void setEndTime(JTextField endTime) {
		this.endTime = endTime;
	}

	public JButton getCreate() {
		return create;
	}

	public void setCreate(JButton create) {
		this.create = create;
	}

	public JButton getSave() {
		return save;
	}

	public void setSave(JButton save) {
		this.save = save;
	}

	public JButton getDiscard() {
		return discard;
	}

	public void setDiscard(JButton discard) {
		this.discard = discard;
	}

	public JRadioButton getEventRB() {
		return eventRB;
	}

	public void setEventRB(JRadioButton eventRB) {
		this.eventRB = eventRB;
	}

	public JRadioButton getTaskRB() {
		return taskRB;
	}

	public void setTaskRB(JRadioButton taskRB) {
		this.taskRB = taskRB;
	}

	public JPanel getCreatePanel() {
		return createPanel;
	}

	public void setCreatePanel(JPanel createPanel) {
		this.createPanel = createPanel;
	}

	public JLabel getCreateTOLabel() {
		return createTOLabel;
	}

	public void setCreateTOLabel(JLabel createTOLabel) {
		this.createTOLabel = createTOLabel;
	}

	public JToggleButton getDay() {
		return day;
	}

	public void setDay(JToggleButton day) {
		this.day = day;
	}

	public JToggleButton getAgenda() {
		return agenda;
	}

	public void setAgenda(JToggleButton agenda) {
		this.agenda = agenda;
	}
	
	public JPanel getDayPanel() {
		return dayPanel;
	}

	public void setDayPanel(JPanel dayPanel) {
		this.dayPanel = dayPanel;
	}




	class CellData {
		private int day;
		private int row;
		private int col;

		CellData(int day, int row, int col) {
			this.day = day;
			this.row = row;
			this.col = col;
		}

		public int getDay() {
			return day;
		}

		public void setDay(int day) {
			this.day = day;
		}

		public int getRow() {
			return row;
		}

		public void setRow(int row) {
			this.row = row;
		}

		public int getCol() {
			return col;
		}

		public void setCol(int col) {
			this.col = col;
		}
		
		public boolean isAt(int row, int col) {
			return this.row==row && this.col==col;
		}

		@Override
		public boolean equals(Object o) {
			if (o instanceof CellData)
				return this.day == ((CellData) o).getDay() && this.row == ((CellData) o).getRow()
						&& this.col == ((CellData) o).getCol();
			else
				return false;
		}

	}
	
	class CellDataHolder {
		private List<CellData> list;

		CellDataHolder() {
			list = new ArrayList<CellData>();
		}

		public List<CellData> getList() {
			return list;
		}
		
		public int getDayAtCell(int row, int col) throws IllegalArgumentException {
			for (CellData cd : list) {
				if(cd.isAt(row, col))
					return cd.getDay();
			}
			throw new IllegalArgumentException("Invalid coordinates");
		}

	}


}
