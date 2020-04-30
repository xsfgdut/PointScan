package com.nexwise.pointscan.bean;

import java.util.List;

public class ProvinceCodeModel {
    private String name;
    private String code;
    private List<CityCodeModel> city;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CityCodeModel> getCity() {
        return city;
    }

    public void setCity(List<CityCodeModel> city) {
        this.city = city;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

