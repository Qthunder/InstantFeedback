package com.InstantFeedback.Lecturer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.*;

public class LecturerMainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    String name = "";
    File path = Environment.getExternalStorageDirectory();
    File file = new File(path, "/lecturername.txt");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        if (file.exists()){
            try {
                // reads lecturer name from file
                FileInputStream input = new FileInputStream(file);
                int c;
                while ((c = input.read()) != -1){
                    name = name + Character.toString((char) c);
                }

                // displays on screen
                TextView welcome = (TextView) findViewById(R.id.textView);
                welcome.setText("Welcome back, " + name + "!");
            } catch (IOException e){

            }
        }
        else {
            showDialog("New User");
        }
    }

    // saves name to file
    public void saveName(){

        try {
            path.mkdirs();
            FileOutputStream output = new FileOutputStream(file);
            output.write(name.getBytes());
            output.close();
        } catch (IOException e){

        }
    }
    // create the popup which allows the lecturer to enter their name
    public void showDialog(String title){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);
        builder.setView(input)
                .setTitle(title)
                .setMessage(R.string.dialog_prompt)
                .setPositiveButton(R.string.dialog_submit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        name = input.getText().toString();
                        TextView welcome = (TextView) findViewById(R.id.textView);
                        welcome.setText("Welcome, " + name + "!");
                        saveName();
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .setCancelable(false)
                .show();
    }

    // called when rename_button pressed
    public void changeName(View view) { showDialog("Edit saved name"); }
}
