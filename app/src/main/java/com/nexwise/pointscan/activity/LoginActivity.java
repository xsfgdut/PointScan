package com.nexwise.pointscan.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.nexwise.pointscan.R;
import com.nexwise.pointscan.base.BaseAct;

import com.nexwise.pointscan.cloudNet.NetRequest;
import com.nexwise.pointscan.constant.CloudConstant;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

import static com.nexwise.pointscan.utils.StringUtil.md5Decode32;

public class LoginActivity extends BaseAct {
    private static final int REQUEST_CODE_PERMISSION_LOCATION = 2;
    private EditText user_name;
    private EditText password;
    private Button loginBtn;
    private String userName;
    private String passwordString;
    private String isFirst;
    // 用来计算返回键的点击间隔时间
    private long exitTime = 0;

    private void initView() {
        user_name = findViewById(R.id.user_name);
        password = findViewById(R.id.password_tv);
        loginBtn = findViewById(R.id.login);
    }

    @Override
    public void onResume() {
        user_name.setText("test");
        password.setText("123456");
        user_name.setSelection(user_name.getText().length());
        password.setSelection(password.getText().length());
        super.onResume();
    }

    private void initListener() {
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user_name.getText().toString().equals("") || password.getText().toString().equals("")) {
                    showToat("用户名或者密码为空");
                    return;
                }
                startIntent();
                doRequest();
            }
        });
    }

    @Override
    protected void findView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        initView();
        checkPermissions();
        initListener();
    }

    private void startIntent() {
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this, MapActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//防止快速点击按钮回造成跳转多次
        startActivity(intent);
    }


    private void doRequest() {
        userName = user_name.getText().toString();
        passwordString = md5Decode32(password.getText().toString());
        Map<String, String> map = new HashMap<>();
        map.put(CloudConstant.ParameterKey.USER_NAME, userName);
        map.put(CloudConstant.ParameterKey.PWD, passwordString);
        map.put(CloudConstant.ParameterKey.CAPTCHA, "4567");
        NetRequest.postJsonRequest(this, "login", map, new NetRequest.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                Log.d("xsf", "login success====" + result);
                JSONObject  dataJson=new JSONObject(result);
                JSONObject  response=dataJson.getJSONObject("result");
                String code = response.getString("code");

                if (code.equals("0000")) {
                    startIntent();
                } else if (code.equals("1010")) {
                    showToat("用户不存在");
                } else if (code.equals("1011")) {
                    showToat("用户名或密码错误");
                } else if (code.equals("1010")) {
                    showToat("验证码错误");
                }
            }

            @Override
            public void requestFailure(Request request, IOException e) {
                showToat(e.getMessage());
            }
        });
    }


    @Override
    public final void onRequestPermissionsResult(int requestCode,
                                                 @NonNull String[] permissions,
                                                 @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                //弹出提示，可以有多种方式
                showToat(getResources().getString(R.string.exit_tv));
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                //结束进程之前可以把你程序的注销或者退出代码放在这段代码之前
                android.os.Process.killProcess(android.os.Process.myPid());
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
