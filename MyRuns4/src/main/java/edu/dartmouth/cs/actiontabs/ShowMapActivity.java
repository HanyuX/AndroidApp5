package edu.dartmouth.cs.actiontabs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class ShowMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private TextView status, avgSpeed, curSpeed, climb, calorie, distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_map);
        setUpMapIfNeeded();

        status = (TextView)findViewById(R.id.type_stats);
        avgSpeed = (TextView) findViewById(R.id.avg_speed);
        curSpeed = (TextView) findViewById(R.id.cur_speed);
        climb = (TextView) findViewById(R.id.climb);
        calorie = (TextView) findViewById(R.id.map_calorie);
        distance = (TextView) findViewById(R.id.map_distance);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String activityType = bundle.getString("ActivityType");
        status.setText("Type: " + activityType);
        double avgspeed = bundle.getDouble("AvgSpeed");
        avgSpeed.setText("Avg speed: " + avgspeed + " m/h");
        double curspeed = bundle.getDouble("CurSpeed");
        curSpeed.setText("Cur Speed: " + curspeed + " m/h");
        double tclimb = bundle.getDouble("Climb");
        climb.setText("Climb: " + tclimb + " Miles");
        int cal = bundle.getInt("Calories");
        calorie.setText("Calorie: " + cal);
        double dis = bundle.getDouble("Distance");
        distance.setText("Distance: " + dis + " Miles");

        List<LatLng> list = bundle.getParcelableArrayList("List");

    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = mapFragment.getMap();
            if (mMap != null) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                // Configure the map display options
            }
        }
        mapFragment.getMapAsync(this);
    }
}
