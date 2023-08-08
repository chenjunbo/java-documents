package com.qf.servletProject.service;

import com.qf.servletProject.entity.Admin;

import java.util.List;

public interface AdminService {
    public Admin login(String username, String password);

    public List<Admin> showAllAdmin();
}
