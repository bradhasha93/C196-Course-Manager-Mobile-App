package wgu.com.bhasha.c196scheduler.managers;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class ListActivityManager extends ListActivity {

    private DatabaseManager dbm;

    @Override
    protected void onDestroy() {
        if (dbm != null) {
            dbm.close();
        }
        super.onDestroy();
    }

    /**
     * Return the database manager instance for the activity to use
     * @return
     */
    public DatabaseManager getDatabaseManager() {
        if (dbm == null) {
            dbm = new DatabaseManager(getApplicationContext());
            dbm.setDatabase();
        }
        return dbm;
    }
}
