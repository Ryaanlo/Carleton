package project.communication;

import java.io.Serializable;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a request for an Elevator by a Floor
 */
public record ServiceRequest(
	LocalTime time,
	int       sourceFloor,
	boolean   isGoingUp,
	int       destinationFloor,
	Error     error
) implements Serializable {
	public enum Error {
		NO_ERROR,
		TRANSIENT_ERROR,
		PERMANENT_ERROR,
		UNKNOWN;

		public static Error from(int rawValue) {
			return switch (rawValue) {
				case 0 -> Error.NO_ERROR;
				case 1 -> Error.TRANSIENT_ERROR;
				case 2 -> Error.PERMANENT_ERROR;
				default -> Error.UNKNOWN;
			};
		}
	}

	public static ServiceRequest fromString(String requestString) {
		var request = requestString.split(" ");

		var time = LocalTime.parse(
			request[0],
			DateTimeFormatter.ofPattern("HH:mm:ss.S")
		);

		var sourceFloor = Integer.parseInt(request[1]);

		var isGoingUp = request[2].toLowerCase().equals("up")
			? true
			: false;

		var destinationFloor = Integer.parseInt(request[3]);

		var error = Error.from(Integer.parseInt(request[4]));

		return new ServiceRequest(
			time,
			sourceFloor,
			isGoingUp,
			destinationFloor,
			error
		);
	}
}
