package wgu.com.bhasha.c196scheduler.managers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import wgu.com.bhasha.c196scheduler.data.Assessment;
import wgu.com.bhasha.c196scheduler.data.Course;
import wgu.com.bhasha.c196scheduler.data.Mentor;

public class CourseManager {

    private SQLiteDatabase db;
    private final String table = "courses";
    private final String course_assessments_table = "course_assessments";
    private final String course_mentors_table = "course_mentors";

    public CourseManager(SQLiteDatabase db) {
        this.db = db;
    }

    public int addCourse(Course course) {
        ContentValues values = new ContentValues();

        values.put("title", course.getTitle());
        values.put("status", course.getStatus());
        values.put("startDate", course.getStartDate());
        values.put("startDateAlert", course.isStartDateAlert());
        values.put("endDate", course.getEndDate());
        values.put("endDateAlert", course.isEndDateAlert());
        values.put("notes", course.getNotes());

        int courseId = (int) db.insert(table, null, values);

        course.getAssessments().forEach(next -> {
            ContentValues courseAssessmentValues = new ContentValues();

            courseAssessmentValues.put("courseid", courseId);
            courseAssessmentValues.put("assessmentId", next.getId());

            db.insert(course_assessments_table, null, courseAssessmentValues);
        });

        course.getMentors().forEach(next -> {
            ContentValues courseMentorValues = new ContentValues();

            courseMentorValues.put("courseId", courseId);
            courseMentorValues.put("mentorId", next.getId());
            db.insert(course_mentors_table, null, courseMentorValues);
        });

        return courseId;
    }

    public void updateCourse(Course course) {
        ContentValues values = new ContentValues();

        values.put("title", course.getTitle());
        values.put("status", course.getStatus());
        values.put("startDate", course.getStartDate());
        values.put("startDateAlert", course.isStartDateAlert());
        values.put("endDate", course.getEndDate());
        values.put("endDateAlert", course.isEndDateAlert());
        values.put("notes", course.getNotes());

        db.update(table, values, "courseId = ?", new String[] {String.valueOf(course.getId())});

        db.delete(course_assessments_table,"courseid = ?", new String[] {String.valueOf(course.getId())});
        db.delete(course_mentors_table,"courseId = ?", new String[] {String.valueOf(course.getId())});

        course.getAssessments().forEach(next -> {
            ContentValues courseAssessmentValues = new ContentValues();

            courseAssessmentValues.put("courseid", course.getId());
            courseAssessmentValues.put("assessmentId", next.getId());
            db.insert(course_assessments_table, null, courseAssessmentValues);
        });

        course.getMentors().forEach(next -> {
            ContentValues courseMentorValues = new ContentValues();

            courseMentorValues.put("courseId", course.getId());
            courseMentorValues.put("mentorId", next.getId());
            db.insert(course_mentors_table, null, courseMentorValues);
        });
    }

    public void deleteCourse(Course course) {
        db.delete(course_assessments_table, "courseid = ?", new String[] {String.valueOf(course.getId())});
        db.delete(course_mentors_table, "courseId = ?", new String[] {String.valueOf(course.getId())});
        db.delete(table, "courseId = ?", new String[] {String.valueOf(course.getId())});
    }

    public ArrayList<Course> getAllCourses() {
        ArrayList<Course> courses = new ArrayList<>();
        final String query = "SELECT * FROM courses;";
        Cursor cursor = db.rawQuery(query, null);

        AssessmentManager assessmentManager = new AssessmentManager(db);
        MentorManager mentorManager = new MentorManager(db);

        if (cursor.moveToFirst()) {
            do {
                Course next = new Course();

                next.setId(cursor.getInt(0));
                next.setTitle(cursor.getString(1));
                next.setStatus(cursor.getString(2));
                next.setStartDate(cursor.getString(3));
                next.setStartDateAlert(cursor.getInt(4) > 0);
                next.setEndDate(cursor.getString(5));
                next.setEndDateAlert(cursor.getInt(6) > 0);
                next.setNotes(cursor.getString(7));

                final String courseAssessmentsQuery = "SELECT assessmentId FROM course_assessments WHERE courseId = ?;";
                Cursor courseAssessmentCursor = db.rawQuery(courseAssessmentsQuery, new String[] {"" + next.getId()});
                if (courseAssessmentCursor.moveToFirst()) {
                    do {
                        Assessment assessment = assessmentManager.getAssessment(courseAssessmentCursor.getString(0));
                        assessment.setSelected(true);

                        next.getAssessments().add(assessment);
                    } while (courseAssessmentCursor.moveToNext());
                }

                final String courseMentorsQuery = "SELECT mentorId FROM course_mentors WHERE courseId = ?;";
                Cursor courseMentorsCursor = db.rawQuery(courseMentorsQuery, new String[] {"" + next.getId()});
                if (courseMentorsCursor.moveToFirst()) {
                    do {
                        Mentor mentor = mentorManager.getMentor(courseMentorsCursor.getString(0));
                        mentor.setSelected(true);

                        next.getMentors().add(mentor);
                    } while (courseMentorsCursor.moveToNext());
                }

                courses.add(next);
            } while (cursor.moveToNext());
        }

        return courses;
    }

    public Course getCourse(String courseId) {
        final String query = "SELECT * FROM courses WHERE courseId = ?;";
        Cursor cursor = db.rawQuery(query, new String[] {courseId});

        AssessmentManager assessmentManager = new AssessmentManager(db);
        MentorManager mentorManager = new MentorManager(db);

        if (cursor.moveToFirst()) {
                Course next = new Course();

                next.setId(cursor.getInt(0));
                next.setTitle(cursor.getString(1));
                next.setStatus(cursor.getString(2));
                next.setStartDate(cursor.getString(3));
                next.setStartDateAlert(cursor.getInt(4) > 0);
                next.setEndDate(cursor.getString(5));
                next.setEndDateAlert(cursor.getInt(6) > 0);
                next.setNotes(cursor.getString(7));

                final String courseAssessmentsQuery = "SELECT assessmentId FROM course_assessments WHERE courseId = ?;";
                Cursor courseAssessmentCursor = db.rawQuery(courseAssessmentsQuery, new String[] {"" + next.getId()});
                if (courseAssessmentCursor.moveToFirst()) {
                    do {
                        Assessment assessment = assessmentManager.getAssessment(courseAssessmentCursor.getString(0));
                        assessment.setSelected(true);

                        next.getAssessments().add(assessment);
                    } while (courseAssessmentCursor.moveToNext());
                }

                final String courseMentorsQuery = "SELECT mentorId FROM course_mentors WHERE courseId = ?;";
                Cursor courseMentorsCursor = db.rawQuery(courseMentorsQuery, new String[] {"" + next.getId()});
                if (courseMentorsCursor.moveToFirst()) {
                    do {
                        Mentor mentor = mentorManager.getMentor(courseMentorsCursor.getString(0));
                        mentor.setSelected(true);

                        next.getMentors().add(mentor);
                    } while (courseMentorsCursor.moveToNext());
                }

                return next;
        }
        return null;
    }

}
