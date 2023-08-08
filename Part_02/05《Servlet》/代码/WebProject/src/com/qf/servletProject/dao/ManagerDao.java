package com.qf.servletProject.dao;

import com.qf.servletProject.entity.Manager;

public interface ManagerDao {
    public Manager select(String username);
}
