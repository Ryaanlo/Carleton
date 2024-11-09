package project.elevator;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import project.Async;
import project.Configuration;
import project.Logger;
import project.communication.CommunicationChannel;
import project.communication.ElevatorStatus;
import project.communication.Message;
import project.communication.SchedulerRequest;
import project.communication.ServiceRequest;

public class ElevatorSubsystem implements CommunicationChannel.Subscriber, ElevatorObserver {
	public static final InetAddress SCHEDULER_ADDRESS = Configuration.shared.schedulerAddress;
	public static final int SCHEDULER_PORT  = Configuration.shared.schedulerPortForElevators;
	public static final int NUMBER_OF_ELEVATORS = Configuration.shared.numberOfElevators;
	public static final int NUMBER_OF_FLOORS = Configuration.shared.numberOfFloors;

	public static final ElevatorSubsystem instance = new ElevatorSubsystem();

	private List<Elevator> elevators = new ArrayList<>();

	private List<ScheduledFuture<?>> backgroundUpdates = new ArrayList<>();

	private CommunicationChannel channel;

	public static void main(String[] args) {
		var system = ElevatorSubsystem.instance;
		var gui = new ElevatorSystemMonitor(system);
		system.attachGUI(gui);

		system.elevators.forEach((e) -> {
			Async.run(e);
		});

		system.elevators.forEach((e) -> {
			var task = Async.repeat(1, 1, () -> {
				system.sendElevatorStatusToScheduler(e);
			});
			system.backgroundUpdates.add(task);
		});
	}

	private ElevatorSubsystem() {
		channel = new CommunicationChannel();
		channel.subscribe(this);

		try {
			channel.connect(SCHEDULER_ADDRESS, SCHEDULER_PORT);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		for (int i = 0; i < NUMBER_OF_ELEVATORS; ++i) {
			var elevator = new Elevator(i);
			elevators.add(elevator);
			elevator.addObserver(this);
		}
	}

	private ElevatorSystemMonitor gui = null;

	public List<Elevator> getElevators() { return elevators; }

	public void attachGUI(ElevatorSystemMonitor gui) { this.gui = gui; }

	@Override
	public String toString() { return "Elevator Subsystem"; }

	@Override
	public void channelReceivedData(CommunicationChannel channel, byte[] data) {
		var type = Message.determineMessageType(data);
		var content = Message.getContent(data);

		if (type == Message.Type.DISPATCH_ELEVATOR) {
			var request = (SchedulerRequest) content;
			var error = checkError(request);
			if (error != null) {
				simulateError(request.elevatorID(), error);
			} else {
				var floors = request.serviceRequests().stream()
						.map((r) -> {
							return r.sourceFloor();
						})
						.toList();
				dispatchElevator(request.elevatorID(), floors);
			}
		}

		else if (type == Message.Type.HANDLE_SERVICE_REQUESTS) {
			var request = (SchedulerRequest) content;
			var error = checkError(request);
			if (error != null) {
				simulateError(request.elevatorID(), error);
			} else {
				var floors = request.serviceRequests().stream()
					.map((r) -> {
						return r.destinationFloor();
					})
					.toList();
				dispatchElevator(request.elevatorID(), floors);
			}
		}

		else {
			Logger.log(
				toString(),
				"Communication",
				"Message Received: " + type
			);
		}
	}

	private ServiceRequest.Error checkError(SchedulerRequest schedulerRequest) {
		for (var request: schedulerRequest.serviceRequests()) {
			var error = request.error();

			if (error != ServiceRequest.Error.NO_ERROR
				&& error != ServiceRequest.Error.UNKNOWN)
				return error;
		}

		return null;
	}

	private void simulateError(int elevatorID, ServiceRequest.Error error) {
		var elevator = elevatorWithID(elevatorID);
		if (error == ServiceRequest.Error.TRANSIENT_ERROR)
			elevator.simulateDoorBlocked();
		else if (error == ServiceRequest.Error.PERMANENT_ERROR) {
			elevator.simulateMovementStuck();
			var index = elevators.indexOf(elevator);
			backgroundUpdates.get(index).cancel(false);
		}
	}

	@Override
	public void elevatorArrivedAtFloor(Elevator elevator, int floor) {
		try {
			var status = new ElevatorStatus(elevator);
			var message = new Message<>(Message.Type.ELEVATOR_ARRIVED, status);
			channel.publish(message);
			Logger.log(
				toString(),
				"Communication",
				"Notyfing scheduler about the arrival of elevator"
			);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void sendElevatorStatusToScheduler(Elevator elevator) {
		try {
			var status = new ElevatorStatus(elevator);
			var message = new Message<>(Message.Type.ELEVATOR_STATUS_UPDATE, status);
			channel.publish(message);
			// Logger.log(
			// 	toString(),
			// 	elevator.toString(),
			// 	"Elevator status sent to scheduler"
			// );
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (gui != null)
			gui.update();
	}

	public void dispatchElevator(int elevatorID, List<Integer> floors) {
		var elevator = elevatorWithID(elevatorID);

		if (elevator == null) {
			// TODO: handle or ignore error
		}

		for (var floor: floors) {
			elevator.addDestination(floor);

			Logger.log(
				toString(),
				"Service Request Handling",
				"Elevator " + elevatorID + " dispatched to floor " + floor
			);
		}
	}

	private Elevator elevatorWithID(int id) {
		for (var e: elevators) {
			if (e.getID() == id)
				return e;
		}
		return null;
	}

	@Override
	public void elevatorIsTerminated(Elevator elevator) {
		var index = elevators.indexOf(elevator);
		backgroundUpdates.get(index).cancel(true);
	}
}
