<%@ include file="/sysmanager/include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.frameworkset.common.poolman.DBUtil"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	String selected_dbname = request.getParameter("dbname");
	selected_dbname = selected_dbname==null?"":selected_dbname;
	List poollist = new ArrayList();
	DBUtil dbUtil = new DBUtil();
	Enumeration enum_ = dbUtil.getAllPoolnames();
	while(enum_.hasMoreElements()){
		String poolname = (String)enum_.nextElement();
		poollist.add(poolname);
	}
%>
<html>
    <title>选择数据源</title>
    <head>
		<script language="JavaScript" src="../include/pager.js" type="text/javascript"></script>
		<script language="JavaScript" src="../user/common.js" type="text/javascript"></script>
		<script language="JavaScript" src="../include/pager.js" type="text/javascript"></script>
		<script type="text/javascript"> 	
		function clearDbCache(){
			var radios = document.getElementsByName("dbname");
			var dbname = '';
			for(var i=0;i<radios.length;i++){
 	            if(radios[i].checked = true){
 	            	dbname = radios[i].value ;
 	                break;
 	            }
 	        }
			if(null != dbname&&""!=dbname){
				$.ajax({
					   type: "POST",
						url : "clearDbCache.page",
						data :{dbname:dbname},
						dataType : 'json',
						async:false,
						beforeSend: function(XMLHttpRequest){
								blockUI();	
						      	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
							},
						success : function(responseText){
							//去掉遮罩	
							unblockUI();
							if(responseText=="success"){
								$.dialog.alert("缓存刷新完成.",function(){},api);
								
							}else{
								$.dialog.alert(responseText,function(){},api);
							}
						}
					  });
			}
		}
		</script>
    </head>
    <body>
    	<div id="changeColor">
			<table width="106%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
			<tr>
				<th width="30px" ></th>
				<th><pg:message code="sany.pdp.dictmanager.db.name"/></th>
			</tr>
 			<%
 			    for(int i=0;i<poollist.size();i++){
 			   request.setAttribute("pool", poollist.get(i));
 			%>	
	 			<tr>	
	 				<td class="td_center">
	 					<pg:equal actual="${param.dbname}"  value="${pool}">
	 						<input type="radio" name="dbname" onClick="doReturn(value)" value="<%=poollist.get(i)%>" width="10"  checked="checked"  / >
	 					</pg:equal>
	 					<pg:notequal actual="${param.dbname}"  value="${pool}">
	 						<input type="radio" name="dbname" onClick="doReturn(value)" value="<%=poollist.get(i)%>" width="10"   / >
	 					</pg:notequal>
					</td>
					<td>
					    <%=poollist.get(i)%>
					</td>
				</tr>
			<%
			    }
			%>
			</table>
			<a  href="javascript:void(0)" class="bt_1 sp"
											id="queryButton" onclick="clearDbCache()"><span>清除</span> </a> 
		</div>
 	</body>
 	<script> 	    
 		var api = frameElement.api, W = api.opener;	
 		
 		function doReturn(value) {
 			W.document.all.db_name.value = value;
 		}
 		
 		/*
 	    window.onunload = function setValue(){
 	        var select_value = "";
 	        var arr = new Array();
 	        arr = document.getElementsByName("dbname");
 	        for(var i=0;i<arr.length;i++){
 	            if(arr[i].checked==true){
 	                select_value = arr[i].value;
 	                break;
 	            }
 	        }
 	        if(select_value!="<%=selected_dbname%>"){
 	            window.dialogArguments.createTypeForm.table_name.value = "";
 	            window.dialogArguments.createTypeForm.field_name.value = "";
				window.dialogArguments.createTypeForm.field_value.value = "";
				window.dialogArguments.createTypeForm.field_order.value = ""; 
				window.dialogArguments.createTypeForm.field_typeid.value = ""; 
 	        }
 	        window.returnValue = select_value;
 	    }
 	    window.onload = function autoRun(){
 	        var selected_value = "<%=selected_dbname%>";
 	        arr = document.getElementsByName("dbname");
 	        for(var i=0;i<arr.length;i++){
 	            if(arr[i].value==selected_value){
 	                arr[i].checked = true;
 	                break;
 	            }
 	        }
 	    }
 	    function selectOne(checkbox_name,e){
 	        arr = document.getElementsByName(checkbox_name);
 	        for(var i=0;i<arr.length;i++){
 	            if(arr[i].value==e.value){
 	                arr[i].checked = true;
 	            }else{
 	                arr[i].checked = false;
 	            }
 	        }
 	    }
 	    function removeValue(checkbox_name){
 	        arr = document.getElementsByName(checkbox_name);
 	        for(var i=0;i<arr.length;i++){ 	            
 	            arr[i].checked = false; 	            
 	        }
 	    }
 	    */
 	</script>
 </html>
