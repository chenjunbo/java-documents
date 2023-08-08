package com.qf.person;

import java.util.List;

public class TestPersonSelectAll {
    public static void main(String[] args) {
        PersonDaoImpl personDao = new PersonDaoImpl();
        List<Person> personList = personDao.selectAll();

        for(Person p : personList){
            System.out.println(p);
        }
    }
}
