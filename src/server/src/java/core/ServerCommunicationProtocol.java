package java.core;

import java.io.*;
import java.net.*;

public class ServerCommunicationProtocol extends Thread {

  static final int FRAGMENT_SIZE = 8192; // Packet size
  byte[] packetSize = new byte[FRAGMENT_SIZE];

  DatagramSocket serverSocket;
  DataInputStream inputStream;
  DataOutputStream mp3Out;
  Dispatcher myDispatcher;
  int portNumber;

  public ServerCommunicationProtocol(int num) {
    this.portNumber = num;
  }

  private void connect() { // portNumber must be > 1023
    try {
      serverSocket = new DatagramSocket(this.portNumber); // Initialize socket
      System.out.println("ServerSocket opened on port: " + this.portNumber);
    } catch (IOException e) {
      System.out.println(e);
    }
  }

  private void listen() { // Opens client socket and listens for requests
    System.out.println("Server listening.");
    try {
      while (true) {
        DatagramPacket requestPacket = new DatagramPacket(packetSize, packetSize.length); // Initialize request packet
        serverSocket.receive(requestPacket); // Receive request packet
        System.out.println("Client packet received: " + requestPacket);

        System.out.println("Creating new thread for handling this client packet."); // Create new thread to handle this
                                                                                    // request packet and return a
                                                                                    // response packet
        Thread t = new ClientRequestPacketHandler(serverSocket, requestPacket);
        t.start();
      }
    } catch (IOException e) {
      System.out.println(e);
    }
  }

  @Override
  // Used to run Server and clients concurrently
  public void run() {
    connect();
    listen();
  }

}