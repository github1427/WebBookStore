package com.li.category.service;

import com.li.category.dao.CategoryDao;
import com.li.category.domain.Category;

import java.sql.SQLException;
import java.util.List;

public class CategoryService {
    private CategoryDao categoryDao=new CategoryDao();
    //查看所有分类
    public List<Category> findAll() throws SQLException {
        return categoryDao.findAll();
    }
    //根据cid查询分类，
    public Category findByCid(String cid) throws SQLException {
        return categoryDao.findByCid(cid);
    }
    //添加分类
    public void addCategory(Category category) throws SQLException {
        categoryDao.addCategory(category);
    }
    //修改一级分类
    public void updateOneLevel(Category category) throws SQLException {
        categoryDao.updateOneLevel(category.getCid(),category.getCname(),category.getDesc());
    }
    //修改二级分类
    public void updateTwoLevel(Category category) throws SQLException{
        categoryDao.updateTwoLevel(category.getCname(),category.getDesc(),category.getParent().getCid(),category.getCid());
    }
    //查找一级分类下所属的二级分类
    public List<Category> findByPid(String cid) throws SQLException {
        return categoryDao.findByPid(cid);
    }
    //删除分类
    public void deleteLevel(String cid) throws SQLException {
        categoryDao.deleteLevel(cid);
    }
}
