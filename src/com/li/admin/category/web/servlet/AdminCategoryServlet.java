package com.li.admin.category.web.servlet;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;
import com.li.book.domain.Book;
import com.li.book.service.BookService;
import com.li.category.domain.Category;
import com.li.category.service.CategoryService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "AdminCategoryServlet",urlPatterns = "/admin/AdminCategoryServlet")
public class AdminCategoryServlet extends BaseServlet {
    private CategoryService categoryService=new CategoryService();
    private BookService bookService=new BookService();
    //查询所有分类
    public String findAllCategory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<Category> categories =categoryService.findAll();
            request.setAttribute("categories",categories);
            return "f:/adminjsps/admin/category/list.jsp";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    //添加一级分类
    public String addOneLevel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        Category category = CommonUtils.toBean(request.getParameterMap(),Category.class);
        category.setCid(CommonUtils.uuid());
        try {
            categoryService.addCategory(category);
            return findAllCategory(request,response);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    //查询当前的一级分类，并且保存发送到add.jsp
    public String queryOneLevel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        try {
            List<Category> categories =categoryService.findAll();
            for (Category category:categories){
                if (category.getParent()!=null){
                    categories.remove(category);
                }
            }
            request.setAttribute("categories",categories);
            return "f:/adminjsps/admin/category/add2.jsp";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    //添加二级分类
    public String addTwoLevel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        Category category =CommonUtils.toBean(request.getParameterMap(),Category.class);
        category.setCid(CommonUtils.uuid());
        Category parent =new Category();
        parent.setCid(request.getParameter("pid"));
        category.setParent(parent);
        try {
            categoryService.addCategory(category);
            return findAllCategory(request,response);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    //根据cid查找一级分类，保存并发送到edit.jsp
    public String findByCid1(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String cid =request.getParameter("cid");
        try {
            Category category =categoryService.findByCid(cid);
            request.setAttribute("category",category);
            return "f:/adminjsps/admin/category/edit.jsp";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    //修改一级分类
    public String updateOneLevel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        Category category =CommonUtils.toBean(request.getParameterMap(),Category.class);
        try {
            categoryService.updateOneLevel(category);
            return findAllCategory(request,response);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    //根据cid查询分类，将其保存，并将所有的一级分类保存 发送到edit2.jsp
    public String findByCid2(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String cid =request.getParameter("cid");
        try {
            Category category =categoryService.findByCid(cid);
            request.setAttribute("category",category);
            List<Category> categories =categoryService.findAll();
            for (Category categoryEle:categories){
                if (categoryEle.getParent()!=null){
                    categories.remove(category);
                }
            }
            request.setAttribute("categories",categories);
            return "f:/adminjsps/admin/category/edit2.jsp";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    //修改二级分类
    public String updateTwoLevel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        Category category =CommonUtils.toBean(request.getParameterMap(),Category.class);
        String pid =request.getParameter("pid");
        Category parent =new Category();
        parent.setCid(pid);
        category.setParent(parent);
        try {
            categoryService.updateTwoLevel(category);
            return findAllCategory(request,response);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    //删除一级分类
    public String deleteOneLevel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String cid =request.getParameter("cid");
        try {
            List<Category> categories =categoryService.findByPid(cid);
            if (categories.isEmpty()){
                categoryService.deleteLevel(cid);
                return findAllCategory(request,response);
            }else {
                request.setAttribute("msg","该类下还存在二级分类，无法删除");
                return "f:/adminjsps/msg.jsp";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    //删除二级分类
    public String deleteTwoLevel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String cid =request.getParameter("cid");
        try {
            List<Book> books =bookService.findBookByCid(cid);
            if (books.isEmpty()){
                categoryService.deleteLevel(cid);
                return findAllCategory(request,response);
            }else {
                request.setAttribute("msg","该类还存在图书，无法删除");
                return "f:/adminjsps/msg.jsp";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
