package project.elevator;

import project.Button;
import project.Lamp;

public class ElevatorButton extends Button {
	private Lamp lamp = new Lamp();

	public ElevatorButton(int floor) {
		super("" + floor);
	}

	public ElevatorButton(String label) {
		super(label);
	}

	@Override
	public void press() {
		lamp.turnOn();
		if (eventHandler != null)
			eventHandler.handleButtonPressed(this);
	}

	@Override
	public void cancel() {
		lamp.turnOff();
		if (eventHandler != null)
			eventHandler.handleButtonCancelled(this);
	}
}
