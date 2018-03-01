package designchallenge1;

public class HTMLCellStringFormatter implements CellStringFormatter {

	@Override
	public String format(int day, String eventString) {
		return "<html>" + String.valueOf(day) + "<br>" + eventString + "</html>";
	}

}
