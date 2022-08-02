package wgu.com.bhasha.c196scheduler;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import wgu.com.bhasha.c196scheduler.adapters.CourseAdapter;
import wgu.com.bhasha.c196scheduler.data.Course;
import wgu.com.bhasha.c196scheduler.managers.ActivityManager;

public class CoursesOverviewActivity extends ActivityManager {

    private CourseAdapter courseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses_overview);

        ListView coursesOverviewListView = (ListView) findViewById(R.id.courseOverviewListView);
        courseAdapter = new CourseAdapter(null, this, android.R.layout.simple_list_item_1,
                getDatabaseManager().getCourseManager().getAllCourses());
        coursesOverviewListView.setAdapter(courseAdapter);

        // Set click listener
        coursesOverviewListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), CoursesDetailActivity.class);
                intent.putExtra("course", (Course) coursesOverviewListView.getAdapter().getItem(position));
                startActivity(intent);
            }
        });

        // Setup add fab
        FloatingActionButton addMentorFab = (FloatingActionButton) findViewById(R.id.addBtn);
        addMentorFab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CoursesDetailActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onResume() {
        if (courseAdapter != null) {
            courseAdapter.clear();
            courseAdapter.addAll(getDatabaseManager().getCourseManager().getAllCourses());
            courseAdapter.notifyDataSetChanged();
        }
        super.onResume();
    }
}
