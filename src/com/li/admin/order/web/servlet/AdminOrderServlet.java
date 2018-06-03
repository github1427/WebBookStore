package com.li.admin.order.web.servlet;

import cn.itcast.servlet.BaseServlet;
import com.li.order.domain.Order;
import com.li.order.service.OrderService;
import com.li.page.Page;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "AdminOrderServlet",urlPatterns = "/admin/AdminOrderServlet")
public class AdminOrderServlet extends BaseServlet {
    private OrderService orderService =new OrderService();
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
    //后台查询所有订单
    public String findAllOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int pageCode =getPageCode(request);
        String url=getURL(request);
        try {
            Page page=orderService.findAllOrder(pageCode);
            page.setPageCode(pageCode);
            page.setUrl(url);
            request.setAttribute("pageBean",page);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "f:/adminjsps/admin/order/list.jsp";
    }
    //查看订单信息
    public String queryOrder(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
        String oid = request.getParameter("oid");
        String btn = request.getParameter("btn");
        Order order =orderService.loadOrderByOid(oid);
        request.setAttribute("order",order);
        request.setAttribute("btn",btn);
        return "f:/adminjsps/admin/order/desc.jsp";
    }
    //取消订单
    public String cancelOrder(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
        String oid =request.getParameter("oid");
        try {
            int status = orderService.queryStatus(oid);
            if (status!=1){
                request.setAttribute("msg","该状态无法取消");
                return "f:/adminjsps/admin/msg.jsp";
            }else {
                orderService.updateStatus(oid,5);
                request.setAttribute("msg","订单取消成功");
                return "f:/adminjsps/admin/msg.jsp";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    //发货
    public String deliverOrder(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
        String oid =request.getParameter("oid");
        try {
            int status = orderService.queryStatus(oid);
            if (status!=1){
                request.setAttribute("msg","该状态无法发货");
                return "f:/adminjsps/admin/msg.jsp";
            }else {
                orderService.updateStatus(oid,3);
                request.setAttribute("msg","已发货");
                return "f:/adminjsps/admin/msg.jsp";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    //根据订单状态查询订单
    public String findOrderByStatus(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int pageCode =getPageCode(request);
        String url=getURL(request);
        Integer status = Integer.parseInt(request.getParameter("status"));
        try {
            Page page=orderService.findOrderByStatus(status,pageCode);
            page.setPageCode(pageCode);
            page.setUrl(url);
            request.setAttribute("pageBean",page);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "f:/adminjsps/admin/order/list.jsp";
    }
}
