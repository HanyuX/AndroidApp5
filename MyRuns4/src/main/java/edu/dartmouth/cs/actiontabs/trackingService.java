package edu.dartmouth.cs.actiontabs;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by xuehanyu on 4/27/16.
 */
public class trackingService extends Service {
    public static final int MSG_REGISTER_CLIENT = 1;
    public static final int MSG_UNREGISTER_CLIENT = 2;
    public static final int MSG_SET_INT_VALUE = 3;
    public static final int MSG_SET_STRING_VALUE = 4;
    private List<Messenger> mClients = new ArrayList<Messenger>();

    @Override
    public void onCreate(){
        super.onCreate();
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
        locationManager.requestLocationUpdates(provider, 2000, 10,
                locationListener);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            sendLocationtoMap(location);
        }

        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status,
                                    Bundle extras) {}
    };

    private void sendLocationtoMap(Location location){
        Iterator<Messenger> messengerIterator = mClients.iterator();
        while(messengerIterator.hasNext()) {
            Messenger messenger = messengerIterator.next();
            try {
//                location.get
            }catch (Exception e) {
                // The client is dead. Remove it from the list.
                mClients.remove(messenger);
            }
        }
    }

    private class IncomingMessageHandler extends Handler { // Handler of incoming messages from clients.
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_REGISTER_CLIENT:
                    mClients.add(msg.replyTo);
                    break;
                case MSG_UNREGISTER_CLIENT:
                    mClients.remove(msg.replyTo);
                    break;
                case MSG_SET_INT_VALUE:
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }
}
