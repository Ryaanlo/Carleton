COMP2401A5
Ryan Lo (101117765)

Source Files:
- cellTower.c
- display.c
- generator.c
- makefile
- README
- simulator.c
- simulator.h
- stop.c
- vehicle.c

Compiling: make

Running:
- Start by running simulator.c in the background 
./simulator&

Either:
Create one vehicle using where x, y, direction are commandline args as ints
./vehicle x y direction &

OR run generator which randomly creates and places 5 vehicles per second
./generator& 

Running "stop" will contact all celltowers and shut them down
./stop
