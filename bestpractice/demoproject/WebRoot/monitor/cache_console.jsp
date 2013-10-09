<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<%--
	
	 --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<!-- <title>操作日志详细</title>-->
<%@ include file="/common/jsp/css-lhgdialog.jsp"%>
<script type="text/javascript">
</script>
</head>
<body>
	<div id="customContent">
	    <sany:menupath />
		<div class="title_box">
			<strong>系统缓存管理
			<dict:user userId="1" attribute="userName"/>
<dict:user userId="1" />
<dict:user userAccount="admin" attribute="userName" />
<dict:user userAccount="admin"  /></strong>
			<a  href="javascript:void(0)" class="bt_1 sp"
											id="queryButton" onclick="clearAll()"><span>全部清除</span> </a> 
		</div>
		<div id="changeColor" style="width: 1150px; overflow: auto;">
			<table width="100%" border="0" cellpadding="0" cellspacing="0"
				class="stable" id="tb">
				
				<tr>
				<th width="20%">缓存类别</th>
				<th width="80%">操作</th>
				</tr>
				<tr>
				<td>用户缓存</td>
				<td >
				<a  href="javascript:void(0)" class="bt_1 sp"
											id="queryButton" onclick="clearUser()"><span>清除</span> </a> 
				</td>
				</tr>
				<tr>
				<td>机构缓存</td>
				<td >
				<a  href="javascript:void(0)" class="bt_1 sp"
											id="queryButton" onclick="clearOrg()"><span>清除</span> </a> 
				</td>
				</tr>
				<tr>
				<td>部门管理员缓存</td>
				<td >
				<a  href="javascript:void(0)" class="bt_1 sp"
											id="queryButton" onclick="clearOrgAdminCache()"><span>清除</span> </a> 
				</td>
				</tr>
				<tr>
				<td>字典缓存</td>
				<td >
				<a  href="javascript:void(0)" class="bt_1 sp"
											id="queryButton" onclick="clearDict()"><span>清除</span> </a> 
				</td>
				</tr>
				<tr>
				<td>权限缓存</td>
				<td >
				<a  href="javascript:void(0)" class="bt_1 sp"
											id="queryButton" onclick="clearPermission()"><span>清除</span> </a> 
				</td>
				</tr>
				<tr>
				<td>角色缓存</td>
				<td >
				<a  href="javascript:void(0)" class="bt_1 sp"
											id="queryButton" onclick="clearRoleCache()"><span>清除</span> </a> 
				</td>
				</tr>
				<tr>
				<td>用户组缓存</td>
				<td >
				<a  href="javascript:void(0)" class="bt_1 sp"
											id="queryButton" onclick="clearGroupCache()"><span>清除</span> </a> 
				</td>
				</tr>
				<tr>
				<td>CMS站点和频道缓存</td>
				<td >
				<a  href="javascript:void(0)" class="bt_1 sp"
											id="queryButton" onclick="clearCMSSite2ndChannelCache()"><span>清除</span> </a> 
				</td>
				</tr>
				<tr>
				<td>CMS发布缓存</td>
				<td >
				<a  href="javascript:void(0)" class="bt_1 sp"
											id="queryButton" onclick="clearCMSPublishCache()"><span>清除</span> </a> 
				</td>
				</tr>
					<tr>
				<td>数据库元数据缓存</td>
				<td >
				<a  href="javascript:void(0)" class="bt_1 sp"
											id="queryButton" onclick="clearDBCache()"><span>清除</span> </a> 
				</td>
				</tr>
				<tr>
				<td>ParamsHandler数据缓存</td>
				<td >
				<a  href="javascript:void(0)" class="bt_1 sp"
											id="queryButton" onclick="clearParamsHandlerCache()"><span>清除</span> </a> 
				</td>
				</tr>
			</table>
			
		</div>
	</div>
	<div id = "custombackContainer"></div>
</body>
</html>

<script language="javascript">
var api = frameElement.api;

