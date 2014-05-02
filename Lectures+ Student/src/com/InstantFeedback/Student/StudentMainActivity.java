package com.InstantFeedback.Student;

import android.app.Activity;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.os.Handler;

import java.net.InetAddress;
import java.util.ArrayList;

import android.os.Message;
import com.InstantFeedback.Library.Lecture;
import com.InstantFeedback.Library.Question;
import com.InstantFeedback.Library.StudentID;


public class StudentMainActivity extends Activity {
    private NsdHelper nsdHelper;
    private LectureAttender lectureAttender;
    private Handler updateHandler;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        nsdHelper = new NsdHelper(getApplicationContext());

        updateHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Bundle receivedBundle = msg.getData();
                String type = receivedBundle.getString("type");
                if (type == "question") {
                    Question question = new Question(receivedBundle.getString("question")); //TODO Implement what to do with the question
                }

            }
        };
    }

    private ArrayList<Lecture> scanForLectures() {
        return nsdHelper.getListOfLectures();
    }

    private void joinLecture(Lecture lecture, StudentID id) { //caller must provide the id of this student
        nsdHelper.chooseLecture(lecture, id);
        NsdServiceInfo service = nsdHelper.getService();

        if (service != null) {
            int port = service.getPort();
            InetAddress address = service.getHost();

            lectureAttender = new LectureAttender(address, port, updateHandler);
        }

    }
}
