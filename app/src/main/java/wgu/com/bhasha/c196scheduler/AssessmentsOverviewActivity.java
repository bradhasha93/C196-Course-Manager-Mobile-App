package wgu.com.bhasha.c196scheduler;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import wgu.com.bhasha.c196scheduler.adapters.AssessmentAdapter;
import wgu.com.bhasha.c196scheduler.adapters.CourseAdapter;
import wgu.com.bhasha.c196scheduler.data.Assessment;
import wgu.com.bhasha.c196scheduler.managers.ActivityManager;
import wgu.com.bhasha.c196scheduler.managers.ListActivityManager;

public class AssessmentsOverviewActivity extends ActivityManager {

    private AssessmentAdapter assessmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessments_overview);

        ListView assessmentsOverviewListView = (ListView) findViewById(R.id.assessmentsOverviewListView);
        assessmentAdapter = new AssessmentAdapter(null, this, android.R.layout.simple_list_item_1,
                getDatabaseManager().getAssessmentManager().getAllAssessments());
        assessmentsOverviewListView.setAdapter(assessmentAdapter);

        // Set click listener
        assessmentsOverviewListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), AssessmentsDetailActivity.class);
                intent.putExtra("assessment", (Assessment) assessmentsOverviewListView.getAdapter().getItem(position));
                startActivity(intent);
            }
        });

        // Setup add fabs
        FloatingActionButton addMentorFab = (FloatingActionButton) findViewById(R.id.addBtn);
        addMentorFab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AssessmentsDetailActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        if (assessmentAdapter != null) {
            assessmentAdapter.clear();
            assessmentAdapter.addAll(getDatabaseManager().getAssessmentManager().getAllAssessments());
            assessmentAdapter.notifyDataSetChanged();
        }
        super.onResume();
    }
}
