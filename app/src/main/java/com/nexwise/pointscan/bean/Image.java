package com.nexwise.pointscan.bean;

import java.io.Serializable;

public class Image implements Serializable {
    int type;//图片类型：1：监控杆图2：地图截图
    String url;//图片访问url

    public Image() {

    }

    public Image(int type, String url) {
        this.type = type;
        this.url = url;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
