<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%
String templet_id = "1";
String report_id = "1";
String tableInfoId = "CAN_BE_TABLE_NAME";
 %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>DataGrid Editer</title>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsp/datagrid/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsp/datagrid/themes/icon.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/classic/tables.css" type="text/css"></link>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/datagrid/jquery-1.4.4.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/datagrid/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/datagrid/json2.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/datagrid/js/DataGrid.js"></script>
	
	<script>
		var editer_params = {	
			templet_id:'<%=templet_id%>',		
			report_id:'<%=report_id%>',
			tableInfoId:'<%=tableInfoId%>'
			
		};
		var dataGrid1 = new DataGrid('#tt',editer_params);
		
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
				alert("对不起！成本不能为空！");
				//定位重新修改
				$('#tt').datagrid('beginEdit', rowIndex);
				return false;
			}
		}
		
		$(function(){
			<!-- datagrid start -->
			var lastIndex;
			$('#tt').datagrid({
				toolbar:[{
					text:'添加',
					iconCls:'icon-add',
					handler:function(){
					$('#tt').datagrid('endEdit', lastIndex);
						$('#tt').datagrid('appendRow',{
							REPORT_ID:'<%=report_id %>',
							itemid:'EST-',
							productid:'',
							listprice:'',
							unitcost:'',
							attr1:'',
							status:'P'
						});
						var lastIndex = $('#tt').datagrid('getRows').length-1;
						$('#tt').datagrid('beginEdit', lastIndex);
					}
				},'-',{
					text:'删除',
					iconCls:'icon-remove',
					handler:function(){
						var row = $('#tt').datagrid('getSelected');
						if (row){
							var index = $('#tt').datagrid('getRowIndex', row);
							$('#tt').datagrid('deleteRow', index);
						}
					}
				},'-',{
					text:'撤销',
					iconCls:'icon-undo',
					handler:function(){
						$('#tt').datagrid('rejectChanges');
					}
				}
				/*	
				,'-',{
					text:'更改信息',
					iconCls:'icon-search',
					handler:function(){
						//var rows = $('#tt').datagrid('getChanges','deleted');	
						var rows = $('#tt').datagrid('getChanges');
						alert('changed rows: ' + rows.length + ' lines');
						if(rows.length>0){							
							alert("改变行:"+rows[0]["itemid"]);							
						}
					}
				}							
				,'-',{
					text:'确认',
					iconCls:'icon-ok',
					handler:function(){
						$('#tt').datagrid('acceptChanges');
					}
				}
				*/
				,'-',{
					text:'保存',
					iconCls:'icon-save',
					handler:function(){
						saveData();
					}
				}],
				fitColumns:true,
				loadMsg:'正在加载数据，请稍后...',
				onBeforeLoad:function(){
					$(this).datagrid('rejectChanges');
				},
				onClickRow:function(rowIndex){
					if (lastIndex != rowIndex){
						$('#tt').datagrid('endEdit', lastIndex);
						$('#tt').datagrid('beginEdit', rowIndex);
					}else{
						$('#tt').datagrid('beginEdit', rowIndex);
					}
					lastIndex = rowIndex;
					
					dataGrid1.currRowIndex = rowIndex;
				},
				onBeforeEdit:function(rowIndex, rowData){
					//alert("rowIndex="+rowIndex+"\nrowData="+object2json(rowData));
				},
				onAfterEdit:function(rowIndex, rowData, changes){
					validator(rowIndex, rowData);
					//alert("rowIndex="+rowIndex+"\nrowData="+object2json(rowData)+"\nchanges="+object2json(changes));
					/*
					if(rowData["unitcost"]==null || rowData["unitcost"] == "" ){
						alert("成本不能为空！");
						$('#tt').datagrid('beginEdit', rowIndex);
						return false;
					}
					*/
				}
				/**
				,columns:[[
						{field:'itemid',title:'Item ID',rowspan:2,width:80,sortable:true},
						{field:'productid',title:'产品',rowspan:2,width:80,sortable:true},				
						{title:'产品属性1',colspan:2},
						{field:'productid',title:'产品',rowspan:2,width:100,sortable:true},	
						{title:'产品属性2',colspan:2}
					],[
						{field:'listprice',title:'价格(￥)',width:80,align:'right',sortable:true,editor:"numberbox"},
						{field:'unitcost',title:'成本(￥)',width:80,align:'right',sortable:true,editor:'numberbox'},
						{field:'attr1',title:'属性',width:100,editor:'text'},
						{field:'status',title:'状态',width:60,align:'center',editor:"{type:'checkbox',options:{on:'P',off:''}}"}
					]]
					**/
				<!-- datagrid end -->
			});
		});
		
		var templet_id = "1";
		var report_id = "1";
		
		/*将对象转换为jsoncode*/
		function object2json(jsonObj){
		
			var jsoncode =  JSON.stringify(jsonObj); 
			//alert(jsoncode);			
			//document.writeln(jsoncode); 
			return jsoncode;
		} 
		
		/*保存数据*/
		function saveData() {
			try{
				//保存当前更改
				//alert("dataGrid1.currRowIndex="+dataGrid1.currRowIndex);			
				$('#tt').datagrid('endEdit', dataGrid1.currRowIndex);	
				//转换为jsoncode码并提交到后台数据库				
				var rows = $('#tt').datagrid('getChanges');				
				if(rows.length>0){					
					submitData();
				}else{
					alert("对不起！数据没有更改或者仍有错误数据，请填好数据！");
					return false;
				}				
			}catch(e){
				onSaveDataError(null, e.description, null);				
			}
		}
		
		/*提交数据*/
		function submitData(data){
			var deleted_rows = $('#tt').datagrid('getChanges','deleted');
			var updated_rows = $('#tt').datagrid('getChanges','updated');
			var inserted_rows = $('#tt').datagrid('getChanges','inserted');	
			
			var deleted_data = object2json(deleted_rows);
			var updated_data = object2json(updated_rows);
			var inserted_data = object2json(inserted_rows);
			
			var data = "{\"deleted\":"+deleted_data+","
						+"\"updated\":"+updated_data+","
						+"\"inserted\":"+inserted_data+"}";
			//var obj = JSON.parse(data);
								
			var url = "${pageContext.request.contextPath}/datagrid/saveData.htm?templet_id=<%=templet_id %>&report_id=<%=report_id %>&tableInfoId=<%=tableInfoId %>";
			param = "data="+data;
			//var data = encodeURIComponent(data);
			
			var r = $.ajax({
							type: "post",
							url: url,
							contentType: "application/x-www-form-urlencoded; charset=utf-8",
							dataType: "json",
							async: false,
							cache: false,
							timeout: 3000,	
							data: {data: data},						
							beforeSend: function(XMLHttpRequest){
								//ShowLoading();
							},
							success: onSaveDataSuccess,
							complete: function(XMLHttpRequest, textStatus){
								//HideLoading();
							},
							error: onSaveDataError
					});
			
			//var r = $.post(url,param,onSaveDataSuccess,"json");	
		}
		
		/*保存数据出错*/
		function onSaveDataError(XMLHttpRequest, textStatus, errorThrown) {			
			alert("对不起！保存失败:"+textStatus+"");
			//保存失败取消客户端本次更改
			//$('#tt').datagrid('rejectChanges');
		}
		
		/*保存数据出错*/
		function onSaveDataSuccess(data, textStatus) {
			
			if (data.status == 1) {
				//保存成功确认客户端本次更改
				$('#tt').datagrid('acceptChanges');
				alert(data.msg);
			} else {
				alert(data.msg);
			}
			//$.messager.show(0, data.msg);  
		}
		
		function init() {
			//loadData();
			//animate();
			initSelect("productid", products);
		}
		
		/*加载数据*/
		function loadData(){
			/*
			alert("");
			var b = $('#tt').datagrid('loadData','datagrid_data2.json');
			alert(b);
			*/
		}
		//该函数不起作用
		//$(document).ready(initSelect("productid", products));
		
		/*初始化数据*/
		function initSelect(selectId, datas) {
			var id = "#"+selectId;
			//products productid
			var selectObj = $(id);
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
<body onload="init();">
	<h1>Editable DataGrid</h1>
	<!-- datagrid_data2.json -->
	<input type="button" onclick="animate();">
	<form>
	<fieldset style="width:643px;height:auto">
		<legend>查询条件</legend>

				<table id="queryTable" class="genericTbl">
					<tr>
						<td width="25%" align="right">
							itemid
						</td>
						<td width="25%" align="right">
							<input type="text" name="itemid">
						</td>
						<td width="25%" align="right">
							产品
						</td>
						<td width="25%" align="right">
							<select style="width:100%;" name="productid" id="productid"></select>
						</td>

					</tr>
					<tr>
						<td style="" width="25%" align="right">
							价格 >=
						</td>
						<td width="25%" align="right">
							<input type="text" name="listprice">
						</td>
						<td style="" width="25%" align="right">
							成本 >=
						</td>
						<td width="25%" align="right">
						<input type="text" name="unitcost">
						</td>
					</tr>
				</table>
			</fieldset>
	</form>
	
	<table id="tt" style="width:650px;height:auto"
			title="数据编辑器" iconCls="icon-edit" singleSelect="true"
			idField="itemid" url="${pageContext.request.contextPath}/datagrid/getData.htm?templet_id=<%=templet_id %>&report_id=<%=report_id %>&tableInfoId=<%=tableInfoId %>">
			<!-- 简单表头·
		<thead>
			<tr>
				<th field="itemid" width="80">Item ID</th>
				<th field="productid" width="100" formatter="formatter_product" editor="{type:'combobox',options:{valueField:'productid',textField:'name',data:products,required:true}}">产品</th>
				<th field="listprice" width="80" align="right" editor="{type:'numberbox',options:{precision:1}}">价格(￥)</th>
				<th field="unitcost" width="80" align="right" editor="numberbox">成本(￥)</th>
				<th field="attr1" width="150" editor="text">属性</th>
				<th field="status" width="60" align="center" editor="{type:'checkbox',options:{on:'P',off:''}}">状态</th>
			</tr>
		</thead>
		-->		
		<!-- 复杂表头 -->
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
	
</body>
</html>