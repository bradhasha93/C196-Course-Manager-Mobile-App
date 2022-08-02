package wgu.com.bhasha.c196scheduler;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import wgu.com.bhasha.c196scheduler.managers.ActivityManager;
import wgu.com.bhasha.c196scheduler.managers.DatabaseManager;
import wgu.com.bhasha.c196scheduler.managers.NotificationManager;

public class MainActivity extends ActivityManager {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NotificationManager.processAlerts(getApplicationContext(), getDatabaseManager());

        // Set click listener to open mentors overview
        Button mentorsBtn = (Button) findViewById(R.id.mentorsBtn);
        mentorsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MentorsOverviewActivity.class);
                startActivity(intent);
            }
        });

        // Set click listener to open assessments overview
        Button assessmentsBtn = (Button) findViewById(R.id.assessmentsBtn);
        assessmentsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AssessmentsOverviewActivity.class);
                startActivity(intent);
            }
        });

        // Set click listener to open courses overview
        Button coursesBtn = (Button) findViewById(R.id.coursesBtn);
        coursesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CoursesOverviewActivity.class);
                startActivity(intent);
            }
        });

        // Set click listener to open terms overview
        Button termsBtn = (Button) findViewById(R.id.termsBtn);
        termsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TermsOverviewActivity.class);
                startActivity(intent);
            }
        });
    }
}
