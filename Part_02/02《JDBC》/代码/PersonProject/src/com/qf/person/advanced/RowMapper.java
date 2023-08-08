package com.qf.person.advanced;

import java.sql.ResultSet;

/**
 * 约束封装对象的ORM
 */
public interface RowMapper<T> {
    public T getRow(ResultSet resultSet);
}
