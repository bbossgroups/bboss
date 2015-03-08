<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<%--
	描述：增加台账信息设置
	作者：许石玉
	版本：1.0
	日期：2012-02-08
	 --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>修改台账</title>
		<%@ include file="/common/jsp/css.jsp"%>
	</head>
	<%	String path = request.getContextPath();%>
	<body>
		<div class="form_box">
			<div>
				<pg:notempty actual="${errmsg}">${errmsg}</pg:notempty>
			</div>
			<pg:beaninfo requestKey="appbom">
				<form id="editForm" name="editForm">
				<fieldset>
				<legend>应用软件基本信息</legend>
					<table  border="0" cellpadding="0" cellspacing="0"
						class="table4">
						<tr>
							<th width=85px>
								编码：
							</th>
							<td width=140px>
								<input type="hidden" id="id" name="id"
									value="<pg:cell colName="id"/>" />
								<input id="bm" name="bm" type="text"
									value="<pg:cell colName="bm"/>"
									class="w120 input_default easyui-validatebox"
									 required="true" maxlength="10" style="width:120px;"/><font color="red">*</font>
							</td>
							<th width=85px>
								软件名称：
							</th>
							<td width=140px>
								<input id="app_name" name="app_name" type="text"
									value="<pg:cell colName="app_name"/>"
									class="w120 input_default easyui-validatebox"
									 required="true" maxlength="100" /><font color="red">*</font>
							</td>
							<th width=70px>
								软件缩写：
							</th>
							<td >
								<input id="app_name_en" name="app_name_en" type="text"
									value="<pg:cell colName="app_name_en"/>" class="w120"
									 maxlength="100" />
							</td>
						</tr>
						<tr>
							<th>
								级别：
							</th>
							<td>
								<select id="soft_level" name="soft_level" class="select1"
									style="width: 125px;">
									<pg:case  colName="soft_level" >
									<option value="1"
										<pg:equal value="1">selected</pg:equal>>
										一级
									</option>
									<option value="2"
										<pg:equal value="2">selected</pg:equal>>
										二级
									</option>
									<option value="3"
										<pg:equal value="3">selected</pg:equal>>
										三级
									</option>
									<option value="10"
										<pg:equal value="10">selected</pg:equal>>
										待定
									</option>
									</pg:case>
								</select>
							</td>

							<th>
								应用功能域：
							</th>
							<td>
								<input id="apply_domain" name="apply_domain" type="text"
									value="<pg:cell colName="apply_domain"/>" class="w120"
									 maxlength="100" />
							</td>
							<th>
								状态：
							</th>
							<td>
								<select id="state" name="state" class="select1"
									style="width: 125px;">
									<pg:case colName="state">
									<option value="1"
										<pg:equal value="1">selected</pg:equal>>
										在用
									</option>
									<option value="2"
										<pg:equal value="2">selected</pg:equal>>
										试运行
									</option>
									<option value="3"
										<pg:equal value="3">selected</pg:equal>>
										升级研发中
									</option>
									<option value="4"
										<pg:equal value="4">selected</pg:equal>>
										停用
									</option>
									<option value="5"
										<pg:equal value="5">selected</pg:equal>>
										暂用
									</option>
									<option value="6"
										<pg:equal value="6">selected</pg:equal>>
										研发中
									</option>
									<option value="7"
										<pg:equal value="7">selected</pg:equal>>
										试验中
									</option>
									</pg:case>
								</select>
							</td>
						</tr>
						<tr>
							<th>
								投建年份：
							</th>
							<td>
								<input id="start_year" name="start_year" type="text"
									value="<pg:cell colName="start_year"/>" class="w120"
									 maxlength="20" />
							</td>
							<th>
								软件厂商：
							</th>
							<td colspan=3>
								<input id="supplier" name="supplier" type="text"
									value="<pg:cell colName="supplier"/>" class="w120"
									 maxlength="100" style="width: 338px;" />
							</td>
						</tr>

						<tr>
							<th>
								功能描述：
							</th>
							<td colspan="6">
								<textarea rows="5" cols="50" id="description" name="description"
									class="w120" style="width: 570px;font-size: 12px;height:40px;" maxlength="500"><pg:cell colName="description" /></textarea>
							</td>
						</tr>
					</table>
				</fieldset>
					
				<fieldset>
				<legend>软件技术方案</legend>
				<table border="0" cellpadding="0" cellspacing="0"
						class="table4">
					<tr>
					<th width=85px>
						结构模式：
					</th>
					<td width=140px>
						<input id="struct_mode" name="struct_mode" type="text"
							value="<pg:cell colName="struct_mode"/>" class="w120"
							 maxlength="100" />
					</td>
					<th width=85px>
						软件语言：
					</th>
					<td width=140px>
						<input id="soft_language" name="soft_language" type="text"
							value="<pg:cell colName="soft_language"/>" class="w120"
							 maxlength="100" />
					</td>
					<th width=70px>
						研发类型：
					</th>
					<td>
						<select id="rd_type" name="rd_type" style="width: 125px;">
						<pg:case colName="rd_type">
							<option value="1" <pg:equal   value="1">selected</pg:equal>>
								自研
							</option>
							<option value="2" <pg:equal   value="2">selected</pg:equal>>
								外购
							</option>
							<option value="3" <pg:equal value="3">selected</pg:equal>>
								免费
							</option>
							<option value="4" <pg:equal   value="4">selected</pg:equal>>
								试用
							</option>
							<option value="5" <pg:equal   value="5">selected</pg:equal>>
								外购+定制
							</option>
						</pg:case>	
						</select>
					</td>
					</tr>
					<tr>
						<th>
							开发工具：
						</th>
						<td>
							<input id="develop_tool" name="develop_tool" type="text"
								value="<pg:cell colName="develop_tool"/>" class="w120"
								 maxlength="100" />
						</td>
						<th>
							版本号：
						</th>
						<td colspan=3>
							<input id="version_no" name="version_no" type="text"
								value="<pg:cell colName="version_no"/>" 
								style="width: 338px;" maxlength="1000" />
						</td>
						
					</tr>
					<tr>
						<th>
							数据库：
						</th>
						<td>
							<input id="db_type" name="db_type" type="text"
								value="<pg:cell colName="db_type"/>" class="w120"
								 maxlength="100" />
						</td>
						<th>
							系统域名：
						</th>
						<td colspan=3>
							<input id="domain_url" name="domain_url" type="text"
								value="<pg:cell colName="domain_url"/>"
								style="width: 338px;" maxlength="500" />
						</td>
					</tr>
				</table>		
				</fieldset>	
				<fieldset>
				<legend>管理信息</legend>
				<table  border="0" cellpadding="0" cellspacing="0"
						class="table4">
					<tr>
						<th width=85px>
							研发部门：
						</th>
						<td width=140px>
							<input id="department_develop" name="department_develop"
								type="text" value="<pg:cell colName="department_develop"/>"
								class="w120"  maxlength="100" />
						</td>
						<th >
							产品经理：
						</th>
						<td width=140px>
							<input id="product_manager" name="product_manager" type="text"
								value="<pg:cell colName="product_manager"/>" class="w120"
								 maxlength="100" />
						</td>
						<th width=70px>
							运维部门：
						</th>
						<td>
							<input id="department_maintain" name="department_maintain"
								type="text" value="<pg:cell colName="department_maintain"/>"
								class="w120"  maxlength="100" />
						</td>

					</tr>
					<tr>
						<th>
							系统经理：
						</th>
						<td>
							<input id="sys_manager" name="sys_manager" type="text"
								value="<pg:cell colName="sys_manager"/>" class="w120"
								 maxlength="100" />
						</td>
						<th>
							规划类型：
						</th>
						<td>
							<select id="plan_type" name="plan_type" style="width: 125px;">
								<pg:case colName="plan_type">
								<option value="1" <pg:equal value="1">selected</pg:equal>>
									目标
								</option>
								<option value="2" <pg:equal value="2">selected</pg:equal>>
									演进
								</option>
								<option value="3" <pg:equal value="3">selected</pg:equal>>
									临时
								</option>
								<option value="10" <pg:equal value="10">selected</pg:equal>>
									待定
								</option>
								</pg:case>
							</select>
						</td>
						<th>
							演进策略：
						</th>
						<td>
							<input id="evolve_strategy" name="evolve_strategy" type="text"
								value="<pg:cell colName="evolve_strategy"/>" class="w120"
								 maxlength="200" />
						</td>
						
					</tr>
					<tr>
						<th>
							演进负责方：
						</th>
						<td>
							<input id="evolve_depart" name="evolve_depart" type="text"
								value="<pg:cell colName="evolve_depart"/>" class="w120"
								 maxlength="100" />
						</td>
						<th>
							演进实施计划：
						</th>
						<td colspan=3>
							<input id="evolve_plan" name="evolve_plan" type="text"
								value="<pg:cell colName="evolve_plan"/>" class="w120"
								 maxlength="200" style="width: 338px;"/>
						</td>
					</tr>
					<tr>							
						<th>
							研发管理范围：
						</th>
						<td colspan=3>
							<select id="manage_scope" name="manage_scope">
							<pg:case colName="manage_scope">
								<option value="1" <pg:equal value="1">selected</pg:equal>>
									管理列表
								</option>
								<option value="2" <pg:equal value="2">selected</pg:equal>>
									管理软件+版本号
								</option>
								<option value="3" <pg:equal value="3">selected</pg:equal>>
									管理软件+版本号+测试
								</option>
								<option value="4" <pg:equal value="4">selected</pg:equal>>
									管理软件+版本号+测试+UI
								</option>
							</pg:case>
							</select>
						</td>
					</tr>
					<tr>
						<th>
							关键说明：
						</th>
						<td colspan="6">
							<textarea rows="5" cols="50" id="main_description"
								name="main_description" class="w120" style="width: 570px;font-size: 12px;height:40px;"
								maxlength="500"><pg:cell colName="main_description" /></textarea>
						</td>
					</tr>	
				</table>
				</fieldset>	
					<div class="btnarea" >
							<a href="javascript:void(0)" class="bt_1" id="editButton"
									onclick="dosubmit()"><span>保存</span> </a>
							<a href="javascript:void(0)" class="bt_2" id="closeButton"
									onclick="closeDlg()"><span>退出</span> </a>
					</div>
			
				</form>
			</pg:beaninfo>
		</div>
	</body>
	<script language="javascript">
	var api = frameElement.api, W = api.opener;
