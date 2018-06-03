$(function () {
    //鼠标移动到立即注册区域时候实现图片切换
    $("#imgRegist").hover(
        function () {
            $("#imgRegist").attr("src", "/goods/images/regist2.jpg");
        },
        function () {
            $("#imgRegist").attr("src", "/goods/images/regist1.jpg");
        }
    );
    $(".loginerror").each(function () {
        checkError($(this));
    });
    //获取光标时隐藏错误信息
    $(".inputClass").focus(function(){
        var lableID=$(this).attr("id")+"Error";
        $("#"+lableID).text("");
        checkError($("#"+lableID));
    });
    $(".inputClass").blur(function(){
        var inputID=$(this).attr("id");
        var funName="validate"+inputID.substring(0,1).toUpperCase()+inputID.substring(1);
        eval(funName+"()");
    });
    $("#imgRegist").click(function () {
        if(validateLoginname()&validateLoginpass()&validateReloginpass()&validateEmail()&validateVerifyCode()){
            $("#registForm").submit();
        }
    });
});
//换一张验证码
function _hyz() {
    $("#verifyCodeImg").attr("src", "/goods/verifyCode?a=" + new Date().getTime());
}
//查看lable标签中是否有内容，有就正常显示，没有就隐藏
function checkError(ele) {
    var text = ele.text();
    if (!text) {
        ele.css('display', "none");
    } else {
        ele.css('display', "");
    }
}
//校验用户名
function validateLoginname() {
    var id="loginname";
    var value=$("#"+id).val();
    if(!value){
        $("#"+id+"Error").text("用户名不能为空");
        checkError($("#"+id+"Error"));
        return false;
    }
    if(value.length<3||value.length>20){
        $("#"+id+"Error").text("用户名长度在3~20之间");
        checkError($("#"+id+"Error"));
        return false;
    }
   $.ajax({
       url:"/goods/UserServlet",
       data:{method:"ajaxValidateLoginname",loginname:value},
       type:"POST",
       dataType:"json",
       async:false,
       cache:false,
       success:function(result){
           if(result===false){
               $("#"+id+"Error").text("用户名已被占用");
               checkError($("#"+id+"Error"));
               return false;
           }
       }
   });
    checkError($("#"+id+"Error"));
    return true;
}
//校验密码
function validateLoginpass() {
    var id = "loginpass";
    var value = $("#" + id).val();
    if (!value) {
        $("#" + id + "Error").text("密码不能为空");
        checkError($("#" + id + "Error"));
        return false;
    }
    if (value.length < 3 || value.length > 20) {
        $("#" + id + "Error").text("密码长度在3~20之间");
        checkError($("#" + id + "Error"));
        return false;
    }
    checkError($("#" + id + "Error"));
    return true;
}
//校验确认密码
function validateReloginpass() {
    var id = "reloginpass";
    var value = $("#" + id).val();
    if (!value) {
        $("#" + id + "Error").text("确认密码不能为空");
        checkError($("#" + id + "Error"));
        return false;
    }
    if (value!=($("#loginpass").val())) {
        $("#" + id + "Error").text("两次输入的密码不一致");
        checkError($("#" + id + "Error"));
        return false;
    }
    checkError($("#" + id + "Error"));
    return true;
}
//校验邮箱
function validateEmail() {
    var id = "email";
    var value = $("#" + id).val();
    if (!value) {
        $("#" + id + "Error").text("邮箱不能为空");
        checkError($("#" + id + "Error"));
        return false;
    }
    if (!/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/.test(value)) {
        $("#" + id + "Error").text("邮箱格式不合法");
        checkError($("#" + id + "Error"));
        return false;
    }
    $.ajax({
        url:"/goods/UserServlet",
        data:{method:"ajaxValidateEmail",email:value},
        type:"POST",
        dataType:"json",
        async:false,
        cache:false,
        success:function(result){
            if(result===false){
                $("#"+id+"Error").text("邮箱已被占用");
                checkError($("#"+id+"Error"));
                return false;
            }
        }
    });
    checkError($("#" + id + "Error"));
    return true;
}
//校验验证码
function validateVerifyCode() {
    var id="verifyCode";
    var value=$("#"+id).val();
    if(!value){
        $("#"+id+"Error").text("验证码不能为空");
        checkError($("#"+id+"Error"));
        return false;
    }
    if(value.length!=4){
        $("#"+id+"Error").text("验证码输入错误");
        checkError($("#"+id+"Error"));
        return false;
    }
    $.ajax({
        url:"/goods/UserServlet",
        data:{method:"ajaxValidateVerifyCode",verifyCode:value},
        type:"GET",
        dataType:"json",
        async:false,
        cache:false,
        success:function(result){
            if(result===false){
                $("#"+id+"Error").text("验证码输入错误");
                checkError($("#"+id+"Error"));
                return false;
            }
        }

    });
    checkError($("#"+id+"Error"));
    return true;
}