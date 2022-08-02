package wgu.com.bhasha.c196scheduler.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import wgu.com.bhasha.c196scheduler.R;
import wgu.com.bhasha.c196scheduler.data.Course;
import wgu.com.bhasha.c196scheduler.data.Term;

public class CourseAdapter extends ArrayAdapter<Course> {

    private Term term;
    private Context context;
    private List<Course> courses;
    private HashMap<Integer, Course> selectedCourses = new HashMap<>();

    public CourseAdapter(Term term, @NonNull Context context, int resource, ArrayList<Course> courses) {
        super(context, resource, courses);
        this.term = term;
        this.context = context;
        this.courses = courses;
        if (term != null) {
            term.getCourses().forEach(next -> selectedCourses.put(next.getId(), next));
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        CheckBox courseCheckbox = null;
        Course course = courses.get(position);

        if (view == null) {
            switch (context.getClass().getSimpleName()) {
                case "TermsDetailActivity":
                    view = LayoutInflater.from(context).inflate(R.layout.term_course_list, parent, false);
                    courseCheckbox = (CheckBox) view.findViewById(R.id.courseCheckbox);
                    break;
                case "CoursesOverviewActivity":
                    view = LayoutInflater.from(context).inflate(R.layout.course, parent, false);
                    break;
            }
        } else {
            switch (context.getClass().getSimpleName()) {
                case "TermsDetailActivity":
                    courseCheckbox = (CheckBox) view.findViewById(R.id.courseCheckbox);
                    break;
            }
        }

        if (courseCheckbox != null) {
            courseCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        course.setSelected(true);
                        selectedCourses.put(course.getId(), course);
                    } else {
                        course.setSelected(false);
                        selectedCourses.remove(course.getId());
                    }
                }
            });
            boolean selected = selectedCourses.get(course.getId()) != null ? selectedCourses.get(course.getId()).isSelected() : false;
            courseCheckbox.setChecked(selected);
        }

        ImageView courseTitleImageView = (ImageView) view.findViewById(R.id.courseTitleImageView);
        ImageView courseStatusImageView = (ImageView) view.findViewById(R.id.courseStatusImageView);
        ImageView courseAssessmentCountImageView = (ImageView) view.findViewById(R.id.assessmentCountImageView);
        ImageView courseMentorCountImageView = (ImageView) view.findViewById(R.id.mentorCountImageView);
        ImageView courseStartDateAlert = (ImageView) view.findViewById(R.id.courseStartDateAlert);
        ImageView courseEndDateAlert = (ImageView) view.findViewById(R.id.courseEndDateAlert);

        courseTitleImageView.setImageResource(R.drawable.course);
        courseStatusImageView.setImageResource(R.drawable.status);
        courseAssessmentCountImageView.setImageResource(R.drawable.assessment);
        courseMentorCountImageView.setImageResource(R.drawable.mentor);

        TextView courseTitle = (TextView) view.findViewById(R.id.courseTitle);
        TextView courseStatus = (TextView) view.findViewById(R.id.courseStatus);
        TextView courseStartDate = (TextView) view.findViewById(R.id.courseStartDate);
        TextView courseEndDate = (TextView) view.findViewById(R.id.courseEndDate);
        TextView courseAssessmentsCount = (TextView) view.findViewById(R.id.assessmentsCount);
        TextView courseMentorsCount = (TextView) view.findViewById(R.id.mentorsCount);

        TextView viewNotesTextView = (TextView) view.findViewById(R.id.viewNotesTextView);

        courseTitle.setText(course.getTitle());
        courseStatus.setText(course.getStatus());
        courseStartDate.setText("Start Date: " + course.getStartDate());
        courseEndDate.setText("End Date: " + course.getEndDate());
        courseAssessmentsCount.setText("" + course.getAssessments().size());
        courseMentorsCount.setText("" + course.getMentors().size());

        if (course.isStartDateAlert()) {
            courseStartDateAlert.setImageResource(R.drawable.alerts_on);
        } else {
            courseStartDateAlert.setImageResource(R.drawable.alerts_off);
        }

        if (course.isEndDateAlert()) {
            courseEndDateAlert.setImageResource(R.drawable.alerts_on);
        } else {
            courseEndDateAlert.setImageResource(R.drawable.alerts_off);
        }

        viewNotesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(v.getContext())
                        .setTitle("Notes")
                        .setMessage(course.getNotes())
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .show();
                TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                textView.setMaxLines(5);
                textView.setScroller(new Scroller(dialog.getContext()));
                textView.setVerticalScrollBarEnabled(true);
                textView.setMovementMethod(new ScrollingMovementMethod());
            }
        });
        return view;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public List<Course> getSelectedCourses() {
        return Arrays.asList(selectedCourses.values().toArray(new Course[selectedCourses.size()]));
    }

    public void cleanupHashMap() {
        selectedCourses.values().forEach(next -> next.setSelected(false));
        selectedCourses.clear();
    }

    @Override
    public int getCount() {
        return courses.size();
    }

    public Term getTerm() {
        return term;
    }

    public void setTerm(Term term) {
        this.term = term;
    }

}
