package com.li.cart.web.servlet;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;
import com.li.book.domain.Book;
import com.li.cart.domain.CartItem;
import com.li.cart.service.CartItemService;
import com.li.user.domain.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "CartItemServlet", urlPatterns = "/CartItemServlet")
public class CartItemServlet extends BaseServlet {
    private CartItemService cartItemService = new CartItemService();

    //查看我的购物车
    public String findMyCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("loginUser");
        try {
            List<CartItem> cartItems = cartItemService.findCartItem(user.getUid());
            request.setAttribute("cartItems", cartItems);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "f://jsps/cart/list.jsp";
    }

    //添加商品条目到我的购物车
    public String addToCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User loginUser = (User) request.getSession().getAttribute("loginUser");
        Map map = request.getParameterMap();
        CartItem cartItem = CommonUtils.toBean(map, CartItem.class);
        Book book = CommonUtils.toBean(map, Book.class);
        User user = (User) request.getSession().getAttribute("loginUser");
        cartItem.setCartItemId(CommonUtils.uuid());
        cartItem.setBook(book);
        cartItem.setUser(user);
        try {
            cartItemService.addCartItem(cartItem);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return findMyCart(request, response);

    }

    //批量删除
    public String batchDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cartItemIds = request.getParameter("cartItemIds");
        try {
            cartItemService.batchDelete(cartItemIds);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return findMyCart(request, response);
    }

    //修改商品条目数量
    public String updateQuantity(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cartItemId = request.getParameter("cartItemId");
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        try {
            CartItem cartItem = cartItemService.updateQuantity(cartItemId, quantity);
            StringBuilder stringBuilder = new StringBuilder("{");
            stringBuilder.append("\"quantity\":").append(cartItem.getQuantity());
            stringBuilder.append(",").append("\"subTotal\":").append(cartItem.getSubTotal());
            stringBuilder.append("}");
            System.out.println(stringBuilder);
            response.getWriter().print(stringBuilder);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //结算时候根据购物车中选中商品组成的cartItemIds查询商品条目
    public String loadCartItems(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cartItemIds = request.getParameter("cartItemIds");
        try {
            List<CartItem> cartItems = cartItemService.findByCartItemIds(cartItemIds);
            String total = request.getParameter("hiddenTotal");
            request.setAttribute("cartItems", cartItems);
            request.setAttribute("total", total);
            request.setAttribute("cartItemIds", cartItemIds);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "f:/jsps/cart/showitem.jsp";
    }
}
