package com.nexwise.pointscan.constant;

/**
 * 网络请求参数
 * Created by adolf_dong on 2016/1/7.
 */
public interface CloudConstant {
    interface Source {
        String SERVER = "http://192.168.3.234/survey";
        String Common = "https://alicloud.on-bright.com/consumer/common?";
        String OAUTH = "https://alicloud.on-bright.com/oauth/token";
        String SERVER_IP = "http://192.168.3.234/";
        String network_IP = "http://183.3.145.138:1780/";
    }


    /**
     * 请求中的参数
     */
    interface ParameterKey {

        /**
         * 参数的key
         */
        String MSG = "msg";
        String USERNAME = "user_name";
        String ACCESS_TOKEN = "access_token";
        String PASS_WORD = "pwd";
        String LICENSE = "license";
        String OBOX_SERIAL_ID = "obox_serial_id";
        String TYPE = "type";
        String APPKEY = "appkey";
        String STATES = "state";
        String CONFIG = "config";
        /**
         * cmd指令
         */
        String CMD = "CMD";
        /**
         * 返回数据的key值
         */
        String IS_SUCCESS = "success";
        String PHONE = "phone";
        String APP_KEY = "appKey";
        String ZONE = "zone";
        String CODE = "code";
        String VERSION = "version";
        String COMMOND = "command";
        String FUNCTIONID = "functionId";
        String VALUE = "value";
        String TIMER = "timer";
        String TIMERID = "timerId";
        String DEVICEID = "deviceId";
        String CONFIGS = "configs";
        String TIMERS = "timers";
        String DEVICE_NAME = "deviceName";
        String PRODUCT_KEY = "productKey";
        String MOBILE = "mobile";

        String SHARESDKAPPKEY = "shareSdkAppkey";
        String GRANT_TYPE = "grant_type";
        String USERNAME_N = "username";
        String PASS_WORD_N = "password";

        String USER_NAME = "userName";
        String PWD = "password";
        String CAPTCHA = "captcha";
        String OLD_PWD = "oldPwd";
        String NEW_PWD = "newPwd";
        String PROVINCE = "province";
        String CITY = "city";
        String DISTRICT = "district";
        String NAME = "name";
        String STATE = "state";
        String PAGE = "page";
        String COUNT = "count";
        String ID = "id";
        String LNG = "lng";
        String LAT = "lat";
        String DETAIL = "detail";
        String IMAGES = "images";
        String CELLS = "cells";
        String TEL = "tel";
        String ADDRESS = "address";
        String REMARK = "remark";
        String KEY = "key";
    }

    /**
     * cmd的对应值
     */
    interface CmdValue {
        String REGISTER = "register";
        String SET_PASSWORD = "set_pwd";
        String FINDPWD = "pwd_forget";
        String QUERY_NODE_REAL_STATUS = "query_node_real_status";
        String SET_ALI_DEV = "set_ali_dev";
        String READ_ALI_DEV = "read_ali_dev";
        String QUERY_ALI_DEV = "query_ali_dev";
        String SET_TIMER = "set_timer";
        String SET_COUNTDOWN_TIMER = "set_countdown";
        /**
         * 注册阿里设备
         */
        String REGIST_ALIDEV = "regist_aliDev";
        String QUERY_TIMER = "query_timer";
        String QUERY_COUNTDOWN = "query_countdown";
        /**
         * 上传阿里设备配置
         */
        String UPLOAD_CONFIG = "upload_config";
        /**
         * 刷新token
         */
        String REFRESH_TOKEN = "refresh_token";
        /**
         * 注册前的申请
         */
        String REGISTER_BEFORE = "register_before";
        /**
         * 删除设备
         */
        String DELETE = "delete_ali_dev";

        String CAPTCHA = "captcha";//取登录验证码请求
        String LOGIN = "login";//登录请求
        String LOGOUT = "logout";//登出请求
        String PWD = "pwd";//修改密码请求
        String POINT_LIST = "point_list";//勘点列表查询
        String DETAIL_INFO = "detail_info";//勘点列表详细信息
        String ADD_POINT = "add_point";//新增勘点
        String UPDATE_INFO = "update_info";//新增/修改勘点详情
        String ADD_USER = "add_user";//新增用户
        String MODIFY_USER_PWD = "modify_user_pwd";//修改用户密码
        String QUERY_USER = "query_user";//查询用户列表
        String MODIFY_USER_INFO = "modify_user_info";//修改用户信息

    }
}