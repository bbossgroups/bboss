<%@ page language="java" pageEncoding="utf-8" session="false"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<!DOCTYPE html>
<!-- 
Template Name: Metronic - Responsive Admin Dashboard Template build with Twitter Bootstrap 3.0.2
Version: 1.5.4
Author: KeenThemes
Website: http://www.keenthemes.com/
Purchase: http://themeforest.net/item/metronic-responsive-admin-dashboard-template/4021469?ref=keenthemes
-->
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en" class="no-js">
<!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
<meta charset="utf-8" />
<title>BBoss | 自动代码生成 - 配置管理</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta content="width=device-width, initial-scale=1.0" name="viewport" />
<meta content="" name="description" />
<meta content="" name="author" />
<meta name="MobileOptimized" content="320">
<!-- BEGIN GLOBAL MANDATORY STYLES -->
<link href="../../assets/plugins/font-awesome/css/font-awesome.min.css"
	rel="stylesheet" type="text/css" />
<link href="../../assets/plugins/bootstrap/css/bootstrap.min.css"
	rel="stylesheet" type="text/css" />
<link href="../../assets/plugins/uniform/css/uniform.default.css"
	rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css"
	href="../../assets/plugins/select2/select2_metro.css" />
<!-- END GLOBAL MANDATORY STYLES -->
<!-- BEGIN THEME STYLES -->
<link href="../../assets/css/style-metronic.css" rel="stylesheet"
	type="text/css" />
<link href="../../assets/css/style.css" rel="stylesheet" type="text/css" />
<link href="../../assets/css/style-responsive.css" rel="stylesheet"
	type="text/css" />
<link href="../../assets/css/plugins.css" rel="stylesheet"
	type="text/css" />
<link href="../../assets/css/themes/default.css" rel="stylesheet"
	type="text/css" id="style_color" />
<link href="../../assets/css/custom.css" rel="stylesheet"
	type="text/css" />
