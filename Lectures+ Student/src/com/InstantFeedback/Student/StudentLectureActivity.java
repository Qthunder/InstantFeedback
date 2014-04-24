package com.InstantFeedback.Student;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by Amy on 24/04/2014.
 */
public class StudentLectureActivity extends Activity {

    String selectedLecturer;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lecture);

        Intent intent = getIntent();
        selectedLecturer = intent.getStringExtra("com.InstantFeedback.Student.SelectedLecturer");

        TextView text = (TextView) findViewById(R.id.textView);
        text.setText("You are connected to " + selectedLecturer);
    }
}
