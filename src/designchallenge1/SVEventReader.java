package designchallenge1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public abstract class SVEventReader extends EventReader {
	protected String filename;
	protected String data;
	protected String separator;
	protected int dateIndex;
	protected int nameIndex;
	protected int colorIndex;
	protected boolean repeating;
	
	protected final static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

	public SVEventReader(String filename) {
		this.filename = filename;
		this.data = "";
	}

	@Override
	protected void getInput() {
		FileReader fr;
		BufferedReader br;
		try {
			fr = new FileReader(filename);
			br = new BufferedReader(fr);

			while (br.ready()) {
				data = data + br.readLine();
				data = data + "\n";
			}

			br.close();
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void parseInput() {
		List<CalendarEvent> tmpEvents = new ArrayList<CalendarEvent>();
		String[] rows = data.split("\n");

		try {
			for (String row : rows) {
				CalendarEvent tmpEvent = new CalendarEvent();
				String[] cells = row.split(separator);
				Calendar cal = GregorianCalendar.getInstance();
				cal.setTime(dateFormat.parse(cells[dateIndex]));
				tmpEvent.setDate(cal);
				tmpEvent.setName(cells[nameIndex].trim());
				tmpEvent.setColor(CalendarColor.getColor(cells[colorIndex].trim()));
				tmpEvent.setRepeating(this.repeating);
				tmpEvents.add(tmpEvent);
			}
			events.addAll(tmpEvents);
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

}
