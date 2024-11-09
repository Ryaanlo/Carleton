package sysc3303a2;

import java.io.*;
import java.net.*;

public class Host {

   DatagramPacket sendPacketClient, receivePacketClient;
   DatagramPacket sendPacketServer, receivePacketServer;
   DatagramSocket sendSocket, receiveSocket;
   DatagramSocket sendReceiveSocket;

   public Host()
   {
      // Receiving Data from the Client
      try {
         // Construct a datagram socket and bind it to any available 
         // port on the local host machine. This socket will be used to
         // send UDP Datagram packets.
         sendSocket = new DatagramSocket();

         // Construct a datagram socket and bind it to port 5000 
         // on the local host machine. This socket will be used to
         // receive UDP Datagram packets.
         receiveSocket = new DatagramSocket(23);
         
         // to test socket timeout (2 seconds)
         //receiveSocket.setSoTimeout(2000);
      } catch (SocketException se) {
         se.printStackTrace();
         System.exit(1);
      } 

      // Sending/Receiving Data from the Server
      try {
        // Construct a datagram socket and bind it to any available 
        // port on the local host machine. This socket will be used to
        // send and receive UDP Datagram packets.
        sendReceiveSocket = new DatagramSocket();
     } catch (SocketException se) {   // Can't create the socket.
        se.printStackTrace();
        System.exit(1);
     }
   }

   public void receiveAndEcho()
   {
      // Construct a DatagramPacket for receiving packets up 
      // to 100 bytes long (the length of the byte array).

      byte dataClient[] = new byte[100];
      receivePacketClient = new DatagramPacket(dataClient, dataClient.length);
      System.out.println("Host: Waiting for Packet.\n");

      // Block until a datagram packet is received from receiveSocket.
      try {        
         System.out.println("Waiting for Request..."); // so we know we're waiting
         receiveSocket.receive(receivePacketClient);
      } catch (IOException e) {
         System.out.print("IO Exception: likely:");
         System.out.println("Receive Socket Timed Out.\n" + e);
         e.printStackTrace();
         System.exit(1);
      }

      // Process the received datagram.
      System.out.println("Host: Packet received:");
      System.out.println("From host: " + receivePacketClient.getAddress());
      System.out.println("Host port: " + receivePacketClient.getPort());
      int len = receivePacketClient.getLength();
      System.out.println("Length: " + len);
      System.out.print("Containing: " );

      // Form a String from the byte array.
      String receivedClient = new String(dataClient,0,len);   
      System.out.println(receivedClient + "\n");
      System.out.println("Packet from Client as Bytes: " + dataClient);
      
      // Slow things down (wait 5 seconds)
      try {
          Thread.sleep(5000);
      } catch (InterruptedException e ) {
          e.printStackTrace();
          System.exit(1);
      }

      // Construct a datagram packet that is to be sent to a specified port 
      // on a specified host.
      // The arguments are:
      //  msg - the message contained in the packet (the byte array)
      //  msg.length - the length of the byte array
      //  InetAddress.getLocalHost() - the Internet address of the 
      //     destination host.
      //     In this example, we want the destination to be the same as
      //     the source (i.e., we want to run the client and server on the
      //     same computer). InetAddress.getLocalHost() returns the Internet
      //     address of the local host.
      //  5000 - the destination port number on the destination host.

      try {
        sendPacketServer = new DatagramPacket(dataClient, dataClient.length,
                                        InetAddress.getLocalHost(), 69);
     } catch (UnknownHostException e) {
        e.printStackTrace();
        System.exit(1);
     }

     System.out.println("Host: Sending packet:");
     System.out.println("To host: " + sendPacketServer.getAddress());
     System.out.println("Destination host port: " + sendPacketServer.getPort());
     //int len = sendPacket.getLength();
     System.out.println("Length: " + len);
     System.out.print("Containing: ");
     System.out.println(new String(sendPacketServer.getData(),0,len)); // or could print "s"

    // Send the datagram packet to the server via the send/receive socket. 
    // Send to server
      try {
        sendReceiveSocket.send(sendPacketServer);
     } catch (IOException e) {
        e.printStackTrace();
        System.exit(1);
     }

     System.out.println("Host: Packet sent to Server.\n");

      // Construct a DatagramPacket for receiving packets up 
      // to 100 bytes long (the length of the byte array).

      byte dataServer[] = new byte[100];
      receivePacketServer = new DatagramPacket(dataServer, dataServer.length);

      try {
         // Block until a datagram is received via sendReceiveSocket.  
         sendReceiveSocket.receive(receivePacketServer);
      } catch(IOException e) {
         e.printStackTrace();
         System.exit(1);
      }

      // Process the received datagram.
      System.out.println("Host: Packet received from Server:");
      System.out.println("From host: " + receivePacketServer.getAddress());
      System.out.println("Host port: " + receivePacketServer.getPort());
      len = receivePacketServer.getLength();
      System.out.println("Length: " + len);
      System.out.print("Containing: ");

      // Form a String from the byte array.
      String receivedServer = new String(dataServer,0,len);   
      System.out.println(receivedServer);

      // Create a new datagram packet containing the string received from the client.

      // Construct a datagram packet that is to be sent to a specified port 
      // on a specified host.
      // The arguments are:
      //  data - the packet data (a byte array). This is the packet data
      //         that was received from the client.
      //  receivePacket.getLength() - the length of the packet data.
      //    Since we are echoing the received packet, this is the length 
      //    of the received packet's data. 
      //    This value is <= data.length (the length of the byte array).
      //  receivePacket.getAddress() - the Internet address of the 
      //     destination host. Since we want to send a packet back to the 
      //     client, we extract the address of the machine where the
      //     client is running from the datagram that was sent to us by 
      //     the client.
      //  receivePacket.getPort() - the destination port number on the 
      //     destination host where the client is running. The client
      //     sends and receives datagrams through the same socket/port,
      //     so we extract the port that the client used to send us the
      //     datagram, and use that as the destination port for the echoed
      //     packet.

      sendPacketClient = new DatagramPacket(dataServer, receivePacketClient.getLength(),
                               receivePacketClient.getAddress(), receivePacketClient.getPort());

      System.out.println( "Host: Sending packet:");
      System.out.println("To host: " + sendPacketClient.getAddress());
      System.out.println("Destination host port: " + sendPacketClient.getPort());
      len = sendPacketClient.getLength();
      System.out.println("Length: " + len);
      System.out.print("Containing: ");
      System.out.println(new String(sendPacketClient.getData(),0,len));
      // or (as we should be sending back the same thing)
      System.out.println(receivedServer); 

     // Send the datagram packet to the server via the send/receive socket. 

     try {
        sendReceiveSocket.send(sendPacketClient);
     } catch (IOException e) {
        e.printStackTrace();
        System.exit(1);
     }

     System.out.println("Host: Packet sent to Client.\n");

      // We're finished, so close the socket.
      sendReceiveSocket.close();

      // We're finished, so close the sockets.
      sendSocket.close();
      receiveSocket.close();
   }

   public static void main( String args[] )
   {
      while (true){
         Host h = new Host();
         h.receiveAndEcho();
      }
   }
}