function checkLength(o,n,min,max) {

	if ( $.trim(o.val()).length > max || $.trim(o.val()).length < min ) {
	//	o.addClass('ui-state-error');
		o.focus();
		alert(n + "的长度必须在 "+min+" 和 "+max+"之间");
		return false;
	} else {
		return true;
	}
}

function checkInvalidCharset(o,n) {
	var content = o.val();
	/*
	if ( content.indexOf("#")>=0 || content.indexOf("&")>=0 || content.indexOf("%")>=0 ){
		o.focus();
		alert(n+"中含有特殊字符#或&");
		return false;
	} 
	*/
	if ( content.indexOf("\"")>=0){
		o.focus();
		alert(n+"中含有特殊字符\",请使用“或”代替");
		return false;
	}
	return true;
}
function checkBM(){
	var bm=$("#bm").val();
	
	var id=$("#id").val();
	var isValid=false;
 	$.ajax({
		   type: "POST",
			url : "CheckBmExist.page",
			data :{"bm":bm,"id":id},
			dataType : 'json',
			async:false,
			beforeSend: function(XMLHttpRequest){
				 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
				},
			success : function(data){
				if(data=="0")
					isValid=true;
				else
					isValid=false;
			}
		  });
		  return isValid;
		  
}
   function dosubmit()
   {
   		
		$.ajax({
		   type: "POST",
			url : "update.page",
			data :formToJson("#editForm"),
			dataType : 'json',
			async:false,
			beforeSend: function(XMLHttpRequest){
					var validated = $("#editForm").form('validate');
			      	if (validated){
			      		blockUI();	
			      		XMLHttpRequest.setRequestHeader("RequestType", "ajax");
			      	}
			      	else
			      	{
			      		return false;
			      	}
				 	
				},
			success : function(responseText){
				//去掉遮罩	
				unblockUI();
				if(responseText=="success"){
					W.$.dialog.alert("修改记录成功",function(){	
							W.modifyQueryData();
							api.close();
							
					},api);	
					
				}else{
					alert("修改出错");
				}
			}
		  });
   }
</script>