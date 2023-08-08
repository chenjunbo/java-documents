package com.qf.JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class TestJDBC {
    public static void main(String[] args)throws Exception {
        //1.注册驱动 加载驱动
        Class.forName("com.mysql.jdbc.Driver");

        //2.获得连接
        String url = "jdbc:mysql://localhost:3306/companydb";
        String user = "root";
        String password="1234";
        Connection connection =  DriverManager.getConnection(url,user,password);

        if(connection!=null){
            System.out.println("连接到数据库啦！");
        }else{
            System.out.println("连接失败！");
        }

        //3.获得执行SQL语句的对象
        Statement statement = connection.createStatement();

        //4.编写SQL语句，执行SQL语句
        String sql = "insert into t_jobs(job_id,job_title,min_salary,max_salary) values('H5_Mgr','H5_Manager',4000,10000)";

        int result = statement.executeUpdate(sql);//DML操作调用的方法

        //5.处理接收结果
        if(result == 1 ){
            System.out.println("成功！");
        }else{
            System.out.println("失败！");
        }

        //6.释放资源 先开后关
        statement.close();
        connection.close();
    }
}
