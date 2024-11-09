package project.tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.util.concurrent.CompletableFuture;

import project.*;
import project.floor.*;

public class FloorTest {
	Floor floor;

	@BeforeEach
	public void beforeEach() {
		floor = new Floor(1);
		Async.run(floor);
	}

	@AfterEach
	public void afterEach() {
		floor.terminate();
		floor = null;
	}
}
