<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>xhEditor demo10 : showIframeModal接口的iframe文件上传</title>
<link rel="stylesheet" href="common.css" type="text/css" media="screen" />
<script type="text/javascript" src="../jquery/jquery-1.4.4.min.js"></script>
<script type="text/javascript" src="../xheditor-1.1.13-zh-cn.min.js"></script>
<script type="text/javascript">
$(pageInit);
function pageInit()
{
	$.extend(xheditor.settings,{skin:'vista',shortcuts:{'ctrl+enter':submitForm}});//修改默认设置
	$('#elm1').xheditor({upLinkUrl:"!<%=request.getContextPath()%>/file/upload.page",upImgUrl:"!<%=request.getContextPath()%>/file/upload.page",upFlashUrl:"!<%=request.getContextPath()%>/file/upload.page",upMediaUrl:"!<%=request.getContextPath()%>/file/upload.page"});
	$('#elm2').xheditor({upLinkUrl:"!{editorRoot}xheditor_plugins/multiupload/multiupload.html?uploadurl=<%=request.getContextPath()%>/file/upload.page%3Fimmediate%3D1&ext=附件文件(*.zip;*.rar;*.txt)",upImgUrl:'!{editorRoot}xheditor_plugins/multiupload/multiupload.html?uploadurl=<%=request.getContextPath()%>/file/upload.page%3Fimmediate%3D1&ext=图片文件(*.jpg;*.jpeg;*.gif;*.png)',upFlashUrl:'!{editorRoot}xheditor_plugins/multiupload/multiupload.html?uploadurl=<%=request.getContextPath()%>/file/upload.page%3Fimmediate%3D1&ext=Flash动画(*.swf)',upMediaUrl:'!{editorRoot}xheditor_plugins/multiupload/multiupload.html?uploadurl=<%=request.getContextPath()%>/file/upload.page%3Fimmediate%3D1&ext=多媒体文件(*.wmv;*.avi;*.wma;*.mp3;*.mid)'});
}
function submitForm(){$('#frmDemo').submit();}
</script>
</head>
<body>
<div id="header-nav">
	<ul>
		<li><a href="demo01.html"><span>默认模式</span></a></li>
		<li><a href="demo02.html"><span>自定义按钮</span></a></li>
		<li><a href="demo03.html"><span>皮肤选择</span></a></li>
		<li><a href="demo04.html"><span>其它选项</span></a></li>
		<li><a href="demo05.html"><span>API交互</span></a></li>
		<li><a href="demo06.html"><span>非utf-8编码调用</span></a></li>
		<li><a href="demo07.html"><span>UBB可视化</span></a></li>
		<li><a href="demo08.html"><span>Ajax上传</span></a></li>
		<li><a href="demo09.html"><span>插件扩展</span></a></li>
		<li><a href="demo10.html"><span>iframe调用上传</span></a></li>
		<li><a href="demo11.html"><span>异步加载</span></a></li>
		<li><a href="demo12.html"><span>远程抓图</span></a></li>
		<li><a href="../wizard.html" target="_blank"><span>生成代码</span></a></li>
	</ul>
</div>
<form id="frmDemo" method="post" action="show.php">
	<h3>xhEditor demo10 : showIframeModal接口的iframe文件上传</h3>
	1,iframe单文件上传演示:<br />
	<textarea id="elm1" name="elm1" rows="12" cols="80" style="width: 90%">
