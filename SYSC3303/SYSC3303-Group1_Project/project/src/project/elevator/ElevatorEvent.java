package project.elevator;

public enum ElevatorEvent {
	// Actions

	/** Commands the elevator to start moving */
	START_MOVING,

	/** A passenger requests to open the door */
	OPEN_DOOR,

	/** A passenger requests to close the door */
	CLOSE_DOOR,

	/** Blocks the door */
	BLOCK_DOOR,

	/** Unblocks the door */
	UNBLOCK_DOOR,

	/** Blocks the movement of the elevator */
	BLOCK_MOVEMENT,

	/** Commands the elevator to terminate its operation */
	TERMINATE,

	// Status

	/** Door is fully opened */
	DOOR_OPENED,

	/** Door is fully closed */
	DOOR_CLOSED,

	/** Elevator has arrived at its next destination */
	DESTINATION_ARRIVED;
}
