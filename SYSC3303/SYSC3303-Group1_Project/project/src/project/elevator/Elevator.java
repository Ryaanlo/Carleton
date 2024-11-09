package project.elevator;

import java.util.HashSet;
import java.util.Set;

import project.Async;
import project.Button;
import project.Configuration;
import project.Direction;
import project.Logger;
import project.StateMachine;

/**
 * Represents an elevator.
 */
public class Elevator implements
	Button.EventHandler,
	Door.EventHandler,
	ElevatorFloorSensor.EventHandler,
	MotorEventHandler,
	StateMachine<Elevator, ElevatorState, ElevatorEvent>
{
	public static final int NUMBER_OF_FLOORS = Configuration.shared.numberOfFloors;
	public static final double LOADING_TIME = Configuration.shared.loadingTime;
	private static final double DOOR_BLOCK_TIME = Configuration.shared.doorBlockTime;

	private Integer id;
	private boolean[] destinations = new boolean[NUMBER_OF_FLOORS];
	private ElevatorState state = ElevatorState.IDLE;
	private Direction currentCycle = Direction.NONE;

	private Door  door  = new Door(this);
	private Motor motor = new Motor();
	private ElevatorFloorSensor sensor = new ElevatorFloorSensor(this);

	private ElevatorButton  openDoorButton = new ElevatorButton("Open Door");
	private ElevatorButton closeDoorButton = new ElevatorButton("Close Door");
	private ElevatorButton[]  floorButtons = new ElevatorButton[NUMBER_OF_FLOORS];

	private Set<ElevatorObserver> observers = new HashSet<>();

	private boolean doorBlocked = false;
	private boolean movementBlocked = false;

	public Elevator() { this(null); }

	public Elevator(Integer id) {
		this.id = id;

		motor.addEventHandler(this);
		motor.addEventHandler(sensor);

		openDoorButton.setEventHandler(this);
		closeDoorButton.setEventHandler(this);

		for (int i = 0; i < destinations.length; ++i)
			destinations[i] = false;

		for (int i = 0; i < NUMBER_OF_FLOORS; ++i) {
			floorButtons[i] = new ElevatorButton(i + 1);
			floorButtons[i].setEventHandler(this);
		}
	}

	// Getters and setters

	public int getID() { return id; }

	@Override
	public ElevatorState currentState() { return state; }

	public int currentFloor() { return sensor.currentFloor(); }

	public double currentSpeed() { return motor.currentSpeed(); }

	public Direction currentCycle() { return currentCycle; }

	// Methods

	@Override
	public String toString() {
		return "Elevator" + (id == null ? "" : " " + id);
	}

	public boolean isMoving() {
		return motor.isMoving();
	}

	public boolean isStopped() {
		return motor.isStopped();
	}

	@Override
	public synchronized boolean isTerminated() {
		return state == ElevatorState.TERMINATED;
	}

	@Override
	public void run() {
		Logger.log(toString(), "Elevator", "Elevator is running");
		Async.run(motor);
		StateMachine.super.run();
	}

	public void addObserver(ElevatorObserver observer) {
		observers.add(observer);
	}

	public void removeObserver(ElevatorObserver observer) {
		observers.remove(observer);
	}

	// Destinations

	public int numberOfDestinations() {
		var n = 0;

		for (var d: destinations)
			if (d) ++n;

		return n;
	}

	public boolean containsDestination(int floor) {
		return destinations[floor - 1];
	}

	public void addDestination(int floor) {
		if (floor < 1 || floor > NUMBER_OF_FLOORS)
			return;

		synchronized (this) {
			if (floor == currentFloor()) {
				handleEvent(ElevatorEvent.DESTINATION_ARRIVED);
			} else {
				destinations[floor - 1] = true;
			}
		}
	}

	public void removeDestination(int floor) {
		synchronized (this) {
			destinations[floor - 1] = false;
		}
	}

	public Integer nextDestination() {
		if (isStopped() || currentCycle() == Direction.UP) {
			for (int i = currentFloor() + 1; i < NUMBER_OF_FLOORS; ++i)
				if (destinations[i - 1])
					return i;
		}

		if (isStopped() || currentCycle() == Direction.DOWN) {
			for (int i = currentFloor() - 1; i >= 1; --i)
				if (destinations[i - 1])
					return i;
		}

		return null;
	}

	// Actions

	public void pressFloorButton(int floor) {
		floorButtons[floor - 1].press();
	}

	public void cancelFloorButton(int floor) {
		floorButtons[floor - 1].cancel();
	}

	public void move(Direction direction, int floors) {
		motor.move(direction, floors);
	}

	public void openDoor() {
		handleEvent(ElevatorEvent.OPEN_DOOR);
		door.open();
	}

	public void closeDoor() {
		handleEvent(ElevatorEvent.CLOSE_DOOR);
		door.close();
	}

	public void simulateDoorBlocked() {
		doorBlocked = true;
	}

	public synchronized void simulateMovementStuck() {
		movementBlocked = true;
	}

	@Override
	public void terminate() {
		Async.run(() -> {
			motor.terminate();
		});
		handleEvent(ElevatorEvent.TERMINATE);
		for (var observer: observers)
			observer.elevatorIsTerminated(this);
	}

	// Event Handling

	// Button Events

	@Override
	public void handleButtonPressed(Button button) {
		if (button == openDoorButton) {
			openDoor();
		} else if (button == closeDoorButton) {
			closeDoor();
		} else {
			try {
				var destination = Integer.valueOf(button.getLabel());
				addDestination(destination);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// Door Events

	@Override
	public void doorOpened() {
		handleEvent(ElevatorEvent.DOOR_OPENED);
		for (var observer: observers)
			observer.elevatorDoorIsOpened(this);
	}

	@Override
	public void doorClosed() {
		handleEvent(ElevatorEvent.DOOR_CLOSED);
		for (var observer: observers)
			observer.elevatorDoorIsClosed(this);
	}

	// Motor Events

	@Override
	public void motorStartingToAccelerate(Motor motor) {
		Logger.log(
			toString(),
			"Motor",
			"Starting to accelerate"
		);
		handleEvent(ElevatorEvent.START_MOVING);
	}

	@Override
	public void motorStartingToDecelerate(Motor motor) {
		Logger.log(
			toString(),
			"Motor",
			"Starting to decelerate"
		);
	}

	@Override
	public void motorStopped(Motor motor) {
		Logger.log(
			toString(),
			"Motor",
			"Arrived at destination"
		);
		handleEvent(ElevatorEvent.DESTINATION_ARRIVED);
	}

	// Sensor Events

	@Override
	public void sensorDetectedArrivalOfFloor(int floor) {
		Logger.log(
			toString(),
			"Floor Sensor",
			"Elevator arrived at floor " + floor
		);

		for (var observer: observers)
			observer.elevatorArrivedAtFloor(this, floor);
	}

	// StateMachine Conformance

	@Override
	public Elevator getStateMachine() { return this; }

	@Override
	public void run(double dt) {
		StateMachine.super.run(dt);

		if (!isMoving())
			return;

		var current = currentFloor();
		var next = nextDestination();
		var movingUp = currentCycle == Direction.UP;
		var movingDown = currentCycle == Direction.DOWN;

		if (next == null) {
			currentCycle = Direction.NONE;
		} else if (next < current && movingUp) {
			currentCycle = Direction.DOWN;
		} else if (next > current && movingDown) {
			currentCycle = Direction.UP;
		}
	}

	@Override
	public void switchState(ElevatorState nextState, ElevatorEvent event) {
		Logger.log(
			this.toString(),
			"State",
			"Switching from " + state + " to " + nextState
		);

		state = nextState;
	}

	@Override
	public void willEnterState(ElevatorState state) {
		for (var observer: observers)
			observer.elevatorWillEnterState(this, state);
	}

	@Override
	public void didEnterState(ElevatorState state) {
		for (var observer: observers)
			observer.elevatorDidEnterState(this, state);

		if (doorBlocked && state == ElevatorState.DOOR_CLOSING) {
			doorBlocked = false;
			handleEvent(ElevatorEvent.BLOCK_DOOR);
			Async.later(DOOR_BLOCK_TIME, () -> {
				handleEvent(ElevatorEvent.UNBLOCK_DOOR);
				closeDoor();
			});
		} else if (
			movementBlocked
			&& (state == ElevatorState.IDLE || state == ElevatorState.MOVING)
		) {
			handleEvent(ElevatorEvent.BLOCK_MOVEMENT);
		}
	}

	@Override
	public void willLeaveState(ElevatorState state) {
		for (var observer: observers)
			observer.elevatorWillLeaveState(this, state);
	}

	@Override
	public void didLeaveState(ElevatorState state) {
		for (var observer: observers)
			observer.elevatorDidLeaveState(this, state);
	}
}
