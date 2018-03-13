package designchallenge2.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import designchallenge1.CellStringFormatter;
import designchallenge1.EventStringFormatter;
import designchallenge1.HTMLCellMarkerFormatter;
import designchallenge1.HTMLCellStringFormatter;
import designchallenge1.HTMLEventMarkerFormatter;
import designchallenge1.HTMLEventStringFormatter;
import designchallenge2.controller.CalendarController;
import designchallenge2.item.CalendarEvent;
import designchallenge2.item.CalendarItem;
import designchallenge2.item.CalendarTask;
import designchallenge2.item.ItemGetFlags;
import designchallenge2.item.LegacyEventConverter;
import designchallenge2.model.CalendarModel;

public class DefaultCalendarView extends JFrame implements CalendarObserver{
	/**** Day Components ****/
	private int yearBound, monthBound, dayBound, yearToday, monthToday;
	private String[] months = { "January", "February", "March", "April", "May", "June", "July", "August", "September",
			"October", "November", "December" };

	/**** Swing Components ****/
	private JLabel monthLabel, titleLabel, dayLabel, filter, createTOLabelDate, createTOLabelTime, taskCountLabel;
	private JTextField createName, startDate, endDate;
	
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
	private CalendarModel model;
	private CalendarController controller;
	private DefaultCalendarView view;
	
	private List<CalendarItem> monthItems;
	private List<CalendarItem> dayItems;
	private ItemGetFlags flags;
	private int taskCount;
	private int dayToday;
	private JComboBox<LocalTime> startTime, endTime;
	private CellDataHolder validCells;
	private final String createPlaceholderName = "Name";
	private final String createPlaceholderStartDate = "Start Date";
	private final String createPlaceholderEndDate = "YYYY/MM/DD";
	private JTable dayTable;
	private DefaultTableModel modelDayTable;
	private JScrollPane scrollDayTable;
	private JTable agendaTable;
	private DefaultTableModel modelAgendaTable;
	private JScrollPane scrollAgendaTable;
	private JPopupMenu dayMenu;
	private JMenuItem delete, markTask;

	public DefaultCalendarView(CalendarModel model, CalendarController controller) {
		super("Calendar Application");
		
		this.model = model;
		this.controller = controller;
		this.view = this;
		
		this.monthItems = new ArrayList<CalendarItem>();
		this.dayItems = new ArrayList<CalendarItem>();
		
		this.flags = new ItemGetFlags();
		flags.setAll(true);
		
		this.taskCount = 0;
		
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
		generateAgendaTable();
		
		setResizable(false);
		setVisible(true);	
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		this.initListeners();
		this.update();
		
		this.toggleAgendaView(false);
		this.toggleDayView(true);
	}
	
	private void instantiate() {
		calendarPanel = new JPanel();
		topPanel = new JPanel();
		createPanel = new JPanel();
		dayPanel = new JPanel();
		agendaPanel = new JPanel();
		
		monthLabel = new JLabel("January");
		dayLabel = new JLabel("");
		titleLabel = new JLabel("My Productivity Tool");
		filter = new JLabel("Filter");
		createTOLabelDate = new JLabel("to");
		createTOLabelTime = new JLabel("to");
		taskCountLabel = new JLabel();
		
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
		startTime = new JComboBox<LocalTime>();
		endTime = new JComboBox<LocalTime>();
		startDate = new JTextField();
		endDate = new JTextField();
		
		dayMenu = new JPopupMenu();
		delete = new JMenuItem("Delete");
		markTask = new JMenuItem("Mark Task");
		
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
		
		modelAgendaTable = new DefaultTableModel() {
			public boolean isCellEditable(int rowIndex, int mColIndex) {
				return false;
			}
		};
		
		validCells = new CellDataHolder();
		calendarTable = new JTable(modelCalendarTable);
		dayTable = new JTable(modelDayTable);
		agendaTable = new JTable(modelAgendaTable);
		scrollCalendarTable = new JScrollPane(calendarTable);
		scrollDayTable = new JScrollPane(dayTable);
		scrollAgendaTable = new JScrollPane(agendaTable);
	}
	
