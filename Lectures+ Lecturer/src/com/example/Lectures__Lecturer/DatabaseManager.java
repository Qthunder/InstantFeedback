package com.example.Lectures__Lecturer;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.Lectures__Lecturer.LecturerDatabaseContract.*;

import java.util.ArrayList;

/**
 *
 * Created by edisach on 15/04/14.
 */
public class DatabaseManager extends Application{
    private DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
    private SQLiteDatabase db;

    private static final String incrementRank =
            "UPDATE ? SET rank = rank + 1 WHERE rank >= ? AND ? = ?";

    private static final String decrementRank =
            "UPDATE ? SET rank = rank - 1 WHERE rank >= ? AND ? = ?";

    // gets the number of items in the given table with given test_id
    private long getSize (String table_name, String id, Integer test_id) {
        String temp = "SELECT COUNT(*) FROM " + table_name + " WHERE " + id + " = " + test_id;
        long ret;
        try {
            //noinspection ConstantConditions
            ret = db.compileStatement(temp).simpleQueryForLong();
        }
        catch (NullPointerException e) {
            System.out.print(e);
            ret = 0;
        }
        return ret;
    }

    // Courses aren't ranked, so no need to insert
    // Create a new course
    public long createCourse(String name, String lecturer) {
        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Courses.COLUMN_NAME_COURSE, name);
        values.put(Courses.COLUMN_NAME_LECTURER, lecturer);

        long newRowId;
        newRowId = db.insert(Courses.TABLE_NAME, null, values);

        return newRowId;
    }

    public ArrayList<Integer> getCourses() {
        ArrayList<Integer> list = new ArrayList<Integer>();
        String[] projection = {
                Courses.COLUMN_NAME_COURSE_ID,
                Courses.COLUMN_NAME_COURSE
        };
        Cursor cursor = db.query(
                Courses.TABLE_NAME,
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

    // Insert a new lecture at a given rank in the course
    public long insertLecture(Integer course_id, String name, long rank) {
        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Lectures.COLUMN_NAME_COURSE_ID, course_id);
        values.put(Lectures.COLUMN_NAME_LECTURE_NAME, name);
        values.put(Lectures.COLUMN_NAME_LECTURE_RANK, rank);

        Object[] bindArgs = new Object[]{Lectures.TABLE_NAME, Lectures.COLUMN_NAME_LECTURE_RANK,
        Lectures.COLUMN_NAME_COURSE_ID, course_id};
        db.execSQL(incrementRank, bindArgs);

        long newRowId;
        newRowId = db.insert(Lectures.TABLE_NAME, null, values);

        return newRowId;
    }

    // Create a new lecture at the end of the course
    public long createLecture(Integer course_id, String name) {
        long temp = getSize(Lectures.TABLE_NAME, Lectures.COLUMN_NAME_COURSE_ID, course_id);
        return insertLecture(course_id, name, temp);
    }

    // Insert a new question at a given rank in the lecture
    public long insertQuestion(Integer lecture_id, String text, String type, long rank){
        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Questions.COLUMN_NAME_LECTURE_ID, lecture_id);
        values.put(Questions.COLUMN_NAME_QUESTION_TEXT, text);
        values.put(Questions.COLUMN_NAME_QUESTION_TYPE, type);
        values.put(Questions.COLUMN_NAME_QUESTION_RANK, rank);

        Object[] bindArgs = new Object[]{Questions.TABLE_NAME, Questions.COLUMN_NAME_QUESTION_RANK,
        Questions.COLUMN_NAME_LECTURE_ID, lecture_id};
        db.execSQL(incrementRank, bindArgs);

        long newRowId;
        newRowId = db.insert(Questions.TABLE_NAME, null, values);

        return newRowId;
    }

    // Create a new question to the end of the course
    public long createQuestion(Integer lecture_id, String text, String type){
        long temp = getSize(Questions.TABLE_NAME, Questions.COLUMN_NAME_LECTURE_ID, lecture_id);
        return insertQuestion(lecture_id, text, type, temp);
    }

    // Insert a new answer to a question at a given rank
    public long insertAnswer(Integer question_id, String text, Boolean truth, long rank){
        db = dbHelper.getWritableDatabase();
        Integer truthInt;
        if (truth) truthInt = 1; else truthInt = 0;

        ContentValues values = new ContentValues();
        values.put(Answers.COLUMN_NAME_QUESTION_ID, question_id);
        values.put(Answers.COLUMN_NAME_ANSWER_TEXT, text);
        values.put(Answers.COLUMN_NAME_ANSWER_BOOL, truthInt);
        values.put(Answers.COLUMN_NAME_ANSWER_RANK, rank);

        Object[] bindArgs = new Object[]{Answers.TABLE_NAME, Answers.COLUMN_NAME_ANSWER_RANK,
        Answers.COLUMN_NAME_QUESTION_ID, question_id};
        db.execSQL(incrementRank, bindArgs);

        long newRowId;
        newRowId = db.insert(Answers.TABLE_NAME, null, values);

        return newRowId;
    }

    // Create a new answer at the end
    public long createAnswer(Integer question_id, String text, Boolean truth) {
        long temp = getSize(Answers.TABLE_NAME, Answers.COLUMN_NAME_QUESTION_ID, question_id);
        return insertAnswer(question_id, text, truth, temp);
    }
}
