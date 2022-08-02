package wgu.com.bhasha.c196scheduler;

import android.app.ListActivity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import wgu.com.bhasha.c196scheduler.adapters.AssessmentAdapter;
import wgu.com.bhasha.c196scheduler.adapters.CourseAdapter;
import wgu.com.bhasha.c196scheduler.adapters.MentorAdapter;
import wgu.com.bhasha.c196scheduler.data.Course;
import wgu.com.bhasha.c196scheduler.data.Term;
import wgu.com.bhasha.c196scheduler.helpers.DatePickerHelper;
import wgu.com.bhasha.c196scheduler.managers.ActivityManager;
import wgu.com.bhasha.c196scheduler.managers.DatabaseManager;
import wgu.com.bhasha.c196scheduler.managers.ListActivityManager;

public class TermsDetailActivity extends ActivityManager {

    private CourseAdapter courseAdapter;
    private Term term;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_detail);

        EditText termTitleEditText = (EditText) findViewById(R.id.termTitleEditText);
        TextView termStartDateTextView = (TextView) findViewById(R.id.termStartDateInputTextView);
        TextView termEndDateTextView = (TextView) findViewById(R.id.termEndDateInputTextView);

        FloatingActionButton deleteFab = (FloatingActionButton) findViewById(R.id.deleteFab);
        FloatingActionButton saveFab = (FloatingActionButton) findViewById(R.id.saveFab);

        new DatePickerHelper(TermsDetailActivity.this, termStartDateTextView);
        new DatePickerHelper(TermsDetailActivity.this, termEndDateTextView);

        DatabaseManager dbm = getDatabaseManager();

        Intent i = getIntent();
        if (i != null) {
            Term loadedTerm = (Term) i.getSerializableExtra("term");

            if (loadedTerm != null) {
                this.term = loadedTerm;

                termTitleEditText.setText(loadedTerm.getTitle());
                termStartDateTextView.setText(loadedTerm.getStartDate());
                termEndDateTextView.setText(loadedTerm.getEndDate());
            } else {
                deleteFab.hide();
            }
        }

        ListView termCoursesListView = (ListView) findViewById(R.id.termCoursesListView);
        courseAdapter = new CourseAdapter(term,this, android.R.layout.simple_list_item_multiple_choice,
                getDatabaseManager().getCourseManager().getAllCourses());
        termCoursesListView.setAdapter(courseAdapter);

        saveFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (term == null) {
                    term = new Term();
                }

                term.setTitle(termTitleEditText.getText().toString());
                term.setStartDate(termStartDateTextView.getText().toString());
                term.setEndDate(termEndDateTextView.getText().toString());

                term.getCourses().clear();
                term.getCourses().addAll(courseAdapter.getSelectedCourses());

                if (term.getId() == -1) {
                    term.setId(dbm.getTermManager().addTerm(term));
                    Toast.makeText(getApplicationContext(), "Created new term " + term.getTitle(), Toast.LENGTH_LONG).show();
                    deleteFab.show();
                } else {
                    dbm.getTermManager().updateTerm(term);
                    Toast.makeText(getApplicationContext(), "Saved term " + term.getTitle(), Toast.LENGTH_LONG).show();
                }
            }
        });

        deleteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (term != null) {
                    if (term.getId() != -1) {

                        if (term.getCourses().size() > 0) {
                            Toast.makeText(TermsDetailActivity.this, "You cannot delete a term that has associated courses.", Toast.LENGTH_LONG).show();
                        } else {
                            dbm.getTermManager().deleteTerm(term);
                            Toast.makeText(getApplicationContext(), "Deleted term " + term.getTitle(), Toast.LENGTH_LONG).show();
                            term = null;

                            termTitleEditText.setText("");
                            termStartDateTextView.setText("");
                            termEndDateTextView.setText("");
                            courseAdapter.cleanupHashMap();
                            onResume();

                            deleteFab.hide();
                        }
                    }
                }
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
