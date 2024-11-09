package project;

public interface ConcurrentTask extends Runnable {
	default public double timeIntervalBetweenRuns() {
		return 0.1;
	}

	default public void run(double dt) {}
	public void terminate();
	public boolean isTerminated();

	@Override
	default void run() {
		while (!isTerminated()) {
			var dt = timeIntervalBetweenRuns();
			try {
				Thread.sleep((int) (dt * 1000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			run(dt);
		}
	}
}
