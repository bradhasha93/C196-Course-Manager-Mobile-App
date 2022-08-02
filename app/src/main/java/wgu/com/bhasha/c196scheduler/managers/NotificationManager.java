package wgu.com.bhasha.c196scheduler.managers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;

import wgu.com.bhasha.c196scheduler.R;
import wgu.com.bhasha.c196scheduler.data.Assessment;
import wgu.com.bhasha.c196scheduler.data.Course;
import wgu.com.bhasha.c196scheduler.receivers.NotificationReceiver;

public class NotificationManager {

    public static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yy");
    public static AtomicInteger notificationId = new AtomicInteger(0);

    public static int getNotificationId() {
        return notificationId.getAndIncrement();
    }

    public static void processAlerts(Context context, DatabaseManager databaseManager) {
        databaseManager.getAssessmentManager().getAllAssessments().forEach(next -> {
            if (next.isGoalDateAlert()) {
                addAssessmentNotification(context, next);
            }
        });

       databaseManager.getCourseManager().getAllCourses().forEach(next -> {
            if (next.isStartDateAlert()) {
                addCourseStartDateNotification(context, next);
            }
            if (next.isEndDateAlert()) {
                addCourseEndDateNotification(context, next);
            }
        });
    }

    public static void addAssessmentNotification(Context context, Assessment assessment) {
        final String date = assessment.getGoalDate();
        if (!date.equals("MM/DD/YYYY")) {
            Intent intent = new Intent(context, NotificationReceiver.class);
            IntentFilter intentFilter = new IntentFilter();
            intent.putExtra("icon", R.drawable.assessment);
            intent.putExtra("title", "Assessment Notification");
            intent.putExtra("text", assessment.getTitle() + " goal date is " + date);
            createAlarm(context, intent, date);
        }
    }

    public static void addCourseStartDateNotification(Context context, Course course) {
        final String date = course.getStartDate();
        if (!date.equals("MM/DD/YYYY")) {
            Intent intent = new Intent(context, NotificationReceiver.class);
            intent.putExtra("icon", R.drawable.course);
            intent.putExtra("title", "Course Notification");
            intent.putExtra("text", course.getTitle() + " starts " + date);
            createAlarm(context, intent, date);
        }
    }

    public static void addCourseEndDateNotification(Context context, Course course) {
        final String date = course.getEndDate();
        Log.d("heyheyhey123", course.getTitle());
        if (!date.equals("MM/DD/YYYY")) {
            Intent intent = new Intent(context, NotificationReceiver.class);
            intent.putExtra("icon", R.drawable.course);
            intent.putExtra("title", "Course Notification");
            intent.putExtra("text", course.getTitle() + " ends " + date);
            createAlarm(context, intent, date);
        }
    }

    private static void createAlarm(Context context, Intent intent, String date) {
        PendingIntent sender = PendingIntent.getBroadcast(context,NotificationManager.getNotificationId(),intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager=(AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(NotificationManager.simpleDateFormat.parse(date));
        } catch (ParseException pe) {

        }
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
    }
}
