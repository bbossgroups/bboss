<%@ page language="java" pageEncoding="utf-8"%>
<%@page import=" org.frameworkset.security.session.impl.SessionHelper"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%--
	描述：数据抽取管理,作业历史记录查看
	作者：yinbp
	版本：1.0
	日期：2015-06-04
	 --%>
<pg:empty actual="${error}" evalbody="true">
<pg:yes>	 
<pg:beaninfo actual="${jobInfo}">
	<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>大数据抽取作业管理-管理节点[<pg:cell
							colName="adminNode" />]-作业[<pg:cell
							colName="jobName" />]</title>
<%@ include file="/include/css.jsp"%>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/include/dialog/lhgdialog.js"></script>

<style type="text/css">
<!--
-->
</style>

<script type="text/javascript">

$(document).ready(function() {
 
	
});
       
 
   
function viewJobstatics(jobname,jobstaticid)
{
	if(jobname == null|| jobname == '' )
	{
		 alert("没有选择作业！");
		 return;
	}
	window.location.href = "viewJobHistoryStatic.page?jobname="+jobname +"&jobstaticid="+jobstaticid;
}
 

  

</script>
</head>

<body><a name="top"></a>
	<div class="mcontent"
		style="width: 98%; margin: 0 auto; overflow: auto;">

		

		<div id="leftContentDiv" class="leftContentDiv">

			<div class="left_menu">
				<ul>
					<li class="select_links">
					<a href="javascript:void(0);">作业执行记录：</a>					
						<ul style="display: block;" id="job_tree_module">
							<pg:list requestKey="jobstatics">
								<li id="<pg:cell colName="jobstaticid"/>" 
								<pg:equal colName="jobstaticid" expressionValue="{0.jobstaticid}">class="select_links"</pg:equal>
								>
								<a href="javascript:void(0)" name="backTop"
									onclick="viewJobstatics('<pg:cell colName="jobname"/>','<pg:cell colName="jobstaticid"/>')"><b><pg:cell colName="savetime" dateformat="yyyy-MM-dd HH:mm:ss"/></b></a>	
									
									</li>
							</pg:list>
						</ul>
					</li>
				</ul>
			</div>
		

		</div>

		<div id="rightContentDiv" class="rightContentDiv">

			
			<div id="datanodes" style="overflow: auto" class="fixedhead">
				<div id="changeColor">
			
				
				<table width="100%" border="0" cellpadding="0" cellspacing="0"
					class="stable" id="tb">
					<tr>
						<th>作业状态：</th>
						<pg:case colName="status" >
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
						<pg:equal value="5">
						<th><font color="red">结束： 作业-<pg:cell colName="jobName"/>强制停止</font></th>
						</pg:equal>
						<pg:other >
						<th><font color="red">作业-<pg:cell colName="jobName"/>状态未知</font></th>
						</pg:other>
						</pg:case>
						 
					</tr>
				</table>
				
			
			</div></div>
				 
			<pg:notempty colName="jobdef">
			<div id="datanodes" style="overflow: auto;margin-top:40px;">
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
						class="stable stableS" id="tb">
						<tr>

							<td width="20%" colspan="2"><b>作业管理节点：<pg:cell colName="adminNode" /> &nbsp; <br>总任务数：<pg:cell colName="totaltasks"/> &nbsp;  完成任务数：<pg:cell colName="completetasks"/>&nbsp;
							正在运行任务数：<pg:cell colName="runtasks"/> &nbsp;     失败任务数：<pg:cell colName="failtasks"/> &nbsp;  
							 等待执行任务数： <pg:cell colName="waittasks"/> &nbsp;   未开始任务数：<pg:cell colName="unruntasks"/> &nbsp;<br> 
							 成功总记录数：<pg:cell colName="successrecords"/>  &nbsp; 失败总记录数：<pg:cell colName="failerecords"/></b></td>

						</tr>
						<pg:list colName="allDataNodes">
							<tr>

								<td width="20%"><pg:rowid increament="1"/><b>.</b>&nbsp; <a href="#<pg:cell />">数据处理节点：<pg:cell /></a>
									</td>
								<td class="tdwrap"><pg:map index="0" colName="jobstaticsIdxByHost" keycell="true">
									运行状态:<pg:case colName="status">
											<pg:equal value="-1">未开始</pg:equal>
											<pg:equal value="0">正在运行</pg:equal>
											<pg:equal value="1">执行完毕</pg:equal>
											<pg:equal value="2">执行异常</pg:equal>
											<pg:equal value="3">强制停止</pg:equal>
										</pg:case> &nbsp;&nbsp;<br>开始时间:<pg:cell colName="startTime"
											dateformat="yyyy-MM-dd HH:mm:ss" />&nbsp;&nbsp;结束时间:<pg:cell colName="endTime"
											dateformat="yyyy-MM-dd HH:mm:ss" />&nbsp;&nbsp;<br>
											节点总任务数：<pg:cell colName="totaltasks"/>&nbsp;&nbsp;
											完成任务数：<pg:cell colName="completetasks"/>&nbsp;&nbsp;
											正在运行任务数：<pg:cell colName="runtasks"/>&nbsp;&nbsp;
											
											失败任务数：<pg:cell colName="failtasks"/>&nbsp;&nbsp;
											等待执行任务数：<pg:cell colName="waittasks"/>&nbsp;&nbsp;
											未开始任务数：<pg:cell colName="unruntasks"/>&nbsp;&nbsp;<br>
											成功记录数：<pg:cell colName="successrecords"/>  &nbsp; 失败记录数：<pg:cell colName="failerecords"/><br>
																				
											错误日志:<pg:cell colName="errormsg" />
											
									</pg:map>	</td>	
									

							</tr>
						</pg:list>


					</table>
				</div>
			</div>

			<div class="title_box">


				<strong><span id="titileSpan">作业详情-<pg:cell
							colName="jobName" /> &nbsp;&nbsp;startid=<pg:cell colName="startid"/>  &nbsp;&nbsp;endid=<pg:cell colName="endid"/></span></strong>

			</div>
			<div id="jobList" style="overflow: auto">
				<div id="changeColor">
					<table width="100%" border="0" cellpadding="0" cellspacing="0"
								class="stable" id="tb">
							<tr>

								<th width="10%">作业配置</th>
								<td><textarea ><pg:cell colName="jobconfig"/></textarea></td>


							</tr>
							
							
							<tr>

								<th>已完成作业任务</th>
								<td class="tdwrap"><textarea class="smalltextarea"><pg:cell colName="successTaskNos"/></textarea></td>


							</tr>
							<tr>

								<th>未执行作业任务</th>
								<td class="tdwrap"><textarea class="smalltextarea"><pg:cell colName="undotaskNos"/></textarea></td>


							</tr>
							
							<tr>

								<th>失败作业任务</th>
								<td class="tdwrap"><textarea class="smalltextarea"><pg:cell colName="failedTaskNos"/></textarea></td>


							</tr>
							
						 
					 </table>
					
						<pg:map colName="jobstaticsIdxByHost">
						 
						  <table width="100%" border="0" cellpadding="0" cellspacing="0"
								class="stable" id="tb">
							<tr>

								<th width="20%"><a name="<pg:mapkey/>" href="#top">数据处理节点-<pg:mapkey/></a></th>
								<th colspan="100"></th>


							</tr>
						
							 
								
								<tr>
									<td colspan="3">运行状态:<pg:case colName="status">
											<pg:equal value="-1">未开始&nbsp;</pg:equal>
											<pg:equal value="0">正在运行</pg:equal>
											<pg:equal value="1">执行完毕&nbsp;</pg:equal>
											<pg:equal value="2">执行异常&nbsp;</pg:equal>
											<pg:equal value="3">强制停止&nbsp;</pg:equal>
										</pg:case> &nbsp;&nbsp;<br>开始时间:<pg:cell colName="startTime" dateformat="yyyy-MM-dd HH:mm:ss" />&nbsp;&nbsp;结束时间:<pg:cell colName="endTime"
											dateformat="yyyy-MM-dd HH:mm:ss" /><br>
											成功记录数：<pg:cell colName="successrecords"/>  &nbsp; 失败记录数：<pg:cell colName="failerecords"/><br>
											节点总任务数：<pg:cell colName="totaltasks"/>&nbsp;&nbsp;
											完成任务数：<pg:cell colName="completetasks"/>&nbsp;&nbsp;
											正在运行任务数：<pg:cell colName="runtasks"/>&nbsp;&nbsp;
											
											失败任务数：<pg:cell colName="failtasks"/>&nbsp;&nbsp;
											等待执行任务数：<pg:cell colName="waittasks"/>&nbsp;&nbsp;
											未开始任务数：<pg:cell colName="unruntasks"/>&nbsp;&nbsp;<br>	
											已完成作业号:<pg:notempty colName="completetaskNos" evalbody="true"><pg:yes><br><textarea class="smalltextarea"><pg:cell colName="completetaskNos"/></textarea><br></pg:yes><pg:no><br></pg:no> </pg:notempty>
											未完成作业号:<pg:notempty colName="undotaskNos" evalbody="true"><pg:yes><br><textarea class="smalltextarea"><pg:cell colName="undotaskNos"/></textarea><br></pg:yes><pg:no><br></pg:no> </pg:notempty>
											失败作业号:<pg:notempty colName="failedtaskNos" evalbody="true"><pg:yes><br><textarea class="smalltextarea"> <pg:cell colName="failedtaskNos"/></textarea><br></pg:yes><pg:no><br></pg:no> </pg:notempty>										 
											错误日志:<pg:cell colName="errormsg" />
											</td>

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

										<td><pg:rowid increament="1"/>-<pg:case colName="status">
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
	<a href="#top" class="backTop" title="返回顶部"></a>
</body>
	</html>
</pg:beaninfo>
</pg:yes>
<pg:no>
<body>
	<div class="mcontent"
		style="width: 98%; margin: 0 auto; overflow: auto;">
		提示：${error }
	</div>	
	</body>
</pg:no>
</pg:empty>