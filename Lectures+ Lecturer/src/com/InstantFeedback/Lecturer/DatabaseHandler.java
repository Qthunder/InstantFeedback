package com.InstantFeedback.Lecturer;

import android.database.Cursor;
import com.InstantFeedback.Lecturer.LecturerDatabaseContract.*;
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
                Courses.COLUMN_NAME_COURSE_ID,
                Courses.COLUMN_NAME_COURSE
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

        int id_index = cursor.getColumnIndex(Courses.COLUMN_NAME_COURSE_ID);
        int name_index = cursor.getColumnIndex(Courses.COLUMN_NAME_COURSE);
        int lecturer_index = cursor.getColumnIndex(Courses.COLUMN_NAME_LECTURER);

        cursor.moveToFirst();
        while (!cursor.isLast()) {
            int id = cursor.getInt(id_index);
            String name = cursor.getString(name_index);
            String lecturer = cursor.getString(lecturer_index);

            Course course = new Course(id, name, lecturer);
            list.add(course);

            cursor.moveToNext();
        }

        return list;
    }
}
