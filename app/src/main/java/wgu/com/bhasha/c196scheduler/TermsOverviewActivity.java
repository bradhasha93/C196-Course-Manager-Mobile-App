package wgu.com.bhasha.c196scheduler;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import wgu.com.bhasha.c196scheduler.adapters.CourseAdapter;
import wgu.com.bhasha.c196scheduler.adapters.TermAdapter;
import wgu.com.bhasha.c196scheduler.data.Course;
import wgu.com.bhasha.c196scheduler.data.Term;
import wgu.com.bhasha.c196scheduler.managers.ActivityManager;
import wgu.com.bhasha.c196scheduler.managers.ListActivityManager;

public class TermsOverviewActivity extends ActivityManager {

    private TermAdapter termAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_overview);

        ListView termsOverviewListView = (ListView) findViewById(R.id.termsOverviewListView);
        termAdapter = new TermAdapter(this, android.R.layout.simple_list_item_1,
                getDatabaseManager().getTermManager().getAllTerms());
        termsOverviewListView.setAdapter(termAdapter);

        // Set click listener
        termsOverviewListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), TermsDetailActivity.class);
                intent.putExtra("term", (Term) termsOverviewListView.getAdapter().getItem(position));
                startActivity(intent);
            }
        });


        // Setup add fab
        FloatingActionButton addMentorFab = (FloatingActionButton) findViewById(R.id.addBtn);
        addMentorFab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TermsDetailActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        if (termAdapter != null) {
            termAdapter.clear();
            termAdapter.addAll(getDatabaseManager().getTermManager().getAllTerms());
            termAdapter.notifyDataSetChanged();
        }
        super.onResume();
    }
}
