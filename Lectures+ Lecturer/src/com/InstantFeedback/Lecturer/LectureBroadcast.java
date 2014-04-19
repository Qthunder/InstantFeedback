package com.InstantFeedback.Lecturer;

import android.util.Log;
import com.InstantFeedback.Library.Question;
import com.InstantFeedback.Library.Request;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static com.InstantFeedback.Library.Variables.DataType;

/**
 * Created by Jasiek on 18/04/2014.
 */
public class LectureBroadcast {
    private static final String TAG = "LectureBroadcast";
    private final Thread serverThread;
    private int port = -1;
    private ServerSocket serverSocket;
    private List<Socket> attenders = new ArrayList<Socket>();

    public int getPort() {
        return port;
    }

    public LectureBroadcast() {
        serverThread = new Thread(new ServerThread());
        serverThread.run();
    }

    public void endBroadcast() {
        //Final questions, etc...
    }


    public void deployQuestion(Question question) {
        for(int i = 0; i < attenders.size(); ++i) sendQuestion(attenders.get(i), question);

        //TODO - start collection question responses
    }

    private void sendQuestion(Socket socket, Question question) {
        try {
            if(socket == null) {
                Log.d(TAG, "Socket is null");
            } else if (socket.getOutputStream() == null) {
                    Log.d(TAG, "Socket output stream is null.");
            }

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(DataType.QUESTION);
            out.writeObject(question);

            Log.d(TAG, "Question sent to socket" + socket.getInetAddress() + " " + socket.getPort());

        } catch (IOException e) {
            Log.e(TAG, "Sending question failed, IOE:", e);
        }
    }

    private class ServerThread implements Runnable {

        private List<Thread> attenderThreads = new ArrayList<Thread>();

        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(0);
                Log.d(TAG, "Server created, awaiting connections");

                while(!Thread.currentThread().isInterrupted()) {
                    Socket socket = serverSocket.accept();
                    attenders.add(socket);
                    Log.d(TAG, "Student connected.");
                    Thread thread = new Thread(new AttenderListenerThread(socket));
                    thread.run();
                    attenderThreads.add(thread);

                    //TODO notify app of student joining
                }

            } catch (IOException e) {
                Log.e(TAG, "Server socket initialization failed.");
            }


        }
    }

    private class AttenderListenerThread implements Runnable {
        private Socket attenderSocket;

        public AttenderListenerThread(Socket socket) {
            attenderSocket = socket;
        }

        @Override
        public void run() {
            ObjectInputStream input;

            try {
                input = new ObjectInputStream(attenderSocket.getInputStream());
                while(!Thread.currentThread().isInterrupted()) {
                    DataType dataType = (DataType) input.readObject();

                    switch(dataType) {
                        case REQUEST:
                            Request request = (Request) input.readObject();
                            //TODO - inform rest of the app about request.
                            break;
                        case ANSWER:
                            //TODO - handle the answer
                        default:
                            Log.d(TAG, "Wrong DataType sent");
                    }
                }
            } catch (ClassNotFoundException e) {
                Log.e(TAG, "Attender Listener thread failed.", e);
            } catch (OptionalDataException e) {
                Log.e(TAG, "Attender Listener thread failed.", e);
            } catch (StreamCorruptedException e) {
                Log.e(TAG, "Attender Listener thread failed, stream corrupted.", e);
            } catch (IOException e) {
                Log.e(TAG, "Attender Listener thread failed, IOE.", e);
            }
        }
    }
}
