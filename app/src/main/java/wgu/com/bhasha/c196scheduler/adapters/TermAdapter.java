package wgu.com.bhasha.c196scheduler.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import wgu.com.bhasha.c196scheduler.R;
import wgu.com.bhasha.c196scheduler.data.Course;
import wgu.com.bhasha.c196scheduler.data.Term;

public class TermAdapter extends ArrayAdapter<Term> {

    private Term term;
    private Context context;
    private List<Term> terms;
    private HashMap<Integer, Course> selectedCourses = new HashMap<>();

    public TermAdapter(@NonNull Context context, int resource, ArrayList<Term> terms) {
        super(context, resource, terms);
        this.context = context;
        this.terms = terms;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        Term term = terms.get(position);

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.term, parent, false);
        }

        ImageView termImageView = (ImageView) view.findViewById(R.id.termImageView);
        ImageView termCourseImageView = (ImageView) view.findViewById(R.id.termCourseImageView);

        termImageView.setImageResource(R.drawable.term);
        termCourseImageView.setImageResource(R.drawable.course);

        TextView termTitle = (TextView) view.findViewById(R.id.termTitle);
        TextView termStartDate = (TextView) view.findViewById(R.id.termStartDate);
        TextView termEndDate = (TextView) view.findViewById(R.id.termEndDate);
        TextView termCourseCount = (TextView) view.findViewById(R.id.termCourseCount);

        termTitle.setText(term.getTitle());
        termStartDate.setText("Start Date: " + term.getStartDate());
        termEndDate.setText("End Date: " + term.getEndDate());
        termCourseCount.setText("" + term.getCourses().size());
        return view;
    }

    public List<Term> getTerms() {
        return terms;
    }

    public void setTerms(List<Term> terms) {
        this.terms = terms;
    }

    @Override
    public int getCount() {
        return terms.size();
    }
}
