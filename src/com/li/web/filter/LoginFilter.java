package com.li.web.filter;

import com.li.user.domain.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "LoginFilter",urlPatterns = {"/jsps/cart/*","/jsps/order/*"},servletNames ={"CartItemServlet","OrderServlet"})
public class LoginFilter implements Filter {
    @Override
    public void destroy() {

    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        User user = (User) request.getSession().getAttribute("loginUser");
        if (user==null){
            request.getRequestDispatcher("/jsps/user/login.jsp").forward(request,response);
        }else {
            chain.doFilter(request,response);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException   {

    }
}
