package com.example.Lectures__Lecturer;

import android.provider.BaseColumns;

/**
 * Contract for the database
 * Created by edisach on 13/04/14.
 */
public final class LecturerDatabaseContract {
    public LecturerDatabaseContract() {}

    public static abstract class Courses implements BaseColumns {
        public static final String TABLE_NAME = "courses";
        public static final String COLUMN_NAME_COURSE_ID = "course_id";
        public static final String COLUMN_NAME_COURSE = "course_name";
        public static final String COLUMN_NAME_LECTURER = "lecturer_name";
    }

    public static abstract class Lectures implements BaseColumns {
        public static final String TABLE_NAME = "lectures";
        public static final String COLUMN_NAME_LECTURE_ID = "lecture_id";
        public static final String COLUMN_NAME_COURSE_ID = "course_id";
        public static final String COLUMN_NAME_LECTURE_NAME = "lecture_name";
        public static final String COLUMN_NAME_LECTURE_RANK = "rank";
    }

    public static abstract class Questions implements BaseColumns {
        public static final String TABLE_NAME = "questions";
        public static final String COLUMN_NAME_QUESTION_ID = "question_id";
        public static final String COLUMN_NAME_LECTURE_ID = "lecture_id";
        public static final String COLUMN_NAME_QUESTION_TEXT = "question_text";
        public static final String COLUMN_NAME_QUESTION_TYPE = "question_type";
        public static final String COLUMN_NAME_QUESTION_RANK = "rank";
    }

    public static abstract class Answers implements BaseColumns {
        public static final String TABLE_NAME = "answers";
        public static final String COLUMN_NAME_ANSWER_ID = "answer_id";
        public static final String COLUMN_NAME_QUESTION_ID = "question_id";
        public static final String COLUMN_NAME_ANSWER_TEXT = "answer_text";
        public static final String COLUMN_NAME_ANSWER_BOOL = "answer_bool"; // Stored as 1/0
        public static final String COLUMN_NAME_ANSWER_RANK = "rank";
    }
}
