package com.li.admin.admin.dao;

import cn.itcast.jdbc.TxQueryRunner;
import com.li.admin.admin.domain.Admin;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.SQLException;

public class AdminDao {
    private QueryRunner queryRunner = new TxQueryRunner();

    //根据给定管理员对象，在数据库中查找并返回该管理员
    public Admin findAdmin(Admin admin) throws SQLException {
        String sql = "select * from t_admin where adminname =? and adminpwd =?";
        Admin admindb = queryRunner.query(sql, new BeanHandler<Admin>(Admin.class), admin.getAdminname(), admin.getAdminpwd());
        return admindb;
    }
}
