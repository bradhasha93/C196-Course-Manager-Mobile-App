package wgu.com.bhasha.c196scheduler.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
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

public class AssessmentAdapter extends ArrayAdapter<Assessment> {

    private Course course;
    private Context context;
    private List<Assessment> assessments;
    private HashMap<Integer, Assessment> selectedAssessments = new HashMap<>();

    public AssessmentAdapter(Course course, @NonNull Context context, int resource, ArrayList<Assessment> assessments) {
        super(context, resource, assessments);
        this.course = course;
        this.context = context;
        this.assessments = assessments;
        if (course != null) {
            course.getAssessments().forEach(next -> {
                selectedAssessments.put(next.getId(), next);
            });
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        CheckBox assessmentCheckbox = null;
        Assessment assessment = assessments.get(position);

        if (view == null) {
            switch (context.getClass().getSimpleName()) {
                case "CoursesDetailActivity":
                    view = LayoutInflater.from(context).inflate(R.layout.course_assessment_list, parent, false);
                    assessmentCheckbox = (CheckBox) view.findViewById(R.id.assessmentCheckbox);
                    break;
                case "AssessmentsOverviewActivity":
                    view = LayoutInflater.from(context).inflate(R.layout.assessment, parent, false);
                    break;
            }
        } else {
            switch (context.getClass().getSimpleName()) {
                case "CoursesDetailActivity":
                    assessmentCheckbox = (CheckBox) view.findViewById(R.id.assessmentCheckbox);
                    break;
            }
        }

        if (assessmentCheckbox != null) {
            assessmentCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        assessment.setSelected(true);
                        selectedAssessments.put(assessment.getId(), assessment);
                    } else {
                        assessment.setSelected(false);
                        selectedAssessments.remove(assessment.getId());
                    }
                }
            });
            boolean selected = selectedAssessments.get(assessment.getId()) != null ? selectedAssessments.get(assessment.getId()).isSelected() : false;
            assessmentCheckbox.setChecked(selected);
        }

        ImageView assessmentImageView = (ImageView) view.findViewById(R.id.assessmentImageView);
        ImageView assessmentTypeImageView = (ImageView) view.findViewById(R.id.assessmentTypeImageView);
        TextView assessmentTitle = (TextView) view.findViewById(R.id.assessmentTitle);
        TextView dueDate = (TextView) view.findViewById(R.id.dueDate);
        TextView goalDate = (TextView) view.findViewById(R.id.goalDate);
        ImageView alertsImageView = (ImageView) view.findViewById(R.id.alertsImageView);

        assessmentImageView.setImageResource(R.drawable.assessment);

        if (assessment.getType().equals("performance")) {
            assessmentTypeImageView.setImageResource(R.drawable.performance_assessment);
        } else if (assessment.getType().equals("objective")) {
            assessmentTypeImageView.setImageResource(R.drawable.objective_assessment);
        }

        assessmentTitle.setText(assessment.getTitle());
        dueDate.setText("Due Date: " + assessment.getDueDate());
        goalDate.setText("Goal Date: " + assessment.getGoalDate());

        if (assessment.isGoalDateAlert()) {
            alertsImageView.setImageResource(R.drawable.alerts_on);
        } else {
            alertsImageView.setImageResource(R.drawable.alerts_off);
        }

        return view;
    }

    public List<Assessment> getAssessments() {
        return assessments;
    }

    public void setAssessments(List<Assessment> assessments) {
        this.assessments = assessments;
    }

    public List<Assessment> getSelectedAssessments() {
        return Arrays.asList(selectedAssessments.values().toArray(new Assessment[selectedAssessments.size()]));
    }

    public void cleanupHashMap() {
        selectedAssessments.values().forEach(next -> next.setSelected(false));
        selectedAssessments.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return assessments.size();
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

}
