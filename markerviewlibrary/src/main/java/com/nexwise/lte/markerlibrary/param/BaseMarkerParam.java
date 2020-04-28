package com.nexwise.lte.markerlibrary.param;

import com.amap.api.maps.model.MarkerOptions;

/**
 * @author xsf
 * Created by xsf on 2019/10/18.
 */
public class BaseMarkerParam {


    private MarkerOptions options = new MarkerOptions();


    public MarkerOptions getOptions() {
        return options;
    }

    public void setOptions(MarkerOptions options) {
        this.options = options;
    }


}
