package wgu.com.bhasha.c196scheduler;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.Calendar;

import wgu.com.bhasha.c196scheduler.data.Assessment;
import wgu.com.bhasha.c196scheduler.helpers.DatePickerHelper;
import wgu.com.bhasha.c196scheduler.managers.ActivityManager;
import wgu.com.bhasha.c196scheduler.managers.DatabaseManager;
import wgu.com.bhasha.c196scheduler.managers.NotificationManager;

public class AssessmentsDetailActivity extends ActivityManager {

    private Assessment assessment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessments_detail);

        // Setup date picker helpers
        DatePickerHelper assessmentDueDateHelper = new DatePickerHelper(AssessmentsDetailActivity.this,
                (TextView) findViewById(R.id.assessmentDueDateInputTextView));
        DatePickerHelper assessmentGoalDateHelper = new DatePickerHelper(AssessmentsDetailActivity.this,
                (TextView) findViewById(R.id.assessmentGoalDateInputTextView));

        FloatingActionButton deleteFab = (FloatingActionButton) findViewById(R.id.deleteFab);
        FloatingActionButton saveFab = (FloatingActionButton) findViewById(R.id.saveFab);

        RadioButton objectiveRadioBtn = (RadioButton) findViewById(R.id.objectiveRadioBtn);
        RadioButton performanceRadioBtn = (RadioButton) findViewById(R.id.performanceRadioBtn);
        EditText assessmentTitleEditText = (EditText) findViewById(R.id.assessmentTitleEditText);
        TextView assessmentDueDateInputTextView = (TextView) findViewById(R.id.assessmentDueDateInputTextView);
        TextView assessmentGoalDateInputTextView = (TextView) findViewById(R.id.assessmentGoalDateInputTextView);
        Switch assessmentGoalDateAlertSwitch = (Switch) findViewById(R.id.assessmentGoalDateAlertSwitch);

        DatabaseManager dbm = getDatabaseManager();

        Intent i = getIntent();
        if (i != null) {
            Assessment loadedAssessment = (Assessment) i.getSerializableExtra("assessment");

            if (loadedAssessment != null) {
                this.assessment = loadedAssessment;

                if (loadedAssessment.getType().equals("performance")) {
                    performanceRadioBtn.setChecked(true);
                } else if (loadedAssessment.getType().equals("objective")) {
                    objectiveRadioBtn.setChecked(true);
                }

                assessmentTitleEditText.setText(loadedAssessment.getTitle());
                assessmentDueDateInputTextView.setText(loadedAssessment.getDueDate());
                assessmentGoalDateInputTextView.setText(loadedAssessment.getGoalDate());
                assessmentGoalDateAlertSwitch.setChecked(loadedAssessment.isGoalDateAlert());
            } else {
                deleteFab.hide();
            }
        }

      saveFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (assessment == null) {
                    assessment = new Assessment();
                }

                if (performanceRadioBtn.isChecked()) {
                    assessment.setType("performance");
                } else if (objectiveRadioBtn.isChecked()) {
                    assessment.setType("objective");
                }

                assessment.setTitle(assessmentTitleEditText.getText().toString());
                assessment.setDueDate(assessmentDueDateInputTextView.getText().toString());
                assessment.setGoalDate(assessmentGoalDateInputTextView.getText().toString());


                assessment.setGoalDateAlert(assessmentGoalDateAlertSwitch.isChecked());

                if (assessment.getId() == -1) {
                    assessment.setId(dbm.getAssessmentManager().addAssessment(assessment));
                    Toast.makeText(getApplicationContext(), "Created new assessment " + assessment.getTitle(), Toast.LENGTH_LONG).show();
                    deleteFab.show();
                } else {
                    dbm.getAssessmentManager().updateAssessment(assessment);
                    Toast.makeText(getApplicationContext(), "Saved assessment " + assessment.getTitle(), Toast.LENGTH_LONG).show();
                }

                if (assessment.isGoalDateAlert()) {
                   NotificationManager.addAssessmentNotification(getApplicationContext(), assessment);
                }
            }
        });

        deleteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (assessment != null) {
                    if (assessment.getId() != -1) {
                        dbm.getAssessmentManager().deleteAssessment(assessment);
                        Toast.makeText(getApplicationContext(), "Deleted assessment " + assessment.getTitle(), Toast.LENGTH_LONG).show();
                        assessment = null;

                        performanceRadioBtn.setSelected(false);
                        objectiveRadioBtn.setSelected(false);
                        assessmentTitleEditText.setText("");
                        assessmentDueDateInputTextView.setText("");
                        assessmentGoalDateInputTextView.setText("");
                        assessmentGoalDateAlertSwitch.setChecked(false);

                        deleteFab.hide();
                    }
                }
            }
        });

    }
}
