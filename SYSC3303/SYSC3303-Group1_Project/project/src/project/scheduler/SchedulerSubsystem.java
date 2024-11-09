package project.scheduler;

import java.io.Serializable;

import project.Async;
import project.ConcurrentTask;
import project.Configuration;
import project.Logger;
import project.communication.CommunicationChannel;
import project.communication.ElevatorStatus;
import project.communication.Message;
import project.communication.ServiceRequest;
import project.communication.Message.Type;

public class SchedulerSubsystem implements ConcurrentTask, CommunicationChannel.Subscriber {
	public static void main(String[] args) {
		var system = SchedulerSubsystem.instance;
		Async.run(system);
	}

	public static final SchedulerSubsystem instance = new SchedulerSubsystem();

	private SchedulerSubsystem() {
		scheduler = new Scheduler();

		floorChannel    = new CommunicationChannel(Configuration.shared.schedulerPortForFloors);
		elevatorChannel = new CommunicationChannel(Configuration.shared.schedulerPortForElevators);

		floorChannel.subscribe(this);
		elevatorChannel.subscribe(this);
	}

	private Scheduler scheduler;
	private CommunicationChannel floorChannel;
	private CommunicationChannel elevatorChannel;

	@Override
	public String toString() { return "Scheduler Subsystem"; }

	@Override
	public boolean isTerminated() { return scheduler.isTerminated(); }

	@Override
	public void terminate() {
		if (isTerminated())
			return;

		scheduler.terminate();

		try { floorChannel.close(); }
		catch (Exception e) { e.printStackTrace(); }

		try { elevatorChannel.close(); }
		catch (Exception e) { e.printStackTrace(); }

		Async.terminateThreadPool();
	}

	@Override
	public void run() {
		Async.run(scheduler);
		ConcurrentTask.super.run();
	}

	@Override
	public void channelReceivedData(CommunicationChannel channel, byte[] data) {
		var type = Message.determineMessageType(data);
		var content = Message.getContent(data);

		if (channel == floorChannel)
			receivedFloorSystemMessage(type, content);
		else if (channel == elevatorChannel)
			receivedElevatorSystemMessage(type, content);
		else
			// Unreachable
			Logger.log(
				toString(),
				"Communication",
				"Received unknown data: " + new String(data)
			);
	}

	private void receivedFloorSystemMessage(Message.Type type, Object content) {
		if (type == Type.NEW_SERVICE_REQUEST) {
			var request = (ServiceRequest) content;
			Logger.log(
				toString(),
				"Communication",
				"Request received: " + request
			);
			scheduler.handleServiceRequest(request);
		}
	}

	private void receivedElevatorSystemMessage(Message.Type type, Object content) {
		var status = (ElevatorStatus) content;

		if (type == Type.ELEVATOR_STATUS_UPDATE) {
			// Logger.log(
			// 	toString(),
			// 	"Elevator " + status.elevatorID(),
			// 	"Elevator status received"
			// );
			scheduler.updateElevator(status);
		} else if (type == Type.ELEVATOR_ARRIVED) {
			Logger.log(
				toString(),
				"Elevator " + status.elevatorID(),
				"Elevator arrived at floor " + status.currentFloor()
			);
			scheduler.elevatorArrived(status);
		}
	}

	public <T extends Serializable> void publishToFloorSubsystem(
		Message.Type type, T content
	) {
		publish(floorChannel, type, content);
	}

	public <T extends Serializable> void publishToElevatorSubsystem(
		Message.Type type, T content
	) {
		publish(elevatorChannel, type, content);
	}

	private <T extends Serializable> void publish(
		CommunicationChannel channel,
		Message.Type type,
		T content
	) {
		try {
			var message = new Message<>(type, content);
			channel.publish(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
