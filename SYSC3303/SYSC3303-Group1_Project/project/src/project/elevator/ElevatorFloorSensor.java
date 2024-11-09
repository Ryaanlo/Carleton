package project.elevator;

import project.Direction;

public class ElevatorFloorSensor implements MotorEventHandler {
	private int floor = 1;

	private EventHandler eventHandler;

	public ElevatorFloorSensor(EventHandler eventHandler) {
		this.eventHandler = eventHandler;
	}

	public interface EventHandler {
		public void sensorDetectedArrivalOfFloor(int floor);
	}

	public int currentFloor() {
		return floor;
	}

	@Override
	public String toString() {
		return "Elevator Floor Sensor";
	}

	@Override
	public void motorPassedAFloor(Motor motor, Direction direction) {
		floor += direction == Direction.UP
			? 1
			: -1;
		if (eventHandler != null)
			eventHandler.sensorDetectedArrivalOfFloor(floor);
	}
}