&lt;p&gt;当前实例调用的Javascript源代码为：&lt;/p&gt;&lt;p&gt;$('#elm1').xheditor({skin:'vista',upLinkUrl:"!uploadgui.php",upImgUrl:"!uploadgui.php",upFlashUrl:"!uploadgui.php",upMediaUrl:"!uploadgui.php"});&lt;/p&gt;&lt;p&gt;&lt;br /&gt;&lt;/p&gt;&lt;p&gt;本页面演示通过showIframeModal接口来调用iframe调用指定的文件上传页来上传和管理文件，可以实现高可定义的文件上传和管理，当前示例页面中在“&lt;strong&gt;超链接、图片、Flash动画和视频&lt;/strong&gt;”按钮中实现了演示。&lt;/p&gt;<BR>&lt;p&gt;此功能可在以下4个按钮中通过参数调用来实现：超链接、图片、动画和视频，接口分别为：upLinkUrl、upImgUrl、upFlashUrl和upMediaUrl，默认这几个函数为编辑器内置的ajax式文件上传，想要在frame中调用自定义上传管理页面，必需在参数值最前面添加感叹号：“!”，注意必需为半角符号。例如：upLinkUrl:'!gui.html'&lt;/p&gt;<BR>&lt;p&gt;上传管理页面可使用接口：callback，callback用来返回上传或者选择的文件链接并关闭模式窗口，您可以查看uploadgui.php页面来了解具体的管理页面制作示例&lt;/p&gt;
	</textarea><br /><br />
	2,iframe多文件批量上传演示:<br />
	<textarea id="elm2" name="elm2" rows="12" cols="80" style="width: 90%">
&lt;p&gt;当前实例调用的Javascript原代码为：&lt;/p&gt;&lt;p&gt;$('#elm2').xheditor({skin:'vista',upImgUrl:'!{editorRoot}xheditor_plugins/multiupload/multiupload.html?uploadurl={editorRoot}demos/upload.php%3Fimmediate%3D1&amp;amp;ext=图片文件(*.jpg;*.jpeg;*.gif;*.png)',upFlashUrl:'!{editorRoot}xheditor_plugins/multiupload/multiupload.html?uploadurl={editorRoot}demos/upload.php%3Fimmediate%3D1&amp;amp;ext=Flash动画(*.swf)',upMediaUrl:'!{editorRoot}xheditor_plugins/multiupload/multiupload.html?uploadurl={editorRoot}demos/upload.php%3Fimmediate%3D1&amp;amp;ext=多媒体文件(*.wmv;*.avi;*.wma;*.mp3;*.mid)'});&lt;/p&gt;&lt;p&gt;&lt;br /&gt;&lt;/p&gt;&lt;p&gt;本演示仅是在上面的iframe单文件演示基础上的更进一步应用。多文件批量接口可在：&lt;strong&gt;超链接、图片&lt;/strong&gt;、&lt;strong&gt;Flash动画&lt;/strong&gt;和&lt;strong&gt;多媒体&lt;/strong&gt;中使用，使用的方法仅需将多个URL地址用制表符(\t )来分隔，例如：1.gif 2.gif 3.gif&lt;/p&gt;&lt;p&gt;本演示利用showIframeModal接口来调用批量上传页面，批量上传页是结合了开源的&lt;strong&gt;swfupload&lt;/strong&gt;组件来实现的，可实现高可定制的批量上传。而界面是参考了&lt;strong&gt;SwfUploadPanel&lt;/strong&gt;组件，在此一并感谢。批量上传演示页的代码并没有太多的整理和优化，因此建议大家使用时再自己另行修改和开发，此模块仅供参考。&lt;br /&gt;&lt;/p&gt;&lt;p&gt;注：批量上传演示页默认使用了立即上传模式，上传完成便会自动插入到编辑器中。&lt;br /&gt;&lt;/p&gt;&lt;p&gt;感谢名单：&lt;/p&gt;&lt;ol&gt;&lt;li&gt;&lt;a target=&quot;_blank&quot; href=&quot;http://swfupload.org/&quot;&gt;http://swfupload.org/&lt;/a&gt;&lt;/li&gt;&lt;li&gt;&lt;a target=&quot;_blank&quot; href=&quot;http://www.extjs.com/learn/Extension:SwfUploadPanel&quot;&gt;http://www.extjs.com/learn/Extension:SwfUploadPanel&lt;/a&gt;&lt;/li&gt;&lt;/ol&gt;
	</textarea>
	<br/><br />
	<input type="submit" name="save" value="Submit" />
	<input type="reset" name="reset" value="Reset" />
</form>
</body>
</html>