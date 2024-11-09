package project.floor;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;

import project.Async;
import project.Configuration;
import project.Logger;
import project.communication.ArrivalOfElevator;
import project.communication.CommunicationChannel;
import project.communication.Message;
import project.communication.ServiceRequest;
import project.communication.Message.Type;

public class FloorSubsystem implements CommunicationChannel.Subscriber {
	public static void main(String[] args) {
		var system = FloorSubsystem.instance;
		Logger.log(system.toString(), "N/A", "Starting floor subsystem");

		Logger.log(system.toString(), "N/A", "Starting floors");
		system.floors.forEach((f) -> {
			Async.run(f);
		});

		Logger.log(system.toString(), "N/A", "Sending requests to floors");
		system.handleRequests();

		Logger.log(system.toString(), "N/A", "Terminating floor subsystem");
		system.terminate();
	}

	private static final InetAddress SCHEDULER_ADDRESS = Configuration.shared.schedulerAddress;
	private static final int SCHEDULER_PORT  = Configuration.shared.schedulerPortForFloors;

	public static final FloorSubsystem instance = new FloorSubsystem();

	private List<Floor>          floors;
	private List<ServiceRequest> requests;
	private CommunicationChannel channel;

	private FloorSubsystem() {
		floors   = new ArrayList<>();
		requests = new ArrayList<>();
		channel  = new CommunicationChannel();
		channel.subscribe(this);

		try {
			channel.connect(SCHEDULER_ADDRESS, SCHEDULER_PORT);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		initFloors(Configuration.shared.numberOfFloors);
		initRequests(Configuration.shared.floorRequestsFilePath);
	}

	private void initRequests(String filepath) {
		try {
			FileReader fileReader = new FileReader(filepath);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			bufferedReader.lines().forEach(line -> {
				requests.add(ServiceRequest.fromString(line));
			});

			bufferedReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initFloors(Integer num_floors) {
		for (int i = 0; i < num_floors; i++) {
			floors.add(new Floor(i + 1));
		}
	}

	@Override
	public String toString() { return "Floor Subsystem"; }

	@Override
	public void channelReceivedData(CommunicationChannel channel, byte[] data) {
		var type = Message.determineMessageType(data);
		var content = Message.getContent(data);

		if (type == Type.ELEVATOR_ARRIVED) {
			var arrival = (ArrivalOfElevator) content;
			floors.get(arrival.floor() - 1)
				.elevatorArrived(arrival);
		}

		else {
			Logger.log(
				toString(),
				"Communication",
				"Message received: " + type
			);
		}
	}

	public void sendRequestToScheduler(ServiceRequest request) {
		var floor = floors.get(request.sourceFloor() - 1);
		try {
			var message = new Message<>(Message.Type.NEW_SERVICE_REQUEST, request);
			channel.publish(message);

			Logger.log(
				toString(),
				floor.toString(),
				"Request sent: " + request
			);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void handleRequests() {
		requests.forEach((r) -> {
			// Sleep for 5 seconds
			try {
				TimeUnit.SECONDS.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			var floor = floors.get(r.sourceFloor() - 1);
			floor.handleRequest(r);
		});
	}

	private void terminate() {
		floors.forEach((f) -> {
			f.terminate();
		});

		try {
			channel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Async.terminateThreadPool();
	}
}
