#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>

#include "simulator.h"

#include <string.h>
void main() {
  int                 clientSocket;  // client socket id
  struct sockaddr_in  clientAddress; // client address
  int                 status;
  unsigned char       command[3];
  command[0] = SHUTDOWN;

  // Contact all the cell towers and ask them to shut down
  // ...

  for (int i = 0; i < NUM_TOWERS; i ++){
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

    while (1){
      printf("SENDING SIGNAL TO SHUTDOWN TOWER\n");
      send(clientSocket, &command, sizeof(command), 0);
      break;
    }
    close(clientSocket);
  }
}
