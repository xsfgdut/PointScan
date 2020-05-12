package com.nexwise.pointscan.bean;

import java.io.Serializable;

public class User implements Serializable {
    String userName;//用户名
    String name;//姓名
    String tel;//联系号码
    String address;//地址
    String remark;//备注

    public User() {

    }

    public User(String userName, String name, String tel, String address, String remark) {
        this.userName = userName;
        this.name = name;
        this.tel = tel;
        this.address = address;
        this.remark = remark;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
