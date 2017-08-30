<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div id="form_id"></div>
<script type="text/javascript">
var  common ={
		
		  excelFormSub:function(sqlName,exportType,exportTime,baseParam){
			  var t = "<form id='formId' target='_blank' method='post' action=''>";
			  for(var key in baseParam){
				  t += "<input type='hidden' name='" + key + "' value='" + baseParam[key] + "'/>";
			  }
			  t += "</form>";
			  document.getElementById("form_id").innerHTML = t;
			  document.getElementById("formId").action ="manage/export.htm?sqlName="+sqlName+"&exportType="+exportType+"&exportTime="+exportTime;

			  document.getElementById("formId").submit();
		  }
}
   
</script>

