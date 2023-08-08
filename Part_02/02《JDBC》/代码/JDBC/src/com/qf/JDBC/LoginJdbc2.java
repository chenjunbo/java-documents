package com.qf.JDBC;

import java.sql.*;
import java.util.Scanner;

public class LoginJdbc2 {
    public static void main(String[] args)throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入用户名");
        String username = scanner.nextLine();
        System.out.println("请输入密码");
        String password= scanner.nextLine();

      Connection connection = DBUtils.getConnection();

        //获得PreparedStatement对象 预编译SQL语句
        PreparedStatement preparedStatement = connection.prepareStatement("select * from users where username =? and password = ?;");

        //为?占位符 赋值
        preparedStatement.setString(1,username);
        preparedStatement.setString(2,password);

        //执行SQL语句，并接收结果
        ResultSet resultSet = preparedStatement.executeQuery();

        //处理结果
        if(resultSet.next()){
            System.out.println("登录成功！");
        }else{
            System.out.println("登录失败！");
        }

        DBUtils.closeAll(connection,preparedStatement,resultSet);
    }
}
