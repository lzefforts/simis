<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>北京人民广播电台语言文字测试中心缴费系统</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- 引入 Bootstrap -->
    <link href="/resource/assets/css/bootstrap.min.css" rel="stylesheet">
    <link href="/resource/assets/css/bootstrap-datetimepicker.css" rel="stylesheet">
    <script src="/resource/assets/js/html5shiv.js"></script>
    <script src="/resource/assets/js/respond.min.js"></script>
    <script src="/resource/assets/js/jquery-1.11.1.js"></script>
    <script src="/resource/assets/js/bootstrap.min.js"></script>
    <script src="/resource/assets/js/date-time/moment.min.js"></script>
    <script src="/resource/assets/js/date-time/bootstrap-datetimepicker.min.js"></script>
    <script src="/resource/assets/js/date-time/bootstrap-datepicker.min.js"></script>
    <script src="/resource/assets/js/date-time/locales/bootstrap-datepicker.zh-CN.js"></script>

 <style type="text/css">

 </style>

 <script type="text/javascript">
     $(function(){
         $('.form_datetime').datetimepicker({
             language:  'zh-CN',
             weekStart: 1,
             todayBtn:  1,
             autoclose: 1,
             todayHighlight: 1,
             startView: 2,
             forceParse: 0,
             showMeridian: 1,
             format: 'yyyy-mm-dd hh:ii'
         }).on('changeDate', function (ev) {
             $(this).datetimepicker('hide');
         });
     });

     //注册
     function register(){
         var params = $("form").serialize();
         $.ajax({
            url : "/customer/register.htm",
            type:'POST', //GET
            async:true,    //或false,是否异步
            data:params,
            timeout:5000,    //超时时间
            dataType:'text',
            success:function(data,textStatus,jqXHR){
                var result = eval("("+data+")");
                if(result.success == true){
                    $("#success").show();
                }else{
                    $("#error").show();
                }
            },
            error:function(xhr,textStatus){
                $("#exception").show();
            }
         });

     }


     //重置
     function reset(){
        //$("#registerform input");

     }

 </script>

</head>
<body>

<!-- form表单 -->
<div>



</div>
<form id="registerform" class="form-horizontal" role="form" style="text-align: center">
    <div class="form-group">
        <label class="col-sm-2 control-label">姓名</label>
        <div class="col-sm-5">
            <input class="form-control" name="name" type="text">
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-2 control-label">性别</label>
        <div class="col-sm-5">
            <input class="form-control" name="sex" type="text">
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-2 control-label">手机号</label>
        <div class="col-sm-5">
            <input class="form-control" name="phone" type="text">
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-2 control-label">邮箱</label>
        <div class="col-sm-5">
            <input class="form-control" name="email" type="text">
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-2 control-label">身份证号</label>
        <div class="col-sm-5">
            <input class="form-control" name="cardNo" type="text">
        </div>
    </div>

    <div class="form-group">
        <label class="col-sm-2 control-label">语言委员会注册时间</label>
        <div class="col-sm-5">
            <%--<input class="form-control" name="clGovRegisterDate" type="text">--%>
            <input  name="clGovRegisterDate" type="text" value="2012-06-15 14:45" readonly class="form_datetime">
        </div>
    </div>

    <div class="form-group">
        <label class="col-sm-2 control-label">用户名</label>
        <div class="col-sm-5">
            <input class="form-control" name="userName" type="password">
        </div>
    </div>

    <div class="form-group">
        <label class="col-sm-2 control-label">登录密码</label>
        <div class="col-sm-5">
            <input class="form-control" name="passWord" type="password">
        </div>
    </div>

    <div class="form-group" style="text-align: left">
        <button type="button" class="btn btn-primary" onclick="pay()">交费</button>
        <button type="button" class="btn btn-primary" onclick="reset()">修改密码</button>
    </div>

</form>

<!--提示信息-->
<div id="success" class="alert alert-success" style="display: none">
    <a href="#" class="close" data-dismiss="alert">&times;</a>
    <strong>注册成功！</strong>
</div>

<div id="error" class="alert alert-danger alert-dismissable" style="display: none">
    <button type="button" class="close" data-dismiss="alert"
            aria-hidden="true">
        &times;
    </button>
    注册失败!
</div>

<div id="exception" class="alert alert-danger alert-dismissable" style="display: none">
    <button type="button" class="close" data-dismiss="alert"
            aria-hidden="true">
        &times;
    </button>
    服务器异常!
</div>




</body>
</html>