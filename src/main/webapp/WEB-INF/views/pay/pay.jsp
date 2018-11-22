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
        $(document).ready(function(){
            setInterval(queryOrderStatus,1000);//每1秒执行一次queryOrderStatus方法
            initFeeData();
        });
        //交费
        function pay(){
            if(!check()){
                return;
            }
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

        //查询订单状态
        function queryOrderStatus() {
            if($("#qcode").is(":hidden")){
                return;
            }
            $.ajax({
                url : "onlinepay/queryWechatOrderStatus.htm",
                type:'POST', //GET
                async:true,    //或false,是否异步
                data:null,
                timeout:5000,    //超时时间
                dataType:'text',
                success:function(data,textStatus,jqXHR){
                    var result = eval("("+data+")");
                    if(result.success == true){
                        location='<%=basePath%>onlinepay/toPaySuccess'
                    }
                    if(result.success == false && result.remark == '0'){
                        location='<%=basePath%>onlinepay/toPayFail'
                    }
                },
                error:function(xhr,textStatus){
//                    alert("通讯异常,请重试")
                }
            });
        }

        //
        function showAddress(){
            if($("#addressTip").is(":hidden")){
                $("#addressTip").show();
                $("#address").show();
            }
            else{
                $("#addressTip").hide();
                $("#address").hide();
            }
        }

        function check(){
            if(!$("#addressTip").is(":hidden")){
                var address = $("#address").val();
                if(address == "" || address == null){
                    alert("请填写收件地址!");
                    return false;
                }
            }
            return true;
        }

        function showVideoTip(){
            if($("#videoTip").is(":hidden")){
                $("#videoTip").show();
            }
            else{
                $("#videoTip").hide();
            }
        }

        //视频信息
        function initFeeData(){
            $.ajax({
                url:'onlinepay/showFeeInfo.htm',
                type:'POST', //GET
                async:true,    //或false,是否异步
                data:null,
                timeout:5000,    //超时时间
                dataType:'text',
                contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
                success:function(data){
                    var result = eval("("+data+")");
                    $("#examFee").val(result.examFee);
                    $("#examFeeSpan").text(result.examFee);

                    $("#bookFee").val(result.bookFee);
                    $("#bookFeeSpan").text(result.bookFee);

                    $("#videoFee").val(result.videoFee);
                    $("#videoFeeSpan").text(result.videoFee);

                    $("#mailFee").val(result.mailFee);
                    $("#mailFeeSpan").text(result.mailFee);
                },
                error:function(xhr,textStatus){
                    alert("通讯异常,请重试");
                }
            });

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

<div  class="zhuce_left">
    <span style="font-size: 8px;font-style: normal">
    普通话测试考生请注意<br>
    1、本期测试名额有限，请报名注册后立刻交费。逾期将无法参加测试。<br>
    2、注册信息请与北京市语言文字测试分中心内容一致（姓名、性别、身份证号码、联系电话。）如报名信息不一致则无法参加测试。<br>
    3、测试报名后请在次日5点之后在北京市语言文字测试分中心网站打印准考证。<br>
    4、考生必须按要求提交标准证件照、照片不符合规则报名失败。<br>
    5、测试报名费用由北京市财政统一收取，考生因个人原因造成的缺考、弃考、测试前不想参加、更换考点均无法退还测试费用。<br>
    6、选择购买教材的考生请认真填写快递地址及联系方式，交费次日将统一快递寄出。<br>
    7、网络辅导课为电脑网络课程，视频上传平台为优酷视频。选购辅导课的考生请用电脑在线观看，如无法观看请下载优酷播放器。手机无法正常观看。<br>
    8、普通话等级测试证书考取后不能修改任何信息，一旦丢失由北京市语言文字测试分中心统一办理补证手续（补证流程详见北京市语言文字测试分中心官方网站），请考生妥善保管。<br>
    9、凡报名参加一级甲等测试的考生为人工测试，无法进行网上报名、交费。考生需携带本人身份证原件、一乙证书原件及复印件、电子版照片（U盘拷贝）到测试点现场报名。<br>
    10、测试结束后，考生请等待40天后登录北京市语言文字测试分中心官网查询成绩。领取证书请携带本人身份证及准考证、办理快递邮寄请发送邮件至：diantaipeixun@vip.sina.com（邮件内注明：姓名、身份证号、快递地址、考试日期、联系方式）<br>
    </span>
</div>

<div class="zhuce_body">
    <div class="zhuce_kong">
        <div class="zc">
            <div class="bj_bai">
                <h3>学员交费</h3>
                <form id="payform">
                    <input type="checkbox" id="examFee" name="examFee" checked="checked" disabled="disabled">考试费用<span id="examFeeSpan"></span>(元)<br>
                    <input type="checkbox" id="bookFee" name="bookFee"  onclick="showAddress()">材料费用<span id="bookFeeSpan"></span>(元)&nbsp&nbsp&nbsp&nbsp<span id="addressTip" style="display: none">收件地址(默认邮费为<span id="mailFeeSpan"></span>元):</span><input type="text" id="address" name="address" style="display: none"><br>
                    <input type="checkbox" id="videoFee" name="videoFee" onclick="showVideoTip()">视频费用<span id="videoFeeSpan"></span>(元)&nbsp&nbsp&nbsp&nbsp<span id="videoTip" style="display: none">视频下载地址会通过邮件的方式发送您的邮箱</span><br>
                    <input name="交费" id="payBtn" type="button" class="btn_zhuce" onclick="pay()" value="交费">
                </form>
                <h4>注意:请在支付后稍等片刻,等待返回支付结果</h4>
                <div class="left" id="qcode" style="display: none">
                    <h4 align="center">微信扫描二维码交费</h4>
                    <div align="center"><img id="qcodeImg" src="" width="150" height="150" /></div>
                </div>
            </div>
        </div>
        <P>欢迎登录北京人民广播电台语言文字测试中心缴费系统</P>
        <div class="wechat">
            <div>关注微信公众号“北京广播影视培训中心”了解更多考试信息</div>
            <img src="resource/images/WechatIMG92.jpeg"/>
        </div>
    </div>
</div>
</body>
</html>