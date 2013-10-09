<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree" %>
<%
String rootPath = org.frameworkset.mvc.FileController.getWorkFoldPath();
%>
<!-- 
	bboss-mvc框架实现文件上传功能
-->
<html>
<head>
<title>资源上传</title>
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
			<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/include/treeview.css">
	<pg:config enablecontextmenu="false" enabletree="false" />
	<script src='../include/mutifile/jquery.MultiFile.js' type="text/javascript"></script>
  <script type="text/javascript">
  var workfolder = "";
 function linktofolder(uri)
 {
	 workfolder = uri;
		$("#tips").empty(); 
 		$("#tips").append("<font color='red' size='5'>当前目录:<b><%=rootPath %>/" + uri + "</b></font>");
 		$("a").removeClass("a_bg_color");
 		$("a[name='"+uri+"']").addClass("a_bg_color"); 
	 $("#workfolder").attr("value",uri);
	 $("#filelist").load("${pageContext.request.contextPath}/file/filelist.page",{uri:uri});
 }
 
 function mkdir()
 { 
	 var dir = $("#dir").val();
	 if(dir == '')
		{
		 alert("请输入目录");
		 return;
		}
	 $.ajax({
		   type: "POST",
			url : "${pageContext.request.contextPath}/file/mkdir.page",
			data :{workfolder:workfolder,uri:dir},
			dataType : 'json',
			async:false,
			beforeSend: function(XMLHttpRequest){
					
			      		
			      		XMLHttpRequest.setRequestHeader("RequestType", "ajax");
			      				 	
				},
			success : function(responseText){
				//去掉遮罩
				
				if(responseText=="success"){
					 $("#foldertree").load("${pageContext.request.contextPath}/file/foldertree.page",{});			
				}else{
					alert("新增出错");
				}
			}
		  });
	
 }
 
 
 function uploadFile(){
	 $("#workfolder").attr("value",workfolder);
     if($("#file").val() == ""){
    	 alert("提示对话框" , "请添加附件!");
       return;	
     }
     
     var url = "${pageContext.request.contextPath}/file/uploadFiles.page";
     $("#upfile").form('submit', {
				    "url": url,
				    onSubmit:function(){			
						
				    },
				    success:function(responseText){	
				    	//去掉遮罩	
						
						if(responseText == "success"){
							alert("提示对话框 , 增加文件成功!");
							$("#filelist").load("${pageContext.request.contextPath}/file/filelist.page",{uri:workfolder});
							 $("#uptable").load("${pageContext.request.contextPath}/file/uptable.page",{
							  },function(){
							  	 $("input[type=file].multi").MultiFile();
							  });
						}
						else{
							alert("提示对话框 , 增加文件失败:"+responseText);
						}
								
				    }
				});	
  	}
 
  

 </script>

</head>
<body>
<span id="tips"><font color='red' size='5'>当前目录:<b><%=rootPath %></b></font></span>

<span id="foldertree">
		    <script type="text/javascript">
				$(document).ready(function(){
					  $("#foldertree").load("${pageContext.request.contextPath}/file/foldertree.page",{});
					});
			</script>
		</span>

					<form  method="POST" name="upfile" id="upfile" enctype="multipart/form-data">
						<input type="hidden" id="workfolder" name="workfolder">
						<span id="uptable">
							<script type="text/javascript">
								$(document).ready(function(){
								  $("#uptable").load("${pageContext.request.contextPath}/file/uptable.page",{
								  },function(){
								  	 $("input[type=file].multi").MultiFile();
								  });
								});
							</script>
						</span>
					</form>
			
			
		
		<span id="filelist">
		<script type="text/javascript">
				$(document).ready(function(){
					  $("#filelist").load("${pageContext.request.contextPath}/file/filelist.page",{});
					});
			</script>
			
		</span>
	
</body>
</html>
