<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>
<%@ page contentType="text/html; charset=GBK"%>
<%
String templet_id = "1";
String report_id = "1";
String tableInfoId = "CAN_BE_TABLE_NAME";
 %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=GBK">
	<title>DataGrid Editer</title>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsp/datagrid/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsp/datagrid/themes/icon.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/classic/tables.css" type="text/css"></link>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/datagrid/jquery-1.4.4.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/datagrid/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/datagrid/json2.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/datagrid/js/datagrid-1.0.js"></script>
	
	<script>
		
		var products = [
		    {productid:'FI-SW-01',name:'Koi'},
		    {productid:'K9-DL-01',name:'Dalmation'},
		    {productid:'RP-SN-01',name:'Rattlesnake'},
		    {productid:'RP-LI-02',name:'Iguana'},
		    {productid:'FL-DSH-01',name:'Manx'},
		    {productid:'FL-DLH-02',name:'Persian'},
		    {productid:'AV-CB-01',name:'Amazon Parrot'}
		];
		/*产品格式器*/
		function formatter_product(value, rowData, rowIndex) {
			for(var i=0; i<products.length; i++){
				if (products[i].productid == value) return products[i].name;
			}
			return value;
		}
		/*成本格式器*/
		function formatter_unitcost(value, rowData, rowIndex) {
			if (null == value || "" == value) {
				//alert("对不起！成本不能为空！");
				return 0.0;
			}
			
			return value;
		}
		
		/*校验器*/
		function validator(rowIndex, rowData) {
			//校验成本
			if(null == rowData["unitcost"] || "" == rowData["unitcost"]){
				$.messager.alert('错误信息','对不起！成本不能为空！','error');
				//alert("对不起！成本不能为空！");
				//定位重新修改
				$('#tt').datagrid('beginEdit', rowIndex);
				return false;
			}
		}
		
		var editer_params = {	
			templet_id:'<%=templet_id%>',		
			report_id:'<%=report_id%>',
			tableInfoId:'<%=tableInfoId%>'
			
		};
		/*初始化对象*/
		var editer1 = new DataGrid('#tt',editer_params);
		
		/*初始化方法*/
		$(function(){
			//初始化tabs
			$('#tabs').tabs({
				tools:[{
					iconCls:'icon-add',
					handler: function(){
						$.messager.alert('提示信息','可以动态添加您想要编辑的表','info');
					}
				},{
					iconCls:'icon-save',
					handler: function(){
						$.messager.alert('提示信息','可以批量保存您修改了的表','info');	
					}
				}]
			});
					
			//初始化datagrid，一定要注意这里
			var editer = editer1;
			<!-- datagrid start -->
			$('#tt').datagrid({
				toolbar:[{
					text:'添加',
					iconCls:'icon-add',
					handler:function(){
						editer.appendRow({
							REPORT_ID:editer.params.report_id,
							itemid:'EST-',
							productid:'',
							listprice:'',
							unitcost:'',
							attr1:'',
							status:'P'
						});
					}
				},'-',{
					text:'删除',
					iconCls:'icon-remove',
					handler:function(){
						editer.deleteRow();
					}
				},'-',{
					text:'撤销',
					iconCls:'icon-undo',
					handler:function(){
						editer.rejectChanges();
					}
				}
				,'-',{
					text:'保存',
					iconCls:'icon-save',
					handler:function(){
						editer.saveData();
					}
				}],
				fitColumns:true,
				loadMsg:'正在加载数据，请稍后...',
				onBeforeLoad:function(){
					editer.rejectChanges();
				},
				onClickRow:function(rowIndex){
					editer.onClickRow(rowIndex);
				},				
				onAfterEdit:function(rowIndex, rowData, changes){
					//validator(rowIndex, rowData);					
				}				
				<!-- datagrid end -->
			});
			
			//初始化数据
			initSelect("productid", products);
			//加载数据
			doQuery();
		});
		
		/*加载数据*/
		function doQuery(){			
			inputs = $("#form1 input,#form1 select");	
			//支持两种方式调用
			//editer1.loadData("itemid=i&a=33");
			editer1.loadData(inputs);
		}
		
		/*初始化数据*/
		function initSelect(selectId, datas) {
			var id = "#"+selectId;
			var selectObj = $(id);
			//alert("data="+jQuery.data(products[0],"productid"));
			//先添加一个空行，以便查询出全部数据
			$(id).append("<option ></option>"); 
			for (var i=0; i<products.length; i++) {
				$(id).append("<option value='"+products[i].productid+"'>"+products[i].name+"</option>"); 
			}
			
		}
		
		/*播放动画效果*/
		function animate() {
			$("#queryTable").hide("slow");
			$("#queryTable").show("slow");
		}
				
	</script>
