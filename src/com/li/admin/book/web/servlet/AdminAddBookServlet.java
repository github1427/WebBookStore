package com.li.admin.book.web.servlet;

import cn.itcast.commons.CommonUtils;
import com.li.book.domain.Book;
import com.li.book.service.BookService;
import com.li.category.domain.Category;
import com.li.category.service.CategoryService;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "AdminAddBookServlet",urlPatterns = "/admin/AdminAddBookServlet")
public class AdminAddBookServlet extends HttpServlet {
    private BookService bookService =new BookService();

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //commons-fileupload 上传三步
        //创建工具
        FileItemFactory fileItemFactory = new DiskFileItemFactory();
        //创建解析器对象
        ServletFileUpload servletFileUpload=new ServletFileUpload(fileItemFactory);
        servletFileUpload.setFileSizeMax(80*1024);//设置单个上传文件上限是80kb
        List<FileItem> fileItemList=null;
        try {
            fileItemList=servletFileUpload.parseRequest(req);
        } catch (FileUploadException e) {
            try {
                error("上传文件超过了80kb",req,resp);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            return;
        }
        //将list<FileItem> fileItemList 封装到book对象中
        Map<String,Object> map =new HashMap<>();
        for (FileItem fileItem:fileItemList){
            if (fileItem.isFormField()){
                map.put(fileItem.getFieldName(),fileItem.getString("UTF-8"));
            }
        }
        Book book = CommonUtils.toBean(map,Book.class);
        book.setBid(CommonUtils.uuid());
        Category category =CommonUtils.toBean(map,Category.class);
        book.setCategory(category);
        try {
            book.setImage_w("book_img/"+saveImagePath(fileItemList,1,req,resp));
            book.setImage_b("book_img/"+saveImagePath(fileItemList,2,req,resp));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            bookService.addBook(book);
            req.setAttribute("msg","添加图书成功");
            req.getRequestDispatcher("/adminjsps/msg.jsp").forward(req,resp);
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
    //保存错误信息
    private void error(String msg, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        req.setAttribute("msg",msg);
        req.setAttribute("categoryList",new CategoryService().findAll());
        req.getRequestDispatcher("/adminjsps/admin/book/add.jsp").forward(req,resp);
    }
    //将上传的图片保存起来，并将图片路径封装到book对象中
    private String saveImagePath(List<FileItem> fileItemList,int i,HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        //获取文件名
        FileItem fileItem=fileItemList.get(i);
        String filename =fileItem.getName();
        //截取文件名，因为有的浏览器上传的是绝对路径
        int index =filename.lastIndexOf("\\");
        if (index!=-1){
            filename = filename.substring(index+1);
        }
        //给文件名🏠UUID前缀，避免文件名重复
        filename =CommonUtils.uuid()+"_"+filename;
        //校验文件名的扩展名
        if (!filename.toLowerCase().endsWith(".jpg")){
            error("上传图片扩展名必须是jpg",req,resp);
            return null;
        }
        //校验图片尺寸
        //保存上传的图片，将图片new成图片对象
        //1 保存图片
        String savepath =req.getServletContext().getRealPath("/book_img");
        //2 创建目标文件
        File destFile =new File(savepath,filename);
        //3 保存文件，将文件保存在指定目录下，并删除临时文件
        try {
            fileItem.write(destFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //校验尺寸
        ImageIcon imageIcon=new ImageIcon(destFile.getAbsolutePath());
        Image image=imageIcon.getImage();
        if (image.getWidth(null)>350||image.getHeight(null)>350){
            error("上传的图片大小超过350*350",req,resp);
            destFile.delete();
            return null;
        }
        return filename;
    }
}
