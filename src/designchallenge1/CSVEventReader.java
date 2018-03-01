package designchallenge1;

public class CSVEventReader extends SVEventReader {
	public CSVEventReader(String filename) {
		super(filename);
		separator = ",";
		dateIndex = 0;
		nameIndex = 1;
		colorIndex = 2;
		repeating = true;
	}
}
