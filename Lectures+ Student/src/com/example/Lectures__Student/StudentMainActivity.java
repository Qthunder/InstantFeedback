package com.example.Lectures__Student;

import android.app.Activity;
import android.os.Bundle;

import java.util.ArrayList;


public class StudentMainActivity extends Activity {
    private NsdHelper nsdHelper;

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
}
