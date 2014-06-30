<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%--
	描述：session管理
	作者：谭湘
	版本：1.0
	日期：2014-06-04
	 --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Session管理</title>
<%@ include file="/include/css.jsp"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/include/dialog/lhgdialog.js"></script>
<script language="javascript" type="text/javascript"
src="${pageContext.request.contextPath}/include/datepicker/My97DatePicker/WdatePicker.js?lang=zh_CN"></script>

<style type="text/css">
<!--
  #rightContentDiv{
	position:absolute;
	margin-left:220px;
	margin-right:8px;
	width: 80.5%;	   
  }
  
  #leftContentDiv{
	width:200px;
	position:absolute;
	left:0px;
	padding-left:20px;  	   
  }

-->
</style>

<script type="text/javascript">

$(document).ready(function() {
	bboss.pager.pagerevent = {   
			                            beforeload:null,   
			                          afterload:function(opt){ 
			                             getTreeDate();   
	}};   

	$("#wait").hide();
       		
	queryList();   
	
	$('#delBatchButton').click(function() {
		delSessions();
    });
	
	$('#delAllButton').click(function() {
		delAllSessions();
    });
	
});
       
//加载实时任务列表数据  
function queryList(){
	
	var appkey = $("#app_key").val();
	var sessionid = $("#sessionid").val();
	var createtime_start = $("#createtime_start").val();
	var createtime_end = $("#createtime_end").val();
	var referip = $("#referip").val();
	var host = $("#host").val();
	var validate = $("#validate").val();
	
	if (!$.trim(appkey)==''){
		$("#titileSpan").text("("+appkey+")Session列表");
	}
	
	
    $("#sessionContainer").load("<%=request.getContextPath()%>/session/sessionManager/querySessionData.page #customContent", 
    	{"appkey":appkey,"sessionid":sessionid,"createtime_start":createtime_start,"createtime_end":createtime_end,
    	"referip":referip,"host":host,"validate":validate},
    	function(){loadjs();});
    
    getTreeDate();
}

function doreset(){
	$("#reset").click();
}

var treeData = null;

function getTreeDate(){
	$.ajax({
 	 	type: "POST",
		url : "<%=request.getContextPath()%>/session/sessionManager/getAppSessionData.page",
		dataType : 'json',
		async:false,
		beforeSend: function(XMLHttpRequest){
			 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
			},
		success : function(data){
			if (data) {
				treeData = data;
			} else {
				
			}
		}	
	 });
	
	initTreeModule('');
}

function initTreeModule(app_query){
	var treeModuleHtml = "";
	if(treeData){
		var seq = 1;
		for(var i=0; i<treeData.length; i++){
			if(app_query!=null && app_query!=""){
				if(treeData[i].appkey.toLowerCase().indexOf(app_query.toLowerCase()) >= 0 ){
					treeModuleHtml += 
    					"<li id=\""+treeData[i].appkey+"\"><a href=\"#\" onclick=\"doClickTreeNode('"+treeData[i].appkey+"',this)\" >"+treeData[i].appkey+" ("+treeData[i].sessions+")</a></li>";
    					seq++;	
				}
			}else{
				
				treeModuleHtml += 
					"<li id=\""+treeData[i].appkey+"\"><a href=\"#\" onclick=\"doClickTreeNode('"+treeData[i].appkey+"',this)\" >"+treeData[i].appkey+" ("+treeData[i].sessions+")</a></li>";
					seq++;	
			}
		}
	}
	
	$("#app_tree_module").html(treeModuleHtml);
	if($("#app_key").val()!=""){
		if($("#"+$("#app_key").val()).length > 0){
			$("#"+$("#app_key").val()).attr("class","select_links");
		}
	}
}

function sortAppTree(){
	var app_query = $("#app_query").val();
	initTreeModule(app_query);
}

function doClickTreeNode(app_id,selectedNode){
	
	$("#app_tree_module").find("li").removeAttr("class");
	$("#"+app_id).attr("class","select_links");
	
	$("#app_key").val(app_id);
   	
	$("#app_query_th").html("&nbsp;");
   	$("#wf_app_name_td").html("&nbsp;");

	queryList();
}

function sessionInfo(sessionid){
	
	var url="<%=request.getContextPath()%>/session/sessionManager/viewSessionInfo.page?"+
			"sessionid="+sessionid+"&appkey="+$("#app_key").val();
	$.dialog({ title:'明细查看-'+$("#app_key").val(),width:1100,height:620, content:'url:'+url});
	
}

