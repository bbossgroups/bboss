<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>


<%--
	描述：台账主页面
	作者：许石玉
	版本：1.0
	日期：2012-02-08
	 --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><pg:message code="sany.appbom.bomManage"/></title>
<%@ include file="/common/jsp/css.jsp"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/html2/js/common.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false&skin=sany"></script>
<script language="javascript">
				function senior()
				{
				    var senior=document.getElementById("senior")
				    var check=document.getElementById("check")
					senior.style.display="none"
				    if(check.checked)
				    {
				       senior.style.display="block";
				    }
				    else if(check.unchecked)
				    {
				       senior.style.display="none";
				    }
				}
			</script>
</head>
	<body>
		<div class="mcontent">
			<sany:menupath />
			<div id="searchblock">
				<div class="search_top">
					<div class="right_top"></div>
					<div class="left_top"></div>
				</div>
				<div class="search_box">
					<form id="queryForm" name="queryForm">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td class="left_box"></td>
								<td>
									<table width="100%" border="0" cellpadding="0" cellspacing="0"
										class="table2">
										<tr>
											<th><pg:message code="sany.appbom.bm"/>：</th>
											<td><input id="bm_query" name="bm" type="text"
													value="" class="w120"/></td>
											<th>
												<pg:message code="sany.appbom.softInitial"/>：
											</th>
											<td>
												<input id="app_name_en_query" name="app_name_en" type="text"
													value="" class="w120" />
											</td>
											<th>
												<pg:message code="sany.appbom.softName"/>：
											</th>
											<td>
												<input id="app_name_query" name="app_name" type="text"
													value="" class="w120" />
											</td>
										</tr>
										<tr>
											<th>
												<pg:message code="sany.appbom.level"/>：
											</th>
											<td >
												<select id="soft_level_query" name="soft_level"
													class="select1" style="width:125px;">
													<option value=""></option>
													<option value="1">
														一级
													</option>
													<option value="2">
														二级
													</option>
													<option value="3">
														三级
													</option>
													<option value="10">
														待定
													</option>
												</select>
											</td>
									
											<th>
												<pg:message code="sany.appbom.state"/>：
											</th>
											<td>
												<select id="state_query" name="state" class="select1" style="width:125px;">
													<option value=""></option>
													<option value="1">
														在用
													</option>
													<option value="2">
														试运行
													</option>
													<option value="3">
														升级研发中
													</option>
													<option value="4">
														停用
													</option>
													<option value="5">
														暂用
													</option>
													<option value="6">
														研发中
													</option>
													<option value="7">
														试验中
													</option>
												</select>
											</td>
											<th>
												<pg:message code="sany.appbom.rdType"/>：
											</th>
											<td>
												<select id="rd_type_query" name="rd_type"
													style="width: 125px;">
													<option value=""></option>
													<option value="1">
														自研
													</option>
													<option value="2">
														外购
													</option>
													<option value="3">
														免费
													</option>
													<option value="4">
														试用
													</option>
													<option value="5">
														外购+定制
													</option>
												</select>
											</td>
											<th>
												&nbsp;
											</th>
											<td>
												<a href="javascript:void" class="bt_1" id="queryButton"><span><pg:message code="sany.pdp.common.operation.search"/></span>
												</a>
												<a href="javascript:void(0)" class="bt_2" id="resetButton" onclick="doreset()"><span><pg:message code="sany.pdp.common.operation.reset"/></span>
												</a>
												<input type="reset" id="reset" style="display:none"/>
											</td>
										</tr>
									</table>
								</td>
								<td class="right_box"></td>
							</tr>
						</table>
					</form>
				</div>
				<div class="search_bottom">
					<div class="right_bottom"></div>
					<div class="left_bottom"></div>
				</div>
			</div>
			<div class="title_box">
				<div class="rightbtn">
				<a href="javascript:void" class="bt_small" id="addButton"><span><pg:message code="sany.pdp.common.operation.new"/></span></a>
				<a href="javascript:void" class="bt_small" id="delBatchButton"><span><pg:message code="sany.pdp.common.operation.batch"/> <pg:message code="sany.pdp.common.operation.delete"/></span></a>
				<a href="javascript:void" class="bt_small" id="exportButton"><span><pg:message code="sany.pdp.common.operation.export"/></span></a>
				<input id="excelType" type="radio" name="excelType"   checked  >2003</input>
				<input  type="radio" name="excelType"  >2007</input>
				</div>
				
				<strong><pg:message code="sany.appbom.appBom"/> <pg:message code="sany.pdp.common.data.list"/></strong>
				<img id="wait" src="../common/images/wait.gif" />				
			</div>
			<div id="custombackContainer" style="overflow:auto">
			
			</div>
		</div>
	</body>
</html>
<script type="text/javascript">

<!--

