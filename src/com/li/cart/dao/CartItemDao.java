package com.li.cart.dao;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;
import com.li.book.domain.Book;
import com.li.cart.domain.CartItem;
import com.li.user.domain.User;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CartItemDao {
    private QueryRunner queryRunner=new TxQueryRunner();
    //根据用户的UID查找购物车条目
    public List<CartItem> findByUid(String uid) throws SQLException {
        String sql = "select * from t_cartitem c,t_book b where c.bid = b.bid and c.uid=? order by c.orderBy";
        List<Map<String,Object>> mapList = queryRunner.query(sql,new MapListHandler(),uid);
        return toCartItemList(mapList);

    }
    //将map转换成CartIterm的私有方法
    private CartItem toCartItem(Map<String,Object> map){
        if (map==null||map.size()==0){
            return null;
        }
        CartItem cartItem = CommonUtils.toBean(map,CartItem.class);
        Book book=CommonUtils.toBean(map,Book.class);
        User user=CommonUtils.toBean(map,User.class);
        cartItem.setBook(book);
        cartItem.setUser(user);
        return cartItem;
    }
    //将maplist转成cartitermlist的私有方法
    private List<CartItem> toCartItemList(List<Map<String,Object>> maps){
        List<CartItem> cartItemList = new ArrayList<>();
        for (Map<String,Object> map : maps){
            CartItem cartItem =toCartItem(map);
            cartItemList.add(cartItem);
        }
        return cartItemList;
    }
    //根据用户UID和商品ID查询购物车中是否存在该商品
    public CartItem findByUidAndBid(String uid,String bid) throws SQLException {
        String sql="select * from t_cartitem where uid = ? and bid = ? ";
        Map<String,Object> map = queryRunner.query(sql,new MapHandler(),uid,bid);
        return toCartItem(map);
    }
    //修改购物车条目的quantity
    public void  updateQuantity( String cartItemId,int quantity) throws SQLException {
        String sql="update t_cartitem set quantity = ? where cartItemId = ?";
        queryRunner.update(sql,quantity,cartItemId);
    }
    //添加购物车条目
    public void addCartItem(CartItem cartItem) throws SQLException {
        String sql="insert into t_cartitem (cartItemId,quantity,bid,uid) values(?,?,?,?)";
        Object [] params ={cartItem.getCartItemId(),cartItem.getQuantity(),cartItem.getBook().getBid(),cartItem.getUser().getUid()};
        queryRunner.update(sql,params);
    }
    //批量删除
    public void batchDelete(String cartItemIds) throws SQLException {
        String whereSql = "delete from t_cartitem where ";
        Object [] objects =cartItemIds.split(",");
        //拼凑出where后面的约束语句
        int length=objects.length;
        StringBuilder stringBuilder =new StringBuilder("cartItemId in (");
        for (int i = 1;i<=length;i++){
            stringBuilder.append("?");
            if (i<length){
                stringBuilder.append(",");
            }
        }
        stringBuilder.append(")");
        String sql =whereSql+stringBuilder.toString();
        queryRunner.update(sql,objects);


    }
    //根据图书条目cartItemId查找图书条目信息
    public CartItem findByCartItemId(String cartItemId) throws SQLException {
        String sql="select * from t_cartitem c,t_book b where c.bid = b.bid and cartItemId = ?";
        Map<String,Object> map =queryRunner.query(sql,new MapHandler(),cartItemId);
        return toCartItem(map);
    }
    //查询根据cartItemIds查询多个商品条目信息
    public List<CartItem> findByCartItemIds(String cartItemIds) throws SQLException {
        String whereSql="select * from t_cartitem c,t_book b where c.bid = b.bid and ";
        Object [] objects =cartItemIds.split(",");
        //拼凑出where后面的约束语句
        int length=objects.length;
        StringBuilder stringBuilder =new StringBuilder("cartItemId in (");
        for (int i = 1;i<=length;i++){
            stringBuilder.append("?");
            if (i<length){
                stringBuilder.append(",");
            }
        }
        stringBuilder.append(")");
        String sql =whereSql+stringBuilder.toString();
        List<Map<String,Object>> cartItems=queryRunner.query(sql,new MapListHandler(),objects);
        return toCartItemList(cartItems);
    }
}