function delSession (sessionid) {
    
    $.dialog.confirm('确定要删除吗？', function(){
     	$.ajax({
	 	type: "POST",
	 	url : "<%=request.getContextPath()%>/session/sessionManager/delSessions.page",
	 	data :{"sessionids":sessionid,"appkey":$("#app_key").val()},
		dataType : 'json',
		async:false,
		beforeSend: function(XMLHttpRequest){
			 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
			},
		success : function(data){
			if (data != 'success') {
				 $.dialog.alert("删除session出错："+data);
			}else {
				queryList();
				close();	
			}
		}	
	 });
	},function(){
	  		
	});         
    
}

function delSessions () {
	var ids="";
	
	$("#tb tr:gt(0)").each(function() {
		if ($(this).find("#CK").get(0).checked == true) {
             ids=ids+$(this).find("#CK").val()+",";
        }
    });
	
    if(ids==""){
       $.dialog.alert('请选择需要删除的session！');
       return false;
    }
    
    delSession(ids);
}

function delAllSessions () {
	if($("#app_key").val() == '')
	{
		  $.dialog.alert('请选择左边的应用,然后再清除应用会话信息!');
		return;
	}
	$.dialog.confirm('确定要清空'+$("#app_key").val()+'应用下所有的session吗？', function(){
     	$.ajax({
	 	type: "POST",
	 	url : "<%=request.getContextPath()%>/session/sessionManager/delAllSessions.page",
	 	data :{"appkey":$("#app_key").val()},
		dataType : 'json',
		async:false,
		beforeSend: function(XMLHttpRequest){
			 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
		},
		success : function(data){
			if (data != 'success') {
				 $.dialog.alert("清空"+$("#app_key").val()+"应用下所有session出错："+data);
			}else {
				queryList();
				close();	
			}
		}	
	 });
	},function(){
	  		
	});         
}

</script>
</head>

<body>
<div class="mcontent" style="width:98%;margin:0 auto;overflow:auto;">
	


	<div id="leftContentDiv">
			    
		<div class="left_menu" style="width:193px;">
		    <ul>
		    	<li class="select_links">
		    		<a href="#">应用查询：</a><input type="input" style="width:100px;" name="app_query" id="app_query" onKeyUp="sortAppTree()" />
		    		<ul style="display: block;" id="app_tree_module">
		    			
		    		</ul>
		    	</li>
		    </ul>
		</div>
		
	</div>
		
	<div id="rightContentDiv">
			
		<div id="searchblock">
			
			<div class="search_top">
				<div class="right_top"></div>
				<div class="left_top"></div>
			</div>
					
			<div class="search_box">
				<input type="hidden" id="app_key" value=""/>
			
				<form id="queryForm" name="queryForm">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td class="left_box"></td>
							<td>
								<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table2">
									<tr>
										<th>SessionID：</th>
										<td><input id="sessionid" name="sessionid" type="text" class="w120"/></td>
										<th>客户端：</th>
										<td><input id="referip" name="referip" type="text" class="w120"/></td>
										<th>创建时间：</th>
										<td><input id="createtime_start" name="createtime_start" type="text"
											 onclick="new WdatePicker({dateFmt:'yyyy/MM/dd HH:mm:ss'})" class="w120" />
											 ~<input id="createtime_end" name="createtime_end" type="text"
											 onclick="new WdatePicker({dateFmt:'yyyy/MM/dd HH:mm:ss'})" class="w120" />
										</td>
										<td rowspan="2" style="text-align:right">
											<a href="javascript:void(0)" class="bt_1" id="queryButton" onclick="queryList()"><span>查询</span></a>
											<a href="javascript:void(0)" class="bt_2" id="resetButton" onclick="doreset()"><span>重置</span></a>
											<input type="reset" id="reset" style="display:none"/>
										</td>
									</tr>
									<tr>
										<th>状态：</th>
										<td>
											<select id="validate" name="validate" class="select1" style="width: 125px;">
												    <option value="" selected>全部</option>
													<option value="1">有效</option>
													<option value="0">无效</option>
											</select>
										</td>
										<th>服务端：</th>
										<td><input id="host" name="host" type="text" class="w120"/></td>
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
				<a href="#" class="bt_small" id="delAllButton"><span>清空应用下Session</span></a>
				<a href="#" class="bt_small" id="delBatchButton"><span>批量删除</span></a>
			</div>
					
			<strong><span id="titileSpan">Session列表</span></strong>
			<img id="wait" src="<%=request.getContextPath()%>/images/wait.gif" />				
		</div>
			
		<div id="sessionContainer" style="overflow:auto"></div>
	</div>
</div>
</body>
</html>
