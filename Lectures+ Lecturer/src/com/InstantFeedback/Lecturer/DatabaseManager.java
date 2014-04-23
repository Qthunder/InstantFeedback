package com.InstantFeedback.Lecturer;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.InstantFeedback.Lecturer.LecturerDatabaseContract.*;

/**
 *
 * Created by edisach on 15/04/14.
 */
public class DatabaseManager extends Application{
    protected DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
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

    private int getRank(String table_name, String id, int test_id) {
        db = dbHelper.getWritableDatabase();
        String sql = "SELECT rank FROM " + table_name + " WHERE " + id + " = " + test_id;
        return (int) db.compileStatement(sql).simpleQueryForLong();
    }

    private int getId(String table_name, String parent_id, String self_id, int test_id) {
        db = dbHelper.getWritableDatabase();
        String sql = "SELECT " + parent_id + " FROM " + table_name + " WHERE " + self_id + " = " + test_id;
        return (int) db.compileStatement(sql).simpleQueryForLong();
    }

    // TODO -- make sure all deletion methods update rank correctly

    // gets the number of items in the given table with given test_id
    private long getSize (String table_name, String id, int test_id) {
        db = dbHelper.getWritableDatabase();
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

    // Deletes a course and all of its lectures etc
    // returns {courses, lectures, questions, answers}
    int[] deleteCourse(int course_id) {
        db = dbHelper.getWritableDatabase();
        int courses;
        int lectures = 0;
        int questions = 0;
        int answers = 0;
        String[] projection = {Lectures.COLUMN_NAME_LECTURE_ID};

        Cursor cursor = db.query(
                Lectures.TABLE_NAME,
                projection,
                Lectures.COLUMN_NAME_COURSE_ID,
                new String[]{String.valueOf(course_id)},
                null,
                null,
                null
        );

        int id_index = cursor.getColumnIndex(Lectures.COLUMN_NAME_LECTURE_ID);
        cursor.moveToFirst();
        while (!cursor.isLast()) {
            int id = cursor.getInt(id_index);
            int[] nums = deleteLecture(id);
            lectures += nums[0];
            questions += nums[1];
            answers += nums[2];
            cursor.moveToNext();
        }
        String where = Courses.COLUMN_NAME_COURSE_ID + " = ";
        String[] vals = new String[]{String.valueOf(course_id)};
        courses = db.delete(Courses.TABLE_NAME, where, vals);

        return new int[]{courses, lectures, questions, answers};
    }

    // Insert a new lecture at a given rank in the course
    long insertLecture(int course_id, String name, long rank) {
        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Lectures.COLUMN_NAME_COURSE_ID, course_id);
        values.put(Lectures.COLUMN_NAME_LECTURE_NAME, name);
        values.put(Lectures.COLUMN_NAME_LECTURE_RANK, rank);

        Object[] bindArgs = new Object[]{Lectures.TABLE_NAME, rank,
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

    // Deletes a lecture and all of its questions
    // Returns an array of length 3, {number lectures, number questions, number answers}
    int[] deleteLecture(int lecture_id) {
        db = dbHelper.getWritableDatabase();
        int lectures;
        int questions = 0;
        int answers = 0;

        String[] projection = {Questions.COLUMN_NAME_QUESTION_ID};

        Cursor cursor = db.query(
                Questions.TABLE_NAME,
                projection,
                Questions.COLUMN_NAME_LECTURE_ID,
                new String[]{String.valueOf(lecture_id)},
                null,
                null,
                null
        );

        int id_index = cursor.getColumnIndex(Questions.COLUMN_NAME_QUESTION_ID);
        cursor.moveToFirst();
        while (!cursor.isLast()) {
            int id = cursor.getInt(id_index);
            int[] nums = deleteQuestion(id);
            questions += nums[0];
            answers += nums[1];
            cursor.moveToNext();
        }

        int rank = getRank(Lectures.TABLE_NAME, Lectures.COLUMN_NAME_LECTURE_ID, lecture_id);
        int id = getId(Lectures.TABLE_NAME, Lectures.COLUMN_NAME_COURSE_ID,
                Lectures.COLUMN_NAME_LECTURE_ID, lecture_id);
        Object[] bindArgs = {Lectures.TABLE_NAME, rank, Lectures.COLUMN_NAME_COURSE_ID, id};
        db.execSQL(decrementRank, bindArgs);

        String where = Lectures.COLUMN_NAME_LECTURE_ID + " = ";
        String[] vals = new String[]{String.valueOf(lecture_id)};
        lectures = db.delete(Lectures.TABLE_NAME, where, vals);

        return new int[]{lectures, questions, answers};
    }

    // Insert a new question at a given rank in the lecture
    long insertQuestion(int lecture_id, String text, String type, long rank){
        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Questions.COLUMN_NAME_LECTURE_ID, lecture_id);
        values.put(Questions.COLUMN_NAME_QUESTION_TEXT, text);
        values.put(Questions.COLUMN_NAME_QUESTION_TYPE, type);
        values.put(Questions.COLUMN_NAME_QUESTION_RANK, rank);

        Object[] bindArgs = new Object[]{Questions.TABLE_NAME, rank,
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

    // Delete the question with given id and all of its answers
    // Returns an array of length 2, first element number of questions, second number of answers deleted
    int[] deleteQuestion(int question_id) {
        db = dbHelper.getWritableDatabase();

        int rank = getRank(Questions.TABLE_NAME, Questions.COLUMN_NAME_QUESTION_ID, question_id);
        int id = getId(Questions.TABLE_NAME, Questions.COLUMN_NAME_LECTURE_ID,
                Questions.COLUMN_NAME_QUESTION_ID, question_id);
        Object[] bindArgs = {Questions.TABLE_NAME, rank, Questions.COLUMN_NAME_LECTURE_ID, id};
        db.execSQL(decrementRank, bindArgs);

        String whereQuestions = Questions.COLUMN_NAME_QUESTION_ID + " = ";
        String whereAnswers = Answers.COLUMN_NAME_QUESTION_ID + " = ";
        int questions = db.delete(Questions.TABLE_NAME, whereQuestions, new String[]{String.valueOf(question_id)});
        int answers = db.delete(Answers.TABLE_NAME, whereAnswers, new String[]{String.valueOf(question_id)});
        return new int[]{questions, answers};
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

        Object[] bindArgs = new Object[]{Answers.TABLE_NAME, rank,
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

    // Delete the answer with given id
    int deleteAnswer(int answer_id) {
        db = dbHelper.getWritableDatabase();

        int rank = getRank(Answers.TABLE_NAME, Answers.COLUMN_NAME_ANSWER_ID, answer_id);
        int id = getId(Answers.TABLE_NAME, Answers.COLUMN_NAME_QUESTION_ID,
                Answers.COLUMN_NAME_ANSWER_ID, answer_id);
        Object[] bindArgs = {Answers.TABLE_NAME, rank, Answers.COLUMN_NAME_ANSWER_ID, answer_id};
        db.execSQL(decrementRank, bindArgs);

        String where = Answers.COLUMN_NAME_ANSWER_ID + " = ";
        String[] vals = new String[]{String.valueOf(answer_id)};
        int num = db.delete(Answers.TABLE_NAME, where, vals);
        return num;
    }
}
