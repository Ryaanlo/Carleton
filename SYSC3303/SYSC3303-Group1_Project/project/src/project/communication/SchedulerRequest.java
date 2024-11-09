package project.communication;

import java.io.Serializable;
import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;

public record SchedulerRequest(
	Instant time,
	Integer elevatorID,
	Collection<ServiceRequest> serviceRequests
) implements Serializable {
	public SchedulerRequest(Integer elevatorID) {
		this(Instant.now(), elevatorID, new HashSet<>());
	}

	public void addRequest(ServiceRequest request) {
		serviceRequests.add(request);
	}

	public void addRequests(Collection<ServiceRequest> requests) {
		serviceRequests.addAll(requests);
	}
}
