#include <string.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <math.h>


// Handle client requests coming in through the server socket.  This code should run
// indefinitiely.  It should wait for a client to send a request, process it, and then
// close the client connection and wait for another client.  The requests that may be
// handles are SHUTDOWN, CONNECT and UPDATE.  A SHUTDOWN request causes the tower to
// go offline.   A CONNECT request contains 4 additional bytes which are the high and
// low bytes of the vehicle's X coordinate, followed by the high and low bytes of its
// Y coordinate.  If within range of this tower, the connection is accepted and a YES
// is returned, along with a char id for the vehicle and the tower id.   If UPDATE is
// received, the additional 4 byes for the (X,Y) coordinate are also received as well
// as the id of the vehicle.   Then YES is returned if the vehicle is still within
// the tower range, otherwise NO is returned.
void *handleIncomingRequests(void *ct) {
  CellTower       *tower = ct;


  int                   serverSocket, clientSocket;
  struct sockaddr_in    serverAddress, clientAddr;
  int                   status, addrSize, bytesRcv;
  unsigned char         command[10];
  unsigned char         response[3];
  int                   x;
  int                   y;
  int                   id;

  // Set up server socket
  serverSocket = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);
  if (serverSocket < 0){
    printf("*** SERVER ERROR: Could not open socket.\n");
    exit(-1);
  }

  // Set up the server address
  memset(&serverAddress, 0, sizeof(serverAddress));
  serverAddress.sin_family = AF_INET;
  serverAddress.sin_addr.s_addr = htonl(INADDR_ANY);
  serverAddress.sin_port = htons((unsigned short) SERVER_PORT + tower->id);

  // Bind the server socket
  status = bind(serverSocket, (struct sockaddr *) &serverAddress, sizeof(serverAddress));

  if (status < 0){
    printf("*** SERVER ERROR: Could not bind socket.\n");
    exit(-1);
  }

  // Listen for connections
  status = listen(serverSocket, MAX_CONNECTIONS);
  if (status < 0){
    printf("*** SERVER ERROR: Could not listen on socket.\n");
    exit(-1);
  }

  // Go into loop to wait for clients
  while(1){
    addrSize = sizeof(clientAddr);
    clientSocket = accept(serverSocket, (struct sockaddr *) &clientAddr, &addrSize);
    if (clientSocket < 0){
      printf("*** SERVER ERROR: Could not accept incoming client connection.\n");
      exit(-1);
    }

    // Go into loop to talk to clients
    while(1){
      // Receive information from vehicle
      bytesRcv = recv(clientSocket, command, sizeof(command), 0);
      // Extract the x and y coordinate of vehicle
      x = command[3] * 256 + command[4];
      y = command[7] * 256 + command[8];

      // If command is SHUTDOWN
      if (command[0] == SHUTDOWN){
        break;
      // If command is CONNECT
      }else if(command[0] == CONNECT){
        // Checking if tower is at max vehicles connected
        if (tower->numConnectedVehicles == MAX_CONNECTIONS){
          // Set response to NO and send it to the client
          response[0] = NO;
          send(clientSocket, response, sizeof(response), 0);
        // If there is still space
        }else{
          // Checking to see if the vehicle is in range of the tower
          if ((sqrt(pow(x - tower->x, 2) + pow(y - tower->y, 2))) < tower->radius){
            // Set the reponse command, tower ID
            response[0] = YES;
            response[2] = tower->id;
            // Checking for open space in tower
            for (int i = 0; i < MAX_CONNECTIONS; i ++){
              // If there is onpen space, update the tower
              // with vehicle information
              if (tower->connectedVehicles[i].connected == 0){
                tower->connectedVehicles[i].x = x;
                tower->connectedVehicles[i].y = y;
                tower->connectedVehicles[i].connected = 1;
                response[1] = i;
                break;
              }
            }
            // Increase tower's vehicle connected count
            tower->numConnectedVehicles ++;
            // Send back response
            send(clientSocket, response, sizeof(response), 0);
          // If vehicle is not in range
          }else{
            // Set reponse to NO and send it back
            response[0] = NO;
            send(clientSocket, response, sizeof(response), 0);
          }
        }
        // Exit connection with client
        break;
      // If the command is UPDATE
      }else if(command[0] == UPDATE){
        // Extract the ID of the position the vehicle is in for the tower
        id = command[9];
        // Checking to see if vehicle is still in range of tower
        if ((sqrt(pow(x - tower->x, 2) + pow(y - tower->y, 2))) < tower->radius){
          // If it is, update the vehicle's position and send back YES response
          tower->connectedVehicles[id].x = x;
          tower->connectedVehicles[id].y = y;
          response[0] = YES;
          send(clientSocket, response, sizeof(response), 0);
        }else{
          // If it's out of range
          // Update tower connection and send back NO response
          tower->numConnectedVehicles --;
          tower->connectedVehicles[id].connected = 0;
          response[0] = NO;
          send(clientSocket, response, sizeof(response), 0);
        }
        // Exit connection
        break;
      }
    }
    // Close the client Socket
    close(clientSocket);
    // If was told to SHUTDOWN then exit
    if (command[0] == SHUTDOWN){
      break;
    }
  }
  // Close the ServerSocket
  close(serverSocket);
  printf("SERVER: Shutting down cellTower : %d\n", tower->id);

}
