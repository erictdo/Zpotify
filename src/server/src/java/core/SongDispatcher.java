package java.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.FileReader;
import java.io.FileInputStream;
import java.io.File;
import java.util.Base64;
import java.io.FileNotFoundException;

public class SongDispatcher {
  static final int FRAGMENT_SIZE = 8192;

  public SongDispatcher() {

  }

  public String getSongChunk(Long key, Long fragment) throws FileNotFoundException, IOException {
    byte buf[] = new byte[FRAGMENT_SIZE];

    File file = new File("./" + key);
    FileInputStream inputStream = new FileInputStream(file);
    inputStream.skip(fragment * FRAGMENT_SIZE);
    inputStream.read(buf);
    inputStream.close();
    // Encode in base64 so it can be transmitted
    return Base64.getEncoder().encodeToString(buf);
  }

  public Integer getFileSize(Long key) throws FileNotFoundException, IOException {
    File file = new File("./" + key);
    Integer total = (int) file.length();

    return total;
  }

}
