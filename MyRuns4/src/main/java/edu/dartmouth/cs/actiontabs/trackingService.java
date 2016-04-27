package edu.dartmouth.cs.actiontabs;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import java.util.Calendar;

/**
 * Created by xuehanyu on 4/27/16.
 */
public class trackingService extends Service {
    private final IBinder mBinder = new trackingBinder();
    private mapItem mMapItem = new mapItem();
    private double lastAltitude = 0;
    private double lastLongtitude = 0;
    private double lastLatitude = 0;
    private Long startTime = Calendar.getInstance().getTimeInMillis();
    private double R = 6371.004*0.621371192;
    private double Pi = 3.1415926;
    @Override
    public void onCreate(){
        super.onCreate();
        Log.d("xue","create");
        LocationManager locationManager;
        String svcName= Context.LOCATION_SERVICE;
        locationManager = (LocationManager)getSystemService(svcName);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(true);
        String provider = locationManager.getBestProvider(criteria, true);

        Location l = locationManager.getLastKnownLocation(provider);
        sendLocationtoMap(l, true);
        locationManager.requestLocationUpdates(provider, 2000, 10,
                locationListener);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            sendLocationtoMap(location, false);
        }

        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status,
                                    Bundle extras) {}
    };

    private void sendLocationtoMap(Location location, boolean flag){
        LatLng l = new LatLng(location.getLatitude(), location.getLongitude());
        mMapItem.latlngs.add(l);
        mMapItem.curSpeed = location.getSpeed();
        if(flag){
            mMapItem.avgSpeed = 0;
            mMapItem.climb = 0;
            mMapItem.distance = 0;
        }else{
            double C = Math.sin(location.getLatitude())*Math.sin(lastLatitude)*Math.cos(location.getLongitude()-lastLongtitude)
                    + Math.cos(location.getLatitude())*Math.cos(lastLatitude);
            mMapItem.distance += Math.abs(R * Math.acos(C)*Pi/180);

            long nowTime = Calendar.getInstance().getTimeInMillis();
            mMapItem.avgSpeed = mMapItem.distance/((nowTime-startTime)/(3600*1000));

            double climb = location.getAltitude() - lastAltitude;
            mMapItem.climb += climb > 0 ? climb : 0;
        }
        lastAltitude = location.getAltitude();
        lastLatitude = location.getLatitude();
        lastLongtitude = location.getLongitude();
        Log.d("xue", "update");
        sendBroadcast(new Intent("Update_Location"));
    }

    public class trackingBinder extends Binder {
        public mapItem getItems() {
            return mMapItem;
        }
    }
}
