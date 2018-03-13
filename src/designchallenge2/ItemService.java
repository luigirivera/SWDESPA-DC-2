package designchallenge2;


import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class ItemService {
	public static final String TABLE = "ITEM";
	public static final String COL_ID = TABLE + ".ITEMid";
	public static final String COL_NAME = TABLE + ".ITEMname";
	public static final String COL_START = TABLE + ".ITEMstart";
	public static final String COL_END = TABLE + ".ITEMend";
	public static final String COL_DONE = TABLE + ".ITEMdone";

	protected CalendarDB calendarDB;

	public ItemService(CalendarDB calendarDB) {
		this.calendarDB = calendarDB;
	}

	private CalendarItem toItem(ResultSet rs) throws SQLException {
		CalendarItem item;

		rs.getBoolean(COL_DONE);
		if (rs.wasNull())
			item = toEvent(rs);
		else
			item = toTask(rs);
		
		item.setId(rs.getInt(COL_ID));
		item.setName(rs.getString(COL_NAME));
		item.setStart(rs.getTimestamp(COL_START).toLocalDateTime());
		item.setEnd(rs.getTimestamp(COL_END).toLocalDateTime());

		return item;
	}

	private CalendarEvent toEvent(ResultSet rs) throws SQLException {
		CalendarEvent event = new CalendarEvent();

		return event;
	}

	private CalendarTask toTask(ResultSet rs) throws SQLException {
		CalendarTask task = new CalendarTask();
		
		task.setDone(rs.getBoolean(COL_DONE));

		return task;
	}

	public List<CalendarItem> getOn(ItemGetFlags flags, LocalDate date){
		List<CalendarItem> items;
		if(flags.isAll()) {
			items = this.getAllOn(date);
			return items;
		}
		items = new ArrayList<CalendarItem>();
		if(flags.isEvent())
			items.addAll(this.getEventsOn(date));
		if(flags.isTask())
			items.addAll(this.getTasksOn(date));
		return items;
	}

	private List<CalendarItem> getAllOn(LocalDate date) {
		List<CalendarItem> items = new ArrayList<CalendarItem>();

		Connection cnt = calendarDB.getConnection();
		
		String query = "SELECT * FROM " + TABLE + " WHERE "
				+ "? BETWEEN DATE(" + COL_START + ") AND "
				+ "DATE(" + COL_END + ") ORDER BY " + COL_START;

		try {
			PreparedStatement ps = cnt.prepareStatement(query);
			
			ps.setDate(1, Date.valueOf(date));

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				items.add(toItem(rs));
			}

			ps.close();
			rs.close();
			cnt.close();

			System.out.println(String.format("[%s] SELECT SUCCESS", TABLE));
		} catch (SQLException e) {
			System.out.println(String.format("[%s] SELECT FAILED", TABLE));
			e.printStackTrace();
		}

		return items;
	}

	private List<CalendarItem> getEventsOn(LocalDate date) {
		List<CalendarItem> items = new ArrayList<CalendarItem>();

		Connection cnt = calendarDB.getConnection();

		String query = "SELECT * FROM " + TABLE + " WHERE "
				+ "? BETWEEN DATE(" + COL_START + ") AND "
				+ "DATE(" + COL_END + ") AND "
				+ COL_DONE + " IS NULL ORDER BY " + COL_START;

		try {
			PreparedStatement ps = cnt.prepareStatement(query);
			
			ps.setDate(1, Date.valueOf(date));

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				items.add(toItem(rs));
			}

			ps.close();
			rs.close();
			cnt.close();

			System.out.println(String.format("[%s] SELECT SUCCESS", TABLE));
		} catch (SQLException e) {
			System.out.println(String.format("[%s] SELECT FAILED", TABLE));
			e.printStackTrace();
		}

		return items;
	}

	private List<CalendarItem> getTasksOn(LocalDate date) {
		List<CalendarItem> items = new ArrayList<CalendarItem>();

		Connection cnt = calendarDB.getConnection();

		String query = "SELECT * FROM " + TABLE + " WHERE "
				+ "? BETWEEN DATE(" + COL_START + ") AND "
				+ "DATE(" + COL_END + ") AND "
				+ COL_DONE + " IS NOT NULL ORDER BY " + COL_START;

		try {
			PreparedStatement ps = cnt.prepareStatement(query);
			
			ps.setDate(1, Date.valueOf(date));

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				items.add(toItem(rs));
			}

			ps.close();
			rs.close();
			cnt.close();

			System.out.println(String.format("[%s] SELECT SUCCESS", TABLE));
		} catch (SQLException e) {
			System.out.println(String.format("[%s] SELECT FAILED", TABLE));
			e.printStackTrace();
		}

		return items;
	}
	
	public List<CalendarItem> getOn(ItemGetFlags flags, YearMonth yearMonth){
		List<CalendarItem> items;
		if(flags.isAll()) {
			items = this.getAllOn(yearMonth);
			return items;
		}
		items = new ArrayList<CalendarItem>();
		if(flags.isEvent())
			items.addAll(this.getEventsOn(yearMonth));
		if(flags.isTask())
			items.addAll(this.getTasksOn(yearMonth));
		return items;
	}

	private List<CalendarItem> getAllOn(YearMonth yearMonth) {
		List<CalendarItem> items = new ArrayList<CalendarItem>();

		Connection cnt = calendarDB.getConnection();

		/*String query = "SELECT * FROM " + TABLE + " WHERE "
				+ "(? = MONTH(" + COL_START + ") AND " + "? = YEAR(" + COL_START + ") OR "
				+ "? = MONTH(" + COL_END + ") AND " + "? = YEAR(" + COL_END + ")) "
				+ "ORDER BY " + COL_START;*/
		
		String query = "SELECT * FROM " + TABLE + " WHERE "
		+ "? BETWEEN MONTH(" + COL_START + ") AND MONTH(" + COL_END + ") AND "
		+ "? BETWEEN YEAR(" + COL_START + ") AND YEAR(" + COL_END + ") "
		+ "ORDER BY " + COL_START;

		try {
			PreparedStatement ps = cnt.prepareStatement(query);
			
			ps.setInt(1, yearMonth.getMonthValue());
			ps.setInt(2, yearMonth.getYear());

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				items.add(toItem(rs));
			}

			ps.close();
			rs.close();
			cnt.close();

			System.out.println(String.format("[%s] SELECT SUCCESS", TABLE));
		} catch (SQLException e) {
			System.out.println(String.format("[%s] SELECT FAILED", TABLE));
			e.printStackTrace();
		}

		return items;
	}

	private List<CalendarItem> getEventsOn(YearMonth yearMonth) {
		List<CalendarItem> items = new ArrayList<CalendarItem>();

		Connection cnt = calendarDB.getConnection();

		String query = "SELECT * FROM " + TABLE + " WHERE "
				+ "(? = MONTH(" + COL_START + ") AND " + "? = YEAR(" + COL_START + ") OR "
				+ "? = MONTH(" + COL_END + ") AND " + "? = YEAR(" + COL_END + ")) AND "
				+ COL_DONE + " IS NULL ORDER BY " + COL_START;

		try {
			PreparedStatement ps = cnt.prepareStatement(query);
			
			ps.setInt(1, yearMonth.getMonthValue());
			ps.setInt(2, yearMonth.getYear());
			ps.setInt(3, yearMonth.getMonthValue());
			ps.setInt(4, yearMonth.getYear());

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				items.add(toItem(rs));
			}

			ps.close();
			rs.close();
			cnt.close();

			System.out.println(String.format("[%s] SELECT SUCCESS", TABLE));
		} catch (SQLException e) {
			System.out.println(String.format("[%s] SELECT FAILED", TABLE));
			e.printStackTrace();
		}

		return items;
	}

	private List<CalendarItem> getTasksOn(YearMonth yearMonth) {
		List<CalendarItem> items = new ArrayList<CalendarItem>();

		Connection cnt = calendarDB.getConnection();

		String query = "SELECT * FROM " + TABLE + " WHERE "
				+ "(? = MONTH(" + COL_START + ") AND " + "? = YEAR(" + COL_START + ") OR "
				+ "? = MONTH(" + COL_END + ") AND " + "? = YEAR(" + COL_END + ")) AND "
				+ COL_DONE + " IS NOT NULL ORDER BY " + COL_START;

		try {
			PreparedStatement ps = cnt.prepareStatement(query);
			
			ps.setInt(1, yearMonth.getMonthValue());
			ps.setInt(2, yearMonth.getYear());
			ps.setInt(3, yearMonth.getMonthValue());
			ps.setInt(4, yearMonth.getYear());

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				items.add(toItem(rs));
			}

			ps.close();
			rs.close();
			cnt.close();

			System.out.println(String.format("[%s] SELECT SUCCESS", TABLE));
		} catch (SQLException e) {
			System.out.println(String.format("[%s] SELECT FAILED", TABLE));
			e.printStackTrace();
		}

		return items;
	}
	
	public int getTaskCount() {
		int result = 0;
		
		Connection cnt = calendarDB.getConnection();

		String query = "SELECT COUNT(*) FROM " + TABLE + " WHERE "
				+ COL_DONE + " = 0";

		try {
			PreparedStatement ps = cnt.prepareStatement(query);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				result = rs.getInt(1);
			}

			ps.close();
			rs.close();
			cnt.close();

			System.out.println(String.format("[%s] SELECT SUCCESS", TABLE));
		} catch (SQLException e) {
			System.out.println(String.format("[%s] SELECT FAILED", TABLE));
			e.printStackTrace();
		}

		return result;
	}

	public boolean add(CalendarItem item) {
		Connection cnt = calendarDB.getConnection();

		String query = "INSERT INTO " + TABLE + " VALUES (?, ?, ?, ?, ?)";

		try {
			PreparedStatement ps = cnt.prepareStatement(query);

			ps.setInt(1, Types.NULL); // because id is auto-increment anyway
			ps.setString(2, item.getName());
			ps.setTimestamp(3, Timestamp.valueOf(item.getStart()));
			ps.setTimestamp(4, Timestamp.valueOf(item.getEnd()));

			if (item instanceof CalendarTask)
				ps.setBoolean(5, false);
			else
				ps.setNull(5, Types.BOOLEAN);

			ps.executeUpdate();

			ps.close();
			cnt.close();
			System.out.println(String.format("[%s] INSERT SUCCESS", TABLE));
		} catch (SQLException e) {
			System.out.println(String.format("[%s] INSERT FAILED", TABLE));
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public boolean update(CalendarItem item) {
		Connection cnt = calendarDB.getConnection();

		String query = "UPDATE " + TABLE + " SET " + COL_NAME + " = ?," + COL_START + " = ?," + COL_END + " = ?,"
				+ COL_DONE + " = ?" + " WHERE " + COL_ID + " = ?";

		try {
			PreparedStatement ps = cnt.prepareStatement(query);

			ps.setInt(5, item.getId()); // because id is auto-increment anyway
			ps.setString(1, item.getName());
			ps.setTimestamp(2, Timestamp.valueOf(item.getStart()));
			ps.setTimestamp(3, Timestamp.valueOf(item.getEnd()));

			if (item instanceof CalendarTask)
				ps.setBoolean(4, ((CalendarTask) item).isDone());
			else
				ps.setNull(4, Types.BOOLEAN);

			ps.executeUpdate();

			ps.close();
			cnt.close();
			System.out.println(String.format("[%s] UPDATE SUCCESS", TABLE));
		} catch (SQLException e) {
			System.out.println(String.format("[%s] UPDATE FAILED", TABLE));
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public boolean delete(CalendarItem item) {
		Connection cnt = calendarDB.getConnection();

		String query = "DELETE FROM " + TABLE + " WHERE " + COL_ID + " = ?";

		try {
			PreparedStatement ps = cnt.prepareStatement(query);

			ps.setInt(1, item.getId());

			ps.executeUpdate();

			ps.close();
			cnt.close();
			System.out.println(String.format("[%s] DELETE SUCCESS", TABLE));
		} catch (SQLException e) {
			System.out.println(String.format("[%s] DELETE FAILED", TABLE));
			e.printStackTrace();
			return false;
		}

		return true;
	}
	
	public boolean markTask(CalendarTask task, boolean done) {
		Connection cnt = calendarDB.getConnection();

		String query = "UPDATE " + TABLE + " SET " 
				+ COL_DONE + " = ?" 
				+ " WHERE " + COL_ID + " = ? AND " + COL_DONE + " IS NOT NULL";

		try {
			PreparedStatement ps = cnt.prepareStatement(query);

			ps.setInt(2, task.getId()); // because id is auto-increment anyway
			ps.setBoolean(1, done);

			ps.executeUpdate();

			ps.close();
			cnt.close();
			System.out.println(String.format("[%s] UPDATE SUCCESS", TABLE));
		} catch (SQLException e) {
			System.out.println(String.format("[%s] UPDATE FAILED", TABLE));
			e.printStackTrace();
			return false;
		}

		return true;
	}
}
