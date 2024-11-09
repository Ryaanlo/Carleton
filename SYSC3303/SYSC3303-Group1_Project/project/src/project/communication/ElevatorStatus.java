package project.communication;

import java.io.Serializable;
import java.time.Instant;

import project.Direction;
import project.elevator.Elevator;
import project.elevator.ElevatorState;

public record ElevatorStatus(
	Instant time,
	int elevatorID,
	int currentFloor,
	Direction currentCycle,
	ElevatorState currentState,
	int numberOfDestinations
) implements Serializable {
	public ElevatorStatus(Elevator elevator) {
		this(
			Instant.now(),
			elevator.getID(),
			elevator.currentFloor(),
			elevator.currentCycle(),
			elevator.currentState(),
			elevator.numberOfDestinations()
		);
	}
}
