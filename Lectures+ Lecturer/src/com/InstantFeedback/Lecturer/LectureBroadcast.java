package com.InstantFeedback.Lecturer;

import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Jasiek on 18/04/2014.
 */
public class LectureBroadcast {
    private static final String TAG = "LectureBroadcast";
    private int port = -1;
    private ServerSocket serverSocket;

    public int getPort() {
        return port;
    }

    public LectureBroadcast() {
        try {
            serverSocket = new ServerSocket(0);
        } catch (IOException e) {
            Log.e(TAG, "Server socket initialization failed.");
        }
    }
}
