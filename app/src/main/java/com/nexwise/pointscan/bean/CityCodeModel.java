package com.nexwise.pointscan.bean;

import java.util.List;

public class CityCodeModel {
    private String code;
    private String name;
    private List<DistrictCodeModel> area;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DistrictCodeModel> getArea() {
        return area;
    }

    public void setArea(List<DistrictCodeModel> area) {
        this.area = area;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}


