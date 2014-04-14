package com.example.Lectures__Student;

import java.net.InetAddress;

/**
 * Created by Jasiek on 15/04/2014.
 */
public class LectureAttender {
    private final InetAddress serverAddress;
    private final int serverPort;
    private final Thread attenderThread;

    public LectureAttender(InetAddress address, int port) {
        serverAddress = address;
        serverPort = port;

        attenderThread = new Thread(new AttenderThread());
        attenderThread.run();

    }

    private class AttenderThread implements Runnable {
        @Override
        public void run() {
            //TODO
        }
    }
}