function clearCMSPublishCache()
{
	$.dialog.confirm("是否CMS站点发布缓存",function()//确定按钮回调函数
	{
		$.ajax({
			   type: "POST",
				url : "clearCMSPublishCache.page",
				data :{},
				dataType : 'json',
				async:false,
				beforeSend: function(XMLHttpRequest){
						blockUI();	
				      	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
					},
				success : function(responseText){
					//去掉遮罩	
					unblockUI();
					if(responseText=="success"){
						$.dialog.alert("CMS站点发布缓存完成.",function(){},api);
						
					}else{
						$.dialog.alert(responseText,function(){},api);
					}
				}
			  });
		
	},
	function()//取消按钮回调函数
	{
			
	} ,
	api);
	
		
		
}
function clearAll()
{
	$.dialog.confirm("是否清除所有缓存",function()//确定按钮回调函数
	{
		$.ajax({
			type: "POST",
			url : "clearAll.page",
			data :{},
			dataType : 'json',
			async:false,
			beforeSend: function(XMLHttpRequest){
					blockUI();	
			      	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
				},
			success : function(responseText){
				//去掉遮罩	
				unblockUI();
				if(responseText=="success"){
					$.dialog.alert("清除所有缓存完成.",function(){},api);
					
				}else{
					$.dialog.alert(responseText,function(){},api);
				}
			}
		  });
		
	},
	function()//取消按钮回调函数
	{
			
	} ,
	api);
	
		
		
}

function clearOrg()
{
	$.dialog.confirm("是否清除机构缓存",function()//确定按钮回调函数
	{
		$.ajax({
			type: "POST",
			url : "clearOrg.page",
			data :{},
			dataType : 'json',
			async:false,
			beforeSend: function(XMLHttpRequest){
					blockUI();	
			      	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
				},
			success : function(responseText){
				//去掉遮罩	
				unblockUI();
				if(responseText=="success"){
					$.dialog.alert("清除机构完成.",function(){},api);
					
				}else{
					$.dialog.alert(responseText,function(){},api);
				}
			}
		  });
		
	},
	function()//取消按钮回调函数
	{
			
	} ,
	api);
	
		
		
}

