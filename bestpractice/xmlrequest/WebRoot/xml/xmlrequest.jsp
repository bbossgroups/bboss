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
		xmlhttp.open("POST","echoxml.page");
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
	function sendjson()
	{		
		var jsonhttp =null;
		if(window.ActiveXObject)
		{
			jsonhttp = new ActiveXObject("Microsoft.XMLHTTP");			
		}
		else if(window.XMLHttpRequest)
		{
			jsonhttp = new XMLHttpRequest();			
		}
		else
		{			
			return;
		}
		var json = $("#sendjson").val();
		jsonhttp.open("POST","echojson.page");
		jsonhttp.setRequestHeader("Content-Length",json.length);
		jsonhttp.setRequestHeader("CONTENT-TYPE","application/json;charset=UTF-8");
		jsonhttp.onreadystatechange=function(){
			if(jsonhttp.readyState == 4)
			{	
				$("#jsonresult").val(jsonhttp.responseText);
			}
		}
		jsonhttp.send(json);
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
			  <textarea rows="5" cols="100" id="sendxml"><org.frameworkset.web.xml.XMLRequestController_-XMLBean>
  <id>15284b36-3404-4bf8-8f14-c2114f2d97fb</id>
  <data>国产j2ee框架 bboss</data>
</org.frameworkset.web.xml.XMLRequestController_-XMLBean></textarea>
			  </td></tr>
			  <tr><td>
			  来自服务器的xml响应： 			  
			  </td></tr>
			  <tr><td>			   
			  <textarea rows="5" cols="100" id="xmlresult"></textarea>
			  </td></tr>
			  </table>
		</span>	
		
		<span id="checkboxtreecontainer">
		<table><tr><td>
			   请输入要发送的json内容：<input type="button" name="send" value="点击发送" onclick="sendjson()">			  
			  </td></tr>
			  <tr><td>			   	
			  <textarea rows="5" cols="100" id="sendjson">{"id":"15284b36-3404-4bf8-8f14-c2114f2d97fb","data":"国产j2ee框架 bboss"}</textarea>
			  </td></tr>
			  <tr><td>
			  来自服务器的json响应： 			  
			  </td></tr>
			  <tr><td>			   
			  <textarea rows="5" cols="100" id="jsonresult"></textarea>
			  </td></tr>
			  </table>
		</span>			
	</body>