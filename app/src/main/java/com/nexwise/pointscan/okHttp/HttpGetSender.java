package com.nexwise.pointscan.okHttp;


import com.nexwise.pointscan.okHttp.utils.OkHttpCallback;
import com.nexwise.pointscan.okHttp.utils.OkHttpUtil;

/**
 * @author Sen
 * @desc
 * @date 2017/10/31.
 */
public class HttpGetSender extends HttpSender {

    /**
     * 执行发送get请求
     *
     * @throws Exception
     */
    public void send() {
        try {
            //执行请求
            enqueue(OkHttpUtil.get(url, params, headers), new OkHttpCallback(handler));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
