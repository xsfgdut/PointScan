package com.nexwise.pointscan.bean;

import java.io.Serializable;

public class Device implements Serializable {
    String id;//设备编号(ID)
    String devType;//设备型号
    int installType;//安装类型：1：新装2：替换
    int netType;//网络类型：1：公网无线回传2：专网
    String ip;//设备ip地址
    int antennaType;//天线类型：1：全向2：定向
    String antennaInfo;//天线朝向说明
    public Device() {

    }
    public Device(String id,String devType,int installType,int netType,String ip,int antennaType,String antennaInfo) {
        this.id = id;
        this.devType = devType;
        this.installType = installType;
        this.netType = netType;
        this.ip = ip;
        this.antennaType = antennaType;
        this.antennaInfo = antennaInfo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDevType() {
        return devType;
    }

    public void setDevType(String devType) {
        this.devType = devType;
    }

    public int getInstallType() {
        return installType;
    }

    public void setInstallType(int installType) {
        this.installType = installType;
    }

    public int getNetType() {
        return netType;
    }

    public void setNetType(int netType) {
        this.netType = netType;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getAntennaType() {
        return antennaType;
    }

    public void setAntennaType(int antennaType) {
        this.antennaType = antennaType;
    }

    public String getAntennaInfo() {
        return antennaInfo;
    }

    public void setAntennaInfo(String antennaInfo) {
        this.antennaInfo = antennaInfo;
    }
}
