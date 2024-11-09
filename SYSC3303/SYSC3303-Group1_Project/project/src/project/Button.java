package project;

abstract public class Button {
	protected String label;
	protected EventHandler eventHandler;

	public Button(String label) {
		this.label = label;
		this.eventHandler = null;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setEventHandler(EventHandler eventHandler) {
		this.eventHandler = eventHandler;
	}

	abstract public void press();

	abstract public void cancel();

	public interface EventHandler {
		public void handleButtonPressed(Button button);
		default public void handleButtonCancelled(Button button) {}
	}
}