	private void init() {
		topPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		dayPanel.setLayout(null);
		calendarPanel.setLayout(null);
		topPanel.setLayout(null);
		createPanel.setLayout(null);
		agendaPanel.setLayout(null);
		
		dayMenu.add(markTask);
		dayMenu.add(delete);
		
		taskCountLabel.setText("<html><div style='text-align: center;'><body>Tasks Left:<br>(number here)</body></div></html>");
		
		titleLabel.setFont(new Font("Arial", Font.BOLD, 25));
		dayLabel.setFont(new Font("Arial", Font.BOLD, 25));
		monthLabel.setFont(new Font("Arial", Font.PLAIN, 15));
		filter.setFont(new Font("Arial", Font.BOLD, 15));
		createTOLabelDate.setFont(new Font("Arial", Font.BOLD, 15));
		createTOLabelTime.setFont(new Font("Arial", Font.BOLD, 15));
		taskCountLabel.setFont(new Font("Arial", Font.BOLD, 15));
		taskCountLabel.setForeground(Color.decode("#DD0000"));
		
		startDate.setHorizontalAlignment(JTextField.CENTER);
		endDate.setHorizontalAlignment(JTextField.CENTER);
		
		startDate.setText(createPlaceholderStartDate);
		createName.setText(createPlaceholderName);
		endDate.setText(createPlaceholderEndDate);
		
		LocalTime tmpTime = LocalTime.of(0, 0);
		for(int i=0 ; i<48 ; i++) {
			startTime.addItem(tmpTime);
			endTime.addItem(tmpTime);
			tmpTime = tmpTime.plusMinutes(30);
		}
		
		eventRB.setSelected(true);
		
		startDate.setForeground(Color.GRAY);
		createName.setForeground(Color.GRAY);
		endDate.setForeground(Color.GRAY);
		
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
		topPanel.add(taskCountLabel);
		
		add(createPanel);
		createPanel.add(createName);
		createPanel.add(startDate);
		createPanel.add(endDate);
		createPanel.add(startTime);
		createPanel.add(endTime);
		createPanel.add(eventRB);
		createPanel.add(taskRB);
		createPanel.add(save);
		createPanel.add(discard);
		createPanel.add(createTOLabelDate);
		createPanel.add(createTOLabelTime);
		createPanel.setVisible(false);
		
		add(dayPanel);
		dayPanel.add(scrollDayTable);
		dayPanel.setVisible(false);
		
		add(agendaPanel);
		agendaPanel.add(scrollAgendaTable);
		agendaPanel.setVisible(false);
		
		topPanel.setBounds(0,0,this.getWidth(), 70);
		titleLabel.setBounds(10, 10, 250, 50);
		today.setBounds(280, 15, 100, 40);
		dayLabel.setBounds(400, 10, 250, 50);
		taskCountLabel.setBounds(660, 5, 100, 60);
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
		startDate.setBounds(10, 120, 120, 40);
		createTOLabelDate.setBounds(140, 120, 20, 40);
		endDate.setBounds(160, 120, 120, 40);
		startTime.setBounds(10, 160, 120, 40);
		createTOLabelTime.setBounds(140, 160, 20, 40);
		endTime.setBounds(160, 160, 120, 40);
		save.setBounds(300, 120, 90, 40);
		discard.setBounds(300, 160, 90, 40);
		
		dayPanel.setBounds(270, 70, this.getWidth() - 270, 610);
		scrollDayTable.setBounds(20, 20, dayPanel.getWidth()-50, dayPanel.getHeight()-50);
		
		agendaPanel.setBounds(270, 70, this.getWidth() - 270, 610);
		scrollAgendaTable.setBounds(20, 20, dayPanel.getWidth()-50, dayPanel.getHeight()-50);
	}
	
