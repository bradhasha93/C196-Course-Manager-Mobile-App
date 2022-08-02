package wgu.com.bhasha.c196scheduler.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import wgu.com.bhasha.c196scheduler.R;
import wgu.com.bhasha.c196scheduler.data.Assessment;
import wgu.com.bhasha.c196scheduler.data.Course;
import wgu.com.bhasha.c196scheduler.data.Mentor;

public class MentorAdapter extends ArrayAdapter<Mentor> {

    private Course course;
    private Context context;
    private List<Mentor> mentors;
    private HashMap<Integer, Mentor> selectedMentors = new HashMap<>();

    public MentorAdapter(Course course, @NonNull Context context, int resource, ArrayList<Mentor> mentors) {
        super(context, resource, mentors);
        this.course = course;
        this.context = context;
        this.mentors = mentors;
        if (course != null) {
            course.getMentors().forEach(next -> selectedMentors.put(next.getId(), next));
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        CheckBox mentorCheckbox = null;

        Mentor mentor = mentors.get(position);

        if (view == null) {
            switch (context.getClass().getSimpleName()) {
                case "CoursesDetailActivity":
                    view = LayoutInflater.from(context).inflate(R.layout.course_mentors_list, parent, false);
                    mentorCheckbox = (CheckBox) view.findViewById(R.id.mentorCheckbox);
                    break;
                case "MentorsOverviewActivity":
                    view = LayoutInflater.from(context).inflate(R.layout.mentor, parent, false);
                    break;
            }
        } else {
            switch (context.getClass().getSimpleName()) {
                case "CoursesDetailActivity":
                    mentorCheckbox = (CheckBox) view.findViewById(R.id.mentorCheckbox);
                    break;
            }
        }


        if (mentorCheckbox != null) {
            mentorCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        mentor.setSelected(true);
                        selectedMentors.put(mentor.getId(), mentor);
                    } else {
                        mentor.setSelected(false);
                        selectedMentors.remove(mentor.getId());
                    }
                }
            });
            boolean selected = selectedMentors.get(mentor.getId()) != null ? selectedMentors.get(mentor.getId()).isSelected() : false;
            mentorCheckbox.setChecked(selected);
        }

        TextView mentorNameTextView = view.findViewById(R.id.mentorNameTextView);
        mentorNameTextView.setText(mentor.getName());

        TextView mentorPhoneTextView = view.findViewById(R.id.mentorPhoneTextView);
        mentorPhoneTextView.setText(mentor.getPhone());

        TextView mentorEmailTextView = view.findViewById(R.id.mentorEmailTextView);
        mentorEmailTextView.setText(mentor.getEmail());

        ImageView mentorImageView = view.findViewById(R.id.mentorImageView);
        mentorImageView.setImageResource(R.drawable.mentor);

        ImageView phoneImageView = view.findViewById(R.id.phoneImageView);
        phoneImageView.setImageResource(R.drawable.phone);

        ImageView emailImageView = view.findViewById(R.id.emailImageView);
        emailImageView.setImageResource(R.drawable.email);

        return view;
    }

    public List<Mentor> getMentors() {
        return mentors;
    }

    public void setMentors(List<Mentor> mentors) {
        this.mentors = mentors;
    }

    public List<Mentor> getSelectedMentors() {
        return Arrays.asList(selectedMentors.values().toArray(new Mentor[selectedMentors.size()]));
    }

    public void cleanupHashMap() {
        selectedMentors.values().forEach(next -> next.setSelected(false));
        selectedMentors.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mentors.size();
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
