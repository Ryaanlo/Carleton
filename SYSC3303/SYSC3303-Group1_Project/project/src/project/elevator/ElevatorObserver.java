package project.elevator;

public interface ElevatorObserver {
	default public void elevatorWillEnterState(Elevator elevator, ElevatorState state) {}
	default public void elevatorDidEnterState(Elevator elevator, ElevatorState state) {}
	default public void elevatorWillLeaveState(Elevator elevator, ElevatorState state) {}
	default public void elevatorDidLeaveState(Elevator elevator, ElevatorState state) {}

	default public void elevatorArrivedAtFloor(Elevator elevator, int floor) {}
	default public void elevatorDoorIsOpened(Elevator elevator) {}
	default public void elevatorDoorIsClosed(Elevator elevator) {}
	default public void elevatorIsTerminated(Elevator elevator) {}
}
