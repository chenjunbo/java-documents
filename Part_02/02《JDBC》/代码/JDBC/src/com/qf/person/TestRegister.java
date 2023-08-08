package com.qf.person;

import java.util.Date;

public class TestRegister {
    public static void main(String[] args) {
        PersonServiceImpl personService = new PersonServiceImpl();
        Person person = new Person("coco",12, DateUtils.strToUtil("1997-07-07"),"Marry@123.com","北京市");
        personService.register(person);
    }
}
