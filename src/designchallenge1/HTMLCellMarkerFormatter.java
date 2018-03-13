package designchallenge1;

public class HTMLCellMarkerFormatter implements CellStringFormatter{
	@Override
	public String format(int day, String eventString) {
		return "<html>" + String.valueOf(day) + " " + eventString + "</html>";
	}
}
