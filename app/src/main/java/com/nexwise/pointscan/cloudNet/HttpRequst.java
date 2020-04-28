package com.nexwise.pointscan.cloudNet;

import android.os.Handler;
import android.util.Base64;
import android.util.Log;

import com.nexwise.pointscan.constant.CloudConstant;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.cache.HeaderConstants;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("deprecation")
public class HttpRequst {
    private static BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();
    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 20, 20, TimeUnit.SECONDS, queue);
    private static HttpRequst httpRequst;
    private String TAG = "HttpRequst";
    public static String URL = CloudConstant.Source.Common;
    /**
     * 超时时间
     */
    private static final int TIME_OUT = 20000;

    public static HttpRequst getHttpRequst() {
        if (httpRequst == null) {
            synchronized (HttpRequst.class) {
                if (httpRequst == null) {
                    httpRequst = new HttpRequst();
                }
            }
        }
        return httpRequst;
    }

    private Handler handler = new Handler();

    private HttpRequst() {
    }

    public void abort() {
        threadPoolExecutor.getQueue().clear();
    }


    public void requestGet(final HttpRespond httpRespond, final String action) {
        httpRespond.onRequest(action);
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                HttpClient httpClient = HttpsFctry.getNewHttpClient();
                List<NameValuePair> nameValuePairs = httpRespond.getParamterN(action);
                HttpGet httpGet = new HttpGet(nameValuePairs.get(0).getValue());
                try {
                    HttpResponse httpResponse = httpClient.execute(httpGet);
                    final int stateCode = httpResponse.getStatusLine().getStatusCode();
                    final String json = EntityUtils.toString(httpResponse.getEntity(), "utf-8").trim();
                    Log.d(TAG, json);
                    if (stateCode >= HttpStatus.SC_OK && stateCode < 300) {
                        // FIXME: 2018/9/20 stateCode不一定能对应上
                        int realCode = 0;
                        try {
                            realCode = new JSONObject(json).getInt("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (realCode >= HttpStatus.SC_OK && realCode < 300) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        // FIXME: 2018/9/19 data数据不一定有
                                        JSONObject jsonObj = new JSONObject(json);
                                        if (jsonObj.has("data")) {
                                            String dataJson = new JSONObject(json).getJSONObject("data").toString();
                                            httpRespond.onSuccessN(action, dataJson);
                                        } else {
                                            httpRespond.onSuccessN(action, "{}");
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        } else {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            httpRespond.operationFailedN(action, json);
                                        }
                                    });
                                    Log.i(TAG, "faild: code = " + stateCode);
                                }
                            });
                        }
                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        httpRespond.operationFailedN(action, json);
                                    }
                                });
                                Log.i(TAG, "faild: code = " + stateCode);
                            }
                        });
                    }
                } catch (final IOException e) {
                    e.printStackTrace();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            httpRespond.onFaild(action, e);
                        }
                    });
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 请求方式
     *
     * @param httpRespond
     * @param action
     */
    public void requestN(final HttpRespond httpRespond, final String action) {
        httpRespond.onRequest(action);
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                HttpClient httpClient = HttpsFctry.getNewHttpClient();
                String url = CloudConstant.Source.Common;
                boolean isAuth = false;
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
                        isAuth = true;
                        break;
                    case CloudConstant.CmdValue.POINT_LIST:
                        url = CloudConstant.Source.SERVER + "/point/list";
                        isAuth = true;
                        break;
                    case CloudConstant.CmdValue.DETAIL_INFO:
                        url = CloudConstant.Source.SERVER + "/point/detail/info";
                        isAuth = true;
                        break;
                    case CloudConstant.CmdValue.ADD_POINT:
                        url = CloudConstant.Source.SERVER + "/point/add";
                        isAuth = true;
                        break;
                    case CloudConstant.CmdValue.UPDATE_INFO:
                        url = CloudConstant.Source.SERVER + "/point/detail/update";
                        isAuth = true;
                        break;
                    case CloudConstant.CmdValue.ADD_USER:
                        url = CloudConstant.Source.SERVER + "/manage/user/add";
                        isAuth = true;
                        break;
                    case CloudConstant.CmdValue.MODIFY_USER_PWD:
                        url = CloudConstant.Source.SERVER + "/manage/user/pwd";
                        isAuth = true;
                        break;
                    case CloudConstant.CmdValue.QUERY_USER:
                        url = CloudConstant.Source.SERVER + "/manage/user/list";
                        isAuth = true;
                        break;
                    case CloudConstant.CmdValue.MODIFY_USER_INFO:
                        url = CloudConstant.Source.SERVER + "/manage/user/info";
                        isAuth = true;
                        break;
                }
                Log.d(TAG, "run: action = " + action);
                final HttpPost httpPost = new HttpPost(url);
                ArrayList<NameValuePair> headerList = new ArrayList<NameValuePair>();
                headerList.add(new BasicNameValuePair("Content-Type", "application/json; charset=utf-8"));
