package com.li.order.web.servlet;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;
import com.li.cart.domain.CartItem;
import com.li.cart.service.CartItemService;
import com.li.order.domain.Order;
import com.li.order.domain.OrderItem;
import com.li.order.service.OrderService;
import com.li.page.Page;
import com.li.user.domain.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

@WebServlet(name = "OrderServlet",urlPatterns = "/OrderServlet")
public class OrderServlet extends BaseServlet {
    private OrderService orderService =new OrderService();
    private CartItemService cartItemService=new CartItemService();
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
    //查找当前用户订单
    public String loadOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int pageCode =getPageCode(request);
        String url=getURL(request);
        User user = (User) request.getSession().getAttribute("loginUser");
        String uid=user.getUid();
        try {
            Page page=orderService.loadOrder(uid,pageCode);
            page.setPageCode(pageCode);
            page.setUrl(url);
            request.setAttribute("pageBean",page);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "f:/jsps/order/list.jsp";
    }
    //创建订单
    public String createOrder(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
        Order order =new Order();
        order.setOid(CommonUtils.uuid());
        order.setOrdertime(String.format("%tF %<tT",new Date()));
        String cartItemIds =request.getParameter("cartItemIds");
        try {
            List<CartItem> cartItems =cartItemService.findByCartItemIds(cartItemIds);
            List<OrderItem> orderItems=new ArrayList<>();
            BigDecimal total=new BigDecimal("0");
            for (CartItem cartItem:cartItems){
                OrderItem orderItem =new OrderItem();
                orderItem.setOrderItemId(CommonUtils.uuid());
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setSubtotal(cartItem.getSubTotal().doubleValue());
                total=total.add(cartItem.getSubTotal());
                orderItem.setBook(cartItem.getBook());
                orderItem.setOrder(order);
                orderItems.add(orderItem);
            }
            order.setTotal(total.doubleValue());
            order.setOrderItems(orderItems);
            order.setAddress(request.getParameter("address"));
            order.setStatus(1);
            order.setUser((User) request.getSession().getAttribute("loginUser"));
            orderService.createOrder(order);
            cartItemService.batchDelete(cartItemIds);
            request.setAttribute("order",order);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "f:jsps/order/ordersucc.jsp";
    }
    //查看订单信息
    public String queryOrder(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
        String oid = request.getParameter("oid");
        String btn = request.getParameter("btn");
        Order order =orderService.loadOrderByOid(oid);
        request.setAttribute("order",order);
        request.setAttribute("btn",btn);
        return "f:/jsps/order/desc.jsp";
    }
    //取消订单
    public String cancelOrder(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
        String oid =request.getParameter("oid");
        try {
            int status = orderService.queryStatus(oid);
            if (status!=1){
                request.setAttribute("code","error");
                request.setAttribute("msg","该状态无法取消");
                return "f:/jsps/msg.jsp";
            }else {
                orderService.updateStatus(oid,5);
                request.setAttribute("code","success");
                request.setAttribute("msg","订单取消成功");
                return "f:/jsps/msg.jsp";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    //确认收货
    public String confirmOrder(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
        String oid =request.getParameter("oid");
        try {
            int status = orderService.queryStatus(oid);
            if (status!=2){
                request.setAttribute("code","error");
                request.setAttribute("msg","该状态无法确认收货");
                return "f:/jsps/msg.jsp";
            }else {
                orderService.updateStatus(oid,4);
                request.setAttribute("code","success");
                request.setAttribute("msg","确认收货成功成功");
                return "f:/jsps/msg.jsp";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    //支付前的准备
    public String paymentPre(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
        String oid = request.getParameter("oid");
        Order order =orderService.loadOrderByOid(oid);
        request.setAttribute("order",order);
        return "f:/jsps/order/pay.jsp";
    }
    //支付订单
    public String payment(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
        //准备13个参数
        String p0_Cmd = "Buy";//业务类型，固定值Buy
        String p1_MerId = ResourceBundle.getBundle("payment").getString("p1_MerId");//商号编码，在易宝的唯一标识
        String p2_Order = request.getParameter("oid");//订单编码
        String p3_Amt = "0.01";//支付金额
        String p4_Cur = "CNY";//交易币种，固定值CNY
        String p5_Pid = "";//商品名称
        String p6_Pcat = "";//商品种类
        String p7_Pdesc = "";//商品描述
        String p8_Url = ResourceBundle.getBundle("payment").getString("p8_Url");//在支付成功后，易宝会访问这个地址。
        String p9_SAF = "";//送货地址
        String pa_MP = "";//扩展信息
        String pd_FrpId = request.getParameter("yh");//支付通道
        String pr_NeedResponse = "1";//应答机制，固定值1
        /*
		 * 2. 计算hmac
		 * 需要13个参数
		 * 需要keyValue
		 * 需要加密算法
		 */
        String keyValue =ResourceBundle.getBundle("payment").getString("keyValue");
        String hmac = PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt,
                p4_Cur, p5_Pid, p6_Pcat, p7_Pdesc, p8_Url, p9_SAF, pa_MP,
                pd_FrpId, pr_NeedResponse, keyValue);

        /*
		 * 3. 重定向到易宝的支付网关
		 */
        StringBuilder sb = new StringBuilder("https://www.yeepay.com/app-merchant-proxy/node");
        sb.append("?").append("p0_Cmd=").append(p0_Cmd);
        sb.append("&").append("p1_MerId=").append(p1_MerId);
        sb.append("&").append("p2_Order=").append(p2_Order);
        sb.append("&").append("p3_Amt=").append(p3_Amt);
        sb.append("&").append("p4_Cur=").append(p4_Cur);
        sb.append("&").append("p5_Pid=").append(p5_Pid);
        sb.append("&").append("p6_Pcat=").append(p6_Pcat);
        sb.append("&").append("p7_Pdesc=").append(p7_Pdesc);
        sb.append("&").append("p8_Url=").append(p8_Url);
        sb.append("&").append("p9_SAF=").append(p9_SAF);
        sb.append("&").append("pa_MP=").append(pa_MP);
        sb.append("&").append("pd_FrpId=").append(pd_FrpId);
        sb.append("&").append("pr_NeedResponse=").append(pr_NeedResponse);
        sb.append("&").append("hmac=").append(hmac);

        response.sendRedirect(sb.toString());
        return null;
    }
    /**
     * 回馈方法
     * 当支付成功时，易宝会访问这里
     * 用两种方法访问：
     * 1. 引导用户的浏览器重定向(如果用户关闭了浏览器，就不能访问这里了)
     * 2. 易宝的服务器会使用点对点通讯的方法访问这个方法。（必须回馈success，不然易宝服务器会一直调用这个方法）
     * @param req
     * @param resp
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String back(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
		/*
		 * 1. 获取12个参数
		 */
        String p1_MerId = req.getParameter("p1_MerId");
        String r0_Cmd = req.getParameter("r0_Cmd");
        String r1_Code = req.getParameter("r1_Code");
        String r2_TrxId = req.getParameter("r2_TrxId");
        String r3_Amt = req.getParameter("r3_Amt");
        String r4_Cur = req.getParameter("r4_Cur");
        String r5_Pid = req.getParameter("r5_Pid");
        String r6_Order = req.getParameter("r6_Order");
        String r7_Uid = req.getParameter("r7_Uid");
        String r8_MP = req.getParameter("r8_MP");
        String r9_BType = req.getParameter("r9_BType");
        String hmac = req.getParameter("hmac");
		/*
		 * 2. 获取keyValue
		 */
        String keyValue = ResourceBundle.getBundle("payment").getString("keyValue");
		/*
		 * 3. 调用PaymentUtil的校验方法来校验调用者的身份
		 *   >如果校验失败：保存错误信息，转发到msg.jsp
		 *   >如果校验通过：
		 *     * 判断访问的方法是重定向还是点对点，如果要是重定向
		 *     修改订单状态，保存成功信息，转发到msg.jsp
		 *     * 如果是点对点：修改订单状态，返回success
		 */
        boolean bool = PaymentUtil.verifyCallback(hmac, p1_MerId, r0_Cmd, r1_Code, r2_TrxId,
                r3_Amt, r4_Cur, r5_Pid, r6_Order, r7_Uid, r8_MP, r9_BType,
                keyValue);
        if(!bool) {
            req.setAttribute("code", "error");
            req.setAttribute("msg", "无效的签名，支付失败！（你不是好人）");
            return "f:/jsps/msg.jsp";
        }
        if(r1_Code.equals("1")) {
            try {
                orderService.updateStatus(r6_Order, 2);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if(r9_BType.equals("1")) {
                req.setAttribute("code", "success");
                req.setAttribute("msg", "恭喜，支付成功！");
                return "f:/jsps/msg.jsp";
            } else if(r9_BType.equals("2")) {
                resp.getWriter().print("success");
            }
        }
        return null;
    }
}
