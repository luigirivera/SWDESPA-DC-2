package designchallenge1;

import java.awt.Color;
import java.util.Arrays;

public enum CalendarColor {
	RED, GREEN, BLUE;
	public String toString() {
		switch (this) {
		case RED:
			return "Red";
		case GREEN:
			return "Green";
		case BLUE:
			return "Blue";
		default:
			return "invalid";
		}
	}

	public static CalendarColor getColor(String name) throws IllegalArgumentException{
		for (CalendarColor cc : Arrays.asList(CalendarColor.values())) {
			if (cc.toString().equalsIgnoreCase(name))
				return cc;
		}
		throw new IllegalArgumentException("CalendarColor not found");
	}

	public String toHex() {
		switch (this) {
		case RED:
			return "#DD0000";
		case GREEN:
			return "#00DD00";
		case BLUE:
			return "#0000DD";
		default:
			return "#000000";
		}
	}
	
	public Color toColor() {
		return Color.decode(this.toHex());
	}
}
