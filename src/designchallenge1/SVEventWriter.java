package designchallenge1;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public abstract class SVEventWriter extends EventWriter{
	protected String filename;
	protected String separator;
	protected int dateIndex;
	protected int nameIndex;
	protected int colorIndex;
	protected boolean repeating;
	protected List<List<String>> events;
	
	protected final static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	
	public SVEventWriter(String filename) { 
		this.filename = filename;
		events = new ArrayList<List<String>>();
	}
	
	@Override
	protected void getData() {
		for(CalendarEvent event : calendarEvents) {
			List<String> data = new ArrayList<String>();
			if(event.isRepeating() == repeating) {
				data.add(dateFormat.format(event.getDate().getTime()));	
				data.add(event.getName());
				data.add(event.getColor().toString());
				events.add(data);			
			}
			
		}
	}
	
	@Override
	protected void prepareData() {
		List<String> temp = new ArrayList<String>();
		for(List<String> event : events) {
			System.out.println(event + " prepared");
			temp.addAll(event);
			event.set(dateIndex, temp.get(0));
			event.set(nameIndex, temp.get(1));
			event.set(colorIndex, temp.get(2));
			temp.clear();
		}
		
	}
	
	@Override
	protected void outputData() {
		FileWriter fw;
		BufferedWriter bw;
		
		try {
			fw = new FileWriter(filename);
			bw = new BufferedWriter(fw);
			
			for(List<String> event : events) {
				bw.write(event.get(0));
				bw.write(' ' + separator + ' ');
				bw.write(event.get(1));
				bw.write(' ' + separator + ' ');
				bw.write(event.get(2));
				bw.newLine();
			}
			bw.close();
			fw.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		
	}
}
