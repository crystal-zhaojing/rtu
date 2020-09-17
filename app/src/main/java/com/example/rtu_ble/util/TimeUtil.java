package com.example.rtu_ble.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间工具类
 */
public class TimeUtil {

    /**
     * 生成系统时间，并转换特定格式
     * @return
     */
    public static String getSystemTime() {
        String res = "";
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        res = formatter.format(date).toString().substring(2, 14);
        return res;
    }
}
