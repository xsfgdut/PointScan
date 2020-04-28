package com.nexwise.pointscan.cloudNet;


import com.google.gson.Gson;
import com.nexwise.pointscan.bean.Detail;
import com.nexwise.pointscan.constant.CloudConstant;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GetParameter {
    /**
     * 口令
     */
    public static String ACCESSTOKEN;
    /**
     * 口令
     */
    public static String COOKIE;

    /**
     * 注册
     *
     * @param name 用户名
     * @param psw  密码
     */
    public static List<NameValuePair> onRegister(String name, String psw, String license) {
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.CMD, CloudConstant.CmdValue.REGISTER));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.USERNAME, name));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.PASS_WORD, psw));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.LICENSE, license));
        return nvps;

    }

    /**
     * 登录
     *
     * @param name 用户名
     * @param psw  密码
     */
    public static List<NameValuePair> onLoginN(String name, String psw) {
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.GRANT_TYPE, CloudConstant.ParameterKey.PASS_WORD_N));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.USERNAME_N, name));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.PASS_WORD_N, MathUtil.endCodePwd(psw)));
        return nvps;
    }

    /**
     * 登录
     *
     * @param name 用户名
     * @param psw  密码
     */
    public static List<NameValuePair> onLogin(String name, String psw) {
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.CMD, CloudConstant.CmdValue.LOGIN));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.USERNAME, name));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.PASS_WORD, psw));
        return nvps;
    }

    /**
     * 修改密码
     *
     * @param type 用户类型
     * @param pwd  密码
     */
    public static List<NameValuePair> setPassword(String type, String serialId, String pwd) {
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.CMD, CloudConstant.CmdValue.SET_PASSWORD));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.ACCESS_TOKEN, ACCESSTOKEN));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.TYPE, type));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.PASS_WORD, pwd));
        if (type.equals("01")) {
            nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.OBOX_SERIAL_ID, serialId));
        }
        return nvps;
    }

    /**
     * 找回密码
     *
     * @param phone        手机号码
     * @param code         验证码
     * @param countryPhone 国家电话号码
     * @param isOther      是否其他用户定制app，目前是民爆
     */
    public static List<NameValuePair> onFindPwd(String phone, String code, String countryPhone, boolean isOther) {
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.CMD, CloudConstant.CmdValue.FINDPWD));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.PHONE, phone));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.APP_KEY, isOther ? "1c7cd49c673f6" : "16eff033f92d9"));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.ZONE, countryPhone));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.CODE, code));
        return nvps;
    }


    /**
     * 查询设备
     */
    public static List<NameValuePair> queryAliDevice() {
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.CMD, CloudConstant.CmdValue.QUERY_ALI_DEV));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.ACCESS_TOKEN, ACCESSTOKEN));
        return nvps;
    }

    /**
     * 读取设备状态
     */
    public static List<NameValuePair> readAliDevStatus(String functionId, String deviceId) {
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.CMD, CloudConstant.CmdValue.READ_ALI_DEV));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.ACCESS_TOKEN, ACCESSTOKEN));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.FUNCTIONID, functionId));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.DEVICEID, deviceId));
        return nvps;
    }

    /**
     * 设置设备状态
     */
    public static List<NameValuePair> setAliDevStatus(String value, String deviceId) {
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.CMD, CloudConstant.CmdValue.SET_ALI_DEV));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.ACCESS_TOKEN, ACCESSTOKEN));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.VALUE, value));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.DEVICEID, deviceId));
        return nvps;
    }

    /**
     * 删除设备
     *
     * @param deviceId 设备序列号
     */
    public static List<NameValuePair> delAliDev(String deviceId) {
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.CMD, CloudConstant.CmdValue.DELETE));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.ACCESS_TOKEN, ACCESSTOKEN));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.DEVICEID, deviceId));
        return nvps;
    }

    /**
     * 设置设备定时
     * command /add/delete/enable/disable
     */
    public static List<NameValuePair> setDevTimer(String command, int timeId, int state, String deviceId, String value, String timer) {
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.CMD, CloudConstant.CmdValue.SET_TIMER));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.ACCESS_TOKEN, ACCESSTOKEN));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.COMMOND, command));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.TIMERID, String.valueOf(timeId)));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.STATES, String.valueOf(state)));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.DEVICEID, deviceId));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.VALUE, value));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.TIMER, timer));
        return nvps;
    }

    /**
     * 查询设备定时
     */
    public static List<NameValuePair> queryDevTimer(String deviceId) {
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.CMD, CloudConstant.CmdValue.QUERY_TIMER));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.ACCESS_TOKEN, ACCESSTOKEN));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.DEVICEID, deviceId));
        return nvps;
    }

    /**
     * 设置设备倒计时
     * command /add/delete
     */
    public static List<NameValuePair> setDevCountDownTimer(String command, String deviceId, String value, String timer) {
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.CMD, CloudConstant.CmdValue.SET_COUNTDOWN_TIMER));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.ACCESS_TOKEN, ACCESSTOKEN));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.COMMOND, command));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.DEVICEID, deviceId));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.VALUE, value));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.TIMER, timer));
        return nvps;
    }

    /**
     * 查询设备倒计时
     */
    public static List<NameValuePair> queryDevCountDown(String deviceId) {
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.CMD, CloudConstant.CmdValue.QUERY_COUNTDOWN));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.ACCESS_TOKEN, ACCESSTOKEN));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.DEVICEID, deviceId));
        return nvps;
    }

    /**
     * 注册阿里设备
     *
     * @param zone 时区
     * @param type 设备类型
     * @return 请求参数列表
     */
    public static List<NameValuePair> registAliDev(String zone, String type) {
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.CMD, CloudConstant.CmdValue.REGIST_ALIDEV));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.ACCESS_TOKEN, ACCESSTOKEN));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.ZONE, zone));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.TYPE, type));
        return nvps;
    }


    /**
     * 注册前的申请
     *
     * @param appKey 设备序列号
     */
    public static List<NameValuePair> onRegisterBefore(String appKey) {
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.CMD, appKey));
        return nvps;
    }

    /**
     * 注册
     *
     * @param mobile
     * @param pwd
     * @param code
     * @return
     */
    public static List<NameValuePair> onRegisterN(String mobile, String pwd, String code) {
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.MOBILE, mobile));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.PASS_WORD, MathUtil.endCodePwd(pwd)));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.CODE, code));
        return nvps;
    }

    /**
     * 找回密码
     *
     * @param phone        手机号码
     * @param code         验证码
     * @param countryPhone 国家电话号码
     * @param isOther      是否其他用户定制app，目前是民爆
     */
    public static List<NameValuePair> onFindPwdN(String phone, String code, String countryPhone, boolean isOther) {
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.MOBILE, phone));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.SHARESDKAPPKEY, isOther ? "1c7cd49c673f6" : "16eff033f92d9"));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.ZONE, countryPhone));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.CODE, code));
        return nvps;
    }

    /**
     * 修改密码
     *
     * @param type 用户类型
     * @param pwd  密码
     */
    public static List<NameValuePair> setPasswordN(String type, String serialId, String pwd) {
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.CMD, CloudConstant.CmdValue.SET_PASSWORD));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.ACCESS_TOKEN, ACCESSTOKEN));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.TYPE, type));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.PASS_WORD, MathUtil.endCodePwd(pwd)));
        if (type.equals("01")) {
            nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.OBOX_SERIAL_ID, serialId));
        }
        return nvps;
    }



    /**
     * 取登录验证码请求
     *
     * @param type 验证码类型
     */
    public static List<NameValuePair> getCaptcha(String type) {
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.CMD, CloudConstant.CmdValue.CAPTCHA));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.TYPE, type));
        return nvps;
    }

    /**
     * 登录请求
     *
     * @param userName 用户名
     * @param password 密码
     * @param captcha  验证码
     */
    public static List<NameValuePair> login(String userName, String password, String captcha) {
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.CMD, CloudConstant.CmdValue.LOGIN));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.USER_NAME, userName));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.PWD, password));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.CAPTCHA, captcha));
        return nvps;
    }

    /**
     * 登出请求
     */
    public static List<NameValuePair> logout() {
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.CMD, CloudConstant.CmdValue.LOGOUT));
        return nvps;
    }

    /**
     * 修改密码请求
     *
     * @param oldPwd  旧密码MD5后
     * @param newPwd  新密码MD5后
     * @param captcha 验证码
     */
    public static List<NameValuePair> modifyPwd(String oldPwd, String newPwd, String captcha) {
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.CMD, CloudConstant.CmdValue.LOGIN));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.OLD_PWD, oldPwd));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.NEW_PWD, newPwd));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.CAPTCHA, captcha));
        return nvps;
    }

    /**
     * 勘点列表查询
     *
     * @param province 省
     * @param city     市
     * @param district 区县
     * @param name     点位名称
     * @param state    点位状态 1：未勘
     *                 2：已勘
     *                 3：已安装
     *                 -1：已拆除(废除)
     * @param page     第几页
     * @param count    每页记录数
     */
    public static List<NameValuePair> queryPointList(String province, String city, String district, String name, int state, int page, int count) {
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.CMD, CloudConstant.CmdValue.POINT_LIST));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.PROVINCE, province));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.CITY, city));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.DISTRICT, district));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.NAME, name));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.STATE, String.valueOf(state)));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.PAGE, String.valueOf(page)));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.COUNT, String.valueOf(count)));
        return nvps;
    }

    /**
     * 勘点列表详细信息
     *
     * @param id 站点编号
     */
    public static List<NameValuePair> queryPointDetail(int id) {
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.CMD, CloudConstant.CmdValue.DETAIL_INFO));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.ID, String.valueOf(id)));
        return nvps;
    }

    /**
     * 新增勘点
     *
     * @param id       站点编号
     * @param province 省
     * @param city     市
     * @param district 区县
     * @param name     点位名称
     * @param state    勘察状态 1：未勘
     *                 2：已勘
     *                 3：已安装
     *                 -1：已拆除(废除)
     * @param lng     经度
     * @param lat     纬度
     */
    public static List<NameValuePair> addPoint(int id, String province, String city, String district, String name, int state, double lng, double lat) {
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.CMD, CloudConstant.CmdValue.ADD_POINT));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.ID, String.valueOf(id)));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.PROVINCE, province));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.CITY, city));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.DISTRICT, district));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.NAME, name));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.STATE, String.valueOf(state)));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.LNG, String.valueOf(lng)));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.LAT, String.valueOf(lat)));
        return nvps;
    }

    /**
     * 新增/修改勘点详情
     *
     * @param detail Json字符串
     * @param images 现场图片文件
     * @param cells 扫频信息文件
     */
    public static List<NameValuePair> updatePointInfo(Detail detail, List<File> images, List<File> cells) {
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.CMD, CloudConstant.CmdValue.UPDATE_INFO));
        Gson gson_detail = new Gson();
        String detail_json = gson_detail.toJson(detail);
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.DETAIL, detail_json));
        Gson gson = new Gson();
        String images_json = gson.toJson(images);
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.IMAGES, images_json));
        Gson gson1 = new Gson();
        String cells_json = gson1.toJson(cells);
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.CELLS, cells_json));
        return nvps;
    }

    /**
     * 新增用户
     *
     * @param userName 用户名
     * @param password 密码
     * @param name 姓名
     * @param tel 联系号码
     * @param address 地址
     * @param remark 备注
     */
    public static List<NameValuePair> addUser(String userName,String password,String name,String tel,String address,String remark) {
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.CMD, CloudConstant.CmdValue.ADD_USER));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.USER_NAME, userName));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.PWD, password));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.NAME, name));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.TEL, tel));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.ADDRESS, address));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.REMARK, remark));
        return nvps;
    }

    /**
     * 修改用户密码
     *
     * @param userName 用户名
     * @param password 密码
     */
    public static List<NameValuePair> modifyUserPwd(String userName,String password) {
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.CMD, CloudConstant.CmdValue.MODIFY_USER_PWD));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.USER_NAME, userName));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.PWD, password));
        return nvps;
    }

    /**
     * 查询用户列表
     *
     * @param key 模糊查询关键字
     * @param count 每页记录数
     * @param page 第几页
     */
    public static List<NameValuePair> queryUserList(String key,String count,String page) {
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.CMD, CloudConstant.CmdValue.QUERY_USER));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.KEY, key));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.COUNT, count));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.PAGE, page));
        return nvps;
    }

    /**
     * 修改用户信息
     * @param userName 用户名
     * @param name 姓名
     * @param tel 联系号码
     * @param address 地址
     * @param remark 备注
     */
    public static List<NameValuePair> modifyUserInfo(String userName,String name,String tel,String address,String remark) {
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.CMD, CloudConstant.CmdValue.MODIFY_USER_INFO));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.USER_NAME, userName));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.NAME, name));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.TEL, tel));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.ADDRESS, address));
        nvps.add(new BasicNameValuePair(CloudConstant.ParameterKey.REMARK, remark));
        return nvps;
    }

}
