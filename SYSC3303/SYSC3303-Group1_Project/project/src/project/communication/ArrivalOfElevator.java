package project.communication;

import java.io.Serializable;

import project.Direction;

public record ArrivalOfElevator(
	int elevatorID,
	int floor,
	Direction currentCycle
) implements Serializable {}
