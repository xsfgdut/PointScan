package com.nexwise.pointscan.okHttp;


import com.nexwise.pointscan.okHttp.utils.OkHttpCallback;
import com.nexwise.pointscan.okHttp.utils.OkHttpUtil;

import java.io.File;
import java.util.Hashtable;

import okhttp3.Request;

/**
 * @author Sen
 * @desc
 * @date 2017/10/31.
 */
public class HttpPostSender extends HttpSender {


    /**
     * 添加单个文件
     *
     * @param name
     * @param file
     * @return
     */
    public HttpPostSender addFile(final String name, final File file) {
        this.files.put(name, file);
        return this;
    }

    /**
     * 添加多个文件
     *
     * @param files 文件集合
     * @return
     */
    public HttpPostSender addFiles(Hashtable<String, File> files) {
        this.files.putAll(files);
        return this;
    }

    /**
     * 执行发送post请求
     *
     * @throws Exception
     */

    public void send() {
        try {
            //获取Request
            Request request = files.size() > 0 ? OkHttpUtil.post(url, params, headers, files) : OkHttpUtil.post(url, params, headers);
            //执行请求并获取返回值
            enqueue(request, new OkHttpCallback(handler));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
