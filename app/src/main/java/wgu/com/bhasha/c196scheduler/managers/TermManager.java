package wgu.com.bhasha.c196scheduler.managers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import wgu.com.bhasha.c196scheduler.data.Assessment;
import wgu.com.bhasha.c196scheduler.data.Course;
import wgu.com.bhasha.c196scheduler.data.Mentor;
import wgu.com.bhasha.c196scheduler.data.Term;

public class TermManager {

    private SQLiteDatabase db;
    private final String table = "terms";
    private final String term_courses_table = "term_courses";

    public TermManager(SQLiteDatabase db) {
        this.db = db;
    }

    public int addTerm(Term term) {
        ContentValues values = new ContentValues();

        values.put("title", term.getTitle());
        values.put("startDate", term.getStartDate());
        values.put("endDate", term.getEndDate());

        int termId = (int) db.insert(table, null, values);

        term.getCourses().forEach(next -> {
            ContentValues termCourseValues = new ContentValues();

            termCourseValues.put("termId", termId);
            termCourseValues.put("courseId", next.getId());

            db.insert(term_courses_table, null, termCourseValues);
        });
        return termId;
    }

    public void updateTerm(Term term) {
        ContentValues values = new ContentValues();

        values.put("title", term.getTitle());
        values.put("startDate", term.getStartDate());
        values.put("endDate", term.getEndDate());

        db.update(table, values, "termId = ?", new String[] {String.valueOf(term.getId())});
        db.delete(term_courses_table,"termId = ?", new String[] {String.valueOf(term.getId())});

        term.getCourses().forEach(next -> {
            ContentValues termCourseValues = new ContentValues();

            termCourseValues.put("termId", term.getId());
            termCourseValues.put("courseId", next.getId());

            db.insert(term_courses_table, null, termCourseValues);
        });
    }

    public void deleteTerm(Term term) {
        db.delete(term_courses_table, "termId = ?", new String[] {String.valueOf(term.getId())});
        db.delete(table, "termId = ?", new String[] {String.valueOf(term.getId())});
    }

    public ArrayList<Term> getAllTerms() {
        ArrayList<Term> terms = new ArrayList<>();
        final String query = "SELECT * FROM terms;";
        Cursor cursor = db.rawQuery(query, null);

        CourseManager courseManager = new CourseManager(db);

        if (cursor.moveToFirst()) {
            do {
                Term next = new Term();

                next.setId(cursor.getInt(0));
                next.setTitle(cursor.getString(1));
                next.setStartDate(cursor.getString(2));
                next.setEndDate(cursor.getString(3));

                final String termCoursesQuery = "SELECT courseId FROM term_courses WHERE termId = ?;";
                Cursor termCourseCursor = db.rawQuery(termCoursesQuery, new String[] {"" + next.getId()});
                if (termCourseCursor.moveToFirst()) {
                    do {
                        Course course = courseManager.getCourse(termCourseCursor.getString(0));
                        course.setSelected(true);

                        next.getCourses().add(course);
                    } while (termCourseCursor.moveToNext());
                }
                terms.add(next);
            } while (cursor.moveToNext());
        }

        return terms;
    }

}
