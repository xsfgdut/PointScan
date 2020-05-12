package com.nexwise.pointscan.bean;

import java.io.Serializable;

public class Cell implements Serializable {
    int type;//运营商类型：1：移动2：联通3：电信
    String url;//扫频结果文件下载url

    public Cell() {

    }

    public Cell(int type, String url) {
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
