package com.qf.finals.test;

import com.qf.finals.utils.DbUtils;

import java.sql.Connection;

public class TestPool {
    public static void main(String[] args)throws Exception {
        for(int i = 1;i<=20;i++){
            Connection connection = DbUtils.getConnection();
            System.out.println(connection);
            //关闭--->放回池中
            connection.close();//调用的是DruidPooledConnection实现类里的close()
        }
    }
}
