<%@page import="com.frameworkset.util.StringUtil"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Session详情</title>
<%@ include file="/include/css.jsp"%>

<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/include/syntaxhighlighter/styles/SyntaxHighlighter.css"></link>
<script language="javascript" src="${pageContext.request.contextPath}/include/syntaxhighlighter/shCore.js"></script>
<script language="javascript" src="${pageContext.request.contextPath}/include/syntaxhighlighter/shBrushJava.js"></script>
<script language="javascript" src="${pageContext.request.contextPath}/include/syntaxhighlighter/shBrushXml.js"></script>
<script language="javascript" src="${pageContext.request.contextPath}/include/syntaxhighlighter/shBrushJScript.js"></script>

<body>
<div class="form">
	<div class="mcontent">
		<div id="searchblock">
			<form id="viewForm" name="viewForm">
				<pg:beaninfo requestKey="sessionInfo">
				
					<fieldset >
						<legend><strong>基本信息</strong></legend>
						
						<table border="0" cellpadding="0" cellspacing="0" class="table4" >
							<tr>
								<th width="60"><strong>SessionID:</strong></th>
								<td width="300"><pg:cell colName="sessionid" /></td>
								<th width="100"><strong>创建时间:</strong></th>
								<td width="150"><pg:cell colName="creationTime" dateformat="yyyy-MM-dd HH:mm:ss"/></td>
								<th width="100"><strong>最后访问时间:</strong></th>
								<td width="150"><pg:cell colName="lastAccessedTime" dateformat="yyyy-MM-dd HH:mm:ss"/></td>
								<th width="100"><strong>客户端:</strong></th>
								<td width="200"><pg:cell colName="referip"/></td>
							</tr>
							<tr>
								<th width="60"><strong>服务端:</strong></th>
								<td width="300"><pg:cell colName="host" /></td>
								<th width="100"><strong>有效期:</strong></th>
								<td width="150"><pg:cell colName="maxInactiveInterval" /></td>
								<th width="100"><strong>失效时间:</strong></th>
								<td width="150"><pg:cell colName="loseTime" dateformat="yyyy-MM-dd HH:mm:ss"/></td>
								<th width="100"><strong>状态:</strong></th>
								<td width="150">
									<pg:true colName="validate">有效</pg:true>
						       		<pg:false colName="validate">无效</pg:false>
					       		</td>
							</tr>
							<tr>
								<th width="60"><strong>Cookie HttpOnly:</strong></th>
								<td width="300" ><pg:true colName="httpOnly">启用</pg:true>
	       		<pg:false colName="httpOnly"><span style=" color: red;">关闭</span></pg:false></td>	
								<th width="60"><strong>Cookie Secure:</strong></th>
								<td width="300" colspan="10"><pg:true colName="secure">启用</pg:true>
	       		<pg:false colName="secure"><span style=" color: red;">关闭</span></pg:false></td>									
							</tr>
							<tr>
								<th width="60"><strong>请求地址:</strong></th>
								<td width="300" ><pg:cell colName="requesturi" /></td>	
								<th width="60"><strong>上次访问地址:</strong></th>
								<td width="300" colspan="10"><pg:cell colName="lastAccessedUrl" /></td>									
							</tr>
							<tr>
							
								<th width="60"><strong>上次访问主机IP:</strong></th>
								<td width="300" colspan="10"><pg:cell colName="lastAccessedHostIP"/></td>									
							</tr>
							
						</table>
					</fieldset>
				
					<fieldset >
						<legend><strong>属性列表</strong></legend>
						<div class="shadow">
						<%if(!StringUtil.isIE(request)){ %>
						<div class='info'><p><div id='detail'>
							<table border='1' cellspacing='0' width='100%' id='resultsTable'><tbody>
							<pg:empty actual="${sessionInfo.attributes}" >
								<tr><td colspan="2">没有session属性</td></tr>
							</pg:empty> 
							<pg:notempty actual="${sessionInfo.attributes}" >
								<pg:map actual='${sessionInfo.attributes}'>
									<tr><td ><pg:mapkey/></td><td><pre name='code' class='xml'><pg:cell htmlEncode='true'/></pre></td></tr>
								</pg:map>
							</pg:notempty> 	
						</tbody></table></div></p></div>
						<%}else{ %>
							<table border='1' cellpadding='0' cellspacing='0' class='table2' width='100%'>
							<pg:empty actual="${sessionInfo.attributes}" >
								<tr><td colspan="2">没有session属性</td></tr>
							</pg:empty> 
							<pg:notempty actual="${sessionInfo.attributes}" >
								<pg:map actual='${sessionInfo.attributes}'>
									<tr height='60px'><td ><pg:mapkey/></td> 
										<td><textarea rows='8' cols='50'  class='w120'  
									           style='width: 800px;font-size: 12px;height:60px;' ><pg:cell htmlEncode='true'/></textarea>
									</td></tr>
								</pg:map>
							</pg:notempty> 	
							
							</table>
						<%} %>
						</div>
					</fieldset>
				</pg:beaninfo>
			</form>
		</div>
  	</div>	
  		
	<div class="btnarea" >
		
	</div>	
</div>
</body>
<script type="text/javascript">

var trHtml ="";

if(!$.browser.msie) {
		
	dp.SyntaxHighlighter.ClipboardSwf = '${pageContext.request.contextPath}/include/syntaxhighlighter/clipboard.swf';
	dp.SyntaxHighlighter.HighlightAll('code');
}else {
	
	
}	

</script>

</script>
</head>
</html>
