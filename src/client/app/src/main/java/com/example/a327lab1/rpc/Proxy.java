package com.example.a327lab1.rpc;

/**
 * The Proxy implements ProxyInterface class. The class is incomplete
 *
 * @author  Oscar Morales-Ponce
 * @version 0.15
 * @since   2019-01-24
 */

import android.content.Context;
import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStream;


public class Proxy {
    private static final int PORT = 9999;
    private Context context;
    private ClientCommunicationProtocol ccp;
    private static int requestID = 1;

    public Proxy(Context context)
    {
        ccp = new ClientCommunicationProtocol();
        this.context = context;
    }

    /**
     * Executes the  remote method "remoteMethod". The method blocks until
     * it receives the reply of the message.
     */
    public synchronized JsonObject synchExecution(String remoteMethod, String[] param)
    {
        RemoteReference rr = new RemoteReference(context);
        JsonObject jsonRequest = rr.getRemoteReference(remoteMethod);
        JsonObject jsonParam = new JsonObject();

        //Setting method params
        for (int i = 0; i < param.length; i++) {
            jsonParam.addProperty(Integer.toString(i), param[i]);
        }

        jsonRequest.add("param", jsonParam);
        jsonRequest.addProperty("requestID", Integer.toString(requestID));
        ccp.send(jsonRequest);
        requestID++;

        JsonObject ret = ccp.getRet();

        return ret;
    }

    /*
     * Executes the  remote method remoteMethod and returns without waiting
     * for the reply. It does similar to synchExecution but does not
     * return any value
     *
     */
    public void asynchExecution(String remoteMethod, String[] param)
    {
        return;
    }
}


