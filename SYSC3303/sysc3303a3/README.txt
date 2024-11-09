SYSC3303 A3
Ryan Lo (101117765)

Files:
Client.java
	- Sends info to the intermediate
	- If data sent is 0 then client is asking for reply
Intermediate.java
	- Acts as an intermediate between Client and Server
	- Receives info from Client and sends it to the Server
Server.java
	- Take information from Host and validates it
	- Sends response back to Host depending on if the info was valid or not
	- If data sent is 0 then server is asking intermediate for data

Setup:
Open the project
Launch Intermediate first
Then Launch Client
And Server last

Questions:
1. Why did I suggest that you use more than one thread for the implementation of the Intermediate task?
	Since the communication between the intermediate with the client and the communication between the intermediate with the server is independent of each other. Having more than one thread allows for the communication to be done simultaneously and faster. We can also avoid blocking or waiting for one of the communication to be finished first before it proceeds to the other one.

2. Is it nescessary to use synchronized in the intermediate task? Explain.
	Since the two threads in the intermediate task are operating independent of each other, there shouldn't be any time where the two threads would be accessing the same shared resources at the same time. Synchronized is not necessary in this case.