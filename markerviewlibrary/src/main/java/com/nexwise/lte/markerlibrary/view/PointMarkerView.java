package com.nexwise.lte.markerlibrary.view;

import android.content.Context;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.nexwise.lte.markerlibrary.base.BaseMarkerBuilder;
import com.nexwise.lte.markerlibrary.marker.BaseMarkerView;
import com.nexwise.lte.markerlibrary.param.PointMarkerParam;


/**
 * @author xsf
 * Created by xsf on 2019/10/23.
 */
public class PointMarkerView<W> extends BaseMarkerView<PointMarkerParam, W> {


    private PointMarkerView(Context context, AMap amap, PointMarkerParam param) {
        super(context, amap, param);
    }


    @Override
    public void addToMap() {
        if (isRemove()) {
            super.addToMap();
        }
    }


    @Override
    public void removeFromMap() {
        super.removeFromMap();
    }


    @Override
    public int getCameraPaddingTop() {
        int paddingTop = super.getCameraPaddingTop();
        return paddingTop;
    }

    @Override
    public int getCameraPaddingLeft() {
        int paddingLeft = super.getCameraPaddingLeft();
        return paddingLeft;
    }

    @Override
    public int getCameraPaddingRight() {
        int paddingRight = super.getCameraPaddingRight();
        return paddingRight;
    }

    @Override
    public int getCameraPaddingBottom() {
        int paddingBottom = super.getCameraPaddingBottom();

        return paddingBottom;
    }


    public void draw(LatLng position) {
        setPosition(position);
    }


    @Override
    public void setPosition(LatLng position) {
        getParam().getOptions().position(position);
        if (isRemove()) {
            addToMap();
        } else {
            super.setPosition(position);

        }

    }


    @Override
    public void destory() {
        super.destory();

    }


    public final static class Builder extends BaseMarkerBuilder<Builder, PointMarkerParam> {


        public Builder(Context context, AMap map) {
            super(context, map);


        }


        @Override
        protected PointMarkerParam returnDefaultParam() {
            return new PointMarkerParam();
        }

        @Override
        protected Builder getChild() {
            return this;
        }

        @Override
        public <W> PointMarkerView<W> create() {

            PointMarkerView<W> pointMarkerView = new PointMarkerView<>(getContext(), getMap(), getParam());
            return pointMarkerView;
        }


        @Override
        public Builder setPosition(LatLng position) {
            super.setPosition(position);
            return this;
        }


    }
}
