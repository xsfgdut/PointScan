package com.nexwise.lte.markerlibrary.base;


import com.nexwise.lte.markerlibrary.marker.BaseMarkerView;

/**
 * @author xsf
 * Created by xsf on 2019/10/15.
 */
public interface IMarkerView<W> {


    /**
     * 绑定数据
     *
     * @param infoWindowView BaseInfoWindowView
     */
    void bindInfoWindowView(BaseMarkerView.BaseInfoWindowView<W> infoWindowView);


    /**
     * 显示InfoWindow
     *
     * @param data W
     */
    void showInfoWindow(W data);

    /**
     * 隐藏InfoWindow
     */
    void hideInfoWindow();


}
