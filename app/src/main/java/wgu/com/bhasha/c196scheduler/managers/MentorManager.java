package wgu.com.bhasha.c196scheduler.managers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import wgu.com.bhasha.c196scheduler.data.Mentor;

public class MentorManager {

    private SQLiteDatabase db;
    private final String table = "mentors";
    private final String course_mentors_table = "course_mentors";

    public MentorManager(SQLiteDatabase db) {
        this.db = db;
    }

    public int addMentor(Mentor mentor) {
        ContentValues values = new ContentValues();

        values.put("name", mentor.getName());
        values.put("phone", mentor.getPhone());
        values.put("email", mentor.getEmail());
        return (int) db.insert(table, null, values);
    }

    public void updateMentor(Mentor mentor) {
        ContentValues values = new ContentValues();

        values.put("name", mentor.getName());
        values.put("phone", mentor.getPhone());
        values.put("email", mentor.getEmail());
        db.update(table, values, "mentorId = ?", new String[]{String.valueOf(mentor.getId())});
    }

    public void deleteMentor(Mentor mentor) {
        db.delete(course_mentors_table, "mentorId = ?", new String[] {String.valueOf(mentor.getId())});
        db.delete(table, "mentorId = ?", new String[]{String.valueOf(mentor.getId())});
    }

    public Mentor getMentor(String mentorId) {
        ArrayList<Mentor> mentors = new ArrayList<>();
        final String query = "SELECT * FROM " + table + " WHERE mentorId = ?;";
        Cursor cursor = db.rawQuery(query, new String[]{mentorId});

        Mentor mentor = new Mentor();
        if (cursor.moveToFirst()) {

            mentor.setId(cursor.getInt(0));
            mentor.setName(cursor.getString(1));
            mentor.setPhone(cursor.getString(2));
            mentor.setEmail(cursor.getString(3));
        }

        return mentor;
    }


    public ArrayList<Mentor> getAllMentors() {
        ArrayList<Mentor> mentors = new ArrayList<>();
        final String query = "SELECT * FROM mentors;";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Mentor next = new Mentor();

                next.setId(cursor.getInt(0));
                next.setName(cursor.getString(1));
                next.setPhone(cursor.getString(2));
                next.setEmail(cursor.getString(3));

                mentors.add(next);
            } while (cursor.moveToNext());
        }

        return mentors;
    }

}
