package main.java.core;

import com.google.gson.JsonObject;

import java.io.FileNotFoundException;
import java.io.IOException;

class MP3Handler {
    final private int FRAGMENTSIZE = 8192;
    public MP3Handler() {
    }

    public String getSongFragment(String songID, String frag) {
        JsonObject ret = new JsonObject();
        MP3Service mp3Service = new MP3Service();
        long i = Long.parseLong(frag);
        int filesize = 0;

        ret.addProperty("currentIndex", i+1);

        try {
            filesize = mp3Service.getFileSize(songID);
            if (FRAGMENTSIZE*(i+1) > filesize)
                ret.addProperty("keepPulling", "false");
            else
                ret.addProperty("keepPulling", "true");

            ret.addProperty("data", mp3Service.getSongChunk(songID, i));
        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        catch (IOException et) {
            et.printStackTrace();
        }
        return ret.toString();
    }
}
