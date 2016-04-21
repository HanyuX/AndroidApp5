package edu.dartmouth.cs.actiontabs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

public class InfoActivity extends Activity {


    private EditText eType, eDate, eDuration, eDistance, eCalories, eHeartRate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String id = bundle.getString("ID");
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
                //delete the item

                finish();
                return true;
        }
        return false;
    }
}
