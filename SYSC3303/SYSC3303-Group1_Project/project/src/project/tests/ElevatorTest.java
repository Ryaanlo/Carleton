package project.tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.util.concurrent.CompletableFuture;

import project.*;
import project.elevator.*;

class ElevatorTest {
	Elevator elevator = null;

	@BeforeEach
	public void initializeElevator() {
		elevator = new Elevator();
		Async.run(elevator);
	}

	@AfterEach
	public void terminateElevator() {
		elevator.terminate();
		elevator = null;
	}

	@Test
	public void testElevatorInitialStatus() {
		assertEquals(1, elevator.currentFloor());
		assertNull(elevator.nextDestination());
		assertTrue(elevator.isStopped());
	}

	@Test
	public void testAddDestinations() {
		for (int i = 1; i < Elevator.NUMBER_OF_FLOORS; ++i) {
			assertFalse(elevator.containsDestination(i));
			elevator.pressFloorButton(i);
			if (i == elevator.currentFloor())
				assertFalse(elevator.containsDestination(i));
			else
				assertTrue(elevator.containsDestination(i));
		}
	}

	@Test
	public void textNextDestination() {
		elevator.pressFloorButton(3);
		assertEquals(3, elevator.nextDestination());
	}

	@Test
	@Timeout(10)
	public void testGoToFloor2() {
		var f = 2;

		elevator.pressFloorButton(f);

		var future = new CompletableFuture<>();
		elevator.addObserver(new ElevatorObserver() {
			@Override
			public void elevatorArrivedAtFloor(Elevator elevator, int floor) {
				if (floor == f)
					future.complete(null);
			}
		});

		wait(future);
		assertFalse(future.isCompletedExceptionally());

		assertTrue(elevator.isStopped());
		assertEquals(f, elevator.currentFloor());
	}

	@Test
	@Timeout(12)
	public void testGoToFloor3() {
		var f = 3;

		elevator.pressFloorButton(f);

		var future = new CompletableFuture<>();
		elevator.addObserver(new ElevatorObserver() {
			@Override
			public void elevatorArrivedAtFloor(Elevator elevator, int floor) {
				if (floor == f)
					future.complete(null);
			}
		});

		wait(future);
		assertFalse(future.isCompletedExceptionally());

		assertTrue(elevator.isStopped());
		assertEquals(f, elevator.currentFloor());
	}

	@Test
	@Timeout(35)
	public void testGoToFloor2ThenFloor5() {
		var f1 = 2;
		var f2 = 5;

		elevator.pressFloorButton(f1);
		elevator.pressFloorButton(f2);

		var future = new CompletableFuture<>();
		elevator.addObserver(new ElevatorObserver() {
			@Override
			public void elevatorArrivedAtFloor(Elevator elevator, int floor) {
				if (floor == f2)
					future.complete(null);
			}
		});

		wait(future);
		assertFalse(future.isCompletedExceptionally());

		assertTrue(elevator.isStopped());
		assertEquals(f2, elevator.currentFloor());
	}

	@Test
	@Timeout(32)
	public void testGoToFloor3ThenFloor4() {
		var f1 = 3;
		var f2 = 4;

		elevator.pressFloorButton(f1);
		elevator.pressFloorButton(f2);

		var future = new CompletableFuture<>();
		elevator.addObserver(new ElevatorObserver() {
			@Override
			public void elevatorArrivedAtFloor(Elevator elevator, int floor) {
				if (floor == f2)
					future.complete(null);
			}
		});

		wait(future);
		assertFalse(future.isCompletedExceptionally());

		assertTrue(elevator.isStopped());
		assertEquals(f2, elevator.currentFloor());
	}

	@Test
	@Timeout(42)
	public void testGoToFloor5ThenFloor2() {
		var lower = 2;
		var middle = 4;
		var upper = 5;

		elevator.pressFloorButton(upper);

		var future = new CompletableFuture<>();
		elevator.addObserver(new ElevatorObserver() {
			boolean hasReachedUpper = false;

			@Override
			public void elevatorArrivedAtFloor(Elevator elevator, int floor) {
				if (floor == lower && hasReachedUpper)
					future.complete(null);
				else if (floor == middle)
					elevator.pressFloorButton(lower);
				else if (floor == upper)
					hasReachedUpper = true;
			}
		});

		wait(future);
		assertFalse(future.isCompletedExceptionally());

		assertTrue(elevator.isStopped());
		assertEquals(lower, elevator.currentFloor());
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
