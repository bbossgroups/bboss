<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ taglib uri="/WEB-INF/commontag.tld" prefix="common"%>
<%
String path = request.getContextPath();
%>
<div id="pagecontent">
              <div class="genericTbl">
				<!--分页显示开始,分页标签初始化-->
			   	<pg:pager scope="request" data="users" 
						  isList="false" containerid="pagecontainer" selector="pagecontent">
				<pg:params name="userName" />
				<div><pg:index/></div>
			  
			<table class="genericTbl">
					<tr >
						<th width="2%" align=center style="width:5%" class="order1 sorted">
						  &nbsp;&nbsp;全选<input class="checkbox" 
							type="checkBox" hidefocus=true 
							name="checkBoxAll" 
							onClick="checkAll('checkBoxAll','id')"> 
						</th>
						<pg:title nowrap="true" width="8%" title="用户ID:"
											sort="true" colName="userId" className="order1 sorted"/>	
						
						<pg:title nowrap="true" width="8%" title="用户NAME:"
											sort="true" colName="userName" className="order1 sorted"/>	
						
						<th width="8%" class="order1 sorted">
							用户口令:					</th>
						
					</tr>

					
					<pg:notify>
					<tr class="cms_report_tr">
					<td width="10%" align=center colspan="100" style="width:5%">
						没有数据
					</td>
					</tr>				
				</pg:notify>
				<pg:list autosort="false">
					<tr class="even">
						<td width="2%" align=center style="width:5%">
							<input class="checkbox"  onClick="checkOne('checkBoxAll','id')" 
							type="checkbox" name="id" value="<pg:cell colName="userId" defaultValue=""/>"></td>
						 <td ><pg:cell colName="userId" defaultValue=""/>  
                         </td>  
                         <td width="8%" >  
                             <pg:cell colName="userName" defaultValue=""/></td>  
                         <td width="8%" >  
                             <pg:cell colName="userPassword" defaultValue=""/>         
                       </td>  
					</tr>
					
				</pg:list>
		
			</table>
			<div><pg:index aindex="false" tagnumber="5" numberend=">" numberpre="<"/></div>
			</pg:pager> 
			</div>
	
</div>