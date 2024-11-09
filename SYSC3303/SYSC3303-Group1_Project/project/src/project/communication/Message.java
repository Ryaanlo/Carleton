package project.communication;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;

public record Message<Content extends Serializable>(
	Message.Type type,
	Content content
) {
	public enum Type {
		/** There is a new service request. */
		NEW_SERVICE_REQUEST,

		/** An elevator has status update. */
		ELEVATOR_STATUS_UPDATE,

		/** Dispatches an elevator to a floor. */
		DISPATCH_ELEVATOR,

		/** Let an elevator handle given requests. */
		HANDLE_SERVICE_REQUESTS,

		/** An elevator has arrived at a floor. */
		ELEVATOR_ARRIVED,

		UNKNOWN;
	}

	public static Type determineMessageType(byte[] data) {
		try (
			var bis = new ByteArrayInputStream(data);
			var ois = new ObjectInputStream(bis);
		) {
			return (Type) ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
			return Type.UNKNOWN;
		}
	}

	public static Object getContent(byte[] data) {
		try (
			var bis = new ByteArrayInputStream(data);
			var ois = new ObjectInputStream(bis);
		) {
			ois.readObject();
			ois.readByte();
			return ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
			return new byte[0];
		}
	}
}
