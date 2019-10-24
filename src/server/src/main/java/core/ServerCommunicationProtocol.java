package main.java.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.*;
import java.net.*;
import java.util.HashMap;

public class ServerCommunicationProtocol extends Thread {
    private static final int PORT = 9999;
    private static final int FRAGMENT_SIZE = 8192;
    private byte[] buffer = new byte[FRAGMENT_SIZE];

    private DatagramSocket serverSocket;
    private DatagramPacket inPacket;
    private Dispatcher dispatcher;

    private static HashMap<Integer,String> sendTracker;

    /**
     * Constructor for the ServerCommunicationProtocol
     */
    public ServerCommunicationProtocol() { }

    /**
     * Communication Initialization before listening for requests
     */
    private void connect() {
        try {
            //Connecting to port
            serverSocket = new DatagramSocket(PORT);
            System.out.println("Socket opened on port: " + PORT);

            //Register Objects & Methods on Dispatcher
            dispatcher = new Dispatcher();
            dispatcher.registerObject(new UserService(), "UserService");
            dispatcher.registerObject(new MusicService(), "MusicService");
            dispatcher.registerObject(new MP3Service(), "MP3Service");
            System.out.println("Initialized Dispatcher");

            //Initialize sendTracker
            sendTracker = new HashMap<Integer,String>();

        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * Listening for a request from client
     */
    private void listen() {
        try {
            while (true) {
                System.out.println("Server listening...");

                //Receiving Request Packet
                buffer = new byte[256];
                inPacket = new DatagramPacket(buffer, buffer.length);
                serverSocket.receive(inPacket);
                System.out.println("Request packet received: " + inPacket);

                //Handling Request on a thread
                CommunicationRunnable runnable = new CommunicationRunnable(buffer, serverSocket, inPacket, dispatcher);
                Thread t = new Thread(runnable);
                t.start();
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        serverSocket.close();
        System.out.println("Server is no longer listening.");
    }

    /**
     * Override Thread's run method to run connect() and listen()
     */
    @Override
    public void run() {
        connect();
        listen();
    }

    public static class CommunicationRunnable implements Runnable {
        private byte[] buffer;
        private DatagramSocket serverSocket;
        private DatagramPacket inPacket;
        private Dispatcher dispatcher;
        private String inMessage;
        private InetAddress clientAddress;
        private int port;

        /**
         * Constructor of CommunicationRunnable
         *
         * @param buffer   The buffer being read
         * @param inPacket The DatagramPacket being received
         */
        public CommunicationRunnable(byte[] buffer, DatagramSocket serverSocket, DatagramPacket inPacket, Dispatcher dispatcher) {
            this.buffer = buffer;
            this.serverSocket = serverSocket;
            this.inPacket = inPacket;
            this.dispatcher = dispatcher;
            this.clientAddress = inPacket.getAddress();
            this.port = inPacket.getPort();
            this.inMessage = new String(inPacket.getData(), 0, inPacket.getLength());
        }

        public void run() {
            try {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();

                // Incoming JSON message from client
                JsonObject inJsonMessage = gson.fromJson(inMessage, JsonObject.class);

                // Get the call semantic of the "Method" Object
                String callSemantic = inJsonMessage.get("call semantics").getAsString();

                // Call dispatcher to return an outMessage
                String outMessage;

                // Send response to client
                if (callSemantic.equals("at-most-one")) {
                    int requestID = inJsonMessage.get("requestID").getAsInt();
                    if (sendTracker.containsKey(requestID)) {
                        outMessage = sendTracker.get(requestID);
                        DatagramPacket outPacket = new DatagramPacket(outMessage.getBytes() , outMessage.getBytes().length, clientAddress, port);
                        serverSocket.send(outPacket);
                    } else {
                        outMessage = dispatcher.dispatch(inJsonMessage.toString());
                        sendTracker.put(requestID, outMessage);
                        DatagramPacket outPacket = new DatagramPacket(outMessage.getBytes(), outMessage.getBytes().length, clientAddress, port);
                        serverSocket.send(outPacket);
                    }
                } else {
                    outMessage = dispatcher.dispatch(inJsonMessage.toString());
                    DatagramPacket outPacket = new DatagramPacket(outMessage.getBytes(), outMessage.getBytes().length, clientAddress, port);
                    serverSocket.send(outPacket);
                }

                System.out.println(Thread.getAllStackTraces().keySet());
                System.out.println("Sending response to client: " + clientAddress + "\n");

            } catch (Exception e) {
                System.out.println(e);
                System.out.println("Error occurred while handling client's request. See stack trace above.");
            }
        }


    }

}