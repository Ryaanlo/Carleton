SYSC3303 A4
Ryan Lo (101117765)

Files:
Context.java
	- Interface to the state machine
State.java
	- The state machine itself
vehiclesGreen.java
	- A state where it is green light and the vehicles may move
vehiclesYellow.java
	- A state where the pedestrian is waiting and vehicles start to low
pedestrianWalk.java
	- A state where the pedestrian can walk
pedestrianFlash.java
	- A state where pedestrians should not start crossing
StateDemo.java
	- Test file

Setup:
Open the project
Launch StateDemo.java

Questions:
A defect in the design is that the pedestrians don't know how long the timer is left that they have to cross the street.

There is a second error in design. Can you spot it? If so, indicate what the error is in the README file included with your submission.

- An error in the design is when the pedestrians are still crossing the road when the signal is changing back to green for the cars.