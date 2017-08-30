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
            $.datepicker.regional['zh-CN'] = {
                clearText: '清除',
                clearStatus: '清除已选日期',
                closeText: '关闭',
                closeStatus: '不改变当前选择',
                prevText: '< 上月',
                prevStatus: '显示上月',
                prevBigText: '<<',
                prevBigStatus: '显示上一年',
                nextText: '下月>',
                nextStatus: '显示下月',
                nextBigText: '>>',
                nextBigStatus: '显示下一年',
                currentText: '今天',
                currentStatus: '显示本月',
                monthNames: ['一月','二月','三月','四月','五月','六月', '七月','八月','九月','十月','十一月','十二月'],
                monthNamesShort: ['一月','二月','三月','四月','五月','六月', '七月','八月','九月','十月','十一月','十二月'],
                monthStatus: '选择月份',
                yearStatus: '选择年份',
                weekHeader: '周',
                weekStatus: '年内周次',
                dayNames: ['星期日','星期一','星期二','星期三','星期四','星期五','星期六'],
                dayNamesShort: ['周日','周一','周二','周三','周四','周五','周六'],
                dayNamesMin: ['日','一','二','三','四','五','六'],
                dayStatus: '设置 DD 为一周起始',
                dateStatus: '选择 m月 d日, DD',
                dateFormat: 'yy-mm-dd',
                firstDay: 1,
                initStatus: '请选择日期',
                isRTL: false};
            $.datepicker.setDefaults($.datepicker.regional['zh-CN']);
            $( "#firstTime" ).datepicker({
                showOtherMonths: true,
                selectOtherMonths: true,
//                showButtonPanel: true,
                showOn: "both",
                //buttonImageOnly: true,
                //buttonImage: "calendar.gif",
                buttonText: "设置",
                changeMonth: true,
                changeYear: false,
                defaultDate:new Date()
            });
            $( "#secondTime" ).datepicker({
                showOtherMonths: true,
                selectOtherMonths: true,
                showButtonPanel: true,
                showOn: "both",
                //buttonImageOnly: true,
                //buttonImage: "calendar.gif",
                buttonText: "设置",
                changeMonth: true,
                changeYear: false,
                defaultDate:new Date()
            });
            firstInitExamTime();
            initVideoData();
            initFeeData();
            initAllExamTime();
        });
        //后台登录
        function login(){
            var params = {};
            var userName = $("#userName").val();
            var passWord = $("#passWord").val();
            params.userName = userName;
            params.passWord = passWord;
            $.ajax({
                url : "manage/login.htm",
                type:'POST', //GET
                async:true,    //或false,是否异步
                data:params,
                timeout:5000,    //超时时间
                dataType:'text',
                success:function(data,textStatus,jqXHR){
                    var result = eval("("+data+")");
                    if(result.success == true){
                        $("#manLoginform").hide();
                        $("#setExamform").show();
                        $("#importDiv").show();
                        $("#exportForm").show();
                        $("#deleteForm").show();
                        $("#videoInfoForm").show();
                        $("#feeInfoForm").show();
                        initExamTime();
                    }else{
                        alert(result.msg);
                    }
                },
                error:function(xhr,textStatus){
                    alert('通讯异常,请重试!');
                }
            });

        }


        //设置考试时间
        function setExamTime(){
            var params = new Array();
            var firstObj = {};
            var firstTime = $("#firstTime").val();
            var firstPepNum = $("#firstPepNum").val();
            var firstExamCode = $("#firstExamCode").val();
            if(firstTime == null || firstTime == ''){
                alert("所选月的第一次考试时间不能为空!")
                return;
            }
            if(firstPepNum == null || firstPepNum == ''){
                alert("所选月的第一次考试的人数不能为空!")
                return;
            }
            if(firstExamCode == null || firstExamCode == ''){
                alert("所选月的第一次考试的验证码不能为空!")
                return;
            }
            var selectedFirstMonth = firstTime.split("-")[1];
            if(Number(selectedFirstMonth)!=Number($("#month").val())){
                alert("所选的考试时间必须为"+Number($("#month").val())+"月!");
                return;
            }
            firstObj.month = $("#month").val();
            firstObj.examTime = firstTime;
            firstObj.examPepNum = firstPepNum;
            firstObj.examCode = firstExamCode;
            firstObj.examType = '0';
            params.push(firstObj);
            //第二次考试时间
            if(!$("#secondTimeP").is(":hidden")){
                var secondObj = {};
                var secondTime = $("#secondTime").val();
                var secondPepNum = $("#secondPepNum").val();
                var secondExamCode = $("#secondExamCode").val();
                if(secondTime == null || firstTime == ''){
                    alert("所选月的第二次考试时间不能为空!")
                    return;
                }
                if(secondPepNum == null || secondPepNum == ''){
                    alert("所选月的第二次考试的人数不能为空!")
                    return;
                }
                if(secondExamCode == null || secondExamCode == ''){
                    alert("所选月的第二次考试的验证码不能为空!")
                    return;
                }
                var selectedSecondMonth = secondTime.split("-")[1];
                if(Number(selectedSecondMonth)!=Number($("#month").val())){
                    alert("所选的考试时间必须为"+Number($("#month").val())+"月!");
                    return;
                }

                secondObj.month = $("#month").val();
                secondObj.examTime = secondTime;
                secondObj.examPepNum = secondPepNum;
                secondObj.examCode = secondExamCode;
                secondObj.examType = '1';
                params.push(secondObj);
            }

            $.ajax({
                url : "manage/changeExamBatch.htm",
                type:'POST', //GET
                async:true,    //或false,是否异步
//                data:JSON.stringify(params),
                data:{"paramVos":JSON.stringify(params)},
                timeout:5000,    //超时时间
                dataType:'text',
                success:function(data,textStatus,jqXHR){
                    var result = eval("("+data+")");
                    if(result.success == true){
                        hideMask()
                        alert(result.msg);
                    }else{
                        alert(result.msg);
                    }
                },
                error:function(xhr,textStatus){
                    alert("通讯异常,请重试");
                }
            });

        }


        //初始化考试时间
        function initExamTime(){
            $("#month").change(function() {
                var option = $(this).children('option:selected').val();
                var params = {};
                params.month = option;
                $.ajax({
                    url : "manage/getExamBatch.htm",
                    type:'POST', //GET
                    async:true,    //或false,是否异步
                    data:params,
                    timeout:5000,    //超时时间
                    dataType:'text',
                    success:function(data,textStatus,jqXHR){
                        var result = eval("("+data+")");
                        if(result.length > 0){
                            hideMask();
                            var firstTime = result[0]['examTime'].split(" ")[0];
                            var firstExamPepNum = result[0]['examPepNum'];
                            var firstPaidPepNum = result[0]['paidPepNum'];
                            var firstExamCode = result[0]['examCode'];
                            $("#firstTime").val(firstTime);
                            $("#firstPepNum").val(firstExamPepNum);
                            $("#firstPaidPepNum").text(firstPaidPepNum);
                            $("#firstExamCode").val(firstExamCode);
                            if(result.length > 1){
                                var secondTime = result[1]['examTime'].split(" ")[0];
                                var secondExamPepNum = result[1]['examPepNum'];
                                var secondPaidPepNum = result[1]['paidPepNum'];
                                var secondExamCode = result[1]['examCode'];
                                $("#secondTime").val(secondTime);
                                $("#secondPepNum").val(secondExamPepNum);
                                $("#secondPaidPepNum").text(secondPaidPepNum);
                                $("#secondExamCode").val(secondExamCode);
                                $("#isShowSecondTimeBtn").prop("checked","checked");
                                $("#secondTimeP").show();
                                $("#secondCodeP").show();
                            }
                            if(result.length == 1){
                                $("#secondTime").val("");
                                $("#secondPepNum").val("");
                                $("#secondPaidPepNum").text("");
                                $("#secondExamCode").val("");
                                $("#isShowSecondTimeBtn").prop("checked",false);
                                $("#secondTimeP").hide();
                                $("#secondCodeP").hide();
                            }
                        }else{
                            $("#firstTime").val("");
                            $("#firstPepNum").val("");
                            $("#firstPaidPepNum").text("");
                            $("#firstExamCode").val("");
                            $("#isShowSecondTimeBtn").prop("checked",false);
                            $("#secondTime").val("");
                            $("#secondPepNum").val("");
                            $("#secondPaidPepNum").text("");
                            $("#secondExamCode").val("");
                            $("#secondTimeP").hide();
                            $("#secondCodeP").hide();
                        }
                    },
                    error:function(xhr,textStatus){
                        alert("通讯异常,请重试");
                    }
                });

            });
        }

        //第一次进页面时初始化考试时间
        function firstInitExamTime(){
            var params = {};
            params.month = 1;
            $.ajax({
                url : "manage/getExamBatch.htm",
                type:'POST', //GET
                async:true,    //或false,是否异步
                data:params,
                timeout:5000,    //超时时间
                dataType:'text',
                success:function(data,textStatus,jqXHR){
                    var result = eval("("+data+")");
                    if(result.length > 0){
                        hideMask();
                        var firstTime = result[0]['examTime'].split(" ")[0];
                        var firstExamPepNum = result[0]['examPepNum'];
                        var firstExamCode = result[0]['examCode'];
                        $("#firstTime").val(firstTime);
                        $("#firstPepNum").val(firstExamPepNum);
                        $("#firstExamCode").val(firstExamCode);
                        if(result.length > 1){
                            var secondTime = result[1]['examTime'].split(" ")[0];
                            var secondExamPepNum = result[1]['examPepNum'];
                            var secondExamCode = result[1]['examCode'];
                            $("#secondTime").val(secondTime);
                            $("#secondExamCode").val(secondExamCode);
                            $("#firstExamCode").val(firstExamCode);
                            $("#isShowSecondTimeBtn").prop("checked",true);
                            $("#secondTimeP").show();
                            $("#secondCodeP").show();
                        }
                    }
                },
                error:function(xhr,textStatus){
                    alert("通讯异常,请重试");
                }
            });

        }


        //导入文件
        function importExcel(){
            if(checkData()){
                showMask();
                var formData = new FormData($( "#form1" )[0]);
                $.ajax({
                    url:'manage/ajaxUpload.htm',
                    type:'POST', //GET
                    data: formData,
                    async: true,
                    cache: false,
                    contentType: false,
                    processData: false,
                    success:function(){
                        hideMask();
                        $("#upfile").val("");
                        alert("上传成功");
                    },
                    error:function(xhr,textStatus){
                        alert("通讯异常,请重试");
                    }
                });

            }
        }

        //导出文件
        function exportExcel(){
            var params = {};
            sqlName  = "customer";
            var exportType = $("#exportType").children('option:selected').val();
            var exportTime = $("#exportTime").children('option:selected').val();
            common.excelFormSub(sqlName,exportType,exportTime,params);
        }

        //JS校验form表单信息
        function checkData(){
            var fileDir = $("#upfile").val();
            var suffix = fileDir.substr(fileDir.lastIndexOf("."));
            if("" == fileDir){
                alert("选择需要导入的Excel文件！");
                return false;
            }
            if(".xls" != suffix && ".xlsx" != suffix ){
                alert("选择Excel格式的文件导入！");
                return false;
            }
            var importTime = $("#importTime").children('option:selected').val();

            if(importTime == null || importTime == ''){
                alert("选择导入文件对应的考试时间！");
                return false;
            }

            return true;
        }

        //重置
        function reset(){
            $("form input").each(function(node){
                node.val("");
            })
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

        //显示第二次考试时间
        function showSecondTime(){
            if($("#secondTimeP").is(":hidden")){
                $("#secondTimeP").show();
                $("#secondCodeP").show();
            }
            else{
                $("#secondTimeP").hide();
                $("#secondCodeP").hide();
            }
        }

        //修改视频资料信息
        function setVideoInfo(){
            if(checkVideoData()){
                showMask();
                var params = $( "#videoInfoForm").serialize();
                $.ajax({
                    url:'manage/changeVideoInfo.htm',
                    type:'POST', //GET
                    async:true,    //或false,是否异步
                    data:params,
                    timeout:5000,    //超时时间
                    dataType:'text',
                    contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
                    success:function(data){
                        var result = eval("("+data+")");
                        if(result.success == true){
                            hideMask()
                            alert(result.msg);
                        }else{
                            alert(result.msg);
                        }
                    },
                    error:function(xhr,textStatus){
                        alert("通讯异常,请重试");
                    }
                });

            }


        }


        function checkVideoData(){
            var videoAddress = $("#videoAddress").val();
            var videoCode = $("#videoCode").val();
            if(videoAddress == null || videoAddress == ''){
                alert("视频地址不能为空!")
                return false;
            }
            if(videoCode == null || videoCode == ''){
                alert("视频密码不能为空!")
                return false;
            }
            return true;
        }

        //视频信息
        function initVideoData(){
            $.ajax({
                url:'manage/showVideoInfo.htm',
                type:'POST', //GET
                async:true,    //或false,是否异步
                data:null,
                timeout:5000,    //超时时间
                dataType:'text',
                contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
                success:function(data){
                    var result = eval("("+data+")");
                    $("#videoAddress").val(result.videoAddress);
                    $("#videoCode").val(result.videoCode);
                },
                error:function(xhr,textStatus){
                    alert("通讯异常,请重试");
                }
            });

        }



        //修改视频资料信息
        function setFeeInfo(){
            if(checkFeeData()){
                showMask();
                var params = $( "#feeInfoForm").serialize();
                $.ajax({
                    url:'manage/changeFeeInfo.htm',
                    type:'POST', //GET
                    async:true,    //或false,是否异步
                    data:params,
                    timeout:5000,    //超时时间
                    dataType:'text',
                    contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
                    success:function(data){
                        var result = eval("("+data+")");
                        if(result.success == true){
                            hideMask()
                            alert(result.msg);
                        }else{
                            alert(result.msg);
                        }
                    },
                    error:function(xhr,textStatus){
                        alert("通讯异常,请重试");
                    }
                });

            }


        }


        function checkFeeData(){
            var examFee = $("#examFee").val();
            var bookFee = $("#bookFee").val();
            var videoFee = $("#videoFee").val();
            var mailFee = $("#mailFee").val();
            if(examFee == null || examFee == ''){
                alert("考试费用不能为空!")
                return false;
            }
            if(bookFee == null || bookFee == ''){
                alert("材料费用不能为空!")
                return false;
            }
            if(videoFee == null || videoFee == ''){
                alert("视频费用不能为空!")
                return false;
            }
            if(mailFee == null || mailFee == ''){
                alert("邮寄费用不能为空!")
                return false;
            }
            return true;
        }

        //视频信息
        function initFeeData(){
            $.ajax({
                url:'manage/showFeeInfo.htm',
                type:'POST', //GET
                async:true,    //或false,是否异步
                data:null,
                timeout:5000,    //超时时间
                dataType:'text',
                contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
                success:function(data){
                    var result = eval("("+data+")");
                    $("#examFee").val(result.examFee);
                    $("#bookFee").val(result.bookFee);
                    $("#videoFee").val(result.videoFee);
                    $("#mailFee").val(result.mailFee);
                },
                error:function(xhr,textStatus){
                    alert("通讯异常,请重试");
                }
            });

        }


        function initAllExamTime(){
            $.ajax({
                url:'manage/getAllExamBatch.htm',
                type:'POST', //GET
                async:true,    //或false,是否异步
                data:null,
                timeout:5000,    //超时时间
                dataType:'text',
                contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
                success:function(data){
                    var result = eval("("+data+")");
                    for(var obj in result){
                        var str = result[obj].examTime.split(" ")[0];
                        var html = '<option value='+str+'>'+str+'</option>';
                        $("#exportTime").append(html);
                        $("#deleteTime").append(html);
                        $("#importTime").append(html);
                    }
                },
                error:function(xhr,textStatus){
                    alert("通讯异常,请重试");
                }
            });

        }



        function deleteData(){
            var deleteTime = $("#deleteTime").children('option:selected').val();
            $.ajax({
                url:'manage/deleteData.htm',
                type:'POST', //GET
                async:true,    //或false,是否异步
                data:{"examTime":deleteTime},
                timeout:5000,    //超时时间
                dataType:'text',
                success:function(data){
                    var result = eval("("+data+")");
                    if(result.success == true){
                        hideMask()
                        alert(result.msg);
                    }else{
                        alert(result.msg);
                    }
                },
                error:function(xhr,textStatus){
                    alert("通讯异常,请重试");
                }
            });
        }


        function showResetPasswrod(){
            $("#oldPswSpan").hide();
            $("#loginBtn").hide();
            $("#resetBtn").hide();
            $("#toChangePswBtn").hide();
            $("#newPswSpan").show();
            $("#resetPasswordBtn").show();
            $("#toLoginBtn").show();
        }

        function showLogin(){
            $("#oldPswSpan").show();
            $("#loginBtn").show();
            $("#resetBtn").show();
            $("#toChangePswBtn").show();
            $("#newPswSpan").hide();
            $("#resetPasswordBtn").hide();
            $("#toLoginBtn").hide();
        }

        //修改密码
        function resetPasswrod(){
            if($("#userName").val() == null || $("#userName").val() == ''){
                alert("用户名不能为空!");
                return;
            }
            if($("#newPassWord").val() == null || $("#newPassWord").val() == ''){
                alert("新密码不能为空!");
                return;
            }
            $.ajax({
                url:'manage/changePassword.htm',
                type:'POST', //GE
                async:true,    //或false,是否异步
                data:{"userName":$("#userName").val(),"password":$("#newPassWord").val()},
                timeout:5000,    //超时时间
                dataType:'text',
                success:function(data){
                    var result = eval("("+data+")");
                    if(result.success == true){
                        hideMask()
                        alert(result.msg);
                    }else{
                        alert(result.msg);
                    }
                },
                error:function(xhr,textStatus){
                    alert("通讯异常,请重试");
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

<jsp:include page="exportExcelCom.jsp"></jsp:include>

<div id="mask" class="mask"><span id="maskSpan" class="div" style="display:none">正在处理...</span></div>
<div  class="zhuce_body">
    <div class="zhuce_kong">
        <div class="zc">
            <div class="bj_bai">
                <h3>后台管理</h3>
                    <!-- 登录-->
                    <form id="manLoginform">
                        用户名称:<input id="userName" name="userName" type="text" class="kuang_txt phone" placeholder="用户名"><br>
                        <span id="oldPswSpan">登录密码:<input id="passWord" name="passWord" type="password" class="kuang_txt possword" placeholder="密码"><br></span>
                        <span id="newPswSpan" style="display: none">新密码:<input id="newPassWord" name="newPassWord" type="password" class="kuang_txt possword" placeholder="新密码"><br></span>
                        <input id="loginBtn" name="登陆" type="button" class="btn_zhuce" onclick="login()" value="管理员登陆">
                        <input id="resetBtn" name="重置" type="button" class="btn_zhuce" onclick="reset()" value="重置">
                        <input id="toChangePswBtn" name="去修改密码" type="button" class="btn_zhuce" onclick="showResetPasswrod()" value="去修改密码">
                        <input id="toLoginBtn" name="去登录" type="button" class="btn_zhuce" style="display: none" onclick="showLogin()" value="去登录">
                        <input id="resetPasswordBtn" name="修改密码" type="button" class="btn_zhuce" style="display: none" onclick="resetPasswrod()" value="修改密码">
                    </form>

                    <!--设置考试时间 -->
                    <form id="setExamform" style="display: none">
                        设置考试时间的月份:
                        <select id="month" name="month">
                            <option value ="1">1月</option>
                            <option value ="2">2月</option>
                            <option value ="3">3月</option>
                            <option value ="4">4月</option>
                            <option value ="5">5月</option>
                            <option value ="6">6月</option>
                            <option value ="7">7月</option>
                            <option value ="8">8月</option>
                            <option value ="9">9月</option>
                            <option value ="10">10月</option>
                            <option value ="11">11月</option>
                            <option value ="12">12月</option>
                        </select><br>
                        <p>设置第一次考试时间:<input type="text" id="firstTime" name="firstTime" readonly="readonly" size="10">设置考试人数:<input type="text" id="firstPepNum" name="firstPepNum" size="5">已交费人数:<span id="firstPaidPepNum"></span></p>
                        <p>设置第一次考试码:<input type="text" id="firstExamCode" name="firstExamCode" size="10"></p>
                        <p>是否设置第二次考试时间:<input type="checkbox" id="isShowSecondTimeBtn" onclick="showSecondTime()"></p>
                        <p id="secondTimeP" style="display: none">设置第二次考试时间:<input type="text" id="secondTime" name="secondTime" readonly="readonly" size="10">设置考试人数:<input type="text" id="secondPepNum" name="secondPepNum" size="5">已交费人数:<span id="secondPaidPepNum"></span></p>
                        <p id="secondCodeP" style="display: none">设置第二次考试码:<input type="text" id="secondExamCode" name="secondExamCode" size="10"></p>
                        <p><input name="修改考试时间" type="button" class="btn_zhuce" onclick="setExamTime()" value="修改考试时间"></p>
                    </form>

                    <form id="videoInfoForm"  style="display: none">
                        <p>视频资料地址:<input type="text" id="videoAddress" name="videoAddress">&nbsp;视频资料密码:<input type="text" id="videoCode" name="videoCode">&nbsp;<input name="修改视频信息" type="button" class="btn_zhuce" onclick="setVideoInfo()" value="修改视频信息"></p>
                    </form>

                    <form id="feeInfoForm" style="display: none">
                        <p>考试费用:<input type="text" id="examFee" name="examFee" size="5">元&nbsp;材料费用:<input type="text" id="bookFee" name="bookFee" size="5">元&nbsp;视频费用:<input type="text" id="videoFee" name="videoFee" size="5">元&nbsp;邮寄费用:<input type="text" id="mailFee" name="mailFee" size="5">元</p>
                        <p><input name="修改费用信息" type="button" class="btn_zhuce" onclick="setFeeInfo()" value="修改费用信息"></p>
                    </form>


                <!--导入-->
                    <div id="importDiv" style="display: none">
                        <p>
                        <%--<form method="POST"  enctype="multipart/form-data" id="form1" action="manage/upload.htm"--%>
                              <%--target="framFile">--%>
                        <form id="form1">
                            导入数据对应的考试时间:
                            <select id="importTime" name="importTime">
                            </select>
                            <input name="flag" value="false" type="hidden">
                            <input id="upfile" type="file" name="file">
                            <%--<input name="提交" type="submit"  onclick="return checkData()" value="上传EXCEL">--%>
                        </form>
                        <input name="导入EXCEL" type="button" class="btn_zhuce" onclick="importExcel()" value="导入">
                        <%--<a href="importTemplate/导入模板.xls"><font style="color: red">导入EXCEL模板</font></a>--%>
                        </p>
                    </div>
                    <!--导出-->
                    <form id="exportForm" style="display: none">
                        <p>
                        导出时间:
                        <select id="exportTime" name="exportTime">
                        </select>
                        导出类型:
                        <select id="exportType" name="exportType">
                            <option value ="0">导出已登记已缴费</option>
                            <option value ="1">导出已缴费未注册</option>
                            <option value ="2">导出全部</option>
                            <option value ="3">导出有邮寄地址的</option>
                        </select>
                        <input name="导出" type="button" class="btn_zhuce" onclick="exportExcel()" value="导出">
                        </p>
                    </form>
                    <!--删除数据-->
                    <form id="deleteForm" style="display: none">
                        <p>
                            删除时间:
                            <select id="deleteTime" name="deleteTime">
                            </select>
                            <input name="删除" type="button" class="btn_zhuce" onclick="deleteData()" value="删除数据">
                        </p>
                    </form>
            </div>
        </div>
        <P>欢迎登录北京人民广播电台语言文字测试中心缴费系统</P>
    </div>
</div>

</body>
</html>