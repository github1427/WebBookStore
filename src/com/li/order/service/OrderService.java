package com.li.order.service;

import cn.itcast.jdbc.JdbcUtils;
import com.li.order.dao.OrderDao;
import com.li.order.domain.Order;
import com.li.page.Page;

import java.sql.SQLException;

public class OrderService {
    private OrderDao orderDao = new OrderDao();

    //查询我的订单
    public Page loadOrder(String uid, int pageCode) throws SQLException {
        return orderDao.findByUid(uid, pageCode);
    }
    //生成订单
    public void createOrder(Order order){
        try {
            JdbcUtils.beginTransaction();
            orderDao.createOrder(order);
            JdbcUtils.commitTransaction();
        } catch (SQLException e) {
            try {
                JdbcUtils.rollbackTransaction();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
    }
    //查看订单
    public Order loadOrderByOid(String oid){
        Order order = null;
        try {
            JdbcUtils.beginTransaction();
            order = orderDao.loadOrderByOid(oid);
            JdbcUtils.commitTransaction();
        } catch (SQLException e) {
            try {
                JdbcUtils.rollbackTransaction();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        return order;
    }
    //查询订单状态
    public int queryStatus(String oid) throws SQLException {
        return orderDao.queryStatus(oid);
    }
    //修改订单状态
    public void updateStatus(String oid,int status) throws SQLException {
        orderDao.updateStatus(oid,status);
    }
    //查询所有订单
    public Page findAllOrder(int pageCode) throws SQLException {
        return orderDao.findAllOrder(pageCode);
    }
    //根据订单状态查询订单
    public Page findOrderByStatus(int status,int pageCode) throws SQLException {
        return orderDao.findOrderByStatus(status,pageCode);
    }
}
