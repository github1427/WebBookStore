package com.li.cart.service;

import com.li.cart.dao.CartItemDao;
import com.li.cart.domain.CartItem;

import java.sql.SQLException;
import java.util.List;

public class CartItemService {
    private CartItemDao cartItemDao=new CartItemDao();
    //查看当前用户购物车中的购物条目
    public List<CartItem> findCartItem(String uid) throws SQLException {
        return cartItemDao.findByUid(uid);
    }
    //添加购物车商品条目
    public void addCartItem(CartItem cartItem) throws SQLException {
        CartItem _cartItem = cartItemDao.findByUidAndBid(cartItem.getUser().getUid(),cartItem.getBook().getBid());
        if (_cartItem==null){

            cartItemDao.addCartItem(cartItem);
        }else {
            int quantity = cartItem.getQuantity()+_cartItem.getQuantity();
            cartItemDao.updateQuantity(_cartItem.getCartItemId(),quantity);
        }
    }
    //批量删除
    public void batchDelete(String cartItemIds) throws SQLException {
        cartItemDao.batchDelete(cartItemIds);
    }
    //在购物车中修改商品条目数量
    public CartItem updateQuantity(String cartItemId,int quantity) throws SQLException {
        cartItemDao.updateQuantity(cartItemId,quantity);
        return cartItemDao.findByCartItemId(cartItemId);
    }
    //根据cartItemIds查询商品条目信息
    public List<CartItem> findByCartItemIds(String cartItemIds) throws SQLException {
        return cartItemDao.findByCartItemIds(cartItemIds);
    }
}
