package com.example.rtu_ble.util;

/**
 * 报文工具类
 */
public class MsgUtil {
    /**
     * 返回16进制报文长度
     * @param msg
     * @return
     */
    public static String getMsgLength(String msg) {
        return numToHex16(msg.length() / 2);
    }

    private static String numToHex16(int b) {
        return String.format("%02x", b);
    }
}
