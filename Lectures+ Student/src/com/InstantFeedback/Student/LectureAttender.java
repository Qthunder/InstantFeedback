package com.InstantFeedback.Student;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.InstantFeedback.Library.Answer;
import com.InstantFeedback.Library.Variables.DataType;
import com.InstantFeedback.Library.Question;
import com.InstantFeedback.Library.Request;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.BlockingDeque;

/**
 * Created by Jasiek on 15/04/2014.
 */
public class LectureAttender {
    private static final String TAG = "Lecture Attender";
    private final InetAddress serverAddress;
    private final int serverPort;
    private final Handler updateHandler;
    private Thread requestsThread;
    private Thread questionsThread;
    private Socket clientSocket;

    public LectureAttender(InetAddress address, int port, Handler handler) {
        updateHandler = handler;
        serverAddress = address;
        serverPort = port;

        requestsThread = new Thread(new RequestsThread());
        requestsThread.run();

    }

    private class RequestsThread implements Runnable {
        private BlockingDeque<Request> requestQueue;
        private final int QUEUE_CAPACITY = 10;

        @Override
        public void run() {
            try {
                if (clientSocket == null) {
                    setSocket(new Socket(serverAddress, serverPort));
                    Log.d(TAG, "Socket Initialized");
                } else Log.d(TAG, "Socket already set, skipping");


            } catch (IOException e) {
                Log.e(TAG, "Lecture Attender thread failed, IOE", e);
            }

            questionsThread = new Thread(new QuestionsThread());
            questionsThread.start();

            while(true) {
                try {
                    Request request = requestQueue.take();
                    sendRequest(request);

                } catch (InterruptedException e) {
                    Log.d(TAG, "Message sending loop interrupted, exiting");
                }
            }

        }

        public void setSocket(Socket socket) {
            if (clientSocket != null && clientSocket.isConnected()) {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    Log.e(TAG, "Closing socket failed", e);
                }
            }

            clientSocket = socket;

        }
    }

    private void sendRequest(Request request) {
        try {
            Socket socket = clientSocket;
            if (socket == null) {
                Log.d(TAG, "Socket is null");
            } else if (socket.getOutputStream() == null) {
                Log.d(TAG, "Socket output stream is null");
            }

            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.writeObject(DataType.REQUEST);
            out.writeObject(request);

            Log.d(TAG, "Student request sent");

        } catch (IOException e) {
            Log.e(TAG, "Request sending failed, IOE", e);
        }

    }

    private class QuestionsThread implements Runnable {
        @Override
        public void run() {
            ObjectInputStream input;

            try {
                input = new ObjectInputStream(clientSocket.getInputStream());
                while(!Thread.currentThread().isInterrupted()) {
                    DataType dataType = (DataType) input.readObject();
                    if(dataType == DataType.QUESTION) {
                        Question question = (Question) input.readObject();

                        Bundle messageBundle = new Bundle();
                        messageBundle.putString("type", "question");
                        messageBundle.putString("question", question.getQuestion()); //TODO send rest of question when it gets implemented
                        Message message = Message.obtain();
                        message.setData(messageBundle);
                        updateHandler.sendMessage(message);
                    }
                }

            } catch (IOException e) {
                Log.e(TAG, "Question listener loop error: ", e);
            } catch (ClassNotFoundException e) {
                Log.e(TAG, "Question listener loop failed, class not found: ", e);
            }
        }
    }

    public void answerQuestion(Answer answer) {
        try {
            Socket socket = clientSocket;
            if (socket == null) {
                Log.d(TAG, "Socket is null");
            } else if (socket.getOutputStream() == null) {
                Log.d(TAG, "Socket output stream is null");
            }

            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.writeObject(DataType.ANSWER);
            out.writeObject(answer);

            Log.d(TAG, "Student answer sent");

        } catch (IOException e) {
            Log.e(TAG, "Answer sending failed, IOE", e);
        }

    }

}
