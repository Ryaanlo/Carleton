all: maze display
	gcc -o mazes mazes.o mazeDisplay.o -lX11 -Wall

maze:	mazes.c mazeDisplay.h graphSet.h
	gcc -c mazes.c
display:	mazeDisplay.c mazeDisplay.h graphSet.h
	gcc -c mazeDisplay.c
clean:
	rm -f mazeDisplay.o mazes.o mazes
