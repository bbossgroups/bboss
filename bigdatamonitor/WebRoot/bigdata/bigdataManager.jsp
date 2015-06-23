<%@ page language="java" pageEncoding="utf-8"%>
<%@page import=" org.frameworkset.security.session.impl.SessionHelper"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%--
	描述：session管理
	作者：谭湘
	版本：1.0
	日期：2014-06-04
	 --%>
<pg:beaninfo actual="${jobInfo}">
	<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>大数据抽取作业管理-管理节点[${adminNode}]-作业[<pg:cell
							colName="jobName" />]</title>
<%@ include file="/include/css.jsp"%>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/include/dialog/lhgdialog.js"></script>

<style type="text/css">
<!--
#rightContentDiv {
	position: absolute;
	margin-left: 220px;
	margin-right: 8px;
	width: 80.5%;
}

#leftContentDiv {
	width: 200px;
	position: absolute;
	left: 0px;
	padding-left: 20px;
}
-->
</style>

<script type="text/javascript">

$(document).ready(function() {
	 

	 
       		
 
	
	 
	$('#submitJob').click(function() {
		submitJob();
    });
	
	
	
	$('#synJobStatus').click(function() {
		synJobStatus();
    });
	
	<pg:true colName="canrun">
	$('#executeJob').click(function() {
		executeJob();
    });
	</pg:true>
	
});
       
 

function executeJob()
{
	var job = $("#job").val()
	if(job == null|| job == '' )
	{
		 alert("没有选择需要执行的作业！");
		 return;
	}
	if(confirm("需要执行的作业-"+job+"吗？"))
	{
		$.ajax({
	 	 	type: "POST",
			url : "<%=request.getContextPath()%>/bigdata/executeJob.page",
			dataType : 'json',
			data :{"job":job},
			async:false,
			beforeSend: function(XMLHttpRequest){
					
				 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
				},
			success : function(data){
				if (data == 'success') {
					 $.dialog.alert("作业-"+job+"执行成功！");
					
				} else {
					 $.dialog.alert("同步失败！作业日志请看jobdef中的输出");
					 $("#jobdef").val(data);
					 
				}
				
			}	
		 });
	}
		
}
//加载实时任务列表数据  
function queryList(job){
	
	if(job && job != '')
    	window.location.href = "<%=request.getContextPath()%>/bigdata/index.page?job="+job;
    else
    	window.location.href = "<%=request.getContextPath()%>/bigdata/index.page";
    
}

function synJobStatus(){
	$.ajax({
 	 	type: "POST",
		url : "<%=request.getContextPath()%>/bigdata/synJobStatus.page",
		dataType : 'json',
		async:false,
		beforeSend: function(XMLHttpRequest){
				
			 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
			},
		success : function(data){
			if (data == 'success') {
				 $.dialog.alert("同步完成！");
				
			} else {
				 $.dialog.alert("同步失败！");
			}
			
		}	
	 });
}

 
function doClickTreeNode(job){
	 queryList(job);
}

 
 
function submitJob () {
	if($("#jobdef").val() == '')
	{
		  $.dialog.alert('请输入job定义!');
		return;
	}
	$.dialog.confirm('确定要提交作业吗？', function(){
     	$.ajax({
	 	type: "POST",
	 	url : "<%=request.getContextPath()%>/bigdata/submitNewJob.page",
	 	data :{"jobdef":$("#jobdef").val()},
		dataType : 'json',
		async:false,
		beforeSend: function(XMLHttpRequest){
			 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
		},
		success : function(data){
			if (data != 'success') {
				 $.dialog.alert("提交作业失败："+data);
			}else {
				queryList();
			}
		}	
	 });
	},function(){
	  		
	});         
}

</script>
</head>

<body>
	<div class="mcontent"
		style="width: 98%; margin: 0 auto; overflow: auto;">



		<div id="leftContentDiv">

			<div class="left_menu" style="width: 193px;">
				<ul>
					<li class="select_links"><a href="javascript:void(0);">作业清单：</a><a href="javascript:void(0);" class="bt_small" id="synJobStatus"><span>同步作业状态</span></a>
						<ul style="display: block;" id="job_tree_module">
							<pg:list colName="allJobNames">
								<li id="<pg:cell/>"><a href="javascript:void(0)"
									onclick="doClickTreeNode('<pg:cell/>')"><pg:cell /></a></li>
							</pg:list>
						</ul></li>
				</ul>
			</div>

		</div>

		<div id="rightContentDiv">

			
			<div id="datanodes" style="overflow: auto">
				<div id="changeColor">
			<pg:case colName="status" >
				
				<table width="100%" border="0" cellpadding="0" cellspacing="0"
					class="stable" id="tb">
					<tr>
						<th>作业状态：</th>
						<pg:equal value="-1">
						<th><font color="red">作业-<pg:cell colName="jobName"/>未执行</font></th>
						</pg:equal>
						<pg:equal value="0">
						<th><font color="red">作业-<pg:cell colName="jobName"/>正在执行</font></th>
						</pg:equal>
						<pg:equal value="1">
						<th><font color="red">结束：作业-<pg:cell colName="jobName"/>执行完毕</font></th>
						</pg:equal>
						<pg:equal value="2">
						<th><font color="red">结束：作业-<pg:cell colName="jobName"/>执行失败</font></th>
						</pg:equal>
						<pg:equal value="3">
						<th><font color="red">结束：作业-<pg:cell colName="jobName"/>部分成功，部分异常</font></th>
						</pg:equal>
						<pg:equal value="4">
						<th><font color="red">挂起： 作业-<pg:cell colName="jobName"/>部分未开始，其他要么完成要么失败，类似于挂起</font></th>
						</pg:equal>
						<pg:other >
						<th><font color="red">作业-<pg:cell colName="jobName"/>状态未知</font></th>
						</pg:other>
						
						<th><a href="javascript:void(0);" class="bt_1" id="executeJob"><span>执行作业-<pg:cell colName="jobName"/></span></a>
						<a href="javascript:void(0);" class="bt_small" id="deleteJobHDFS"><span>删除作业-<pg:cell colName="jobName"/>-HDFS目录</span></a></th>
					</tr>
				</table>
				
			</pg:case>
			</div></div>
			<div id="datanodes" style="overflow: auto">
				<div id="changeColor">
					<form id="queryForm" name="queryForm">
						<input type="hidden" name="job" id="job"
							value="<pg:cell colName="jobName"/>" />
						<table width="100%" border="0" cellpadding="0" cellspacing="0"
						class="stable" id="tb">
						 
										<tr>
											<th align="left">输入要执行的作业：</th>

