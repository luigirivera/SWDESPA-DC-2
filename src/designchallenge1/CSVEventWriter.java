package designchallenge1;

public class CSVEventWriter extends SVEventWriter{
	public CSVEventWriter(String filename) {
		super(filename);
		separator = ",";
		dateIndex = 0;
		nameIndex = 1;
		colorIndex = 2;
		repeating = true;
	}
}
