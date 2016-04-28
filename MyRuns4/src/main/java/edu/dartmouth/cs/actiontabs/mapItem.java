package edu.dartmouth.cs.actiontabs;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuehanyu on 4/27/16.
 */
public class mapItem {
    private List<LatLng> latlngs = new ArrayList<>();
    private double distance = 0;
    private double climb = 0;
    private double avgSpeed = 0;
    private double curSpeed = 0;

    public List<LatLng> getLatlngs() {
        return latlngs;
    }

    public void setLatlngs(List<LatLng> latlngs) {
        this.latlngs = latlngs;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getClimb() {
        return climb;
    }

    public void setClimb(double climb) {
        this.climb = climb;
    }

    public double getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(double avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public double getCurSpeed() {
        return curSpeed;
    }

    public void setCurSpeed(double curSpeed) {
        this.curSpeed = curSpeed;
    }
}
