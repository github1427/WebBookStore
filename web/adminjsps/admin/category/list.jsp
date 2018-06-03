<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>分类列表</title>

    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <!--
    <link rel="stylesheet" type="text/css" href="styles.css">
    -->
    <link rel="stylesheet" type="text/css" href="<c:url value='/adminjsps/admin/css/category/list.css'/>">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/css.css'/>">
</head>

<body>
<h2 style="text-align: center;">分类列表</h2>
<table align="center" border="1" cellpadding="0" cellspacing="0">
    <caption class="captionAddOneLevel">
        <a href="<c:url value='/adminjsps/admin/category/add.jsp'/>">添加一级分类</a>
    </caption>
    <tr class="trTitle">
        <th>分类名称</th>
        <th>描述</th>
        <th>操作</th>
    </tr>

    <c:forEach items="${categories}" var="parent">
        <tr class="trOneLevel">
            <td width="200px;">${parent.cname}</td>
            <td>${parent.desc}</td>
            <td width="200px;">
                <a href="<c:url value='/admin/AdminCategoryServlet?method=queryOneLevel'/>">添加二级分类</a>
                <a href="<c:url value='/admin/AdminCategoryServlet?method=findByCid1&cid=${parent.cid}'/>">修改</a>
                <a onclick="return confirm('您是否真要删除该一级分类？')" href="<c:url value='/admin/AdminCategoryServlet?method=deleteOneLevel&cid=${parent.cid}'/>">删除</a>
            </td>
        </tr>
        <c:forEach items="${parent.children}" var="children">
            <tr class="trTwoLevel">
                <td>${children.cname}</td>
                <td>${children.desc}</td>
                <td width="200px;" align="right">
                    <a href="<c:url value='/admin/AdminCategoryServlet?method=findByCid2&cid=${children.cid}'/>">修改</a>
                    <a onclick="return confirm('您是否真要删除该二级分类？')" href="<c:url value='/admin/AdminCategoryServlet?method=deleteTwoLevel&cid=${children.cid}'/>">删除</a>
                </td>
            </tr>
        </c:forEach>

    </c:forEach>


</table>
</body>
</html>
