<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title>文件管理</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<pg:config enablecontextmenu="false"/>
	<script type="text/javascript" src="<%=path %>/include/jquery.form.js"></script>
	<link href="<%=path %>/common/css/table.css" rel="stylesheet" type="text/css" />
	<link href="<%=path %>/common/css/input.css" rel="stylesheet" type="text/css" />
	<link href="<%=path %>/common/css/button.css" rel="stylesheet" type="text/css" />

	<link href="<%=path %>/common/css/jf.css" rel="stylesheet" type="text/css" />
	<link href="<%=path %>/common/css/grxx.css" rel="stylesheet" type="text/css" />
	
	<script type="text/javascript">
		function uploadFile()
		{
			var url = "<%=path %>/upload/uploadFileWithMultipartFiles.page";
			var fm = document.getElementById("fm");
			fm.action = url;
			fm.enctype = "multipart/form-data";
			fm.target = "queryDown";
			fm.method = "GET";
			fm.submit();
		} 
		function jqFormUploadFile()
		{
			$("#fm").form('submit',{url:"<%=path %>/upload/uploadFileWithMultipartFilesJson.page",
				
				success:function(data)
				{
					$("#downfile_div").html(data);
				}
			});
		}
		
	</script>
  </head>
  
  
  <body>
  	<div style="width: 100%;">
  		<center>
	  		<table width="80%" border="0" cellspacing="0" cellpadding="0" class="bgcolor2">
	  			<tr class="jfxl3">
	  				<td width="50%" align="center">文件上传</td>
	  				<td width="50%" align="center">文件下载</td>
	  			</tr>
	  			<tr>
	  				<td align="center">
	  					<form id="fm" action="" method="post" enctype="multipart/form-data">
	  						<table>
	  							<tr><td>
						  			<input type="file" name="upload1">
	  							</td></tr>
	  							<tr><td>
						  			<input type="file" name="upload1">
	  							</td></tr>
	  							<tr><td align="center">
	  								<input type="button" value="开始上传" onclick="jqFormUploadFile()" />
	  							</td></tr>
	  						</table>
				  		</form>
	  				</td>
	  				<td align="center">
	  					<div id="downfile_div">
	  					
	  					</div>
	  				</td>
	  			</tr>
	  		</table>
  		</center>
  	</div>
  </body>
</html>
