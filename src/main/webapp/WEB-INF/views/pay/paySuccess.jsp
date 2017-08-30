<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    // 获得本项目的地址(例如: http://localhost:8080/MyApp/)赋值给basePath变量
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
    <base href="<%=basePath%>">
    <meta charset="UTF-8">
    <title>北京人民广播电台语言文字测试中心缴费系统</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="resource/core/css/mask.css" rel="stylesheet">
    <link href="resource/core/css/login.css" rel="stylesheet">
    <link href="resource/assets/css/jquery.loadmask.css" rel="stylesheet" type="text/css" />
    <script src="resource/assets/js/jquery-1.11.1.js"></script>
    <script src="resource/assets/js/jquery.loadmask.js"></script>
    <style>
        #content { padding:5px;width:200px; }
        .mask {
            position: absolute; top: 0px; filter: alpha(opacity=60); background-color: #777;
            z-index: 1002; left: 0px;
            opacity:0.5; -moz-opacity:0.5;
        }
        .div{position: absolute; top: 300px;left:500px;padding:0 auto;width:100px;height:25px;line-height:25px;fong-size:25px;}
    </style>
    <script type="text/javascript">
        //交费
        function pay(){
            showMask();
            var params = $("#payform").serialize();
            $.ajax({
                url : "onlinepay/pay.htm",
                type:'POST', //GET
                async:true,    //或false,是否异步
                data:params,
                timeout:5000,    //超时时间
                dataType:'text',
                success:function(data,textStatus,jqXHR){
                    var result = eval("("+data+")");
                    if(result.success == true){
                        $("#qcodeImg").attr("src", result.msg);
                        $("#qcode").show();
                        //TODO 置灰交费按钮
                    }else{
                        alert(result.msg)
                    }
                    hideMask();
                },
                error:function(xhr,textStatus){
                    alert("通讯异常,请重试")
                }
            });
        }

        function showMask(){
            $("#mask").css("height",$(document).height());
            $("#mask").css("width",$(document).width());
            $("#maskSpan").show();
            $("#mask").show();
        }
        //隐藏遮罩层
        function hideMask(){
            $("#mask").hide();
            $("#maskSpan").hide();
        }
    </script>

</head>
<body id="content" class="jiaofei_bj">

<%--<div class="zhuce_btn_div"  onclick="location='customer/toregister'"></div>--%>
<%--<div class="jiaofei_btn_div" onclick="location='onlinepay/topay'"></div>--%>
<%--<div class="houtai_btn_div" onclick="location='manage/tomanage'"></div>--%>

<div class="zhuce_btn_div"  onclick="location='<%=basePath%>customer/toregister'"></div>
<div class="jiaofei_btn_div" onclick="location='<%=basePath%>onlinepay/topay'"></div>
<div class="houtai_btn_div" onclick="location='<%=basePath%>manage/tomanage'"></div>

<div id="mask" class="mask"><span id="maskSpan" class="div" style="display:none">正在处理...</span></div>
<div class="zhuce_body">
    <div class="zhuce_kong">
        <div class="zc">
            <div class="bj_bai">
                <h3>学员交费</h3>
                <h3><font color="red"><b>支付成功!</b></font></h3>
            </div>
        </div>
        <P>欢迎登录北京人民广播电台语言文字测试中心缴费系统</P>
    </div>
</div>
</body>
</html>