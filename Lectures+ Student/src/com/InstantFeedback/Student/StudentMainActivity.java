package com.InstantFeedback.Student;

import android.app.Activity;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import com.example.Lectures__Library.StudentID;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.ArrayList;


public class StudentMainActivity extends Activity {
    private NsdHelper nsdHelper;
    private LectureAttender lectureAttender;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        nsdHelper = new NsdHelper(getApplicationContext());
    }

    private ArrayList<Lecture> scanForLectures() {
        return nsdHelper.getListOfLectures();
    }

    private void joinLecture(Lecture lecture, StudentID id) {
        nsdHelper.chooseLecture(lecture, id);
        NsdServiceInfo service = nsdHelper.getService();

        if (service != null) {
            int port = service.getPort();
            InetAddress address = service.getHost();

            lectureAttender = new LectureAttender(address, port);
        }

    }
}
