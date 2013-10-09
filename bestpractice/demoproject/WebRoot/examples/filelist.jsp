<%@ page language="java" import="java.util.*,java.io.*,java.text.SimpleDateFormat" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
   
    
    <title>资源下载列表</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<link rel="shortcut icon"
		href="${pageContext.request.contextPath}/css/favicon.gif">
	<link rel="stylesheet"
		href="${pageContext.request.contextPath}/css/classic/tables.css"
		type="text/css">
	<link rel="stylesheet"
		href="${pageContext.request.contextPath}/css/classic/main.css"
		type="text/css">
	<link rel="stylesheet"
		href="${pageContext.request.contextPath}/css/classic/mainnav.css"
		type="text/css">
	<link rel="stylesheet"
		href="${pageContext.request.contextPath}/css/classic/messages.css"
		type="text/css">
	<link rel="stylesheet"
		href="${pageContext.request.contextPath}/css/classic/tooltip.css"
		type="text/css">
  </head>
  
  <body> 
     <table class="genericTbl">
       <tr>
       <th class="order1 sorted">
            文件名
       </th>
       <th class="order1 sorted">
          文件路径
       </th>
       <th class="order1 sorted">
          文件大小(单位:byte)
       </th>
       <th class="order1 sorted">
          修改时间
       </th>
       
       </tr>
       <pg:list requestKey="files">
       <tr class="even">
         <td >    
            <pg:cell colName="name"/>
            
         </td>
         <td >
            <pg:cell colName="uri"/>
         </td>
         <td >
            <pg:cell colName="size"/>
         </td>
         <td >
            <pg:cell colName="modifyTime" dateformat="yyyy-MM-dd HH:mm:ss"/>
         </td>
         
         
        </tr>
        </pg:list>
     </table>
  
  
  </body>
</html>