	private void generateAgendaTable() {
		DefaultTableCellRenderer rightRender = new DefaultTableCellRenderer();
		rightRender.setHorizontalAlignment(SwingConstants.RIGHT);
		rightRender.setOpaque(false);
		
		modelAgendaTable.setColumnCount(2);
		
		agendaTable.setRowHeight(50);
		agendaTable.getColumnModel().getColumn(0).setCellRenderer(rightRender);
		agendaTable.getColumnModel().getColumn(0).setPreferredWidth(150);
		
		AgendaTableCellRenderer itemRender = new AgendaTableCellRenderer();
		itemRender.setOpaque(false);
		agendaTable.getColumnModel().getColumn(1).setCellRenderer(itemRender);
		
		
		agendaTable.getColumnModel().getColumn(1).setPreferredWidth(scrollDayTable.getWidth() - dayTable.getColumnModel().getColumn(0).getWidth()-95);
		agendaTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		agendaTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		agendaTable.getTableHeader().setReorderingAllowed(false);
		agendaTable.getTableHeader().setResizingAllowed(false);
		agendaTable.setTableHeader(null);
		
		agendaTable.setOpaque(false);
		scrollAgendaTable.setOpaque(false);
		scrollAgendaTable.getViewport().setOpaque(false);
		scrollAgendaTable.setBorder(BorderFactory.createEmptyBorder());
		agendaTable.setBorder(BorderFactory.createEmptyBorder());
		agendaTable.setShowGrid(false);

		((DefaultTableCellRenderer)agendaTable.getDefaultRenderer(Object.class)).setOpaque(false);
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
		dayTable.getColumnModel().getColumn(1).setCellRenderer(new DayTableCellRenderer());//luis
		dayTable.getColumnModel().getColumn(1).setPreferredWidth(scrollDayTable.getWidth() - dayTable.getColumnModel().getColumn(0).getWidth() - 45);
		dayTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		dayTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		dayTable.getTableHeader().setReorderingAllowed(false);
		dayTable.getTableHeader().setResizingAllowed(false);
		
		/*
		for(int i = 0; i < 48; i++)
			if(i%2==0)
				dayTable.setValueAt(i/2 + ":00", i, 0);*/
		
		LocalTime tmpTime = LocalTime.of(0, 0);
		for(int i=0 ; i<48 ; i++) {
			dayTable.setValueAt(tmpTime, i, 0);
			tmpTime = tmpTime.plusMinutes(30);
		}
				
	}
	
