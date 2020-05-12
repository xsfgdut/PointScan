package com.nexwise.pointscan.bean;

import java.io.Serializable;

public class DistrictCodeModel implements Serializable {

    private String code;
    private String name;

    public DistrictCodeModel() {
        super();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

