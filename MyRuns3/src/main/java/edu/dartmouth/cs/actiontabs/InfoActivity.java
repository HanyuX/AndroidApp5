package edu.dartmouth.cs.actiontabs;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.ArrayList;

public class InfoActivity extends Activity{


    private EditText eType, eDate, eDuration, eDistance, eCalories, eHeartRate;
    private String id;
    private DataBaseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        id = bundle.getString("ID");
        String activityType = bundle.getString("ActivityType");
        String dateTime = bundle.getString("DateTime");
        Double duration = bundle.getDouble("Duration");
        Double distance = bundle.getDouble("Distance");
        int calories = bundle.getInt("Calories");
        int heartrate = bundle.getInt("HeartRate");

        eType = (EditText) findViewById(R.id.info_type);
        eType.setText(activityType);

        eDate = (EditText) findViewById(R.id.info_datetime);
        eDate.setText(dateTime);

        eDuration = (EditText) findViewById(R.id.info_duration);
        int minute = duration.intValue();
        int second = (int)((duration - minute) * 60);
        eDuration.setText(minute + "mins " + second + "secs");

        eDistance = (EditText) findViewById(R.id.info_distance);
        eDistance.setText(distance + " Miles");

        eCalories = (EditText) findViewById(R.id.info_calories);
        eCalories.setText(calories + " cals");

        eHeartRate = (EditText) findViewById(R.id.info_heartrate);
        eHeartRate.setText(heartrate + " bpm");

        helper = new DataBaseHelper(getApplicationContext());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete:
                new asyncTask(id).execute();
                finish();
                return true;
        }
        return false;
    }

    class asyncTask extends AsyncTask<Void, Void, Void> {
        private String ID;

        public asyncTask(String ID){
            this.ID = ID;
        }
        @Override
        protected Void doInBackground(Void... params) {
            helper.deleteItem(ID);
            return null;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(Void result) {
        }
    }
}
