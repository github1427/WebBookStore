<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: vain
  Date: 2017/12/27
  Time: 下午3:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>注册界面</title>
    <link rel="stylesheet" href="/goods/jsps/css/user/regist.css">
    <script src="/goods/jquery/jquery-1.5.1.js"></script>
    <script src="/goods/jsps/js/user/regist.js"></script>
</head>
<body>
<div id="divMain">
    <div id="divTitle">
        <span id="spanTitle">新用户注册</span>
    </div>
    <div id="divBody">
        <form action="/goods/UserServlet" id="registForm">
            <input type="text" name="method" value="regist" hidden>
            <table id="tableID" align="center">
                <tr class="trClass">
                    <td class="td1Class">用户名：</td>
                    <td class="td2Class"><input type="text" name="loginname" id="loginname" class="inputClass"
                                                value="${formUser.loginname}"></td>
                    <td class="td3Class">
                        <lable class="loginError" id="loginnameError">${errors.loginname}</lable>
                    </td>
                </tr>
                <tr class="trClass">
                    <td class="td1Class">密码：</td>
                    <td class="td2Class"><input type="password" name="loginpass" id="loginpass" class="inputClass"
                                                value="${formUser.loginpass}"></td>
                    <td class="td3Class">
                        <lable class="loginError" id="loginpassError">${errors.loginpass}</lable>
                    </td>
                </tr>
                <tr class="trClass">
                    <td class="td1Class">确认密码：</td>
                    <td class="td2Class"><input type="password" name="reloginpass" id="reloginpass" class="inputClass"
                                                value="${formUser.reloginpass}">
                    </td>
                    <td class="td3Class">
                        <lable class="loginError" id="reloginpassError">${errors.reloginpass}</lable>
                    </td>
                </tr>
                <tr class="trClass">
                    <td class="td1Class">Email：</td>
                    <td class="td2Class"><input type="text" name="email" id="email" class="inputClass"
                                                value="${formUser.email}"></td>
                    <td class="td3Class">
                        <lable class="loginError" id="emailError">${errors.email}</lable>
                    </td>
                </tr>
                <tr class="trClass">
                    <td class="td1Class">图形验证码：</td>
                    <td class="td2Class"><input type="text" name="verifyCode" id="verifyCode" class="inputClass"
                                                value="${formUser.verifyCode}"></td>
                    <td class="td3Class">
                        <lable class="loginError" id="verifyCodeError">${errors.verifyCode}</lable>
                    </td>
                </tr>
                <tr class="trClass">
                    <td class="td1Class"></td>
                    <td class="td2Class">
                        <div id="verifyCodeDiv"><img id="verifyCodeImg" src="/goods/verifyCode" width="100px"></div>
                    </td>
                    <td class="td3Class"><a href="javascript:_hyz()">换一张</a>
                </tr>
                <tr class="trClass">
                    <td class="td1Class"></td>
                    <td class="td2Class"><img src="/goods/images/regist2.jpg" id="imgRegist"/></td>
                    <td class="td3Class"></td>
                </tr>
            </table>
        </form>
    </div>
</div>
</body>
</html>
