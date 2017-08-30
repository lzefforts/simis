//机构下拉树形结构统一调用方法

var tree;

/*机构树节点实体类*/
function TreeNode(bm,status){
	this.id=bm.id;
	this.bmid=bm.bmid;
	this.bmmc=bm.bmmc;
	this.bmjc=bm.bmjc;
	this.pid=bm.sjbmid;
	this.nsrsbh=bm.nsrsbh;
	this.children=new Array();
}

/*机构树实体类*/
function Tree(bms){
	this.treeNodes = new Array();
	this.root;
	this.bms = bms;
	
	this.divId    = "BmDiv";
	this.treeId   = "BmTree";
	this.mcEl     = "txt_bm_mc";
	this.idEl     = "txt_bm_id";
	this.bmidEl   = "txt_bm_bmid";
	this.nsrsbhEl = "txt_bm_nsrsbh";
	
	
	//造树，深度遍历构造机构树
	this.createTree = function(){
		var stack = new Array();
		var pNode = new TreeNode(this.bms[0]);
		
		this.treeNodes = new Array();
		this.treeNodes.push(pNode);
		stack.push(pNode);
		
		for(var i=1; i < this.bms.length; i=i+1){
			
			var node = new TreeNode(this.bms[i]);
			
			while(true){
				if(this.bms[i].sjbmid == pNode.id){
					stack.push(node);
					pNode.children.push(node);
					pNode = node;
					break;
				} else {
					stack.pop();
					pNode = stack[stack.length-1];
				}
			}
		}
	};
	//画树，深度遍历
	this.drawTree = function(){
		
		
		var setting = {
			view : {
				selectedMulti: false	//单选
			},
			data : {
				key: {
					name: "bmmc"		//将哪一个属性显示
				},
				simpleData : {
					enable : true,		//简单数据，保持Array，不需要转换为嵌套的JSon
					idKey:   "id",		//关键字字段属性名
					pIdKey:  "sjbmid"	//父节点字段属性名
				}
			},
			callback : {
				beforeClick : this.beforeJgClick,
				onClick : this.onJgClick//点击之后调用的赋值方法
			}
		};
		
		$.fn.zTree.init($("#" + this.treeId), setting, this.bms);//调用JQuery的zTree框架初始化树形数据
		
	};
	
	this.beforeJgClick = function(){
		return true;
	};
	
	this.onJgClick = function(){
		var zTree = $.fn.zTree.getZTreeObj(tree.treeId);
		var nodes = zTree.getSelectedNodes();
		
		$("#" + tree.mcEl    ).val(nodes[0].bmmc);
		$("#" + tree.idEl    ).val(nodes[0].id);
		$("#" + tree.bmidEl  ).val(nodes[0].bmid);
		$("#" + tree.nsrsbhEl).val(nodes[0].nsrsbh);
		
		$("#" + tree.divId).fadeOut("fast");
	};
	
	this.initBmShow = function(){
		
		var mcElement     = document.getElementById(tree.mcEl);
		var idElement     = document.getElementById(tree.idEl);
		var bmidElement   = document.getElementById(tree.bmidEl);
		var nsrsbhElement = document.getElementById(tree.nsrsbhEl);
		
		if(mcElement != null && mcElement.value == "")
			mcElement.value = $.trim(tree.treeNodes[0].bmmc);
		
		if(idElement != null && idElement.value == "")
			idElement.value = $.trim(tree.treeNodes[0].id);
		
		if(bmidElement != null && bmidElement.value == "")
			bmidElement.value = $.trim(tree.treeNodes[0].bmid);
		
		if(nsrsbhElement != null && nsrsbhElement.value == "")
			nsrsbhElement.value = $.trim(tree.treeNodes[0].nsrsbh);
	}
}

