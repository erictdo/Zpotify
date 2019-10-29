package main.java.core;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.FileInputStream;
import java.io.File;
import java.util.Base64;
import java.io.FileNotFoundException;

public class MP3Service {
  static final int FRAGMENT_SIZE = 8192;

  public MP3Service() {

  }

  public String getSongChunk(String key, Long fragment) throws FileNotFoundException, IOException {
    byte buf[] = new byte[FRAGMENT_SIZE];

    File file = new File(key);
    FileInputStream inputStream = new FileInputStream(file);
    inputStream.skip(fragment * FRAGMENT_SIZE);
    inputStream.read(buf);
    inputStream.close();
    // Encode in base64 so it can be transmitted
    return Base64.getEncoder().encodeToString(buf);
  }

  public Integer getFileSize(String key) throws FileNotFoundException, IOException {
    File file = new File(key);
    Integer total = (int) file.length();

    return total;
  }

}
