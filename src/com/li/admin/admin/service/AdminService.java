package com.li.admin.admin.service;

import com.li.admin.admin.dao.AdminDao;
import com.li.admin.admin.domain.Admin;

import java.sql.SQLException;

public class AdminService {
    AdminDao adminDao =new AdminDao();
    //管理员登录
    public Admin findAdmin(Admin admin) throws SQLException {
        return adminDao.findAdmin(admin);
    }
}