function getBmData(divId,treeId,mcEl,idEl,bmidEl,nsrsbhEl,cxflag,qxbmid,qxbmmc){

	var treeVal;
	
	tree.divId    =    divId == undefined ? "BmDiv"         : divId;
	tree.treeId   =   treeId == undefined ? "BmTree"        : treeId;
	tree.mcEl     =     mcEl == undefined ? "txt_bm_mc"     : mcEl;
	tree.idEl     =     idEl == undefined ? "txt_bm_id"     : idEl;
	tree.bmidEl   =   bmidEl == undefined ? "txt_bm_bmid"   : bmidEl;
	tree.nsrsbhEl = nsrsbhEl == undefined ? "txt_bm_nsrsbh" : nsrsbhEl;
	
	if(cxflag == "" || (cxflag == "1" && window.top.GlobalBmUpList != null)){
		if(cxflag == ""){
			treeVal = new Tree(window.top.GlobalBmList);
		}
		if(cxflag == "1"){
			treeVal = new Tree(window.top.GlobalBmUpList);
		}
		
		treeVal.createTree();
		var el = document.getElementById(bmmcEl);
		treeVal.drawTree(divId);
		treeVal.allCollapsable();
		treeVal.addEvent(divId,el,idEl,bmidEl,nsrsbhEl);
	} else {
		$.ajax({
			url: "jggl/bmList.action",
			dataType:"json",
			data : { 'cxflag' : cxflag },
			cache:false,
			async: false,
			success:function(BmList){
			 	
				if(cxflag == "1")
					window.top.GlobalBmUpList = BmList;
				
				treeVal = new Tree(BmList);
				treeVal.createTree();
				var el = document.getElementById(bmmcEl);
				treeVal.drawTree(divId);
				showDiv();
			}
		});
	}
	
	tree.initBmShow();
	
	//带出指定的部门名称及部门id
	if(qxbmid != undefined && qxbmid != null)
		$("#" + idEl).value = qxbmid;
	if(qxbmmc != undefined && qxbmmc != null)
		$("#" + bmmcEl).value = qxbmmc;
}


/**
 * 显示部门方框，参数可为空，为空时取默认id
 * 
 * @param divId	部门树的外框div
 * @param treeId 部门树
 * @param mcEl 部门名称输入框
 * @param idEl 部门ID隐藏框
 * @param bmidEl 部门bmid隐藏框
 * @param nsrsbhEl 部门纳税人识别号隐藏框
 * @return
 */
function showBmDiv(divId,treeId,mcEl,idEl,bmidEl,nsrsbhEl){
	
	tree.divId    =    divId == undefined ? "BmDiv"         : divId;
	tree.treeId   =   treeId == undefined ? "BmTree"        : treeId;
	tree.mcEl     =     mcEl == undefined ? "txt_bm_mc"     : mcEl;
	tree.idEl     =     idEl == undefined ? "txt_bm_id"     : idEl;
	tree.bmidEl   =   bmidEl == undefined ? "txt_bm_bmid"   : bmidEl;
	tree.nsrsbhEl = nsrsbhEl == undefined ? "txt_bm_nsrsbh" : nsrsbhEl;
	
	if(window.top.GlobalBmList[0].isInit == true){
		alert("由于机构较多，正在初始化机构数据，请稍后重试！");
		return;
	}
	
	if(tree.bms[0].isInit == true){
		tree.bms = window.top.GlobalBmList;
		tree.createTree();
		tree.initBmShow();
	}
	
	if($("#" + tree.divId).attr("drawed") == undefined){
		tree.drawTree();
		$("#" + tree.divId).attr("drawed","drawed");
	}
	
	showDiv();
	
}

//显示ztree，根据不同输入显示相应ztree
function showDiv(input, menuId) {
	var bmOffset = $("#" + tree.mcEl).offset();
	//设置树显示位置
	$("#" + tree.divId).css({ 
		left: bmOffset.left + "px", 
		top: (bmOffset.top - 30) + "px" 
	}).slideDown("fast");
	$("body").bind("mousedown", onBodyDown);
}
function onBodyDown(event) {
	if (!(event.target.id == tree.mcEl || event.target.id == tree.divId || 
			$(event.target).parents("#"+tree.divId).length > 0)) {
		hideMenu(tree.mcEl, tree.divId);
	}
}
function hideMenu(input, menuId) {
	$("#" + tree.divId).fadeOut("fast");			
	$("body").unbind("mousedown", onBodyDown);
}




$(document).ready(function(){
	
	tree = new Tree(window.top.GlobalBmList);
	
	tree.createTree();
	
	tree.initBmShow();
	
});
