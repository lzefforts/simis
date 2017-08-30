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
     $(function() {



     });

     //初始化考试时间
     function initExamTime(){
         $.ajax({
             url : "customer/getExamTime.htm",
             type:'POST', //GET
             async:true,    //或false,是否异步
             data:null,
             timeout:5000,    //超时时间
             dataType:'text',
             success:function(data,textStatus,jqXHR){
                 var result = eval("("+data+")");
                 var size = result.length;
                 var $select = $('#examTime');
                 for(var i=0;i<size;i++){
                     var time = result[i]['examTime'];
                     if(time != ''){
                         time = time.split(" ")[0];
                     }
                     var examCode = result[i]['examCode'];
                     $select.append('<option value="'+time+'">'+time+'</option>');
                     $select.append('<input id="'+time+'sysExamCode" type="hidden" value="'+examCode+'">');
                 }
             },
             error:function(xhr,textStatus){
                 alert('通讯异常!');
             }
         });
     }


     //登陆
     function login(){
         var params = $("#loginform").serialize();
         $.ajax({
             url : "customer/login.htm",
             type:'POST', //GET
             async:true,    //或false,是否异步
             data:params,
             timeout:5000,    //超时时间
             dataType:'text',
             success:function(data,textStatus,jqXHR){
                 var result = eval("("+data+")");
                 if(result.success == true){
                     $("#content").unmask();
                     showPayStatus(result.remark);
                 }else{
                     $("#content").unmask();
                     alert(result.msg);
                 }
             },
             error:function(xhr,textStatus){
                 alert('通讯异常!');
             }
         });
     }

     //展示交费状态
     function showPayStatus(flag){
         if(flag == '1'){
             $("#paidStatus").text("已缴费");
             $("#toPayBtn").hide();
         }else{
             $("#paidStatus").text("未缴费;请继续缴费");
         }
         $("#loginSuccess").show();
         $("#loginform").hide();
     }


     //跳转到注册页面
     function toRegister(){
         $("#loginform").hide();
         $("#loginH3").hide();
         $("#registerform").show();
         $("#registerH3").show();
         initExamTime();
     }

     //跳转到登录页面
     function toLogin(){
         $("#loginform").show();
         $("#loginH3").show();
         $("#registerform").hide();
         $("#registerH3").hide();
     }

     //校验手机号\邮箱\身份证号码是否格式正确
     function checkMessage(){
         //校验用户名
         var name = $("#name").val();
         if(name == null || name === ''){
            alert("姓名不能为空!");
            return false;
         }
         //校验手机号
         var phone = $("#phone").val();
         if(phone == null || phone === ''){
             alert("手机号不能为空!");
             return false;
         }
         var phoneStr = /^1\d{10}$/;
         if (!phoneStr.test(phone)) {
             alert("手机号格式错误!");
             return false;
         }
         //校验邮箱
         var email = $("#email").val();
         if(email == null || email === ''){
             alert("邮箱不能为空!");
             return false;
         }
         var emailStr = /^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/;
         if (!emailStr.test(email)) {
             alert("邮箱格式错误!");
             return false;
         }
         //校验身份证号
         var cardNo = $("#cardNo").val();
         if(cardNo == null || cardNo === ''){
             alert("身份证号不能为空!");
             return false;
         }
         if (checkIdcard(cardNo)!='验证通过!') {
             alert("身份证号格式错误!");
             return false;
         }
         //校验密码
         var password = $("#passWord").val();
         if(password == null || password === ''){
             alert("密码不能为空!");
             return false;
         }

         //校验考试验证码
         var examCode = $("#examCode").val();
         if(examCode == null || examCode === ''){
             alert("考试验证码不能为空!");
             return false;
         }

         var name = $("#examTime").children('option:selected').val();
         var codeName = name+"sysExamCode";
         var sysExamCode = $("#"+codeName).val();
         if(examCode!=sysExamCode){
             alert("填写的考试验证码与考试时间不符!");
             return false;
         }
         return true;
     }

     //验证身份证
     function checkIdcard(idcard) {
         var Errors = new Array(
                 "验证通过!",
                 "身份证号码位数不对!",
                 "身份证号码出生日期超出范围或含有非法字符!",
                 "身份证号码校验错误!",
                 "身份证地区非法!"
         );
         var area = { 11: "北京", 12: "天津", 13: "河北", 14: "山西", 15: "内蒙古", 21: "辽宁", 22: "吉林", 23: "黑龙江", 31: "上海", 32: "江苏", 33: "浙江", 34: "安徽", 35: "福建", 36: "江西", 37: "山东", 41: "河南", 42: "湖北", 43: "湖南", 44: "广东", 45: "广西", 46: "海南", 50: "重庆", 51: "四川", 52: "贵州", 53: "云南", 54: "西藏", 61: "陕西", 62: "甘肃", 63: "青海", 64: "宁夏", 65: "新疆", 71: "台湾", 81: "香港", 82: "澳门", 91: "国外" }

         var idcard, Y, JYM;
         var S, M;
         var idcard_array = new Array();
         idcard_array = idcard.split("");
         //地区检验
         if (area[parseInt(idcard.substr(0, 2))] == null) return Errors[4];
         //身份号码位数及格式检验
         switch (idcard.length) {
             //15位身份号码检测
             case 15:
                 if ((parseInt(idcard.substr(6, 2)) + 1900) % 4 == 0 || ((parseInt(idcard.substr(6, 2)) + 1900) % 100 == 0 && (parseInt(idcard.substr(6, 2)) + 1900) % 4 == 0)) {
                     ereg = /^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}$/;//测试出生日期的合法性
                 } else {
                     ereg = /^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}$/;//测试出生日期的合法性
                 }
                 if (ereg.test(idcard)) return Errors[0];
                 else return Errors[2];
                 break;
             //18位身份号码检测
             case 18:
                 if (parseInt(idcard.substr(6, 4)) % 4 == 0 || (parseInt(idcard.substr(6, 4)) % 100 == 0 && parseInt(idcard.substr(6, 4)) % 4 == 0)) {
                     ereg = /^[1-9][0-9]{5}19[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}[0-9Xx]$/;//闰年出生日期的合法性正则表达式
                 } else {
                     ereg = /^[1-9][0-9]{5}19[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}[0-9Xx]$/;//平年出生日期的合法性正则表达式
                 }
                 if (ereg.test(idcard)) {//测试出生日期的合法性
                     //计算校验位
                     S = (parseInt(idcard_array[0]) + parseInt(idcard_array[10])) * 7
                             + (parseInt(idcard_array[1]) + parseInt(idcard_array[11])) * 9
                             + (parseInt(idcard_array[2]) + parseInt(idcard_array[12])) * 10
                             + (parseInt(idcard_array[3]) + parseInt(idcard_array[13])) * 5
                             + (parseInt(idcard_array[4]) + parseInt(idcard_array[14])) * 8
                             + (parseInt(idcard_array[5]) + parseInt(idcard_array[15])) * 4
                             + (parseInt(idcard_array[6]) + parseInt(idcard_array[16])) * 2
                             + parseInt(idcard_array[7]) * 1
                             + parseInt(idcard_array[8]) * 6
                             + parseInt(idcard_array[9]) * 3;
                     Y = S % 11;
                     M = "F";
                     JYM = "10X98765432";
                     M = JYM.substr(Y, 1);//判断校验位
                     if (M == idcard_array[17]) return Errors[0];//检测ID的校验位
                     else return Errors[3];
                 }
                 else return Errors[2];
                 break;
             default:
                 return Errors[1];
                 break;
         }

     }



     //注册
     function register(){
         if(checkMessage()){
             showMask();
             var params = $("#registerform").serialize();
             var examTime = $("#examTime").val();
             params.examTime = examTime;
             $.ajax({
                 url : "customer/register.htm",
                 type:'POST', //GET
                 async:true,    //或false,是否异步
                 data:params,
                 timeout:5000,    //超时时间
                 dataType:'text',
                 success:function(data,textStatus,jqXHR){
                     var result = eval("("+data+")");
                     if(result.success == true){
                         hideMask();
                         $("#registerform").hide();
                         showPayStatus(result.remark);
                     }else{
                         hideMask();
                         alert(result.msg);
                     }
                 },
                 error:function(xhr,textStatus){
                     alert("通讯异常,请重试");
                 }
             });
         }
     }

     //重置
     function reset(){
         $("form input").each(function(node){
             node.val("");
         })
     }

     //交费跳转
     function pay(){
         window.location='<%=basePath%>onlinepay/topay';
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
<body id="content" class="login_bj">
<div class="zhuce_btn_div"  onclick="location='<%=basePath%>customer/toregister'"></div>
<div class="jiaofei_btn_div" onclick="location='<%=basePath%>onlinepay/topay'"></div>
<div class="houtai_btn_div" onclick="location='<%=basePath%>manage/tomanage'"></div>

<div id="mask" class="mask"><span id="maskSpan" class="div" style="display:none">正在处理...</span></div>
<div  class="zhuce_body">
    <div class="zhuce_kong">
        <div class="zc">
            <div class="bj_bai">
                <h3 id="loginH3">学员登录</h3>
                <form id="loginform">
                    <table>
                        <tr>
                            <td>身份证号:</td>
                            <td><input name="userName" type="text" class="kuang_txt phone" placeholder="身份证号"></td>
                        </tr>
                        <tr>
                            <td>密   码:</td>
                            <td><input name="passWord" type="password" class="kuang_txt possword" placeholder="密码"></td>
                        </tr>
                    </table>
                    <input name="登陆" type="button" class="btn_zhuce" onclick="login()" value="登陆">
                    <input name="注册" type="button" class="btn_zhuce" onclick="toRegister()" value="去注册">
                </form>
                <h3 id="registerH3" style="display: none">学员注册</h3>
                <form id="registerform" style="display: none">
                    <table>
                        <tr>
                            <td>姓  名:</td>
                            <td><input id="name" name="name" type="text" class="kuang_txt phone" placeholder="姓名"></td>
                        </tr>
                        <tr>
                            <td>手机号:</td>
                            <td><input id="phone" name="phone" type="text" class="kuang_txt phone" placeholder="手机号"></td>
                        </tr>
                        <tr>
                            <td>邮   箱:</td>
                            <td><input id="email" name="email" type="text" class="kuang_txt email" placeholder="邮箱"></td>
                        </tr>
                        <tr>
                            <td>身份证号:</td>
                            <td><input id="cardNo" name="cardNo" type="text" class="kuang_txt phone" placeholder="身份证号"></td>
                        </tr>
                        <tr>
                            <td>密   码:</td>
                            <td><input id="passWord" name="passWord" type="passWord" class="kuang_txt possword" placeholder="密码"></td>
                        </tr>
                        <tr>
                            <td>性别:</td>
                            <td>
                                <select name="sex">
                                    <option value ="0">男</option>
                                    <option value ="1">女</option>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td>考试时间:</td>
                            <td>
                                <select id="examTime" name="examTime">
                                    <%--<option value ="0">本月第一批</option>--%>
                                    <%--<option value ="1">本月第二批</option>--%>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td>考试验证码:</td>
                            <td><input id="examCode" class="kuang_txt phone" placeholder="考试验证码"></td>
                        </tr>
                    </table>
                    <br>
                    <input name="注册" type="button" class="btn_zhuce" onclick="register()" value="注册">
                    <input name="重置" type="button" class="btn_zhuce" onclick="reset()" value="重置">
                    <input name="登陆" type="button" class="btn_zhuce" onclick="toLogin()" value="去登陆">
                </form>
                <div id="loginSuccess" style="display: none">
                    欢迎登陆系统<br>
                    交费状态为<font id="paidStatus" color="red"></font><br>
                    <input id="toPayBtn" name="交费" type="button" class="btn_zhuce" onclick="pay()" value="继续交费">
                </div>
            </div>
        </div>
        <P>欢迎登录北京人民广播电台语言文字测试中心缴费系统</P>
    </div>
</div>

</body>
</html>