package edu.dartmouth.cs.actiontabs;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuehanyu on 4/27/16.
 */
public class mapItem {
    public List<LatLng> latlngs = new ArrayList<>();
    public double distance = 0;
    public double climb = 0;
    public double avgSpeed = 0;
    public double curSpeed = 0;
}
