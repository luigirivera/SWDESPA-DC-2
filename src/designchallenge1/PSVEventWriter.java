package designchallenge1;

public class PSVEventWriter extends SVEventWriter {

	public PSVEventWriter(String filename) {
		super(filename);
		separator = "|";
		dateIndex = 1;
		nameIndex = 0;
		colorIndex = 2;
		repeating = false;
	}

}
