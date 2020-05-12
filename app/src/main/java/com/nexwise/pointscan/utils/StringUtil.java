package com.nexwise.pointscan.utils;


import com.nexwise.pointscan.constant.OBConstant;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具
 * <p>
 * Created by adolf_dong on 2016/6/16.
 */
public class StringUtil {
    /**
     * @param str  判断字符
     * @param type 判断数据类型 0中英 1 phone  2 psw
     * @param len  字符串转成utf-8字节数组的长度
     * @return 合法
     */
    public static boolean isLegit(String str, int type, int len) {
//        String limitEx="^[a-zA-Z0-9\u4E00-\u9FA5]{0,5}|[a-zA-Z0-9]{0,16}$";
        // FIXME: 2016/6/12
        String limitEx;
        final int phone = 1;
        final int psw = 2;
        final int obox_psw = 3;
        switch (type) {
            case phone:
                limitEx = "[0-9]{11}";
                break;
            case psw:
                limitEx = "[a-zA-Z0-9]{8,16}";
                break;
            case obox_psw:
                limitEx = "[A-z0-9]{8}";
                break;
            default:
                limitEx = "[A-z0-9\u4e00-\u9fa5]{1,16}";
                break;
        }

        Pattern pattern = Pattern.compile(limitEx);
        Matcher m = pattern.matcher(str);
        boolean lenVial = false;
        try {
            lenVial = str.getBytes(OBConstant.StringKey.UTF8).length <= len;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return m.matches() && lenVial;
    }

    /**
     * 32位MD5加密
     *
     * @param content -- 待加密内容
     * @return
     */
    public static String md5Decode32(String content) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(content.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("NoSuchAlgorithmException", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UnsupportedEncodingException", e);
        }
        //对生成的16字节数组进行补零操作
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    /**
     * 16位MD5加密
     * 实际是截取的32位加密结果的中间部分(8-24位)
     *
     * @param content
     * @return
     */
    public static String md5Decode16(String content) {
        return md5Decode32(content).substring(8, 24);
    }

}
