package wgu.com.bhasha.c196scheduler.data;

import java.io.Serializable;

public class Assessment implements Serializable {

    private int id = -1;
    private String type;
    private String title;
    private String dueDate;
    private String goalDate;
    private boolean goalDateAlert;
    private boolean isSelected;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type == null ? "" : type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getGoalDate() {
        return goalDate;
    }

    public void setGoalDate(String goalDate) {
        this.goalDate = goalDate;
    }

    public boolean isGoalDateAlert() {
        return goalDateAlert;
    }

    public void setGoalDateAlert(boolean goalDateAlert) {
        this.goalDateAlert = goalDateAlert;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
