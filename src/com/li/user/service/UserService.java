package com.li.user.service;

import cn.itcast.commons.CommonUtils;
import cn.itcast.mail.Mail;
import cn.itcast.mail.MailUtils;
import com.li.user.dao.UserDao;
import com.li.user.domain.User;
import com.li.user.exception.UserException;

import javax.mail.MessagingException;
import javax.mail.Session;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UserService {
    private UserDao userDao=new UserDao();
    //校验用户名是否被注册
    public boolean ajaxValidateLoginname(String loginname) throws SQLException {
       return userDao.ajaxValidateLoginname(loginname);
    }
    //检查邮箱是否已被注册
    public boolean ajaxValidateEmail(String email) throws SQLException {
      return userDao.ajaxValidateEmail(email);
    }
    //注册用户
    public void regist(User user) throws SQLException, IOException, MessagingException {
        user.setUid(CommonUtils.uuid());
        user.setStatus(0);
        user.setActivationCode(CommonUtils.uuid()+CommonUtils.uuid());
        userDao.addUser(user);
        sendActiveMail(user);
    }
    //发送激活邮件
    private void sendActiveMail(User user) throws IOException, MessagingException {
        String host= ResourceBundle.getBundle("email_template").getString("host");
        String username=ResourceBundle.getBundle("email_template").getString("username");
        String password=ResourceBundle.getBundle("email_template").getString("password");
        Session session= MailUtils.createSession(host,username,password);
        String from=ResourceBundle.getBundle("email_template").getString("from");
        String to=user.getEmail();
        String subject=ResourceBundle.getBundle("email_template").getString("subject");
        String content=ResourceBundle.getBundle("email_template").getString("content");
        Mail mail=new Mail(from,to,subject,content);
        MailUtils.send(session,mail);
    }
    //激活用户
    public void activationUser(String activationCode) throws SQLException, UserException {
       User user = userDao.findByActivationCode(activationCode);
       if (null == user){
           throw new UserException("无效的激活码");
       }
       if (user.getStatus()==1){
           throw new UserException("您已激活，二次激活不合法");
       }
       userDao.updateStatus(user.getUid(),1);
    }
    //用户登录
    public User login(User user) throws SQLException {
        return userDao.findByLoginnameAndLoginpass(user.getLoginname(),user.getLoginpass());
    }
    //修改用户密码
    public void updateLoginpass(String uid,String oldpass,String newpass) throws SQLException, UserException {
        Boolean flag =userDao.findByUidAndLoginpass(uid,oldpass);
        if (!flag){
            throw new UserException("原密码错误");
        }
        userDao.updateLoginpass(uid,newpass);
    }
}
