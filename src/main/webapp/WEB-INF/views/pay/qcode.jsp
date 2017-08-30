<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    // 获得本项目的地址(例如: http://localhost:8080/MyApp/)赋值给basePath变量
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
    <base href="<%=basePath%>simis/">
    <meta charset="UTF-8">
    <title>北京人民广播电台语言文字测试中心缴费系统</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="/resource/core/css/mask.css" rel="stylesheet">
    <link href="/resource/core/css/login.css" rel="stylesheet">
    <link href="/resource/assets/css/jquery.loadmask.css" rel="stylesheet" type="text/css" />
    <script src="/resource/assets/js/jquery-1.11.1.js"></script>
    <script src="/resource/assets/js/jquery.loadmask.js"></script>
    <style>
        #content { padding:5px;width:200px; }
    </style>
    <script type="text/javascript">
        //交费
        function pay(){
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
                        alert(result.msg);
//                        $("#qcodeImgDiv").
                        $("#qcodeImg").attr("src", result.msg);
                        $("#qcode").show();
                    }else{
                        alert(result.msg)
                    }
                },
                error:function(xhr,textStatus){
                    alert("通讯异常,请重试")
                }
            });
        }


    </script>

</head>
<body id="content" class="jiaofei_bj">

<div class="zhuce_btn_div"  onclick="location='customer/toregister'"></div>
<div class="jiaofei_btn_div" onclick="location='onlinepay/topay'"></div>
<div class="houtai_btn_div" onclick="location='manage/tomanage'"></div>

<div  class="zhuce_body">
    <div class="zhuce_kong">
        <div class="zc">
            <div class="bj_bai">
                <h3>学员交费</h3>
                <form id="payform">
                    <input type="checkbox" id="examFee" name="examFee" value="50" checked readonly>考试费用50(元)<br>
                    <input type="checkbox" id="bookFee" name="bookFee" value="10">材料费用10(元)<br>
                    <input type="checkbox" id="videoFee" name="videoFee" value="10">视频费用10(元)<br>
                    <input name="交费" type="button" class="btn_zhuce" onclick="pay()" value="交费">
                </form>
                <div class="left" id="qcode" style="display: none">
                    <h4 align="center">手机扫描二维码交费</h4>
                    <div id="qcodeImgDiv" align="center">
                        <%--<img id="qcodeImg" src="" width="150" height="150" />--%>
                    </div>
                </div>
            </div>
        </div>
        <P>欢迎登录北京人民广播电台语言文字测试中心缴费系统</P>
    </div>
</div>
</body>
</html>