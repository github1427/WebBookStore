package com.li.book.web.servlet;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;
import com.li.book.domain.Book;
import com.li.book.service.BookService;
import com.li.page.Page;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "BookServlet",urlPatterns = "/BookServlet")
public class BookServlet extends BaseServlet{
    private BookService bookService=new BookService();
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
    //根据分类查找
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
        return "f:/jsps/book/list.jsp";
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
        return "f:/jsps/book/list.jsp";
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
        return "f:/jsps/book/list.jsp";
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
        return "f:/jsps/book/list.jsp";
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
        return "f:/jsps/book/list.jsp";
    }
    //根据图书编号加载单本图书
    public String load(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException{
        String bid =request.getParameter("bid");
        try {
            Book book = bookService.findByBid(bid);
            request.setAttribute("book",book);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "f:/jsps/book/desc.jsp";
    }
}
