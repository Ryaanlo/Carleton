package project.elevator;

import project.StateMachine;

public enum MotorState implements StateMachine.State<Motor, MotorState, MotorEvent> {
	IDLE {
		@Override
		public MotorState nextState(MotorEvent event, Motor motor) {
			return switch (event) {
				case ACCELERATE -> MotorState.ACCELERATING;
				default -> super.nextState(event, motor);
			};
		}

		@Override
		public void enter(Motor motor) {
			for (var eventHandler: motor.eventHandlers())
				eventHandler.motorStopped(motor);
		}
	},

	ACCELERATING {
		@Override
		public MotorState nextState(MotorEvent event, Motor motor) {
			return switch (event) {
				case MAX_SPEED_REACHED -> MotorState.CRUISING;
				case DECELERATE        -> MotorState.DECELERATING;
				default -> super.nextState(event, motor);
			};
		}

		@Override
		public void enter(Motor motor) {
			for (var eventHandler: motor.eventHandlers())
				eventHandler.motorStartingToAccelerate(motor);
		}

		@Override
		public void run(double dt, Motor motor) {
			motor.accelerate(dt);
		}
	},

	CRUISING {
		@Override
		public MotorState nextState(MotorEvent event, Motor motor) {
			return switch (event) {
				case DECELERATE -> MotorState.DECELERATING;
				default -> super.nextState(event, motor);
			};
		}

		@Override
		public void enter(Motor motor) {
			for (var eventHandler: motor.eventHandlers())
				eventHandler.motorReachedMaxSpeed(motor);
		}
	},

	DECELERATING {
		@Override
		public MotorState nextState(MotorEvent event, Motor motor) {
			return switch (event) {
				case STOPPED    -> MotorState.IDLE;
				case ACCELERATE -> MotorState.ACCELERATING;
				default -> super.nextState(event, motor);
			};
		}

		@Override
		public void enter(Motor motor) {
			for (var eventHandler: motor.eventHandlers())
				eventHandler.motorStartingToDecelerate(motor);
		}

		@Override
		public void run(double dt, Motor motor) {
			motor.decelerate(dt);
		}
	},

	TERMINATED {
		@Override
		public MotorState nextState(MotorEvent event, Motor motor) {
			return null;
		}

		@Override
		public void enter(Motor motor) {
			for (var eventHandler: motor.eventHandlers())
				eventHandler.motorTerminated(motor);
		}
	};

	@Override
	public MotorState nextState(MotorEvent event, Motor motor) {
		return switch (event) {
			case TERMINATE -> TERMINATED;
			default -> null;
		};
	}
}
