package wgu.com.bhasha.c196scheduler.managers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import wgu.com.bhasha.c196scheduler.data.Assessment;
import wgu.com.bhasha.c196scheduler.data.Mentor;

public class AssessmentManager {

    private SQLiteDatabase db;
    private final String table = "assessments";
    private final String course_assessments_table = "course_assessments";

    public AssessmentManager(SQLiteDatabase db) {
        this.db = db;
    }

    public int addAssessment(Assessment assessment) {
        ContentValues values = new ContentValues();

        values.put("type", assessment.getType());
        values.put("title", assessment.getTitle());
        values.put("dueDate", assessment.getDueDate());
        values.put("goalDate", assessment.getGoalDate());
        values.put("goalDateAlert", assessment.isGoalDateAlert());

        return (int) db.insert(table, null, values);
    }

    public void updateAssessment(Assessment assessment) {
        ContentValues values = new ContentValues();

        values.put("type", assessment.getType());
        values.put("title", assessment.getTitle());
        values.put("dueDate", assessment.getDueDate());
        values.put("goalDate", assessment.getGoalDate());
        values.put("goalDateAlert", assessment.isGoalDateAlert());
        db.update(table, values, "assessmentId = ?", new String[]{String.valueOf(assessment.getId())});
    }

    public void deleteAssessment(Assessment assessment) {
        db.delete(course_assessments_table, "assessmentId = ?", new String[] {String.valueOf(assessment.getId())});
        db.delete(table, "assessmentId = ?", new String[]{String.valueOf(assessment.getId())});
    }

    public Assessment getAssessment(String assessmentId) {
        final String query = "SELECT * FROM " + table + " WHERE assessmentId = ?;";
        Cursor cursor = db.rawQuery(query, new String[]{assessmentId});

        Assessment assessment = new Assessment();
        if (cursor.moveToFirst()) {
            assessment.setId(cursor.getInt(0));
            assessment.setType(cursor.getString(1));
            assessment.setTitle(cursor.getString(2));
            assessment.setDueDate(cursor.getString(3));
            assessment.setGoalDate(cursor.getString(4));
            assessment.setGoalDateAlert(cursor.getInt(5) > 0);
        }
        return assessment;
    }

    public ArrayList<Assessment> getAllAssessments() {
        ArrayList<Assessment> assessments = new ArrayList<>();
        final String query = "SELECT * FROM assessments;";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Assessment next = new Assessment();

                next.setId(cursor.getInt(0));
                next.setType(cursor.getString(1));
                next.setTitle(cursor.getString(2));
                next.setDueDate(cursor.getString(3));
                next.setGoalDate(cursor.getString(4));
                next.setGoalDateAlert(cursor.getInt(5) > 0);

                assessments.add(next);
            } while (cursor.moveToNext());
        }

        return assessments;
    }

}
