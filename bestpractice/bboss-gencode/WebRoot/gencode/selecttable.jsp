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
						<a href="javascript:void;"  class="btn blue" onclick="totableconfig('tableconfig',event)"> 进入表单配置</a>
						<a href="tableconfig.page"  class="btn blue ajaxify display-hide" id="tableconfig" formid="tableform"> 进入表单配置步骤</a>
					</div>
				</div>
			</div>
		</form>
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