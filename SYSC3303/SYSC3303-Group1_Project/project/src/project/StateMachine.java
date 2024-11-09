package project;

public interface StateMachine<
	SomeStateMachine,
	SomeState extends StateMachine.State<
		SomeStateMachine,
		SomeState,
		SomeEvent
	>,
	SomeEvent
> extends ConcurrentTask {
	public interface State<TheStateMachine, TheState, TheEvent> {
		public TheState nextState(TheEvent event, TheStateMachine stateMachine);

		default public void enter(TheStateMachine stateMachine) {}

		default public void leave(TheStateMachine stateMachine) {}

		default public void run(double dt, TheStateMachine stateMachine) {}
	}

	public SomeState currentState();
	public SomeStateMachine getStateMachine();
	public void switchState(SomeState nextState, SomeEvent event);

	default public void willEnterState(SomeState state) {}
	default public void didEnterState(SomeState state) {}
	default public void willLeaveState(SomeState state) {}
	default public void didLeaveState(SomeState state) {}

	default public void handleEvent(SomeEvent event) {
		synchronized (this) {
			var stateMachine = getStateMachine();

			var currentState = currentState();
			var nextState = currentState.nextState(event, stateMachine);

			if (nextState != null && nextState != currentState) {
				willLeaveState(currentState);
				currentState().leave(stateMachine);
				didLeaveState(currentState);

				switchState(nextState, event);

				willEnterState(nextState);
				nextState.enter(stateMachine);
				didEnterState(nextState);
			}
		}
	}

	@Override
	default void run(double dt) {
		var stateMachine = getStateMachine();
		synchronized (stateMachine) {
			currentState().run(dt, stateMachine);
		}
	}
}
