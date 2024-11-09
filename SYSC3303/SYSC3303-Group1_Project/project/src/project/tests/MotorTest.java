package project.tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.util.concurrent.CompletableFuture;

import project.*;
import project.elevator.*;

public class MotorTest {
	Motor motor;

	@BeforeEach
	public void initializeElevator() {
		motor = new Motor();
		Async.run(motor);
	}

	@AfterEach
	public void terminateElevator() {
		motor.terminate();
		motor = null;
	}

	@Test
	public void testInitialization() {
		assertFalse(motor.isMoving());
		assertEquals(MotorState.IDLE, motor.currentState());
	}

	@Test
	public void testStateTransitions() {
		testStateTransition(MotorState.IDLE, MotorEvent.ACCELERATE, MotorState.ACCELERATING);
		testStateTransition(MotorState.ACCELERATING, MotorEvent.MAX_SPEED_REACHED, MotorState.CRUISING);
		testStateTransition(MotorState.CRUISING, MotorEvent.DECELERATE, MotorState.DECELERATING);
		testStateTransition(MotorState.DECELERATING, MotorEvent.STOPPED, MotorState.IDLE);

		testStateTransition(MotorState.IDLE, MotorEvent.ACCELERATE, MotorState.ACCELERATING);
		testStateTransition(MotorState.ACCELERATING, MotorEvent.DECELERATE, MotorState.DECELERATING);
		testStateTransition(MotorState.DECELERATING, MotorEvent.ACCELERATE, MotorState.ACCELERATING);
	}

	private void testStateTransition(
		MotorState oldState,
		MotorEvent event,
		MotorState newState
	) {
		var future = new CompletableFuture<>();
		motor.addEventHandler(new MotorEventHandler() {
			@Override
			public void motorDidEnterState(Motor motor, MotorState state) {
				future.complete(null);
			}
		});

		assertEquals(oldState, motor.currentState());
		motor.handleEvent(event);
		wait(future);
		assertFalse(future.isCompletedExceptionally());
		assertEquals(newState, motor.currentState());
	}

	@Test
	@Timeout(10)
	public void testMove1FloorUp() {
		var future = new CompletableFuture<>();
		motor.addEventHandler(new MotorEventHandler() {
			@Override
			public void motorStopped(Motor motor) {
				future.complete(null);
			}
		});

		motor.move(Direction.UP, 1);
		assertTrue(motor.isMoving());
		assertEquals(Direction.UP, motor.currentMovingDirection());

		wait(future);
		assertFalse(future.isCompletedExceptionally());

		assertTrue(motor.isStopped());
		assertEquals(Direction.NONE, motor.currentMovingDirection());
	}

	@Test
	@Timeout(10)
	public void testMove1FloorDown() {
		var future = new CompletableFuture<>();
		motor.addEventHandler(new MotorEventHandler() {
			@Override
			public void motorStopped(Motor motor) {
				future.complete(null);
			}
		});

		motor.move(Direction.DOWN, 1);
		assertTrue(motor.isMoving());
		assertEquals(Direction.DOWN, motor.currentMovingDirection());

		wait(future);
		assertFalse(future.isCompletedExceptionally());

		assertTrue(motor.isStopped());
		assertEquals(Direction.NONE, motor.currentMovingDirection());
	}

	@Test
	@Timeout(12)
	public void testMove2FloorUp() {
		var future = new CompletableFuture<>();
		motor.addEventHandler(new MotorEventHandler() {
			@Override
			public void motorStopped(Motor motor) {
				future.complete(null);
			}
		});

		motor.move(Direction.UP, 2);
		assertTrue(motor.isMoving());
		assertEquals(Direction.UP, motor.currentMovingDirection());

		wait(future);
		assertFalse(future.isCompletedExceptionally());

		assertTrue(motor.isStopped());
		assertEquals(Direction.NONE, motor.currentMovingDirection());
	}

	@Test
	@Timeout(12)
	public void testMove2FloorDown() {
		var future = new CompletableFuture<>();
		motor.addEventHandler(new MotorEventHandler() {
			@Override
			public void motorStopped(Motor motor) {
				future.complete(null);
			}
		});

		motor.move(Direction.DOWN, 2);
		assertTrue(motor.isMoving());
		assertEquals(Direction.DOWN, motor.currentMovingDirection());

		wait(future);
		assertFalse(future.isCompletedExceptionally());

		assertTrue(motor.isStopped());
		assertEquals(Direction.NONE, motor.currentMovingDirection());
	}

	private synchronized void wait(CompletableFuture<?> future) {
		while (!future.isDone()) {
			try {
				wait(1000);
			} catch (InterruptedException e) {
				future.completeExceptionally(e);
				return;
			}
		}
	}
}
