<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>xhEditor demo8 : Ajax文件上传</title>
<link rel="stylesheet" href="common.css" type="text/css" media="screen" />
<script type="text/javascript" src="../jquery/jquery-1.4.4.min.js"></script>
<script type="text/javascript" src="../xheditor-1.1.13-zh-cn.min.js"></script>
<script type="text/javascript">
$(pageInit);
function pageInit()
{
	$.extend(xheditor.settings,{shortcuts:{'ctrl+enter':submitForm}});
	$('#elm1').xheditor({upLinkUrl:"<%=request.getContextPath()%>/file/upload.page",upLinkExt:"zip,rar,txt",upImgUrl:"<%=request.getContextPath()%>/file/uploadwithbean.page?testparam=中文",upImgExt:"jpg,jpeg,gif,png",upFlashUrl:"<%=request.getContextPath()%>/file/upload.page",upFlashExt:"swf",upMediaUrl:"<%=request.getContextPath()%>/file/upload.page",upMediaExt:"wmv,avi,wma,mp3,mid"});
	$('#elm2').xheditor({upLinkUrl:"<%=request.getContextPath()%>/file/upload.page?immediate=1",upLinkExt:"zip,rar,txt",upImgUrl:"<%=request.getContextPath()%>/file/upload.page?testparam=test&immediate=1",upImgExt:"jpg,jpeg,gif,png",upFlashUrl:"<%=request.getContextPath()%>/file/upload.page?immediate=1",upFlashExt:"swf",upMediaUrl:"<%=request.getContextPath()%>/file/upload.page?immediate=1",upMediaExt:"wmv,avi,wma,mp3,mid"});
	$('#elm3').xheditor({upLinkUrl:"<%=request.getContextPath()%>/file/upload.page",upLinkExt:"zip,rar,txt"});
	$('#elm4').xheditor({upImgUrl:"<%=request.getContextPath()%>/file/upload.page",upImgExt:"jpg,jpeg,gif,png"});
	$('#elm5').xheditor({upFlashUrl:"<%=request.getContextPath()%>/file/upload.page",upFlashExt:"swf",upMediaUrl:"<%=request.getContextPath()%>/file/upload.page",upMediaExt:"wmv,avi,wma,mp3,mid"});
	$('#elm6').xheditor({upLinkUrl:"<%=request.getContextPath()%>/file/upload.page",upLinkExt:"zip,rar,txt",upImgUrl:"<%=request.getContextPath()%>/file/upload.page",upImgExt:"jpg,jpeg,gif,png",onUpload:insertUpload});
}
function insertUpload(arrMsg)
{
	var i,msg;
	for(i=0;i<arrMsg.length;i++)
	{
		msg=arrMsg[i];
		$("#uploadList").append('<option value="'+msg.id+'">'+msg.localname+'</option>');
	}
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
	<h3>xhEditor demo8 : Ajax文件上传</h3>
	1,普通上传模式:<br />
	<textarea id="elm1" name="elm1" rows="12" cols="80" style="width: 80%">
&lt;p&gt;当前实例调用的Javascript源代码为：&lt;/p&gt;&lt;p&gt;$('#elm1').xheditor({upLinkUrl:"upload.php",upLinkExt:"zip,rar,txt",upImgUrl:"upload.php",upImgExt:"jpg,jpeg,gif,png",upFlashUrl:"upload.php",upFlashExt:"swf",upMediaUrl:"upload.php",upMediaExt:"avi"});&lt;/p&gt;&lt;p&gt;&lt;br /&gt;&lt;/p&gt;&lt;p&gt;请确保当前目录中的upload.php有相应的PHP执行权限，若您使用的是其它的服务器脚本语言，请自行对初始化参数中的upLinkUrl、upImgUrl、upFlashUrl和upMediaUrl进行修改，并开发相应服务器上传接收程序。&lt;/p&gt;注：upload.php仅为演示代码，若您希望在自己的项目中实际使用，请自行修改代码或者重新开发，开发过程中请注意上传文件的格式及大小限制，注意服务器安全问题。 &lt;br /&gt;&lt;br /&gt;&lt;strong&gt;上传接收程序开发规范：&lt;br /&gt;&lt;/strong&gt;1,上传文件域名字为：filedata&lt;br /&gt;2,返回结构必需为json，并且结构如下：{"err":"","msg":"200906030521128703.gif"}&lt;br /&gt;若上传出现错误，请将错误内容保存在err变量中；若上传成功，请将服务器上的绝对或者相对地址保存在msg变量中。&lt;br /&gt;编辑器若发现返回的err变量不为空，则弹出窗口显示返回的错误内容。&lt;br /&gt;&lt;br /&gt;&lt;br /&gt;&lt;strong&gt;上传管理方案建议：&lt;/strong&gt;&lt;br /&gt;1,在编辑器初始化时在upload.php后面跟上一个服务器生成的绝对唯一的跟踪值，例如：upload.php?infoid=121312121&lt;br /&gt;2,在服务器接收程序中以这个跟踪值保存到数据库中，同样可以限制单个跟踪值下总上传文件数或者总文件大小，否则就是一个可以上传无限个文件的漏洞了&lt;br /&gt;3,最终当前表单提交时，再根据编辑器提交的HTML内容和数据库中上传内容进行比较，删除所有没有使用的上传文件&lt;br /&gt;4,定期由服务器脚本删除上传数据库中没提交的文件记录，这样就能防止别人将您的网站作为免费相册空间了
	</textarea><br /><br />
	2,立即上传模式:<br />
	<textarea id="elm2" name="elm2" rows="12" cols="80" style="width: 80%">
&lt;p&gt;当前实例调用的Javascript源代码为：&lt;/p&gt;&lt;p&gt;$('#elm2').xheditor({upLinkUrl:"upload.php?immediate=1",upLinkExt:"zip,rar,txt",upImgUrl:"upload.php?immediate=1",upImgExt:"jpg,jpeg,gif,png",upFlashUrl:"upload.php?immediate=1",upFlashExt:"swf",upMediaUrl:"upload.php?immediate=1",upMediaExt:"avi"});&lt;/p&gt;&lt;p&gt;&lt;br /&gt;&lt;/p&gt;&lt;p&gt;若返回的地址最前面为&lt;span style="color:#fe2419;"&gt;&lt;strong&gt;半角的感叹号：“!”&lt;/strong&gt;&lt;/span&gt;，表示为&lt;strong&gt;&lt;span style="color:#fe2419;"&gt;立即上传模式&lt;/span&gt;&lt;/strong&gt;，上传成功后不需要点“确定”按钮，随后自动插入到编辑器内容中。&lt;br /&gt;&lt;/p&gt;
	</textarea><br /><br />
	3,带链接文字的附件上传:<br />
	<textarea id="elm3" name="elm3" rows="12" cols="80" style="width: 80%">
&lt;p&gt;当前实例调用的Javascript源代码为：&lt;/p&gt;&lt;p&gt;$('#elm3').xheditor({upLinkUrl:&quot;uploadattach.php&quot;,upLinkExt:&quot;zip,rar,txt&quot;});&lt;/p&gt;&lt;p&gt;&lt;br /&gt;&lt;/p&gt;&lt;p&gt;&lt;span style=&quot;color:#fe2419;&quot;&gt;&lt;strong&gt;带链接文字的附件上传&lt;/strong&gt;&lt;/span&gt;仅可在“超链接”按钮中使用，URL链接和链接文字之间用&lt;span style=&quot;color:#fe2419;&quot;&gt;&lt;strong&gt;&lt;/strong&gt;&lt;/span&gt;&lt;span style=&quot;color:#fe2419;&quot;&gt;&lt;strong&gt;“||”分隔&lt;/strong&gt;&lt;/span&gt;，例如：test.zip||download，前面为下载的URL链接，后面为超链接的文字内容，例如可以是附件的文件名。&lt;/p&gt;&lt;p&gt;特别说明：uploadattach.php是静态内容，仅为演示用，无论上传了什么文件都返回一个演示用的文件。&lt;/p&gt;
	</textarea><br /><br />
	4,缩略图上传模式:<br />
	<textarea id="elm4" name="elm4" rows="12" cols="80" style="width: 80%">
&lt;p&gt;当前实例调用的Javascript源代码为：&lt;/p&gt;&lt;p&gt;$('#elm4').xheditor({upImgUrl:&quot;uploadthumb.php&quot;,upImgExt:&quot;jpg,jpeg,gif,png&quot;});&lt;/p&gt;&lt;p&gt;&lt;br /&gt;&lt;/p&gt;&lt;p&gt;&lt;span style=&quot;color:#fe2419;&quot;&gt;&lt;strong&gt;缩略图模式&lt;/strong&gt;&lt;/span&gt;仅可在“图片”按钮中使用，小图和大图链接之间用&lt;span style=&quot;color:#fe2419;&quot;&gt;&lt;strong&gt;“||”分隔&lt;/strong&gt;&lt;/span&gt;，例如：small.gif||big.html，大图链接可以是图片，也可以是URL网址。&lt;/p&gt;&lt;p&gt;缩略图模式可与多文件插入混合使用，例如：1.gif||1.htm	2.gif||2.html&lt;br /&gt;&lt;/p&gt;&lt;p&gt;特别说明：uploadthumb.php是静态内容，仅为演示用，无论上传了什么图片都返回内置的演示图片文件。&lt;/p&gt;
	</textarea><br /><br />
	5,Flash和多媒体自定高宽上传:<br />
	<textarea id="elm5" name="elm5" rows="12" cols="80" style="width: 80%">
&lt;p&gt;当前实例调用的Javascript源代码为：&lt;/p&gt;&lt;p&gt;$('#elm5').xheditor({upFlashUrl:&quot;uploadembed.php&quot;,upFlashExt:&quot;swf&quot;,upMediaUrl:&quot;uploadembed.php&quot;,upMediaExt:&quot;wmv,avi,wma,mp3,mid&quot;});&lt;/p&gt;&lt;p&gt;&lt;br /&gt;&lt;/p&gt;&lt;p&gt;Flash和多媒体两个模块上传接口的3个参数分别代表：url、宽度、高度，之间用&lt;span style=&quot;color:#ff0000;&quot;&gt;&lt;strong&gt;“||”分隔&lt;/strong&gt;&lt;/span&gt;，例如：test.swf||100||100&lt;br /&gt;&lt;/p&gt;&lt;p&gt;自定高宽可与批量插入混合使用，例如：1.swf||100||100	2.swf||200||200，或者1.mp3||100||100	2.mp3||200||200&lt;/p&gt;&lt;p&gt;特别说明：uploadembed.php是静态内容，仅为演示用，无论上传了什么文件都返回内置的演示文件。&lt;/p&gt;
	</textarea><br /><br />
	6,上传文件URL回调:<br />
	<textarea id="elm6" name="elm6" rows="12" cols="80" style="width: 80%">
&lt;p&gt;当前实例调用的Javascript源代码为：&lt;/p&gt;&lt;p&gt;$('#elm6').xheditor({upLinkUrl:&quot;upload.php&quot;,upLinkExt:&quot;zip,rar,txt&quot;,upImgUrl:&quot;upload.php&quot;,upImgExt:&quot;jpg,jpeg,gif,png&quot;,&lt;span style=&quot;color:#ff0000;&quot;&gt;onUpload:insertUpload&lt;/span&gt;});&lt;/p&gt;&lt;p&gt;&lt;br /&gt;&lt;/p&gt;&lt;p&gt;上传文件URL回调接口onUpload可扩展编辑器内置的文件上传功能，例如可以将编辑器中上传的图片应用在文章主图片上。&lt;/p&gt;
	</textarea>
	<br /><br />上传文件列表：<select id="uploadList" style="width:350px;"></select>
	<br/><br />
	<input type="submit" name="save" value="Submit" />
	<input type="reset" name="reset" value="Reset" />
</form>
</body>
</html>