all:	simulator dynamic
	gcc -o fishingSimulator fishingSimulator.o
	gcc -o dynamicVersion dynamicVersion.o

simulator:	fishingSimulator.c
	gcc -c fishingSimulator.c

dynamic:	dynamicVersion.c
	gcc -c dynamicVersion.c

clean:
	rm -f fishingSimulator.o dynamicVersion.o fishingSimulator dynamicVersion
