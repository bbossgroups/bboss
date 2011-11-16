<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>SWFUpload-限制最大2M的单个文件</title>
		<link
			href="<%=request.getContextPath() %>/include/smartupload/default.css"
			rel="stylesheet" type="text/css" />
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
							<script type="text/javascript"
								src="<%=request.getContextPath() %>/include/smartupload/jquery-1.5.js"></script>
							<script type="text/javascript"
								src="<%=request.getContextPath() %>/include/smartupload/swfupload.js"></script>
							<script type="text/javascript"
								src="<%=request.getContextPath() %>/include/smartupload/swfupload.queue.js"></script>
							<script type="text/javascript"
								src="<%=request.getContextPath() %>/include/smartupload/fileprogress.js"></script>
							<script type="text/javascript"
								src="<%=request.getContextPath() %>/include/smartupload/handlers.js"></script>
							<script type="text/javascript" charset="UTF-8">
		var swfu;
		window.onload = function() {
			var settings = {
				flash_url : "<%=request.getContextPath() %>/include/smartupload/swfupload.swf",
				upload_url: "<%=request.getContextPath() %>/swfupload/upload.htm",
				post_params: {"" : ""},
				file_size_limit : "2 MB",
				file_types : "*.zip;*.rar;*.png;*.doc;*.docx;*.ppt;*.pptx",
				file_types_description : "All Files",
				file_upload_limit : 100,
				file_queue_limit : 0,
				file_post_name:"attachment",
				custom_settings : {
					progressTarget : "fsUploadProgress",
					cancelButtonId : "abbreviations"
				},
				debug: false,

				// Button settings
				//button_image_url: "<%=request.getContextPath() %>/include/smartupload/TestImageNoText_65x29.png",
				button_width: "65",
				button_height: "29",
				button_placeholder_id: "spanButtonPlaceHolder",
				button_text: '浏览',
				button_cursor: SWFUpload.CURSOR.ARROW,
				//button_text_style: ".theFont { font-size: 16; }",
				//button_text_left_padding: 12,
				//button_text_top_padding: 3,
				
				// The event handler functions are defined in handlers.js
				file_queued_handler : fileQueued,
				file_queue_error_handler : fileQueueError,
				file_dialog_complete_handler : function(){},
				upload_start_handler : uploadStart,
				upload_progress_handler : uploadProgress,
				upload_error_handler : uploadError,
				upload_success_handler : uploadSuccess,
				upload_complete_handler : uploadComplete,
				queue_complete_handler : queueComplete	// Queue plugin event
			};

			swfu = new SWFUpload(settings);
	     };

        
	     
	     function uploadComplete(file) {
	     	var fileName = document.createElement("div");
	     	var fileSize = document.createElement("div")
	     	var fileType = document.createElement("div")
	     	var operate = document.createElement("div")
	     	fileName.innerHTML = file.name;
	     	fileSize.innerHTML = file.size+" byte";
	     	fileType.innerHTML = file.type;
	       // var del =  document.createElement("<input type=checkbox name=delete/>");
	        var del =  document.createElement("input");
	        del.setAttribute("type","checkbox");
	        del.setAttribute("name","delete");
	        
	        del.value = file.name;
	        operate.appendChild(del);
	        
	        
	        var show = document.getElementById("show");
	        var nodeTr = show.insertRow(show.rows.length);
	        nodeTr.background = "#FFFFE0";
	        var nodeTd = nodeTr.insertCell(0);
	        nodeTd.appendChild(fileName);
	        var nodeTd = nodeTr.insertCell(1);
	        nodeTd.appendChild(fileSize);
	        var nodeTd = nodeTr.insertCell(2);
	        nodeTd.appendChild(fileType);
	        var nodeTd = nodeTr.insertCell(3);
	        nodeTd.appendChild(operate);
	     }
	     
	     function deleteOpera(){
	            var deleteFileNames="";
	     		var getDeleteOption = $("input:checked");
	     		if(getDeleteOption.length==1&&getDeleteOption[0].name == "del"||getDeleteOption.length==0){
                    return;	     		 
	     		}
	           for(var i=0;i<getDeleteOption.length;i++){
	             if(getDeleteOption[i].name != "del"){
	               deleteFileNames =deleteFileNames+","+getDeleteOption[i].value;
	            }
	            }
	            $.post("<%=request.getContextPath() %>/swfupload/deletefiles.htm",{fileNames:encodeURIComponent(deleteFileNames)},deleteResult);
	            
	     }
	     function deleteResult(data){
	     alert("删除成功！");
	     var getDeleteOption = $("input:checked");
	     for(var i=0;i<getDeleteOption.length;i++){
	       if(getDeleteOption[i].name != "del"){
	       var fatherOption = getDeleteOption[i].parentNode.parentNode.parentNode.rowIndex;
	       document.getElementById("show").deleteRow(fatherOption);
	       }
  	     }
	     
	     }
	     
	     function upStart(){
	    // swfu.addPostParam("upload_",document.getElementById("upload_").value);
	    //alert("对不起，服务器空间有限，关闭附件上传功能。")
	    
	     	swfu.startUpload();
	     }
	     
	     function allSelect(obj){
	        var allcheck = document.getElementsByName("delete");
	        if(obj.checked){
	        for(var i=0;i<allcheck.length;i++){
	           allcheck[i].checked = true;
	        }
	       }else{
	          for(var i=0;i<allcheck.length;i++){
	           allcheck[i].checked = false;
	       }
	       }
	     }
	     
	     
	</script>
	</head>
	<body>
	<form id="form1" action="" method="post"
					enctype="multipart/form-data">
		<div id="mainBody">
			<ul class="options">


				<li id="size">
					<a id="spanButtonPlaceHolder" >浏览</a>
				</li>
				<li id="size">
					<a id="upload" href="#" onclick="upStart()">上传</a>
				</li>


				<li id="abbreviations">
					<a href="#" onclick="swfu.cancelQueue()">取消上传</a>
				</li>
				
				
			</ul>
		
		    <div class="blockContainer">
			         <div>待上传文件:</div>
			         <table class="genericTbl">
			         <tr>
			            <th class="order1 sorted">
			               <div class="fieldset flash" id="fsUploadProgress"></div>
			            </th>
			            
			         </tr>
			         </table>
					
					<div id="divStatus">
									<h3>O文件上传</h3>
					</div>
			</div>
		</div>
		</form>
		<div id="mainBody">

			<ul class="options">



				<li id="size">
					已上传文件列表
				</li>


				<li id="abbreviations">
					<a href="#" onclick="deleteOpera()">删除</a>
				</li>
			</ul>

			<div class="blockContainer">

				<table class="genericTbl" id="show">

					<tr>
						<th align="center" class="order1 sorted">
							文件名
						</th>
						<th align="center" class="order1 sorted">
							文件类型
						</th>
						<th align="center" class="order1 sorted">
							文件大小
						</th>

						<th align="center">
						     全选:
							<input type="checkbox" name="del" onclick="allSelect(this)" />
						</th>
					</tr>
					<pg:list requestKey="files">
						<tr>

							<td align="center">
								<div>
									<pg:cell colName="fileName" />
								</div>
							</td>
							<td align="center">
								<div>
									<pg:cell colName="fileSize" />
									&nbsp;byte
								</div>
							</td>
							<td align="center">
								<div>
									<pg:cell colName="fileType" />
								</div>
							</td>
							<td align="center">
								<div>
									<input type="checkbox" name="delete"
										value="<pg:cell colName="fileName"/>" />
								</div>
							</td>

						</tr>
					</pg:list>

				</table>

			</div>
		</div>
	</body>
</html>
