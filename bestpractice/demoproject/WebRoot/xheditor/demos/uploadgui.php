<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
<html xmlns="http://www.w3.org/1999/xhtml"> 
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" /> 
<title>文件上传</title>
<style>
body{
	padding:5px;
	margin:0px;
	font-size:12px;
}	
</style>
<script type="text/javascript">
//----------------跨域支持代码开始(非跨域环境请删除这段代码)----------------
var JSON = JSON || {};
JSON.stringify = JSON.stringify || function (obj) {
	var t = typeof (obj);
	if (t != "object" || obj === null) {
		if (t == "string")obj = '"'+obj.replace(/(["\\])/g,'\\$1')+'"';
		return String(obj);
	}
	else { 
		var n, v, json = [], arr = (obj && obj.constructor == Array);
		for (n in obj) {
			v = obj[n]; t = typeof(v);
			if (t == "string") v = '"'+v.replace(/(["\\])/g,'\\$1')+'"';
			else if (t == "object" && v !== null) v = JSON.stringify(v);
			json.push((arr ? "" : '"' + n + '":') + String(v));
		}
		return (arr ? "[" : "{") + String(json) + (arr ? "]" : "}");
	}  
};
var callback = callback || function(v){
	v=JSON.stringify(v);
	window.name=escape(v);
	window.location='http://'+location.search.match(/[\?&]parenthost=(.*?)(&|$)/i)[1]+'/xheditorproxy.html';//这个文件最好是一个0字节文件，如果无此文件也会正常工作	
}
var unloadme =  unloadme || function(){
	callback(null);//返回null，直接关闭当前窗口
}
//----------------跨域支持代码结束----------------


function upload(){
	callback('test1.zip');//非跨域允许直接传输JSON对象
}
</script>
</head> 
<body> 
<a href="javascript:void(0);" onclick="upload();return false;">点击这里直接返回文件URL</a> | <a href="javascript:void(0);" onclick="unloadme();return false;">点击这里关闭模式窗口</a><br />
<p style="color:#999;">注：此页面仅供演示用，您可以在此页面的基础上实现文件上传和用户文件浏览功能<br />本页面提供两个接口用来和编辑器进行互动：callback和unloadme，callback用来返回上传的地址，唯一返回参数就是返回值，unloadme方法用来关闭当前模式窗口，无任何参数</p>
<form id="frmUpload" method="post" action="uploadguiupload.php">
<input type="hidden" name="parenthost" value="<?php echo $_GET['parenthost'];?>" />
<input type="submit" name="save" value="点击这里提交上传表单" />
</form>
</body> 
</html>