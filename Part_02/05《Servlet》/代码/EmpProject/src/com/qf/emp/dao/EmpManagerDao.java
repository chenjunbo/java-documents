package com.qf.emp.dao;

import com.qf.emp.entity.EmpManager;

public interface EmpManagerDao {
    public EmpManager select(String username);
}
