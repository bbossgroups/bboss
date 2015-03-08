<%@ page language="java" pageEncoding="UTF-8"%>
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
		<title>新增台账</title>
		<%@ include file="/common/jsp/css.jsp"%>
	</head>
	<body>
		<div class="form_box">
			<form id="addForm" name="addForm" method="post">
			<!--  class="collapsible"  收缩 -->
			<fieldset>
				<legend><pg:message code="sany.appbom.softBaseInformation"/></legend>
				<table border="0" cellpadding="0" cellspacing="0" class="table4">
					<tr>
						<th width=85px >
							<pg:message code="sany.appbom.bm"/>：
						</th>
						<td width=140px>
							<input id="bm" name="bm" type="text" value=""
								class="w120 input_default easyui-validatebox" required="true"
								maxlength="10" /><font color="red">*</font>
						</td>
						<th width=85px>
							<pg:message code="sany.appbom.softName"/>：
						</th>
						<td width=140px>
							<input id="app_name" name="app_name" type="text" value=""
								class="w120 input_default easyui-validatebox" required="true"
								maxlength="100" /><font color="red">*</font>
						</td>
						<th width=70px>
							<pg:message code="sany.appbom.softInitial"/>：
						</th>
						<td>
							<input id="app_name_en" name="app_name_en" type="text" value=""
								class="w120" maxlength="100" />
						</td>
	
						</tr>
						<tr>
						<th>
							<pg:message code="sany.appbom.level"/>：
						</th>
						<td>
							<select id="soft_level" name="soft_level" class="select1"
								style="width: 125px;">
								<option value="1">
									一级
								</option>
								<option value="2">
									二级
								</option>
								<option value="3">
									三级
								</option>
								<option value="10">
									待定
								</option>
							</select>
						</td>

						<th>
							<pg:message code="sany.appbom.applyDomain"/>：
						</th>
						<td>
							<input id="apply_domain" name="apply_domain" type="text" value=""
								class="w120" maxlength="100" />
						</td>
						<th>
							<pg:message code="sany.appbom.state"/>：
						</th>
						<td>
							<select id="state" name="state" class="select1"
								style="width: 125px;">

								<option value="1">
									在用
								</option>
								<option value="2">
									试运行
								</option>
								<option value="3">
									升级研发中
								</option>
								<option value="4">
									停用
								</option>
								<option value="5">
									暂用
								</option>
								<option value="6">
									研发中
								</option>
								<option value="7">
									试验中
								</option>
							</select>
						</td>
						
					</tr>
				
					<tr>
						<th>
							<pg:message code="sany.appbom.yearOfBuild"/>：
						</th>
						<td>
							<input id="start_year" name="start_year" type="text" value=""
								class="w120" maxlength="20" />
						</td>
						<th>
							<pg:message code="sany.appbom.softFactory"/>：
						</th>
						<td colspan=3 >
							<input id="supplier" name="supplier" type="text" value=""
								class="w120" style="width: 338px;" maxlength="100" />
						</td>
			
					</tr>
		
					<tr>
						<th>
							<pg:message code="sany.appbom.functionDescribe"/>：
						</th>
						<td colspan="7">
							<textarea rows="5" cols="50" id="description" name="description"
								class="w120" style="width: 570px;font-size: 12px;height:40px;" maxlength="500"></textarea>
						</td>
					</tr>
				
				</table>
			</fieldset>
			<fieldset>
				<legend><pg:message code="sany.appbom.softTechnicalScenario"/></legend>
				<table class="table4" border=0 cellpadding="0" cellspacing="0">
				<tr>
						<th width=85px>
							<pg:message code="sany.appbom.structMode"/>：
						</th>
						<td width=140px>
							<input id="struct_mode" name="struct_mode" type="text" value=""
								class="w120" maxlength="100" />
						</td>
						<th width=85px>
							<pg:message code="sany.appbom.softLanguage"/>：
						</th>
						<td width=140px>
							<input id="soft_language" name="soft_language" type="text"
								value="" class="w120" maxlength="100" />
						</td>
						

						<th width=70px>
							<pg:message code="sany.appbom.rdType"/>：
						</th>
						<td>
							<select id="rd_type" name="rd_type" style="width: 125px;">
								<option value="1">
									自研
								</option>
								<option value="2">
									外购
								</option>
								<option value="3">
									免费
								</option>
								<option value="4">
									试用
								</option>
								<option value="5">
									外购+定制
								</option>
							</select>
						</td>
					</tr>
					<tr>
						<th >
							<pg:message code="sany.appbom.softTool"/>：
						</th>
						<td>
							<input id="develop_tool" name="develop_tool" type="text" value=""
								class="w120" maxlength="100" />
						</td>
						<th>
							<pg:message code="sany.appbom.version"/>：
						</th>
						<td colspan=3>
							<input id="version_no" name="version_no" type="text" value=""
								class="w120" style="width: 338px;;" maxlength="1000" />
						</td>
						
					</tr>
					<tr>
						<th>
							<pg:message code="sany.appbom.dbType"/>：
						</th>
						<td>
							<input id="db_type" name="db_type" type="text" value=""
								class="w120" maxlength="100" />
						</td>
						<th>
							<pg:message code="sany.appbom.domainUrl"/>：
						</th>
						<td colspan=3>
							<input id="domain_url" name="domain_url" type="text" value=""
								class="w120" style="width: 338px;" maxlength="500" />
						</td>
					</tr>
				</table>	
			</fieldset>
			<fieldset>
				<legend><pg:message code="sany.appbom.manageInformation"/></legend>
				<table class="table4" border=0 cellpadding="0" cellspacing="0">
					<tr>						
						<th width=85px>
							<pg:message code="sany.appbom.developDepart"/>：
						</th>
						<td width=140px>
							<input id="department_develop" name="department_develop"
								type="text" value="" class="w120" maxlength="100" />
						</td>
						<th >
							<pg:message code="sany.appbom.productManager"/>：
						</th>
						<td width=140px>
							<input id="product_manager" name="product_manager" type="text"
								value="" class="w120" maxlength="100" />
						</td>
						<th width=70px>
							<pg:message code="sany.appbom.departmentMaintain"/>：
						</th>
						<td>
							<input id="department_maintain" name="department_maintain"
								type="text" value="" class="w120" maxlength="100" />
						</td>
					</tr>
					<tr>
						<th>
							<pg:message code="sany.appbom.sysManager"/>：
						</th>
						<td>
							<input id="sys_manager" name="sys_manager" type="text" value=""
								class="w120" maxlength="100" />
						</td>
						<th>
							<pg:message code="sany.appbom.planType"/>：
						</th>
						<td>
							<select id="plan_type" name="plan_type" style="width: 125px;">
								<option value="1">
									目标
								</option>
								<option value="2">
									演进
								</option>
								<option value="3">
									临时
								</option>
								<option value="10">
									待定
								</option>
							</select>
						</td>
						<th>
							<pg:message code="sany.appbom.evolveStrategy"/>：
						</th>
						<td>
							<input id="evolve_strategy" name="evolve_strategy" type="text"
								value="" class="w120" maxlength="200" />
						</td>
					</tr>
					<tr>
						<th>
							<pg:message code="sany.appbom.evolveDepart"/>：
							
						</th>
						<td>
							<input id="evolve_depart" name="evolve_depart" type="text"
								value="" class="w120" maxlength="100" />
						</td>
						<th>
							<pg:message code="sany.appbom.evolvePlan"/>：
						</th>
						<td colspan=3 >
							<input id="evolve_plan" name="evolve_plan" type="text" value=""
								class="w120" maxlength="200" style="width: 338px;"/>
						</td>
					</tr>
					<tr>
						<th>
							<pg:message code="sany.appbom.manageScope"/>：
							
						</th>
						<td colspan="5" >
							<select id="manage_scope" name="manage_scope" style="width:180px;">
								<option value="1">
									管理列表
								</option>
								<option value="2">
									管理软件+版本号
								</option>
								<option value="3">
									管理软件+版本号+测试
								</option>
								<option value="4">
									管理软件+版本号+测试+UI
								</option>
							</select>
						</td>
					</tr>
					<tr>
						<th>
							<pg:message code="sany.appbom.mainDescription"/>：
						</th>
						<td colspan="7">
							<textarea rows="3" cols="50" id="main_description"
								name="main_description" class="w120" style="width: 570px;font-size: 12px;height:40px;"
								maxlength="500" ></textarea>
						</td>
					</tr>
				</table>		
			</fieldset>
			<div class="btnarea" >
				<a href="javascript:void(0)" class="bt_1" id="addButton" onclick="dosubmit()"><span><pg:message code="sany.pdp.common.operation.add"/></span></a>
				<a href="javascript:void(0)" class="bt_2" id="resetButton" onclick="doreset()"><span><pg:message code="sany.pdp.common.operation.reset"/></span></a>
				<a href="javascript:void(0)" class="bt_2" id="resetButton" onclick="closeDlg()"><span><pg:message code="sany.pdp.common.operation.exit"/></span></a>
				<input type="reset" id="reset" style="display: none;" />
			</div>
			</form>
		</div>
	</body>
<script language="javascript">
var api = frameElement.api, W = api.opener;
function checkBM(){
	var bm=$("#bm").val();
	var id="";
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
		if(!checkBM()){
			
			W.$.dialog.alert("编码已存在，请重新录入!",function(){return;},api);
			return;
		}
		$.ajax({
		   type: "POST",
			url : "addBom.page",
			data :formToJson("#addForm"),
			dataType : 'json',
			async:false,
			beforeSend: function(XMLHttpRequest){
					var validated = $("#addForm").form('validate');
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
					W.$.dialog.alert("新增记录成功",function(){	
							W.modifyQueryData();
							api.close();
					},api);													
				}else{
					W.$.dialog.alert("新增出错",function(){},api);
				}
			}
		  });
   	 }
function doreset(){
	$("#reset").click();
}
</script>