//                httpPost.addHeader(HeaderConstants.AUTHORIZATION, "Basic " + new String(Base64.encode(("webApp:webApp".getBytes()), Base64.NO_WRAP)));
                httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, TIME_OUT);
                List<NameValuePair> nameValuePairs = httpRespond.getParamterN(action);
//                nameValuePairs.add(new BasicNameValuePair(CloudConstant.ParameterKey.APPKEY, SmartApp.getDeviceId()));
                Log.i(TAG, nameValuePairs.toString());
                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                try {
                    for (int i = 0; i < headerList.size(); i++) {
                        httpPost.addHeader(headerList.get(i).getName(),
                                headerList.get(i).getValue());
                    }
                    HttpResponse response = httpClient.execute(httpPost);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            httpRespond.onRespond(action);
                        }
                    });
                    final int stateCode = response.getStatusLine().getStatusCode();
                    final String json = EntityUtils.toString(response.getEntity(), "utf-8").trim();
                    Log.d(TAG, json);
                    Log.d(TAG, "run: stateCode==" + stateCode);
                    if (stateCode >= HttpStatus.SC_OK && stateCode < 300) {
                        int realCode = 200;
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(json);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (jsonObject != null && jsonObject.has("status")) {
                            try {
                                realCode = jsonObject.getInt("status");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        // FIXME: 2018/9/20 stateCode不一定能对应上
                        if (realCode >= HttpStatus.SC_OK && realCode < 300) {
                            if (isAuth) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        httpRespond.onSuccessN(action, json);
                                    }
                                });
                            } else {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            // FIXME: 2018/9/19 data数据不一定有
                                            JSONObject jsonObj = new JSONObject(json);
                                            if (jsonObj.has("data")) {
                                                String dataJson = new JSONObject(json).getJSONObject("data").toString();
                                                httpRespond.onSuccessN(action, dataJson);
                                            } else {
                                                httpRespond.onSuccessN(action, "{}");
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        } else {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            httpRespond.operationFailedN(action, json);
                                        }
                                    });
                                    Log.i(TAG, "faild: code = " + stateCode);
                                }
                            });
                        }
                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        httpRespond.operationFailedN(action, json);
                                    }
                                });
                                Log.i(TAG, "faild: code = " + stateCode);
                            }
                        });
                    }
                } catch (final IOException e) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            httpRespond.onFaild(action, e);
                        }
                    });
                    e.printStackTrace();
                }
            }
        });
    }

    public void request(final HttpRespond httpRespond, final String action) {
        httpRespond.onRequest(action);
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                HttpClient httpclient = HttpsFctry.getNewHttpClient();
                HttpPost httpPost = new HttpPost(CloudConstant.Source.Common);
                httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, TIME_OUT);
                try {
                    List<NameValuePair> nameValuePairs = httpRespond.getParamter(action);
//                    nameValuePairs.add(new BasicNameValuePair(CloudConstant.ParameterKey.APPKEY, SmartApp.getDeviceId()));
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf-8"));
                    Log.i(TAG, nameValuePairs.toString());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                try {
                    HttpResponse response = httpclient.execute(httpPost);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            httpRespond.onRespond(action);
                        }
                    });
                    final int stateCode = response.getStatusLine().getStatusCode();
                    if (stateCode == HttpStatus.SC_OK) {
                        final String json = EntityUtils.toString(response.getEntity(), "utf-8").trim();
                        if (!CloudParseUtil.isSucceful(json)) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    httpRespond.operationFailed(action, json);
                                    Log.i(TAG, json);
                                }
                            });
                            return;
                        }
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                httpRespond.onSuccess(action, json);
                            }
                        });
                        Log.i(TAG, json);
                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Log.i(TAG, "faild: code = " + stateCode);
                                httpRespond.onFaild(action, stateCode);
                            }
                        });
                    }
                } catch (final IOException e) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            httpRespond.onFaild(action, e);
                        }
                    });
                    e.printStackTrace();
                }
            }
        });
    }
}