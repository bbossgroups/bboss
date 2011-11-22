<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree"%>

<%@ page import="com.chinacreator.security.AccessControl"%>

<%
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request, response);

%>
<head>
	<title></title>
	<style type="text/css">
<!--
.style1 {
	color: #CC0000
}
.headdiv {
	background-color: #C4E1FF;
	background-position: center center;
	border-top-style: none;
	border-right-style: solid;
	border-bottom-style: none;
	border-left-style: none;
	border-top-color: #FFFFFF;
	border-right-color: #FFFFFF;
	border-bottom-color: #FFFFFF;
	border-left-color: #FFFFFF;	
	border-right-width: thin;
	line-height: 25px;
	text-align: center;
	cursor:hand;
	vertical-align:middle;	
}
backColor {
	background-color: #3399FF;
}

-->
    </style>
</head>
<html>

	<SCRIPT language="JavaScript" SRC="../../sysmanager/scripts/validateForm.js"></SCRIPT>
	<SCRIPT LANGUAGE="JavaScript"> 
	
		//给String添加trim方法 
String.prototype.trim = function(){
	return this.replace(/(^\s*)|(\s*$)/g, "");
}	

var users = null ;
	
function checkedUser(){ 

	 var users =  new Array();
	  var userInfos=document.getElementsByName("userTree");
	   for(i=0;i<userInfos.length;i++){
		if(userInfos[i].checked&&userInfos[i].value.trim()!=""){ 
			var name = userInfos[i].id.split("_")[1];		
			users.push(name+"="+userInfos[i].value+"=1")
		}
	  } 
	 <%@ page language="java" contentType="text/html; charset=GBK"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree"%>

<%@ page import="com.chinacreator.security.AccessControl"%>

<%
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request, response);
	String txtReceiver = request.getParameter("txtReceiver");
	
%>
<head>
	<title></title>
	<style type="text/css">
<!--
.style1 {
	color: #CC0000
}
-->
</style>
	<link href="../../css/windows.css" rel="stylesheet" type="text/css">
	<link href="../../css/tab.winclassic.css" rel="stylesheet"
		type="text/css">
	<link href="../../css/contentpage.css" rel="stylesheet" type="text/css">
	<link href="sysmanager/css/windows.css" rel="stylesheet"
		type="text/css">
	<link href="sysmanager/css/tab.winclassic.css" rel="stylesheet"
		type="text/css">
	<link href="sysmanager/css/contentpage.css" rel="stylesheet"
		type="text/css">
	<base target="_self">
	<script language="JavaScript"
		src="<%=request.getContextPath()%>/purviewmanager/scripts/func.js"
		type="text/javascript"></script>
</head>
<html>

	<SCRIPT language="JavaScript" SRC="sysmanager/scripts/validateForm.js"></SCRIPT>
	<SCRIPT LANGUAGE="JavaScript"> 
	//alert("<%=txtReceiver%>");
		//给String添加trim方法 
       String.prototype.trim = function(){
          return this.replace(/(^\s*)|(\s*$)/g, "");
         }	

		 function checkedUser(){
		  var pwin=window.dialogArguments;
		  pwin.document.getElementById("txtReceiver").value="";
		  var userId=""
		  var flag=true;
		  var userInfos=document.getElementsByName("userTree");
		   for(i=0;i<userInfos.length;i++){
		   	if(userInfos[i].checked&&userInfos[i].value.trim()!=""){				
		   	   if(userId.trim()==""){
		   	   	userId=userInfos[i].value;
		   	  }else{
		   	  	userId+=","+userInfos[i].value;
		   	  }
		   	}
		  }  
		  var noloaders = getNonLoaderNodes('<%=txtReceiver%>');
		 for(var i = 0; i < noloaders.length; i ++)
		{
			 if(userId.trim()==""){
		   	   	userId=noloaders[i];
		   	  }else{
		   	  	userId+=","+noloaders[i];
		   	  }
		}
		  pwin.document.getElementById("txtReceiver").value=userId;
		  if(userId.trim() != "")
      window.returnValue = true;  
		  window.close();
	} 

	function getNonLoaderNodes(nodes)
	{
		var noloaders = new Array();
		var temp = nodes.split('$$');
		for(var i = 0; i < temp.length; i ++)
		{
			if(!document.getElementById("div_" + temp[i]))
				noloaders[noloaders.length] = temp[i];
		}
		return noloaders;

	}
	
	
	function init(){
		var pwin=window.dialogArguments;
	  var users=pwin.document.getElementById("txtReceiver").value;
	  var userItems=users.split(",");
	  var userInfos=document.getElementsByName("userTree");
	  for(i=0;i<userInfos.length;i++){
	  	
	  	for(j=0;j<userItems.length;j++){
	  		
	  		if(userInfos[i].value==userItems[j]&&userItems[j].trim()!="")
	  		userInfos[i].checked=true;
	  	}
	  }
	}

