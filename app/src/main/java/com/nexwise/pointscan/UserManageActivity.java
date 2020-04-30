package com.nexwise.pointscan;


import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nexwise.pointscan.adapter.UserListAdapter;
import com.nexwise.pointscan.base.BaseAct;
import com.nexwise.pointscan.bean.User;
import com.nexwise.pointscan.cloudNet.NetRequest;
import com.nexwise.pointscan.constant.CloudConstant;
import com.nexwise.pointscan.view.AddUserDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

public class UserManageActivity extends BaseAct {
    private TextView addUser;
    private ListView userList;
    private List<User> users = new ArrayList<>();
    private UserListAdapter userListAdapter;
    private AddUserDialog addUserDialog;
    private String userName;
    private String password;
    private String name;
    private String tel;
    private String address;
    private String remark;

    @Override
    protected void findView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_user_manage);
        initView();
        initListener();
        doQueryUserRequest();
    }

    private void initView() {
        addUser = findViewById(R.id.new_user);
        userList = findViewById(R.id.user_list);
    }

    private void initListener() {
        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddUserDialog();
            }
        });
    }

    private void setAdapter() {
        if (userListAdapter == null) {
            userListAdapter = new UserListAdapter(this, users);
            userList.setAdapter(userListAdapter);
        } else {
            userListAdapter.notifyDataSetChanged();
        }
    }

    private void showAddUserDialog() {
        addUserDialog = new AddUserDialog(this);
        addUserDialog.setOnClickCommitListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userName = ((EditText)addUserDialog.findViewById(R.id.user_name)).getText().toString();
                password = ((EditText)addUserDialog.findViewById(R.id.psw)).getText().toString();
                name = ((EditText)addUserDialog.findViewById(R.id.name_value)).getText().toString();
                tel = ((EditText)addUserDialog.findViewById(R.id.number_value)).getText().toString();
                address = ((EditText)addUserDialog.findViewById(R.id.address_value)).getText().toString();
                remark = ((EditText)addUserDialog.findViewById(R.id.remark_value)).getText().toString();
                if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
                    showToat("请检查非空项");
                    return;
                }
                doAddUserRequest();
                addUserDialog.dismiss();
            }
        });
        addUserDialog.setOnClickCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addUserDialog.dismiss();
            }
        });
        addUserDialog.show();
    }

    private void doQueryUserRequest() {
        Map<String, String> map = new HashMap<>();
//        map.put(CloudConstant.ParameterKey.KEY, "test");
        map.put(CloudConstant.ParameterKey.PAGE, String.valueOf(1));
        map.put(CloudConstant.ParameterKey.COUNT, String.valueOf(2000));
        NetRequest.postJsonRequest(this, CloudConstant.CmdValue.QUERY_USER, map, new NetRequest.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                disMissProgressDialog();
                Log.d("xsf", "query user success====" + result);
                JSONObject dataJson = new JSONObject(result);
                JSONObject response = dataJson.getJSONObject("result");
                String code = response.getString("code");
                if (code.equals("0000")) {
                    int total = dataJson.getInt("totalCount");
                    Log.d("xsf", "total====" + total);
                    JSONArray jsonArray = dataJson.getJSONArray("users");
                    Gson gson = new Gson();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        User user = gson.fromJson(jsonArray.getString(i), User.class);
                        users.add(user);
                    }
                    setAdapter();
                }
            }

            @Override
            public void requestFailure(Request request, IOException e) {
                showToat(e.getMessage());
            }
        });
    }

    private void doAddUserRequest() {
        Map<String, String> map = new HashMap<>();
        map.put(CloudConstant.ParameterKey.USER_NAME, userName);
        map.put(CloudConstant.ParameterKey.PASS_WORD_N, password);
        map.put(CloudConstant.ParameterKey.NAME, name);
        map.put(CloudConstant.ParameterKey.TEL, tel);
        map.put(CloudConstant.ParameterKey.ADDRESS, address);
        map.put(CloudConstant.ParameterKey.REMARK, remark);
        NetRequest.postJsonRequest(this, CloudConstant.CmdValue.ADD_USER, map, new NetRequest.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                disMissProgressDialog();
                Log.d("xsf", "add user success====" + result);
                JSONObject dataJson = new JSONObject(result);
                JSONObject response = dataJson.getJSONObject("result");
                String code = response.getString("code");
                if (code.equals("0000")) {
                    doQueryUserRequest();
                }
            }

            @Override
            public void requestFailure(Request request, IOException e) {
                showToat(e.getMessage());
            }
        });
    }
}
