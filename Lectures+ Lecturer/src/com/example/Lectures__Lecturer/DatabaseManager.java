package com.example.Lectures__Lecturer;

import android.app.Application;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import com.example.Lectures__Lecturer.LecturerDatabaseContract.*;

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

    private long getSize (String table_name, String id, Integer test) {
        String temp = "SELECT number FROM table_name WHERE " + id + " = " + test;
        return db.compileStatement(temp).simpleQueryForLong();
    }

    private Integer getSize(String table, Integer id) {
        String sql = "SELECT size FROM " + table + "WHERE " + table + "_id = " + id;
        SQLiteStatement compiled = db.compileStatement(sql);
        Integer size = (int)(long) compiled.simpleQueryForLong();
        return size;
    }

    // Courses aren't ranked, so no need to insert
    // Create a new course
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
    public Integer insertLecture(Integer course_id, String name, Integer rank) {
        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Lectures.COLUMN_NAME_COURSE_ID, course_id);
        values.put(Lectures.COLUMN_NAME_LECTURE_NAME, name);
        if (rank != null) {
            values.put(Lectures.COLUMN_NAME_LECTURE_RANK, rank);
            Object[] bindArgs = new Object[]{Lectures.TABLE_NAME, Lectures.COLUMN_NAME_LECTURE_RANK,
            Lectures.COLUMN_NAME_COURSE_ID, course_id};
            db.execSQL(incrementRank, bindArgs);
        }
        else {
            long temp = getSize(Lectures.TABLE_NAME, Lectures.COLUMN_NAME_COURSE_ID, course_id);
            values.put(Lectures.COLUMN_NAME_LECTURE_RANK, temp);
        }

        Integer newRowId;
        newRowId = (int)(long)db.insert(Lectures.TABLE_NAME, null, values);

        return newRowId;
    }

    // Add a new lecture on to the end of the course
    public Integer createLecture(Integer course_id, String name) {
        return insertLecture(course_id, name, null);
    }

    // Add a new question at a given rank in the lecture
    public Integer insertQuestion(Integer lecture_id, String text, String type, Integer rank){
        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Questions.COLUMN_NAME_LECTURE_ID, lecture_id);
        values.put(Questions.COLUMN_NAME_QUESTION_TEXT, text);
        values.put(Questions.COLUMN_NAME_QUESTION_TYPE, type);
        if (rank != null) {
            values.put(Questions.COLUMN_NAME_QUESTION_RANK, rank);
            Object[] bindArgs = new Object[]{Questions.TABLE_NAME, Questions.COLUMN_NAME_QUESTION_RANK,
            Questions.COLUMN_NAME_LECTURE_ID, lecture_id};
            db.execSQL(incrementRank, bindArgs);
        }
        else {
            long temp = getSize(Questions.TABLE_NAME, Questions.COLUMN_NAME_LECTURE_ID, lecture_id);
            values.put(Questions.COLUMN_NAME_QUESTION_RANK, temp);
        }

        Integer newRowId;
        newRowId = (int)(long) db.insert(Questions.TABLE_NAME, null, values);

        return newRowId;
    }

    // Add a new question to the end of the course
    public Integer addQuestion(Integer lecture_id, String text, String type){
        return insertQuestion(lecture_id, text, type, null);
    }

    // Add a new answer to a question at a given rank
    public Integer insertAnswer(Integer question_id, String text, Boolean truth, Integer rank){
        db = dbHelper.getWritableDatabase();
        Integer truthInt;
        if (truth) truthInt = 1; else truthInt = 0;

        ContentValues values = new ContentValues();
        values.put(Answers.COLUMN_NAME_QUESTION_ID, question_id);
        values.put(Answers.COLUMN_NAME_ANSWER_TEXT, text);
        values.put(Answers.COLUMN_NAME_ANSWER_BOOL, truthInt);
        if (rank != null) {
            values.put(Answers.COLUMN_NAME_ANSWER_RANK, rank);
            Object[] bindArgs = new Object[]{Answers.TABLE_NAME, Answers.COLUMN_NAME_ANSWER_RANK,
            Answers.COLUMN_NAME_QUESTION_ID, question_id};
            db.execSQL(incrementRank, bindArgs);
        }
        else {
            long temp = getSize(Answers.TABLE_NAME, Answers.COLUMN_NAME_QUESTION_ID, question_id);
            values.put(Answers.COLUMN_NAME_ANSWER_RANK, temp);
        }

        Integer newRowId;
        newRowId = (int)(long) db.insert(Answers.TABLE_NAME, null, values);

        return newRowId;
    }

    // Add a new answer at the end
    public Integer addAnswer(Integer question_id, String text, Boolean truth) {
        return insertAnswer(question_id, text, truth, null);
    }
}
