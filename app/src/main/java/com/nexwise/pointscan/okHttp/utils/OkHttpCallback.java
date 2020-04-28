package com.nexwise.pointscan.okHttp.utils;


import android.os.Handler;
import android.os.Message;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @author Sen
 * @desc 自定义CallBack用于处理返回结果
 * @date 2017/10/31.
 */

public class OkHttpCallback implements Callback {

    //用于传递请求消息
    private Handler handler;

    public OkHttpCallback(Handler handler) {
        this.handler = handler;
    }


    @Override
    public void onFailure(Call call, IOException e) {
        if (!call.isCanceled()) {
            MessageBean message = new MessageBean();
            message.setCode(1);
            message.setException(e);
            message.setMessage("服务器繁忙,请稍后再试");
            sendMessage(message);
        }
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if (!call.isCanceled()) {
            if (response != null && response.isSuccessful()) {
                String resStr = response.body().string();
                MessageBean message = new MessageBean();
                message.setCode(0);
                message.setMessage(resStr);
                sendMessage(message);
            } else {
                throw new IOException("Unexpected code " + response);
            }
        }
    }

    /**
     * 发送处理消息
     *
     * @param errorInfo
     */
    public void sendMessage(MessageBean errorInfo) {
        if (handler != null) {
            Message message = handler.obtainMessage();
            message.obj = errorInfo;
            handler.sendMessage(message);
        }
    }
}
