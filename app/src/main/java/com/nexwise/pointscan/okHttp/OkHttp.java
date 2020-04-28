package com.nexwise.pointscan.okHttp;

/**
 * @author Sen
 * @desc
 * @date 2017/10/31.
 */
public class OkHttp {


    /**
     * 创建一个post请求
     *
     * @return
     * @throws Exception
     */
    public static final HttpPostSender post() {
        return new HttpPostSender();
    }

    /**
     * 创建一个get请求
     *
     * @return
     * @throws Exception
     */
    public static final HttpGetSender get() {
        return new HttpGetSender();
    }

}
