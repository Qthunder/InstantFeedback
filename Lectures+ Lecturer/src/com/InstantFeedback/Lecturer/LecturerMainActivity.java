package com.InstantFeedback.Lecturer;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.InstantFeedback.Library.Question;
import com.InstantFeedback.Library.Request;

public class LecturerMainActivity extends Activity {
    private static final String TAG = "Lecturer";
    private NsdHelperLecturer nsdHelperLecturer;
    private LectureBroadcast lectureBroadcast;
    private Handler updateHandler;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        nsdHelperLecturer = new NsdHelperLecturer(getApplicationContext());

        updateHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Bundle receivedBundle = msg.getData();
                String type = receivedBundle.getString("type");
                if (type == "Request") {
                    Request request = new Request(receivedBundle.getString("request"));
                    //TODO handle request
                }
                if (type == "Answer") {
                    //TODO handle answer
                }
            }
        };

        lectureBroadcast = new LectureBroadcast(updateHandler);
    }

    void startLecture() {
        if (lectureBroadcast.getPort() > -1) {
            nsdHelperLecturer.registerService(lectureBroadcast.getPort());
        } else {
            Log.d(TAG, "ServerSocket isn't bound.");
        }

    }

    void endLecture() {
        lectureBroadcast.endBroadcast();
        nsdHelperLecturer.endService();
    }

    public void deployQuestion(Question question) {
        lectureBroadcast.deployQuestion(question);
    }
}
