package com.example.a327lab1.rpc;

import android.content.Context;
import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStream;

public class RemoteReference {
    private static final String METHOD_REF_FILE = "methods.json";
    private Context context;
    private AssetManager am;

    public RemoteReference(Context context) {
        this.context = context;
        am = context.getAssets();
    }

    public JsonObject getRemoteReference(String remoteMethod) {
        try {
            JsonObject returnJson = new JsonObject();

            String myJson = inputStreamToString(am.open(METHOD_REF_FILE));

            // Get List of Remote Method References from method.json
            JsonObject methodRefJO = new Gson().fromJson(myJson, JsonObject.class);
            JsonArray jArray = methodRefJO.get("methods").getAsJsonArray();

            for (int i = 0; i < jArray.size(); i++) {
                String interested = jArray.get(i).getAsJsonObject().get("remoteMethod").getAsString();
                if (remoteMethod.equals(interested)) {
                    return jArray.get(i).getAsJsonObject();
                }
            }
            return returnJson;

        } catch(IOException e) {
            return null;
        }

    }

    /**
     * Reads a file using inputstream
     *
     * @param inputStream The inputstream to read from
     * @return The string read from the inputstream
     */
    public static String inputStreamToString(InputStream inputStream) {
        try {
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes, 0, bytes.length);
            String json = new String(bytes);
            return json;
        } catch (IOException e) {
            return null;
        }
    }
}
