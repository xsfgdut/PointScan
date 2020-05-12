package com.nexwise.pointscan.cloudNet;

import android.util.Log;

import com.nexwise.pointscan.constant.CloudConstant;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static java.lang.String.valueOf;

/**
 * 检查当前网络判断工作模式，执行服务器连接请求
 * Created by adolf_dong on 2016/5/19.
 */
public class NetUtil {
    /**
     * 发起网络请求
     *
     * @param respond HttpRespond的实现实例
     * @param action  请求的命令
     */
    public static void doCloudEven(HttpRespond respond, String action) {
        HttpRequst.getHttpRequst().requestN(respond, action);
    }

    /**
     * post方式二：stirng类型参数和上传文件参数
     */
    public static Call uploadMoreFile(Map<String, Object> params) {
        //post请求方式二：multipart/form-data(不仅能够上传string类型的参数，还可以上传文件（流的形式，file）)
        OkHttpClient okHttpClient1 = new OkHttpClient();
        MultipartBody.Builder builder1 = new MultipartBody.Builder();
        builder1.setType(MultipartBody.FORM);
        for (Map.Entry<String, Object> stringObjectEntry : params.entrySet()) {
            String key = stringObjectEntry.getKey();
            Object value = stringObjectEntry.getValue();
            if (value instanceof File) {//如果请求的值是文件
                File file = (File) value;
                //MediaType.parse("application/octet-stream")以二进制的形式上传文件
                builder1.addFormDataPart("images", ((File) value).getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
            } else {//如果请求的值是string类型
                builder1.addFormDataPart(key, value.toString());
            }
        }
        Request request1 = new Request.Builder().post(builder1.build()).url(CloudConstant.Source.SERVER + "/point/detail/update").build();
        Call call = okHttpClient1.newCall(request1);
        return call;
    }

    protected void post_file(final String url, final Map<String, Object> map, File file) {
        OkHttpClient client = new OkHttpClient();
        // form 表单形式上传
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (file != null) {
            // MediaType.parse() 里面是上传的文件类型。
            RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
            String filename = file.getName();
            // 参数分别为， 请求key ，文件名称 ， RequestBody
            requestBody.addFormDataPart("headImage", file.getName(), body);
        }
        if (map != null) {
            // map 里面是请求中所需要的 key 和 value
            for (Map.Entry entry : map.entrySet()) {
                requestBody.addFormDataPart(valueOf(entry.getKey()), valueOf(entry.getValue()));
            }
        }
        Request request = new Request.Builder().url("请求地址").post(requestBody.build()).tag("").build();
        // readTimeout("请求超时时间" , 时间单位);
        client.newBuilder().readTimeout(5000, TimeUnit.MILLISECONDS).build().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("lfq", "onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String str = response.body().string();
                    Log.i("lfq", response.message() + " , body " + str);

                } else {
                    Log.i("lfq", response.message() + " error : body " + response.body().string());
                }
            }
        });

    }


}
