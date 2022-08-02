package wgu.com.bhasha.c196scheduler.data;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Course implements Serializable {

    private int id = -1;
    private String title;
    private String status;
    private String startDate;
    private boolean startDateAlert;
    private String endDate;
    private boolean endDateAlert;
    private String notes;
    private ArrayList<Mentor> mentors = new ArrayList<>();
    private ArrayList<Assessment> assessments = new ArrayList<>();
    private boolean isSelected;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public boolean isStartDateAlert() {
        return startDateAlert;
    }

    public void setStartDateAlert(boolean startDateAlert) {
        this.startDateAlert = startDateAlert;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public boolean isEndDateAlert() {
        return endDateAlert;
    }

    public void setEndDateAlert(boolean endDateAlert) {
        this.endDateAlert = endDateAlert;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public ArrayList<Mentor> getMentors() {
        return mentors;
    }

    public void setMentors(ArrayList<Mentor> mentors) {
        this.mentors = mentors;
    }

    public ArrayList<Assessment> getAssessments() {
        return assessments;
    }

    public void setAssessments(ArrayList<Assessment> assessments) {
        this.assessments = assessments;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
