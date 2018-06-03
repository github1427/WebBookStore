<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>cartlist.jsp</title>

    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <!--
    <link rel="stylesheet" type="text/css" href="styles.css">
    -->
    <script src="<c:url value='/jquery/jquery-1.5.1.js'/>"></script>
    <script src="<c:url value='/js/round.js'/>"></script>

    <link rel="stylesheet" type="text/css" href="<c:url value='/jsps/css/cart/list.css'/>">
    <script type="text/javascript">
        $(function () {
            showSubtotal();
            $("#selectAll").click(function () {
                var bool=$("#selectAll").attr("checked");
                selectAll(bool);
                showSubtotal();
            });
            $(":checkbox[name=checkboxBtn]").click(function () {
                jiesuanChange();
            });
            $(".jian").click(function () {
                var cartItemId = $(this).attr("id").substr(0,32);
                var quantity=$("#"+cartItemId+"Quantity").val();
                if(Number(quantity)===1){
                    if(confirm("是否删除该商品")){
                        location.href="/goods/CartItemServlet?method=batchDelete&cartItemIds="+cartItemId;
                    }
                }else {
                    sendUpdateQuantity(cartItemId,Number(quantity)-1);
                }
            });
            $(".jia").click(function () {
                var cartItemId = $(this).attr("id").substr(0,32);
                var quantity=$("#"+cartItemId+"Quantity").val();
                sendUpdateQuantity(cartItemId,Number(quantity)+1);
            });


        });
        function sendUpdateQuantity(cartItemId,quantity) {
            $.ajax({
                url:"/goods/CartItemServlet",
                data:{method:"updateQuantity",cartItemId:cartItemId,quantity:quantity},
                type:"GET",
                dataType:"json",
                async:false,
                cache:false,
                success:function(result){
                  $("#"+cartItemId+"Quantity").val(result.quantity);
                  $("#"+cartItemId+"SubTotal").text(result.subTotal);
                  showSubtotal();
                }
            });
        }

        function showSubtotal() {
            var subTotal = 0;
            $(":checkbox[name=checkboxBtn][checked=true]").each(function () {
                var cartItemId = $(this).val();
                var num = $("#" + cartItemId + "SubTotal").text();
                subTotal = subTotal+ Number(num);
            });
            $("#total").text(round(subTotal,2));
        }

        function selectAll(bool) {
            $(":checkbox[name=checkboxBtn]").attr("checked", bool);
        }

        function jiesuanChange() {
            var length =  $(":checkbox[name=checkboxBtn]").length;
            var selectLength =  $(":checkbox[name=checkboxBtn][checked=true]").length;
            if(selectLength===length){
                $("#selectAll").attr("checked",true);
            }else if (selectLength===0){
                $("#selectAll").attr("checked",false);
                $("#jiesuan").removeClass("jiesuan").addClass("kill");
                $("#jiesuan").click(function () {
                    return false;
                });
            }else {
                $("#selectAll").attr("checked",false);
                $("#jiesuan").removeClass("kill").addClass("jiesuan");
                $("#jiesuan").unbind("click");
            }
            showSubtotal();
        }
        function batchDelete() {
            var cartItemIds = new Array();
            var cartItemId;
            $(":checkbox[name=checkboxBtn][checked=true]").each(function () {
                cartItemId=$(this).val();
                cartItemIds.push(cartItemId);
            });
            location.href="/goods/CartItemServlet?method=batchDelete&cartItemIds="+cartItemIds;
        }
        function jiesuan() {
            var cartItemIds = new Array();
            var cartItemId;
            $(":checkbox[name=checkboxBtn][checked=true]").each(function () {
                cartItemId=$(this).val();
                cartItemIds.push(cartItemId);
            });
            $("#cartItemIds").val(cartItemIds.toString());
            $("#hiddenTotal").val($("#total").text());
            $("#jiesuanForm").submit();
        }

    </script>
</head>
<body>

<c:choose>
    <c:when test="${empty cartItems}">
        <table width="95%" align="center" cellpadding="0" cellspacing="0">
            <tr>
                <td align="right">
                    <img align="top" src="<c:url value='/images/icon_empty.png'/>"/>
                </td>
                <td>
                    <span class="spanEmpty">您的购物车中暂时没有商品</span>
                </td>
            </tr>
        </table>
    </c:when>
    <c:otherwise>
        <table width="95%" align="center" cellpadding="0" cellspacing="0">
            <tr align="center" bgcolor="#efeae5">
                <td align="left" width="50px">
                    <input type="checkbox" id="selectAll" checked="checked" /><label for="selectAll">全选</label>
                </td>
                <td colspan="2">商品名称</td>
                <td>单价</td>
                <td>数量</td>
                <td>小计</td>
                <td>操作</td>
            </tr>


            <c:forEach items="${cartItems}" var="cartItem">
                <tr align="center">
                    <td align="left">
                        <input value="${cartItem.cartItemId}" type="checkbox" name="checkboxBtn" checked="checked"/>
                    </td>
                    <td align="left" width="70px">
                        <a class="linkImage" href="<c:url value='/jsps/book/desc.jsp'/>"><img border="0" width="54" align="top"
                                                                                              src="<c:url value='/${cartItem.book.image_b}'/>"/></a>
                    </td>
                    <td align="left" width="400px">
                        <a href="<c:url value='/jsps/book/desc.jsp'/>"><span>${cartItem.book.bname}</span></a>
                    </td>
                    <td><span>&yen;<span class="currPrice" id="12345CurrPrice">${cartItem.book.currPrice}</span></span></td>
                    <td>
                        <a class="jian" id="${cartItem.cartItemId}Jian"></a><input class="quantity" readonly="readonly"
                                                                                   id="${cartItem.cartItemId}Quantity"
                                                                                   type="text" value="${cartItem.quantity}"/><a
                            class="jia" id="${cartItem.cartItemId}Jia"></a>
                    </td>
                    <td width="100px">
                <span class="price_n">&yen;<span class="subTotal"
                                                 id="${cartItem.cartItemId}SubTotal">${cartItem.subTotal}</span></span>
                    </td>
                    <td>
                        <a href="<c:url value='/CartItemServlet?method=batchDelete&cartItemIds=${cartItem.cartItemId}'/>">删除</a>
                    </td>
                </tr>
            </c:forEach>


            <tr>
                <td colspan="4" class="tdBatchDelete">
                    <a href="javascript:batchDelete()">批量删除</a>
                </td>
                <td colspan="3" align="right" class="tdTotal">
                    <span>总计：</span><span class="price_t">&yen;<span id="total"></span></span>
                </td>
            </tr>
            <tr>
                <td colspan="7" align="right">
                    <a href="javascript:jiesuan()" id="jiesuan" class="jiesuan"></a>
                </td>
            </tr>
        </table>
        <form id="jiesuanForm" action="/goods/CartItemServlet" method="post">
            <input type="hidden" name="cartItemIds" id="cartItemIds"/>
            <input type="hidden" name="hiddenTotal" id="hiddenTotal"/>
            <input type="hidden" name="method" value="loadCartItems"/>
        </form>
    </c:otherwise>
</c:choose>


</body>
</html>
