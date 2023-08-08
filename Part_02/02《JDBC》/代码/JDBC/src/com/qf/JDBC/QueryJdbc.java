package com.qf.JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class QueryJdbc {
    public static void main(String[] args) throws Exception{
        //1.注册驱动
        Class.forName("com.mysql.jdbc.Driver");
        //2.获取连接对象
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/companydb","root","1234");
        //3.获取执行SQL语句的对象
        Statement statement = connection.createStatement();
        //4.执行SQL语句，接收结果
        ResultSet resultSet = statement.executeQuery("select * from t_jobs");
        //5.处理结果
        while(resultSet.next()){//判断下一行是否有数据
            //对当前行每列数据进行获取  根据列的编号
            String job_id = resultSet.getString(1);
            String job_title = resultSet.getString(2);
            String min_salary = resultSet.getString(3);
            String max_salary = resultSet.getString(4);
            System.out.println(job_id+"\t"+job_title+"\t"+min_salary+"\t"+max_salary);
        }
        //6.释放资源
        resultSet.close();
        statement.close();
        connection.close();
    }
}
