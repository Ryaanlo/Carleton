package project.elevator;

import project.Async;
import project.Direction;
import project.Logger;
import project.StateMachine;

public enum ElevatorState implements StateMachine.State<Elevator, ElevatorState, ElevatorEvent> {
	IDLE {
		@Override
		public ElevatorState nextState(ElevatorEvent event, Elevator elevator) {
			return switch (event) {
				case DESTINATION_ARRIVED -> ARRIVED_AT_DESTINATION;
				case START_MOVING        -> MOVING;
				case BLOCK_MOVEMENT      -> ELEVATOR_BLOCKED;
				default -> super.nextState(event, elevator);
			};
		}

		@Override
		public void run(double dt, Elevator elevator) {
			var nextDestination = elevator.nextDestination();
			if (nextDestination == null)
				return;

			var floors = nextDestination - elevator.currentFloor();
			var direction = floors > 0 ? Direction.UP : Direction.DOWN;

			elevator.move(direction, Math.abs(floors));
		}
	},

	MOVING {
		@Override
		public ElevatorState nextState(ElevatorEvent event, Elevator elevator) {
			return switch (event) {
				case DESTINATION_ARRIVED -> ARRIVED_AT_DESTINATION;
				case BLOCK_MOVEMENT      -> ELEVATOR_BLOCKED;
				default -> super.nextState(event, elevator);
			};
		}
	},

	ARRIVED_AT_DESTINATION {
		@Override
		public ElevatorState nextState(ElevatorEvent event, Elevator elevator) {
			return switch (event) {
				case OPEN_DOOR -> DOOR_OPENING;
				default -> super.nextState(event, elevator);
			};
		}

		@Override
		public void enter(Elevator elevator) {
			var f = elevator.currentFloor();
			elevator.removeDestination(f);
			elevator.cancelFloorButton(f);
			Async.later(3, () -> {
				elevator.openDoor();
			});
		}
	},

	DOOR_OPENING {
		@Override
		public ElevatorState nextState(ElevatorEvent event, Elevator elevator) {
			return switch (event) {
				case DOOR_OPENED -> AWAITING_PASSENGERS;
				case CLOSE_DOOR  -> DOOR_CLOSING;
				default -> super.nextState(event, elevator);
			};
		}
	},

	AWAITING_PASSENGERS {
		@Override
		public ElevatorState nextState(ElevatorEvent event, Elevator elevator) {
			return switch (event) {
				case CLOSE_DOOR -> DOOR_CLOSING;
				default -> super.nextState(event, elevator);
			};
		}

		@Override
		public void enter(Elevator elevator) {
			Async.later(
				Elevator.LOADING_TIME,
				() -> {
					elevator.closeDoor();
				}
			);
		}
	},

	DOOR_CLOSING {
		@Override
		public ElevatorState nextState(ElevatorEvent event, Elevator elevator) {
			return switch (event) {
				case OPEN_DOOR   -> DOOR_OPENING;
				case DOOR_CLOSED -> IDLE;
				case BLOCK_DOOR  -> DOOR_BLOCKED;
				default -> super.nextState(event, elevator);
			};
		}
	},

	DOOR_BLOCKED {
		@Override
		public ElevatorState nextState(ElevatorEvent event, Elevator elevator) {
			return switch (event) {
				case UNBLOCK_DOOR -> DOOR_CLOSING;
				default -> super.nextState(event, elevator);
			};
		}
	},

	ELEVATOR_BLOCKED,

	TERMINATED {
		@Override
		public ElevatorState nextState(ElevatorEvent event, Elevator elevator) {
			return null;
		}

		@Override
		public void enter(Elevator elevator) {
			Logger.log(
				elevator.toString(),
				"Elevator",
				"Elevator is terminated"
			);
		}
	};

	@Override
	public ElevatorState nextState(ElevatorEvent event, Elevator elevator) {
		return switch (event) {
			case TERMINATE -> TERMINATED;
			default -> null;
		};
	}
}
