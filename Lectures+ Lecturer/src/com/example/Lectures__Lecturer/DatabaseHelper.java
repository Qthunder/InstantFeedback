package com.example.Lectures__Lecturer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.Lectures__Lecturer.LecturerDatabaseContract.*;

/**
 * Creates and manages the database
 * Created by edisach on 15/04/14.
 */
public class DatabaseHelper extends SQLiteOpenHelper{

    // Database creation constants

    private static final String DATABASE_NAME = "lecturer.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String COMMA = ",";
    private static final String NOT_NULL = " NOT NULL";
    private static final String DEFAULT_0 = " DEFAULT 0";
    private static final String PRIMARY_KEY = " INTEGER PRIMARY KEY AUTOINCREMENT";
    private static final String DROP_IF_EXISTS = "DROP TABLE IF EXISTS ";

    private static final String CREATE_COURSES =
            "CREATE TABLE " + Courses.TABLE_NAME + " ("
            + Courses.COLUMN_NAME_COURSE_ID + PRIMARY_KEY + COMMA
            + Courses.COLUMN_NAME_COURSE + TEXT_TYPE + NOT_NULL + COMMA
            + Courses.COLUMN_NAME_LECTURER + TEXT_TYPE + NOT_NULL + ")";

    private static final String DROP_COURSES = DROP_IF_EXISTS + Courses.TABLE_NAME;

    private static final String CREATE_LECTURES =
            "CREATE TABLE " + Lectures.TABLE_NAME + " ("
            + Lectures.COLUMN_NAME_LECTURE_ID + PRIMARY_KEY + COMMA
            + Lectures.COLUMN_NAME_COURSE_ID + INT_TYPE + NOT_NULL + COMMA
            + Lectures.COLUMN_NAME_LECTURE_NAME + TEXT_TYPE + NOT_NULL + COMMA
            + Lectures.COLUMN_NAME_LECTURE_RANK + INT_TYPE + DEFAULT_0 + ")";

    private static final String DROP_LECTURES = DROP_IF_EXISTS + Lectures.TABLE_NAME;

    private static final String CREATE_QUESTIONS =
            "CREATE TABLE " + Questions.TABLE_NAME + " ("
            + Questions.COLUMN_NAME_QUESTION_ID + PRIMARY_KEY + COMMA
            + Questions.COLUMN_NAME_LECTURE_ID + INT_TYPE + NOT_NULL + COMMA
            + Questions.COLUMN_NAME_QUESTION_TEXT + TEXT_TYPE + NOT_NULL + COMMA
            + Questions.COLUMN_NAME_QUESTION_TYPE + TEXT_TYPE + NOT_NULL + COMMA
            + Questions.COLUMN_NAME_QUESTION_RANK + INT_TYPE + DEFAULT_0 + ")";

    private static final String DROP_QUESTIONS = DROP_IF_EXISTS + Questions.TABLE_NAME;

    private static final String CREATE_ANSWERS =
            "CREATE TABLE " + Answers.TABLE_NAME + " ("
            + Answers.COLUMN_NAME_ANSWER_ID + PRIMARY_KEY + COMMA
            + Answers.COLUMN_NAME_QUESTION_ID + INT_TYPE + NOT_NULL + COMMA
            + Answers.COLUMN_NAME_ANSWER_TEXT + TEXT_TYPE + NOT_NULL + COMMA
            + Answers.COLUMN_NAME_ANSWER_BOOL + INT_TYPE + NOT_NULL + COMMA
            + Answers.COLUMN_NAME_ANSWER_RANK + INT_TYPE + DEFAULT_0 + ")";

    private static final String DROP_ANSWERS = DROP_IF_EXISTS + Answers.TABLE_NAME;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_COURSES);
        db.execSQL(CREATE_LECTURES);
        db.execSQL(CREATE_QUESTIONS);
        db.execSQL(CREATE_ANSWERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_ANSWERS);
        db.execSQL(DROP_QUESTIONS);
        db.execSQL(DROP_LECTURES);
        db.execSQL(DROP_COURSES);
        onCreate(db);
    }
}
