package com.li.admin.admin.web.AdminServlet;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;
import com.li.admin.admin.domain.Admin;
import com.li.admin.admin.service.AdminService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

@WebServlet(name = "AdminServlet",urlPatterns = "/AdminServlet")
public class AdminServlet extends BaseServlet {
    private AdminService adminService =new AdminService();
    //管理员登录
    public String loginAdmin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String[]> map =request.getParameterMap();
        Admin adminform = CommonUtils.toBean(map,Admin.class);
        try {
            Admin admindb=adminService.findAdmin(adminform);
            if (admindb==null){
                request.setAttribute("msg","用户名或密码错误");
                return "f:/adminjsps/login.jsp";
            }else {
                request.getSession().setAttribute("loginAdmin",admindb);
                return "f:/adminjsps/admin/index.jsp";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
