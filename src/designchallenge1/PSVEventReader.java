package designchallenge1;

public class PSVEventReader extends SVEventReader {
	public PSVEventReader(String filename) {
		super(filename);
		separator = "\\|";
		dateIndex = 1;
		nameIndex = 0;
		colorIndex = 2;
		repeating = false;
	}
}
