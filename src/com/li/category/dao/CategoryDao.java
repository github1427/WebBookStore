package com.li.category.dao;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;
import com.li.category.domain.Category;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CategoryDao {
    QueryRunner queryRunner = new TxQueryRunner();

    //添加分类（一级分类，二级分类都适用）
    public void addCategory(Category category) throws SQLException {
        String sql = "insert into t_category (cid,cname,pid,`desc`) values (?,?,?,?)";
        Category parent = category.getParent();
        if (parent == null) {
            queryRunner.update(sql, category.getCid(), category.getCname(), parent, category.getDesc());
        } else {
            queryRunner.update(sql, category.getCid(), category.getCname(), parent.getCid(), category.getDesc());
        }
    }

    //查询所有的分类
    public List<Category> findAll() throws SQLException {
        String sql = "select * from t_category where pid is null order by orderBy";
        List<Map<String, Object>> maps = queryRunner.query(sql, new MapListHandler());
        List<Category> categoryList = toCategoryList(maps);
        for (Category category : categoryList) {
            List<Category> categoryList1 = findByPid(category.getCid());
            category.setChildren(categoryList1);
        }
        return categoryList;
    }

    //查找二级分类
    public List<Category> findByPid(String pid) throws SQLException {
        String sql = "select * from t_category where pid = ? order by orderBy";
        List<Map<String, Object>> maps = queryRunner.query(sql, new MapListHandler(), pid);
        return toCategoryList(maps);
    }

    //将map类型转换成category类型的私有方法
    private Category toCategory(Map<String, Object> map) {
        Category category = CommonUtils.toBean(map, Category.class);
        String pid = (String) map.get("pid");
        if (pid != null) {
            Category parent = new Category();
            parent.setCid(pid);
            category.setParent(parent);
        }
        return category;
    }

    //将maplist转换成categorylist的私有方法
    private List<Category> toCategoryList(List<Map<String, Object>> maps) {
        List<Category> categoryList = new ArrayList<>();
        for (Map<String, Object> map : maps) {
            Category category = toCategory(map);
            categoryList.add(category);
        }
        return categoryList;
    }
    //根据cid查询分类，并返回一个category
    public Category findByCid(String cid) throws SQLException {
        String sql ="select * from t_category where cid =?";
        Map<String,Object> map =queryRunner.query(sql,new MapHandler(),cid);
        return toCategory(map);
    }

    //修改一级分类的名称cname和分类描述desc
    public void updateOneLevel(String cid, String cname, String desc) throws SQLException {
        String sql = "update t_category set cname = ?,`desc` = ? where cid = ?";
        queryRunner.update(sql, cname, desc, cid);
    }

    //修改二级分类信息
    public void updateTwoLevel(String cname, String desc, String pid, String cid) throws SQLException {
        String sql = "update t_category set cname = ?,`desc` = ?,pid=? where cid = ?";
        queryRunner.update(sql,cname,desc,pid,cid);
    }
    //删除分类
    public void deleteLevel(String cid) throws SQLException {
        String sql ="delete from t_category where cid = ?";
        queryRunner.update(sql,cid);
    }
}
