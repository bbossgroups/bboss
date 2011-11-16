<%@ page contentType="text/html; charset=GBK" language="java" %>
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld"%>
<tab:tabConfig />
<script>
		$(function(){
			$('#tt2').datagrid({
				title:'My Title',
				iconCls:'icon-save',
				width:600,
				height:350,
				nowrap: false,
				striped: true,
				fit: true,
				url:'datagrid_data.jsp',
				sortName: 'code',
				sortOrder: 'desc',
				idField:'code',
				frozenColumns:[[
	                {field:'ck',checkbox:true},
	                {title:'code',field:'code',width:80,sortable:true}
				]],
				columns:[[
			        {title:'Base Information',colspan:3},
					{field:'opt',title:'Operation',width:100,align:'center', rowspan:2,
						formatter:function(value,rec){
							return '<span style="color:red">Edit Delete</span>';
						}
					}
				],[
					{field:'name',title:'Name',width:120},
					{field:'addr',title:'Address',width:120,rowspan:2,sortable:true},
					{field:'col4',title:'Col41',width:150,rowspan:2}
				]],
				pagination:true,
				rownumbers:true,
				queryParams:{name:"test",value:"value"}
			});
		});
		
	</script>
	
	<body class="easyui-layout">
	<div id="mymenu" style="width:150px;">
		<div>item1</div>
		<div>item2</div>
	</div>
		<div region="north" title="North Title" split="true" style="height:100px;padding:10px;">
			<p>n1</p>
			<p>n2</p>
			<p>n3</p>
			<p>n4</p>
			<p>n5</p>
		</div>
		<div region="south" title="South Title" split="true" style="height:100px;padding:10px;background:#efefef;">
			<div class="easyui-layout" fit="true" style="background:#ccc;">
				<div region="center">sub center</div>
				<div region="east" split="true" style="width:200px;">sub center</div>
			</div>
		</div>
		<div region="east" icon="icon-reload" title="Tree Menu" split="true" style="width:180px;">
			<ul class="easyui-tree" url="tree_data.json"></ul>
		</div>
		<div region="west" split="true" title="West Menu" style="width:280px;padding1:1px;overflow:hidden;">
			<div class="easyui-accordion" fit="true" border="false">
				<div title="Title1" style="overflow:auto;">
					<p>content1</p>
					<p>content1</p>
					<p>content1</p>
					<p>content1</p>
					<p>content1</p>
					<p>content1</p>
					<p>content1</p>
					<p>content12</p>
				</div>
				<div title="Title2" selected="true" style="padding:10px;">
					content2
					<a href="#" onclick="addmenu()">addmenu</a>
				</div>
				<div title="Title3">
					content3
				</div>
			</div>
		</div>
		<div region="center" title="Main Title" style="overflow:hidden;">
			<div class="easyui-tabs" fit="true" border="false">
				<div title="Tab1" style="padding:20px;overflow:hidden;"> 
					<div style="margin-top:20px;">
						<h3>jQuery EasyUI framework help you build your web page easily.</h3>
						<li>easyui is a collection of user-interface plugin based on jQuery.</li> 
						<li>using easyui you don't write many javascript code, instead you defines user-interface by writing some HTML markup.</li> 
						<li>easyui is very easy but powerful.</li> 
					</div>
				</div>
				<div title="Tab2" closable="true" style="padding:20px;">This is Tab2 width close button.</div>
				<div title="Tab3" icon="icon-reload" closable="true" style="overflow:hidden;padding:5px;">
					<table id="tt2"></table> 
				</div>
			</div>
		</div>
</body>