package com.qf.person;

public class TestPersonSelect {
    public static void main(String[] args) {
        PersonDaoImpl personDao = new PersonDaoImpl();
        Person person = personDao.select(2);
        if(person!=null){
            System.out.println(person);
        }else{
            System.out.println("没有查到该用户！");
        }

    }
}