</SCRIPT>
	<%@ include file="/epp/css/cssControl.jsp"%><body
		class="contentbodymargin" scroll="yes" >
		<div align="center">
			<br />
			<form action="" method="post">
				<br>

				<fieldset style="width: 90%;">
					<LEGEND align=center>
						<FONT size="2">选择人员</FONT>
					</LEGEND>
					<table width="100%" height="35" border="0" align="left"
						cellpadding="0" cellspacing="1" class="table">
						
						<tr>
							<td width="" class="detailcontent">
								<tree:tree tree="app_org_user_tree"
									node="app_org_user_tree.node"
									imageFolder="../images/tree_images/" collapse="true"
									includeRootNode="true" mode="static-dynamic">
									<tree:param name="txtReceiver"/>
									<tree:checkbox name="userTree" recursive="true" defaultValues="<%=txtReceiver%>"
											uprecursive="false" />
									<tree:treedata
										treetype="com.chinacreator.xzsp.baseservice.menu.ActUsersTree"
										scope="request" rootid="0" rootName="机构人员树" expandLevel="1"
										showRootHref="false" needObserver="false" />
								</tree:tree>
							</td>
						</tr>
					</table>
					<br />
				</fieldset>
				<br />
				<br />
				<button onclick="checkedUser()">
					确定
				</button>&nbsp;&nbsp;
				<button onclick="javascript:window.close()">取消</button>

			</form>

		</div>
	</body>
</html>

	   
	  window.returnValue = users.join(":");  
	  window.close();
} 
	
function init(){ 
	users =window.dialogArguments;
	 var userArr = users;
	 for(var i=0;i<userArr.length;i++){
		var userEle = userArr[i].split("=");		
		var typeid=userEle[2];
		if(typeid==2){
			var box = roleIframe.document.getElementById("checkbox_"+userEle[0]);
			if(box){
				box.checked= true;
			}
		}else if(typeid==3){
			var box = groupIframe.document.getElementById("checkbox_"+userEle[0]);
			if(box){
				box.checked= true;
			}
		}else{
			var box = document.getElementById("checkbox_"+userEle[0]);
			if(box){
				box.checked= true;
			}
		}
		
	}
	
}


</SCRIPT>
	<%@ include file="/epp/css/cssControl.jsp"%><body
		class="contentbodymargin" scroll="yes" onLoad="init()">
		<div align="center">
			<br />
			<form name="form1" action="" method="post">
				<br>
				<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
				
				<tr>
				<td width="20">&nbsp;</td>
				<td align="left" colspan="4">						
				
				<div id="taborg">
					<table width="80%" border="1"  height="300" align="left"
						cellpadding="0" cellspacing="1" class="table">						
						<tr>
							<td class="detailcontent" valign="top">
								<tree:tree tree="app_org_user_tree"
									node="app_org_user_tree.node"
									imageFolder="../../images/tree_images/" collapse="true"
									includeRootNode="true" dynamic="false" mode="static">
									<tree:param name="roleName"/> 
									<tree:checkbox name="userTree" recursive="true"
											uprecursive="false" />
									<tree:treedata
										treetype="com.chinacreator.xzsp.baseservice.menu.ActUsersTree"
										scope="request" rootid="0"  rootName="行政审批实施模型机构" expandLevel="1"
										showRootHref="false" needObserver="false" />
								</tree:tree>
							</td>
						</tr>
					</table>
					</div>
					
			</td>
			</tr>
			<tr>
			<td colspan="5" align="center">
					<br />				
				<br />
				<br />
				<button onClick="checkedUser()">
					确定
				</button>&nbsp;&nbsp;
				<button onClick="javascript:window.close()">取消</button> 
				</td>
			</tr>
			</table>
			</form>

		</div>
	</body>
</html>