$(document).ready(function() {
	bboss.pager.pagerevent = {   
			                            beforeload:function(opt){   
			                                alert("beforeload containerid:"+opt.containerid);   
			                            },   
			                          afterload:function(opt){   
			                              alert("afterload containerid:"+opt.containerid);   
	}};   

	 $("#wait").hide();
	 queryList();

  $("#CKA").click(function() {
        $("#tb tr:gt(1)").each(function() {
            $(this).find("#CK").get(0).checked = $("#CKA").get(0).checked;
        });
    });
    $('#queryButton').click(function() {
    	queryData();
    });
    $('#delBatchButton').click(function() {
    	delBatch();
    });
    $('#exportButton').click(function() {
    	exportExcel();
    });           
    
    $('#addButton').click(function() {  
  	 var url="<%=request.getContextPath()%>/appbom/appPre.page";
	 	 $.dialog({ id:'iframeNewId', title:'<pg:message code="sany.pdp.common.operation.new"/>&nbsp;<pg:message code="sany.appbom.appBom"/>',width:740,height:560, content:'url:'+url});   			 		  
    });            
});

function queryList(){	
	$("#custombackContainer").load("queryListAppBom.page #customContent",function(){})
}
function exportExcel(){	 
	var bm=$("#bm_query").val();
	var app_name_en=$("#app_name_en_query").val();
	var app_name=$("#app_name_query").val();
	var soft_level=$("#soft_level_query").val();
	var state=$("#state_query").val();
	var rd_type=$("#rd_type_query").val();
	var excelType="";
	if($("#excelType").attr("checked")){
		excelType=0;
	}
	else{
		excelType=1;
	} 	
	if(containSpecial(bm)|| containSpecial(app_name_en)||containSpecial(app_name)){
		alert("查询字符串含有非法字符集,请检查输入条件");
		return;
	}

	 window.location.href="exportExcel.page?bm="+encodeURI(encodeURI(bm))+"&app_name="+encodeURI(encodeURI(app_name))+"&app_name_en="+encodeURI(encodeURI(app_name_en))
		+"&soft_level="+soft_level+"&state="+state+"&rd_type="+rd_type+"&excelType="+excelType;
	 return;

}	
function containSpecial( s ) {  
	  var containSpecial = RegExp(/[(\ )(\~)(\!)(\@)(\#)(\$)(\%)(\^)(\&)(\+)(\=)(\[)(\])(\{)(\})(\|)(\\)(\;)(\:)(\')(\")(\,)(\/)(\<)(\>)(\?)(\)]+/);      
   return ( containSpecial.test(s) );      
}

//查找
function queryData()
{	var bm=$("#bm_query").val();
	var app_name_en=$("#app_name_en_query").val();
	var app_name=$("#app_name_query").val();
	var soft_level=$("#soft_level_query").val();
	var state=$("#state_query").val();
	var rd_type=$("#rd_type_query").val();
	if(containSpecial(bm)|| containSpecial(app_name_en)||containSpecial(app_name)){
		$.dialog.alert('查询字符串含有非法字符集,请检查输入条件！');
		return;
	}
	$("#custombackContainer").load("queryListAppBom.page #customContent",
	{bm:bm,app_name_en: app_name_en,app_name:app_name,soft_level:soft_level,
	state:state,rd_type:rd_type},function(){loadjs()});


}

function modifyQueryData()
{	
	$("#custombackContainer").load("queryListAppBom.page?"+$("#querystring").val()+" #customContent");
}
function editBom(id){
	var url="<%=request.getContextPath()%>/appbom/updatePre.page?id="+id;
	$.dialog({ title:'修改台账信息',width:740,height:560, content:'url:'+url,lock: true}); 		
}
function viewBom(id){
	var url="<%=request.getContextPath()%>/appbom/viewBom.page?id="+id;		
	$.dialog({ title:'查看台账信息',width:740,height:560, content:'url:'+url,lock: true}); 	
}
function doreset(){
	$("#reset").click();
}
function delBom(id){		
	$.dialog.confirm('<pg:message code="sany.pdp.common.operation.remove.confirm"/>', function(){ 
		 $.ajax({
	 	 type: "POST",
		url : "<%=request.getContextPath()%>/appbom/delete.page",
		data :{"id":id},
		dataType : 'json',
		async:false,
		beforeSend: function(XMLHttpRequest){
			 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
			},
		success : function(data){
	 		modifyQueryData();
	 		//alert("成功删除记录");
		}	
	  });
	}, function(){ 
		 
	});
		
	  
}
function delBatch(){
	var ids="";
	 $("#tb tr:gt(0)").each(function() {
	 
            if ($(this).find("#CK").get(0).checked == true) {
                ids=ids+$(this).find("#id").val()+",";
              	}
       });
       if(ids==""){
        $.dialog.alert('请选择需要删除的记录！');
        return false;
       }
     $.dialog.confirm('<pg:message code="sany.pdp.common.operation.remove.confirm"/>', function(){
     	$.ajax({
		 	 	type: "POST",
				url : "<%=request.getContextPath()%>/appbom/deletebatch.page",
				data :{"ids":ids},
				dataType : 'json',
				async:false,
				beforeSend: function(XMLHttpRequest){
					 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
					},
				success : function(data){
			 		modifyQueryData();
			 		//alert("成功删除记录");
				}	
			 });
      },function(){
      		
      });         
}

//-->
</script>
