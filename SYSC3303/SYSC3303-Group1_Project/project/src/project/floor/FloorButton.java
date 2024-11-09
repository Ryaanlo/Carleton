package project.floor;

import project.Button;
import project.Direction;
import project.Lamp;
import project.Logger;

/**
 * Represents a Floor button
 * Can be an UP button or a DOWN button
 */
public class FloorButton extends Button {
	private Lamp lamp;

	public FloorButton(Direction direction) {
		super(direction == Direction.UP ? "Up" : "Down");
		lamp = new Lamp();
	}

	@Override
	public void press() {
		lamp.turnOn();
		Logger.log(FloorSubsystem.instance.toString(), "Floor Lamp", "FloorLamp ON");
		if (eventHandler != null)
			eventHandler.handleButtonPressed(this);
	}

	@Override
	public void cancel() {
		lamp.turnOff();
		Logger.log(FloorSubsystem.instance.toString(), "Floor Lamp", "FloorLamp OFF");
		if (eventHandler != null)
			eventHandler.handleButtonCancelled(this);
	}
}
