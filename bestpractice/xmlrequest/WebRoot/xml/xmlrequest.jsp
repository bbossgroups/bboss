<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>发送xml数据demo</title>
<script type="text/javascript"> 
	function sendxml()
	{		
		var xmlhttp =null;
		if(window.ActiveXObject)
		{
			xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");			
		}
		else if(window.XMLHttpRequest)
		{
			xmlhttp = new XMLHttpRequest();			
		}
		else
		{			
			return;
		}
		var xml = $("#sendxml").val();
		xmlhttp.open("POST","echo.page");
		xmlhttp.setRequestHeader("Content-Length",xml.length);
		xmlhttp.setRequestHeader("CONTENT-TYPE","text/xml;charset=UTF-8");
		xmlhttp.onreadystatechange=function(){
			if(xmlhttp.readyState == 4)
			{	
				$("#xmlresult").val(xmlhttp.responseText);
			}
		}
		xmlhttp.send(xml);
	}
	</script>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<script type="text/javascript" src="<%=request.getContextPath() %>/include/jquery-1.4.4.min.js"></script>		
	</head>	
		<span id="checkboxtreecontainer">
		<table><tr><td>
			   请输入要发送的xml内容：<input type="button" name="send" value="点击发送" onclick="sendxml()">			  
			  </td></tr>
			  <tr><td>			   	
			  <textarea rows="5" cols="100" id="sendxml"><web><version>国产j2ee框架 bbossgroups 3.2</version></web></textarea>
			  </td></tr>
			  <tr><td>
			  来自服务器的xml响应： 			  
			  </td></tr>
			  <tr><td>			   
			  <textarea rows="5" cols="100" id="xmlresult"></textarea>
			  </td></tr>
			  </table>
		</span>			
	</body>