package project.elevator;

import java.util.HashSet;
import java.util.Set;

import project.Async;
import project.Configuration;
import project.Direction;
import project.Logger;
import project.StateMachine;

public class Motor implements StateMachine<Motor, MotorState, MotorEvent> {
	private static final double ACCELERATION_RATE = Configuration.shared.accelerationRate;
	private static final double FLOOR_HEIGHT = Configuration.shared.floorHeight;
	private static final double MAX_SPEED = Configuration.shared.maxSpeed;
	private static final double TIME_REQUIRED_PER_FLOOR = FLOOR_HEIGHT / MAX_SPEED;
	private static final double TIME_REQUIRED_TO_DECELERATE = MAX_SPEED / ACCELERATION_RATE;

	public static double timeToMoveFloors(int floors) {
		return TIME_REQUIRED_PER_FLOOR * floors + TIME_REQUIRED_TO_DECELERATE;
	}

	private double speed = 0;
	private Direction direction = Direction.NONE;
	private MotorState state = MotorState.IDLE;

	private Set<MotorEventHandler> eventHandlers = new HashSet<>();

	// Getters and setters

	public double currentSpeed() { return speed; }

	public Direction currentMovingDirection() { return direction; }

	@Override
	public MotorState currentState() { return state; }

	public Set<MotorEventHandler> eventHandlers() { return eventHandlers; }

	// Methods

	@Override
	public String toString() { return "Motor"; }

	public void addEventHandler(MotorEventHandler eventHandler) {
		eventHandlers.add(eventHandler);
	}

	public void removeEventHandler(MotorEventHandler eventHandler) {
		eventHandlers.remove(eventHandler);
	}

	public boolean isMoving() {
		return speed != 0
			|| (state != MotorState.IDLE && state != MotorState.TERMINATED);
	}

	public boolean isStopped() { return speed == 0 || isTerminated(); }

	public void accelerate(double dt) { updateSpeed(ACCELERATION_RATE * dt); }

	public void decelerate(double dt) { updateSpeed(-ACCELERATION_RATE * dt); }

	private void updateSpeed(double dv) {
		if (0 <= speed && speed <= MAX_SPEED)
			speed += dv;

		if (speed > MAX_SPEED) {
			speed = MAX_SPEED;
			handleEvent(MotorEvent.MAX_SPEED_REACHED);
		} else if (speed <= 0) {
			speed = 0;
			direction = Direction.NONE;
			handleEvent(MotorEvent.STOPPED);
		} // else 0 <= motor.speed <= MAX_SPEED
	}

	public void move(Direction direction, int floors) {
		if (floors < 1)
			return;

		this.direction = direction;
		handleEvent(MotorEvent.ACCELERATE);

		var timeRequiredToMove = TIME_REQUIRED_PER_FLOOR * floors + TIME_REQUIRED_TO_DECELERATE;
		var delayBeforeDecelerate = floors == 1
			? timeRequiredToMove / 2
			: timeRequiredToMove - TIME_REQUIRED_TO_DECELERATE;

		Async.later(delayBeforeDecelerate, () -> {
			handleEvent(MotorEvent.DECELERATE);
		});

		// notify event handlers that the motor has passed through intermediate floors
		for (int f = 1; f < floors; ++f) {
			var delay = TIME_REQUIRED_PER_FLOOR * (f - 1)
				+ TIME_REQUIRED_TO_DECELERATE
				- timeIntervalBetweenRuns() / 2;
			Async.later(delay, () -> {
				for (var eventHandler: eventHandlers)
					eventHandler.motorPassedAFloor(this, direction);
			});
		}

		Async.later(timeRequiredToMove, () -> {
			for (var eventHandler: eventHandlers)
				eventHandler.motorPassedAFloor(this, direction);
		});
	}

	@Override
	public void terminate() {
		if (isTerminated())
			return;

		synchronized (this) {
			speed = 0;
			direction = Direction.NONE;
		}

		handleEvent(MotorEvent.TERMINATE);
	}

	@Override
	public synchronized boolean isTerminated() { return state == MotorState.TERMINATED; }

	@Override
	public Motor getStateMachine() { return this; }

	@Override
	public void switchState(MotorState nextState, MotorEvent event) {
		if (nextState == null || state == nextState)
			return;

		Logger.log(
			toString(),
			"State",
			"Switching from " + state + " to " + nextState
		);

		for (var eventHandler: eventHandlers)
			eventHandler.motorSwitchingState(this, state, nextState);

		state = nextState;
	};

	@Override
	public void willEnterState(MotorState state) {
		for (var eventHandler: eventHandlers)
			eventHandler.motorWillEnterState(this, state);
	}

	@Override
	public void didEnterState(MotorState state) {
		for (var eventHandler: eventHandlers)
			eventHandler.motorDidEnterState(this, state);
	}

	@Override
	public void willLeaveState(MotorState state) {
		for (var eventHandler: eventHandlers)
			eventHandler.motorWillLeaveState(this, state);
	}

	@Override
	public void didLeaveState(MotorState state) {
		for (var eventHandler: eventHandlers)
			eventHandler.motorDidLeaveState(this, state);
	}
}
