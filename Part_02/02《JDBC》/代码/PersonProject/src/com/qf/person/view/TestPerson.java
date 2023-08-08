package com.qf.person.view;

import com.qf.person.dao.PersonDao;
import com.qf.person.dao.impl.PersonDaoImpl;
import com.qf.person.entity.Person;
import com.qf.person.utils.DateUtils;

public class TestPerson {
    public static void main(String[] args) {
        PersonDao personDao = new PersonDaoImpl();
//        Person person = new Person("Gavin",19, DateUtils.strToUtil("1998-09-09"),"Gavin@163.com","北京市昌平区");
////        int result = personDao.insert(person);
////        System.out.println(result);
        Person person = personDao.select(6);
        System.out.println(person);
    }
}
