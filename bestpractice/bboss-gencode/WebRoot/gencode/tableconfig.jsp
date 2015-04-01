<%@ page language="java" pageEncoding="utf-8" session="false"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<!-- BEGIN PAGE HEADER-->
 
<div class="portlet box green">
	<div class="portlet-title">
		<div class="caption">
			<i class="fa fa-gift"></i>表单配置 <small>${tableName}-${dbname }
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
								<select name="dbname" class="select2me form-control" onchange="ComponentsDropdowns.loadtables(event,'tableName')">
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
							<label class="control-label col-md-3">选择表<span class="required">
										* </span></label>
										
										
							<div class="col-md-9">
								<select name="tableName" id="tableName"  data-required="1"  class="select2me form-control">
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
						
						<a href="tableconfig.page" class="btn blue ajaxify" formid="tableform"> 进入表单配置步骤</a>
					</div>
				</div>
			</div>
		</form>
	</div>
</div>
<!-- END PAGE CONTENT-->