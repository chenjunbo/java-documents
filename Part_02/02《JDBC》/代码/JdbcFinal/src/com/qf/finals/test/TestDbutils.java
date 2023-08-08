package com.qf.finals.test;

import com.qf.finals.dao.UserDao;
import com.qf.finals.dao.impl.UserDaoImpl;
import com.qf.finals.entity.User;

import java.util.List;

public class TestDbutils {
    public static void main(String[] args) {
        UserDao userDao = new UserDaoImpl();
        User user = new User(5,"Aaron","1234","北京市延庆区","12345678901");
//        int result = userDao.insert(user);
//        System.out.println(result);
        //修改
//        int result = userDao.update(user);
//        System.out.println(result);
        //删除
//        int result = userDao.delete(5);
//        System.out.println(result);
        //查询单个
//        User users = userDao.select(1);
//        System.out.println(users);
//        List<User> userList = userDao.selectAll();
//        userList.forEach(System.out::println);
        long count= userDao.selectUserNums();
        System.out.println("一共有："+count);
    }
}
