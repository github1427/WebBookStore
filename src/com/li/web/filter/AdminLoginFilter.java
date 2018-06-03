package com.li.web.filter;

import com.li.admin.admin.domain.Admin;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "AdminLoginFilter",urlPatterns = {"/adminjsps/admin/*","/admin/*"})
public class AdminLoginFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        Admin admin = (Admin) request.getSession().getAttribute("loginAdmin");
        if (admin==null){
            request.getRequestDispatcher("/adminjsps/login.jsp").forward(request,response);
        }else {
            chain.doFilter(request,response);
        }
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
