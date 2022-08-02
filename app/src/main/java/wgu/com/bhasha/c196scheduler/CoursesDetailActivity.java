package wgu.com.bhasha.c196scheduler;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import wgu.com.bhasha.c196scheduler.adapters.AssessmentAdapter;
import wgu.com.bhasha.c196scheduler.adapters.MentorAdapter;
import wgu.com.bhasha.c196scheduler.data.Course;
import wgu.com.bhasha.c196scheduler.helpers.DatePickerHelper;
import wgu.com.bhasha.c196scheduler.managers.ActivityManager;
import wgu.com.bhasha.c196scheduler.managers.DatabaseManager;
import wgu.com.bhasha.c196scheduler.managers.NotificationManager;

public class CoursesDetailActivity extends ActivityManager {

    private MentorAdapter mentorsAdapter;
    private AssessmentAdapter assessmentsAdapter;
    private Course course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses_detail);

        EditText courseTitleEditText = (EditText) findViewById(R.id.courseTitleEditText);
        EditText courseStatusEditText = (EditText) findViewById(R.id.courseStatusEditText);

        TextView courseStartDateInputTextView = (TextView) findViewById(R.id.courseStartDateInputTextView);
        new DatePickerHelper(CoursesDetailActivity.this, courseStartDateInputTextView);

        Switch courseStartDateAlertSwitch = (Switch) findViewById(R.id.courseStartDateAlertSwitch);

        TextView courseEndDateInputTextView = (TextView) findViewById(R.id.courseEndDateInputTextView);
        new DatePickerHelper(CoursesDetailActivity.this, courseEndDateInputTextView);

        Switch courseEndDateAlertSwitch = (Switch) findViewById(R.id.courseEndDateAlertSwitch);

        EditText courseNotesInputEditText = (EditText) findViewById(R.id.courseNotesInputEditText);
        Button courseNotesShareBtn = (Button) findViewById(R.id.courseNotesShareBtn);

        ListView coursesAssessmentsListView = (ListView) findViewById(R.id.courseAssessmentsListView);
        ListView coursesMentorsListView = (ListView) findViewById(R.id.courseMentorsListView);

        FloatingActionButton deleteFab = (FloatingActionButton) findViewById(R.id.deleteFab);
        FloatingActionButton saveFab = (FloatingActionButton) findViewById(R.id.saveFab);

        DatabaseManager dbm = getDatabaseManager();

        Intent i = getIntent();
        if (i != null) {
            Course loadedCourse = (Course) i.getSerializableExtra("course");

            if (loadedCourse != null) {
                this.course = loadedCourse;

                courseTitleEditText.setText(loadedCourse.getTitle());
                courseStatusEditText.setText(loadedCourse.getStatus());
                courseStartDateInputTextView.setText(loadedCourse.getStartDate());
                courseStartDateAlertSwitch.setChecked(loadedCourse.isStartDateAlert());
                courseEndDateInputTextView.setText(loadedCourse.getEndDate());
                courseEndDateAlertSwitch.setChecked(loadedCourse.isEndDateAlert());
                courseNotesInputEditText.setText(loadedCourse.getNotes());
            } else {
                deleteFab.hide();
            }
        }

        assessmentsAdapter = new AssessmentAdapter(course, this, android.R.layout.simple_list_item_multiple_choice,
                getDatabaseManager().getAssessmentManager().getAllAssessments());
        coursesAssessmentsListView.setAdapter(assessmentsAdapter);

        mentorsAdapter = new MentorAdapter(course, this, android.R.layout.simple_list_item_multiple_choice,
                getDatabaseManager().getMentorManager().getAllMentors());
        coursesMentorsListView.setAdapter(mentorsAdapter);

        courseNotesShareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String notes = courseNotesInputEditText.getText().toString();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, notes);
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, sendIntent.getExtras().get(Intent.EXTRA_TEXT).toString()));
            }
        });

        saveFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (course == null) {
                    course = new Course();
                }

                course.setTitle(courseTitleEditText.getText().toString());
                course.setStatus(courseStatusEditText.getText().toString());
                course.setStartDate(courseStartDateInputTextView.getText().toString());
                course.setStartDateAlert(courseStartDateAlertSwitch.isChecked());
                course.setEndDate(courseEndDateInputTextView.getText().toString());
                course.setEndDateAlert(courseEndDateAlertSwitch.isChecked());
                course.setNotes(courseNotesInputEditText.getText().toString());


                course.getAssessments().clear();
                course.getAssessments().addAll(assessmentsAdapter.getSelectedAssessments());

                course.getMentors().clear();
                ;
                course.getMentors().addAll(mentorsAdapter.getSelectedMentors());

                if (course.getId() == -1) {
                    course.setId(dbm.getCourseManager().addCourse(course));
                    Toast.makeText(getApplicationContext(), "Created new course " + course.getTitle(), Toast.LENGTH_LONG).show();
                    deleteFab.show();
                } else {
                    dbm.getCourseManager().updateCourse(course);
                    Toast.makeText(getApplicationContext(), "Saved course " + course.getTitle(), Toast.LENGTH_LONG).show();
                }

                if (course.isStartDateAlert()) {
                    NotificationManager.addCourseStartDateNotification(getApplicationContext(), course);
                }

                if (course.isEndDateAlert()) {
                    NotificationManager.addCourseEndDateNotification(getApplicationContext(), course);
                }
            }
        });

        deleteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (course != null) {
                    if (course.getId() != -1) {
                        dbm.getCourseManager().deleteCourse(course);
                        Toast.makeText(getApplicationContext(), "Deleted course " + course.getTitle(), Toast.LENGTH_LONG).show();
                        course = null;

                        courseTitleEditText.setText("");
                        courseStatusEditText.setText("");
                        courseStartDateInputTextView.setText("");
                        courseStartDateAlertSwitch.setChecked(false);
                        courseEndDateInputTextView.setText("");
                        courseEndDateAlertSwitch.setChecked(false);
                        courseNotesInputEditText.setText("");
                        assessmentsAdapter.cleanupHashMap();
                        mentorsAdapter.cleanupHashMap();
                        onResume();

                        deleteFab.hide();
                    }
                }
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
        if (assessmentsAdapter != null) {
            assessmentsAdapter.clear();
            assessmentsAdapter.addAll(getDatabaseManager().getAssessmentManager().getAllAssessments());
        }
        super.onResume();
    }
}
