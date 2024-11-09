package project;

/**
 * Represents a lamp
 * A Lamp corresponds to a Button
 */
public class Lamp {
	private boolean on;

	public Lamp() {
		on = false;
	}

	/**
	 * Turns on the lamp
	 */
	synchronized public void turnOn() {
		on = true;
	}

	/**
	 * Turns off the lamp
	 */
	synchronized public void turnOff() {
		on = false;
	}

	synchronized public boolean isOn() {
		return on;
	}

	synchronized public boolean isOff() {
		return !on;
	}
}
