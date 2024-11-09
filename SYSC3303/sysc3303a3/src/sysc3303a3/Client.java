package sysc3303a3;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Random;

public class Client {

   DatagramPacket sendPacket, receivePacket;
   DatagramSocket sendReceiveSocket;

   public Client()
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

   public int send(byte request, String fileName, String mode)
   {
      // Data that is going to be sent
      byte msgByte[];

      // Prepare a DatagramPacket and send it via sendReceiveSocket
      // to port 5000 on the destination host.
 
      // If request is 0, we are asking Intermediate for data reply back
      if (request == 0) {
         msgByte = new byte[0];
      // If we are sending data to the Intermediate
      }else{
         ArrayList<Byte> message = new ArrayList<Byte>();
         byte zero = 0;
         byte nameBytes[] = new String(fileName).getBytes();
         byte modeBytes[] = new String(mode).getBytes();
   
         // Add first zero byte to the send packet 
         message.add(zero);
         // Add the read/write request byte
         message.add(request);
   
         // Add the file name to the packet
         for (int i = 0; i < nameBytes.length; i++){
            message.add(nameBytes[i]);
         }
   
         // Add zero byte to the send packet 
         message.add(zero);
   
         // Add the mode to the packet
         for (int i = 0; i < modeBytes.length; i++){
            message.add(modeBytes[i]);
         }
   
         // Add zero byte to the send packet 
         message.add(zero);
   
         // Java stores characters as 16-bit Unicode values, but 
         // DatagramPackets store their messages as byte arrays.
         // Convert the String into bytes according to the platform's 
         // default character encoding, storing the result into a new 
         // byte array.
   
         msgByte = new byte[message.size()];
         for (int i = 0; i < message.size(); i++){
            msgByte[i] = message.get(i).byteValue();
         }
      }

      // If we are asking intermediate for reply
      if (msgByte.length == 0){
         System.out.println("Client: asking Intermediate for reply...\n");
      // If we are sending data to intermediate
      }else{
         System.out.println("Client: message as Bytes: " + msgByte);
         String ss = new String(msgByte,0, msgByte.length);   
         System.out.println("Client: message as String: "+ ss);

         System.out.println("Client: sending a packet to Intermediate...\n");
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
         sendPacket = new DatagramPacket(msgByte, msgByte.length,
                                         InetAddress.getLocalHost(), 23);
      } catch (UnknownHostException e) {
         e.printStackTrace();
         System.exit(1);
      }

      System.out.println("Client: Sending packet:");
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

      System.out.println("Client: Packet sent to Intermediate.\n");

      return len;
   }

   public void receive(int len){
      // Construct a DatagramPacket for receiving packets up 
      // to 100 bytes long (the length of the byte array).

      byte data[] = new byte[4];
      receivePacket = new DatagramPacket(data, data.length);

      try {
         // Block until a datagram is received via sendReceiveSocket.  
         sendReceiveSocket.receive(receivePacket);
      } catch(IOException e) {
         e.printStackTrace();
         System.exit(1);
      }

      // Process the received datagram.
      System.out.println("Client: Packet received from Intermediate:");
      System.out.println("From host: " + receivePacket.getAddress());
      System.out.println("Host port: " + receivePacket.getPort());
      len = receivePacket.getLength();
      System.out.println("Length: " + len);
      System.out.print("Containing: ");

      // Form a String from the byte array.
      String received = new String(data,0,len); 
      System.out.println("Client: Received packet from Intermediate as String: " + received);  
      System.out.println("Client: Received packet from Intermediate as Bytes: " + data);

      System.out.println("Client: Received packet from Intermediate as Individual Bytes: ");
      for (int i = 0; i < data.length; i++){
         System.out.print(data[i] + ", ");
      }
      System.out.println("\n");

   }

   public void closeSockets(){
      // We're finished, so close the socket.
      sendReceiveSocket.close();
   }

   public static void main(String args[])
   {
      String modes[] = {"netascii", "octet", "mail"}; 
      String filename = "test.txt";
      int len = 0;

      Random rand = new Random();

      for (int i = 0; i < 11; i++){
         Client c = new Client();

         if (i == 10){
            // Send invalid request with byte 3
            len = c.send((byte) 3, filename, modes[rand.nextInt(3)]);
            c.receive(len);

            // Send request to Intermediate for reply
            len = c.send((byte) 0, filename, modes[rand.nextInt(3)]);
            c.receive(len);
         }else{
            // (i % 2) + 1 alternates between 1 and 2
            len = c.send((byte) ((i % 2) + 1), filename, modes[rand.nextInt(3)]);
            c.receive(len);
            
            // Send request to Intermediate for reply
            len = c.send((byte) 0, filename, modes[rand.nextInt(3)]);
            c.receive(len);            
         }
         
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
