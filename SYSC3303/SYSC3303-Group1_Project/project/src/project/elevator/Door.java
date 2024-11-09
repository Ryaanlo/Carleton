package project.elevator;

import project.Async;
import project.Configuration;

public class Door {
	public static final double OPEN_CLOSE_TIME = Configuration.shared.doorOpenCloseTime;
	private boolean open = false;

	private EventHandler eventHandler;

	public interface EventHandler {
		public void doorOpened();
		public void doorClosed();
	}

	public Door(EventHandler eventHandler) {
		this.eventHandler = eventHandler;
	}

	public boolean isOpen() {
		return open;
	}

	public boolean isClosed() {
		return !open;
	}

	public void open() {
		Async.later(OPEN_CLOSE_TIME, () -> {
			synchronized (this) {
				open = true;
			}
			eventHandler.doorOpened();
		});
	}

	public void close() {
		Async.later(OPEN_CLOSE_TIME, () -> {
			synchronized (this) {
				open = false;
			}
			eventHandler.doorClosed();
		});
	}
}
