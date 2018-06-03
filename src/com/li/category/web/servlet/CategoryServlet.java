package com.li.category.web.servlet;

import cn.itcast.servlet.BaseServlet;
import com.li.category.domain.Category;
import com.li.category.service.CategoryService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "CategoryServlet",urlPatterns ="/CategoryServlet")
public class CategoryServlet extends BaseServlet {
    CategoryService categoryService=new CategoryService();
    public String findAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<Category> categoryList=categoryService.findAll();
            request.setAttribute("categoryList",categoryList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "f:/jsps/left.jsp";
    }
}
