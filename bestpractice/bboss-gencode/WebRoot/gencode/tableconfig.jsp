<%@ page language="java" pageEncoding="utf-8" session="false"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<!-- BEGIN PAGE HEADER-->
<h3 class="page-title">表单配置-数据源${dbname }-表${tableName }</h3>

<div class="portlet box green">
	<div class="portlet-title">
		<div class="caption">
			<i class="fa fa-gift"></i>请详细填写
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
		<form action="#" id="configform" class="form-horizontal">
			<pg:empty actual="${tableName }" evalbody="true">
				<pg:yes>请选择表</pg:yes>
				<pg:no>
					<div class="form-body">
					<div class="alert alert-danger display-hide">
										<button class="close" data-close="alert"></button>
										You have some form errors. Please check below.
									</div>
									<div class="alert alert-success display-hide">
										<button class="close" data-close="alert"></button>
										Your form validation is successful!
									</div>
					
						<h3 class="form-section">基本信息</h3>
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">数据源</label>
									<div class="col-md-9">
										<input type="hidden" name="dbname" value="${dbname }"
											class="form-control">
										<p class="form-control-static">${dbname }</p>
									</div>
								</div>
							</div>
							<!--/span-->
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">表名称</label>
									<div class="col-md-9">
										<input type="hidden" name="tableName" value="${tableName }"
											class="form-control">
										<p class="form-control-static">${tableName }</p>
									</div>
								</div>
							</div>
							<!--/span-->
						</div>
						<!--/row-->
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">主键名称</label>
									<div class="col-md-9">
										<input id="pkname" name="pkname" type="text"
											class="form-control" placeholder="主键名称"> <span
											class="help-block"><font color="blue">对应于tableinfo表中的table_name字段
										</font></span>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">系统名称</label>
									<div class="col-md-9">
										<input id="system" name="system" type="text"
											class="form-control" placeholder="系统名称"><span
											class="help-block"><font color="blue">可选项 </font></span>
									</div>
								</div>
							</div>
							<!--/span-->

							<!--/span-->
						</div>

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">模块名称 <span
										class="required"> * </span>
									</label>
									<div class="col-md-9">
										<input id="moduleName" name="moduleName" type="text"
											class="form-control" placeholder="模块名称">
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">模块中文名称<span
										class="required"> * </span></label>
									<div class="col-md-9">
										<input id="moduleCNName" name="moduleCNName" type="text"
											class="form-control" placeholder="模块中文名称">
									</div>
								</div>
							</div>
							<!--/span-->

							<!--/span-->
						</div>
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">包路径<span
										class="required"> * </span></label>
									<div class="col-md-9">
										<input id="packagePath" name="packagePath" type="text"
											class="form-control" placeholder="包路径">
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">源码存放路径<span
										class="required"> * </span></label>
									<div class="col-md-9">
										<input id="sourcedir" name="sourcedir" type="text"
											class="form-control" placeholder="源码存放路径">
									</div>
								</div>
							</div>
							<!--/span-->

							<!--/span-->
						</div>
						<div class="row">

						
							<div class="col-md-6">
								<div class="form-group">

									<label class="control-label col-md-3">界面风格</label>
									<div class="col-md-9">
										<select class="form-control" id="theme" name="theme">
											<option value="default">default</option>
											<option value="bootstrap">bootstrap</option>
											<option value="mobile">mobile</option>
										</select>
									</div>

								</div>
							</div>

							<!--/span-->
						</div>
						<div class="row">
													<div class="col-md-6">
														<div class="form-group">
															<label class="control-label col-md-3">代码控制参数</label>
									<div class="col-md-9">
										<input type="hidden" id="controlParams" name="controlParams"
											class="form-control  select2"
											value="geni18n, clearSourcedir,genRPC,autopk">
									</div>
														</div>
													</div>
												</div>

						<div class="row">
							
							<div class="col-md-6">
								<div class="form-group">
									
									
									<label class="control-label col-md-3">excel版本号</label>
									<div class="col-md-9">
										<select class="form-control" id="excelVersion"
											name="excelVersion">
											<option value="2003">2003</option>
											<option value="2007">2007</option>
											<option value="2010">2010</option>
											<option value="2013">2013</option>
										</select>
									</div>
								</div>
							</div>
							<!--/span-->

							<!--/span-->
						</div>
						 
						<h3 class="form-section">版权信息</h3>
						<!--/row-->
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">作者<span class="required">*</span></label>
									<div class="col-md-9">
										<input type="text" id="author" name="author"  class="form-control" >
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">公司<span class="required">*</span></label>
									<div class="col-md-9">
										<input type="text" id="company" name="company"  class="form-control" >
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">版本号<span class="required">*</span></label>
									<div class="col-md-9">
										<input type="text" id="version" name="version" class="form-control"   value="v1.0">
									</div>
								</div>
							</div>
							<!--/span-->
							
							<!--/span-->
						</div>
						 
						 
						<!--/row-->
						<h3 class="form-section">字段设置</h3>
						<!-- table -->
						<div class="table-scrollable">
								<table class="table table-striped table-bordered table-advance table-hover">
								<thead>
								<tr>
									<th scope="col" >
										 <i class="fa fa-briefcase"></i>字段名称
									</th>
									
									<th scope="col">
										 <i class="fa fa-shopping-cart">字段类型
									</th>
									<th scope="col">
										 <i class="fa fa-user">属性名称
									</th>
									<th scope="col">
										 java类型
									</th>
									<th scope="col">
										 中文名称
									</th>
									
									<th scope="col">
										 必填项
									</th>
									<th scope="col">
										 类型校验
									</th>
									<th scope="col">
										 日期格式
									</th>
									<th scope="col">
										 日期范围
									</th>
									<th scope="col">
										 查询条件
									</th>
									<th scope="col">
										 查询方式
									</th>
									<th scope="col">
										排序字段
									</th>
									<th scope="col">
										排序方式
									</th>
									<th scope="col">
										是否隐藏
									</th>
									<th scope="col">
										默认值
									</th>
								</tr>
								</thead>
								<tbody>
								<pg:list requestKey="fields">
														
															<tr>
																<td><pg:cell colName="columnname"/><input type="hidden" name="columnname" class="form-control" value="<pg:cell colName="columnname"/>"><input type="hidden" name="rowid" class="form-control" value="<pg:rowid/>"></td>
															
																<td><pg:cell colName="columntype"/><input type="hidden" name="columntype" class="form-control" value="<pg:cell colName="columnname"/>"></td>
																	<td><pg:cell colName="fieldName"/><input type="hidden" name="fieldName" class="form-control" value="<pg:cell colName="fieldName"/>"><input type="hidden" name="mfieldName" class="form-control" value="<pg:cell colName="mfieldName"/>"></td>
																<td><select  class="form-control input-small select2me" name="type" id="type">
																	<pg:case colName="type">
																	<option value="String" <pg:equal value="String">selected</pg:equal>>String</option>
																	<option value="long" <pg:equal value="long">selected</pg:equal>>long</option>
																	<option value="int" <pg:equal value="int">selected</pg:equal>>int</option>
																	<option value="Timestamp" <pg:equal value="Timestamp">selected</pg:equal>>Timestamp</option>
																	<option value="Date" <pg:equal value="Date">selected</pg:equal>>Date</option>
																	<option value="url" <pg:equal value="url">selected</pg:equal>>url</option>
																	<option value="creditcard" <pg:equal value="creditcard">selected</pg:equal>>creditcard</option>
																	<option value="email" <pg:equal value="email">selected</pg:equal>>email</option>
																	<option value="file" <pg:equal value="file">selected</pg:equal>>file</option>
																	<option value="idcard" <pg:equal value="idcard">selected</pg:equal>>idcard</option>
																	</pg:case>
																</select>
											
																</td>
																<td><input type="text" placeholder="<pg:cell colName="fieldName"/>" name="fieldCNName" class="form-control  input-small" value="<pg:cell colName="fieldCNName"/>"></td>
																<td > 
																<div class="radio-list"><label><input type="radio" name="<pg:rowid/>_typecheck" id="<pg:rowid/>_typecheck" value="1">是			</label>													 
																<label ><input type="radio" name="<pg:rowid/>_typecheck" id="<pg:rowid/>_typecheck" value="0"  checked>否	</label></div>															 
															    </td>
																<td ><div class="radio-list"><label ><input type="radio" name="<pg:rowid/>_required" id="<pg:rowid/>_required" value="1">是			</label>													 
																<label  ><input type="radio" name="<pg:rowid/>_required" id="<pg:rowid/>_required" value="0"  checked>否	</label></div>		</td>
																<td ><select  class="form-control  input-medium select2me" name="dateformat" id="dateformat">
																	<pg:case colName="dateformat">
																	<option value="yyyy-MM-dd HH:mm:ss" <pg:equal value="yyyy-MM-dd HH:mm:ss">selected</pg:equal>>yyyy-MM-dd HH:mm:ss</option>
																	<option value="yyyy/MM/dd HH:mm:ss" <pg:equal value="yyyy/MM/dd HH:mm:ss">selected</pg:equal>>yyyy/MM/dd HH:mm:ss</option>
																	</pg:case>
																</select>
																</td>
																<td ><div class="radio-list"><label ><input type="radio" name="<pg:rowid/>_daterange" id="<pg:rowid/>_daterange" value="1">是</label>																
																<label ><input type="radio" name="<pg:rowid/>_daterange" id="<pg:rowid/>_daterange" value="0"  checked>否</label></div></td>
																<td ><div class="radio-list"><label ><input type="radio" name="<pg:rowid/>_qcondition" id="<pg:rowid/>_qcondition" value="1">是</label>																
																<label ><input type="radio" name="<pg:rowid/>_qcondition" id="<pg:rowid/>_qcondition" value="0"  checked>否</label></div></td>
																<td >
																<div class="radio-list"><label ><input type="radio" name="<pg:rowid/>_qtype" id="<pg:rowid/>_qtype" value="1">模糊</label>																
																<label ><input type="radio" name="<pg:rowid/>_qtype" id="<pg:rowid/>_qtype" value="0"  checked>精确</label></div>																
															 	</td>
															 	<td >
																<div class="radio-list"><label ><input type="radio" name="<pg:rowid/>_sfield" id="<pg:rowid/>_sfield" value="1">是</label>																
																<label ><input type="radio" name="<pg:rowid/>_sfield" id="<pg:rowid/>_sfield" value="0"  checked>否</label></div>																
															 	</td>
															 	<td >
																<div class="radio-list"><label ><input type="radio" name="<pg:rowid/>_stype" id="<pg:rowid/>_stype" value="1">降序</label>																
																<label ><input type="radio" name="<pg:rowid/>_stype" id="<pg:rowid/>_stype" value="0"  checked>升序</label></div>																
															 	</td>
																<td >
																<div class="radio-list"><label ><input type="radio" name="<pg:rowid/>_hidden" id="<pg:rowid/>_hidden" value="1">是</label>																
																<label ><input type="radio" name="<pg:rowid/>_hidden" id="<pg:rowid/>_hidden" value="0"  checked>否</label></div>																
															 	</td>
																<td><input type="text" placeholder="<pg:cell colName="fieldName"/>"  name="defaultValue" class="form-control  input-small" value="<pg:cell colName="defaultValue"/>"></td>
															</tr>
													</pg:list>	
								
								</tbody>
								</table>
							</div>
						
						<!-- /table -->
					</div>
					<div class="form-actions">
						<div class="row">
							<div class="col-md-6">
								<div class="row">
									<div class="col-md-offset-3 col-md-9">
										<button type="submit" class="btn green">提交</button>
									</div>
								</div>
							</div>
							<div class="col-md-6"></div>
						</div>
					</div>
				</pg:no>
			</pg:empty>
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
<script>
jQuery(document).ready(function() {
	var handleValidation2 = function() {
        // for more info visit the official plugin documentation: 
            // http://docs.jquery.com/Plugins/Validation

            var form2 = $('#configform');
            

            var error1 = $('.alert-danger', form2);
            var success1 = $('.alert-success', form2);
            form2.validate({
            	errorElement: 'span', //default input error message container
                errorClass: 'help-block help-block-error', // default input error message class
                focusInvalid: false, // do not focus the last invalid input
                ignore: "",  // validate all fields including form hidden input
                messages: {
                    select_multi: {
                        maxlength: jQuery.validator.format("Max {0} items allowed for selection"),
                        minlength: jQuery.validator.format("At least {0} items must be selected")
                    }
                },
                rules: {
                	moduleName: {
                        minlength: 1,
                        required: true
                    } ,
                    
                    moduleCNName: {
                        minlength: 1,
                        required: true
                    }  ,
                    
                    packagePath: {
                        minlength: 1,
                        required: true
                    } ,
                    
                    sourcedir: {
                        minlength: 1,
                        required: true
                    } ,
                    
                    author: {
                        minlength: 1,
                        required: true
                    } ,
                    
                    company: {
                        minlength: 1,
                        required: true
                    },
                    
                    version: {
                        minlength: 1,
                        required: true
                    }    
                },

                invalidHandler: function (event, validator) { //display error alert on form submit              
                	success1.hide();
                    error1.show();
                    Metronic.scrollTo(error1, -200);
                },

                highlight: function (element) { // hightlight error inputs
                    $(element)
                        .closest('.form-group').addClass('has-error'); // set error class to the control group
                },

                unhighlight: function (element) { // revert the change done by hightlight
                    $(element)
                        .closest('.form-group').removeClass('has-error'); // set error class to the control group
                },

                success: function (label) {
                    label
                        .closest('.form-group').removeClass('has-error'); // set success class to the control group
                },

                submitHandler: function (form) {
                	 success1.show();
                     error1.hide();
                }
			});
        };
            handleValidation2();
	 $("#controlParams").select2({
         tags: ["geni18n", "clearSourcedir","genRPC","autopk","genwf"]
     });
});

</script>

<!-- END PAGE CONTENT-->