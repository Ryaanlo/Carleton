package sysc3303a3;

import java.io.*;
import java.net.*;

public class Intermediate extends Thread{

   DatagramPacket sendPacket, receivePacket;
   DatagramSocket sendSocket, receiveSocket;
   static volatile byte mainData[];
   static volatile byte replyData[];
   int port;

   public Intermediate(int port)
   {
      try {
         // Construct a datagram socket and bind it to any available 
         // port on the local host machine. This socket will be used to
         // send UDP Datagram packets.
         sendSocket = new DatagramSocket();

         // Construct a datagram socket and bind it to port 5000 
         // on the local host machine. This socket will be used to
         // receive UDP Datagram packets.
         receiveSocket = new DatagramSocket(port);
         this.port = port;
         
         // to test socket timeout (2 seconds)
         //receiveSocket.setSoTimeout(2000);
      } catch (SocketException se) {
         se.printStackTrace();
         System.exit(1);
      } 
   }

   public synchronized int receive()
   {
      // If port is 23 - communicating with client
      if (port == 23){
         // If no data yet
         if (mainData == null || mainData.length == 0){
            // Construct a DatagramPacket for receiving packets up 
            // to 100 bytes long (the length of the byte array).

            byte data[] = new byte[100];
            receivePacket = new DatagramPacket(data, data.length);
            System.out.println("Intermediate: Waiting for Packet.\n");

            // Block until a datagram packet is received from receiveSocket.
            try {        
               System.out.println("Waiting...\n"); // so we know we're waiting
               receiveSocket.receive(receivePacket);
            } catch (IOException e) {
               System.out.print("IO Exception: likely:");
               System.out.println("Receive Socket Timed Out.\n" + e);
               e.printStackTrace();
               System.exit(1);
            }

            // Process the received datagram.
            System.out.println("Intermediate: Packet received:");
            System.out.println("From host: " + receivePacket.getAddress());
            System.out.println("Host port: " + receivePacket.getPort());
            int len = receivePacket.getLength();
            System.out.println("Length: " + len);
            System.out.print("Containing: " );

            // Form a String from the byte array.
            String received = new String(data,0,len);   
            System.out.println(received + "\n");

            mainData = new byte[len];

            // Copy over the bytes from the received data
            for (int i = 0; i < len; i ++){
               mainData[i] = data[i];
            }
            
            // Slow things down (wait 5 seconds)
            try {
               Thread.sleep(5000);
            } catch (InterruptedException e ) {
               e.printStackTrace();
               System.exit(1);
            }

            return len;
         
         // If it already got data from client
         }else{
            return 0;
         }

      // port = 69
      }else{
         // If no data yet
         if (replyData == null || replyData.length == 0){
            // Construct a DatagramPacket for receiving packets up 
            // to 100 bytes long (the length of the byte array).

            byte data[] = new byte[100];
            receivePacket = new DatagramPacket(data, data.length);
            System.out.println("Intermediate: Waiting for Packet.\n");

            // Block until a datagram packet is received from receiveSocket.
            try {        
               System.out.println("Waiting...\n"); // so we know we're waiting
               receiveSocket.receive(receivePacket);
            } catch (IOException e) {
               System.out.print("IO Exception: likely:");
               System.out.println("Receive Socket Timed Out.\n" + e);
               e.printStackTrace();
               System.exit(1);
            }

            // Process the received datagram.
            System.out.println("Intermediate: Packet received:");
            System.out.println("From host: " + receivePacket.getAddress());
            System.out.println("Host port: " + receivePacket.getPort());
            int len = receivePacket.getLength();
            System.out.println("Length: " + len);
            System.out.print("Containing: " );

            // Form a String from the byte array.
            String received = new String(data,0,len);   
            System.out.println(received + "\n");

            replyData = new byte[len];

            // Copy over the bytes from the received data
            for (int i = 0; i < len; i ++){
               replyData[i] = data[i];
            }
            
            // Slow things down (wait 5 seconds)
            try {
               Thread.sleep(5000);
            } catch (InterruptedException e ) {
               e.printStackTrace();
               System.exit(1);
            }

            return len;
         
         // If it already got data from client
         }else{
            return 0;
         } 
      }

   }

   public synchronized void send(int len){
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

      // Check if it's a read or write request or if it's invalid

      if (port == 23){
         while (replyData == null || replyData.length == 0){
            // Slow things down (wait 5 seconds)
            try {
               Thread.sleep(1000);
            } catch (InterruptedException e ) {
               e.printStackTrace();
               System.exit(1);
            }            
         }
         sendPacket = new DatagramPacket(replyData, replyData.length,
                                 receivePacket.getAddress(), receivePacket.getPort());

         System.out.println( "Intermediate: Sending reply:");
         System.out.println("To host: " + sendPacket.getAddress());
         System.out.println("Destination host port: " + sendPacket.getPort());
         len = sendPacket.getLength();
         System.out.println("Length: " + len);
         System.out.print("Containing: ");
         for (int i = 0; i < replyData.length; i++){
            System.out.print(replyData[i] + ", ");
         }
         System.out.print("\n");
         
         // Send the datagram packet to the client via the send socket. 
         try {
            sendSocket.send(sendPacket);
         } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
         }

         System.out.println("Intermediate: reply sent");

         // Reset the main data
         mainData = null;
      }else{
         while (mainData == null || mainData.length == 0){
            // Slow things down (wait 5 seconds)
            try {
               Thread.sleep(1000);
            } catch (InterruptedException e ) {
               e.printStackTrace();
               System.exit(1);
            }            
         }
         sendPacket = new DatagramPacket(mainData, mainData.length,
         receivePacket.getAddress(), receivePacket.getPort());

         System.out.println( "Intermediate: Sending reply:");
         System.out.println("To host: " + sendPacket.getAddress());
         System.out.println("Destination host port: " + sendPacket.getPort());
         len = sendPacket.getLength();
         System.out.println("Length: " + len);
         System.out.print("Containing: ");
         for (int i = 0; i < mainData.length; i++){
         System.out.print(mainData[i] + ", ");
         }
         System.out.print("\n");

         // Send the datagram packet to the client via the send socket. 
         try {
               sendSocket.send(sendPacket);
            } catch (IOException e) {
               e.printStackTrace();
               System.exit(1);
         }

         System.out.println("Intermediate: reply sent");

         // Reset the replydata
         replyData = null;
      }
   }

   public void closeSockets(){
      sendSocket.close();
      receiveSocket.close();
   }

   public void run(){
      while (true){
         int len = receive();
         send(len);
      }
   }

   public static void main( String args[] )
   {
      Thread h1 = new Intermediate(23);
      h1.start();

      Thread h2 = new Intermediate(69);
      h2.start();
   }
}
