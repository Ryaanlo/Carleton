package project.scheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import project.Configuration;
import project.Direction;
import project.Logger;
import project.StateMachine;
import project.communication.ArrivalOfElevator;
import project.communication.ElevatorStatus;
import project.communication.Message;
import project.communication.SchedulerRequest;
import project.communication.ServiceRequest;
import project.communication.Message.Type;
import project.elevator.ElevatorState;

/**
 * Represents the scheduler in an elevator system.
 */
public class Scheduler implements StateMachine<Scheduler, SchedulerState, SchedulerEvent> {
	private static final int NUMBER_OF_FLOORS    = Configuration.shared.numberOfFloors;
	private static final int NUMBER_OF_ELEVATORS = Configuration.shared.numberOfElevators;

	private boolean terminated = false;

	private Map<Integer, Set<ServiceRequest>> requests = new HashMap<>();
	private List<ElevatorStatus> elevators = new ArrayList<>();

	private SchedulerState state = SchedulerState.IDLE;

	/**
	 * Determines whether there is an elevator dispatched to a given floor that
	 * has a request going up.
	 */
	private boolean[] elevatorDispatchedUp = new boolean[NUMBER_OF_FLOORS];

	/**
	 * Determines whether there is an elevator dispatched to a given floor that
	 * has a request going down.
	 */
	private boolean[] elevatorDispatchedDown = new boolean[NUMBER_OF_FLOORS];

	public Scheduler() {
		for (int i = 0; i < NUMBER_OF_FLOORS; i++) {
			requests.put(i, new HashSet<>());
			elevatorDispatchedUp[i] = false;
			elevatorDispatchedDown[i] = false;
		}

		for (int i = 0; i < NUMBER_OF_ELEVATORS; i++)
			elevators.add(null);
	}

	// Methods

	@Override
	public String toString() { return "Scheduler"; }

	// Requests

	public void handleServiceRequest(ServiceRequest serviceRequest) {
		var floor = serviceRequest.sourceFloor();
		var direction = serviceRequest.isGoingUp()
			? Direction.UP
			: Direction.DOWN;

		synchronized (requests) {
			var requests = requestsFromFloor(floor);
			requests.add(serviceRequest);

			if (elevatorDispatched(floor, direction))
				return;

			var elevatorID = selectElevator(floor, direction);
			var request = new SchedulerRequest(elevatorID);
			request.addRequest(serviceRequest);

			SchedulerSubsystem.instance
				.publishToElevatorSubsystem(
					Type.DISPATCH_ELEVATOR, request
				);

			if (direction == Direction.UP)
				elevatorDispatchedUp[floor - 1] = true;
			else
				elevatorDispatchedDown[floor - 1] = true;
		}
	}

	public Set<ServiceRequest> requestsFromFloor(int floor) {
		synchronized (requests) {
			return requests.get(floor - 1);
		}
	}

	private Set<ServiceRequest> pullRequestsFromFloor(int floor) {
		synchronized (requests) {
			var requests = this.requests.get(floor - 1);
			this.requests.put(floor - 1, new HashSet<>());
			return requests;
		}
	}

	// Elevators

	private int selectElevator(int floor, Direction requestDirection) {
		// TODO: improve selection algorithm
		var rng = new Random();

		// elevators that are still working
		var working = elevators.stream()
			.filter((e) -> {
				return e != null;
			})
			.toList();

		// elevators that are in the same cycle as the request
		var sameCycle = working.stream()
			.filter((e) -> {
				return e.currentCycle() == requestDirection
					|| e.currentCycle() == Direction.NONE;
			})
			.toList();

		if (sameCycle.size() > 0) {
			var index = rng.nextInt(sameCycle.size());
			return sameCycle.get(index).elevatorID();
		}

		var index = rng.nextInt(working.size());
		return working.get(index).elevatorID();
	}

	private boolean elevatorDispatched(int floor, Direction requestDirection) {
		if (requestDirection == Direction.UP)
			return elevatorDispatchedUp[floor - 1];
		else
			return elevatorDispatchedDown[floor - 1];
	}

	public void updateElevator(ElevatorStatus status) {
		var id = status.elevatorID();
		synchronized (elevators) {
			if (status.currentState() == ElevatorState.ELEVATOR_BLOCKED) {
				elevators.set(id, null);
			} else {
				elevators.set(id, status);
			}
		}
	}

	public void elevatorArrived(ElevatorStatus status) {
		updateElevator(status);

		var id = status.elevatorID();
		var floor = status.currentFloor();

		if (status.currentState() == ElevatorState.DOOR_BLOCKED
			|| status.currentState() == ElevatorState.ELEVATOR_BLOCKED)
			return;

		synchronized (requests) {
			var serviceRequests = pullRequestsFromFloor(floor);
			var schedulerRequest = new SchedulerRequest(id);
			schedulerRequest.addRequests(serviceRequests);
			var arrival = new ArrivalOfElevator(id, floor, status.currentCycle());

			SchedulerSubsystem.instance
				.publishToElevatorSubsystem(
					Message.Type.HANDLE_SERVICE_REQUESTS,
					schedulerRequest
				);

			SchedulerSubsystem.instance
				.publishToFloorSubsystem(
					Message.Type.ELEVATOR_ARRIVED,
					arrival
				);

			elevatorDispatchedUp[floor - 1] = false;
		}
	}

	// StateMachine conformance

	@Override
	public boolean isTerminated() { return terminated; }

	@Override
	public void terminate() { terminated = true; }

	@Override
	public SchedulerState currentState() { return state; }

	@Override
	public Scheduler getStateMachine() { return this; }

	@Override
	public void switchState(SchedulerState nextState, SchedulerEvent event) {
		Logger.log(
			toString(),
			"State",
			"Switching from " + state + " to " + nextState
		);

		state = nextState;
	}
}
