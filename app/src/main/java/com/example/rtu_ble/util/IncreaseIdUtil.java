package com.example.rtu_ble.util;

public class IncreaseIdUtil {
    private static int i = 0;

    public static String getId() {
        String res = numToHex16(i);
        i++;
        return res.toUpperCase();
    }

    private static String numToHex16(int b) {
        return String.format("%04x", b);
    }
}
