<%@ page contentType="text/html; charset=GBK" language="java" %>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.common.tag.contextmenu.ContextMenu"%>
<%@ page import="com.frameworkset.common.tag.contextmenu.ContextMenuImpl"%>
<%@ page import="com.frameworkset.common.tag.contextmenu.Menu"%>
<!-- 
	测试在通过数据加载器获取分页列表数据
	并且在表名上添加右键菜单功能
-->
<html>
<head>
<title>测试在通过数据加载器获取分页列表数据</title>
<script type="text/javascript"> 
	function edit(message)
	{
		alert(message);
	}
</script>
</head>
<body>
	<table>
	    
				<pg:listdata dataInfo="test.pager.TableInfoListData" keyName="TableInfoListData" />
				<!--分页显示开始,分页标签初始化-->
				<pg:pager maxPageItems="15" scope="request" data="TableInfoListData" 
						  isList="false">
					<tr class="cms_report_tr">
						<!--设置分页表头-->

						<td width="2%" align=center style="width:5%">
						<input class="checkbox" 
							type="checkBox" hidefocus=true 
							name="checkBoxAll" 
							onClick="checkAll('checkBoxAll','ID')"> 
						</td>
						<td width="8%">
							TABLE_NAME					</td>
						<td width="8%">
							TABLE_ID_NAME						</td>
						<td width="28%">
							TABLE_ID_INCREMENT</td>
						
						<td width="6%">
							TABLE_ID_VALUE						</td>
						<td width="9%">
							TABLE_ID_GENERATOR						</td>
						
						<td width="10%" height='30'>TABLE_ID_TYPE</td>
						<td width="10%" height='30'>TABLE_ID_PREFIX</td>
					</tr>
				<pg:notify>
						<tr  class="labeltable_middle_tr_01">
							<td colspan=100 align='center' height="18px">
								没有数据
							</td>
						</tr>
				</pg:notify>
				<%
						ContextMenu contextmenu = new ContextMenuImpl();
				%>
					
				<pg:list>
				
				<% 
						Menu menu = new Menu();
						
						
						
						menu.setIdentity("conmenu_" + rowid);			
						menu.addContextMenuItem("添加","javascript:edit('添加')",Menu.icon_edit);
						//Menu.ContextMenuItem sitemenuitem0 = new Menu.ContextMenuItem();
						//sitemenuitem0.setName("编辑编辑编辑编辑");
						//sitemenuitem0.setLink("javascript:edit('编辑')");
						//sitemenuitem0.setIcon(Menu.icon_edit);
						//menu.addContextMenuItem(sitemenuitem0);
						menu.addSeperate();
						menu.addContextMenuItem("编辑编辑编辑编辑","javascript:edit('编辑')",Menu.icon_add);
						
						Menu.ContextMenuItem sitemenuitem2 = menu.addContextMenuItem("sitemenuitem2","javascript:edit('sitemenuitem2')",Menu.icon_ok);
						sitemenuitem2.addSubContextMenuItem("子menusubmenuitem_","javascript:edit('子menusubmenuitem_')",Menu.icon_ok);	
						sitemenuitem2.addSubContextMenuItem("子cut","javascript:edit('子cut')",Menu.icon_cut);				
						sitemenuitem2.addSubContextMenuItem("子icon_back","javascript:edit('子icon_back')",Menu.icon_back);
						sitemenuitem2.addSubContextMenuItem("子icon_cancel","javascript:edit('子icon_cancel')",Menu.icon_cancel);
						sitemenuitem2.addSubContextMenuItem("子icon_help","javascript:edit('子icon_help')",Menu.icon_help);
						sitemenuitem2.addSubContextMenuItem("子icon_no","javascript:edit('子icon_no')",Menu.icon_no);
						sitemenuitem2.addSubContextMenuItem("子icon_print","javascript:edit('子icon_print')",Menu.icon_print);
						sitemenuitem2.addSubContextMenuItem("子icon_redo","javascript:edit('子icon_redo')",Menu.icon_redo);
						sitemenuitem2.addSubContextMenuItem("子icon_reload","javascript:edit('icon_reload')",Menu.icon_reload);
						sitemenuitem2.addSubContextMenuItem("icon_remove","javascript:edit('icon_remove')",Menu.icon_remove);
						sitemenuitem2.addSubContextMenuItem("icon_save","javascript:edit('icon_save')",Menu.icon_save);
						Menu.ContextMenuItem sitemenuitem__ = sitemenuitem2.addSubContextMenuItem("icon_search","javascript:edit('icon_search')",Menu.icon_search);
						
						sitemenuitem__.setDisabled(true);
						sitemenuitem__.setDisableMsg("禁用的功能");
						
						Menu.ContextMenuItem sitemenuitem3 = sitemenuitem2.addSubContextMenuItem("icon_undo","javascript:edit('icon_undo')",Menu.icon_undo);
						sitemenuitem3.setDisabled(true);
						contextmenu.addContextMenu(menu);
				%>
					<tr class="cms_report_tr">
						

						<td width="2%" align=center style="width:5%">
							<input class="checkbox" hideFocus onClick="checkOne('checkBoxAll','ID')" 
							type="checkbox" name="ID" 
							value="<pg:cell colName="document_id" defaultValue=""/>">										
						</td>
						<td width="10%" id="conmenu_<pg:rowid/>">
							<pg:cell colName="TABLE_NAME" defaultValue=""/>					</td>
						<td width="8%">
							<pg:cell colName="TABLE_ID_NAME" defaultValue=""/>						</td>
						<td width="28%">
							<pg:cell colName="TABLE_ID_INCREMENT" defaultValue=""/></td>
						
						<td width="6%">
							<pg:cell colName="TABLE_ID_VALUE" defaultValue=""/>						</td>
						<td width="9%">
							<pg:cell colName="TABLE_ID_GENERATOR" defaultValue=""/>						</td>
						
						<td width="10%" height='30'><pg:cell colName="TABLE_ID_TYPE" defaultValue=""/></td>
						<td width="10%" height='30'>&nbsp;<pg:cell colName="TABLE_ID_PREFIX" defaultValue=""/></td>
					</tr>
					</pg:list>
					<%
							request.setAttribute("testcontextmenu",contextmenu);
					%>
					<pg:contextmenu context="testcontextmenu" enablecontextmenu="true" scope="request"/>
					<tr class="labeltable_middle_tr_01">
						<td colspan=11 ><div class="Data_List_Table_Bottom"> 
							共
							<pg:rowcount />
							条记录
							每页显示15条
							<pg:index />					</div>  </td>
					</tr>
					<input id="queryString" name="queryString" value="<pg:querystring/>" type="hidden">
					<tr></tr>
				</pg:pager>
				
	</table>
</body>
</html>
