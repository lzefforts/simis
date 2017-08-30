//打开一个本地连接
function openAjax(address, content) {
	var respText = '';
	var xml;
	var xmlhttp;
	//判断浏览器是否支持ActiveX控件
	if(window.ActiveXObject){
	//支持-通过ActiveXObject的一个新实例来创建XMLHttpRequest对象
	xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
	//不支持
	else if(window.XMLHttpRequest){
	xmlhttp = new XMLHttpRequest();
	}		 
	xmlhttp.open("post", address, false);
	xmlhttp.send(content);
	var result = xmlhttp.status;
	if (result == 200) {
		respText = xmlhttp.responseText;
	}
	xmlhttp = null;

	return respText;
}


//获取返回参数
function getRequest() {
	var respText;
	var xml;
	var xmlhttp = window.XMLHttpRequest ? new XMLHttpRequest()
			: window.ActiveXObject ? new ActiveXObject("Microsoft.XMLHTTP")
					: new XMLHttpRequest();
	return xmlhttp;
}

//获取xml对象的返回参数
function get_xml_object(xml_str) {        
        var xmlObj;
    	if (window.ActiveXObject) {
    		xmlObj = new ActiveXObject("Microsoft.XMLDOM");
    		xmlObj.async = false;
    		xmlObj.loadXML(xml_str);
    	} else {
    		xmlObj = new DOMParser().parseFromString(xml_str, "text/xml");
    	}

    	return xmlObj;
}
//初始化扫描
function doinit(fplx) {
	var address = getIP();
	var init = "<?xml version=\"1.0\" encoding=\"utf-8\" ?><req><type>SCANINI</type><reqData><fplx>"
			+ fplx + "</fplx></reqData></req>";
	var respText = openAjax(address, init);// undefined
	return respText;
}
//扫描
function doscan() {
	var address = getIP();
	var scan = "<?xml version=\"1.0\" encoding=\"utf-8\" ?><req><type>SCAN</type></req>";
	var respText = openAjax(address, scan);
	return respText;
}
//扫描结束
function doover() {
	var address = getIP();
	var over = "<?xml version=\"1.0\" encoding=\"utf-8\" ?><req><type>SCANOVER</type></req>";
	var respText = openAjax(address, over);
	return respText;
}
//超时
function quit() {
	var address = getIP();
	var over = "<?xml version=\"1.0\" encoding=\"utf-8\" ?><req><type>TIMEOUT</type></req>";
	openAjax(address, over);
}
//设定的本地ip
function getIP(){
	var address = "http://localhost:7749";
	return address;
}


//开启金税盘0 手工上传//1 自动上传（默认）
function opencard(password,scfs) {
	var address = getIP();
	var over = "<?xml version=\"1.0\" encoding=\"utf-8\" ?><req><type>OpenCard</type><reqData><CertPassWord>"+password+"</CertPassWord><UploadInvoiceAuto>"+scfs+"</UploadInvoiceAuto></reqData></req>";
	var respText = openAjax(address, over);
	var xmloo = get_xml_object(respText);
	var RetCode = $(xmloo).find("RetCode").text();
	if(RetCode=="1011"){
		var IsInvEmpty = $(xmloo).find("IsInvEmpty").text();
		if(IsInvEmpty=="0"){
			var IsLockReached = $(xmloo).find("IsLockReached").text();
			if(IsLockReached=="0"){
				//正常返回
			}else{
				jQuery.jqalert("已到锁死期!");
				return null;
			}
		}else{
			jQuery.jqalert("无可用发票!");
			return null;
		}
	}else{
		var RetMsg = $(xmloo).find("RetMsg").text();
		if(RetMsg!=undefined && RetMsg!=null && RetMsg!="")
		jQuery.jqalert(RetMsg);
		return null;
	}
	return xmloo;
}



//查询库存发票0 增值税专用发票 ；2 增值税普通发票；11 货物运输业增值税专用发票；12 机动车销售统一发票
function getinfo(fplx) {
	var address = getIP();
	var over = "<?xml version=\"1.0\" encoding=\"utf-8\" ?><req><type>GetInfo</type><reqData><InfoKind>"+fplx+"</InfoKind></reqData></req>";
	var respText = openAjax(address, over);
	var xmloo = get_xml_object(respText);
	var RetCode = $(xmloo).find("RetCode").text();
	var InfoTypeCode = $(xmloo).find("InfoTypeCode").text();
	if(RetCode=="3011"){
		if(InfoTypeCode!=null && InfoTypeCode.length>0){
			
		}else{
			jQuery.jqalert("发票库存为空!");
			return null;
		}
		//查询成功
		//		RetCode 3011 读取成功；其它为失败
		//		RetMsg 错误描述信息
		//		InfoTypeCode 要开具发票的十位代码。
		//		为空时，表示无可用发票
		//		InfoNumber 要开具发票的号码
		//		InvStock 剩余的可用发票份数
		//		TaxClock 金税盘时钟

	}else{
		var RetMsg = $(xmloo).find("RetMsg").text();
		if(RetMsg!=undefined && RetMsg!=null && RetMsg!="")
		jQuery.jqalert(RetMsg);
		return null;
	}
	return xmloo;
}




//传入开票数据，将开票数据记入防伪税控开票数据库，并在金税盘中开具此发票。
//调用本方法前需将 CheckEWM 值设置为 0（为 1 时用于发票
//校验，为 2 时用于空白作废，默认值为 0）
//注意：一旦 CheckEWM 值设置为 1 用于发票校验之后，如果进行发票开具必须手动将 CheckEWM 值置为 0， 否则 Invoice()方法的功能将一直处于发票校验状态
function invoice(checkewm,reqData) {
	var address = getIP();
	var over = "<?xml version=\"1.0\" encoding=\"utf-8\" ?><req><type>Invoice</type><reqData><CheckEWM>"+checkewm+"</CheckEWM>"+reqData+"</reqData></req>";
	over = encodeURI(over);
	var respText = openAjax(address, over);
	var xmloo = get_xml_object(respText);
	var RetCode = $(xmloo).find("RetCode").text();
	if(RetCode=="4011"){
		//开具成功
	}else{
/*		var RetMsg = $(xmloo).find("RetMsg").text();
		if(RetMsg!=undefined && RetMsg!=null && RetMsg!="")
		jQuery.jqalert(RetMsg);
		return null;*/
	}
	return xmloo;
}



//调用防伪开票标准打印程序，打印指定发票；InfoKind –发票种类（0：专用发票 2：普通发票 11：货物运输业增值税专用发票 12：机动车销售统一发票）
//InfoTypeCode – 要打印发票的十位代码
//InfoNumber – 要打印发票的号码
//GoodsListFlag 销货清单标志，0 – 打印发票，1 – 打印清单
//InfoShowPrtDlg是否显示边距确认对话框，0 – 否，1 –是
function printinv(InfoKind,InfoTypeCode,InfoNumber,GoodsListFlag,InfoShowPrtDlg) {
	var address = getIP();
	var over = "<?xml version=\"1.0\" encoding=\"utf-8\" ?><req><type>PrintInv</type><reqData><InfoKind>"+InfoKind+"</InfoKind><InfoTypeCode>"+InfoTypeCode+"</InfoTypeCode><InfoNumber>"+InfoNumber+"</InfoNumber><GoodsListFlag>"+GoodsListFlag+"</GoodsListFlag><InfoShowPrtDlg>"+InfoShowPrtDlg+"</InfoShowPrtDlg></reqData></req>";
	var respText = openAjax(address, over);
	var xmloo = get_xml_object(respText);
	var RetCode = $(xmloo).find("RetCode").text();
	if(RetCode=="5011"){
		//打印成功
	}else if(RetCode=="5012"){
		//未打印
	}else{
		var RetMsg = $(xmloo).find("RetMsg").text();
		if(RetMsg!=undefined && RetMsg!=null && RetMsg!="")
		jQuery.jqalert(RetMsg);
		return null;
	}
	return xmloo;
}



//释放对金税卡的设备占用，释放其它占用的资源
function closecard() {
	var address = getIP();
	var over = "<?xml version=\"1.0\" encoding=\"utf-8\" ?><req><type>CloseCard</type></req>";
	var respText = openAjax(address, over);
	var xmloo = get_xml_object(respText);
	var RetCode = $(xmloo).find("RetCode").text();
	if(RetCode=="9000"){
		//关闭成功成功

	}else{
		var RetMsg = $(xmloo).find("RetMsg").text();
		if(RetMsg!=undefined && RetMsg!=null && RetMsg!="")
		jQuery.jqalert(RetMsg);
		return null;
	}
	return xmloo;
}



/*设置已开具发票的上传模式
0 手工上传
1 自动上传（默认）
设置为自动上传后， 每次开具成功后自动将发票信息上
传到局端数据库， 下一张发票开具前自动更新已上传发
票的本地状态信息。
设置为手工上传时， 需要分别调用上传和下载接口完成
发票报送。*/
function uploadinvoiceauto(scfs) {
	var address = getIP();
	var over = "<?xml version=\"1.0\" encoding=\"utf-8\" ?><req><type>UploadInvoiceAuto</type><reqData><UploadInvoiceAuto>"+scfs+"</UploadInvoiceAuto></reqData></req>";
	var respText = openAjax(address, over);
	var xmloo = get_xml_object(respText);
	var RetCode = $(xmloo).find("RetCode").text();
	if(RetCode=="0"){
		//关闭成功成功

	}else{
		var RetMsg = $(xmloo).find("RetMsg").text();
		if(RetMsg!=undefined && RetMsg!=null && RetMsg!="")
		jQuery.jqalert(RetMsg);
		return null;
	}
	return xmloo;
}



//本项功能在发票开具接口(Invoice)调用成功后调用。若不设置输入参数，则手工将刚刚开具的发票信息上传到局端。若设置输入参数，则将指定的发票数据上传到局端.
function uploadinvoice(fpdm,fphm) {
	var address = getIP();
	var over = "<?xml version=\"1.0\" encoding=\"utf-8\" ?><req><type>UploadInvoice</type><reqData><InfoTypeCode>"+fpdm+"</InfoTypeCode><InfoNumber>"+fphm+"</InfoNumber></reqData></req>";
	var respText = openAjax(address, over);
	var xmloo = get_xml_object(respText);
	var RetCode = $(xmloo).find("RetCode").text();
	if(RetCode=="8000"){
		//上传成功

	}else{
		var RetMsg = $(xmloo).find("RetMsg").text();
		if(RetMsg!=undefined && RetMsg!=null && RetMsg!="")
		jQuery.jqalert(RetMsg);
		return null;
	}
	return xmloo;
}



//本项功能在发票上传接口(UploadInvoice)调用成功后调用。手工更新已成功上传到局端的发票本地状态信息。
function updateinvoicestatus() {
	var address = getIP();
	var over = "<?xml version=\"1.0\" encoding=\"utf-8\" ?><req><type>UpdateInvoiceStatus</type></req>";
	var respText = openAjax(address, over);
	var xmloo = get_xml_object(respText);
	var RetCode = $(xmloo).find("RetCode").text();
	if(RetCode=="8050"){
		//更新成功

	}else{
		var RetMsg = $(xmloo).find("RetMsg").text();
		if(RetMsg!=undefined && RetMsg!=null && RetMsg!="")
		jQuery.jqalert(RetMsg);
		return null;
	}
	return xmloo;
}





//在金税卡及防伪开票数据库中作废已开发票,fplb(0：专用发票；2：普通发票；11：货物运输业增值税专用发票；12：机动车销售统一发票)
function cancelinv(fplb,fpdm,fphm) {
	var address = getIP();
	var over = "<?xml version=\"1.0\" encoding=\"utf-8\" ?><req><type>CancelInv</type><reqData><InfoKind>"+fplb+"</InfoKind><InfoTypeCode>"+fpdm+"</InfoTypeCode><InfoNumber>"+fphm+"</InfoNumber></reqData></req>";
	var respText = openAjax(address, over);
	var xmloo = get_xml_object(respText);
	var RetCode = $(xmloo).find("RetCode").text();
	if(RetCode=="6011"){
		//作废成功

	}else{
		var RetMsg = $(xmloo).find("RetMsg").text();
		if(RetMsg!=undefined && RetMsg!=null && RetMsg!="")
		jQuery.jqalert(RetMsg);
		return null;
	}
	return xmloo;
}


//开启/关闭开票系统手工开票作废功能handmade –开启/关闭选项（0：开启 1：关闭）
function enhand(handmade) {
	var address = getIP();
	var over = "<?xml version=\"1.0\" encoding=\"utf-8\" ?><req><type>EnHand</type><reqData><HandMade>"+fpdm+"</HandMade></reqData></req>";
	var respText = openAjax(address, over);
	var xmloo = get_xml_object(respText);
	var RetCode = $(xmloo).find("RetCode").text();
	if(RetCode=="9000"){
		//操作成功

	}else{
		var RetMsg = $(xmloo).find("RetMsg").text();
		if(RetMsg!=undefined && RetMsg!=null && RetMsg!="")
		jQuery.jqalert(RetMsg);
		return null;
	}
	return xmloo;
}



//查询已开发票信息
function qryinv(xsdh,fplb,fpdm,fphm) {
	var address = getIP();
	var over = "<?xml version=\"1.0\" encoding=\"utf-8\" ?><req><type>QryInv</type><reqData><InfoBillNumber>"+xsdh+"</InfoBillNumber><InfoKind>"+fplb+"</InfoKind><InfoTypeCode>"+fpdm+"</InfoTypeCode><InfoNumber>"+fphm+"</InfoNumber></reqData></req>";
	var respText = openAjax(address, over);
	var xmloo = get_xml_object(respText);
	var RetCode = $(xmloo).find("RetCode").text();
	if(RetCode=="7011"){
		//查询成功成功

	}else{
		var RetMsg = $(xmloo).find("RetMsg").text();
		if(RetMsg!=undefined && RetMsg!=null && RetMsg!="")
		jQuery.jqalert(RetMsg);
		return null;
	}
	return xmloo;
}



