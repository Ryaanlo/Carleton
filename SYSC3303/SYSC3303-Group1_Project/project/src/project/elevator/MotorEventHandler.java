package project.elevator;

import project.Direction;

public interface MotorEventHandler {
	default public void motorStartingToAccelerate(Motor motor) {}
	default public void motorReachedMaxSpeed(Motor motor) {}
	default public void motorStartingToDecelerate(Motor motor) {}

	default public void motorStopped(Motor motor) {}
	default public void motorTerminated(Motor motor) {}

	default public void motorPassedAFloor(Motor motor, Direction direction) {}

	default public void motorSwitchingState(Motor motor, MotorState oldState, MotorState newsState) {}
	default public void motorWillEnterState(Motor motor, MotorState state) {}
	default public void motorDidEnterState(Motor motor, MotorState state) {}
	default public void motorWillLeaveState(Motor motor, MotorState state) {}
	default public void motorDidLeaveState(Motor motor, MotorState state) {}
}
