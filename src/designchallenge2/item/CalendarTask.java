package designchallenge2.item;


import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;

public class CalendarTask extends CalendarItem {
	public static final String DEFAULT_COLOR = "#33EE33";
	public static final TemporalAmount DURATION = Duration.ofMinutes(30);
	private boolean done;

	public CalendarTask() {
		super();
		this.done = false;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

}
