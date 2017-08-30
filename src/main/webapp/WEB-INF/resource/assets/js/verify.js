function __changeUserName(of)
{
	var username=$('#'+of).val();
	if(of=='email')
	{
		if (username.search(/^[\w\.+-]+@[\w\.+-]+$/) == -1) {
			showTooltips('email_input','请输入正确的Email地址');
			return;
		}					
	}
	else
	{
		if(username=='' || !isMobilePhone(username)) 
		{
			showTooltips('mobile_input','请输入正确的手机号码');
			return;
		}
	}
}

function checkPwd1(pwd1) 
{
	if (pwd1.search(/^.{6,20}$/) == -1) 
	{
		showTooltips('password1_input','密码为空或位数太少');
	}
	else 
	{
		hideTooltips('password1_input');
	}
}	

function checkEmail(email) 
{
	if (email.search(/^.+@.+$/) == -1) 
	{
		showTooltips('email_input','邮箱格式不正确');
	}
	else 
	{
		hideTooltips('email_input');
	}
}

function checkAuthCode(authcode) 
{
	if (authcode == '' || authcode.length != 6) 
	{
		showTooltips('code_input','验证码不正确');
	}
	else 
	{
		hideTooltips('code_input');
	}     
}

function check()
 {
	hideAllTooltips();
	var ckh_result = true;
	if ($('#email').val() == '') 
	{
		showTooltips('email_input','邮箱不能为空');
		ckh_result = false;
	}
	if ($('#password1').val() == '')
	{
		showTooltips('password1_input','密码不能为空');
		ckh_result = false;
	}      
	if($('#mobile').val()=='' || !isMobilePhone($('#mobile').val())) {            
		showTooltips('mobile_input','手机号码不正确');
		ckh_result = false;
	}
	if ($('#code').val() == '' || $('#code').val().length !=6) {
		showTooltips('code_input','验证码不正确');
		ckh_result = false;
	}
	if ($('#verify').attr('checked') == false){
		showTooltips('checkbox_input','对不起，您不同意Webluker的《使用协议》无法注册');
		ckh_result = false;
	}
	return ckh_result;
}

function checkMobilePhone(telphone) {
	if(telphone=='' || !isMobilePhone(telphone)) {
		showTooltips('mobile_input','请输入正确的手机号码');
	}else{
		hideTooltips('mobile_input');
	}
}

function isMobilePhone(value) {
	if(value.search(/^(\+\d{2,3})?\d{11}$/) == -1)
	return false;
	else
	return true;
} 