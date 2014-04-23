package com.InstantFeedback.Lecturer;

import android.database.Cursor;
import java.util.ArrayList;

/**
 * Wrapper class for the database handling
 * API methods here
 * Created by edisach on 13/04/14.
 */
public class DatabaseHandler extends DatabaseManager {

    public ArrayList<Course> getCourses() {
        ArrayList<Course> list = new ArrayList<Course>();
        String[] projection = {
                LecturerDatabaseContract.Courses.COLUMN_NAME_COURSE_ID,
                LecturerDatabaseContract.Courses.COLUMN_NAME_COURSE
        };
        Cursor cursor = db.query(
                LecturerDatabaseContract.Courses.TABLE_NAME,
                projection,
                null, // Where columns
                null, // Where values
                null, // Group by
                null, // Filter
                null  // Sort order
        );

        cursor.moveToFirst();

        return list;
    }
}
