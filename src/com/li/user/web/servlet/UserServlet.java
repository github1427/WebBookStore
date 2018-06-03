package com.li.user.web.servlet;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;
import com.li.user.domain.User;
import com.li.user.exception.UserException;
import com.li.user.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "UserServlet", urlPatterns = "/UserServlet")
public class UserServlet extends BaseServlet {
    private UserService userService = new UserService();

    public String ajaxValidateLoginname(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String loginname = req.getParameter("loginname");
        try {
            Boolean bool = userService.ajaxValidateLoginname(loginname);
            resp.getWriter().print(bool);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String ajaxValidateEmail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        try {
            Boolean bool = userService.ajaxValidateEmail(email);
            resp.getWriter().print(bool);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String ajaxValidateVerifyCode(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String verifyCode = req.getParameter("verifyCode");
        String vCode = (String) req.getSession().getAttribute("vCode");
        Boolean bool = verifyCode.equalsIgnoreCase(vCode);
        resp.getWriter().print(bool);
        return null;
    }

    public String regist(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //封装表单数据到Javabean中
        User formUser = CommonUtils.toBean(req.getParameterMap(), User.class);
        //校验表单参数
        Map<String, String> errors = validateForm(formUser, req.getSession());
        if (errors.size() != 0) {
            req.setAttribute("formUser", formUser);
            req.setAttribute("errors", errors);
            return "f:/jsps/user/regist.jsp";
        }
        try {
            userService.regist(formUser);
            req.setAttribute("code","success");
            req.setAttribute("msg","注册成功，请尽快激活");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "f:/jsps/msg.jsp";
    }

    private Map validateForm(User formUser, HttpSession session) {
        Map<String, String> errors = new HashMap<>();
        //校验用户名
        String loginname = formUser.getLoginname();
        if (loginname == null || loginname.trim().length() == 0) {
            errors.put("loginname", "用户名不能为空");
        } else if (loginname.length() < 3 || loginname.length() > 20) {
            errors.put("loginname", "用户名长度在3~20之间");
        } else try {
            if (!userService.ajaxValidateLoginname(loginname)) {
                errors.put("loginname", "用户名已占用");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //校验密码
        String loginpass = formUser.getLoginpass();
        if (loginpass == null || loginpass.trim().length() == 0) {
            errors.put("loginpass", "密码不能为空");
        } else if (loginname.length() < 3 || loginname.length() > 20) {
            errors.put("loginpass", "密码长度在3~20之间");
        }
        //校验确认密码
        String reloginpass = formUser.getReloginpass();
        if (reloginpass == null || reloginpass.trim().length() == 0) {
            errors.put("reloginpass", "确认密码不能为空");
        } else if (!loginpass.equals(reloginpass)) {
            errors.put("reloginpass", "两次输入的密码不一致");
        }
        //校验邮箱
        String email = formUser.getEmail();
        if (email == null || email.trim().length() == 0) {
            errors.put("email", "邮箱不能为空");
        } else if (!email.matches("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0-9_-]{2,3}){1,2})$")) {
            errors.put("email", "邮箱格式不正确");
        } else try {
            if (!userService.ajaxValidateEmail(email)) {
                errors.put("email", "邮箱已被占用");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //校验验证码
        String verifyCode = formUser.getVerifyCode();
        String vCode = (String) session.getAttribute("vCode");
        if (verifyCode == null || verifyCode.trim().length() == 0) {
            errors.put("verifyCode", "验证码不能为空不能为空");
        } else if (!verifyCode.equalsIgnoreCase(vCode)) {
            errors.put("verifyCode", "验证码错误");
        }
        //返回错误信息集合
        return errors;
    }
    //激活用户
    public String activation(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException{
        String activationCode=req.getParameter("activationCode");
        try {
            userService.activationUser(activationCode);
            req.setAttribute("code","success");
            req.setAttribute("msg","激活成功，请尽快登录");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (UserException e) {
            req.setAttribute("code","error");
            req.setAttribute("msg",e.getMessage());
            e.printStackTrace();
        }
        return "f:/jsps/msg.jsp";
    }
    //用户登录
    public String login(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        User formUser=CommonUtils.toBean(req.getParameterMap(),User.class);
        try {
            User user=userService.login(formUser);
            Map<String,String> errors=validateLogin(formUser,req.getSession());
            if (errors.size() != 0) {
                req.setAttribute("formUser", formUser);
                req.setAttribute("errors", errors);
                return "f:/jsps/user/login.jsp";
            }
            if (user==null){
                req.setAttribute("formUser",formUser);
                req.setAttribute("loginError","用户名或密码不正确");
                return "f:/jsps/user/login.jsp";
            }else {
                if (user.getStatus()==0){
                    req.setAttribute("formUser",formUser);
                    req.setAttribute("loginError","用户未激活");
                    return "f:/jsps/user/login.jsp";
                }else {
                    req.getSession().setAttribute("loginUser",user);
                    return "r:/index.jsp";
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }
    //校验登录表单
    private Map validateLogin(User formUser, HttpSession session) {
        Map<String, String> errors = new HashMap<>();
        //校验用户名
        String loginname = formUser.getLoginname();
        if (loginname == null || loginname.trim().length() == 0) {
            errors.put("loginname", "用户名不能为空");
        } else if (loginname.length() < 3 || loginname.length() > 20) {
            errors.put("loginname", "用户名长度在3~20之间");
        }
        //校验密码
        String loginpass = formUser.getLoginpass();
        if (loginpass == null || loginpass.trim().length() == 0) {
            errors.put("loginpass", "密码不能为空");
        } else if (loginname.length() < 3 || loginname.length() > 20) {
            errors.put("loginpass", "密码长度在3~20之间");
        }
        //校验验证码
        String verifyCode = formUser.getVerifyCode();
        String vCode = (String) session.getAttribute("vCode");
        if (verifyCode == null || verifyCode.trim().length() == 0) {
            errors.put("verifyCode", "验证码不能为空");
        } else if (!verifyCode.equalsIgnoreCase(vCode)) {
            errors.put("verifyCode", "验证码错误");
        }
        //返回错误信息集合
        return errors;
    }
    //修改密码
    public String updateLoginpass(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        User formUser = CommonUtils.toBean(req.getParameterMap(),User.class);
        if (formUser==null){
            req.setAttribute("msg","请先完成登录");
            return "f:/jsps/user/login.jsp";
        }
        //获取登录用户
        User loginUser = (User) req.getSession().getAttribute("loginUser");
        try {
            userService.updateLoginpass(loginUser.getUid(),formUser.getLoginpass(),formUser.getNewloginpass());
            req.setAttribute("code","success");
            req.setAttribute("msg","修改成功");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (UserException e) {
            req.setAttribute("msg",e.getMessage());
            req.setAttribute("formUser",formUser);
            e.printStackTrace();
            return "f:/jsp/user/pwd.jsp";
        }
        return "f:/jsps/msg.jsp";

    }
    //退出登录
    public String quit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        req.getSession().invalidate();
        return "r:/jsps/user/login.jsp";
    }
}
