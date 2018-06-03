package com.li.book.dao;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;
import com.li.book.domain.Book;
import com.li.category.domain.Category;
import com.li.page.Page;
import com.li.page.PageConstants;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class BookDao {
    private QueryRunner queryRunner=new TxQueryRunner();
    //按分类查找
    public Page findByCid(String cid,int pageCode) throws SQLException {
        Page pageBean=new Page();
        //得到每页记录数
        int perPageRecord= PageConstants.perPageRecord;
        //得到总记录数
        String sql="select count(*) from t_book where cid = ?";
        Number totalRecord = (Number) queryRunner.query(sql,new ScalarHandler(),cid);
        //得到每页所记录的数据
        String sql1="select * from t_book where cid = ? order by orderBy limit ?,?";
        List<Book> bookList=queryRunner.query(sql1,new BeanListHandler<Book>(Book.class),cid,(pageCode-1)*perPageRecord,perPageRecord);
        pageBean.setPerPageRecord(perPageRecord);
        pageBean.setTotalRecord(totalRecord.intValue());
        pageBean.setPageRecord(bookList);
        return pageBean;
    }
    //按图书名查询
    public Page findByBname(String bname,int pageCode) throws SQLException {
        Page pageBean=new Page();
        //得到每页记录数
        int perPageRecord= PageConstants.perPageRecord;
        //得到总记录数
        String sql="select count(*) from t_book where bname = ?";
        Number totalRecord = (Number) queryRunner.query(sql,new ScalarHandler(),bname);
        //得到每页所记录的数据
        String sql1="select * from t_book where bname = ? order by orderBy limit ?,?";
        List<Book> bookList=queryRunner.query(sql1,new BeanListHandler<Book>(Book.class),bname,(pageCode-1)*perPageRecord,perPageRecord);
        pageBean.setPerPageRecord(perPageRecord);
        pageBean.setTotalRecord(totalRecord.intValue());
        pageBean.setPageRecord(bookList);
        return pageBean;

    }
    //按作者名查询
    public Page findByAuthor(String author,int pageCode) throws SQLException {
        Page pageBean=new Page();
        //得到每页记录数
        int perPageRecord= PageConstants.perPageRecord;
        //得到总记录数
        String sql="select count(*) from t_book where author = ?";
        Number totalRecord = (Number) queryRunner.query(sql,new ScalarHandler(),author);
        //得到每页所记录的数据
        String sql1="select * from t_book where author = ? order by orderBy limit ?,?";
        List<Book> bookList=queryRunner.query(sql1,new BeanListHandler<Book>(Book.class),author,(pageCode-1)*perPageRecord,perPageRecord);
        pageBean.setPerPageRecord(perPageRecord);
        pageBean.setTotalRecord(totalRecord.intValue());
        pageBean.setPageRecord(bookList);
        return pageBean;

    }
    //按出版社名查询
    public Page findByPress(String press,int pageCode) throws SQLException {
        Page pageBean=new Page();
        //得到每页记录数
        int perPageRecord= PageConstants.perPageRecord;
        //得到总记录数
        String sql="select count(*) from t_book where press = ?";
        Number totalRecord = (Number) queryRunner.query(sql,new ScalarHandler(),press);
        //得到每页所记录的数据
        String sql1="select * from t_book where press = ? order by orderBy limit ?,?";
        List<Book> bookList=queryRunner.query(sql1,new BeanListHandler<Book>(Book.class),press,(pageCode-1)*perPageRecord,perPageRecord);
        pageBean.setPerPageRecord(perPageRecord);
        pageBean.setTotalRecord(totalRecord.intValue());
        pageBean.setPageRecord(bookList);
        return pageBean;

    }
    //按图书名模糊查询
    public Page findByBnameDim(String bname,int pageCode) throws SQLException {
        Page pageBean=new Page();
        //得到每页记录数
        int perPageRecord= PageConstants.perPageRecord;
        //得到总记录数
        String sql="select count(*) from t_book where bname like '%' ? '%'";
        Number totalRecord = (Number) queryRunner.query(sql,new ScalarHandler(),bname);
        //得到每页所记录的数据
        String sql1="select * from t_book where bname like '%' ? '%' order by orderBy limit ?,?";
        List<Book> bookList=queryRunner.query(sql1,new BeanListHandler<Book>(Book.class),bname,(pageCode-1)*perPageRecord,perPageRecord);
        pageBean.setPerPageRecord(perPageRecord);
        pageBean.setTotalRecord(totalRecord.intValue());
        pageBean.setPageRecord(bookList);
        return pageBean;

    }
    //高级搜索多条件查询
    public Page findByCriteria(Book book,int pageCode) throws SQLException {
        Page pageBean=new Page();
        //得到每页记录数
        int perPageRecord= PageConstants.perPageRecord;
        //得到总记录数
        String sql="select count(*) from t_book where bname like '%' ? '%'  and author like '%' ? '%'  and press like '%' ? '%'";
        Number totalRecord = (Number) queryRunner.query(sql,new ScalarHandler(),book.getBname(),book.getAuthor(),book.getPress());
        //得到每页所记录的数据
        String sql1="select * from t_book where bname like '%' ? '%' and author like '%' ? '%' and press like '%' ? '%' order by orderBy limit ?,?";
        List<Book> bookList=queryRunner.query(sql1,new BeanListHandler<Book>(Book.class),book.getBname(),book.getAuthor(),book.getPress(),(pageCode-1)*perPageRecord,perPageRecord);
        pageBean.setPerPageRecord(perPageRecord);
        pageBean.setTotalRecord(totalRecord.intValue());
        pageBean.setPageRecord(bookList);
        return pageBean;

    }
    //按图书编号查找
    public Book findByBid(String bid) throws SQLException {
        String sql="select * from t_book where bid = ?";
        Map<String,Object> map =queryRunner.query(sql,new MapHandler(),bid);
        Book book = CommonUtils.toBean(map,Book.class);
        Category category=CommonUtils.toBean(map,Category.class);
        book.setCategory(category);
        return book;
    }
    //根据二级分类cid查找图书
    public List<Book> findBookByCid(String cid) throws SQLException {
        String sql="select * from t_book where cid = ?";
        List<Book> books =queryRunner.query(sql,new BeanListHandler<Book>(Book.class),cid);
        return books;
    }
    //添加图书
    public void addBook(Book book) throws SQLException {
        String sql ="insert into t_book (bid,bname,author,price,currPrice,discount,press,publishtime,edition,pageNum,wordNum,printtime,booksize,paper,cid,image_w,image_b) "+
                "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Object[] params={book.getBid(),book.getBname(),book.getAuthor(),book.getPrice(),book.getCurrPrice(),book.getDiscount(),book.getPress(),book.getPublishtime(),
                            book.getEdition(),book.getPageNum(),book.getWordNum(),book.getPrinttime(),book.getBooksize(),book.getPaper(),book.getCategory().getCid(),
                            book.getImage_w(),book.getImage_b()};
        queryRunner.update(sql,params);
    }
    //修改图书信息
    public void updateBookDesc(Book book) throws SQLException {
        String sql ="update t_book set bname=?,author=?,price=?,currPrice=?,discount=?,press=?,publishtime=?,edition=?,pageNum=?,wordNum=?,printtime=?,booksize=?,paper=?,cid=? where bid=?";
        Object[] params={book.getBname(),book.getAuthor(),book.getPrice(),book.getCurrPrice(),book.getDiscount(),book.getPress(),book.getPublishtime(),book.getEdition(),book.getPageNum()
                ,book.getWordNum(),book.getPrinttime(),book.getBooksize(),book.getPaper(),book.getCategory().getCid(),book.getBid()};
        queryRunner.update(sql,params);
    }
    //删除图书
    public void deleteBook(String bid) throws SQLException {
        String sql ="delete from t_book where bid =?";
        queryRunner.update(sql,bid);
    }
}
