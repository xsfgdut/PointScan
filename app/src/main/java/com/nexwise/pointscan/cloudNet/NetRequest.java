package com.nexwise.pointscan.cloudNet;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.gson.Gson;
import com.nexwise.pointscan.base.BaseApplication;
import com.nexwise.pointscan.constant.CloudConstant;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.Dispatcher;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetRequest {

    private static NetRequest netRequest;
    private static OkHttpClient okHttpClient; // OKHttp网络请求
    private Handler mHandler;

    private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();
    //设置请求超时
    private NetRequest() {
        okHttpClient = new OkHttpClient();
        okHttpClient
                .newBuilder()
                .connectTimeout(1000, TimeUnit.SECONDS)
                .readTimeout(1000, TimeUnit.SECONDS)
                .writeTimeout(1000, TimeUnit.SECONDS);
        mHandler = new Handler(Looper.getMainLooper());
    }

    //单例模式  获取NetRequest实例
    private static NetRequest getInstance() {
        if (netRequest == null) {
            netRequest = new NetRequest();
        }
        return netRequest;
    }

    //----------对外提供的方法Start--------------
    //建立网络框架，获取网络数据，异步get请求（Form）
    public static void getFormRequest(Context context, String url, Map<String, String> params, DataCallBack callBack) {
        getInstance().inner_getFormAsync(context, url, params, callBack);
    }

    //建立网络框架，获取网络数据，异步post请求（Form）
    public static void postFormRequest(Context context, String url, Map<String, String> params, DataCallBack callBack) {
        getInstance().inner_postFormAsync(context, url, params, callBack);
    }

    //建立网络框架，获取网络数据，异步post请求（json）
    public static void postJsonRequest(Context context, String action, Map<String, String> params, DataCallBack callBack) {
        String url = "";
        switch (action) {
            case CloudConstant.CmdValue.CAPTCHA:
                url = CloudConstant.Source.SERVER + "/captcha";
                break;
            case CloudConstant.CmdValue.LOGIN:
                url = CloudConstant.Source.SERVER + "/account/login";
                break;
            case CloudConstant.CmdValue.LOGOUT:
                url = CloudConstant.Source.SERVER + "/account/logout";
                break;
            case CloudConstant.CmdValue.PWD:
                url = CloudConstant.Source.SERVER + "/account/pwd";
                break;
            case CloudConstant.CmdValue.POINT_LIST:
                url = CloudConstant.Source.SERVER + "/point/list";
                break;
            case CloudConstant.CmdValue.DETAIL_INFO:
                url = CloudConstant.Source.SERVER + "/point/detail/info";
                break;
            case CloudConstant.CmdValue.ADD_POINT:
                url = CloudConstant.Source.SERVER + "/point/add";
                break;
            case CloudConstant.CmdValue.UPDATE_INFO:
                url = CloudConstant.Source.SERVER + "/point/detail/update";
                break;
            case CloudConstant.CmdValue.ADD_USER:
                url = CloudConstant.Source.SERVER + "/manage/user/add";
                break;
            case CloudConstant.CmdValue.MODIFY_USER_PWD:
                url = CloudConstant.Source.SERVER + "/manage/user/pwd";
                break;
            case CloudConstant.CmdValue.QUERY_USER:
                url = CloudConstant.Source.SERVER + "/manage/user/list";
                break;
            case CloudConstant.CmdValue.MODIFY_USER_INFO:
                url = CloudConstant.Source.SERVER + "/manage/user/info";
                break;
        }
        getInstance().inner_postJsonAsync(context, url, params, callBack);
    }

    //----------对外提供的方法 End ---------------

    //异步get请求（Form），内部实现方法
    private void inner_getFormAsync(Context context, String url, Map<String, String> params, final DataCallBack callBack) {
        if (params == null) {
            params = new HashMap<>();
        }
        // 请求url（baseUrl+参数）
        final String doUrl = urlJoint(url, params);
        final Request request = new Request.Builder().url(doUrl).build();
        //执行请求获得响应结果
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                deliverDataFailure(request, e, callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) { // 请求成功
                    //执行请求成功的操作
                    String result = response.body().string();
                    deliverDataSuccess(result, callBack);
                } else {
                    throw new IOException(response + "");
                }
            }
        });
    }

    //异步post请求（Form）,内部实现方法
    private void inner_postFormAsync(Context context, String url, Map<String, String> params, final DataCallBack callBack) {
        RequestBody requestBody;
        if (params == null) {
            params = new HashMap<>();
        }
        FormBody.Builder builder = new FormBody.Builder();
        /**
         * 在这对添加的参数进行遍历
         */
        for (Map.Entry<String, String> map : params.entrySet()) {
            String key = map.getKey();
            String value;
            /**
             * 判断值是否是空的
             */
            if (map.getValue() == null) {
                value = "";
            } else {
                value = map.getValue();
            }
            /**
             * 把key和value添加到formbody中
             */
            builder.add(key, value);
        }
        requestBody = builder.build();
        final Request request = new Request.Builder().url(url).post(requestBody).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                deliverDataFailure(request, e, callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) { // 请求成功
                    //执行请求成功的操作
                    String result = response.body().string();
                    deliverDataSuccess(result, callBack);
                } else {
                    throw new IOException(response + "");
                }
            }
        });
    }

    //post请求传json
    private void inner_postJsonAsync(final Context context, String url, Map<String, String> params, final DataCallBack callBack) {
        // 将map转换成json,需要引入Gson包
        String mapToJson = new Gson().toJson(params);
        Log.d("xsf",mapToJson + "== mapToJson");
        final Request request = buildJsonPostRequest(url, mapToJson);
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                deliverDataFailure(request, e, callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) { // 请求成功, 执行请求成功的操作
                    Headers headers=response.headers();
                    List<String> cookies=headers.values("Set-Cookie");
                    if(cookies.size()>0){
                        String session=cookies.get(0);
                        String cookie=session.substring(0,session.indexOf(";"));
                        GetParameter.COOKIE = cookie;
                       Log.d("xsf","cookie=" + cookie);
                       for (String onecook :cookies) {
                           if (onecook.contains("JSESSIONID")) {
                               GetParameter.COOKIE = onecook.split(";")[0];
                               Log.d("xsf","cookie1=" + cookie);
                           }
                       }
                    }

                    String result = response.body().string();
                    deliverDataSuccess(result, callBack);
                } else {
                    throw new IOException(response + "");
                }
            }
        });
    }

    //Json_POST请求参数
    private Request buildJsonPostRequest(String url, String json) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Log.d("xsf",GetParameter.COOKIE + "=GetParameter.COOKIE");
        if (GetParameter.COOKIE != null) {
            Map<String,String> cookie = new HashMap<>();
            cookie.put("JSESSIONID",GetParameter.COOKIE.substring(11));
            String cookieToJson = new Gson().toJson(cookie);
            Log.d("xsf",cookieToJson + "=cookieToJson");
            Log.d("xsf",GetParameter.COOKIE.substring(11) + "=GetParameter.COOKIE11");
            return new Request.Builder().addHeader("cookie",GetParameter.COOKIE).url(url).post(requestBody).build();
        } else {
            return new Request.Builder().url(url).post(requestBody).build();
        }

    }

    //分发失败的时候调用
    private void deliverDataFailure(final Request request, final IOException e, final DataCallBack callBack) {
        //在这里使用异步处理
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.requestFailure(request, e);
                }
            }
        });
    }

    //分发成功的时候调用
    private void deliverDataSuccess(final String result, final DataCallBack callBack) {
        //在这里使用异步线程处理
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    try {
                        callBack.requestSuccess(result);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    //数据回调接口
    public interface DataCallBack {
        void requestSuccess(String result) throws Exception;
        void requestFailure(Request request, IOException e);
    }

    //拼接url和请求参数
    private static String urlJoint(String url, Map<String, String> params) {
        StringBuilder endUrl = new StringBuilder(url);
        boolean isFirst = true;
        Set<Map.Entry<String, String>> entrySet = params.entrySet();
        for (Map.Entry<String, String> entry : entrySet) {
            if (isFirst && !url.contains("?")) {
                isFirst = false;
                endUrl.append("?");
            } else {
                endUrl.append("&");
            }
            endUrl.append(entry.getKey());
            endUrl.append("=");
            endUrl.append(entry.getValue());
        }
        return endUrl.toString();
    }

    //取消请求 按 TAG 来取消对应的请求
    public static void cancelRequest(String tag){
        Dispatcher dispatcher = okHttpClient.dispatcher();
        synchronized (dispatcher){
            for (Call call : dispatcher.queuedCalls()) {
                if (tag.equals(call.request().tag())) {
                    call.cancel();
                }
            }
            for (Call call : dispatcher.runningCalls()) {
                if (tag.equals(call.request().tag())) {
                    call.cancel();
                }
            }
        }
    }




    //图片与参数一起上传
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png; charset=utf-8");//png格式图片
    private static final MediaType MEDIA_TYPE_TXT = MediaType.parse("text/x-markdown; charset=utf-8");//txt格式文件

    //建立网络框架，获取网络数据，图片与参数一起上传
    public static void imageFileRequest(Context context, String reqUrl, Map<String, String> params, String pic_key, List<File> files, String cell_key,List<File> cellsFile, DataCallBack callBack) {
        getInstance().imageFileAsync(context, reqUrl, params, pic_key, files,cell_key,cellsFile, callBack);
    }

    private void imageFileAsync(Context context, String reqUrl, Map<String, String> params, String pic_key, List<File> files, String cell_key,List<File> cellsFile,final DataCallBack callBack) {
        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();
        multipartBodyBuilder.setType(MultipartBody.FORM);
        //遍历map中所有参数到builder
        if (params != null){
            for (String key : params.keySet()) {
                multipartBodyBuilder.addFormDataPart(key, params.get(key),RequestBody.create(MediaType.parse("application/json; charset=utf-8"), params.get(key)));

            }
        }
        //遍历paths中所有图片绝对路径到builder，并约定key如“upload”作为后台接受多张图片的key
        if (cellsFile != null){
            for (File file : cellsFile) {
                multipartBodyBuilder.addFormDataPart(cell_key, file.getName(), RequestBody.create(MEDIA_TYPE_TXT, file));
            }
        }
        if (files != null){
            for (File file : files) {
                multipartBodyBuilder.addFormDataPart(pic_key, file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file));
            }
        }
        //构建请求体
        RequestBody requestBody = multipartBodyBuilder.build();

        Request.Builder RequestBuilder = new Request.Builder();
        RequestBuilder.addHeader("cookie",GetParameter.COOKIE);
        RequestBuilder.url(reqUrl);// 添加URL地址
        RequestBuilder.post(requestBody);
        final Request request = RequestBuilder.build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                deliverDataFailure(request, e, callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) { // 请求成功
                    //执行请求成功的操作
                    String result = response.body().string();
                    deliverDataSuccess(result, callBack);
                } else {
                    throw new IOException(response + "");
                }
            }
        });
    }

}

