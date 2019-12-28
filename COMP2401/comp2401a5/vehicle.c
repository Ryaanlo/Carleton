#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <math.h>
#include <time.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>

#include "simulator.h"


// GPS Data for this client as well as the connected tower ID
short  x;
short  y;
short  direction;
char  connectionID;
char  connectedTowerID;


int main(int argc, char * argv[]) {
  int                 clientSocket;  // client socket id
  struct sockaddr_in  clientAddress; // client address
  int                 status;
  unsigned char       request[3];
  unsigned char       bytesArray[10];
  int                 r;

  // Set up the random seed
  srand(time(NULL));

  // Get the starting coordinate and direction from the command line arguments
  x = atoi(argv[1]);
  y = atoi(argv[2]);
  direction = atoi(argv[3]);

  // To start, this vehicle is not connected to any cell towers
  connectionID = -1;
  connectedTowerID = -1;

  // Go into an infinite loop to keep sending updates to cell towers
  while(1) {
    usleep(50000);  // A delay to slow things down a little

    // Randomize vehicle turning
    r = rand() % 3;
    if (r == 0){
      direction += VEHICLE_TURN_ANGLE;
    }else if(r == 1){
      direction -= VEHICLE_TURN_ANGLE;
    }
    // Move the vehicle
    x += VEHICLE_SPEED * cos(direction);
    y += VEHICLE_SPEED * sin(direction);

    // Set the bytesArray response
    // to the updated vehicle's new x and y
    bytesArray[1] = (x >> 24) & 0xFF;
    bytesArray[2] = (x >> 16) & 0xFF;
    bytesArray[3] = (x >> 8) & 0xFF;
    bytesArray[4] = x & 0xFF;
    bytesArray[5] = (y >> 24) & 0xFF;
    bytesArray[6] = (y >> 16) & 0xFF;
    bytesArray[7] = (y >> 8) & 0xFF;
    bytesArray[8] = y & 0xFF;

    // Checking if vehicle is connected to any tower
    // If it's not connected
    if (connectionID < 0){
      // Check if vehicle is out of city
      // If so, exit
      if (x < (0 - VEHICLE_RADIUS) || x > (CITY_WIDTH + VEHICLE_RADIUS) || y < (0 - VEHICLE_RADIUS) || y > (CITY_HEIGHT + VEHICLE_RADIUS)){
        break;
      }
      // Set the response to CONNECT
      bytesArray[0] = CONNECT;

      // Loop over all towers
      for (int i = 0; i < NUM_TOWERS; i ++){
        // Set the sockets and try to connect to each tower
        clientSocket = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);

        if (clientSocket < 0){
          printf("*** CLIENT ERROR: Could not open socket.\n");
          exit(-1);
        }

        // Set up the server address
        memset(&clientAddress, 0, sizeof(clientAddress));
        clientAddress.sin_family = AF_INET;
        clientAddress.sin_addr.s_addr = inet_addr(SERVER_IP);
        clientAddress.sin_port = htons((unsigned short) SERVER_PORT + i);

        status = connect(clientSocket, (struct sockaddr *) &clientAddress, sizeof(clientAddress));

        if (status < 0){
          printf("*** CLIENT ERROR: Could not connect.\n");
          exit(-1);
        }

        // Send the response to the tower
        send(clientSocket, bytesArray, sizeof(bytesArray), 0);
        // Get back a response
        recv(clientSocket, request, sizeof(request), 0);

        // If the response from the tower was YES
        if (request[0] == YES){
          // Set the vehicle's IDs from the received data
          connectionID = request[1];
          connectedTowerID = request[2];
          close(clientSocket);
          break;
        // If request was NO
        }else{
          // Close socket and try next tower
          close(clientSocket);
        }
      }
    // If the vehicle is already connected
    }else{
      // Set to UPDATE and the ID at which the vehicle is connected to the tower
      bytesArray[0] = UPDATE;
      bytesArray[9] = connectionID;

      // Set up the socket
      clientSocket = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);

      if (clientSocket < 0){
        printf("*** CLIENT ERROR: Could not open socket.\n");
        exit(-1);
      }

      // Set up the server address
      memset(&clientAddress, 0, sizeof(clientAddress));
      clientAddress.sin_family = AF_INET;
      clientAddress.sin_addr.s_addr = inet_addr(SERVER_IP);
      clientAddress.sin_port = htons((unsigned short) SERVER_PORT + connectedTowerID);

      status = connect(clientSocket, (struct sockaddr *) &clientAddress, sizeof(clientAddress));

      if (status < 0){
        printf("*** CLIENT ERROR: Could not connect.\n");
        exit(-1);
      }

      // Send the response
      send(clientSocket, bytesArray, sizeof(bytesArray), 0);
      // Receive response from server
      recv(clientSocket, request, sizeof(request), 0);

      // If the server says vehicle is out of range
      if (request[0] == NO){
        // Disconnect it and close the socket
        connectionID = -1;
        connectedTowerID = -1;
      }
      // Close socket
      close(clientSocket);
    }
  }
  printf("Vehicle Exiting\n");
}
