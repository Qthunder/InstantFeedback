package com.InstantFeedback.Lecturer;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class LecturerMainActivity extends Activity {
    private static final String TAG = "Lecturer";
    private NsdHelperLecturer nsdHelperLecturer;
    private LectureBroadcast lectureBroadcast;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        nsdHelperLecturer = new NsdHelperLecturer(getApplicationContext());
    }

    void startLecture() {
        if (lectureBroadcast.getPort() > -1) {
            nsdHelperLecturer.registerService(lectureBroadcast.getPort());
        } else {
            Log.d(TAG, "ServerSocket isn't bound.");
        }

    }
}