<!-- END THEME STYLES -->
<link rel="shortcut icon" href="../../favicon.ico" />
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<body class="page-header-fixed">
	<!-- BEGIN HEADER -->
	<div class="header navbar navbar-inverse navbar-fixed-top">
		<!-- BEGIN TOP NAVIGATION BAR -->
		<div class="header-inner">
			<!-- BEGIN LOGO -->
			<a class="navbar-brand" href="index.html"> <img
				src="../../assets/img/logo.png" alt="logo" class="img-responsive" />
			</a>
			<!-- END LOGO -->
			<!-- BEGIN RESPONSIVE MENU TOGGLER -->
			<a href="javascript:;" class="navbar-toggle" data-toggle="collapse"
				data-target=".navbar-collapse"> <img
				src="../../assets/img/menu-toggler.png" alt="" />
			</a>
			<!-- END RESPONSIVE MENU TOGGLER -->
			<!-- BEGIN TOP NAVIGATION MENU -->
			<ul class="nav navbar-nav pull-right">
				<!-- BEGIN NOTIFICATION DROPDOWN -->
				<li class="dropdown" id="header_notification_bar"><a href="#"
					class="dropdown-toggle" data-toggle="dropdown"
					data-hover="dropdown" data-close-others="true"> <i
						class="fa fa-warning"></i> <span class="badge">6</span>
				</a>
					<ul class="dropdown-menu extended notification">
						<li>
							<p>You have 14 new notifications</p>
						</li>
						<li>
							<ul class="dropdown-menu-list scroller" style="height: 250px;">
								<li><a href="#"> <span
										class="label label-icon label-success"><i
											class="fa fa-plus"></i></span> New user registered. <span
										class="time">Just now</span>
								</a></li>
								<li><a href="#"> <span
										class="label label-icon label-danger"><i
											class="fa fa-bolt"></i></span> Server #12 overloaded. <span
										class="time">15 mins</span>
								</a></li>
								<li><a href="#"> <span
										class="label label-icon label-warning"><i
											class="fa fa-bell-o"></i></span> Server #2 not responding. <span
										class="time">22 mins</span>
								</a></li>
								<li><a href="#"> <span
										class="label label-icon label-info"><i
											class="fa fa-bullhorn"></i></span> Application error. <span
										class="time">40 mins</span>
								</a></li>
								<li><a href="#"> <span
										class="label label-icon label-danger"><i
											class="fa fa-bolt"></i></span> Database overloaded 68%. <span
										class="time">2 hrs</span>
								</a></li>
								<li><a href="#"> <span
										class="label label-icon label-danger"><i
											class="fa fa-bolt"></i></span> 2 user IP blocked. <span class="time">5
											hrs</span>
								</a></li>
								<li><a href="#"> <span
										class="label label-icon label-warning"><i
											class="fa fa-bell-o"></i></span> Storage Server #4 not responding. <span
										class="time">45 mins</span>
								</a></li>
								<li><a href="#"> <span
										class="label label-icon label-info"><i
											class="fa fa-bullhorn"></i></span> System Error. <span class="time">55
											mins</span>
								</a></li>
								<li><a href="#"> <span
										class="label label-icon label-danger"><i
											class="fa fa-bolt"></i></span> Database overloaded 68%. <span
										class="time">2 hrs</span>
								</a></li>
							</ul>
						</li>
						<li class="external"><a href="#">See all notifications <i
								class="m-icon-swapright"></i></a></li>
					</ul></li>
				<!-- END NOTIFICATION DROPDOWN -->
				<!-- BEGIN INBOX DROPDOWN -->
				<li class="dropdown" id="header_inbox_bar"><a href="#"
					class="dropdown-toggle" data-toggle="dropdown"
					data-hover="dropdown" data-close-others="true"> <i
						class="fa fa-envelope"></i> <span class="badge">5</span>
				</a>
					<ul class="dropdown-menu extended inbox">
						<li>
							<p>You have 12 new messages</p>
						</li>
						<li>
							<ul class="dropdown-menu-list scroller" style="height: 250px;">
								<li><a href="inbox.html?a=view"> <span class="photo"><img
											src="../../assets/img/avatar2.jpg" alt="" /></span> <span
										class="subject"> <span class="from">Lisa Wong</span> <span
											class="time">Just Now</span>
									</span> <span class="message"> Vivamus sed auctor nibh congue
											nibh. auctor nibh auctor nibh... </span>
								</a></li>
								<li><a href="inbox.html?a=view"> <span class="photo"><img
											src="../../assets/img/avatar3.jpg" alt="" /></span> <span
										class="subject"> <span class="from">Richard Doe</span>
											<span class="time">16 mins</span>
									</span> <span class="message"> Vivamus sed congue nibh auctor
											nibh congue nibh. auctor nibh auctor nibh... </span>
								</a></li>
								<li><a href="inbox.html?a=view"> <span class="photo"><img
											src="../../assets/img/avatar1.jpg" alt="" /></span> <span
										class="subject"> <span class="from">Bob Nilson</span> <span
											class="time">2 hrs</span>
									</span> <span class="message"> Vivamus sed nibh auctor nibh
											congue nibh. auctor nibh auctor nibh... </span>
								</a></li>
								<li><a href="inbox.html?a=view"> <span class="photo"><img
											src="../../assets/img/avatar2.jpg" alt="" /></span> <span
										class="subject"> <span class="from">Lisa Wong</span> <span
											class="time">40 mins</span>
									</span> <span class="message"> Vivamus sed auctor 40% nibh
											congue nibh... </span>
								</a></li>
								<li><a href="inbox.html?a=view"> <span class="photo"><img
											src="../../assets/img/avatar3.jpg" alt="" /></span> <span
										class="subject"> <span class="from">Richard Doe</span>
											<span class="time">46 mins</span>
									</span> <span class="message"> Vivamus sed congue nibh auctor
											nibh congue nibh. auctor nibh auctor nibh... </span>
								</a></li>
							</ul>
						</li>
						<li class="external"><a href="inbox.html">See all
								messages <i class="m-icon-swapright"></i>
						</a></li>
					</ul></li>
				<!-- END INBOX DROPDOWN -->
				<!-- BEGIN TODO DROPDOWN -->
				<li class="dropdown" id="header_task_bar"><a href="#"
					class="dropdown-toggle" data-toggle="dropdown"
					data-hover="dropdown" data-close-others="true"> <i
						class="fa fa-tasks"></i> <span class="badge">5</span>
				</a>
					<ul class="dropdown-menu extended tasks">
						<li>
							<p>You have 12 pending tasks</p>
						</li>
						<li>
							<ul class="dropdown-menu-list scroller" style="height: 250px;">
								<li><a href="#"> <span class="task"> <span
											class="desc">New release v1.2</span> <span class="percent">30%</span>
									</span> <span class="progress"> <span style="width: 40%;"
											class="progress-bar progress-bar-success" aria-valuenow="40"
											aria-valuemin="0" aria-valuemax="100"> <span
												class="sr-only">40% Complete</span>
										</span>
									</span>
								</a></li>
								<li><a href="#"> <span class="task"> <span
											class="desc">Application deployment</span> <span
											class="percent">65%</span>
									</span> <span class="progress progress-striped"> <span
											style="width: 65%;" class="progress-bar progress-bar-danger"
											aria-valuenow="65" aria-valuemin="0" aria-valuemax="100">
												<span class="sr-only">65% Complete</span>
										</span>
									</span>
								</a></li>
								<li><a href="#"> <span class="task"> <span
											class="desc">Mobile app release</span> <span class="percent">98%</span>
									</span> <span class="progress"> <span style="width: 98%;"
											class="progress-bar progress-bar-success" aria-valuenow="98"
											aria-valuemin="0" aria-valuemax="100"> <span
												class="sr-only">98% Complete</span>
										</span>
									</span>
								</a></li>
								<li><a href="#"> <span class="task"> <span
											class="desc">Database migration</span> <span class="percent">10%</span>
									</span> <span class="progress progress-striped"> <span
											style="width: 10%;" class="progress-bar progress-bar-warning"
											aria-valuenow="10" aria-valuemin="0" aria-valuemax="100">
												<span class="sr-only">10% Complete</span>
										</span>
									</span>
								</a></li>
								<li><a href="#"> <span class="task"> <span
											class="desc">Web server upgrade</span> <span class="percent">58%</span>
									</span> <span class="progress progress-striped"> <span
											style="width: 58%;" class="progress-bar progress-bar-info"
											aria-valuenow="58" aria-valuemin="0" aria-valuemax="100">
												<span class="sr-only">58% Complete</span>
										</span>
									</span>
								</a></li>
								<li><a href="#"> <span class="task"> <span
											class="desc">Mobile development</span> <span class="percent">85%</span>
									</span> <span class="progress progress-striped"> <span
											style="width: 85%;" class="progress-bar progress-bar-success"
											aria-valuenow="85" aria-valuemin="0" aria-valuemax="100">
												<span class="sr-only">85% Complete</span>
										</span>
									</span>
								</a></li>
								<li><a href="#"> <span class="task"> <span
											class="desc">New UI release</span> <span class="percent">18%</span>
									</span> <span class="progress progress-striped"> <span
											style="width: 18%;"
											class="progress-bar progress-bar-important"
											aria-valuenow="18" aria-valuemin="0" aria-valuemax="100">
												<span class="sr-only">18% Complete</span>
										</span>
									</span>
								</a></li>
							</ul>
						</li>
						<li class="external"><a href="#">See all tasks <i
								class="m-icon-swapright"></i></a></li>
					</ul></li>
				<!-- END TODO DROPDOWN -->
				<!-- BEGIN USER LOGIN DROPDOWN -->
				<li class="dropdown user"><a href="#" class="dropdown-toggle"
					data-toggle="dropdown" data-hover="dropdown"
					data-close-others="true"> <img alt=""
						src="../../assets/img/avatar1_small.jpg" /> <span class="username">Yin Biao Ping</span> <i class="fa fa-angle-down"></i>
				</a>
					<ul class="dropdown-menu">
						<li><a href="extra_profile.html"><i class="fa fa-user"></i>
								My Profile</a></li>
						<li><a href="page_calendar.html"><i
								class="fa fa-calendar"></i> My Calendar</a></li>
						<li><a href="inbox.html"><i class="fa fa-envelope"></i>
								My Inbox <span class="badge badge-danger">3</span></a></li>
						<li><a href="#"><i class="fa fa-tasks"></i> My Tasks <span
								class="badge badge-success">7</span></a></li>
						<li class="divider"></li>
						<li><a href="javascript:;" id="trigger_fullscreen"><i
								class="fa fa-move"></i> Full Screen</a></li>
						<li><a href="extra_lock.html"><i class="fa fa-lock"></i>
								Lock Screen</a></li>
						<li><a href="login.html"><i class="fa fa-key"></i> Log
								Out</a></li>
					</ul></li>
				<!-- END USER LOGIN DROPDOWN -->
			</ul>
			<!-- END TOP NAVIGATION MENU -->
		</div>
		<!-- END TOP NAVIGATION BAR -->
	</div>
	<!-- END HEADER -->
	<div class="clearfix"></div>
	<!-- BEGIN CONTAINER -->
	<div class="page-container">
		<!-- BEGIN SIDEBAR -->
		<div class="page-sidebar navbar-collapse collapse">
			<!-- BEGIN SIDEBAR MENU -->
			<ul class="page-sidebar-menu">
				<li>
					<!-- BEGIN SIDEBAR TOGGLER BUTTON -->
					<div class="sidebar-toggler hidden-phone"></div> <!-- BEGIN SIDEBAR TOGGLER BUTTON -->
				</li>
				<li>
					<!-- BEGIN RESPONSIVE QUICK SEARCH FORM -->
					<form class="sidebar-search" action="extra_search.html"
						method="POST">
						<div class="form-container">
							<div class="input-box">
								<a href="javascript:;" class="remove"></a> <input type="text"
									placeholder="Search..." /> <input type="button" class="submit"
									value=" " />
							</div>
						</div>
					</form> <!-- END RESPONSIVE QUICK SEARCH FORM -->
				</li>

				<li class="active "><a href="javascript:;"> <i
						class="fa fa-th"></i> <span class="title">BSPF DB Tables</span> <span
						class="selected"></span> <span class="arrow open"></span>
				</a>
					<ul class="sub-menu">
						<pg:list requestKey="tables">
							<li class="tooltips" data-placement="right"
								data-original-title="<pg:cell colName="tableName"/>"><a
								href="tableset.page?tableName=<pg:cell colName="tableName"/>&dbname=bspf">
									<pg:cell colName="tableName" maxlength="17" replace="..." />
							</a></li>
						</pg:list>

					</ul></li>
				<li class=""><a href="javascript:;"> <i
						class="fa fa-file-text"></i> <span class="title">Portlets</span> <span
						class="arrow "></span>
				</a>
					<ul class="sub-menu">
						<li><a href="portlet_general.html"> General Portlets</a></li>
						<li><a href="portlet_draggable.html"> Draggable Portlets</a>
						</li>
					</ul></li>
				<li class=""><a href="javascript:;"> <i
						class="fa fa-map-marker"></i> <span class="title">Maps</span> <span
						class="arrow "></span>
				</a>
					<ul class="sub-menu">
						<li><a href="maps_google.html"> Google Maps</a></li>
						<li><a href="maps_vector.html"> Vector Maps</a></li>
					</ul></li>
				<li class="last "><a href="charts.html"> <i
						class="fa fa-bar-chart-o"></i> <span class="title">Visual
							Charts</span>
				</a></li>
			</ul>
			<!-- END SIDEBAR MENU -->
		</div>
		<!-- END SIDEBAR -->
		<!-- BEGIN PAGE -->
		<div class="page-content">
			<!-- BEGIN SAMPLE PORTLET CONFIGURATION MODAL FORM-->
			<div class="modal fade" id="portlet-config" tabindex="-1"
				role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal"
								aria-hidden="true"></button>
							<h4 class="modal-title">Modal title</h4>
						</div>
						<div class="modal-body">Widget settings form goes here</div>
						<div class="modal-footer">
							<button type="button" class="btn blue">Save changes</button>
							<button type="button" class="btn default" data-dismiss="modal">Close</button>
						</div>
					</div>
					<!-- /.modal-content -->
				</div>
				<!-- /.modal-dialog -->
			</div>
			<!-- /.modal -->
			<!-- END SAMPLE PORTLET CONFIGURATION MODAL FORM-->
			<!-- BEGIN STYLE CUSTOMIZER -->
			<div class="theme-panel hidden-xs hidden-sm">
				<div class="toggler"></div>
				<div class="toggler-close"></div>
				<div class="theme-options">
					<div class="theme-option theme-colors clearfix">
						<span>THEME COLOR</span>
						<ul>
							<li class="color-black current color-default"
								data-style="default"></li>
							<li class="color-blue" data-style="blue"></li>
							<li class="color-brown" data-style="brown"></li>
							<li class="color-purple" data-style="purple"></li>
							<li class="color-grey" data-style="grey"></li>
							<li class="color-white color-light" data-style="light"></li>
						</ul>
					</div>
					<div class="theme-option">
						<span>Layout</span> <select
							class="layout-option form-control input-small">
							<option value="fluid" selected="selected">Fluid</option>
							<option value="boxed">Boxed</option>
						</select>
					</div>
					<div class="theme-option">
						<span>Header</span> <select
							class="header-option form-control input-small">
							<option value="fixed" selected="selected">Fixed</option>
							<option value="default">Default</option>
						</select>
					</div>
					<div class="theme-option">
						<span>Sidebar</span> <select
							class="sidebar-option form-control input-small">
							<option value="fixed">Fixed</option>
							<option value="default" selected="selected">Default</option>
						</select>
					</div>
					<div class="theme-option">
						<span>Footer</span> <select
							class="footer-option form-control input-small">
							<option value="fixed">Fixed</option>
							<option value="default" selected="selected">Default</option>
						</select>
					</div>
				</div>
			</div>
			
			<!-- END BEGIN STYLE CUSTOMIZER -->
			<!-- BEGIN PAGE HEADER-->
			<div class="row">
				<div class="col-md-12">
					<!-- BEGIN PAGE TITLE & BREADCRUMB-->
					<h3 class="page-title">
						BBoss自动代码生成框架 <small>配置管理</small>
					</h3>
					
					<ul class="page-breadcrumb breadcrumb">
						<li class="btn-group">
							<button type="button" class="btn blue dropdown-toggle"
								data-toggle="dropdown" data-hover="dropdown" data-delay="1000"
								data-close-others="true">
								<span>Actions</span> <i class="fa fa-angle-down"></i>
							</button>
							<ul class="dropdown-menu pull-right" role="menu">
								<li><a href="#">Action</a></li>
								<li><a href="#">Another action</a></li>
								<li><a href="#">Something else here</a></li>
								<li class="divider"></li>
								<li><a href="#">Separated link</a></li>
							</ul>
						</li>
						<li><i class="fa fa-home"></i> <a href="index.html">首页</a> <i
							class="fa fa-angle-right"></i></li>
						<li><a href="#">自动代码生成框架</a> <i class="fa fa-angle-right"></i>
						</li>
						<li><a href="#">配置管理</a></li>
					</ul>
					<!-- END PAGE TITLE & BREADCRUMB-->
					
				</div>
			</div>
			<!-- END PAGE HEADER-->
			<!-- BEGIN PAGE CONTENT-->
			<div class="row">
				<div class="col-md-12">

					<!-- BEGIN SAMPLE TABLE PORTLET-->


					<div class="portlet box green">
						<div class="portlet-title">
							<div class="caption">
								<i class="fa fa-reorder"></i>表[${tableName}]代码生成
							</div>
							<div class="tools">
								<a href="javascript:;" class="collapse"></a> <a
									href="#portlet-config" data-toggle="modal" class="config"></a>
								<a href="javascript:;" class="reload"></a> <a
									href="javascript:;" class="remove"></a>
							</div>
						</div>
						<div class="portlet-body form flip-scroll">
						<form action="#" id="tableset" class="form-horizontal">
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
											<div class="form-group ">
												<label class="control-label col-md-3">表名称<span class="required">*</span></label>
												<div class="col-md-9">
													<div class="input-icon right">                                       
														<i class="fa"></i>   
														<input type="text" id="tableName" name="tableName" class="form-control" value="${tableName}">                          
													</div>
												
												</div>
											</div>
										
										</div>
										<!--/span-->
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-3">主键名称</label>
												<div class="col-md-9">
													<input type="text" id="pkname" name="pkname" class="form-control" placeholder="主键名称" value="${tableName}">
												</div>
											</div>
										</div>
										<!--/span-->
									</div>
									<!--/row-->
									<div class="row">
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-3">系统名称</label>
												<div class="col-md-9">
													<input type="text" id="system" name="system" class="form-control" placeholder="系统名称">
												</div>
											</div>
										</div>
										<!--/span-->
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-3">数据源名称</label>
												<div class="col-md-9">
													<input type="text"  id="datasourceName" name="datasourceName" class="form-control" placeholder="数据源名称"
														value="${dbname }">
												</div>
											</div>
										</div>
										<!--/span-->
									</div>
									<!--/row-->
									<div class="row">
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-3">模块名称<span class="required">*</span></label>
												<div class="col-md-9"><div class="input-icon right">                                       
														<i class="fa"></i>   
													<input type="text" id="moduleName" name="moduleName" class="form-control"></div>
												</div>
											</div>
										</div>
										<!--/span-->
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-3">模块中文名称<span class="required">*</span></label>
												<div class="col-md-9"><div class="input-icon right">                                       
														<i class="fa"></i>   
													<input type="text" id="moduleCNName" name="moduleCNName"  class="form-control"
														></div>
												</div>
											</div>
										</div>
										<!--/span-->
									</div>
									<!--/row-->
									<div class="row">
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-3">包路径<span class="required">*</span></label>
												<div class="col-md-9"><div class="input-icon right">                                       
													<i class="fa"></i> 
													<input type="text" id="packagePath" name="packagePath"  class="form-control"></div>
												</div>
											</div>
										</div>
										<!--/span-->
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-3">源码存放目录<span class="required">*</span></label>
												<div class="col-md-9"><div class="input-icon right">                                       
													<i class="fa"></i> 
													<input type="text" id="sourcedir" name="sourcedir"  class="form-control"
														></div>
												</div>
											</div>
										</div>
										<!--/span-->
									</div>
									<!--/row-->
									<div class="row">

										<!--/span-->
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-3">代码控制参数</label>
												<div class="col-md-9">
													<input type="hidden" id="controlParams" name="controlParams"  class="form-control select2_sample3"
														value="clearSourcedir">
												</div>
											</div>
										</div>
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-3">界面风格</label>
												<div class="col-md-9">
													<input type="hidden" id="theme" name="theme"  class="form-control select2_sample4"
														value="default">
												</div>
											</div>
										</div>
										
										<!--/span-->
									</div>
									<h3 class="form-section">查询字段</h3>
									<div class="row">

										<!--/span-->
										<div class="col-md-12">
											<div class="form-group">
												<div class="col-md-12">
													<input type="hidden" id="queryFields" name="queryFields"  class="form-control select2_sample5">
												</div>
											</div>
										</div>
										
										
										<!--/span-->
									</div>
									<h3 class="form-section">排序字段</h3>
									<div class="row">										
										<div class="col-md-12">
											<div class="form-group">
												
												<div class="col-md-12">
													<input type="hidden" id="sortFields" name="sortFields"  class="form-control select2_sample6">
												</div>
											</div>
										</div>
										
										<!--/span-->
									</div>

									<h3 class="form-section">版权信息</h3>
									<!--/row-->
									<div class="row">
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-3">作者<span class="required">*</span></label>
												<div class="col-md-9"><div class="input-icon right">                                       
													<i class="fa"></i> 
													<input type="text" id="author" name="author"  class="form-control" ></div>
												</div>
											</div>
										</div>
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-3">公司<span class="required">*</span></label>
												<div class="col-md-9"><div class="input-icon right">                                       
													<i class="fa"></i> 
													<input type="text" id="company" name="company"  class="form-control" ></div>
												</div>
											</div>
										</div>
									</div>
									<div class="row">
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-3">版本号<span class="required">*</span></label>
												<div class="col-md-9"><div class="input-icon right">                                       
													<i class="fa"></i> 
													<input type="text" id="version" name="version" class="form-control"   value="v1.0"></div>
												</div>
											</div>
										</div>
										<!--/span-->
										
										<!--/span-->
									</div>
									
									<h3 class="form-section">字段属性设置</h3>
									<div class="row">
										<div class="col-md-12">
											<div class="form-group">
												<div class="col-md-12">	
												<div class="table-scrollable">											
												<table
													class="table table-striped table-bordered table-hover">
													<thead  >
														<tr>
															<th>字段名称</th>
															<th>属性名称</th>															
															<th>字段类型</th>
															<th>中文名称</th>
															<th>java类型</th>
															<th>必填项</th>
															<th>类型校验</th>
															<th>日期格式</th>	
															<th>日期范围</th>	
															<th>查询方式</th>
															<th>排序方式</th>
															<th>是否隐藏</th>				
														</tr>
													</thead>
													<tbody>
													<pg:list requestKey="fields">
														
															<tr>
																<td><pg:cell colName="columnname"/><input type="hidden" name="columnname" class="form-control" value="<pg:cell colName="columnname"/>"></td>
																<td><pg:cell colName="fieldName"/><input type="hidden" name="fieldName" class="form-control" value="<pg:cell colName="columnname"/>"></td>
																
																<td><pg:cell colName="columntype"/><input type="hidden" name="columntype" class="form-control" value="<pg:cell colName="columnname"/>"></td>
																<td><input type="text" name="fieldCNName" class="form-control" value="<pg:cell colName="fieldName"/>"></td>
																<td><input type="text" name="type" class="form-control" value="<pg:cell colName="type"/>"></td>
																<td > 
																<div class="radio-list"><label><input type="radio" name="<pg:rowid/>_typecheck" id="<pg:rowid/>_typecheck" value="1">是			</label>													 
																<label ><input type="radio" name="<pg:rowid/>_typecheck" id="<pg:rowid/>_typecheck" value="0"  checked>否	</label></div>															 
															    </td>
																<td ><div class="radio-list"><label ><input type="radio" name="<pg:rowid/>_required" id="<pg:rowid/>_required" value="1">是			</label>													 
																<label  ><input type="radio" name="<pg:rowid/>_required" id="<pg:rowid/>_required" value="0"  checked>否	</label></div>		</td>
																<td ><input type="text" name="dateformat" class="form-control" value="<pg:cell colName="dateformat" defaultValue="yyyy-MM-dd HH:mm:ss"/>"></td>
																<td ><div class="radio-list"><label ><input type="radio" name="<pg:rowid/>_daterange" id="<pg:rowid/>_daterange" value="1">是</label>																
																<label ><input type="radio" name="<pg:rowid/>_daterange" id="<pg:rowid/>_daterange" value="0"  checked>否</label></div></td>
																<td >
																<div class="radio-list"><label ><input type="radio" name="<pg:rowid/>_qtype" id="<pg:rowid/>_qtype" value="1">模糊</label>																
																<label ><input type="radio" name="<pg:rowid/>_qtype" id="<pg:rowid/>_qtype" value="0"  checked>精确</label></div>																
															 	</td>
															 	<td >
																<div class="radio-list"><label ><input type="radio" name="<pg:rowid/>_stype" id="<pg:rowid/>_stype" value="1">降序</label>																
																<label ><input type="radio" name="<pg:rowid/>_stype" id="<pg:rowid/>_stype" value="0"  checked>升序</label></div>																
															 	</td>
																<td >
																<div class="radio-list"><label ><input type="radio" name="<pg:rowid/>_hidden" id="<pg:rowid/>_hidden" value="1">是</label>																
																<label ><input type="radio" name="<pg:rowid/>_hidden" id="<pg:rowid/>_hidden" value="0"  checked>否</label></div>																
															 	</td>
																
															</tr>
													</pg:list>	
													</tbody>
												</table>
												</div>
												</div>
											</div>
										</div>
									</div>
									<!--/row-->
								</div>
								<div class="form-actions fluid">
									<div class="row">
										<div class="col-md-6">
											<div class="col-md-offset-3 col-md-9">
												<button type="submit" class="btn green">生成代码</button>
												<button type="button" class="btn default">暂存</button>
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
					<div class="note note-info">
						<h4 class="block">自动代码生成框架功能说明</h4>
						<p>
							根据模板，自动生成给定表的增、删、改、分页查询、列表查询、国际化功能对应的程序和配置文件：
							<ul>
							<li>1.mvc控制器</li>
							<li>2.业务组件</li>
							<li>3.实体类 </li>
							<li>4.jsp文件  可以定制不同风格的界面模板，目前提供了平台的基础ui风格</li>
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

				</div>
				
			</div>
			
			<!-- END PAGE CONTENT-->
		</div>
		<!-- END PAGE -->
	</div>
	<!-- END CONTAINER -->
	<!-- BEGIN FOOTER -->
	<div class="footer">
		<div class="footer-inner">2015 &copy; BBoss Code Gen Framework by Biaoping.Yin.</div>
		<div class="footer-tools">
			<span class="go-top"> <i class="fa fa-angle-up"></i>
			</span>
		</div>
	</div>
	<!-- END FOOTER -->
	<!-- BEGIN JAVASCRIPTS(Load javascripts at bottom, this will reduce page load time) -->
	<!-- BEGIN CORE PLUGINS -->
	<!--[if lt IE 9]>
	<script src="../../assets/plugins/respond.min.js"></script>
	<script src="../../assets/plugins/excanvas.min.js"></script> 
	<![endif]-->
	<script src="../../assets/plugins/jquery-1.10.2.min.js"
		type="text/javascript"></script>
	<script src="../../assets/plugins/jquery-migrate-1.2.1.min.js"
		type="text/javascript"></script>
	<script src="../../assets/plugins/bootstrap/js/bootstrap.min.js"
		type="text/javascript"></script>
	<script
		src="../../assets/plugins/bootstrap-hover-dropdown/twitter-bootstrap-hover-dropdown.min.js"
		type="text/javascript"></script>
	<script
		src="../../assets/plugins/jquery-slimscroll/jquery.slimscroll.min.js"
		type="text/javascript"></script>
	<script src="../../assets/plugins/jquery.blockui.min.js"
		type="text/javascript"></script>
	<script src="../../assets/plugins/jquery.cookie.min.js"
		type="text/javascript"></script>
	<script src="../../assets/plugins/uniform/jquery.uniform.min.js"
		type="text/javascript"></script>
	<!-- END CORE PLUGINS -->
	<script type="text/javascript" src="../../assets/plugins/jquery-validation/dist/jquery.validate.min.js"></script>
	<script type="text/javascript" src="../../assets/plugins/jquery-validation/dist/additional-methods.min.js"></script>
	<script type="text/javascript"
		src="../../assets/plugins/select2/select2.min.js"></script>
	<script src="../../assets/scripts/app.js" type="text/javascript"></script>
	<script src="../../assets/scripts/form-tableset-validation.js"></script> 
	<script>
		jQuery(document).ready(function() {
			// initiate layout and plugins
			App.init('../..');
			 FormValidation.init();
			 $(".select2_sample3").select2({
	                tags: ["geni18n", "clearSourcedir","genRPC","autopk","genworkflow"]
	            });
	            
	            $(".select2_sample4").select2({
	                tags: ["default", "bootstrap"]
	            });
			 $(".select2_sample5").select2({
	                tags: [<pg:list requestKey="fields"><pg:upper expression="{rowid}" value="0">,</pg:upper>'<pg:cell colName="columnname"/>'</pg:list>]
	            });
			 $(".select2_sample6").select2({
	                tags: [<pg:list requestKey="fields"><pg:upper expression="{rowid}" value="0">,</pg:upper>'<pg:cell colName="columnname"/>'</pg:list>]
	            });
		});
		
	</script>
</body>
<!-- END BODY -->
</html>