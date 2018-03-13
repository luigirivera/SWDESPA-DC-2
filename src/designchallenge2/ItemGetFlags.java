package designchallenge2;

public class ItemGetFlags {
	private boolean event;
	private boolean task;

	public ItemGetFlags() {
		this.event = false;
		this.task = false;
	}

	public boolean isAll() {
		return event && task;
	}

	public void setAll(boolean all) {
		this.event = all;
		this.task = all;
	}

	public boolean isEvent() {
		return event;
	}

	public void setEvent(boolean event) {
		this.event = event;
	}

	public boolean isTask() {
		return task;
	}

	public void setTask(boolean task) {
		this.task = task;
	}

}
