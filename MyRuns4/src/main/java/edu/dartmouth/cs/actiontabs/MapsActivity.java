package edu.dartmouth.cs.actiontabs;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.Calendar;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, ServiceConnection {

    private GoogleMap mMap;
    private ServiceConnection mConnection = this;
    private trackingService.trackingBinder binder;
    private databaseItem item;
    private TextView status, avgSpeed, curSpeed, climb, calorie, distance;
    private String type, inputType;
    private PolylineOptions rectOptions;
    private DataBaseHelper helper;
    private Calendar mDateAndTime = Calendar.getInstance();
    private long startTime;
    private Marker startMarker, endMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        setUpMapIfNeeded();
        Intent intent = getIntent();
        type = intent.getStringExtra("ActivityType");
        inputType = intent.getStringExtra("InputType");
        System.out.println(inputType);
        status = (TextView)findViewById(R.id.type_stats);
        avgSpeed = (TextView) findViewById(R.id.avg_speed);
        curSpeed = (TextView) findViewById(R.id.cur_speed);
        climb = (TextView) findViewById(R.id.climb);
        calorie = (TextView) findViewById(R.id.map_calorie);
        distance = (TextView) findViewById(R.id.map_distance);
        helper = new DataBaseHelper(getApplicationContext());
    }

    private BroadcastReceiver onEvent = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent i) {
            Log.d("liu", "receive notification");
            if (binder != null) {
                item = binder.getItems();
                status.setText("Type: " + type);
                avgSpeed.setText("Avg speed: " + item.AvgSpeed + "m/h");
                curSpeed.setText("Cur Speed: " + item.CurSpeed + "m/h");
                climb.setText("Climb: " + item.Climb + " Miles");
                int cal = (int)(item.Distance * 99.456);
                calorie.setText("Calorie: " + cal);
                distance.setText("Distance: " + item.Distance + " Miles");
                LatLng loc = item.Latlngs.get(item.Latlngs.size() - 1);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc,
                        17));
                rectOptions.add(loc);
                rectOptions.color(Color.BLACK);
                mMap.addPolyline(rectOptions);
                rectOptions = new PolylineOptions().add(loc);
                if (endMarker != null) {
                    endMarker.remove();
                }
                endMarker = mMap.addMarker(new MarkerOptions().position(loc).icon(BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_RED)));
            }
        }
    };

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
        Log.d("xue", "mapCreate");
        bindService(new Intent(this, trackingService.class), mConnection, Context.BIND_AUTO_CREATE);
        IntentFilter filter = new IntentFilter(trackingService.ACTION_UPDATE);
        registerReceiver(onEvent, filter);
        startTime = Calendar.getInstance().getTimeInMillis();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = mapFragment.getMap();
            if (mMap != null) {
                setUpMap();
                // Configure the map display options
            }
        }
        mapFragment.getMapAsync(this);
    }

    private void setUpMap() {
        mMap.setMyLocationEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service){
        Log.d("xue","connected");
        binder = (trackingService.trackingBinder)service;
        item = binder.getItems();
        status.setText("Type: " + type);
        avgSpeed.setText("Avg speed: " + item.AvgSpeed + "m/h");
        curSpeed.setText("Cur Speed: " + item.CurSpeed + "m/h");
        climb.setText("Climb: " + item.Climb + " Miles");
        int cal = (int)(item.Distance * 99.456);
        calorie.setText("Calorie: " + cal);
        distance.setText("Distance: " + item.Distance + " Miles");
        LatLng loc = item.Latlngs.get(item.Latlngs.size() - 1);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc,
                17));
        rectOptions = new PolylineOptions().add(loc);
        startMarker = mMap.addMarker(new MarkerOptions().position(loc).icon(BitmapDescriptorFactory.defaultMarker(
                BitmapDescriptorFactory.HUE_GREEN)));
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        status.setText("Type: " + type);
        avgSpeed.setText("Avg speed: 0 m/h");
        curSpeed.setText("Cur Speed: 0 m/h");
        climb.setText("Climb: 0 Miles");
        calorie.setText("Calorie: 0");
        distance.setText("Distance: 0 Miles");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        unbindService(mConnection);
        unregisterReceiver(onEvent);
    }

    public void saveMap(View view) {
        item.ActivityType = type;
        item.InputType = inputType;
        item.ID = System.currentTimeMillis()+"-"+item.InputType+"-"+item.ActivityType;
        item.Date = mDateAndTime.get(Calendar.YEAR) +"-"+ (mDateAndTime.get(Calendar.MONTH)+1) +"-"+ mDateAndTime.get(Calendar.DAY_OF_MONTH);
        item.Time = mDateAndTime.get(Calendar.HOUR_OF_DAY) +":"+ mDateAndTime.get(Calendar.MINUTE) +":"+
                (mDateAndTime.get(Calendar.SECOND) == 0 ? "00" : mDateAndTime.get(Calendar.SECOND));
        long nowTime = Calendar.getInstance().getTimeInMillis();
        item.Duration = (nowTime - startTime) / (1000 * 60 * 1.0);
        new asyncTask(item).execute();
        finish();
    }

    public void cancelMap(View view) {
        finish();
    }

    class asyncTask extends AsyncTask<Void, Void, Integer> {
        private databaseItem item;

        public asyncTask(databaseItem item){
            this.item = item;
        }
        @Override
        protected Integer doInBackground(Void... params) {
            helper.addItem(item);
            return helper.allItems().size();
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(Integer result) {
            Toast.makeText(getApplicationContext(), "Entry #"+ result + " saved.",
                    Toast.LENGTH_SHORT).show();
        }
    }
}