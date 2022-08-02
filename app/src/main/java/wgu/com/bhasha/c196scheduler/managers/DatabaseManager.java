package wgu.com.bhasha.c196scheduler.managers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import wgu.com.bhasha.c196scheduler.data.Course;
import wgu.com.bhasha.c196scheduler.data.Mentor;


public class DatabaseManager extends SQLiteOpenHelper {

    private static final String NAME = "c196";
    private static final int VERSION = 5;
    private static final String DELIMITER = ";";

    private SQLiteDatabase database;
    private Context context;

    private MentorManager mentorManager;
    private AssessmentManager assessmentManager;
    private CourseManager courseManager;
    private TermManager termManager;

    public DatabaseManager(Context context) {
        super(context, NAME, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try (Scanner scanner = new Scanner(context.getAssets().open("sql/create_database.sql")).useDelimiter(DELIMITER)) {
            scanner.forEachRemaining(next -> db.execSQL(next));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void setDatabase() {
        if (database == null) {
            database = getWritableDatabase();
        }
        mentorManager = new MentorManager(database);
        assessmentManager = new AssessmentManager(database);
        courseManager = new CourseManager(database);
        termManager = new TermManager(database);
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public MentorManager getMentorManager() {
        return mentorManager;
    }

    public AssessmentManager getAssessmentManager() {
        return assessmentManager;
    }

    public CourseManager getCourseManager() {
        return courseManager;
    }

    public TermManager getTermManager() {
        return termManager;
    }
}
