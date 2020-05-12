package com.nexwise.pointscan.cloudNet;


import android.util.Base64;

import com.nexwise.pointscan.constant.OBConstant;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 算法工具
 * Created by adolf_dong on 2016/5/23.
 */
public class MathUtil {
    /**
     * 获得数组中的有效数据
     *
     * @param arry 源数组
     * @return 去掉无用位置的有效数组
     */
    public static byte[] validArray(byte[] arry) {
        String cache = null;
        try {
            cache = new String(arry, OBConstant.StringKey.UTF8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] goal = null;
        try {
            if (cache != null) {
                goal = cache.trim().getBytes(OBConstant.StringKey.UTF8);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return goal;
    }

    /**
     * 获取字节数据的去符号位值
     */
    public static int validByte(byte src) {
        return src & 0xff;
    }

    /**
     * 获取字节中的特定一个位值
     *
     * @param src   byte
     * @param index 0-7 取第几个位
     * @return 第index位的值  0 1
     */
    public static int byteIndexValid(byte src, int index) {
        return ((src & 0xff) >> index) & 0x01;
    }

    /**
     * 从直接的startPos开始取len长度的bit的值
     *
     * @param src      字节
     * @param startPos 开始位置，注意bit7-bit0的bit顺序
     * @param len      取的长度
     */
    public static int byteIndexValid(byte src, int startPos, int len) {
        if (startPos + len > 8) {
            throw new IndexOutOfBoundsException();
        }
        int srcInt = src & 0xff;
        int sum = 0;
        for (int i = 0; i < len; i++) {
            sum += ((srcInt >> (startPos + i)) & 0x01) << i;
        }
        return sum;
    }

    /**
     * 获取密码
     *
     * @param pwd 密码
     * @return 目标字符串
     */
    public static String endCodePwd(String pwd) {
        String md5src = new String(Base64.encode(pwd.getBytes(), Base64.NO_WRAP)) + pwd;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(md5src.getBytes("UTF-8"));
            byte[] encryption = md5.digest();
            StringBuilder strBuf = new StringBuilder();
            for (byte anEncryption : encryption) {
                if (Integer.toHexString(0xff & anEncryption).length() == 1) {
                    strBuf.append("0").append(Integer.toHexString(0xff & anEncryption));
                } else {
                    strBuf.append(Integer.toHexString(0xff & anEncryption));
                }
            }
            return strBuf.toString();
        } catch (NoSuchAlgorithmException e) {
            return "";
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }
}

