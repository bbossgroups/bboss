<%@ page language="java"  pageEncoding="utf-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>

<%--
描述：SessionList
作者：谭湘
版本：1.0
日期：2014-06-04
 --%>	

<div id="customContent">
<pg:empty actual="${sessionList}" >
	<div class="nodata">
	没有session数据</div>
</pg:empty> 
<pg:notempty actual="${sessionList}" >

   <pg:pager scope="request"  data="sessionList" desc="true" isList="false" containerid="sessionContainer" selector="customContent">
	<pg:param name="sessionid"/>
	<pg:param name="appkey"/>
	<pg:param name="referip"/>
	<pg:param name="createtime_start"/>
	<pg:param name="createtime_end"/>
	<pg:param name="host"/>
	<pg:param name="validate"/>
	<!-- 加入 class="tableOutline" 可控制表格宽度，滚动条展示 -->
	<div id="changeColor">
	 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
        <pg:header>
            <th align=center><input id="CKA" name="CKA" type="checkbox" 
							onClick="checkAll('CKA','CK')"></th>
			<th>SessionID</th>
       		<th>创建时间</th>
       		<th>最后访问时间</th>
       		<th>失效时间</th>
       		<th>请求地址</th>
       		<th>上次访问地址</th>
       		<th>上次访问主机IP</th>
       		<th>客户端IP</th>
       		<th>初始服务端IP</th>
       		<th>有效期</th>       		
       		<th>状态</th>
       		<th>httpOnly</th>
       		<th>secure</th>
       		<th>操作</th>
       	</pg:header>	

      <pg:list >
   		<tr >
	        <td class="td_center">
                <input id="CK" type="checkbox" name="CK" onClick="checkOne('CKA','CK')" value="<pg:cell colName="sessionid" />"/>
            </td>
    		<td><pg:cell colName="sessionid" /></td> 
    		<td><pg:cell colName="creationTime" dateformat="yyyy-MM-dd HH:mm:ss"/></td>
    		<td><pg:cell colName="lastAccessedTime" dateformat="yyyy-MM-dd HH:mm:ss"/></td>       
       		<td><pg:cell colName="loseTime" dateformat="yyyy-MM-dd HH:mm:ss"/></td>
       		<td><pg:cell colName="requesturi"/></td>
       		<td ><pg:cell colName="lastAccessedUrl"/></td>
       		<td><pg:cell colName="lastAccessedHostIP"/></td>
       		<td><pg:cell colName="referip"/></td>
       		<td><pg:cell colName="host" /></td>
       		<td><pg:cell colName="maxInactiveInterval" /></td>
       		<td>
	       		<pg:true colName="validate">有效</pg:true>
	       		<pg:false colName="validate"><span style=" color: red;">无效</span></pg:false>
       		</td>
       		<td>
	       		<pg:true colName="httpOnly">启用</pg:true>
	       		<pg:false colName="httpOnly"><span style=" color: red;">关闭</span></pg:false>
       		</td>
       		<td>
	       		<pg:true colName="secure">启用</pg:true>
	       		<pg:false colName="secure"><span style=" color: red;">关闭</span></pg:false>
       		</td>
            <td class="td_center">
            	<a href="javascript:void(0)" id="viewTaskDetailInfo" onclick="delSession('<pg:cell colName="sessionid" />')">删除</a>|
            	<a href="javascript:void(0)" id="viewTaskDetailInfo" onclick="sessionInfo('<pg:cell colName="sessionid" />')">详情</a>
            </td>    
        </tr>
	 </pg:list>
    </table>
    </div>
	<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="10,20,50,100"/></div>

    </pg:pager>
   </pg:notempty> 

</div>		
