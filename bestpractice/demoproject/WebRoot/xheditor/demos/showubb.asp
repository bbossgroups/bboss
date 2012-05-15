<%@ CODEPAGE=65001 %>
<!--#include file="../serverscript/asp/ubb2html.asp"-->
<%
'此程序为UBB模式下的服务端显示测试程序
Response.Charset="UTF-8"
dim sHtml
sHtml=ubb2html(request("elm1"))'Server.HTMLEncode()
sHtml=showCode(sHtml)
sHtml=showFlv(sHtml)
%><script language="javascript" runat="server">
function showCode(sHtml)
{
	sHtml=sHtml.replace(/\[code\s*(?:=\s*((?:(?!")[\s\S])+?)(?:"[\s\S]*?)?)?\]([\s\S]*?)\[\/code\]/ig,function(all,t,c){//code特殊处理
		t=t.toLowerCase();
		if(!t)t='plain';
		c=c.replace(/[<>]/g,function(c){return {'<':'&lt;','>':'&gt;'}[c];});
		return '<pre class="prettyprint lang-'+t+'">'+c+'</pre>';
	});
	return sHtml;
}
function showFlv(sHtml)
{
	sHtml=sHtml.replace(/\[flv\s*(?:=\s*(\d+)\s*,\s*(\d+)\s*)?\]\s*(((?!")[\s\S])+?)(?:"[\s\S]*?)?\s*\[\/flv\]/ig,function(all,w,h,url){
		if(!w)w=480;if(!h)h=400;
		return '<embed type="application/x-shockwave-flash" src="mediaplayer/player.swf" wmode="transparent" allowscriptaccess="always" allowfullscreen="true" quality="high" bgcolor="#ffffff" width="'+w+'" height="'+h+'" flashvars="file='+url+'" />';
	});
	return sHtml;
}
</script><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>UBB文章显示测试页</title>
<style type="text/css">
body{margin:5px;border:2px solid #ccc;padding:5px;font:12px tahoma,arial,sans-serif;line-height:1.2}
</style>
<link type="text/css" rel="stylesheet" href="prettify/prettify.css"/>
<script type="text/javascript" src="prettify/prettify.js"></script>
<body>
	<%=sHtml%>
	<script type="text/javascript">prettyPrint();</script>
</body>
</html>