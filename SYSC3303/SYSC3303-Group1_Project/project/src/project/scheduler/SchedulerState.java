package project.scheduler;

import project.StateMachine;

public enum SchedulerState implements StateMachine.State<Scheduler, SchedulerState, SchedulerEvent> {
	IDLE {
		@Override
		public SchedulerState nextState(SchedulerEvent event, Scheduler scheduler) {
			return switch (event) {
				case NEW_SERVICE_REQUEST -> PROCESSING_SERVICE_REQUEST;
				case NEW_ELEVATOR_STATUS_UPDATE -> RECEIVING_ELEVATOR_STATUS_UPDATE;
				default -> super.nextState(event, scheduler);
			};
		}
	},

	PROCESSING_SERVICE_REQUEST {
		@Override
		public SchedulerState nextState(SchedulerEvent event, Scheduler scheduler) {
			return switch (event) {
				case SERVICE_REQUEST_PROCESSED -> IDLE;
				default -> super.nextState(event, scheduler);
			};
		}
	},

	RECEIVING_ELEVATOR_STATUS_UPDATE {
		@Override
		public SchedulerState nextState(SchedulerEvent event, Scheduler scheduler) {
			return switch (event) {
				case FINISHED_PROCESSING_ELEVATOR_UPDATE -> IDLE;
				default -> super.nextState(event, scheduler);
			};
		}
	};

	@Override
	public SchedulerState nextState(SchedulerEvent event, Scheduler scheduler) {
		return null;
	}
}