<th align="left"><a href="javascript:void(0);" class="bt_1" id="submitJob"><span>提交一个新作业</span></a> </th>

										</tr>
										<tr>

											<td width="100%" colspan="2"><textarea id="jobdef" name="jobdef"></textarea></td>


										</tr>
									 
						 
						</table>
					</form>
				</div>
			</div>
			<pg:notempty colName="jobdef">
			<div id="datanodes" style="overflow: auto">
				<div id="changeColor">
					
						
						<table width="100%" border="0" cellpadding="0" cellspacing="0"
						class="stable" id="tb">
						 
										<tr>
											<th align="left" >作业定义-<pg:cell colName="jobName"/>：</th>



										</tr>
										<tr>

											<td width="100%" ><textarea><pg:cell colName="jobdef"/></textarea></td>


										</tr>
									 
						 
						</table>
					
				</div>
			</div>
			</pg:notempty>
			 
			<div class="title_box">


				<strong><span id="titileSpan">所有作业节点</span></strong>

			</div>

			<div id="datanodes" style="overflow: auto">
				<div id="changeColor">
					<table width="100%" border="0" cellpadding="0" cellspacing="0"
						class="stable" id="tb">
						<tr>

							<td>作业管理节点：${adminNode}</td>

						</tr>
						<pg:list requestKey="allDataNodes">
							<tr>

								<td>数据处理节点：<pg:cell /></td>

							</tr>
						</pg:list>


					</table>
				</div>
			</div>

			<div class="title_box">


				<strong><span id="titileSpan">作业详情-<pg:cell
							colName="jobName" /></span></strong>

			</div>
			<div id="jobList" style="overflow: auto">
				<div id="changeColor">
					<table width="100%" border="0" cellpadding="0" cellspacing="0"
								class="stable" id="tb">
							<tr>

								<th>作业配置</th>
								<td><textarea height="200px"><pg:cell colName="jobconfig"/></textarea></td>


							</tr>
							
						 
					 </table>
					
						<pg:map colName="jobstaticsIdxByHost">
						 
						  <table width="100%" border="0" cellpadding="0" cellspacing="0"
								class="stable" id="tb">
							<tr>

								<th>数据处理节点-<pg:mapkey/></th>
								<th colspan="100"></th>


							</tr>
						
							 
								
								<tr>
									<td colspan="3">运行状态:<pg:case colName="status">
											<pg:equal value="-1">未开始</pg:equal>
											<pg:equal value="0">正在运行</pg:equal>
											<pg:equal value="1">执行完毕</pg:equal>
											<pg:equal value="2">执行异常</pg:equal>

										</pg:case> &nbsp;&nbsp;<br>开始时间:<pg:cell colName="startTime"
											dateformat="yyyy-MM-dd HH:mm:ss" />&nbsp;&nbsp;<br>结束时间:<pg:cell colName="endTime"
											dateformat="yyyy-MM-dd HH:mm:ss" />&nbsp;&nbsp;<br>错误日志:<pg:cell colName="errormsg" /></td>

								</tr>
							 
								<tr>
									<th colspan="3">任务列表</th>
								</tr>

							 
								<tr>

									<th>任务状态</th>
									<th>任务详情</th>
									<th >异常信息</th>

								</tr>

								<pg:list colName="runtasksInfos">
									<tr>

										<td><pg:case colName="status">
												<pg:equal value="-1">未开始</pg:equal>
												<pg:equal value="0">正在运行</pg:equal>
												<pg:equal value="1">执行完毕</pg:equal>
												<pg:equal value="2">执行异常</pg:equal>
												<pg:equal value="3">排队等候</pg:equal>
											</pg:case>&nbsp;&nbsp;
										成功行数:<pg:cell colName="handlerows" />&nbsp;&nbsp;
										错误行数:<pg:cell colName="errorrows" />
										</td>
										<td ><pg:cell colName="taskInfo" /></td>
										<td ><pg:cell colName="errorInfo" /></td>

									</tr>
								</pg:list>

							 
						
						</table>	
						
					</pg:map>
					 
				</div>
			</div>
		</div>
	</div>
</body>
	</html>
</pg:beaninfo>