package com.nexwise.pointscan.bean;

import com.amap.api.maps.model.Marker;

import java.io.Serializable;
import java.util.List;

public class Point implements Serializable {
    String id;//站点编号
    String province;//省
    String city;//市
    String district;//区县
    String address;
    String name;//点位名称
    int state;//点位状态。1：未勘2：已勘3：已安装-1：已拆除(废除)
    double lng;//经度
    double lat;//纬度
    Marker marker;

    int geoType;//地理类型：1：卡口2：高速公路3：村庄
    String operator;//勘察人员
    long time;//勘察时间
    String location;//位置说明
    String env;//环境说明
    List<Device> devices;//预装设备信息
    List<Image> images;//点位图片信息
    List<Cell> cells;//无线环境小区

    public Point() {

    }

    public Point(String id, String province, String city, String district, String address, String name, int state, double lng, double lat, Marker marker,
                 int geoType, String operator, long time, String location, String env, List<Device> devices, List<Image> images, List<Cell> cells) {
        this.id = id;
        this.province = province;
        this.city = city;
        this.district = district;
        this.name = name;
        this.address = address;
        this.state = state;
        this.lng = lng;
        this.lat = lat;
        this.marker = marker;
        this.geoType = geoType;
        this.operator = operator;
        this.time = time;
        this.location = location;
        this.env = env;
        this.devices = devices;
        this.images = images;
        this.cells = cells;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public int getGeoType() {
        return geoType;
    }

    public void setGeoType(int geoType) {
        this.geoType = geoType;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public List<Cell> getCells() {
        return cells;
    }

    public void setCells(List<Cell> cells) {
        this.cells = cells;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
