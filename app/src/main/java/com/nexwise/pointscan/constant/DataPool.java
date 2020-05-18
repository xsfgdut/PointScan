package com.nexwise.pointscan.constant;

public class DataPool {
    private static String ipValue;

    public static String getIpValue() {
        return ipValue;
    }

    public static void setIpValue(String ipValue) {
        DataPool.ipValue = ipValue;
    }

}
