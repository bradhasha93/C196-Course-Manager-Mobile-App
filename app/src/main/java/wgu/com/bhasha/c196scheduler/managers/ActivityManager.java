package wgu.com.bhasha.c196scheduler.managers;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class ActivityManager extends AppCompatActivity {

    private DatabaseManager dbm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (!getClass().getSimpleName().equals("MainActivity")) {
            ActionBar actionBar = getSupportActionBar(); // or getActionBar();
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        if (dbm != null) {
            dbm.close();
        }
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return true;
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