</head>
<body>
	<div style="width:1000px;height:auto;">
	<h1 align="center">Editable DataGrid</h1>
	<!-- tab start -->
	<div id="tabs" >
		<div title="增删改查表" style="padding:15px;">
			<!-- form start -->
			<form name='form1' id='form1'>
			<table id="queryTable" class="genericTbl"  style="width:100%;height:auto">
			<tr>
			<td>
			<fieldset>
				<legend>查询条件</legend>
		
						<table id="queryTable1">
							<tr>
								<td width="10%" align="right">
									itemid like
								</td>
								<td width="20%" align="left">
									<input type="text" name="itemid">
								</td>
								<td width="10%" align="right">
									产品 =
								</td>
								<td width="20%" align="left">
									<select style="width:100%;" name="productid" id="productid"></select>
								</td>
								<td width="20%" align="center" nowrap>
									<a class="easyui-linkbutton" icon="icon-add" href="javascript:void(0)" onclick="">重置</a>
								</td>
							</tr>
							<tr>
								<td style="" width="10%" align="right">
									价格 >=
								</td>
								<td width="20%" align="left">
									<input type="text" name="listprice">
								</td>
								<td style="" width="10%" align="right">
									成本 >=
								</td>
								<td width="20%" align="left">
								<input type="text" name="unitcost">
								</td>
								<td width="20%" align="center" nowrap>
									<a class="easyui-linkbutton" icon="icon-search" href="javascript:void(0)" onclick="doQuery()">查询</a>
								</td>
							</tr>
						</table>
					</fieldset>
					</td>
					</tr>
			</table>
			</form>
			<!-- form end -->
			<!-- dadagrid start -->
			<!-- url="http://127.0.0.1:8000/bboss/datagrid/getData.htm?templet_id=1&report_id=1&tableInfoId=CAN_BE_TABLE_NAME" -->
			<table id="tt" 
					title="数据编辑器" iconCls="icon-edit" singleSelect="true"
					idField="itemid" url="http://172.16.81.53:8000/bboss/datagrid/getData.htm?templet_id=1&report_id=1&tableInfoId=CAN_BE_TABLE_NAME">
				<thead>
					<tr>
						<th field="itemid" width="80" rowspan='2' editor="{type:'text'}">Item ID</th>
						<th field="productid" width="100" rowspan='2' formatter="formatter_product" editor="{type:'combobox',options:{valueField:'productid',textField:'name',data:products,required:true}}">产品</th>
						<th colspan='2'>产品属性1</th>
						<!-- 
						<th field="productid" width="100" rowspan='2'>产品</th>
						 -->
						<th colspan='2'>产品属性2</th>	
								
					</tr>
					<tr>				
						<th sorter="listpriceSorter" field="listprice" width="80" align="right" editor="{type:'numberbox',options:{precision:1,required:true}}">价格(￥)</th>
						<th field="unitcost" width="80" align="right" editor="numberbox" formatter="formatter_unitcost">成本(￥)</th>
						<th field="attr1" width="150" editor="text">属性</th>
						<th field="status" width="60" align="center" editor="{type:'checkbox',options:{on:'P',off:''}}">状态</th>
					</tr>
				</thead>
			</table>
			<!-- dadagrid end -->
			
		</div>
		<div title="系统管理表" closable="true" style="padding:15px;" cache="false" href="tabs_href_test.html">
			This is Tab2 with close button.
		</div>
		<div title="内嵌iframe" closable="true">
			<iframe scrolling="yes" frameborder="0"  src="http://www.google.com" style="width:100%;height:100%;"></iframe>
		</div>
		<div title="DataGrid介绍" closable="false" align="left" style="padding:15px;">
				<p>
					作者：李峰高
				</p>
				<p>
					已实现功能
				</p>					
				<ul>
					<li>
						可自由进行增删改查操作，无需进行任何数据库操作，由datagrid自动完成
					</li>
					<li>
						
					</li>
				</ul>
				<p>
					未实现功能
				</p>					
				<ul>
					<li>
						暂时没有实现数据导入导出
					</li>
					<li>
						
					</li>
				</ul>
			</div>
	</div>
	<!-- tab end -->
	</div>
</body>
</html>