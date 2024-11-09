<h1>Use CASE 1: Operating the Oasis Pro</h1>

         	Primary Actor: User
         	Scope: CES Device
         	Level: User Goal
         	Stakeholders and Interests:
         	User – wants to use the device
         	Device – works as intended
         	Precondition: CES device is turned off
         	Minimal guarantees:
         	Success guarantees: Device works and shuts down when done
         	Trigger: user uses oasis pro
         	Main success scenario:
                  
1.       User inserts the battery
2.       User presses power button to turn on device
3.       CES device gets powered on
4.       Display menu shows
5.       Battery level indicates the remaining battery
6.       User selects session length
7.       User selects a session type
8.       Electrical connection gets tested
9.       User adjusts intensity
10.      Session begins
11.      Session finishes
12.      Device shuts down when completed session
     
     Extensions:

         5a. If battery level drops below critical level, then warning message “low battery” is shown

         6a. If user selects session length from menu, go to use case 2

         7a. If user selects session type from menu, go to use case 3

         8a. If user selects connect test, go to use case 4

         9a. If user selects intensity from menu, go to use case 5

         10a. If user selects ending a session from menu, go to use case 6

         11a. If user holds select button to save user preferences, go to use case 7
 
<h1>Use CASE 2: Select Session Length</h1>

         	Primary Actor: User
         	Scope: CES Device
         	Level: User Goal
         	Stakeholders and Interests:
         	User – wants to use the device
         	Device – works as intended
         	Precondition: CES device is turned on and session length selected from menu
         	Minimal guarantees: Device shutdown
         	Success guarantees: Sets session length
         	Trigger: user selects session length from menu
         	Main success scenario:
1.      User is given a list of session lengths (4 presets)
2.      User selects a length from the list by using the navigation buttons and presses OK to confirm
3.      Frequency and mode icons associated with session lights up to indicate it being used
4.      Back to use case 1 step 7
           	
Extensions:
        
        2a. If no number is lit, then group has no sessions programmed into it
 
<h1>Use CASE 3: Select Session Type</h1>

         	Primary Actor: User
         	Scope: CES Device
         	Level: User Goal
         	Stakeholders and Interests:
         	User – wants to use the device
         	Device – works as intended
         	Precondition: CES device is turned on and session length selected from menu
         	Minimal guarantees: Device shutdown
         	Success guarantees: Sets session type
         	Trigger: user selects session length from menu
         	Main success scenario:
                  
1.      User is given a list of session types (4 presets)
2.      User selects a type from the list by using the navigation buttons and presses OK to confirm
3.      Frequency and mode icons associated with session lights up to indicate it being used
4.      Back to use case 1 step 8
        
<h1>Use CASE 4: Connection Test</h1>

         	Primary Actor: User
         	Scope: CES Device
         	Level: User Goal
         	Stakeholders and Interests:
         	User – wants to use the device
         	Device – works as intended
         	Precondition: CES device is turned on and session starting
         	Minimal guarantees: Device shutdown
         	Success guarantees: Connection is good
         	Trigger: user selects session
         	Main success scenario:
                  
1.      Device checks for electrical connection
2.      Status of connection is displayed
3.      Back to use case 1 step 9

Extensions:
        
        1a. If ear clips are disconnected, device with pause session and wait for ear clips reconnection
 
<h1>Use CASE 5: Selecting Intensity</h1>

         	Primary Actor: User
         	Scope: CES Device
         	Level: User Goal
         	Stakeholders and Interests:
         	User – wants to use the device
         	Device – works as intended
         	Precondition: CES device is turned on and session starting
         	Minimal guarantees: Device shutdown
         	Success guarantees: Intensity selected
         	Trigger: user starts session
         	Main success scenario:
                  
1.      Display shows intensity level
2.      Intensity adjusted by user using up and down buttons
3.      Back to use case 1 step 10
 
<h1>Use CASE 6: Saving User Preferences</h1>

         	Primary Actor: User
         	Scope: CES Device
         	Level: User Goal
         	Stakeholders and Interests:
         	User – wants to use the device
         	Device – works as intended
         	Precondition: CES device is turned on and session done
         	Minimal guarantees: Device shutdown
         	Success guarantees: Preferences saved
         	Trigger: user starts session
         	Main success scenario:
                  
1.      User holds select button for one second
2.      Display shows an animation for saving preferences
3.      Back to use case 1 step 12
