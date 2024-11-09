package project;

import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Async {
	private static final int CORE_THREAD_POOL_SIZE = 50;
	private static final ScheduledThreadPoolExecutor executor
		= new ScheduledThreadPoolExecutor(CORE_THREAD_POOL_SIZE);

	/**
	 * Asynchronously run a task once.
	 * @param task The task to perform
	 * @return The reference to the running task
	 */
	public static Future<?> run(Runnable task) {
		return executor.submit(task);
	}

	/**
	 * Asynchronously perform a task once after the specified delay has passed.
	 * @param delay The delay before the task will be executed
	 * @param task The task to perform
	 * @return The reference to the running task
	 */
	public static ScheduledFuture<?> later(double delay, Runnable task) {
		return executor.schedule(
			task,
			ms(delay),
			TimeUnit.MILLISECONDS
		);
	}

	/**
	 * Asynchronously perform a task repeatedly  with a specified period.
	 * @param period The period of the task
	 * @param task The task to perform
	 * @return The reference to the running task
	 */
	public static ScheduledFuture<?> repeat(double period, Runnable task) {
		return repeat(0, period, task);
	}

	/**
	 * Asynchronously perform a task repeatedly after the specified delay has
	 * passed and with a specified period.
	 * @param delay The delay before the task will be executed
	 * @param period The period of the task
	 * @param task The task to perform
	 * @return The reference to the running task
	 */
	public static ScheduledFuture<?> repeat(double delay, double period, Runnable task) {
		return executor.scheduleAtFixedRate(
			task,
			ms(delay),
			ms(period),
			TimeUnit.MILLISECONDS
		);
	}

	public static void terminateThreadPool() {
		executor.shutdown();
	}

	/**
	 * Converts a time in seconds to milliseconds.
	 * @param seconds The time in seconds
	 * @return The time in milliseconds
	 */
	private static long ms(double seconds) {
		return (long) (seconds * 1000);
	}
}
