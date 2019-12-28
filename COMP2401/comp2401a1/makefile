all:	cards patio
	gcc -o cards cards.o
	gcc -o patioBuilder patioBuilder.o -lm

cards:	cards.c
	gcc -c cards.c

patio:	patioBuilder.c
	gcc -c patioBuilder.c

clean:
	rm -f cards.o patioBuilder.o cards patioBuilder
