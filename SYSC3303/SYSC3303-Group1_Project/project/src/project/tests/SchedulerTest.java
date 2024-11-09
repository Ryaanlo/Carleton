package project.tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.util.concurrent.CompletableFuture;

import project.*;
import project.scheduler.*;

public class SchedulerTest {
	Scheduler scheduler;

	@BeforeEach
	public void beforeEach() {
		scheduler = new Scheduler();
		Async.run(scheduler);
	}

	@AfterEach
	public void afterEach() {
		scheduler.terminate();
		scheduler = null;
	}
}
