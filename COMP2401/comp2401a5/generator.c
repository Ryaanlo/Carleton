#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <time.h>
#include <string.h>
#include "simulator.h"

void main() {
  // Set up the random seed
  srand(time(NULL));
  char  buffer[20];
  char *args[5];
  int   child;
  char  xbuf[5];
  char  ybuf[5];
  char  dbuf[10];

  while(1) {
    for (int i=0; i<5; i++) {
      // Start off with a random location and direction
      short x = (int)(rand()/(double)(RAND_MAX)*CITY_WIDTH);
      short y = (int)(rand()/(double)(RAND_MAX)*CITY_HEIGHT);
      short direction = (int)((rand()/(double)(RAND_MAX))*360 - 180);
      sprintf(buffer, "./vehicle %hi %hi %hi&", x, y, direction);
      system(buffer);
    }
    sleep(1);
  }
}
