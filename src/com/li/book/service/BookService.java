package com.li.book.service;

import com.li.book.dao.BookDao;
import com.li.book.domain.Book;
import com.li.page.Page;

import java.sql.SQLException;
import java.util.List;

public class BookService {
    private BookDao bookDao=new BookDao();
    //按分类查询
    public Page findByCid(String cid,int pageCode) throws SQLException {
        return bookDao.findByCid(cid,pageCode);
    }
    //按图书名查询
    public Page findByBname(String bname , int pageCode) throws SQLException {
        return bookDao.findByBname(bname,pageCode);
    }
    //按作者查询
    public Page findByAuthor(String author,int pageCode) throws SQLException{
        return bookDao.findByAuthor(author,pageCode);
    }
    //按出版社查询
    public Page findByPress(String press,int pageCode) throws SQLException{
        return bookDao.findByPress(press,pageCode);
    }
    //按书名模糊查询
    public Page findByBnameDim(String bname,int pageCode) throws SQLException{
        return bookDao.findByBnameDim(bname,pageCode);
    }
    //高级搜索多条件查询
    public Page findByCriteria(Book book,int pageCode) throws SQLException{
        return bookDao.findByCriteria(book,pageCode);
    }
    //根据图书编号查找
    public Book findByBid(String bid) throws SQLException{
        return bookDao.findByBid(bid);
    }
    //根据二级分类cid查找图书
    public List<Book> findBookByCid(String cid) throws SQLException {
        return bookDao.findBookByCid(cid);
    }
    //添加图书
    public void addBook(Book book) throws SQLException {
        bookDao.addBook(book);
    }
    //修改图书信息
    public void updateBookDesc(Book book) throws SQLException {
        bookDao.updateBookDesc(book);
    }
    //删除图书
    public void deleteBook(String bid) throws SQLException {
        bookDao.deleteBook(bid);
    }
}