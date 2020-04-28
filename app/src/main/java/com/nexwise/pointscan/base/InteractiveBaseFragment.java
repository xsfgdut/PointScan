package com.nexwise.pointscan.base;


import com.nexwise.pointscan.R;
import com.nexwise.pointscan.cloudNet.CloudParseUtil;
import com.nexwise.pointscan.cloudNet.HttpRequst;
import com.nexwise.pointscan.cloudNet.HttpRespond;
import com.nexwise.pointscan.constant.CloudConstant;

import org.apache.http.NameValuePair;

import java.util.List;

/**
 * 实现网络交互接口并完成共性操作
 * Created by adolf_dong on 2016/8/12.
 */
public abstract class InteractiveBaseFragment extends BaseFragment implements HttpRespond {

    @Override
    public void onSuccessN(String action, String json) {
        onSuccess(action, json);
    }

    @Override
    public List<NameValuePair> getParamterN(String action) {
        return getParamter(action);
    }

    @Override
    public void operationFailedN(String action, String json) {
        if (!isAdded()) {
            return;
        }
        if (json.contains("message")) {
            showToat(CloudParseUtil.getJsonParm(json, "message"));
        }
    }

    /**
     * 获取错误码
     */
    protected int getWrongCode(String action, String json) {
        if (json.contains("status")) {
            return Integer.valueOf(CloudParseUtil.getJsonParm(json, "status"));
        }
        return 0;
    }

    @Override
    public void operationFailed(String action, String json) {
        if (!isAdded()) {
            return;
        }
        String msgType = CloudParseUtil.getJsonParm(json, CloudConstant.ParameterKey.MSG);
        String content = "";
        try {
            switch (Integer.parseInt(msgType)) {
                case 1001:
                    content = getResources().getString(R.string.error_code_1001);
                    break;
                case 4004:
                    content = getResources().getString(R.string.error_code_4004);
                    break;
                case 9999:
                    content = getResources().getString(R.string.error_code_9999);
                case 1010:
                    content = getResources().getString(R.string.user_not_exist);
                case 1011:
                    content = getResources().getString(R.string.user_psw_error);
                case 1012:
                    content = getResources().getString(R.string.verify_code_error);
                    break;
                default:
                    content = msgType;
                    break;
            }
        } catch (NumberFormatException nf) {
            nf.printStackTrace();
        }
        showToat(content);
    }


    @Override
    public void onFaild(String action, Exception e) {
        if (!isAdded()) {
            return;
        }
        disMissProgressDialog();
        showToat(getString(R.string.net_err));
    }

    @Override
    public void onFaild(String action, int state) {
        if (!isAdded()) {
            return;
        }
        showToat(getString(R.string.check_wifi_tips));
    }

    @Override
    public void onRespond(String action) {
        if (!isAdded()) {
            return;
        }
        disMissProgressDialog();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        HttpRequst.getHttpRequst().abort();
    }
}
