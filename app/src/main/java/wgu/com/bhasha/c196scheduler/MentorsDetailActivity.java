package wgu.com.bhasha.c196scheduler;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import wgu.com.bhasha.c196scheduler.data.Mentor;
import wgu.com.bhasha.c196scheduler.managers.ActivityManager;
import wgu.com.bhasha.c196scheduler.managers.DatabaseManager;

public class MentorsDetailActivity extends ActivityManager {

    private Mentor mentor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentors_detail);

        FloatingActionButton deleteFab = (FloatingActionButton) findViewById(R.id.deleteFab);
        FloatingActionButton saveFab = (FloatingActionButton) findViewById(R.id.saveFab);

        EditText mentorNameEditText = (EditText) findViewById(R.id.mentorNameEditText);
        EditText mentorPhoneEditText = (EditText) findViewById(R.id.mentorPhoneEditText);
        EditText mentorEmailEditText = (EditText) findViewById(R.id.mentorEmailEditText);
        DatabaseManager dbm = getDatabaseManager();

        Intent i = getIntent();
        if (i != null) {
            Mentor loadedMentor = (Mentor) i.getSerializableExtra("mentor");

            if (loadedMentor != null) {
                this.mentor = loadedMentor;

                mentorNameEditText.setText(loadedMentor.getName());
                mentorPhoneEditText.setText(loadedMentor.getPhone());
                mentorEmailEditText.setText(loadedMentor.getEmail());
            } else {
                deleteFab.hide();
            }
        }

        saveFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "toast", Toast.LENGTH_SHORT);
                if (mentor == null) {
                    mentor = new Mentor();
                }
                mentor.setName(mentorNameEditText.getText().toString());
                mentor.setPhone(mentorPhoneEditText.getText().toString());
                mentor.setEmail(mentorEmailEditText.getText().toString());

                if (mentor.getId() == -1) {
                    mentor.setId(dbm.getMentorManager().addMentor(mentor));
                    Toast.makeText(getApplicationContext(), "Created new mentor " + mentor.getName(), Toast.LENGTH_LONG).show();
                    deleteFab.show();
                } else {
                    dbm.getMentorManager().updateMentor(mentor);
                    Toast.makeText(getApplicationContext(), "Saved mentor " + mentor.getName(), Toast.LENGTH_LONG).show();
                }
            }
        });

        deleteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mentor != null) {
                    if (mentor.getId() != -1) {
                        dbm.getMentorManager().deleteMentor(mentor);
                        Toast.makeText(getApplicationContext(), "Deleted mentor " + mentor.getName(), Toast.LENGTH_LONG).show();
                        mentor = null;
                        mentorNameEditText.setText("");
                        mentorPhoneEditText.setText("");
                        mentorEmailEditText.setText("");
                        deleteFab.hide();
                    }
                }
            }
        });
    }
}
