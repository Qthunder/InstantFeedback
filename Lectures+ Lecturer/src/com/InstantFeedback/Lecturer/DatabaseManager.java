package com.InstantFeedback.Lecturer;

import android.app.Application;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import com.InstantFeedback.Lecturer.LecturerDatabaseContract.*;

/**
 *
 * Created by edisach on 15/04/14.
 */
public class DatabaseManager extends Application{
    private DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
    SQLiteDatabase db;

    class Course {
        private int id;
        private String name;
        private String lecturer;

        public Course(int id, String name, String lecturer) {
            this.id = id;
            this.name = name;
            this.lecturer = lecturer;
        }

        // TODO -- make setters update the database so courses can be edited
        public Integer getId() {
            return id;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getLecturer() {
            return lecturer;
        }
        public void setLecturer(String lecturer) {
            this.lecturer = lecturer;
        }

    }

    class Lecture {
        private int id;
        private String name;
        private int rank;

        Lecture(int id, String name, int rank) {
            this.id = id;
            this.name = name;
            this.rank = rank;
        }

        // TODO -- make setName update the database
        public int getId() {
            return id;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public int getRank() {
            return rank;
        }
    }

    class Question {
        private int id;
        private String text;
        private String question_type;
        private int rank;

        Question(int id, String text, String question_type, int rank) {
            this.id = id;
            this.text = text;
            this.question_type = question_type;
            this.rank = rank;
        }

        // TODO -- make setters update database
        public int getId() {
            return id;
        }
        public String getText() {
            return text;
        }
        public void setText(String text) {
            this.text = text;
        }
        public String getQuestion_type() {
            return question_type;
        }
        public void setQuestion_type(String question_type) {
            this.question_type = question_type;
        }
        public int getRank() {
            return rank;
        }
    }

    class Answer {
        private int id;
        private String text;
        private Boolean truth;
        private int rank;

        Answer(int id, String text, int truth, int rank) {
            this.id = id;
            this.text = text;
            if (truth == 1) this.truth = true;
            else this.truth = false;
            this.rank = rank;
        }

        // TODO -- make setters update database, setTruth convert bool to int
        public int getId() {
            return id;
        }
        public String getText() {
            return text;
        }
        public void setText(String text) {
            this.text = text;
        }
        public Boolean getTruth() {
            return truth;
        }
        public void setTruth(Boolean truth) {
            this.truth = truth;
        }
        public int getRank() {
            return rank;
        }
    }

    private static final String incrementRank =
            "UPDATE ? SET rank = rank + 1 WHERE rank >= ? AND ? = ?";

    private static final String decrementRank =
            "UPDATE ? SET rank = rank - 1 WHERE rank >= ? AND ? = ?";

    // gets the number of items in the given table with given test_id
    private long getSize (String table_name, String id, int test_id) {
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
    long createCourse(String name, String lecturer) {
        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Courses.COLUMN_NAME_COURSE, name);
        values.put(Courses.COLUMN_NAME_LECTURER, lecturer);

        long newRowId;
        newRowId = db.insert(Courses.TABLE_NAME, null, values);

        return newRowId;
    }

    // Insert a new lecture at a given rank in the course
    long insertLecture(int course_id, String name, long rank) {
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
    long createLecture(int course_id, String name) {
        long temp = getSize(Lectures.TABLE_NAME, Lectures.COLUMN_NAME_COURSE_ID, course_id);
        return insertLecture(course_id, name, temp);
    }

    // Insert a new question at a given rank in the lecture
    long insertQuestion(int lecture_id, String text, String type, long rank){
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
    long createQuestion(int lecture_id, String text, String type){
        long temp = getSize(Questions.TABLE_NAME, Questions.COLUMN_NAME_LECTURE_ID, lecture_id);
        return insertQuestion(lecture_id, text, type, temp);
    }

    // Insert a new answer to a question at a given rank
    long insertAnswer(int question_id, String text, Boolean truth, long rank){
        db = dbHelper.getWritableDatabase();
        int truthInt;
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
    long createAnswer(int question_id, String text, Boolean truth) {
        long temp = getSize(Answers.TABLE_NAME, Answers.COLUMN_NAME_QUESTION_ID, question_id);
        return insertAnswer(question_id, text, truth, temp);
    }

    int deleteAnswer(int answer_id) {
        String where = Answers.COLUMN_NAME_QUESTION_ID + " = ";
        return db.delete(Answers.TABLE_NAME, where, new String[]{String.valueOf(answer_id)});
    }
}
