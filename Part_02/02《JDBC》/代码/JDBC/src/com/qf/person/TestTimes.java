package com.qf.person;

import java.text.SimpleDateFormat;

public class TestTimes {
    public static void main(String[] args)throws Exception {
        //获得当前系统的日期时间
        System.out.println(new java.util.Date());
        //字符串：自定义日期
        String str="1999-09-09";

        //将字符串转换成Util.Date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //parse方法转换为util.Date
        java.util.Date date = sdf.parse(str);
        System.out.println(date);
        //format方法转换为String类型
        String dates = sdf.format(new java.util.Date());
        System.out.println(dates);
        //sql.Date 不支持字符串转换  只支持毫秒值创建
        //通过util.Date拿到指定日期的毫秒值，转换为sql.Date
        java.sql.Date sqlDate =  new java.sql.Date(date.getTime());
        System.out.println(sqlDate);
    }
}
