<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%
String report_id = "1";
String tableInfoId = "CAN_BE_TABLE_NAME";
 %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>DataGrid Editer</title>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsp/datagrid/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsp/datagrid/themes/icon.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/jsp/datagrid/css/tables.css" type="text/css"></link>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/datagrid/jquery-1.4.4.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/datagrid/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/datagrid/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/datagrid/json2.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/datagrid/js/log4js-1.0.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/datagrid/js/datagrid-1.0.js"></script>
	
	
	<script>
		
	</script>
</head>
	<body>
	<div class="tabs-container" style="width: auto; height: auto;">
		<table border="1" style="width: 100%; height: auto;">
			<tr>
				<td colspan="2">
					<div style="width:100%;height:25px;">
						工具栏:
						<a class="easyui-linkbutton" icon="icon-button_up" href="javascript:void(0)" onclick="document.forms[0].reset();">调右</a>
						<a class="easyui-linkbutton" icon="icon-button_down" href="javascript:void(0)" onclick="document.forms[0].reset();">调左</a>
						<a class="easyui-linkbutton" icon="icon-button_left" href="javascript:void(0)" onclick="document.forms[0].reset();">调上</a>
						<a class="easyui-linkbutton" icon="icon-button_right" href="javascript:void(0)" onclick="document.forms[0].reset();">调下</a>
						<a class="easyui-linkbutton" icon="icon-add" href="javascript:void(0)" onclick="document.forms[0].reset();">添加</a>
						<a class="easyui-linkbutton" icon="icon-no" href="javascript:void(0)" onclick="document.forms[0].reset();">删除</a>
					</div>
				</td>
			</tr>
			<tr>
				<td colspan="1" style="width:80%;height:auto;">
					<div >
						操作区:
					</div>
					<div id="table_container" style="width:100%;height:auto;">
						
					</div>
				</td>
				<td colspan="1" style="width:100px;height:auto;">
					<div >
						属性栏:
					</div>
				</td>
			</tr>
		</table>
	</div>
	</body>
</html>