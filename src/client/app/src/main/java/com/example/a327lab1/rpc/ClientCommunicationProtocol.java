package com.example.a327lab1.rpc;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;

public class ClientCommunicationProtocol {
    private static final int PORT = 5000;
    private static final int TIMEOUT_DURATION = 5000; // 5 seconds

    private DatagramSocket clientSocket;
    private InetAddress ipAddress;
    private JsonObject ret;

    public ClientCommunicationProtocol() {
        try {
            clientSocket = new DatagramSocket();
            clientSocket.setSoTimeout(TIMEOUT_DURATION);
            ipAddress = getLocalHost();

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void send(JsonObject request) {
        try {
            String message = request.toString();
            String callSemantic = ((request).get("call semantics").getAsString());

            DatagramPacket outPacket = new DatagramPacket(message.getBytes(), message.length(), ipAddress, PORT);

            if (callSemantic.equals("at-least-one")) {
                String reply = "invalid";
                while (reply.equals("invalid")) {
                    //JSONObject response;
                    clientSocket.send(outPacket);
                    ret = null;
                    try {
                        ret = receive();
                        if (ret != null) {
                            reply = "valid";
                        }
                    } catch (SocketTimeoutException e) {
                        Log.d("RETCM", "Timeout achieved");
                        clientSocket.close();
                    }
                }
            } else if (callSemantic.equals("at-most-one")) {
                String requestSent = "invalid";
                while (requestSent.equals("invalid")) {
                    clientSocket.send(outPacket);
                    requestSent = "valid";

                    ret = null;

                    try {
                        ret = receive();
                        if (ret == null) {
                            requestSent = "invalid";
                        }
                    } catch (SocketTimeoutException e) {
                        Log.d("RETCM", "Timeout achieved");
                        clientSocket.close();
                    }
                }
            } else if (callSemantic.equals("maybe")) {
                clientSocket.send(outPacket);
                ret = receive();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JsonObject receive() throws SocketTimeoutException {
        JsonObject response = null;
        try {
            byte[] buffer = new byte[65000];
            DatagramPacket inPacket = new DatagramPacket(buffer, buffer.length);
            clientSocket.receive(inPacket);
            response = new Gson().fromJson(new String(inPacket.getData(), 0, inPacket.getLength()), JsonObject.class);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;

    }

    public JsonObject getRet() {
        return ret;
    }

    public static InetAddress getLocalHost() {
        AsyncTask<String, Void, InetAddress> task = new AsyncTask<String, Void, InetAddress>() {
            @Override
            protected InetAddress doInBackground(String... params) {
                try {
                    return InetAddress.getByName("localhost");
                } catch (UnknownHostException e) {
                    return null;
                }
            }
        };
        try {
            return task.execute().get();
        } catch (InterruptedException e) {
            return null;
        } catch (ExecutionException e) {
            return null;
        }
    }

//    public static class SendRunnable implements Runnable {
//        private DatagramSocket clientSocket;
//        private DatagramPacket outPacket;
//        private InetAddress ipAddress;
//        private JsonObject ret;
//
//        private String message;
//        private String callSemantic;
//
//
//        public SendRunnable(String message, String callSemantic) {
//            this.message = message;
//            this.callSemantic = callSemantic;
//        }
//
//        public void run() {
//
//        }
//    }

}
