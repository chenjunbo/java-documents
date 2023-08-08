package com.qf.person;

public class PersonServiceImpl {
    //注册！ 一个业务功能由多个DAO的访问操作组成
    public void register(Person person) {
        PersonDaoImpl personDao = new PersonDaoImpl();
        //1.查询用户是否存在
        Person persons = personDao.select(person.getName());
        //2.不存在，新增
        if (persons == null) {
            personDao.insert(person);
            System.out.println("注册成功！");
        } else {
            //3.存在，就返回已注册！
            System.out.println("该用户已存在！");
        }
    }
    public void updateInfo(Person person){
        //查用户是否存在
        //修改信息
        //提示用户不存在！
    }
}
