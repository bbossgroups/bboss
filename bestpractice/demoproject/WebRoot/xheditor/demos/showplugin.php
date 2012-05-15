<?php
//此程序为demo09的服务端显示演示程序
header('Content-Type: text/html; charset=utf-8');
$sHtml=$_POST['elm1'];
function fixPre($match)
{
	$match[2]=preg_replace('/<br\s*\/?>/i',"\r\n",$match[2]);
	$match[2]=preg_replace('/<\/?[\w:]+(\s+[^>]+?)?>/i',"",$match[2]);//去除所有HTML标签
	return $match[1].$match[2].$match[3];
}
$sHtml=preg_replace_callback('/(<pre(?:\s+[^>]*?)?>)([\s\S]+?)(<\/pre>)/i','fixPre',$sHtml);
?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>demo09插件显示测试页</title>
<style type="text/css">
body{margin:5px;border:2px solid #ccc;padding:5px;font:12px tahoma,arial,sans-serif;line-height:1.2}
</style>
<link type="text/css" rel="stylesheet" href="prettify/prettify.css"/>
<script type="text/javascript" src="prettify/prettify.js"></script>
<body>
	<?php echo $sHtml?>
	<script type="text/javascript">prettyPrint();</script>
</body>
</html>