package com.qf.servletProject.service;

import com.qf.servletProject.entity.Manager;

public interface ManagerService {
    public Manager login(String username, String password);
}
