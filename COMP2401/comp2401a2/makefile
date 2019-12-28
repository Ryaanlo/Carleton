all:	corrupt decode encode fix
	gcc -o corrupt corrupt.o
	gcc -o decode decode.o
	gcc -o encode encode.o
	gcc -o fix fix.o

corrupt:	corrupt.c
	gcc -c corrupt.c

decode:	decode.c
	gcc -c decode.c

encode:	encode.c
	gcc -c encode.c

fix:	fix.c
	gcc -c fix.c

clean:
	rm -f corrupt.o decode.o encode.o fix.o corrupt decode encode fix
