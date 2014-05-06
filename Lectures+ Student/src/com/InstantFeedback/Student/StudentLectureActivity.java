package com.InstantFeedback.Student;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

        TextView text = (TextView) findViewById(R.id.UserName);
        text.setText("You are connected to " + selectedLecturer);
    }

    public void goSlower(View view) {
        Toast.makeText(this, "Go Slower !", Toast.LENGTH_SHORT).show();
    }

    public void goFaster(View view) {
        Toast.makeText(this, "Go Faster !",Toast.LENGTH_SHORT).show();
    }

    public void putQuestion(View view){
        Toast.makeText(this,"Put Question "  , Toast.LENGTH_SHORT).show();
    }
    public void sendAnswer(View view){

        Toast.makeText(this,"Sent answer "  , Toast.LENGTH_SHORT).show();

    }
}

