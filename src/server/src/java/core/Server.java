package java.core;

import java.model.Music;
import java.model.User;
import java.util.Deserializer;
import java.net.*;
import java.io.*;

public class Server {
  private static final int PORT = 5000;

  public static Deserializer d;
  public static Arraylist<Music> musicList;
  public static Arraylist<User> userList;

  public static void main(String args[]) {
    d = new Deserializer();
    musicList = d.getMusicDatabase();
    userList = d.getUserDatabase();

    ServerCommunicationProtocol scp = new ServerCommunicationProtocol(PORT);
    scp.start();
  }
}