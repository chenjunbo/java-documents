package com.qf.JDBC;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.Scanner;

public class LoginJdbc {
    public static void main(String[] args)throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入用户名");
        String username = scanner.nextLine();
        System.out.println("请输入密码");
        String password= scanner.nextLine();

        Class.forName("com.mysql.jdbc.Driver");

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/companydb","root","1234");

        Statement statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery("select * from users where username='"+username+"' and password= '"+password+"'");

        if(resultSet.next()){//查询到了数据。
            System.out.println("登录成功！");
        }else{
            System.out.println("登录失败！");
        }

        resultSet.close();
        statement.close();
        connection.close();
    }
}
