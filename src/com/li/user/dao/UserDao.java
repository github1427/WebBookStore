package com.li.user.dao;

import cn.itcast.jdbc.TxQueryRunner;
import com.li.user.domain.User;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;

public class UserDao {
    private QueryRunner queryRunner=new TxQueryRunner();
    //用户名是否在数据库中存在
    public boolean ajaxValidateLoginname(String loginname) throws SQLException {
        String sql="select count(*) from t_user where loginname=?";
        Number count= (Number) queryRunner.query(sql,new ScalarHandler(),loginname);
        return count.intValue()==0;
    }
    //邮箱是否在数据库中存在
    public boolean ajaxValidateEmail(String email) throws SQLException {
        String sql="select count(*) from t_user where email=?";
        Number count= (Number) queryRunner.query(sql,new ScalarHandler(),email);
        return count.intValue()==0;
    }
    //添加注册用户信息
    public void addUser(User user) throws SQLException {
        String sql="insert into t_user values(?,?,?,?,?,?)";
        Object [] param={user.getUid(),user.getLoginname(),user.getLoginpass(),user.getEmail(),user.getStatus(),user.getActivationCode()};
        queryRunner.update(sql,param);
    }
    //根据激活码查找用户
    public User findByActivationCode(String activationCode) throws SQLException {
        String sql="select * from t_user where activationCode = ?";
        return queryRunner.query(sql,new BeanHandler<User>(User.class),activationCode);
    }
    //修改用户激活状态
    public void updateStatus(String uid,int status) throws SQLException {
        String sql="update t_user set status = ? where uid =?";
        queryRunner.update(sql,status,uid);
    }
    //根据用户名和密码查询用户
    public User findByLoginnameAndLoginpass(String loginname,String loginpass) throws SQLException {
        String sql="select * from t_user where loginname = ? and loginpass = ?";
        return queryRunner.query(sql,new BeanHandler<User>(User.class),loginname,loginpass);
    }
    //根据用户UID和用户密码查询用户是否存在
    public boolean findByUidAndLoginpass(String uid,String oldpass) throws SQLException {
        String sql="select count(*) from t_user where uid = ? and loginpass = ?";
        Number number = (Number) queryRunner.query(sql,new ScalarHandler(),uid,oldpass);
        return number.intValue()>0;

    }
    //修改密码
    public void updateLoginpass(String uid,String newpass) throws SQLException {
        String sql="update t_user set loginpass = ? where uid = ?";
        queryRunner.update(sql,newpass,uid);
    }
}
