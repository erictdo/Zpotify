package server.core;

import com.google.gson.JsonObject;
import server.model.User;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

/**
 * This class represents a dispatcher for Login information
 */
public class LoginDispatcher extends Dispatcher {

  /**
   * Fragment size for Login information
   */
  private static final int FRAGMENT_SIZE = 8192;

  /**
   * Return login token if authorized
   *
   * @param username the username from client
   * @param password the password from client
   */
  public String login(String username, String password) throws IOException {
    User user = Server.usersInfo.get(username + password);

    if (user != null) {
      // User is valid, give token
      Server.currentSessions.add(user);
      System.out.println(Server.usersInfo);

      JsonObject loginToken = new JsonObject();
      loginToken.addProperty("loginToken", Integer.toString(Server.currentSessions.indexOf(user)));

      return bytetize(loginToken.toString());
    }

    return null;
  }

  /**
   * Converts the supplied String to bytes, creates an InputStream from those
   * bytes to read the information, and decodes the byte array back to a String.
   *
   * @param str String to be "bytetized"
   * @return Original String after being "bytetized"
   */
  // TODO: Move to serializer or something similar (...also give it a better
  // name.)
  private String bytetize(String str) {
    byte[] strBytes = str.toString().getBytes();
    byte[] buf = new byte[FRAGMENT_SIZE];
    InputStream is = new ByteArrayInputStream(strBytes);

    try {
      is.read(buf);
      is.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return Base64.getEncoder().encodeToString(buf);
  }
}