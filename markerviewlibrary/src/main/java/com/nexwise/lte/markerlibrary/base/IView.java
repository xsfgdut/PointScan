package com.nexwise.lte.markerlibrary.base;

/**
 * @author xsf
 * Created by xsf on 2019/10/15.
 */
public interface IView {


    /**
     * 添加到地图上
     */
    void addToMap();

    /**
     * 从地图上移除
     */
    void removeFromMap();

    /**
     * 从地图上摧毁
     */
    void destory();

    /**
     * 是否移除
     * @return boolean
     */
    boolean isRemove();


}
