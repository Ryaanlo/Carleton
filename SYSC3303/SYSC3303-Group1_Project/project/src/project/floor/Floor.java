package project.floor;

import project.Button;
import project.Direction;
import project.communication.ArrivalOfElevator;
import project.communication.ServiceRequest;
import project.ConcurrentTask;
import project.Configuration;

/**
 * Represents the floor subsystem.
 * A single instance is a single floor, so for a building with many floors,
 * there will be same amount of the floor instances.
 */
public class Floor implements ConcurrentTask, Button.EventHandler {
	private int id;
	private FloorButton upButton = null;
	private FloorButton downButton = null;

	private boolean terminated = false;

	public Floor(Integer id) {
		this.id = id;

		if (id < Configuration.shared.numberOfFloors) {
			upButton = new FloorButton(Direction.UP);
			upButton.setEventHandler(this);
		}

		if (id > 1) {
			downButton = new FloorButton(Direction.DOWN);
			downButton.setEventHandler(this);
		}
	}

	public void handleRequest(ServiceRequest request) {
		if (request.sourceFloor() != id)
			return;
		if (request.isGoingUp() && upButton != null) {
			upButton.press();
			FloorSubsystem.instance.sendRequestToScheduler(request);
		} else if (!request.isGoingUp() && downButton != null) {
			downButton.press();
			FloorSubsystem.instance.sendRequestToScheduler(request);
		}
	}

	@Override
	public void handleButtonPressed(Button button) {}

	public Integer getID() { return id; }

	@Override
	public boolean isTerminated() { return terminated; }

	/**
	 * Terminates the floor system.
	 * The system will not terminate immediately, instead it will terminate
	 * when it is woken up by its running thread.
	 */
	@Override
	public void terminate() { terminated = true; }

	@Override
	public String toString() {
		return "Floor " + id;
	}

	public void elevatorArrived(ArrivalOfElevator arrival) {
		var direction = arrival.currentCycle();
		switch (direction) {
			case UP:
				if (upButton != null)
					upButton.cancel();
				break;
			case DOWN:
				if (downButton != null)
					downButton.cancel();
				break;
			case NONE:
				// unreachable
				break;
		}
	}
}
