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
										<label><input type="checkbox" checked data-column="0">Rendering engine</label>
										<label><input type="checkbox" checked data-column="1">Browser</label>
										<label><input type="checkbox" checked data-column="2">Platform(s)</label>
										<label><input type="checkbox" checked data-column="3">Engine version</label>
										<label><input type="checkbox" checked data-column="4">CSS grade</label>
									</div>
								</div>
							</div>
						</div>

						<div class="portlet-body">
							<table class="table table-striped table-hover" id="sample_5">
							<thead>
							<tr>
								<th>
									 Rendering engine
								</th>
								<th>
									 Browser
								</th>
								<th class="hidden-xs">
									 Platform(s)
								</th>
								<th class="hidden-xs">
									 Engine version
								</th>
								<th class="hidden-xs">
									 CSS grade
								</th>
							</tr>
							</thead>
							<tbody>
							<tr>
								<td>
									 Trident
								</td>
								<td>
									 Internet Explorer 4.0
								</td>
								<td>
									 Win 95+
								</td>
								<td>
									 4
								</td>
								<td>
									 X
								</td>
							</tr>
							<tr>
								<td>
									 Trident
								</td>
								<td>
									 Internet Explorer 5.0
								</td>
								<td>
									 Win 95+
								</td>
								<td>
									 5
								</td>
								<td>
									 C
								</td>
							</tr>
							<tr>
								<td>
									 Trident
								</td>
								<td>
									 Internet Explorer 5.5
								</td>
								<td>
									 Win 95+
								</td>
								<td>
									 5.5
								</td>
								<td>
									 A
								</td>
							</tr>
							<tr>
								<td>
									 Trident
								</td>
								<td>
									 Internet Explorer 6
								</td>
								<td>
									 Win 98+
								</td>
								<td>
									 6
								</td>
								<td>
									 A
								</td>
							</tr>
							<tr>
								<td>
									 Trident
								</td>
								<td>
									 Internet Explorer 7
								</td>
								<td>
									 Win XP SP2+
								</td>
								<td>
									 7
								</td>
								<td>
									 A
								</td>
							</tr>
							<tr>
								<td>
									 Trident
								</td>
								<td>
									 AOL browser (AOL desktop)
								</td>
								<td>
									 Win XP
								</td>
								<td>
									 6
								</td>
								<td>
									 A
								</td>
							</tr>
							<tr>
								<td>
									 Gecko
								</td>
								<td>
									 Firefox 1.0
								</td>
								<td>
									 Win 98+ / OSX.2+
								</td>
								<td>
									 1.7
								</td>
								<td>
									 A
								</td>
							</tr>
							<tr>
								<td>
									 Gecko
								</td>
								<td>
									 Firefox 1.5
								</td>
								<td>
									 Win 98+ / OSX.2+
								</td>
								<td>
									 1.8
								</td>
								<td>
									 A
								</td>
							</tr>
							<tr>
								<td>
									 Gecko
								</td>
								<td>
									 Firefox 2.0
								</td>
								<td>
									 Win 98+ / OSX.2+
								</td>
								<td>
									 1.8
								</td>
								<td>
									 A
								</td>
							</tr>
							<tr>
								<td>
									 Gecko
								</td>
								<td>
									 Firefox 3.0
								</td>
								<td>
									 Win 2k+ / OSX.3+
								</td>
								<td>
									 1.9
								</td>
								<td>
									 A
								</td>
							</tr>
							<tr>
								<td>
									 Gecko
								</td>
								<td>
									 Camino 1.0
								</td>
								<td>
									 OSX.2+
								</td>
								<td>
									 1.8
								</td>
								<td>
									 A
								</td>
							</tr>
							<tr>
								<td>
									 Gecko
								</td>
								<td>
									 Camino 1.5
								</td>
								<td>
									 OSX.3+
								</td>
								<td>
									 1.8
								</td>
								<td>
									 A
								</td>
							</tr>
							<tr>
								<td>
									 Gecko
								</td>
								<td>
									 Netscape 7.2
								</td>
								<td>
									 Win 95+ / Mac OS 8.6-9.2
								</td>
								<td>
									 1.7
								</td>
								<td>
									 A
								</td>
							</tr>
							<tr>
								<td>
									 Gecko
								</td>
								<td>
									 Netscape Browser 8
								</td>
								<td>
									 Win 98SE+
								</td>
								<td>
									 1.7
								</td>
								<td>
									 A
								</td>
							</tr>
							<tr>
								<td>
									 Gecko
								</td>
								<td>
									 Netscape Navigator 9
								</td>
								<td>
									 Win 98+ / OSX.2+
								</td>
								<td>
									 1.8
								</td>
								<td>
									 A
								</td>
							</tr>
							<tr>
								<td>
									 Gecko
								</td>
								<td>
									 Mozilla 1.0
								</td>
								<td>
									 Win 95+ / OSX.1+
								</td>
								<td>
									 1
								</td>
								<td>
									 A
								</td>
							</tr>
							<tr>
								<td>
									 Gecko
								</td>
								<td>
									 Mozilla 1.1
								</td>
								<td>
									 Win 95+ / OSX.1+
								</td>
								<td>
									 1.1
								</td>
								<td>
									 A
								</td>
							</tr>
							<tr>
								<td>
									 Gecko
								</td>
								<td>
									 Mozilla 1.2
								</td>
								<td>
									 Win 95+ / OSX.1+
								</td>
								<td>
									 1.2
								</td>
								<td>
									 A
								</td>
							</tr>
							<tr>
								<td>
									 Gecko
								</td>
								<td>
									 Mozilla 1.3
								</td>
								<td>
									 Win 95+ / OSX.1+
								</td>
								<td>
									 1.3
								</td>
								<td>
									 A
								</td>
							</tr>
							<tr>
								<td>
									 Gecko
								</td>
								<td>
									 Mozilla 1.4
								</td>
								<td>
									 Win 95+ / OSX.1+
								</td>
								<td>
									 1.4
								</td>
								<td>
									 A
								</td>
							</tr>
							<tr>
								<td>
									 Gecko
								</td>
								<td>
									 Mozilla 1.5
								</td>
								<td>
									 Win 95+ / OSX.1+
								</td>
								<td>
									 1.5
								</td>
								<td>
									 A
								</td>
							</tr>
							<tr>
								<td>
									 Gecko
								</td>
								<td>
									 Mozilla 1.6
								</td>
								<td>
									 Win 95+ / OSX.1+
								</td>
								<td>
									 1.6
								</td>
								<td>
									 A
								</td>
							</tr>
							<tr>
								<td>
									 Gecko
								</td>
								<td>
									 Mozilla 1.7
								</td>
								<td>
									 Win 98+ / OSX.1+
								</td>
								<td>
									 1.7
								</td>
								<td>
									 A
								</td>
							</tr>
							<tr>
								<td>
									 Gecko
								</td>
								<td>
									 Mozilla 1.8
								</td>
								<td>
									 Win 98+ / OSX.1+
								</td>
								<td>
									 1.8
								</td>
								<td>
									 A
								</td>
							</tr>
							<tr>
								<td>
									 Gecko
								</td>
								<td>
									 Seamonkey 1.1
								</td>
								<td>
									 Win 98+ / OSX.2+
								</td>
								<td>
									 1.8
								</td>
								<td>
									 A
								</td>
							</tr>
							<tr>
								<td>
									 Gecko
								</td>
								<td>
									 Epiphany 2.20
								</td>
								<td>
									 Gnome
								</td>
								<td>
									 1.8
								</td>
								<td>
									 A
								</td>
							</tr>
							<tr>
								<td>
									 Webkit
								</td>
								<td>
									 Safari 1.2
								</td>
								<td>
									 OSX.3
								</td>
								<td>
									 125.5
								</td>
								<td>
									 A
								</td>
							</tr>
							<tr>
								<td>
									 Webkit
								</td>
								<td>
									 Safari 1.3
								</td>
								<td>
									 OSX.3
								</td>
								<td>
									 312.8
								</td>
								<td>
									 A
								</td>
							</tr>
							<tr>
								<td>
									 Webkit
								</td>
								<td>
									 Safari 2.0
								</td>
								<td>
									 OSX.4+
								</td>
								<td>
									 419.3
								</td>
								<td>
									 A
								</td>
							</tr>
							<tr>
								<td>
									 Webkit
								</td>
								<td>
									 Safari 3.0
								</td>
								<td>
									 OSX.4+
								</td>
								<td>
									 522.1
								</td>
								<td>
									 A
								</td>
							</tr>
							<tr>
								<td>
									 Webkit
								</td>
								<td>
									 OmniWeb 5.5
								</td>
								<td>
									 OSX.4+
								</td>
								<td>
									 420
								</td>
								<td>
									 A
								</td>
							</tr>
							<tr>
								<td>
									 Webkit
								</td>
								<td>
									 iPod Touch / iPhone
								</td>
								<td>
									 iPod
								</td>
								<td>
									 420.1
								</td>
								<td>
									 A
								</td>
							</tr>
							<tr>
								<td>
									 Webkit
								</td>
								<td>
									 S60
								</td>
								<td>
									 S60
								</td>
								<td>
									 413
								</td>
								<td>
									 A
								</td>
							</tr>
							<tr>
								<td>
									 Presto
								</td>
								<td>
									 Opera 7.0
								</td>
								<td>
									 Win 95+ / OSX.1+
								</td>
								<td>
									 -
								</td>
								<td>
									 A
								</td>
							</tr>
							<tr>
								<td>
									 Presto
								</td>
								<td>
									 Opera 7.5
								</td>
								<td>
									 Win 95+ / OSX.2+
								</td>
								<td>
									 -
								</td>
								<td>
									 A
								</td>
							</tr>
							<tr>
								<td>
									 Presto
								</td>
								<td>
									 Opera 8.0
								</td>
								<td>
									 Win 95+ / OSX.2+
								</td>
								<td>
									 -
								</td>
								<td>
									 A
								</td>
							</tr>
							<tr>
								<td>
									 Presto
								</td>
								<td>
									 Opera 8.5
								</td>
								<td>
									 Win 95+ / OSX.2+
								</td>
								<td>
									 -
								</td>
								<td>
									 A
								</td>
							</tr>
							<tr>
								<td>
									 Presto
								</td>
								<td>
									 Opera 9.0
								</td>
								<td>
									 Win 95+ / OSX.3+
								</td>
								<td>
									 -
								</td>
								<td>
									 A
								</td>
							</tr>
							<tr>
								<td>
									 Presto
								</td>
								<td>
									 Opera 9.2
								</td>
								<td>
									 Win 88+ / OSX.3+
								</td>
								<td>
									 -
								</td>
								<td>
									 A
								</td>
							</tr>
							<tr>
								<td>
									 Presto
								</td>
								<td>
									 Opera 9.5
								</td>
								<td>
									 Win 88+ / OSX.3+
								</td>
								<td>
									 -
								</td>
								<td>
									 A
								</td>
							</tr>
							<tr>
								<td>
									 Presto
								</td>
								<td>
									 Opera for Wii
								</td>
								<td>
									 Wii
								</td>
								<td>
									 -
								</td>
								<td>
									 A
								</td>
							</tr>
							<tr>
								<td>
									 Presto
								</td>
								<td>
									 Nokia N800
								</td>
								<td>
									 N800
								</td>
								<td>
									 -
								</td>
								<td>
									 A
								</td>
							</tr>
							<tr>
								<td>
									 Presto
								</td>
								<td>
									 Nintendo DS browser
								</td>
								<td>
									 Nintendo DS
								</td>
								<td>
									 8.5
								</td>
								<td>
									 C/A<sup>1</sup>
								</td>
							</tr>
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