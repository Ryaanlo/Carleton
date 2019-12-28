all:	convert disasters
	gcc -o convertToBinary convertToBinary.o
	gcc -o disasterAnalyzer disasterAnalyzer.o

convert:	convertToBinary.c CDD.h
	gcc -c convertToBinary.c

disasters:	disasterAnalyzer.c
	gcc -c disasterAnalyzer.c

clean:
	rm -f convertToBinary.o disasterAnalyzer.o convertToBinary disasterAnalyzer
