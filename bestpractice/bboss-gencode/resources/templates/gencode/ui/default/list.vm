<%@ page language="java"  pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<%--
描述：台账列表信息设置
作者：许石玉
版本：1.0
日期：2012-02-08
 --%>	

<div id="customContent">
<pg:empty actual="${datas}" >
	<div class="nodata">
	<img src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>"/></div>
</pg:empty> 
<pg:notempty actual="${datas}">
   <pg:pager scope="request"  data="datas" desc="false" isList="false" containerid="custombackContainer" selector="customContent">
	<pg:param name="bm"/>
	<pg:param name="app_name_en"/>
	<pg:param name="app_name"/>
	<pg:param name="soft_level"/>
	<pg:param name="state"/>
	<pg:param name="rd_type"/>
	<!-- 加入 class="tableOutline" 可控制表格宽度，滚动条展示 -->
	<div id="changeColor" >
	 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
        <pg:header>
            <th align=center><input id="CKA" name="CKA" type="checkbox" 
							onClick="checkAll('CKA','CK')"></th>
			
       		<pg:title sort="true" type="th" align="center" colName="bm" titlecode="sany.appbom.bm"/>
       		<th><pg:message code="sany.appbom.softInitial"/></th>
       		<th><pg:message code="sany.appbom.softName"/></th>
       		<th><pg:message code="sany.appbom.applyDomain"/></th>
       		<th><pg:message code="sany.appbom.functionDescribe"/></th>
       		<th><pg:message code="sany.appbom.level"/></th>
       		<th><pg:message code="sany.appbom.state"/></th>
       		<th><pg:message code="sany.appbom.rdType"/></th>
       		<th><pg:message code="sany.appbom.structMode"/></th>
       		<th><pg:message code="sany.appbom.softLanguage"/></th>
       		<th><pg:message code="sany.appbom.softTool"/></th>
       		<th><pg:message code="sany.pdp.common.operation"/></th>
       	</pg:header>	
      <pg:list>

   		<tr onDblClick="viewBom('<pg:cell colName="id" />')">
   		        <td class="td_center">
                    <input id="CK" type="checkbox" name="CK" onClick="checkOne('CKA','CK')" value="<pg:cell colName="id" />"/>
                    <input id="id" type="hidden" name="id" value="<pg:cell colName="id" />"/></td>
                <td><pg:rowid increament="1" offset="false"/><pg:cell colName="bm"/></td>
                <td><span class="toolTip" title="<pg:cell colName="app_name_en"/>"><pg:cell colName="app_name_en" maxlength="10" replace="..."/></span></td>
                <td><a href="javascript:void(0)" onclick="viewBom('<pg:cell colName="id" />')"><pg:cell colName="app_name" maxlength="8" replace="..."/></a></td>
        		<td><pg:cell colName="apply_domain" /></td>       
                <td><span class="toolTip" title="<pg:cell colName="description"/>"><pg:cell colName="description" maxlength="8" replace="..."/></span></td>  
           		<td>
           		<pg:case  colName="soft_level">
	           		<pg:equal value="1">一级</pg:equal>
	                <pg:equal value="2">二级</pg:equal>
	                <pg:equal value="3">三级</pg:equal>
	                <pg:equal value="10">待定</pg:equal>
                </pg:case>
                </td>	   
           		<td>
           			<pg:case  colName="state">
                        <pg:equal value="1">在用</pg:equal>
                        <pg:equal value="2">试运行</pg:equal>
                        <pg:equal value="3">升级研发中</pg:equal>
                        <pg:equal value="4">停用</pg:equal>
                        <pg:equal value="5">暂用</pg:equal>
                        <pg:equal value="6">研发中</pg:equal>
                        <pg:equal value="7">试验中</pg:equal>
                      </pg:case>   
                </td>	   	  
           		<td>
           		<pg:case  colName="rd_type">
           				<pg:equal value="1">自研</pg:equal>
                		<pg:equal value="2">外购</pg:equal>
                        <pg:equal value="3">免费</pg:equal>
                        <pg:equal value="4">试用</pg:equal>
                        <pg:equal value="5">外购+定制</pg:equal>
                 </pg:case>          
                </td>		  
                <td><pg:cell colName="struct_mode" maxlength="8" replace="..."/></td>	  
           		<td><pg:cell colName="soft_language" maxlength="8" replace="..."/></td>  
           		<td><pg:cell colName="develop_tool" maxlength="10" replace="..."/></td>  
                <td class="td_center"><a href="javascript:void(0)" id="editBom" onclick="editBom('<pg:cell colName="id" />')"><pg:message code="sany.pdp.common.operation.update"/></a><a href="javascript:void(0)" id="delBom" onclick="delBom('<pg:cell colName="id" />')"><pg:message code="sany.pdp.common.operation.delete"/></a></td>    
                 
        </tr>
	 </pg:list>
    </table>
    </div>
	<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="10" sizescope="5,10,20,50,100"/></div>

    </pg:pager>
    </pg:notempty>
</div>		
