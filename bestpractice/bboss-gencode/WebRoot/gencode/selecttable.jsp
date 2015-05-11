<%@ page language="java" pageEncoding="utf-8" session="false"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<!-- BEGIN PAGE HEADER-->
<h3 class="page-title">
	自动代码生成框架
</h3>

<div class="portlet box green">
	<div class="portlet-title">
		<div class="caption">
			<i class="fa fa-gift"></i>选择数据源和数据库表
		</div>
		<div class="tools">
			<a href="javascript:;" class="collapse"> </a> <a
				href="#portlet-config" data-toggle="modal" class="config"> </a> <a
				href="javascript:;" class="reload"> </a> <a href="javascript:;"
				class="remove"> </a>
		</div>
	</div>
	<div class="portlet-body form">
		<!-- BEGIN FORM-->
		<form action="#" id="tableform" class="form-horizontal">
			<div class="form-body">
				<div class="alert alert-danger display-hide">
					<button class="close" data-close="alert"></button>
					You have some form errors. Please check below.
				</div>
				<div class="alert alert-success display-hide">
					<button class="close" data-close="alert"></button>
					Your form validation is successful!
				</div>
				<!-- END PAGE HEADER-->
				<!-- BEGIN PAGE CONTENT-->
				
				<div class="row">
					<div class="col-md-6">
						<div class="form-group">
							<label class="control-label col-md-3">数据源</label>
							<div class="col-md-9">
								<select name="dbname" id="dbname" class="select2me form-control" onchange="ComponentsDropdowns.loadtables(event,'tableName')">
								<pg:list requestKey="dbs">
									<option value="<pg:cell/>"><pg:cell/></option>
								</pg:list>	
									
								</select> 
							</div>
						</div>
					</div>
					<!--/span-->
					<div class="col-md-6">
						<div class="form-group">
							<label class="control-label col-md-3">选择表 </label>
										
										
							<div class="col-md-9">
								<select name="tableName" id="tableName"   class="select2me form-control">
								<pg:list requestKey="tables">
									<option value="<pg:cell/>"><pg:cell/></option>
								</pg:list>	
									
								</select> 
							</div>
						</div>
					</div>
					<!--/span-->
				</div>

				<div class="row">
					<div class="col-md-12">
						<a href="javascript:void(0);"  class="btn blue" onclick="totableconfig('tableconfig',event)"> 进入表单配置</a>
						<a href="tableconfig.page"  class="btn blue ajaxify display-hide" id="tableconfig" formid="tableform"> 进入表单配置步骤</a>
					</div>
				</div>
			</div>
		</form>
	</div>
</div>


<div class="portlet box green-haze">
						<div class="portlet-title">
							<div class="caption">
								<i class="fa fa-globe"></i>历史记录
							</div>
							<div class="actions">
								<div class="btn-group">
									<a class="btn default" href="#" data-toggle="dropdown">
									Columns <i class="fa fa-angle-down"></i>
									</a>
									<div id="sample_5_column_toggler" class="dropdown-menu hold-on-click dropdown-checkboxes pull-right">
										<label><input type="checkbox" checked data-column="2"><pg:message code="gencode.id"/></label>
										<label><input type="checkbox" checked data-column="3"><pg:message code="gencode.author"/></label>
										<label><input type="checkbox" checked data-column="4"><pg:message code="gencode.company"/></label>
										<label><input type="checkbox" checked data-column="5"><pg:message code="gencode.tablename"/></label>
										<label><input type="checkbox" checked data-column="6"><pg:message code="gencode.dbname"/></label>
										<label><input type="checkbox" checked data-column="7"><pg:message code="gencode.createtime"/></label>
										<label><input type="checkbox" checked data-column="8"><pg:message code="gencode.updatetime"/></label>
									</div>
								</div>
							</div>
						</div>

						<div class="portlet-body">
							<table class="table table-striped table-hover" id="sample_5">
							<thead>
							<tr>
								<th class="table-checkbox">
									<input type="checkbox" id="group-checkable" data-set="#sample_5 .checkboxes"/>
								</th>
								
								<th>序号</th>							
					       		<th><pg:message code="gencode.id"/></th>       		
					       		<th><pg:message code="gencode.author"/></th>       		
					       		<th class="hidden-xs"><pg:message code="gencode.company"/></th> 
					       		<th class="hidden-xs"><pg:message code="gencode.dbname"/></th> 
					       		<th class="hidden-xs"><pg:message code="gencode.tablename"/></th>    
					       		<th class="hidden-xs"><pg:message code="gencode.createtime"/></th>        		
					       		<th class="hidden-xs"><pg:message code="gencode.updatetime"/></th>       		
								<th>操作</th>
								
								
							</tr>
							</thead>
							<tbody>
							<pg:list requestKey="gencodes">
	
					   		<tr >
					   			<td>
									<input name="cked" type="checkbox" class="checkboxes" value="<pg:cell colName="id" />"/>
									<input id="id" type="hidden" name="id" value="<pg:cell colName="id" />"/>
								</td>
								<td><pg:rowid increament="1" offset="false"/></td>    
				                <td><pg:cell colName="id"/></td>
				                <td><pg:cell colName="author"/></td>
				                <td><pg:cell colName="company"/></td>
				               
				                <td><pg:cell colName="dbname"/></td>
				                <td><pg:cell colName="tablename"/></td>
				                 <td><pg:cell colName="createtime"/></td>
				                <td><pg:cell colName="updatetime"/></td>				        		
				                <td >
				                <a href="javascript:void(0)" onclick="TableAdvanced.regencode('<pg:cell colName="id" />','tablereconfig.page',event)" class="btn default btn-xs purple">
										<i class="fa fa-edit"></i> 编辑 </a>
								<a href="javascript:void(0)" onclick="TableAdvanced.regencode('<pg:cell colName="id" />','tablereconfig.page',event)" class="btn default btn-xs purple">
										<i class="fa fa-edit"></i>下载 </a>
								 <a href="javascript:void(0)" onclick="TableAdvanced.regencode('<pg:cell colName="id" />','tablereconfig.page',event)" class="btn default btn-xs black">
										<i class="fa fa-trash-o"></i> 删除 </a>
				                </td>    
			                 
	        				</tr>
							</pg:list>
							
							</tbody>
							</table>
						</div>
					</div>

<div class="note note-success">
							<h4 class="block">自动代码生成框架功能说明</h4>
							<p>根据模板，自动生成给定表的增、删、改、分页查询、列表查询、国际化功能对应的程序和配置文件：
							<ul>
								<li>1.mvc控制器</li>
								<li>2.业务组件</li>
								<li>3.实体类</li>
								<li>4.jsp文件 可以定制不同风格的界面模板，目前提供了平台的基础ui风格</li>
								<li>5.cxf webservice服务类文件</li>
								<li>6.hessian服务类文件</li>
								<li>7.sql配置文件</li>
								<li>8.ioc/mvc组件装配部署和服务发布配置文件.</li>
								<li>9.国际化属性文件和国际化配置</li>
								<li>10.readme.txt 代码和配置文件集成配置说明</li>
							</ul>
							</p>
							<p>所有文件存放在服务器指定的目录中</p>

						</div>
<!-- END PAGE CONTENT-->
<script>jQuery(document).ready(function() {
 TableAdvanced.init();
});