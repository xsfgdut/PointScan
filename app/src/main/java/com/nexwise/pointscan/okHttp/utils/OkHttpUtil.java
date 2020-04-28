package com.nexwise.pointscan.okHttp.utils;


import java.io.File;

import java.util.HashMap;
import java.util.Iterator;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;


/**
 * @author Sen
 * @desc 网络请求封装类
 * @date 2017/10/31.
 */
public class OkHttpUtil {

    /**
     * 设置请求头信息
     *
     * @param builder
     * @param headers
     * @return
     */
    private static Request.Builder setHeaders(Request.Builder builder, HashMap<String, String> headers) {
        if (headers != null && headers.size() > 0) {
            Iterator<String> iterator = headers.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                String value = headers.get(key);
                builder.addHeader(key, value);
            }
        }
        return builder;
    }

    /**
     * 设置post请求参数列表
     *
     * @param builder
     * @param params
     * @return
     */
    private static FormBody.Builder setPostParams(FormBody.Builder builder, HashMap<String, Object> params) {
        if (params != null && params.size() > 0) {
            Iterator<String> iterator = params.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                Object value = params.get(key);
                builder.add(key, value == null ? "" : value.toString());
            }
        }
        return builder;
    }

    /**
     * 执行post请求
     *
     * @param url     路径
     * @param params  参数列表
     * @param headers 头信息列表
     * @return
     * @throws Exception
     */
    public static Request post(String url, HashMap<String, Object> params, HashMap<String, String> headers) throws Exception {
        //post键值对构造器
        FormBody.Builder formBody = new FormBody.Builder();
        //设置参数列表
        formBody = setPostParams(formBody, params);
        //获取请求body
        RequestBody body = formBody.build();

        Request.Builder builder = new Request.Builder();
        //设置头信息
        builder = setHeaders(builder, headers);
        Request request = builder
                .url(url)
                .post(body)
                .build();
        //返回post请求
        return request;
    }


    /**
     * 发送post请求并且上传文件
     *
     * @param url     请求路径
     * @param params  参数列表
     * @param headers 头信息列表
     * @param files   文件列表
     * @return
     * @throws Exception
     */
    public static Request post(String url, HashMap<String, Object> params, HashMap<String, String> headers, HashMap<String, File> files) throws Exception {

        //post键值对构造器
        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder();
        //设置类型
        multipartBuilder.setType(MultipartBody.FORM);
        //设置参数列表
        if (params != null && params.size() > 0) {
            Iterator<String> iterator = params.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                Object value = params.get(key);
                multipartBuilder.addFormDataPart(key, value == null ? "" : value.toString());
            }
        }
        //设置文件列表
        if (files != null && files.size() > 0) {
            Iterator<String> iterator = files.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                File file = files.get(key);
                multipartBuilder.addFormDataPart(key, file.getName(), RequestBody.create(MediaType.parse("image/png"), file));
            }
        }
        //获取请求body
        RequestBody body = multipartBuilder.build();

        Request.Builder builder = new Request.Builder();
        //设置头信息
        builder = setHeaders(builder, headers);
        Request request = builder
                .url(url)
                .post(body)
                .build();
        //返回post请求
        return request;
    }

    /**
     * 封装get请求
     *
     * @param url
     * @param params
     * @param headers
     * @return
     * @throws Exception
     */
    public static Request get(String url, HashMap<String, Object> params, HashMap<String, String> headers) throws Exception {
        Request.Builder builder = new Request.Builder();
        //设置头信息
        builder = setHeaders(builder, headers);
        //创建请求对象
        Request request = builder
                .url(mapToBasicNameValue(url, params))
                .build();
        //返回get请求
        return request;
    }

    /**
     * 为get请求添加参数
     *
     * @param params
     * @return
     */
    private static String mapToBasicNameValue(String url, HashMap<String, Object> params) {
        StringBuffer buffer = null;
        if (params != null && !params.isEmpty()) {
            buffer = new StringBuffer();
            buffer.append(url + "?");
            Iterator<String> iterator = params.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                Object value = params.get(key);
                buffer.append(key + "=" + value + "&");
            }
            return buffer.substring(0, buffer.toString().length() - 1);
        } else {
            return url;
        }
    }


//    private static Cache provideCache() {
//        return new Cache(mContext.getCacheDir(), 10240 * 1024);
//    }

}
