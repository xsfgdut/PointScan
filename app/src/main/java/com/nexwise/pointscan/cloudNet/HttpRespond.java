package com.nexwise.pointscan.cloudNet;

import org.apache.http.NameValuePair;

import java.util.List;

/**
 * 网络交互过程中的处理
 * Created by adolf_dongon 2016/1/6.
 */
public interface HttpRespond {

    void onRequest(String action);

    List<NameValuePair> getParamter(String action);

    /**
     * 云架构取参
     */
    List<NameValuePair> getParamterN(String action);


    void onSuccess(String action, String json);

    /**
     * 云架构的回复
     */
    void onSuccessN(String action, String json);

    void onFaild(String action, Exception e);

    void onFaild(String action, int state);

    void onRespond(String action);

    void operationFailed(String action, String json);

    /**
     * 云架构出错回调
     */
    void operationFailedN(String action, String json);
}