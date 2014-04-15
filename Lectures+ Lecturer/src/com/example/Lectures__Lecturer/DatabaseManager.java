package com.example.Lectures__Lecturer;

import android.app.Application;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import com.example.Lectures__Lecturer.LecturerDatabaseContract.*;

/**
 * Created by edisach on 15/04/14.
 */
public class DatabaseManager extends Application{
    private DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
    SQLiteDatabase db;

    private static final String incrementRank =
            "UPDATE ? SET rank = rank + 1 WHERE rank >= ?";

    private static final String decrementRank =
            "UPDATE ? SET rank = rank - 1 WHERE rank >= ?";

    // Courses aren't ranked, so no need to insert
    public Integer createCourse(String name, String lecturer) {
        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Courses.COLUMN_NAME_COURSE, name);
        values.put(Courses.COLUMN_NAME_LECTURER, lecturer);

        Integer newRowId;
        newRowId = (int)(long) db.insert(Courses.TABLE_NAME, null, values);

        return newRowId;
    }

    // Insert a new lecture at a given rank in the course
    public Integer insertLecture(Integer course_id, String lecture_name, Integer lecture_rank) {
        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Lectures.COLUMN_NAME_COURSE_ID, course_id);
        values.put(Lectures.COLUMN_NAME_LECTURE_NAME, lecture_name);
        if (lecture_rank != null) {
            values.put(Lectures.COLUMN_NAME_LECTURE_RANK, lecture_rank);
            Object[] bindArgs = new Object[]{Lectures.TABLE_NAME, Lectures.COLUMN_NAME_LECTURE_RANK};
            db.execSQL(incrementRank, bindArgs);
        }

        Integer newRowId;
        newRowId = (int)(long)db.insert(Lectures.TABLE_NAME, null, values);

        return newRowId;
    }

    // Add a new lecture on to the end of the course
    public Integer createLecture(Integer course_id, String lecture_name) {
        return insertLecture(course_id, lecture_name, null);
    }
}