function clearUser()
{
	$.dialog.confirm("是否清除用户缓存",function()//确定按钮回调函数
	{
		$.ajax({
			type: "POST",
			url : "clearUserCache.page",
			data :{},
			dataType : 'json',
			async:false,
			beforeSend: function(XMLHttpRequest){
					blockUI();	
			      	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
				},
			success : function(responseText){
				//去掉遮罩	
				unblockUI();
				if(responseText=="success"){
					$.dialog.alert("清除用户缓冲完成.",function(){},api);
					
				}else{
					$.dialog.alert(responseText,function(){},api);
				}
			}
		  });
		
	},
	function()//取消按钮回调函数
	{
			
	} ,
	api);
	
		
		
}
function clearOrgAdminCache()
{
	$.dialog.confirm("是否清除机构管理员缓存",function()//确定按钮回调函数
	{
		$.ajax({
			type: "POST",
			url : "clearOrgAdminCache.page",
			data :{},
			dataType : 'json',
			async:false,
			beforeSend: function(XMLHttpRequest){
					blockUI();	
			      	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
				},
			success : function(responseText){
				//去掉遮罩	
				unblockUI();
				if(responseText=="success"){
					$.dialog.alert("清除机构管理员缓存完成.",function(){},api);
					
				}else{
					$.dialog.alert(responseText,function(){},api);
				}
			}
		  });
		
	},
	function()//取消按钮回调函数
	{
			
	} ,
	api);
	
		
		
}
function clearDict()
{
	$.dialog.confirm("是否清除字典缓存",function()//确定按钮回调函数
	{
		$.ajax({
			type: "POST",
			url : "clearDict.page",
			data :{},
			dataType : 'json',
			async:false,
			beforeSend: function(XMLHttpRequest){
					blockUI();	
			      	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
				},
			success : function(responseText){
				//去掉遮罩	
				unblockUI();
				if(responseText=="success"){
					$.dialog.alert("清除字典缓存完成.",function(){},api);
					
				}else{
					$.dialog.alert(responseText,function(){},api);
				}
			}
		  });
		
	},
	function()//取消按钮回调函数
	{
			
	} ,
	api);
	
		
		
}
function clearPermission()
{
	$.dialog.confirm("是否清除权限缓存",function()//确定按钮回调函数
	{
		$.ajax({
			type: "POST",
			url : "clearPermission.page",
			data :{},
			dataType : 'json',
			async:false,
			beforeSend: function(XMLHttpRequest){
					blockUI();	
			      	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
				},
			success : function(responseText){
				//去掉遮罩	
				unblockUI();
				if(responseText=="success"){
					$.dialog.alert("清除权限缓存完成.",function(){},api);
					
				}else{
					$.dialog.alert(responseText,function(){},api);
				}
			}
		  });
		
	},
	function()//取消按钮回调函数
	{
			
	} ,
	api);
	
		
		
}
function clearRoleCache()
{
	$.dialog.confirm("是否清除角色缓存",function()//确定按钮回调函数
	{
		$.ajax({
			type: "POST",
			url : "clearRoleCache.page",
			data :{},
			dataType : 'json',
			async:false,
			beforeSend: function(XMLHttpRequest){
					blockUI();	
			      	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
				},
			success : function(responseText){
				//去掉遮罩	
				unblockUI();
				if(responseText=="success"){
					$.dialog.alert("清除角色缓存完成.",function(){},api);
					
				}else{
					$.dialog.alert(responseText,function(){},api);
				}
			}
		  });
		
	},
	function()//取消按钮回调函数
	{
			
	} ,
	api);
	
		
		
}
function clearGroupCache()
{
	$.dialog.confirm("是否清除用户组缓存",function()//确定按钮回调函数
	{
		$.ajax({
			type: "POST",
			url : "clearGroupCache.page",
			data :{},
			dataType : 'json',
			async:false,
			beforeSend: function(XMLHttpRequest){
					blockUI();	
			      	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
				},
			success : function(responseText){
				//去掉遮罩	
				unblockUI();
				if(responseText=="success"){
					$.dialog.alert("清除用户组缓存完成.",function(){},api);
					
				}else{
					$.dialog.alert(responseText,function(){},api);
				}
			}
		  });
		
	},
	function()//取消按钮回调函数
	{
			
	} ,
	api);
	
		
		
}
function clearCMSSite2ndChannelCache()
{
	$.dialog.confirm("是否清除站点和频道缓存",function()//确定按钮回调函数
	{
		$.ajax({
			type: "POST",
			url : "clearCMSSite2ndChannelCache.page",
			data :{},
			dataType : 'json',
			async:false,
			beforeSend: function(XMLHttpRequest){
					blockUI();	
			      	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
				},
			success : function(responseText){
				//去掉遮罩	
				unblockUI();
				if(responseText=="success"){
					$.dialog.alert("清除站点和频道缓存完成.",function(){},api);
					
				}else{
					$.dialog.alert(responseText,function(){},api);
				}
			}
		  });
		
	},
	function()//取消按钮回调函数
	{
			
	} ,
	api);
	
		
		
}
function clearDBCache()
{
	$.dialog.confirm("是否清除数据库元数据缓存",function()//确定按钮回调函数
	{
		$.ajax({
			type: "POST",
			url : "clearDBMetaCache.page",
			data :{},
			dataType : 'json',
			async:false,
			beforeSend: function(XMLHttpRequest){
					blockUI();	
			      	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
				},
			success : function(responseText){
				//去掉遮罩	
				unblockUI();
				if(responseText=="success"){
					$.dialog.alert("清除数据库元数据缓存完成.",function(){},api);
					
				}else{
					$.dialog.alert(responseText,function(){},api);
				}
			}
		  });
		
	},
	function()//取消按钮回调函数
	{
			
	} ,
	api);
	
		
		
}

function clearParamsHandlerCache()
{
	$.dialog.confirm("是否清除ParamsHandler数据缓存",function()//确定按钮回调函数
	{
		$.ajax({
			type: "POST",
			url : "clearParamsHandlerCache.page",
			data :{},
			dataType : 'json',
			async:false,
			beforeSend: function(XMLHttpRequest){
					blockUI();	
			      	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
				},
			success : function(responseText){
				//去掉遮罩	
				unblockUI();
				if(responseText=="success"){
					$.dialog.alert("清除ParamsHandler数据缓存完成.",function(){},api);
					
				}else{
					$.dialog.alert(responseText,function(){},api);
				}
			}
		  });
		
	},
	function()//取消按钮回调函数
	{
			
	} ,
	api);
	
		
		
}


</script>