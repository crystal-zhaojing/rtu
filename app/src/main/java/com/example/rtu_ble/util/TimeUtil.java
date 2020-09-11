package com.example.rtu_ble.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {

    public static String getSystemTime() {
        String res = "";
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        res = formatter.format(date).toString().substring(2, 14);
        return res;
    }
}
