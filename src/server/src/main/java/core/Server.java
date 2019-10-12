package main.java.core;

import main.java.model.Music;
import main.java.model.User;
import main.java.utils.Deserializer;

import java.util.List;

public class Server {
  private static final int PORT = 5000;

  public static Deserializer d;
  public static List<Music> musicList;
  public static List<User> userList;

  public static void main(String args[]) {
    try {
      System.out.println("Initializing Server...");

      System.out.println("~~~");
      System.out.println("Starting Server Communication Protocol");
      ServerCommunicationProtocol scp = new ServerCommunicationProtocol(PORT);
      scp.start();
    } catch (Exception e) {
      System.out.println(e);
    }

  }
}