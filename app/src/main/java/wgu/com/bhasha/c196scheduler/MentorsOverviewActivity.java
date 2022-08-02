package wgu.com.bhasha.c196scheduler;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import wgu.com.bhasha.c196scheduler.adapters.CourseAdapter;
import wgu.com.bhasha.c196scheduler.adapters.MentorAdapter;
import wgu.com.bhasha.c196scheduler.data.Mentor;
import wgu.com.bhasha.c196scheduler.managers.ActivityManager;
import wgu.com.bhasha.c196scheduler.managers.ListActivityManager;


public class MentorsOverviewActivity extends ActivityManager {

    private MentorAdapter mentorsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentors_overview);

        ListView mentorsOverviewListView = (ListView) findViewById(R.id.mentorsOverviewListView);
        mentorsAdapter = new MentorAdapter(null, this, android.R.layout.simple_list_item_1,
                getDatabaseManager().getMentorManager().getAllMentors());
        mentorsOverviewListView.setAdapter(mentorsAdapter);

        // Set click listener
        mentorsOverviewListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), MentorsDetailActivity.class);
                intent.putExtra("mentor", (Mentor) mentorsOverviewListView.getAdapter().getItem(position));
                startActivity(intent);
            }
        });

        // Setup add fab
        FloatingActionButton addMentorFab = (FloatingActionButton) findViewById(R.id.addBtn);
        addMentorFab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MentorsDetailActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        if (mentorsAdapter != null) {
            mentorsAdapter.clear();
            mentorsAdapter.addAll(getDatabaseManager().getMentorManager().getAllMentors());
            mentorsAdapter.notifyDataSetChanged();
        }
        super.onResume();
    }
}
