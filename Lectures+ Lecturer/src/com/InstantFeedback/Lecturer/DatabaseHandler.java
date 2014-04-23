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
                Courses.COLUMN_NAME_COURSE,
                Courses.COLUMN_NAME_LECTURER
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

    public ArrayList<Lecture> getLectures(int course_id) {
        ArrayList<Lecture> list = new ArrayList<Lecture>();
        String[] projection = {
                Lectures.COLUMN_NAME_LECTURE_ID,
                Lectures.COLUMN_NAME_LECTURE_NAME,
                Lectures.COLUMN_NAME_LECTURE_RANK
        };

        Cursor cursor = db.query(
                Lectures.TABLE_NAME,
                projection,
                Lectures.COLUMN_NAME_COURSE_ID,
                new String[]{String.valueOf(course_id)},
                null,
                null,
                Lectures.COLUMN_NAME_LECTURE_RANK
        );

        int id_index = cursor.getColumnIndex(Lectures.COLUMN_NAME_LECTURE_ID);
        int name_index = cursor.getColumnIndex(Lectures.COLUMN_NAME_LECTURE_NAME);
        int rank_index = cursor.getColumnIndex(Lectures.COLUMN_NAME_LECTURE_RANK);

        cursor.moveToFirst();
        while (!cursor.isLast()) {
            int id = cursor.getInt(id_index);
            String name = cursor.getString(name_index);
            int rank = cursor.getInt(rank_index);

            Lecture lecture = new Lecture(id, name, rank);
            list.add(lecture);

            cursor.moveToNext();
        }

        return list;
    }

    public ArrayList<Question> getQuestions(int lecture_id) {
        ArrayList<Question> list = new ArrayList<Question>();
        String[] projection = {
                Questions.COLUMN_NAME_QUESTION_ID,
                Questions.COLUMN_NAME_QUESTION_TEXT,
                Questions.COLUMN_NAME_QUESTION_TYPE,
                Questions.COLUMN_NAME_QUESTION_RANK
        };

        Cursor cursor = db.query(
                Questions.TABLE_NAME,
                projection,
                Questions.COLUMN_NAME_LECTURE_ID,
                new String[] {String.valueOf(lecture_id)},
                null,
                null,
                Questions.COLUMN_NAME_QUESTION_RANK
        );

        int id_index = cursor.getColumnIndex(Questions.COLUMN_NAME_QUESTION_ID);
        int text_index = cursor.getColumnIndex(Questions.COLUMN_NAME_QUESTION_TEXT);
        int type_index = cursor.getColumnIndex(Questions.COLUMN_NAME_QUESTION_TYPE);
        int rank_index = cursor.getColumnIndex(Questions.COLUMN_NAME_QUESTION_RANK);

        cursor.moveToFirst();
        while(!cursor.isLast()) {
            int id = cursor.getInt(id_index);
            String text = cursor.getString(text_index);
            String type = cursor.getString(type_index);
            int rank = cursor.getInt(rank_index);

            Question question = new Question(id, text, type, rank);
            list.add(question);

            cursor.moveToNext();
        }
        return  list;
    }

    public ArrayList<Answer> getAnswers(int question_id) {
        ArrayList<Answer> list = new ArrayList<Answer>();
        String[] projection = {
                Answers.COLUMN_NAME_ANSWER_ID,
                Answers.COLUMN_NAME_ANSWER_TEXT,
                Answers.COLUMN_NAME_ANSWER_BOOL,
                Answers.COLUMN_NAME_ANSWER_RANK
        };

        Cursor cursor = db.query(
                Answers.TABLE_NAME,
                projection,
                Answers.COLUMN_NAME_QUESTION_ID,
                new String[] {String.valueOf(question_id)},
                null,
                null,
                Answers.COLUMN_NAME_ANSWER_RANK
        );

        int id_index = cursor.getColumnIndex(Answers.COLUMN_NAME_ANSWER_ID);
        int text_index = cursor.getColumnIndex(Answers.COLUMN_NAME_ANSWER_TEXT);
        int bool_index = cursor.getColumnIndex(Answers.COLUMN_NAME_ANSWER_BOOL);
        int rank_index = cursor.getColumnIndex(Answers.COLUMN_NAME_ANSWER_RANK);

        cursor.moveToFirst();
        while(!cursor.isLast()) {
            int id = cursor.getInt(id_index);
            String text = cursor.getString(text_index);
            int bool = cursor.getInt(bool_index);
            int rank = cursor.getInt(rank_index);

            Answer answer = new Answer(id, text, bool, rank);
            list.add(answer);

            cursor.moveToNext();
        }
        return list;
    }
}