	private void generateCalendar() {
		GregorianCalendar cal = new GregorianCalendar();
		dayBound = cal.get(GregorianCalendar.DAY_OF_MONTH);
		monthBound = cal.get(GregorianCalendar.MONTH);
		yearBound = cal.get(GregorianCalendar.YEAR);
		monthToday = monthBound;
		yearToday = yearBound;
		dayToday = dayBound; //added in dc2

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
	
	public void initListeners() {
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
		view.addEventCheckBoxListener(new eventCheckBoxListener());
		view.addTaskCheckBoxListener(new taskCheckBoxListener());
		view.addDayTableListener(new dayTableMouseListener());
		view.addAgendaTableListener(new agendaTableMouseListener());
		
		view.addTodayButtonListener(new todayButtonListener());
		view.addMarkTaskListener(new markTaskListener());
		view.addDeleteItemListener(new deleteItemListener());
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
		this.refreshHeader();
		this.refreshDay();
		this.refreshAgenda();
		this.refreshTaskCount();
	}

	/* Added this */
	public void refreshTileEvents(int day, int row, int column) throws NullPointerException {
		EventStringFormatter esformatter = new HTMLEventMarkerFormatter();
		CellStringFormatter csformatter = new HTMLCellMarkerFormatter();

		modelCalendarTable.setValueAt(csformatter.format(day,
				esformatter.formatEvents(LegacyEventConverter.convert(model.getItemsOn(flags, LocalDate.of(yearToday, monthToday+1, day))))), row, column);
	}
	
	public void refreshHeader() {
		view.getDayLabel().setText(view.getMonths()[view.getMonthToday()] + " " + dayToday + ", " + view.getYearToday());
	}
	
	public void refreshDay() {
		for (int row = 0 ; row < modelDayTable.getRowCount() ; row++) {
			modelDayTable.setValueAt("", row, 1);
			for (CalendarItem item : dayItems) {
				LocalDateTime tmpStartTime = item.getStart();
				LocalDateTime tmpEndTime = item.getEnd();
				LocalDateTime tmpTableTime = LocalDateTime.of(LocalDate.of(yearToday, monthToday+1, dayToday), (LocalTime)modelDayTable.getValueAt(row, 0));
				if((tmpStartTime.equals(tmpTableTime) || tmpStartTime.isBefore(tmpTableTime)) &&
						tmpEndTime.isAfter(tmpTableTime)) {
					modelDayTable.setValueAt(item, row, 1);
					break;
				}
			}
		}
	}
	
	public void refreshAgenda() {
		view.clearAgenda();
		for(int row = 0 ; row < dayItems.size() ; row++) {
			Object[] data = new Object[2];
			LocalDateTime tmpItemStart = dayItems.get(row).getStart();
			LocalDateTime tmpItemEnd = dayItems.get(row).getEnd();
			LocalDateTime todayMin = LocalDateTime.of(LocalDate.of(yearToday, monthToday+1, dayToday), LocalTime.MIN);
			LocalDateTime todayMax = LocalDateTime.of(LocalDate.of(yearToday, monthToday+1, dayToday), LocalTime.MAX);
			if(tmpItemStart.isBefore(todayMin)) {
				tmpItemStart = todayMin.truncatedTo(ChronoUnit.MINUTES);
			}
			if(tmpItemEnd.isAfter(todayMax)) {
				tmpItemEnd = todayMax.truncatedTo(ChronoUnit.MINUTES);
			}
			
			data[0] = tmpItemStart.toLocalTime().toString() + " - " + tmpItemEnd.toLocalTime().toString();
			data[1] = dayItems.get(row);
			modelAgendaTable.addRow(data);
		}
	}
	
	public void clearAgenda() {
		for (int row = modelAgendaTable.getRowCount()-1 ; row >= 0 ; row--) {
			modelAgendaTable.removeRow(row);
		}
	}
	
	public void refreshTaskCount() {
		this.taskCountLabel.setText("<html><div style='text-align: center;'><body>Tasks Left:<br>" + this.taskCount + "</body></div></html>");
	}
	
	// ------------TABLE MODELS------------//
	class DayTableCellRenderer extends DefaultTableCellRenderer{
		private static final long serialVersionUID = 1L;
		private ItemStringFormatter formatter;
		
		public DayTableCellRenderer() {
			super();
			formatter = new DayHTMLItemStringFormatter();
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			// TODO Auto-generated method stub
			Component cmp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			TableModel tmodel = table.getModel();
			
			if(tmodel.getValueAt(row, column) instanceof CalendarEvent) {
				cmp.setBackground(Color.decode(CalendarEvent.DEFAULT_COLOR));
			}
			else if(tmodel.getValueAt(row, column) instanceof CalendarTask) {
				cmp.setBackground(Color.decode(CalendarTask.DEFAULT_COLOR));
			}
			else {
				cmp.setBackground(table.getBackground());
			}
			
			return cmp;
		}
		
		@Override
		public void setValue(Object value) {
			super.setValue(value);
			if(value instanceof CalendarItem)
				setText(formatter.format((CalendarItem)value));
		}
		
		
	}
	
	class AgendaTableCellRenderer extends DefaultTableCellRenderer{
		private static final long serialVersionUID = 1L;
		private ItemStringFormatter formatter;
		
		public AgendaTableCellRenderer() {
			super();
			formatter = new AgendaHTMLItemStringFormatter();
		}
		
		@Override
		public void setValue(Object value) {
			super.setValue(value);
			if(value instanceof CalendarItem)
				setText(formatter.format((CalendarItem)value));
		}
		
		
	}
	
	// ------------OVERRIDE METHODS------------//
	@Override
	public void update() {
		try {
			monthItems = model.getItemsOn(flags, YearMonth.of(yearToday, monthToday+1));
			dayItems = model.getItemsOn(flags, LocalDate.of(yearToday, monthToday+1, dayToday));
			taskCount = model.getTaskCount();
			refreshCurrentPage();
		}catch (DateTimeException e) {}
	}
	
	// ------------LISTENER CLASSES------------//
	
	class btnPrev_Action implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (view.getMonthToday() == 0) {
				view.setMonthToday(11);
				view.setYearToday(view.getYearToday() - 1);
			} else {
				view.setMonthToday(view.getMonthToday()-1);
			}
			view.refreshCalendar(view.getMonthToday(), view.getYearToday());
			view.update();
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
			view.update();
		}
	}
	
	class dayTableMouseListener implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent arg0) {
			int row = view.getDayTable().getSelectedRow();
			if(SwingUtilities.isRightMouseButton(arg0) && modelDayTable.getValueAt(row, 1) instanceof CalendarItem)
			{
				//if(not yet marked as done)
					//enable mark as done and disable mark as undone
				//else
					//enable mark as undone and disable mark as done
				
				if(modelDayTable.getValueAt(row, 1) instanceof CalendarTask) {
					markTask.setEnabled(true);
					view.toggleMarkTaskText((CalendarTask)modelDayTable.getValueAt(row, 1));
				}
				else
					markTask.setEnabled(false);
				
				view.getDayMenu().show(view.getDayTable(), arg0.getX(), arg0.getY()); 
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
	
	class agendaTableMouseListener implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent arg0) {
			int row = view.getAgendaTable().getSelectedRow();
			if(SwingUtilities.isRightMouseButton(arg0) && modelAgendaTable.getValueAt(row, 1) instanceof CalendarItem)
			{
				//if(not yet marked as done)
					//enable mark as done and disable mark as undone
				//else
					//enable mark as undone and disable mark as done
				
				if(modelAgendaTable.getValueAt(row, 1) instanceof CalendarTask) {
					markTask.setEnabled(true);
					view.toggleMarkTaskText((CalendarTask)modelAgendaTable.getValueAt(row, 1));
				}
				else
					markTask.setEnabled(false);
				
				view.getDayMenu().show(view.getAgendaTable(), arg0.getX(), arg0.getY()); 
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
	
	class calendarTableMouseListener implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent evt) {
			int col = view.getCalendarTable().getSelectedColumn();
			int row = view.getCalendarTable().getSelectedRow();
			
			try {
				int day = view.getValidCells().getDayAtCell(row, col);
				// TODO: show the day/agenda for that day
				view.dayToday = day;
				view.update();
				
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
	
	public void toggleMarkTaskText(CalendarTask task) {
		if(task.isDone())
			markTask.setText("Unmark Task");
		else
			markTask.setText("Mark Task");
	}
	
	class createbtnListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			view.toggleCreateView(true);
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
			view.toggleCreateView(false);
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
			view.toggleAgendaView(false);
			view.toggleDayView(true);
		}
	}
	
	class agendaToggleBtnListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			view.toggleDayView(false);
			view.toggleAgendaView(true);
		}
		
	}
	
	private void toggleAgendaView(boolean toggle) {
		view.getAgenda().setSelected(toggle);
		view.getAgendaPanel().setVisible(toggle);
		view.getAgendaPanel().setEnabled(toggle);
	}
	
	private void toggleDayView(boolean toggle) {
		view.getDay().setSelected(toggle);
		view.getDayPanel().setVisible(toggle);
		view.getDayPanel().setEnabled(toggle);
	}
	
	private void toggleCreateView(boolean toggle) {
		view.toggleDayView(!toggle);
		view.toggleAgendaView(false);
		view.getDay().setEnabled(!toggle);
		view.getAgenda().setEnabled(!toggle);
		view.today.setEnabled(!toggle);
		view.getCreate().setEnabled(!toggle);
		view.getCreatePanel().setVisible(toggle);
		view.getCreatePanel().setEnabled(toggle);
		view.clearCreatePanel();
	}
	
	class eventCheckBoxListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(view.getEvent().isSelected())
			{
				if(view.getTask().isSelected())
				{
					//TODO: view both
					view.flags.setAll(true);
				}
				else
				{
					//TODO: view only events
					view.flags.setAll(false);
					view.flags.setEvent(true);
				}
			}
			
			else
			{
				if(view.getTask().isSelected())
				{
					//TODO: view only tasks
					view.flags.setAll(false);
					view.flags.setTask(true);
				}
				
				else
				{
					//TODO: view both
					view.flags.setAll(true);
				}
			}
			view.update();
			
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
					view.flags.setAll(true);
				}
				else
				{
					//TODO: view only tasks
					view.flags.setAll(false);
					view.flags.setTask(true);
				}
			}
			
			else
			{
				if(view.getEvent().isSelected())
				{
					//TODO: view only events
					view.flags.setAll(false);
					view.flags.setEvent(true);
				}
				
				else
				{
					//TODO: view both
					view.flags.setAll(true);
				}
			}
			view.update();
			
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
	
	class todayButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			GregorianCalendar cal = new GregorianCalendar();
			view.dayBound = cal.get(GregorianCalendar.DAY_OF_MONTH);
			view.monthBound = cal.get(GregorianCalendar.MONTH);
			view.yearBound = cal.get(GregorianCalendar.YEAR);
			view.monthToday = monthBound;
			view.yearToday = yearBound;
			view.dayToday = dayBound;
			view.update();
		}
	}
	
	class markTaskListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JTable invoker = (JTable)dayMenu.getInvoker();
			CalendarTask task;
			if(invoker.getValueAt(invoker.getSelectedRow(), 1) instanceof CalendarTask) {
				task = (CalendarTask)invoker.getValueAt(invoker.getSelectedRow(), 1);
				controller.markTask(task, !task.isDone());
				view.update();
				System.out.println("Gets here");
			}
		}
	}
	
	class deleteItemListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JTable invoker = (JTable)dayMenu.getInvoker();
			CalendarItem item;
			if(invoker.getValueAt(invoker.getSelectedRow(), 1) instanceof CalendarItem) {
				item = (CalendarItem)invoker.getValueAt(invoker.getSelectedRow(), 1);
				controller.deleteItem(item);
				view.update();
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
		String[] startDate = new String[3];
		String[] endDate = new String[3];
		LocalDateTime startDateTime, endDateTime;
		
		try {
			if(view.getCreateName().getText().equals(createPlaceholderName) || view.getCreateName().getText().isEmpty())
				throw new Exception("Please enter a name");
			if(view.getStartDate().getText().equals(createPlaceholderStartDate) || view.getStartDate().getText().isEmpty())
				throw new Exception("Please enter a starting date");
			if((view.getEndDate().getText().equals(createPlaceholderEndDate) || view.getEndDate().getText().isEmpty()) && view.getEventRB().isSelected())
				throw new Exception("Please enter a ending date");
			startDate = view.getStartDate().getText().split("/");
			if(startDate.length !=3)
				throw new Exception("Invalid date format");
			startDateTime = LocalDateTime.of(LocalDate.of(Integer.valueOf(startDate[0]), 
					Integer.valueOf(startDate[1]), Integer.valueOf(startDate[2])), (LocalTime) view.getStartTime().getSelectedItem());
			if(view.getTaskRB().isSelected()) {
				controller.addTask(view.getCreateName().getText(), startDateTime);
			}
			else{
				endDate = view.getEndDate().getText().split("/");
				if(endDate.length != 3)
					throw new Exception("Invalid date format");
				endDateTime = LocalDateTime.of(LocalDate.of(Integer.valueOf(endDate[0]), 
						Integer.valueOf(endDate[1]), Integer.valueOf(endDate[2])), (LocalTime) view.getEndTime().getSelectedItem());
				System.out.println(startDateTime);
				System.out.println(endDateTime);
				controller.addEvent(view.getCreateName().getText(), startDateTime, endDateTime);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
		view.toggleCreateView(false);
		view.update();
	}
	
	// ------------LISTENER SETTERS------------//
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

	public void addCreateStartDateListener(FocusListener f, KeyListener k) {
		startDate.addFocusListener(f);
		startDate.addKeyListener(k);
	}
	
	public void addCreateEndDateListener(FocusListener f, KeyListener k) {
		endDate.addFocusListener(f);
		endDate.addKeyListener(k);
	}
	
	public void addEventCheckBoxListener(ActionListener e) {
		event.addActionListener(e);
	}
	
	public void addTaskCheckBoxListener(ActionListener e) {
		task.addActionListener(e);
	}
	
	public void addDayTableListener(MouseListener e) {
		dayTable.addMouseListener(e);
	}
	
	public void addAgendaTableListener(MouseListener e) {
		agendaTable.addMouseListener(e);
	}
	
	public void addTodayButtonListener(ActionListener e) {
		today.addActionListener(e);
	}
	
	public void addMarkTaskListener(ActionListener e) {
		markTask.addActionListener(e);
	}
	
	public void addDeleteItemListener(ActionListener e) {
		delete.addActionListener(e);
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
		return model;
	}

	public void setCalendarModel(CalendarModel calendarModel) {
		this.model = calendarModel;
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

	public JTextField getStartDate() {
		return startDate;
	}

	public void setStartDate(JTextField date) {
		this.startDate = date;
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

	public JLabel getCreateTOLabelDate() {
		return createTOLabelDate;
	}

	public void setCreateTOLabelDate(JLabel createTOLabel) {
		this.createTOLabelDate = createTOLabel;
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

	public JPanel getAgendaPanel() {
		return agendaPanel;
	}

	public void setAgendaPanel(JPanel agendaPanel) {
		this.agendaPanel = agendaPanel;
	}

	public JCheckBox getEvent() {
		return event;
	}

	public void setEvent(JCheckBox event) {
		this.event = event;
	}

	public JCheckBox getTask() {
		return task;
	}

	public void setTask(JCheckBox task) {
		this.task = task;
	}
	
	public JLabel getCreateTOLabelTime() {
		return createTOLabelTime;
	}

	public void setCreateTOLabelTime(JLabel createTOLabelTime) {
		this.createTOLabelTime = createTOLabelTime;
	}

	public JTextField getEndDate() {
		return endDate;
	}

	public void setEndDate(JTextField endDate) {
		this.endDate = endDate;
	}

	public JScrollPane getScrollCalendarTable() {
		return scrollCalendarTable;
	}

	public void setScrollCalendarTable(JScrollPane scrollCalendarTable) {
		this.scrollCalendarTable = scrollCalendarTable;
	}

	public DefaultTableModel getModelCalendarTable() {
		return modelCalendarTable;
	}

	public void setModelCalendarTable(DefaultTableModel modelCalendarTable) {
		this.modelCalendarTable = modelCalendarTable;
	}

	public JTable getDayTable() {
		return dayTable;
	}

	public void setDayTable(JTable dayTable) {
		this.dayTable = dayTable;
	}

	public DefaultTableModel getModelDayTable() {
		return modelDayTable;
	}

	public void setModelDayTable(DefaultTableModel modelDayTable) {
		this.modelDayTable = modelDayTable;
	}

	public JScrollPane getScrollDayTable() {
		return scrollDayTable;
	}

	public void setScrollDayTable(JScrollPane scrollDayTable) {
		this.scrollDayTable = scrollDayTable;
	}

	public JTable getAgendaTable() {
		return agendaTable;
	}

	public void setAgendaTable(JTable agendaTable) {
		this.agendaTable = agendaTable;
	}

	public DefaultTableModel getModelAgendaTable() {
		return modelAgendaTable;
	}

	public void setModelAgendaTable(DefaultTableModel modelAgendaTable) {
		this.modelAgendaTable = modelAgendaTable;
	}

	public JScrollPane getScrollAgendaTable() {
		return scrollAgendaTable;
	}

	public void setScrollAgendaTable(JScrollPane scrollAgendaTable) {
		this.scrollAgendaTable = scrollAgendaTable;
	}

	public JPopupMenu getDayMenu() {
		return dayMenu;
	}

	public void setDayMenu(JPopupMenu dayMenu) {
		this.dayMenu = dayMenu;
	}

	public JMenuItem getDelete() {
		return delete;
	}

	public void setDelete(JMenuItem delete) {
		this.delete = delete;
	}

	public JMenuItem getMarkDone() {
		return markTask;
	}

	public void setMarkDone(JMenuItem markDone) {
		this.markTask = markDone;
	}

	public JLabel getTaskCountLabel() {
		return taskCountLabel;
	}

	public void setTaskCountLabel(JLabel taskCountLabel) {
		this.taskCountLabel = taskCountLabel;
	}
	
	public JComboBox<LocalTime> getStartTime() {
		return startTime;
	}

	public void setStartTime(JComboBox<LocalTime> startTime) {
		this.startTime = startTime;
	}

	public JComboBox<LocalTime> getEndTime() {
		return endTime;
	}

	public void setEndTime(JComboBox<LocalTime> endTime) {
		this.endTime = endTime;
	}
	
	// ------------CELL DATA------------//

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
