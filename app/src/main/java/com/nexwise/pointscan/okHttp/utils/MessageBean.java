package com.nexwise.pointscan.okHttp.utils;

import java.io.IOException;

/**
 * @author Sen
 * @desc 请求消息状态处理
 * @date 2017/10/31.
 */

public class MessageBean {

    //请求状态 0为响应成功 1为失败
    private int code;
    //请求信息
    private String message;
    //请求失败异常
    private IOException exception;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public IOException getException() {
        return exception;
    }

    public void setException(IOException exception) {
        this.exception = exception;
    }
}
