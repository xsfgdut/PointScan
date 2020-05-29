package com.nexwise.pointscan.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.nexwise.pointscan.R;
import com.nexwise.pointscan.base.BaseAct;
import com.nexwise.pointscan.cloudNet.NetRequest;
import com.nexwise.pointscan.constant.CloudConstant;
import com.nexwise.pointscan.constant.DataPool;
import com.nexwise.pointscan.utils.Share;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

import static com.nexwise.pointscan.utils.StringUtil.md5Decode32;

public class SplashActivity extends BaseAct {

    private Handler handler = new Handler();
    private String userName;
    private String password;
    private String passwordString;
    private boolean first_run = false;
    private static final int REQUEST_CODE_PERMISSION_LOCATION = 2;
    private String[] ipValue;
    private String ip;
    private String port;

    @Override
    protected void findView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_splash);
        firstRunCheck();
        getDefaultValue();
        checkPermissions();
        getIPPort();
        if (first_run) {
            setDelayProcess();
        }

    }

    private void firstRunCheck() {
        first_run = Share.getBoolean("FirstRun", getApplicationContext());
        if (!first_run) {
            Share.putBoolean("FirstRun", true, getApplicationContext());
        } else {
            Share.putBoolean("FirstRun", true, getApplicationContext());
        }


    }

    private void setDelayProcess() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
                    startLaunch();
                } else {
                    doLoginRequest();
                }

            }
        }, 1 * 1000);
    }

    private void getIPPort() {
        String default_ip = Share.getString(getApplicationContext(), "ip_value", "");
        String ip_set = "";
        Log.d("xsf", default_ip + "=default_ip");
        if (TextUtils.isEmpty(default_ip)) {
            ipValue = new String[]{"183", "3", "145", "138"};
            port = "1780";
            DataPool.setIpValue("http://183.3.145.138:1780/");
        } else {
            String[] ip_port = default_ip.split(":");
            if (ip_port.length > 1) {
                port = ip_port[1];
            } else {
                port = "";
            }
            String ip_string = ip_port[0];
            Log.d("xsf", ip_string + "=ip_string");
            ipValue = ip_string.split("\\.");
            Log.d("xsf", port + "=port");
            Log.d("xsf", ipValue.length + "=ip_value");
            if (TextUtils.isEmpty(port)) {
                ip_set = "http://" + ip_string + "/";
            } else {
                ip_set = "http://" + ip_string + ":" + port + "/";
            }
            DataPool.setIpValue(ip_set);
        }
    }

    private void getDefaultValue() {
        userName = Share.getString("user_name", getApplicationContext());
        password = Share.getString("pass_word", getApplicationContext());
    }

    private void startLaunch() {
        Intent intent = new Intent();
        intent.setClass(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void startIntent() {
        Intent intent = new Intent();
        intent.setClass(SplashActivity.this, MapActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//防止快速点击按钮回造成跳转多次
        startActivity(intent);
        finish();
    }

    private void doLoginRequest() {
//        userName = user_name.getText().toString();
        passwordString = md5Decode32(password);
        Map<String, String> map = new HashMap<>();
        map.put(CloudConstant.ParameterKey.USER_NAME, userName);
        map.put(CloudConstant.ParameterKey.PWD, passwordString);
        map.put(CloudConstant.ParameterKey.CAPTCHA, "4567");
        NetRequest.postJsonRequest(this, CloudConstant.CmdValue.LOGIN, map, new NetRequest.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                Log.d("xsf", "login success====" + result);
                JSONObject dataJson = new JSONObject(result);
                JSONObject response = dataJson.getJSONObject("result");
                String code = response.getString("code");

                if (code.equals("0000")) {
                    startIntent();
                } else if (code.equals("1010")) {
                    showToat("用户不存在");
                } else if (code.equals("1011")) {
                    showToat("用户名或密码错误");
                } else if (code.equals("1012")) {
                    showToat("验证码错误");
                }
            }

            @Override
            public void requestFailure(Request request, IOException e) {
                showToat(e.getMessage());
                startLaunch();
            }

            @Override
            public void requestNetWorkError() {
                showToat("网络错误");
                startLaunch();
            }
        });
    }

    @Override
    public final void onRequestPermissionsResult(int requestCode,
                                                 @NonNull String[] permissions,
                                                 @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0) {
            List<String> deniedPermissionList = new ArrayList<>();
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    deniedPermissionList.add(permissions[i]);
                }
            }
            if (deniedPermissionList.isEmpty()) {
                //已经全部授权
                setDelayProcess();
            } else {

            }

        }
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_LOCATION:
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            onPermissionGranted(permissions[i]);
                        }
                    }

                } else {

                    break;
                }
        }
    }

    private void checkPermissions() {
        String[] permissions = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };
        List<String> permissionDeniedList = new ArrayList<>();
        for (String permission : permissions) {
            int permissionCheck = ContextCompat.checkSelfPermission(this, permission);
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                onPermissionGranted(permission);
            } else {
                permissionDeniedList.add(permission);
            }
        }
        if (!permissionDeniedList.isEmpty()) {
            String[] deniedPermissions = permissionDeniedList.toArray(new String[permissionDeniedList.size()]);
            ActivityCompat.requestPermissions(this, deniedPermissions, REQUEST_CODE_PERMISSION_LOCATION);
        }
    }

    private void onPermissionGranted(String permission) {
        switch (permission) {
            case Manifest.permission.ACCESS_FINE_LOCATION:
                break;
        }
    }

}
