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
		
		var products = [
		    {productid:'FI-SW-01',name:'&nbsp;&nbsp;&nbsp;&nbsp;Koi'},
		    {productid:'K9-DL-01',name:'&nbsp;&nbsp;&nbsp;Dalmation'},
		    {productid:'RP-SN-01',name:'&nbsp;&nbsp;Rattlesnake'},
		    {productid:'RP-LI-02',name:'&nbsp;Iguana'},
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
		
		/*初始化对象*/
		//设置DataGrid可调试
		DataGrid.debug(true);
		
		var editer1 = new DataGrid('#table1');
		var editer2 = new DataGrid('#table2');
		
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
			$('#table1').datagrid({
				toolbar:[{
					text:'添加',
					iconCls:'icon-add',
					handler:function(){
						editer.appendRow({
							REPORT_ID:editer.getQueryParams().report_id,
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
				},'-',{
					text:'保存',
					iconCls:'icon-save',
					handler:function(){
						editer.saveData();
					}
				},'-',{
					text:"调试信息",		
					iconCls:'icon-warning',			
					handler:function(){
						//editer.setDebug();
						//alert($('#table1').datagrid('options').toolbar[8].text);
						DataGrid.log("info:黑色，error:红色，message:蓝色","message");
						DataGrid.showLogs();
					}
				},'-',{
					text:"动态列",		
					iconCls:'icon-custom_display',			
					handler:function(){
						editer.customDisplay();						
					}
				}],
				collapsible:true,
				idField:'itemid',
				//frozenColumns:[[{field:'itemid',title:'Item ID',rowspan:2,width:80,sortable:true}]],
				queryParams:{report_id:'<%=report_id%>',tableInfoId:'<%=tableInfoId%>'},
				pagination:true,
				rownumbers:true,
				pageSize:10,
				pageList:[5,10,15,20],
				striped:true,
				fitColumns:true,
				showFooter:true,
				singleSelect:true,
				loadMsg:'正在加载数据，请稍后...',
				//loadFilter:editer.loadFilter,
				onBeforeLoad:function(){
					editer.rejectChanges();
				},
				onClickRow:function(rowIndex){
					editer.onClickRow(rowIndex);
				},				
				onAfterEdit:function(rowIndex, rowData, changes){
					//validator(rowIndex, rowData);					
				},	
				onLoadSuccess:function(data){
					editer.onLoadSuccess();
				}			
			});
			<!-- datagrid end -->
			
			//初始化datagrid，一定要注意这里
			//var editer = editer2;
			<!-- datagrid start -->
			$('#table2').datagrid({
				toolbar:[{
					text:'添加',
					iconCls:'icon-add',
					handler:function(){
						editer2.appendRow({
							f1:'请输入表名',
							f2:'id',
							f3:1,
							f4:1,
							f5:'',
							f6:'',
							f7:''
						});
					}
				},'-',{
					text:'删除',
					iconCls:'icon-remove',
					handler:function(){
						editer2.deleteRow();
					}
				},'-',{
					text:'撤销',
					iconCls:'icon-undo',
					handler:function(){
						editer2.rejectChanges();
					}
				}
				,'-',{
					text:'保存',
					iconCls:'icon-save',
					handler:function(){
						editer2.saveData();
					}
				}],
				queryParams:{tableInfoId:'TABLEINFO'},
				fitColumns:true,
				rownumbers:true,
				loadMsg:'正在加载数据，请稍后...',
				onBeforeLoad:function(){
					editer2.rejectChanges();
				},
				onClickRow:function(rowIndex){
					editer2.onClickRow(rowIndex);
				},				
				onAfterEdit:function(rowIndex, rowData, changes){
					//validator(rowIndex, rowData);					
				}				
			});
			<!-- datagrid end -->
			
			//初始化数据
			initSelect("productid", products);
			//加载编辑器1的数据
			doQuery();
			//给编辑器2加载全部数据
			editer2.loadData();
			//改变事件
			$( "#itemid2" ).change(function() {
				var newValue = "%"+this.value+"%";
				$('#itemid').val(newValue);
			});
			
		});
		/*加载数据*/
		function doQuery(){			
			/*加载数据，支持四种参数，字符串，jquery对象，普通对象，自定义函数*/
			editer1.loadData("#form1 input,#form1 select:enabled");
			//editer1.loadData($("#form1 input:enabled,#form1 select:enabled"));
			//editer1.loadData({itemid:'e',unioncost:4});
			//editer1.loadData(addParams);//addParams is a function,etc function addParams(queryParams){...}
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
	<!-- <h1 align="center">Editable DataGrid</h1> -->
	<!-- tab start -->
	<div id="tabs" >
		<div title="增删改查表" style="padding:15px;font-size:14px;">
			<!-- form start -->
			<form name='form1' id='form1'>
			<table id="queryTable" class="genericTbl" >
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
									<input type="hidden" name="itemid" id="itemid">
									<input type="text" name="itemid2" id="itemid2">
								</td>
								<td width="10%" align="right">
									产品 =
								</td>
								<td width="20%" align="left">
									<select style="width:100%;" name="productid" id="productid"></select>
								</td>
								<td width="20%" align="center" nowrap>
									<a class="easyui-linkbutton" icon="icon-pointer" href="javascript:void(0)" onclick="document.forms[0].reset();">重置</a>
								</td>
							</tr>
							<tr>
								<td style="" width="10%" align="right">
									价格 >
								</td>
								<td width="20%" align="left">
									<input type="text" name="listprice">
								</td>
								<td style="" width="10%" align="right">
									成本 >
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
			<!-- url="${pageContext.request.contextPath}/datagrid/getData.htm?templet_id=1&report_id=1&tableInfoId=CAN_BE_TABLE_NAME" -->
			<table id="table1" 
					title="数据编辑器" iconCls="icon-edit" singleSelect="true"
					>
				<thead>
					<tr>
						<th field="itemid" width="80" rowspan='2' editor="{type:'text'}" >Item ID</th>
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
						<th field="attr1" width="100" editor="text">属性</th>
						<th field="status" width="60" align="center" editor="{type:'checkbox',options:{on:'P',off:''}}">状态</th>
					</tr>
				</thead>
			</table>
			<!-- dadagrid end -->
			
		</div>
		<div title="编辑TABLEINF表" closable="true" style="padding:15px;" cache="false">
			<!-- dadagrid start -->
			<table id="table2" 
					singleSelect="true"
					idField="f1">
				<thead>
					<tr>
						<th field="f1" width="100" editor="{type:'text'}">表名</th>
						<th field="f2" width="100" editor="{type:'text'}">主键名</th>
						<th field="f3" width="80" align="right" editor="{type:'numberbox',options:{precision:0,required:true}}">递增量</th>
						<th field="f4" width="80" align="right" editor="{type:'numberbox',options:{precision:0,required:true}}">主键当前值</th>
						<th field="f5" width="80" editor="text">主键生成机制</th>
						<th field="f6" width="60" align="center" editor="{type:'text'}">主键类型</th>
						<th field="f7" width="60" align="center" editor="{type:'text'}">主键前缀</th>
					</tr>
				</thead>
			</table>
			<!-- dadagrid end -->
		</div>
		<div title="内嵌iframe" closable="true">
			<iframe scrolling="yes" frameborder="0"  src="" style="width:100%;height:100%;"></iframe>
		</div>
		<div title="DataGrid介绍" closable="false" align="left" style="padding:15px;" href="${pageContext.request.contextPath}/jsp/datagrid/page/datagrid_info.jsp">
				
			</div>
	</div>
	<!-- tab end -->
	</div>
</body>
</html>