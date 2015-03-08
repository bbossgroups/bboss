<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
	<%--
	描述：增加台账信息设置
	作者：许石玉
	版本：1.0
	日期：2012-02-08
	 --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>查看台账</title>
<%@ include file="/common/jsp/css.jsp"%>
</head>
<body>
		<div class="mcontent">
			<div id="searchblock">
				<pg:beaninfo requestKey="appbom">
				<form id="editForm" name="editForm">
				<fieldset>
				<legend>应用软件基本信息</legend>
					<table border="0" cellpadding="0" cellspacing="0"
						class="table4">
						<tr height="25px">
							<th width=85px >编码：</th>
							<td width=140px><pg:cell colName="bm"/></td>
							<th width=85px >软件名称：</th>
							<td width=140px>
								<pg:cell colName="app_name"/>
							</td>
							<th width=70px>软件缩写：</th>
							<td width=140px>
								<pg:cell colName="app_name_en"/>
							</td>
						</tr>
						<tr height="25px">	
							<th >级别：</th>
							<td >
							<pg:case colName="soft_level" >
								<pg:equal value="1">一级</pg:equal>
				                <pg:equal value="2">二级</pg:equal>
				                <pg:equal value="3">三级</pg:equal>
				                <pg:equal value="10">待定</pg:equal>
				            </pg:case>    
							</td>

							<th width=85px >应用功能域：</th>
							<td><pg:cell colName="apply_domain"/></td>
							<th>
								状态：
							</th>
							<td>
							<pg:case colName="state" >
								    <pg:equal value="1">在用</pg:equal>
			                        <pg:equal value="2">试运行</pg:equal>
			                        <pg:equal value="3">升级研发中</pg:equal>
			                        <pg:equal value="4">停用</pg:equal>
			                        <pg:equal value="5">暂用</pg:equal>
			                        <pg:equal value="6">研发中</pg:equal>
			                        <pg:equal value="7">试验中</pg:equal>
			                </pg:case>            
							</td>
						</tr>
						<tr height="25px">
							<th>投建年份：</th>
							<td><pg:cell colName="start_year"/></td>
							<th width=85px >
								软件厂商：
							</th>
							<td colspan=3><pg:cell colName="supplier"/></td>
	
						</tr>
						<tr >
							<th>功能描述：</th>
							<td colspan=5><pg:cell colName="description"/></td>
						</tr> 
					</table>
				</fieldset>
				<fieldset>
				<legend>软件技术方案</legend>
				<table  border="0" cellspacing="0" cellpadding="0" class="table4">
					<tr height="25px">
						<th width=85px>结构模式：</th>
						<td width=140px><pg:cell colName="struct_mode"/></td>	
						<th width=85px>软件语言：</th>
						<td width=140px><pg:cell colName="soft_language"/></td>	
						<th width=70px>研发类型：</th>
						<td width=140px>
						<pg:case colName="rd_type">
							<pg:equal value="1">自研</pg:equal>
	                		<pg:equal value="2">外购</pg:equal>
	                        <pg:equal value="3">免费</pg:equal>
	                        <pg:equal value="4">试用</pg:equal>
	                        <pg:equal value="5">外购+定制</pg:equal>
	                    </pg:case>    
						</td>
					</tr>
					<tr height="30px">
							<th>开发工具：</th>
							<td ><pg:cell colName="develop_tool"/></td>	
							<th>数据库：</th>
							<td colspan=3><pg:cell colName="db_type"/></td>	

					</tr>
					<tr height="25px">
					<th>系统域名：</th>
					<td colspan=5><pg:cell colName="domain_url"/></td>
					</tr>	
					<tr height="25px">
					<th>版本号：</th>
					<td colspan=5><pg:cell colName="version_no"/></td>
					</tr>
				</table>
				</fieldset>	
				<fieldset>
				<legend>管理信息</legend>
				<table  border="0" cellspacing="0" cellpadding="0" class="table4">
					<tr height="25px">
						<th width=85px>研发部门：</th>
						<td width=140px><pg:cell colName="department_develop"/></td>
						<th width=85px>产品经理：</th>
						<td width=140px><pg:cell colName="product_manager"/></td>
						<th width=70px>运维部门：</th>
						<td width=140px><pg:cell colName="department_maintain"/></td>
					</tr>
					<tr height="25px">
					<th>系统经理：</th>
					<td ><pg:cell colName="sys_manager"/></td>
         			<th>规划类型：</th>
					<td >  
					<pg:case colName="plan_type">
						<pg:equal value="1">目标</pg:equal>
                        <pg:equal value="2">演进</pg:equal>
                        <pg:equal value="3">临时</pg:equal>
                        <pg:equal value="10">待定</pg:equal>
                    </pg:case>        
	                </td>
		             <th>演进策略：</th>
		             <td colspan=3 ><pg:cell colName="evolve_strategy"/></td>
					</tr>
					<tr height="25px">
						<th>演进负责方：</th>
						<td ><pg:cell colName="evolve_depart"/></td>
						 <th>演进实施计划：</th>
		                <td colspan=3><pg:cell colName="evolve_plan"/></td>		
							
					</tr>
					<tr height="25px">
						<th>研发管理范围：</th>	
		             	<td colspan=3>
		             	<pg:case colName="manage_scope">
			                <pg:equal value="1">管理列表</pg:equal>
	                        <pg:equal value="2">管理软件+版本号</pg:equal>
	                        <pg:equal value="3">管理软件+版本号+测试</pg:equal>
	                        <pg:equal value="4">管理软件+版本号+测试+UI</pg:equal>
	                        <pg:equal value="5">管理软件+版本号+UI</pg:equal>
	                    </pg:case>        
		                </td>
					</tr>
					<tr >
						<th>关键说明：</th>
						<td colspan=5><pg:cell colName="main_description"/></td>
					</tr> 
		
				</table>
				</fieldset>
				<div class="btnarea" >
					<a href="javascript:void(0)" class="bt_2" id="closeButton" onclick="closeDlg()"><span>退出</span></a>
				</div>	
				</form>
				</pg:beaninfo>
			</div>
			
  
  	</div>					
</body>