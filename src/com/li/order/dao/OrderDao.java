package com.li.order.dao;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;
import com.li.book.domain.Book;
import com.li.order.domain.Order;
import com.li.order.domain.OrderItem;
import com.li.page.Page;
import com.li.page.PageConstants;
import com.li.user.domain.User;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderDao {
    private QueryRunner queryRunner=new TxQueryRunner();
    //查看订单信息返回一个order对象
    public Order loadOrderByOid(String oid) throws SQLException {
        String sql ="select * from t_order where oid = ?";
        Map<String,Object> map = queryRunner.query(sql,new MapHandler(),oid);
        Order order =CommonUtils.toBean(map,Order.class);
        addOrderItem(order);
        return order;
    }
    //将订单及订单条目添加到数据库中
    public void createOrder(Order order) throws SQLException {
        String sql ="insert into t_order values (?,?,?,?,?,?)";
        Object [] object =new Object[]{order.getOid(),order.getOrdertime(),order.getTotal(),order.getStatus(),order.getAddress(),order.getUser().getUid()};
        queryRunner.update(sql,object);
        List<OrderItem> orderItems =order.getOrderItems();
        sql ="insert into t_orderitem values (?,?,?,?,?,?,?,?)";
        int len =orderItems.size();
        Object [][] objects =new Object[len][];
        for (int i =0;i<len;i++){
            OrderItem orderItem =orderItems.get(i);
            objects[i]=new Object[]{orderItem.getOrderItemId(),orderItem.getQuantity(),orderItem.getSubtotal(),orderItem.getBook().getBid(),
                                    orderItem.getBook().getBname(),orderItem.getBook().getCurrPrice(),orderItem.getBook().getImage_b(),orderItem.getOrder().getOid()};
        }
        queryRunner.batch(sql,objects);
    }
    //根据用户UID查询订单信息
    public Page findByUid(String uid, int pageCode) throws SQLException {
        Page pageBean=new Page();
        //得到每页记录数
        int perPageRecord= PageConstants.orderPageRecord;
        //得到总记录数
        String sql="select count(*) from t_order where uid = ?";
        Number totalRecord = (Number) queryRunner.query(sql,new ScalarHandler(),uid);
        //得到每页所记录的数据
        String sql1="select * from t_order where uid = ? order by ordertime desc limit ?,?";
        List<Order> orderList=queryRunner.query(sql1,new BeanListHandler<Order>(Order.class),uid,(pageCode-1)*perPageRecord,perPageRecord);
        for (Order order:orderList){
            User user =new User();
            user.setUid(uid);
            order.setUser(user);
            addOrderItem(order);
        }
        pageBean.setPerPageRecord(perPageRecord);
        pageBean.setTotalRecord(totalRecord.intValue());
        pageBean.setPageRecord(orderList);
        return pageBean;

    }
    //将订单条目封装到order对象中
    private void addOrderItem(Order order) throws SQLException {
        String sql ="select * from t_orderItem where oid = ?";
        List<Map<String,Object>> maps =queryRunner.query(sql,new MapListHandler(),order.getOid());
        List<OrderItem> orderItems = toOrderItemList(maps);
        order.setOrderItems(orderItems);
    }
    //将map转换成orderItem对象
    private OrderItem toOrderItem(Map<String,Object> map){
        OrderItem orderItem = CommonUtils.toBean(map,OrderItem.class);
        Book book =CommonUtils.toBean(map,Book.class);
        Order order =CommonUtils.toBean(map,Order.class);
        orderItem.setBook(book);
        orderItem.setOrder(order);
        return orderItem;
    }
    //将maps转换为orderItemList
    private List<OrderItem> toOrderItemList(List<Map<String,Object>> maps){
        List<OrderItem> orderItems =new ArrayList<>();
        for (Map<String,Object> map:maps){
            orderItems.add(toOrderItem(map));
        }
        return orderItems;
    }
    //查询订单状态
    public int queryStatus(String oid) throws SQLException {
        String sql ="select status from t_order where oid = ?";
        Number number = (Number) queryRunner.query(sql,new ScalarHandler(),oid);
        return number.intValue();
    }
    //修改订单状态
    public void updateStatus(String oid,int status) throws SQLException {
        String sql = "update t_order set status = ? where oid = ?";
        queryRunner.update(sql,status,oid);
    }
    //查询所有订单信息
    public Page findAllOrder(int pageCode) throws SQLException {
        Page pageBean=new Page();
        //得到每页记录数
        int perPageRecord= PageConstants.orderPageRecord;
        //得到总记录数
        String sql="select count(*) from t_order";
        Number totalRecord = (Number) queryRunner.query(sql,new ScalarHandler());
        //得到每页所记录的数据
        String sql1="select * from t_order order by ordertime desc limit ?,?";
        List<Order> orderList=queryRunner.query(sql1,new BeanListHandler<Order>(Order.class),(pageCode-1)*perPageRecord,perPageRecord);
        for (Order order:orderList){
            addOrderItem(order);
        }
        pageBean.setPerPageRecord(perPageRecord);
        pageBean.setTotalRecord(totalRecord.intValue());
        pageBean.setPageRecord(orderList);
        return pageBean;
    }
    //根据订单状态查询状态
    public Page findOrderByStatus(int status, int pageCode) throws SQLException {
        Page pageBean=new Page();
        //得到每页记录数
        int perPageRecord= PageConstants.orderPageRecord;
        //得到总记录数
        String sql="select count(*) from t_order where status = ?";
        Number totalRecord = (Number) queryRunner.query(sql,new ScalarHandler(),status);
        //得到每页所记录的数据
        String sql1="select * from t_order where status = ? order by ordertime desc limit ?,?";
        List<Order> orderList=queryRunner.query(sql1,new BeanListHandler<Order>(Order.class),status,(pageCode-1)*perPageRecord,perPageRecord);
        for (Order order:orderList){
            addOrderItem(order);
        }
        pageBean.setPerPageRecord(perPageRecord);
        pageBean.setTotalRecord(totalRecord.intValue());
        pageBean.setPageRecord(orderList);
        return pageBean;

    }

}
