package designchallenge2;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
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

	/**** Swing Components ****/
	private JLabel monthLabel, yearLabel;
	private JButton btnPrev, btnNext;
	private JComboBox cmbYear;
	private Container pane;
	private JScrollPane scrollCalendarTable;
	private JPanel calendarPanel;

	/**** Calendar Table Components ***/
	private JTable calendarTable;
	private DefaultTableModel modelCalendarTable;

	/**** Added during the project ****/
	private CalendarModel calendarModel;
	private CellDataHolder validCells;

	public CalendarView() {
		super("Calendar Application");
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}
		setLayout(null);
		setSize(1060, 750);
		
		instantiate();
		init();
		generateCalendar();
		
		setResizable(false);
		setVisible(true);
		
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	private void instantiate() {
		monthLabel = new JLabel("January");
		yearLabel = new JLabel("Change year:");
		cmbYear = new JComboBox();
		btnPrev = new JButton("<<");
		btnNext = new JButton(">>");
		modelCalendarTable = new DefaultTableModel() {
			public boolean isCellEditable(int rowIndex, int mColIndex) {
				return false;
			}
		};
		validCells = new CellDataHolder();
		calendarTable = new JTable(modelCalendarTable);
		scrollCalendarTable = new JScrollPane(calendarTable);
		calendarPanel = new JPanel();
	}
	
	private void init() {
		calendarPanel.setBorder(BorderFactory.createTitledBorder("Calendar"));
		
		calendarPanel.setLayout(null);
		
		add(calendarPanel);
		calendarPanel.add(monthLabel);
		calendarPanel.add(yearLabel);
		calendarPanel.add(cmbYear);
		calendarPanel.add(btnPrev);
		calendarPanel.add(btnNext);
		calendarPanel.add(scrollCalendarTable);

		calendarPanel.setBounds(0, 0, 640, 670);
		monthLabel.setBounds(320 - monthLabel.getPreferredSize().width / 2, 50, 200, 50);
		yearLabel.setBounds(20, 610, 160, 40);
		cmbYear.setBounds(460, 610, 160, 40);
		btnPrev.setBounds(20, 50, 100, 50);
		btnNext.setBounds(520, 50, 100, 50);
		scrollCalendarTable.setBounds(20, 100, 600, 500);
		
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

		calendarTable.setRowHeight(76);
		modelCalendarTable.setColumnCount(7);
		modelCalendarTable.setRowCount(6);

		for (int i = yearBound - 100; i <= yearBound + 100; i++) {
			cmbYear.addItem(String.valueOf(i));
		}
		
		refreshCalendar(monthBound, yearBound); // Refresh calendar
	
	}
	
	public void refreshCalendar(int month, int year) {
		String[] months = { "January", "February", "March", "April", "May", "June", "July", "August", "September",
				"October", "November", "December" };
		int nod, som, i, j;

		btnPrev.setEnabled(true);
		btnNext.setEnabled(true);
		if (month == 0 && year <= yearBound - 10)
			btnPrev.setEnabled(false);
		if (month == 11 && year >= yearBound + 100)
			btnNext.setEnabled(false);

		monthLabel.setText(months[month]);
		monthLabel.setBounds(320 - monthLabel.getPreferredSize().width / 2, 50, 360, 50);

		cmbYear.setSelectedItem("" + year);

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
	
	public void addcmbYearListener(ActionListener e) {
		cmbYear.addActionListener(e);
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

	public JComboBox getCmbYear() {
		return cmbYear;
	}

	public void setCmbYear(JComboBox cmbYear) {
		this.cmbYear = cmbYear;
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
