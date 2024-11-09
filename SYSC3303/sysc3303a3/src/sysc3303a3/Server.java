package sysc3303a3;

import java.io.*;
import java.net.*;

public class Server {

   DatagramPacket sendPacket, receivePacket;
   DatagramSocket sendReceiveSocket;
   byte replyData[];

   public Server()
   {
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

   public void sendAndReceive()
   {
      // Prepare a DatagramPacket and send it via sendReceiveSocket
      // to port 5000 on the destination host.


      // Java stores characters as 16-bit Unicode values, but 
      // DatagramPackets store their messages as byte arrays.
      // Convert the String into bytes according to the platform's 
      // default character encoding, storing the result into a new 
      // byte array.

      if (replyData == null || replyData.length == 0){
         replyData = new byte[0];
      }else{
         byte reply[] = new byte[4];

         if (replyData[1] == 1 || replyData[1] == 2){
            reply[0] = 0;
            reply[1] = 3;
            reply[2] = 0;
            reply[3] = 1;
         }else{
            // Invalid
            reply[0] = 0;
            reply[1] = 4;
            reply[2] = 0;
            reply[3] = 0;
         }

         replyData = reply;
      }

      System.out.println("Server: message as Bytes: " + replyData);
      String ss = new String(replyData,0, replyData.length);   
      System.out.println("Server: message as String: "+ ss);

      System.out.println("Server: sending a packet to Intermediate...\n");

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
         sendPacket = new DatagramPacket(replyData, replyData.length,
                                         InetAddress.getLocalHost(), 69);
      } catch (UnknownHostException e) {
         e.printStackTrace();
         System.exit(1);
      }

      System.out.println("Server: Sending packet:");
      System.out.println("To host: " + sendPacket.getAddress());
      System.out.println("Destination host port: " + sendPacket.getPort());
      int len = sendPacket.getLength();
      System.out.println("Length: " + len);
      System.out.print("Containing: ");
      System.out.println(new String(sendPacket.getData(),0,len)); // or could print "s"

      // Send the datagram packet to the server via the send/receive socket. 

      try {
         sendReceiveSocket.send(sendPacket);
      } catch (IOException e) {
         e.printStackTrace();
         System.exit(1);
      }

      System.out.println("Server: Packet sent to Intermediate.\n");

      // Construct a DatagramPacket for receiving packets up 
      // to 100 bytes long (the length of the byte array).

      byte data[] = new byte[50];
      receivePacket = new DatagramPacket(data, data.length);

      try {
         // Block until a datagram is received via sendReceiveSocket.  
         sendReceiveSocket.receive(receivePacket);
      } catch(IOException e) {
         e.printStackTrace();
         System.exit(1);
      }

      // Process the received datagram.
      System.out.println("Server: Packet received from Intermediate:");
      System.out.println("From host: " + receivePacket.getAddress());
      System.out.println("Host port: " + receivePacket.getPort());
      len = receivePacket.getLength();
      System.out.println("Length: " + len);
      System.out.print("Containing: ");

      // Form a String from the byte array.
      String received = new String(data,0,len); 
      System.out.println("Server: Received reply from Intermediate as String: " + received);  
      System.out.println("Server: Received reply from Intermediate as Bytes: " + data);

      System.out.println("Server: Received reply from Intermediate as Individual Bytes: ");
      for (int i = 0; i < data.length; i++){
         System.out.print(data[i] + ", ");
      }
      System.out.println("\n");

      replyData = new byte[len];

      for (int i = 0; i < len; i++){
         replyData[i] = data[i];
      }

   }

   public void closeSockets(){
      // We're finished, so close the socket.
      sendReceiveSocket.close();
   }

   public static void main(String args[])
   {
      Server s = new Server();
      while (true){
         s.sendAndReceive();

         // Slow things down (wait 5 seconds)
         try {
            Thread.sleep(5000);
         } catch (InterruptedException e ) {
            e.printStackTrace();
            System.exit(1);
         }
      }

   }
}
