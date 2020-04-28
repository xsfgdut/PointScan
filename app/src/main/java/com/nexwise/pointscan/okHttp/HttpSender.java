package com.nexwise.pointscan.okHttp;


import android.os.Handler;
import android.os.Message;

import com.nexwise.pointscan.okHttp.utils.MessageBean;
import com.nexwise.pointscan.okHttp.utils.RequestManager;

import java.io.File;
import java.util.HashMap;


/**
 * @author Sen
 * @desc
 * @date 2017/10/31.
 */
public abstract class HttpSender extends RequestManager {

    //参数列表
    protected HashMap<String, Object> params = new HashMap<String, Object>();

    //头信息列表
    protected HashMap<String, String> headers = new HashMap<String, String>();

    //上传文件列表
    protected HashMap<String, File> files = new HashMap<String, File>();

    //请求路径
    protected String url;

    //临时存放执行成功回调对象
    protected HttpSuccessCallBack successCallBack;

    //临时存放执行失败回调对象
    protected HttpErrorCallBack errorCallBack;

    //请求返回的信息
    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MessageBean message = (MessageBean) msg.obj;
            switch (message.getCode()) {
                case 0:
                    //响应成功
                    String json = message.getMessage();
                    //如果存在执行成功后的接口对象，执行onResponse方法将返回值传递给方法
                    if (successCallBack != null) {
                        try {
                            successCallBack.onResponse(json);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 1:
                    //响应失败
                    try {
                        errorCallBack.haveErrors(message.getException());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 添加请求路径
     *
     * @param url 请求地址
     * @return
     */
    public HttpSender url(String url) {
        this.url = url;
        return this;
    }

    /**
     * 添加单个参数
     *
     * @param name  参数名称
     * @param value 参数值
     * @return
     */
    public HttpSender addParam(final String name, final Object value) {
        this.params.put(name, value);
        return this;
    }

    /**
     * 添加多个参数集合
     *
     * @param params 参数集合
     * @return
     */
    public HttpSender addParams(HashMap<String, Object> params) {
        //写入到父类参数列表集合这种
        this.params.putAll(params);
        return this;
    }

    /**
     * 添加头信息
     *
     * @param name  头信息名称
     * @param value 头信息值
     * @return
     */
    public HttpSender addHeader(final String name, final String value) {
        this.headers.put(name, value);
        return this;
    }

    /**
     * 添加头信息集合
     *
     * @param headers 信息集合
     * @return
     */
    public HttpSender addHeaders(HashMap<String, String> headers) {
        this.headers.putAll(headers);
        return this;
    }

    /**
     * 执行成功的回调函数
     *
     * @return
     */
    public HttpSender success(HttpSuccessCallBack callBack) {
        this.successCallBack = callBack;
        return this;
    }

    /**
     * 执行失败回到函数
     *
     * @param callBack
     * @return
     */
    public HttpSender error(HttpErrorCallBack callBack) {
        this.errorCallBack = callBack;
        return this;
    }

    /**
     * 执行发送请求
     */
    public abstract void send();


}
