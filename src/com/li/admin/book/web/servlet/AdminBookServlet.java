package com.li.admin.book.web.servlet;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;
import com.li.book.domain.Book;
import com.li.book.service.BookService;
import com.li.category.domain.Category;
import com.li.category.service.CategoryService;
import com.li.page.Page;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "AdminBookServlet",urlPatterns = "/admin/AdminBookServlet")
public class AdminBookServlet extends BaseServlet{
    private CategoryService categoryService =new CategoryService();
    private BookService bookService = new BookService();
    //查询出所有分类 显示在left.jsp
    public String findAllCategory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<Category> categoryList =categoryService.findAll();
            request.setAttribute("categoryList",categoryList);
            return "f:/adminjsps/admin/book/left.jsp";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    //按分类查找图书
    public String findByCategory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int pageCode =getPageCode(request);
        String url=getURL(request);
        String cid=request.getParameter("cid");
        try {
            Page page=bookService.findByCid(cid,pageCode);
            page.setPageCode(pageCode);
            page.setUrl(url);
            request.setAttribute("pageBean",page);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "f:/adminjsps/admin/book/list.jsp";
    }
    //获取当前页码
    private int getPageCode(HttpServletRequest request){
        String pc=request.getParameter("pageCode");
        if (pc!=null&&!pc.trim().isEmpty()){
            return Integer.parseInt(pc);
        }else {
            return 1;
        }
    }
    //获取URL
    private String getURL(HttpServletRequest request){
        String url = request.getRequestURI()+"?"+request.getQueryString();
        int index = url.lastIndexOf("&pageCode=");
        if (index!=-1){
            url = url.substring(0,index);
        }
        return url;
    }

    //按作者查找
    public String findByAuthor(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int pageCode =getPageCode(request);
        String url=getURL(request);
        String author=request.getParameter("author");
        try {
            Page page=bookService.findByAuthor(author,pageCode);
            page.setPageCode(pageCode);
            page.setUrl(url);
            request.setAttribute("pageBean",page);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "f:/adminjsps/admin/book/list.jsp";
    }
    //按出版社查找
    public String findByPress(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int pageCode =getPageCode(request);
        String url=getURL(request);
        String press=request.getParameter("press");
        try {
            Page page=bookService.findByPress(press,pageCode);
            page.setPageCode(pageCode);
            page.setUrl(url);
            request.setAttribute("pageBean",page);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "f:/adminjsps/admin/book/list.jsp";
    }
    //按书名模糊查找
    public String findByBnameDim(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int pageCode =getPageCode(request);
        String url=getURL(request);
        String bname=request.getParameter("bname");
        try {
            Page page=bookService.findByBnameDim(bname,pageCode);
            page.setPageCode(pageCode);
            page.setUrl(url);
            request.setAttribute("pageBean",page);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "f:/adminjsps/admin/book/list.jsp";
    }
    //高级搜索多条件查找
    public String findByCriteria(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int pageCode =getPageCode(request);
        String url=getURL(request);
        Book book = CommonUtils.toBean(request.getParameterMap(),Book.class);
        try {
            Page page=bookService.findByCriteria(book,pageCode);
            page.setPageCode(pageCode);
            page.setUrl(url);
            request.setAttribute("pageBean",page);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "f:/adminjsps/admin/book/list.jsp";
    }
    //根据图书编号加载单本图书
    public String load(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException{
        String bid =request.getParameter("bid");
        try {
            Book book = bookService.findByBid(bid);
            List<Category> categories =categoryService.findAll();//所有一级分类
            request.setAttribute("categories",categories);
            Category category=categoryService.findByCid(book.getCategory().getCid());//当前图书所属二级分类
            book.setCategory(category);
            request.setAttribute("book",book);
            List<Category> categoriesChild=categoryService.findByPid(category.getParent().getCid());//所属一级分类下的所有二级分类
            request.setAttribute("categoriesChild",categoriesChild);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "f:/adminjsps/admin/book/desc.jsp";
    }
    //添加图书
    //第一步，查询分类，并保存发送到add.jsp
    public String addPre(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<Category> categories =categoryService.findAll();
            request.setAttribute("categories",categories);
            return "f:/adminjsps/admin/book/add.jsp";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }
   //异步查询二级分类，将结果构造成json字符串
    public String ajaxFindTwoLevel(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException {
        String pid =request.getParameter("pid");
        try {
            List<Category> categories =categoryService.findByPid(pid);
            String json = toJson(categories);
            response.getWriter().print(json);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String toJson(List<Category> categories) {
        StringBuilder stringBuilder =new StringBuilder();
        stringBuilder.append("[");
        for (int i =0;i<categories.size();i++){
            stringBuilder.append("{");
            Category category=categories.get(i);
            stringBuilder.append(toJson(category));
            stringBuilder.append("}");
            if (i<categories.size()-1){
                stringBuilder.append(",");
            }
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    private String toJson(Category category) {
        StringBuilder stringBuilder =new StringBuilder();
        stringBuilder.append("\"cid\":").append("\"").append(category.getCid()).append("\"");
        stringBuilder.append(",");
        stringBuilder.append("\"cname\":").append("\"").append(category.getCname()).append("\"");
        return stringBuilder.toString();
    }
    //修改图书信息
    public String updateBookDesc(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
        Book book=CommonUtils.toBean(request.getParameterMap(),Book.class);
        Category category=CommonUtils.toBean(request.getParameterMap(),Category.class);
        book.setCategory(category);
        try {
            bookService.updateBookDesc(book);
            request.setAttribute("msg","编辑成功");
            return "f:/adminjsps/msg.jsp";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    //删除图书
    public String deleteBook(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
        String bid =request.getParameter("bid");
        try {
            bookService.deleteBook(bid);
            request.setAttribute("msg","删除成功");
            return "f:/adminjsps/msg.jsp";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
