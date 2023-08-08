package com.qf.JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DeleteJdbc {
    public static void main(String[] args) throws Exception{
        //1.加载驱动
        Class.forName("com.mysql.jdbc.Driver");
        //2.获得连接对象
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/companydb","root","1234");
        //3.获得执行SQL的对象
        Statement statement = connection.createStatement();
        //4.执行SQL语句，并接收结果
        int result = statement.executeUpdate("delete from t_jobs where job_id = 'H5_mgr';");
        //5.处理结果
        if(result==1){
            System.out.println("删除成功！");
        }else{
            System.out.println("删除失败！");
        }
        //6.释放资源
        statement.close();
        connection.close();
    }
}
