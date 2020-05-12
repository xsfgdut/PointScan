package com.nexwise.pointscan.bean;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;

public class MarkerBean {
    Marker marker;
    LatLng latLng;
    String title;
    int state;

    public MarkerBean() {
    }

    public MarkerBean(Marker marker, LatLng latLng, String title, int state) {
        this.marker = marker;
        this.latLng = latLng;
        this.title = title;
        this.state = state;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
