package com.InstantFeedback.Student;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class StudentMainActivity extends Activity implements AdapterView.OnItemSelectedListener {
    String[] lecturers = {"a", "b", "c", "d", "e"};
    String selectedLecturer;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // fill array with connected lecturers (to be implemented)

        // fill the drop-down with a list of connected lecturers
        Spinner spinner = (Spinner) findViewById(R.id.connect_spinner);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this,
                android.R.layout.simple_spinner_item, lecturers);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void onItemSelected(AdapterView parent, View view, int pos, long id){
        selectedLecturer = lecturers[pos];
    }

    public void onNothingSelected(AdapterView parent){

    }

    // called when button is pressed
    public void connect(){

    }
}
