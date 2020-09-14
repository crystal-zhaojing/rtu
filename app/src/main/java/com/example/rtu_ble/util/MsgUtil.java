package com.example.rtu_ble.util;

public class MsgUtil {
    public static String getMsgLength(String msg) {
        return numToHex16(msg.length() / 2);
    }

    private static String numToHex16(int b) {
        return String.format("%02x", b);
    }
}
