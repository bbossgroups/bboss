<%@ page language="java" pageEncoding="utf-8"%>
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
<!--[if !IE]><!--> <html lang="en" class="no-js"> <!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
	<meta charset="utf-8" />
	<title>Metronic | Data Tables - Responsive Datatables</title>
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta content="width=device-width, initial-scale=1.0" name="viewport" />
	<meta content="" name="description" />
	<meta content="" name="author" />
	<meta name="MobileOptimized" content="320">
	<!-- BEGIN GLOBAL MANDATORY STYLES -->          
	<link href="assets/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
	<link href="assets/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
	<link href="assets/plugins/uniform/css/uniform.default.css" rel="stylesheet" type="text/css"/>
	<!-- END GLOBAL MANDATORY STYLES -->
	<!-- BEGIN THEME STYLES --> 
	<link href="assets/css/style-metronic.css" rel="stylesheet" type="text/css"/>
	<link href="assets/css/style.css" rel="stylesheet" type="text/css"/>
	<link href="assets/css/style-responsive.css" rel="stylesheet" type="text/css"/>
	<link href="assets/css/plugins.css" rel="stylesheet" type="text/css"/>
	<link href="assets/css/themes/default.css" rel="stylesheet" type="text/css" id="style_color"/>
	<link href="assets/css/custom.css" rel="stylesheet" type="text/css"/>
	<!-- END THEME STYLES -->
	<link rel="shortcut icon" href="favicon.ico" />
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<body class="page-header-fixed">
	<!-- BEGIN HEADER -->   
	<div class="header navbar navbar-inverse navbar-fixed-top">
		<!-- BEGIN TOP NAVIGATION BAR -->
		<div class="header-inner">
			<!-- BEGIN LOGO -->  
			<a class="navbar-brand" href="index.html">
			<img src="assets/img/logo.png" alt="logo" class="img-responsive" />
			</a>
			<!-- END LOGO -->
			<!-- BEGIN RESPONSIVE MENU TOGGLER --> 
			<a href="javascript:;" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
			<img src="assets/img/menu-toggler.png" alt="" />
			</a> 
			<!-- END RESPONSIVE MENU TOGGLER -->
			<!-- BEGIN TOP NAVIGATION MENU -->
			<ul class="nav navbar-nav pull-right">
				<!-- BEGIN NOTIFICATION DROPDOWN -->
				<li class="dropdown" id="header_notification_bar">
					<a href="#" class="dropdown-toggle" data-toggle="dropdown" data-hover="dropdown"
						data-close-others="true">
					<i class="fa fa-warning"></i>
					<span class="badge">6</span>
					</a>
					<ul class="dropdown-menu extended notification">
						<li>
							<p>You have 14 new notifications</p>
						</li>
						<li>
							<ul class="dropdown-menu-list scroller" style="height: 250px;">
								<li>  
									<a href="#">
									<span class="label label-icon label-success"><i class="fa fa-plus"></i></span>
									New user registered. 
									<span class="time">Just now</span>
									</a>
								</li>
								<li>  
									<a href="#">
									<span class="label label-icon label-danger"><i class="fa fa-bolt"></i></span>
									Server #12 overloaded. 
									<span class="time">15 mins</span>
									</a>
								</li>
								<li>  
									<a href="#">
									<span class="label label-icon label-warning"><i class="fa fa-bell-o"></i></span>
									Server #2 not responding.
									<span class="time">22 mins</span>
									</a>
								</li>
								<li>  
									<a href="#">
									<span class="label label-icon label-info"><i class="fa fa-bullhorn"></i></span>
									Application error.
									<span class="time">40 mins</span>
									</a>
								</li>
								<li>  
									<a href="#">
									<span class="label label-icon label-danger"><i class="fa fa-bolt"></i></span>
									Database overloaded 68%. 
									<span class="time">2 hrs</span>
									</a>
								</li>
								<li>  
									<a href="#">
									<span class="label label-icon label-danger"><i class="fa fa-bolt"></i></span>
									2 user IP blocked.
									<span class="time">5 hrs</span>
									</a>
								</li>
								<li>  
									<a href="#">
									<span class="label label-icon label-warning"><i class="fa fa-bell-o"></i></span>
									Storage Server #4 not responding.
									<span class="time">45 mins</span>
									</a>
								</li>
								<li>  
									<a href="#">
									<span class="label label-icon label-info"><i class="fa fa-bullhorn"></i></span>
									System Error.
									<span class="time">55 mins</span>
									</a>
								</li>
								<li>  
									<a href="#">
									<span class="label label-icon label-danger"><i class="fa fa-bolt"></i></span>
									Database overloaded 68%. 
									<span class="time">2 hrs</span>
									</a>
								</li>
							</ul>
						</li>
						<li class="external">   
							<a href="#">See all notifications <i class="m-icon-swapright"></i></a>
						</li>
					</ul>
				</li>
				<!-- END NOTIFICATION DROPDOWN -->
				<!-- BEGIN INBOX DROPDOWN -->
				<li class="dropdown" id="header_inbox_bar">
					<a href="#" class="dropdown-toggle" data-toggle="dropdown" data-hover="dropdown"
						data-close-others="true">
					<i class="fa fa-envelope"></i>
					<span class="badge">5</span>
					</a>
					<ul class="dropdown-menu extended inbox">
						<li>
							<p>You have 12 new messages</p>
						</li>
						<li>
							<ul class="dropdown-menu-list scroller" style="height: 250px;">
								<li>  
									<a href="inbox.html?a=view">
									<span class="photo"><img src="./assets/img/avatar2.jpg" alt=""/></span>
									<span class="subject">
									<span class="from">Lisa Wong</span>
									<span class="time">Just Now</span>
									</span>
									<span class="message">
									Vivamus sed auctor nibh congue nibh. auctor nibh
									auctor nibh...
									</span>  
									</a>
								</li>
								<li>  
									<a href="inbox.html?a=view">
									<span class="photo"><img src="./assets/img/avatar3.jpg" alt=""/></span>
									<span class="subject">
									<span class="from">Richard Doe</span>
									<span class="time">16 mins</span>
									</span>
									<span class="message">
									Vivamus sed congue nibh auctor nibh congue nibh. auctor nibh
									auctor nibh...
									</span>  
									</a>
								</li>
								<li>  
									<a href="inbox.html?a=view">
									<span class="photo"><img src="./assets/img/avatar1.jpg" alt=""/></span>
									<span class="subject">
									<span class="from">Bob Nilson</span>
									<span class="time">2 hrs</span>
									</span>
									<span class="message">
									Vivamus sed nibh auctor nibh congue nibh. auctor nibh
									auctor nibh...
									</span>  
									</a>
								</li>
								<li>  
									<a href="inbox.html?a=view">
									<span class="photo"><img src="./assets/img/avatar2.jpg" alt=""/></span>
									<span class="subject">
									<span class="from">Lisa Wong</span>
									<span class="time">40 mins</span>
									</span>
									<span class="message">
									Vivamus sed auctor 40% nibh congue nibh...
									</span>  
									</a>
								</li>
								<li>  
									<a href="inbox.html?a=view">
									<span class="photo"><img src="./assets/img/avatar3.jpg" alt=""/></span>
									<span class="subject">
									<span class="from">Richard Doe</span>
									<span class="time">46 mins</span>
									</span>
									<span class="message">
									Vivamus sed congue nibh auctor nibh congue nibh. auctor nibh
									auctor nibh...
									</span>  
									</a>
								</li>
							</ul>
						</li>
						<li class="external">   
							<a href="inbox.html">See all messages <i class="m-icon-swapright"></i></a>
						</li>
					</ul>
				</li>
				<!-- END INBOX DROPDOWN -->
				<!-- BEGIN TODO DROPDOWN -->
				<li class="dropdown" id="header_task_bar">
					<a href="#" class="dropdown-toggle" data-toggle="dropdown" data-hover="dropdown" data-close-others="true">
					<i class="fa fa-tasks"></i>
					<span class="badge">5</span>
					</a>
					<ul class="dropdown-menu extended tasks">
						<li>
							<p>You have 12 pending tasks</p>
						</li>
						<li>
							<ul class="dropdown-menu-list scroller" style="height: 250px;">
								<li>  
									<a href="#">
									<span class="task">
									<span class="desc">New release v1.2</span>
									<span class="percent">30%</span>
									</span>
									<span class="progress">
									<span style="width: 40%;" class="progress-bar progress-bar-success" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100">
									<span class="sr-only">40% Complete</span>
									</span>
									</span>
									</a>
								</li>
								<li>  
									<a href="#">
									<span class="task">
									<span class="desc">Application deployment</span>
									<span class="percent">65%</span>
									</span>
									<span class="progress progress-striped">
									<span style="width: 65%;" class="progress-bar progress-bar-danger" aria-valuenow="65" aria-valuemin="0" aria-valuemax="100">
									<span class="sr-only">65% Complete</span>
									</span>
									</span>
									</a>
								</li>
								<li>  
									<a href="#">
									<span class="task">
									<span class="desc">Mobile app release</span>
									<span class="percent">98%</span>
									</span>
									<span class="progress">
									<span style="width: 98%;" class="progress-bar progress-bar-success" aria-valuenow="98" aria-valuemin="0" aria-valuemax="100">
									<span class="sr-only">98% Complete</span>
									</span>
									</span>
									</a>
								</li>
								<li>  
									<a href="#">
									<span class="task">
									<span class="desc">Database migration</span>
									<span class="percent">10%</span>
									</span>
									<span class="progress progress-striped">
									<span style="width: 10%;" class="progress-bar progress-bar-warning" aria-valuenow="10" aria-valuemin="0" aria-valuemax="100">
									<span class="sr-only">10% Complete</span>
									</span>
									</span>
									</a>
								</li>
								<li>  
									<a href="#">
									<span class="task">
									<span class="desc">Web server upgrade</span>
									<span class="percent">58%</span>
									</span>
									<span class="progress progress-striped">
									<span style="width: 58%;" class="progress-bar progress-bar-info" aria-valuenow="58" aria-valuemin="0" aria-valuemax="100">
									<span class="sr-only">58% Complete</span>
									</span>
									</span>
									</a>
								</li>
								<li>  
									<a href="#">
									<span class="task">
									<span class="desc">Mobile development</span>
									<span class="percent">85%</span>
									</span>
									<span class="progress progress-striped">
									<span style="width: 85%;" class="progress-bar progress-bar-success" aria-valuenow="85" aria-valuemin="0" aria-valuemax="100">
									<span class="sr-only">85% Complete</span>
									</span>
									</span>
									</a>
								</li>
								<li>  
									<a href="#">
									<span class="task">
									<span class="desc">New UI release</span>
									<span class="percent">18%</span>
									</span>
									<span class="progress progress-striped">
									<span style="width: 18%;" class="progress-bar progress-bar-important" aria-valuenow="18" aria-valuemin="0" aria-valuemax="100">
									<span class="sr-only">18% Complete</span>
									</span>
									</span>
									</a>
								</li>
							</ul>
						</li>
						<li class="external">   
							<a href="#">See all tasks <i class="m-icon-swapright"></i></a>
						</li>
					</ul>
				</li>
				<!-- END TODO DROPDOWN -->
				<!-- BEGIN USER LOGIN DROPDOWN -->
				<li class="dropdown user">
					<a href="#" class="dropdown-toggle" data-toggle="dropdown" data-hover="dropdown" data-close-others="true">
					<img alt="" src="assets/img/avatar1_small.jpg"/>
					<span class="username">Bob Nilson</span>
					<i class="fa fa-angle-down"></i>
					</a>
					<ul class="dropdown-menu">
						<li><a href="extra_profile.html"><i class="fa fa-user"></i> My Profile</a></li>
						<li><a href="page_calendar.html"><i class="fa fa-calendar"></i> My Calendar</a></li>
						<li><a href="inbox.html"><i class="fa fa-envelope"></i> My Inbox <span class="badge badge-danger">3</span></a></li>
						<li><a href="#"><i class="fa fa-tasks"></i> My Tasks <span class="badge badge-success">7</span></a></li>
						<li class="divider"></li>
						<li><a href="javascript:;" id="trigger_fullscreen"><i class="fa fa-move"></i> Full Screen</a></li>
						<li><a href="extra_lock.html"><i class="fa fa-lock"></i> Lock Screen</a></li>
						<li><a href="login.html"><i class="fa fa-key"></i> Log Out</a></li>
					</ul>
				</li>
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
					<div class="sidebar-toggler hidden-phone"></div>
					<!-- BEGIN SIDEBAR TOGGLER BUTTON -->
				</li>
				<li>
					<!-- BEGIN RESPONSIVE QUICK SEARCH FORM -->
					<form class="sidebar-search" action="extra_search.html" method="POST">
						<div class="form-container">
							<div class="input-box">
								<a href="javascript:;" class="remove"></a>
								<input type="text" placeholder="Search..."/>
								<input type="button" class="submit" value=" "/>
							</div>
						</div>
					</form>
					<!-- END RESPONSIVE QUICK SEARCH FORM -->
				</li>
				
				<li class="active ">
					<a href="javascript:;">
					<i class="fa fa-th"></i> 
					<span class="title">Data Tables</span>
					<span class="selected"></span>
					<span class="arrow open"></span>
					</a>
					<ul class="sub-menu">
						<li >
							<a href="table_basic.html">
							Basic Datatables</a>
						</li>
						<li class="active">
							<a href="tableset.page">
							Responsive Datatables</a>
						</li>
						<li >
							<a href="table_managed.html">
							Managed Datatables</a>
						</li>
						<li >
							<a href="table_editable.html">
							Editable Datatables</a>
						</li>
						<li >
							<a href="table_advanced.html">
							Advanced Datatables</a>
						</li>
						<li >
							<a href="table_ajax.html">
							Ajax Datatables</a>
						</li>
					</ul>
				</li>
				<li class="">
					<a href="javascript:;">
					<i class="fa fa-file-text"></i> 
					<span class="title">Portlets</span>
					<span class="arrow "></span>
					</a>
					<ul class="sub-menu">
						<li >
							<a href="portlet_general.html">
							General Portlets</a>
						</li>
						<li >
							<a href="portlet_draggable.html">
							Draggable Portlets</a>
						</li>
					</ul>
				</li>
				<li class="">
					<a href="javascript:;">
					<i class="fa fa-map-marker"></i> 
					<span class="title">Maps</span>
					<span class="arrow "></span>
					</a>
					<ul class="sub-menu">
						<li >
							<a href="maps_google.html">
							Google Maps</a>
						</li>
						<li >
							<a href="maps_vector.html">
							Vector Maps</a>
						</li>
					</ul>
				</li>
				<li class="last ">
					<a href="charts.html">
					<i class="fa fa-bar-chart-o"></i> 
					<span class="title">Visual Charts</span>
					</a>
				</li>
			</ul>
			<!-- END SIDEBAR MENU -->
		</div>
		<!-- END SIDEBAR -->
		<!-- BEGIN PAGE -->
		<div class="page-content">
			<!-- BEGIN SAMPLE PORTLET CONFIGURATION MODAL FORM-->               
			<div class="modal fade" id="portlet-config" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
							<h4 class="modal-title">Modal title</h4>
						</div>
						<div class="modal-body">
							Widget settings form goes here
						</div>
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
							<li class="color-black current color-default" data-style="default"></li>
							<li class="color-blue" data-style="blue"></li>
							<li class="color-brown" data-style="brown"></li>
							<li class="color-purple" data-style="purple"></li>
							<li class="color-grey" data-style="grey"></li>
							<li class="color-white color-light" data-style="light"></li>
						</ul>
					</div>
					<div class="theme-option">
						<span>Layout</span>
						<select class="layout-option form-control input-small">
							<option value="fluid" selected="selected">Fluid</option>
							<option value="boxed">Boxed</option>
						</select>
					</div>
					<div class="theme-option">
						<span>Header</span>
						<select class="header-option form-control input-small">
							<option value="fixed" selected="selected">Fixed</option>
							<option value="default">Default</option>
						</select>
					</div>
					<div class="theme-option">
						<span>Sidebar</span>
						<select class="sidebar-option form-control input-small">
							<option value="fixed">Fixed</option>
							<option value="default" selected="selected">Default</option>
						</select>
					</div>
					<div class="theme-option">
						<span>Footer</span>
						<select class="footer-option form-control input-small">
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
						Responsive Datatables <small>responsive datatable samples</small>
					</h3>
					<ul class="page-breadcrumb breadcrumb">
						<li class="btn-group">
							<button type="button" class="btn blue dropdown-toggle" data-toggle="dropdown" data-hover="dropdown" data-delay="1000" data-close-others="true">
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
						<li>
							<i class="fa fa-home"></i>
							<a href="index.html">Home</a> 
							<i class="fa fa-angle-right"></i>
						</li>
						<li>
							<a href="#">Data Tables</a>
							<i class="fa fa-angle-right"></i>
						</li>
						<li><a href="#">Responsive Datatables</a></li>
					</ul>
					<!-- END PAGE TITLE & BREADCRUMB-->
				</div>
			</div>
			<!-- END PAGE HEADER-->
			<!-- BEGIN PAGE CONTENT-->          
			<div class="row">
				<div class="col-md-12">
					<div class="note note-success">
						<p>
							Please try to re-size your browser window in order to see the tables in responsive mode.
						</p>
					</div>
					<!-- BEGIN SAMPLE TABLE PORTLET-->
					<div class="portlet box green">
						<div class="portlet-title">
							<div class="caption"><i class="fa fa-cogs"></i>Responsive Flip Scroll Tables</div>
							<div class="tools">
								<a href="javascript:;" class="collapse"></a>
								<a href="#portlet-config" data-toggle="modal" class="config"></a>
								<a href="javascript:;" class="reload"></a>
								<a href="javascript:;" class="remove"></a>
							</div>
						</div>
						<div class="portlet-body flip-scroll">
							<table class="table table-bordered table-striped table-condensed flip-content">
								<thead class="flip-content">
									<tr>
										<th>Code</th>
										<th>Company</th>
										<th class="numeric">Price</th>
										<th class="numeric">Change</th>
										<th class="numeric">Change %</th>
										<th class="numeric">Open</th>
										<th class="numeric">High</th>
										<th class="numeric">Low</th>
										<th class="numeric">Volume</th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td>AAC</td>
										<td>AUSTRALIAN AGRICULTURAL COMPANY LIMITED.</td>
										<td class="numeric">$1.38</td>
										<td class="numeric">-0.01</td>
										<td class="numeric">-0.36%</td>
										<td class="numeric">$1.39</td>
										<td class="numeric">$1.39</td>
										<td class="numeric">$1.38</td>
										<td class="numeric">9,395</td>
									</tr>
									<tr>
										<td>AAD</td>
										<td>ARDENT LEISURE GROUP</td>
										<td class="numeric">$1.15</td>
										<td class="numeric">  +0.02</td>
										<td class="numeric">1.32%</td>
										<td class="numeric">$1.14</td>
										<td class="numeric">$1.15</td>
										<td class="numeric">$1.13</td>
										<td class="numeric">56,431</td>
									</tr>
									<tr>
										<td>AAX</td>
										<td>AUSENCO LIMITED</td>
										<td class="numeric">$4.00</td>
										<td class="numeric">-0.04</td>
										<td class="numeric">-0.99%</td>
										<td class="numeric">$4.01</td>
										<td class="numeric">$4.05</td>
										<td class="numeric">$4.00</td>
										<td class="numeric">90,641</td>
									</tr>
									<tr>
										<td>ABC</td>
										<td>ADELAIDE BRIGHTON LIMITED</td>
										<td class="numeric">$3.00</td>
										<td class="numeric">  +0.06</td>
										<td class="numeric">2.04%</td>
										<td class="numeric">$2.98</td>
										<td class="numeric">$3.00</td>
										<td class="numeric">$2.96</td>
										<td class="numeric">862,518</td>
									</tr>
									<tr>
										<td>ABP</td>
										<td>ABACUS PROPERTY GROUP</td>
										<td class="numeric">$1.91</td>
										<td class="numeric">0.00</td>
										<td class="numeric">0.00%</td>
										<td class="numeric">$1.92</td>
										<td class="numeric">$1.93</td>
										<td class="numeric">$1.90</td>
										<td class="numeric">595,701</td>
									</tr>
									<tr>
										<td>ABY</td>
										<td>ADITYA BIRLA MINERALS LIMITED</td>
										<td class="numeric">$0.77</td>
										<td class="numeric">  +0.02</td>
										<td class="numeric">2.00%</td>
										<td class="numeric">$0.76</td>
										<td class="numeric">$0.77</td>
										<td class="numeric">$0.76</td>
										<td class="numeric">54,567</td>
									</tr>
									<tr>
										<td>ACR</td>
										<td>ACRUX LIMITED</td>
										<td class="numeric">$3.71</td>
										<td class="numeric">  +0.01</td>
										<td class="numeric">0.14%</td>
										<td class="numeric">$3.70</td>
										<td class="numeric">$3.72</td>
										<td class="numeric">$3.68</td>
										<td class="numeric">191,373</td>
									</tr>
									<tr>
										<td>ADU</td>
										<td>ADAMUS RESOURCES LIMITED</td>
										<td class="numeric">$0.72</td>
										<td class="numeric">0.00</td>
										<td class="numeric">0.00%</td>
										<td class="numeric">$0.73</td>
										<td class="numeric">$0.74</td>
										<td class="numeric">$0.72</td>
										<td class="numeric">8,602,291</td>
									</tr>
									<tr>
										<td>AGG</td>
										<td>ANGLOGOLD ASHANTI LIMITED</td>
										<td class="numeric">$7.81</td>
										<td class="numeric">-0.22</td>
										<td class="numeric">-2.74%</td>
										<td class="numeric">$7.82</td>
										<td class="numeric">$7.82</td>
										<td class="numeric">$7.81</td>
										<td class="numeric">148</td>
									</tr>
									<tr>
										<td>AGK</td>
										<td>AGL ENERGY LIMITED</td>
										<td class="numeric">$13.82</td>
										<td class="numeric">  +0.02</td>
										<td class="numeric">0.14%</td>
										<td class="numeric">$13.83</td>
										<td class="numeric">$13.83</td>
										<td class="numeric">$13.67</td>
										<td class="numeric">846,403</td>
									</tr>
									<tr>
										<td>AGO</td>
										<td>ATLAS IRON LIMITED</td>
										<td class="numeric">$3.17</td>
										<td class="numeric">-0.02</td>
										<td class="numeric">-0.47%</td>
										<td class="numeric">$3.11</td>
										<td class="numeric">$3.22</td>
										<td class="numeric">$3.10</td>
										<td class="numeric">5,416,303</td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
					<!-- END SAMPLE TABLE PORTLET-->
					<!-- BEGIN SAMPLE TABLE PORTLET-->
					<div class="portlet box red">
						<div class="portlet-title">
							<div class="caption"><i class="fa fa-cogs"></i>Basic Bootstrap 3.0 Responsive Table</div>
							<div class="tools">
								<a href="javascript:;" class="collapse"></a>
								<a href="#portlet-config" data-toggle="modal" class="config"></a>
								<a href="javascript:;" class="reload"></a>
								<a href="javascript:;" class="remove"></a>
							</div>
						</div>
						<div class="portlet-body">
							<div class="table-responsive">
								<table class="table">
									<thead>
										<tr>
											<th>#</th>
											<th>Table heading</th>
											<th>Table heading</th>
											<th>Table heading</th>
											<th>Table heading</th>
											<th>Table heading</th>
											<th>Table heading</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td>1</td>
											<td>Table cell</td>
											<td>Table cell</td>
											<td>Table cell</td>
											<td>Table cell</td>
											<td>Table cell</td>
											<td>Table cell</td>
										</tr>
										<tr>
											<td>2</td>
											<td>Table cell</td>
											<td>Table cell</td>
											<td>Table cell</td>
											<td>Table cell</td>
											<td>Table cell</td>
											<td>Table cell</td>
										</tr>
										<tr>
											<td>3</td>
											<td>Table cell</td>
											<td>Table cell</td>
											<td>Table cell</td>
											<td>Table cell</td>
											<td>Table cell</td>
											<td>Table cell</td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
					</div>
					<!-- END SAMPLE TABLE PORTLET-->
					<!-- BEGIN SAMPLE TABLE PORTLET-->
					<div class="portlet box blue">
						<div class="portlet-title">
							<div class="caption"><i class="fa fa-cogs"></i>Bordered Bootstrap 3.0 Responsive Table</div>
							<div class="tools">
								<a href="javascript:;" class="collapse"></a>
								<a href="#portlet-config" data-toggle="modal" class="config"></a>
								<a href="javascript:;" class="reload"></a>
								<a href="javascript:;" class="remove"></a>
							</div>
						</div>
						<div class="portlet-body">
							<div class="table-responsive">
								<table class="table table-bordered">
									<thead>
										<tr>
											<th>#</th>
											<th>Table heading</th>
											<th>Table heading</th>
											<th>Table heading</th>
											<th>Table heading</th>
											<th>Table heading</th>
											<th>Table heading</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td>1</td>
											<td>Table cell</td>
											<td>Table cell</td>
											<td>Table cell</td>
											<td>Table cell</td>
											<td>Table cell</td>
											<td>Table cell</td>
										</tr>
										<tr>
											<td>2</td>
											<td>Table cell</td>
											<td>Table cell</td>
											<td>Table cell</td>
											<td>Table cell</td>
											<td>Table cell</td>
											<td>Table cell</td>
										</tr>
										<tr>
											<td>3</td>
											<td>Table cell</td>
											<td>Table cell</td>
											<td>Table cell</td>
											<td>Table cell</td>
											<td>Table cell</td>
											<td>Table cell</td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
					</div>
					<!-- END SAMPLE TABLE PORTLET-->
					<!-- BEGIN SAMPLE TABLE PORTLET-->
					<div class="portlet box yellow">
						<div class="portlet-title">
							<div class="caption"><i class="fa fa-cogs"></i>All in One Bootstrap 3.0 Responsive Table</div>
							<div class="tools">
								<a href="javascript:;" class="collapse"></a>
								<a href="#portlet-config" data-toggle="modal" class="config"></a>
								<a href="javascript:;" class="reload"></a>
								<a href="javascript:;" class="remove"></a>
							</div>
						</div>
						<div class="portlet-body">
							<div class="table-responsive">
								<table class="table table-striped table-bordered table-hover">
									<thead>
										<tr>
											<th>#</th>
											<th>Table heading</th>
											<th>Table heading</th>
											<th>Table heading</th>
											<th>Table heading</th>
											<th>Table heading</th>
											<th>Table heading</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td>1</td>
											<td>Table cell</td>
											<td>Table cell</td>
											<td>Table cell</td>
											<td>Table cell</td>
											<td>Table cell</td>
											<td>Table cell</td>
										</tr>
										<tr>
											<td>2</td>
											<td>Table cell</td>
											<td>Table cell</td>
											<td>Table cell</td>
											<td>Table cell</td>
											<td>Table cell</td>
											<td>Table cell</td>
										</tr>
										<tr>
											<td>3</td>
											<td>Table cell</td>
											<td>Table cell</td>
											<td>Table cell</td>
											<td>Table cell</td>
											<td>Table cell</td>
											<td>Table cell</td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
					</div>
					<!-- END SAMPLE TABLE PORTLET-->
					<!-- BEGIN SAMPLE TABLE PORTLET-->
					<div class="portlet box purple">
						<div class="portlet-title">
							<div class="caption"><i class="fa fa-cogs"></i>Horizontal Scrollable Responsive Table</div>
							<div class="tools">
								<a href="javascript:;" class="collapse"></a>
								<a href="#portlet-config" data-toggle="modal" class="config"></a>
								<a href="javascript:;" class="reload"></a>
								<a href="javascript:;" class="remove"></a>
							</div>
						</div>
						<div class="portlet-body">
							<div class="table-scrollable">
								<table class="table table-striped table-bordered table-hover">
									<thead>
										<tr>
											<th scope="col" style="width:450px !important">Column header 1</th>
											<th scope="col" >Column header 2</th>
											<th scope="col" >Column header 3</th>
											<th scope="col" >Column header 4</th>
											<th scope="col" >Column header 5</th>
											<th scope="col" >Column header 6</th>
											<th scope="col" >Column header 7</th>
											<th scope="col" >Column header 8</th>
											<th scope="col" >Column header 9</th>
											<th scope="col" >Column header 10</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td>Table data</td>
											<td>Table data</td>
											<td>Table data</td>
											<td>Table data</td>
											<td>Table data</td>
											<td>Table data</td>
											<td>Table data</td>
											<td>Table data</td>
											<td>Table data</td>
											<td>Table data</td>
										</tr>
										<tr>
											<td>Table data</td>
											<td>Table data</td>
											<td>Table data</td>
											<td>Table data</td>
											<td>Table data</td>
											<td>Table data</td>
											<td>Table data</td>
											<td>Table data</td>
											<td>Table data</td>
											<td>Table data</td>
										</tr>
										<tr>
											<td>Table data</td>
											<td>Table data</td>
											<td>Table data</td>
											<td>Table data</td>
											<td>Table data</td>
											<td>Table data</td>
											<td>Table data</td>
											<td>Table data</td>
											<td>Table data</td>
											<td>Table data</td>
										</tr>
										<tr>
											<td>Table data</td>
											<td>Table data</td>
											<td>Table data</td>
											<td>Table data</td>
											<td>Table data</td>
											<td>Table data</td>
											<td>Table data</td>
											<td>Table data</td>
											<td>Table data</td>
											<td>Table data</td>
										</tr>
										<tr>
											<td>Table data</td>
											<td>Table data</td>
											<td>Table data</td>
											<td>Table data</td>
											<td>Table data</td>
											<td>Table data</td>
											<td>Table data</td>
											<td>Table data</td>
											<td>Table data</td>
											<td>Table data</td>
										</tr>
										<tr>
											<td>Table data</td>
											<td>Table data</td>
											<td>Table data</td>
											<td>Table data</td>
											<td>Table data</td>
											<td>Table data</td>
											<td>Table data</td>
											<td>Table data</td>
											<td>Table data</td>
											<td>Table data</td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
					</div>
					<!-- END SAMPLE TABLE PORTLET-->
				</div>
			</div>
			<!-- END PAGE CONTENT-->
		</div>
		<!-- END PAGE -->
	</div>
	<!-- END CONTAINER -->
	<!-- BEGIN FOOTER -->
	<div class="footer">
		<div class="footer-inner">
			2013 &copy; Metronic by keenthemes.
		</div>
		<div class="footer-tools">
			<span class="go-top">
			<i class="fa fa-angle-up"></i>
			</span>
		</div>
	</div>
	<!-- END FOOTER -->
	<!-- BEGIN JAVASCRIPTS(Load javascripts at bottom, this will reduce page load time) -->
	<!-- BEGIN CORE PLUGINS -->   
	<!--[if lt IE 9]>
	<script src="assets/plugins/respond.min.js"></script>
	<script src="assets/plugins/excanvas.min.js"></script> 
	<![endif]-->   
	<script src="assets/plugins/jquery-1.10.2.min.js" type="text/javascript"></script>
	<script src="assets/plugins/jquery-migrate-1.2.1.min.js" type="text/javascript"></script>    
	<script src="assets/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
	<script src="assets/plugins/bootstrap-hover-dropdown/twitter-bootstrap-hover-dropdown.min.js" type="text/javascript" ></script>
	<script src="assets/plugins/jquery-slimscroll/jquery.slimscroll.min.js" type="text/javascript"></script>
	<script src="assets/plugins/jquery.blockui.min.js" type="text/javascript"></script>  
	<script src="assets/plugins/jquery.cookie.min.js" type="text/javascript"></script>
	<script src="assets/plugins/uniform/jquery.uniform.min.js" type="text/javascript" ></script>
	<!-- END CORE PLUGINS -->
	<script src="assets/scripts/app.js"></script>      
	<script>
		jQuery(document).ready(function() {       
		   // initiate layout and plugins
		   App.init();
		});
	</script>
</body>
<!-- END BODY -->
</html>