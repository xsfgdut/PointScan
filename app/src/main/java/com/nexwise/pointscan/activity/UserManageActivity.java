package com.nexwise.pointscan.activity;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nexwise.pointscan.R;
import com.nexwise.pointscan.adapter.UserListAdapter;
import com.nexwise.pointscan.base.BaseAct;
import com.nexwise.pointscan.bean.User;
import com.nexwise.pointscan.cloudNet.NetRequest;
import com.nexwise.pointscan.constant.CloudConstant;
import com.nexwise.pointscan.refreshSwipe.RefreshSwipeMenuListView;
import com.nexwise.pointscan.refreshSwipe.SwipeMenu;
import com.nexwise.pointscan.refreshSwipe.SwipeMenuCreator;
import com.nexwise.pointscan.refreshSwipe.SwipeMenuItem;
import com.nexwise.pointscan.view.AddUserDialog;
import com.nexwise.pointscan.view.ModifyUserPswDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

public class UserManageActivity extends BaseAct implements RefreshSwipeMenuListView.OnRefreshListener{
    private TextView addUser;
    private RefreshSwipeMenuListView userList;
    private List<User> users = new ArrayList<>();
    private UserListAdapter userListAdapter;
    private AddUserDialog addUserDialog;
    private ModifyUserPswDialog modifyUserPswDialog;
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
        userList.setListViewMode(RefreshSwipeMenuListView.BOTH);
        userList.setOnRefreshListener(this);
        // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem editItem = new SwipeMenuItem(getApplicationContext());
                // set item background
                editItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9, 0xCE)));
                // set item width
                editItem.setWidth(dp2px(90));
                // set item title
                editItem.setTitle("编辑");
                // set item title fontsize
                editItem.setTitleSize(18);
                // set item title font color
                editItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(editItem);

                // create "delete" item
                SwipeMenuItem pswItem = new SwipeMenuItem(getApplicationContext());
                // set item background
                pswItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                // set item width
                pswItem.setWidth(dp2px(90));
                // set a icon
                pswItem.setTitle("修改密码");
                // set item title fontsize
                pswItem.setTitleSize(18);
                // set item title font color
                pswItem.setTitleColor(Color.BLUE);
                // add to menu
                menu.addMenuItem(pswItem);
            }
        };
        // set creator
        userList.setMenuCreator(creator);

        // step 2. listener item click event
        userList.setOnMenuItemClickListener(new RefreshSwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
                User item = users.get(position);
                switch (index) {
                    case 0:
                        showAddUserDialog(false,position);
                        break;
                    case 1:
                        showModifyPsw(position);
                        break;
                }
            }
        });

        // set SwipeListener
        userList.setOnSwipeListener(new RefreshSwipeMenuListView.OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {
                // swipe start
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });

        // other setting
        // listView.setCloseInterpolator(new BounceInterpolator());

        // test item long click
        userList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), position + " long click", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    private void initListener() {
        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddUserDialog(true,0);
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

    private void showModifyPsw(final int index) {
        modifyUserPswDialog = new ModifyUserPswDialog(this);
        modifyUserPswDialog.setValue(users.get(index).getUserName());
        modifyUserPswDialog.setOnClickCommitListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = users.get(index);
                userName = user.getUserName();
                password = ((EditText)modifyUserPswDialog.findViewById(R.id.new_psw)).getText().toString();
                if (TextUtils.isEmpty(password)) {
                    showToat("请检查非空项");
                    return;
                }
                doModifyUserPswRequest();
                modifyUserPswDialog.dismiss();
            }
        });
        modifyUserPswDialog.setOnClickCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modifyUserPswDialog.dismiss();
            }
        });
        modifyUserPswDialog.show();
    }

    private void showAddUserDialog(final boolean isAdd,int index) {
        addUserDialog = new AddUserDialog(this,isAdd);
        if (!isAdd) {
            User user = users.get(index);
            addUserDialog.setValue(user.getUserName(),user.getName(),user.getTel(),user.getAddress(),user.getRemark());
        }
        addUserDialog.setOnClickCommitListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userName = ((EditText)addUserDialog.findViewById(R.id.user_name)).getText().toString();
                password = ((EditText)addUserDialog.findViewById(R.id.psw)).getText().toString();
                name = ((EditText)addUserDialog.findViewById(R.id.name_value)).getText().toString();
                tel = ((EditText)addUserDialog.findViewById(R.id.number_value)).getText().toString();
                address = ((EditText)addUserDialog.findViewById(R.id.address_value)).getText().toString();
                remark = ((EditText)addUserDialog.findViewById(R.id.remark_value)).getText().toString();

                if (isAdd) {
                    if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
                        showToat("请检查非空项");
                        return;
                    }
                    doAddUserRequest();
                } else {
                    if (TextUtils.isEmpty(userName)) {
                        showToat("请检查非空项");
                        return;
                    }
                    doModifyUserRequest();
                }
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
                    if (users.size() > 0) {
                        users.clear();
                    }
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
            @Override
            public void requestNetWorkError() {
                showToat("网络错误");
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
            @Override
            public void requestNetWorkError() {
                showToat("网络错误");
            }
        });
    }

    private void doModifyUserRequest() {
        Map<String, String> map = new HashMap<>();
        map.put(CloudConstant.ParameterKey.USER_NAME, userName);
        map.put(CloudConstant.ParameterKey.NAME, name);
        map.put(CloudConstant.ParameterKey.TEL, tel);
        map.put(CloudConstant.ParameterKey.ADDRESS, address);
        map.put(CloudConstant.ParameterKey.REMARK, remark);
        NetRequest.postJsonRequest(this, CloudConstant.CmdValue.MODIFY_USER_INFO, map, new NetRequest.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                disMissProgressDialog();
                Log.d("xsf", "modify user success====" + result);
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
            @Override
            public void requestNetWorkError() {
                showToat("网络错误");
            }
        });
    }

    private void doModifyUserPswRequest() {
        Map<String, String> map = new HashMap<>();
        map.put(CloudConstant.ParameterKey.USER_NAME, userName);
        map.put(CloudConstant.ParameterKey.PASS_WORD_N, password);
        NetRequest.postJsonRequest(this, CloudConstant.CmdValue.MODIFY_USER_PWD, map, new NetRequest.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                disMissProgressDialog();
                Log.d("xsf", "modify user psw success====" + result);
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
            @Override
            public void requestNetWorkError() {
                showToat("网络错误");
            }
        });
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            userList.complete();
        }
    };

    @Override
    public void onRefresh() {
        mHandler.sendMessageDelayed(new Message(), 3000);
    }

    @Override
    public void onLoadMore() {
        mHandler.sendMessageDelayed(new Message(), 3000);
    }
}
