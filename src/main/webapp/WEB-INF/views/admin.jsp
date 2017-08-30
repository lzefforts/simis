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
    <link href="resource/assets/css/jquery-ui.css" rel="stylesheet" type="text/css" />
    <script src="resource/assets/js/jquery-1.11.1.js"></script>
    <script src="resource/assets/js/jquery.loadmask.js"></script>
    <script src="resource/assets/js/jquery-ui.js"></script>
    <script src="resource/assets/js/json2.js"></script>
    <style>
        #content { padding:5px;width:200px; }
        .mask {
            position: absolute; top: 0px; filter: alpha(opacity=60); background-color: #777;
            z-index: 1002; left: 0px;
            opacity:0.5; -moz-opacity:0.5;
        }
        .div{position: relative; top: 300px;left:500px;padding:0 auto;width:100px;height:25px;line-height:25px;fong-size:25px;}
    </style>
    <script type="text/javascript">
        $(function() {
            $.ajax({
                url : "manage/getEndDay.htm",
                type:'POST', //GET
                async:true,    //或false,是否异步
                data:{},
                timeout:5000,    //超时时间
                dataType:'text',
                success:function(data,textStatus,jqXHR){
                    var result = eval("("+data+")");
                    if(result.success == true){
                        $("#endDay").val(result.msg);
                    }else{
                        alert(result.msg);
                    }
                },
                error:function(xhr,textStatus){
                    alert('通讯异常,请重试!');
                }
            });
        });


        //后台登录
        function changeEndDay(){
            var params = {};
            var endDay = $("#endDay").val();
            params.endDay = endDay;
            $.ajax({
                url : "manage/updateEndDay.htm",
                type:'POST', //GET
                async:true,    //或false,是否异步
                data:params,
                timeout:5000,    //超时时间
                dataType:'text',
                success:function(data,textStatus,jqXHR){
                    var result = eval("("+data+")");
                    if(result.success == true){
                        alert(result.msg);
                    }else{
                        alert(result.msg);
                    }
                },
                error:function(xhr,textStatus){
                    alert('通讯异常,请重试!');
                }
            });

        }

    </script>

</head>
<body id="content" class="houtai_bj">

<%--<div class="zhuce_btn_div"  onclick="location='customer/toregister'"></div>--%>
<%--<div class="jiaofei_btn_div" onclick="location='onlinepay/topay'"></div>--%>
<%--<div class="houtai_btn_div" onclick="location='manage/tomanage'"></div>--%>
<div class="zhuce_btn_div"  onclick="location='<%=basePath%>customer/toregister'"></div>
<div class="jiaofei_btn_div" onclick="location='<%=basePath%>onlinepay/topay'"></div>
<div class="houtai_btn_div" onclick="location='<%=basePath%>manage/tomanage'"></div>

<div id="mask" class="mask"><span id="maskSpan" class="div" style="display:none">正在处理...</span></div>
<div  class="zhuce_body">
    <div class="zhuce_kong">
        <div class="zc">
            <div class="bj_bai">
                <h3>后台管理</h3>
                    <!-- 登录-->
                    <form id="manLoginform">
                        用户到期时间:<input id="endDay" name="endDay" type="text" class="kuang_txt phone" placeholder="用户到期时间"><br>
                        <input id="endDayBtn" name="用户到期时间" type="button" class="btn_zhuce" onclick="changeEndDay()" value="修改">
                    </form>
            </div>
        </div>
        <P>欢迎登录北京人民广播电台语言文字测试中心缴费系统</P>
    </div>
</div>

</body>
</html>