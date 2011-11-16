<%--
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://probe.jstripe.com/d/license.shtml
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
 * WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 --%>

<%@ page contentType="text/html;charset=UTF-8" language="java"
	session="false"%>
<%@ page import="java.io.ByteArrayOutputStream"%>
<%@ page import="java.io.PrintStream"%>
<%@ page import="java.io.File"%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/fmt' prefix='fmt'%>

<%--
    Servlet exception handler page. Unfortunately we cannot use here any fancy libraries if we want
    any kind of reliability. Spring or sitemesh could have caused the exception we are handling here.

    Author: Vlad Ilyushchenko
--%>

<html>

	<head>
		<title>bboss-mvc - demo index</title>

		<link rel="shortcut icon"
			href="${pageContext.request.contextPath}/css/favicon.gif">
		<link rel="stylesheet"
			href="${pageContext.request.contextPath}/css/classic/tables.css"
			type="text/css">
		<link rel="stylesheet"
			href="${pageContext.request.contextPath}/css/classic/main.css"
			type="text/css">
		<link rel="stylesheet"
			href="${pageContext.request.contextPath}/css/classic/mainnav.css"
			type="text/css">
		<link rel="stylesheet"
			href="${pageContext.request.contextPath}/css/classic/messages.css"
			type="text/css">
		<link rel="stylesheet"
			href="${pageContext.request.contextPath}/css/classic/tooltip.css"
			type="text/css">
		<link type="text/css" href="/probe/css/classic/syntax.css"
			rel="stylesheet">
		<link type="text/css" href="/probe/css/classic/scroller.css"
			rel="stylesheet">

		<script src="/probe/js/prototype.js" language="javascript"
			type="text/javascript"></script>
		<script src="/probe/js/behaviour.js" language="javascript"
			type="text/javascript"></script>
		<script src="/probe/js/scriptaculous.js" language="javascript"
			type="text/javascript"></script>
		<script src="http://localhost:8080/probe/js/builder.js"
			type="text/javascript"></script>
		<script src="http://localhost:8080/probe/js/effects.js"
			type="text/javascript"></script>
		<script src="http://localhost:8080/probe/js/dragdrop.js"
			type="text/javascript"></script>
		<script src="http://localhost:8080/probe/js/controls.js"
			type="text/javascript"></script>
		<script src="http://localhost:8080/probe/js/slider.js"
			type="text/javascript"></script>
		<script src="/probe/js/areascroller.js" language="javascript"
			type="text/javascript"></script>
			
			<script src="/probe/js/prototype.js" language="javascript"
				type="text/javascript"></script>
			<script src="/probe/js/scriptaculous.js" language="javascript"
				type="text/javascript"></script>
			<script src="http://localhost:8080/probe/js/builder.js"
				type="text/javascript"></script>
			<script src="http://localhost:8080/probe/js/effects.js"
				type="text/javascript"></script>
			<script src="http://localhost:8080/probe/js/dragdrop.js"
				type="text/javascript"></script>
			<script src="http://localhost:8080/probe/js/controls.js"
				type="text/javascript"></script>
			<script src="http://localhost:8080/probe/js/slider.js"
				type="text/javascript"></script>
			<script src="/probe/js/effects.js" language="javascript"
				type="text/javascript"></script>
			<script src="/probe/js/func.js" language="javascript"
				type="text/javascript"></script>
			<script src="/probe/js/behaviour.js" language="javascript"
				type="text/javascript"></script>

			<script type="text/javascript">
    function handleContextReload(idx, context) {
        var img = $('ri_'+idx);
        var status = $('rs_'+idx);
        reload_url = '/probe/app/reload.ajax?webapp='+context;
        img.src='/probe/css/classic/gifs/animated_reset.gif';
        status.innerHTML="wait...";
        new Ajax.Updater(status,
                         '/probe/app/reload.ajax?webapp='+context,
                         {method:'get',asynchronous:true}).onComplete = function() {
            img.src='/probe/css/classic/gifs/reset.gif';
        };
        return false;
    }

    function toggleContext(idx, url, context) {
        status = $('rs_'+idx);
        status.innerHTML = '&lt;img border="0" src="/probe/css/classic/gifs/progerssbar_editnplace.gif"/&gt;'
        new Ajax.Updater(status, url+'?webapp='+context, {method:'get',asynchronous:true});
        return false;
    }

</script>

	</head>
	<body>
	
	------------------------------
		<div id="mainBody">
			

			<ul class="options">



				<li id="size">
					<a href="?size=true">estimate sessions size (could be slow)</a>
				</li>


				<li id="abbreviations">
					<a href="">What are those abbreviations?</a>
				</li>
			</ul>

			<div class="blockContainer">

				<div style="display: none;" class="helpMessage" id="help">
					<div class="ajax_activity"></div>
				</div>






				<table cellspacing="0" cellpadding="0" id="app" class="genericTbl">
					<thead>
						<tr>
							<th class="order1 sorted">
								&nbsp;
							</th>
							<th class="sortable">
								<a href="?d-16387-o=2&amp;size=&amp;d-16387-s=1">Name</a>
							</th>
							<th class="sortable">
								<a href="?d-16387-o=2&amp;size=&amp;d-16387-s=2">Status</a>
							</th>
							<th>
								&nbsp;
							</th>
							<th>
								Description
							</th>
							<th class="sortable">
								<a href="?d-16387-o=2&amp;size=&amp;d-16387-s=5">Req.</a>
							</th>
							<th class="sortable">
								<a href="?d-16387-o=2&amp;size=&amp;d-16387-s=6">Sess.</a>
							</th>
							<th class="sortable">
								<a href="?d-16387-o=2&amp;size=&amp;d-16387-s=7">S.Attr</a>
							</th>
							<th class="sortable">
								<a href="?d-16387-o=2&amp;size=&amp;d-16387-s=8">C.Attr</a>
							</th>
							<th class="sortable">
								<a href="?d-16387-o=2&amp;size=&amp;d-16387-s=9">Sess.Timeout</a>
							</th>
							<th>
								JSP
							</th>
							<th class="sortable">
								<a href="?d-16387-o=2&amp;size=&amp;d-16387-s=11">Jdbc Usage</a>
							</th>
							<th class="sortable">
								<a href="?d-16387-o=2&amp;size=&amp;d-16387-s=12">Clstred.?</a>
							</th>
							<th class="sortable">
								<a href="?d-16387-o=2&amp;size=&amp;d-16387-s=13">Ser.?</a>
							</th>
						</tr>
					</thead>
					<tbody>
						<tr class="odd">
							<td class="leftMostIcon">
								<a
									onclick="return confirm('This operation cannot be reversed. Do you really want to REMOVE /bboss-mvc?')"
									href="/probe/adm/undeploy.htm?webapp=%2fbboss-mvc"
									class="imglink"> <img title="Undeploy /bboss-mvc"
										alt="Undeploy" src="/probe/css/classic/img/bin.jpg"
										class="lnk"> </a>
							</td>
							<td>
								<a href="/probe/appsummary.htm?webapp=%2fbboss-mvc&amp;size=">/bboss-mvc</a>
							</td>
							<td>


								<a title="Stop /bboss-mvc"
									href="/probe/app/stop.htm?webapp=/bboss-mvc" class="okValue"
									onclick="return toggleContext('2', '/probe/app/toggle.ajax', '/bboss-mvc');">
									<div id="rs_2">
										running
									</div> </a>



							</td>
							<td>
								<a href="/probe/app/reload.htm?webapp=/bboss-mvc"
									class="imglink"
									onclick="return handleContextReload('2', '/bboss-mvc');"> <img
										border="0" title="Reload /bboss-mvc" alt="reload"
										src="/probe/css/classic/gifs/reset.gif" id="ri_2"> </a>
							</td>
							<td>
								BBOSS-MVC&nbsp;
							</td>
							<td>
								<a href="/probe/appservlets.htm?webapp=/bboss-mvc">9</a>
							</td>
							<td>
								<a href="/probe/sessions.htm?webapp=%2fbboss-mvc&amp;size=">
									0 </a>
							</td>
							<td>
								0
							</td>
							<td>
								<a href="/probe/appattributes.htm?webapp=%2fbboss-mvc"> 6 </a>
							</td>
							<td>
								30
							</td>
							<td>
								<a href="/probe/app/jsp.htm?webapp=%2fbboss-mvc" class="imglink">
									<img border="0" alt="view"
										src="/probe/css/classic/gifs/magnifier.gif"> </a>
							</td>
							<td class="score_wrapper">
								<div class="score_wrapper">
									<a href="/probe/resources.htm?webapp=/bboss-mvc"
										class="imglink"><img border="0" title="Max.conn.usage 0%"
											alt="+" src="/probe/css/classic/gifs/rb_a0.gif"> </a><a
										href="/probe/resources.htm?webapp=/bboss-mvc" class="imglink"><img
											border="0" title="Max.conn.usage 0%" alt="+"
											src="/probe/css/classic/gifs/rb_0.gif"> </a><a
										href="/probe/resources.htm?webapp=/bboss-mvc" class="imglink"><img
											border="0" title="Max.conn.usage 0%" alt="+"
											src="/probe/css/classic/gifs/rb_0.gif"> </a><a
										href="/probe/resources.htm?webapp=/bboss-mvc" class="imglink"><img
											border="0" title="Max.conn.usage 0%" alt="+"
											src="/probe/css/classic/gifs/rb_0.gif"> </a><a
										href="/probe/resources.htm?webapp=/bboss-mvc" class="imglink"><img
											border="0" title="Max.conn.usage 0%" alt="+"
											src="/probe/css/classic/gifs/rb_0.gif"> </a><a
										href="/probe/resources.htm?webapp=/bboss-mvc" class="imglink"><img
											border="0" title="Max.conn.usage 0%" alt="+"
											src="/probe/css/classic/gifs/rb_0.gif"> </a><a
										href="/probe/resources.htm?webapp=/bboss-mvc" class="imglink"><img
											border="0" title="Max.conn.usage 0%" alt="+"
											src="/probe/css/classic/gifs/rb_0.gif"> </a><a
										href="/probe/resources.htm?webapp=/bboss-mvc" class="imglink"><img
											border="0" title="Max.conn.usage 0%" alt="+"
											src="/probe/css/classic/gifs/rb_0.gif"> </a><a
										href="/probe/resources.htm?webapp=/bboss-mvc" class="imglink"><img
											border="0" title="Max.conn.usage 0%" alt="+"
											src="/probe/css/classic/gifs/rb_0.gif"> </a><a
										href="/probe/resources.htm?webapp=/bboss-mvc" class="imglink"><img
											border="0" title="Max.conn.usage 0%" alt="+"
											src="/probe/css/classic/gifs/rb_0.gif"> </a><a
										href="/probe/resources.htm?webapp=/bboss-mvc" class="imglink"><img
											border="0" title="Max.conn.usage 0%" alt="+"
											src="/probe/css/classic/gifs/rb_0.gif"> </a><a
										href="/probe/resources.htm?webapp=/bboss-mvc" class="imglink"><img
											border="0" title="Max.conn.usage 0%" alt="+"
											src="/probe/css/classic/gifs/rb_b0.gif"> </a>
								</div>
							</td>
							<td>



								<span class="errorValue">no</span>


							</td>
							<td>


								<span class="okValue">yes</span>



							</td>
						</tr>
						<tr class="even">
							<td class="leftMostIcon">
								<a
									onclick="return confirm('This operation cannot be reversed. Do you really want to REMOVE /monitor?')"
									href="/probe/adm/undeploy.htm?webapp=%2fmonitor"
									class="imglink"> <img title="Undeploy /monitor"
										alt="Undeploy" src="/probe/css/classic/img/bin.jpg"
										class="lnk"> </a>
							</td>
							<td>
								<a href="/probe/appsummary.htm?webapp=%2fmonitor&amp;size=">/monitor</a>
							</td>
							<td>


								<a title="Stop /monitor"
									href="/probe/app/stop.htm?webapp=/monitor" class="okValue"
									onclick="return toggleContext('3', '/probe/app/toggle.ajax', '/monitor');">
									<div id="rs_3">
										running
									</div> </a>



							</td>
							<td>
								<a href="/probe/app/reload.htm?webapp=/monitor" class="imglink"
									onclick="return handleContextReload('3', '/monitor');"> <img
										border="0" title="Reload /monitor" alt="reload"
										src="/probe/css/classic/gifs/reset.gif" id="ri_3"> </a>
							</td>
							<td>
								Tomcat monitor&nbsp;
							</td>
							<td>
								<a href="/probe/appservlets.htm?webapp=/monitor">0</a>
							</td>
							<td>
								<a href="/probe/sessions.htm?webapp=%2fmonitor&amp;size="> 0
								</a>
							</td>
							<td>
								0
							</td>
							<td>
								<a href="/probe/appattributes.htm?webapp=%2fmonitor"> 7 </a>
							</td>
							<td>
								30
							</td>
							<td>
								<a href="/probe/app/jsp.htm?webapp=%2fmonitor" class="imglink">
									<img border="0" alt="view"
										src="/probe/css/classic/gifs/magnifier.gif"> </a>
							</td>
							<td class="score_wrapper">
								<div class="score_wrapper">
									<a href="/probe/resources.htm?webapp=/monitor" class="imglink"><img
											border="0" title="Max.conn.usage 0%" alt="+"
											src="/probe/css/classic/gifs/rb_a0.gif"> </a><a
										href="/probe/resources.htm?webapp=/monitor" class="imglink"><img
											border="0" title="Max.conn.usage 0%" alt="+"
											src="/probe/css/classic/gifs/rb_0.gif"> </a><a
										href="/probe/resources.htm?webapp=/monitor" class="imglink"><img
											border="0" title="Max.conn.usage 0%" alt="+"
											src="/probe/css/classic/gifs/rb_0.gif"> </a><a
										href="/probe/resources.htm?webapp=/monitor" class="imglink"><img
											border="0" title="Max.conn.usage 0%" alt="+"
											src="/probe/css/classic/gifs/rb_0.gif"> </a><a
										href="/probe/resources.htm?webapp=/monitor" class="imglink"><img
											border="0" title="Max.conn.usage 0%" alt="+"
											src="/probe/css/classic/gifs/rb_0.gif"> </a><a
										href="/probe/resources.htm?webapp=/monitor" class="imglink"><img
											border="0" title="Max.conn.usage 0%" alt="+"
											src="/probe/css/classic/gifs/rb_0.gif"> </a><a
										href="/probe/resources.htm?webapp=/monitor" class="imglink"><img
											border="0" title="Max.conn.usage 0%" alt="+"
											src="/probe/css/classic/gifs/rb_0.gif"> </a><a
										href="/probe/resources.htm?webapp=/monitor" class="imglink"><img
											border="0" title="Max.conn.usage 0%" alt="+"
											src="/probe/css/classic/gifs/rb_0.gif"> </a><a
										href="/probe/resources.htm?webapp=/monitor" class="imglink"><img
											border="0" title="Max.conn.usage 0%" alt="+"
											src="/probe/css/classic/gifs/rb_0.gif"> </a><a
										href="/probe/resources.htm?webapp=/monitor" class="imglink"><img
											border="0" title="Max.conn.usage 0%" alt="+"
											src="/probe/css/classic/gifs/rb_0.gif"> </a><a
										href="/probe/resources.htm?webapp=/monitor" class="imglink"><img
											border="0" title="Max.conn.usage 0%" alt="+"
											src="/probe/css/classic/gifs/rb_0.gif"> </a><a
										href="/probe/resources.htm?webapp=/monitor" class="imglink"><img
											border="0" title="Max.conn.usage 0%" alt="+"
											src="/probe/css/classic/gifs/rb_b0.gif"> </a>
								</div>
							</td>
							<td>



								<span class="errorValue">no</span>


							</td>
							<td>


								<span class="okValue">yes</span>



							</td>
						</tr>
						<tr class="odd">
							<td class="leftMostIcon">
								<a
									onclick="return confirm('This operation cannot be reversed. Do you really want to REMOVE /probe?')"
									href="/probe/adm/undeploy.htm?webapp=%2fprobe" class="imglink">
									<img title="Undeploy /probe" alt="Undeploy"
										src="/probe/css/classic/img/bin.jpg" class="lnk"> </a>
							</td>
							<td>
								<a href="/probe/appsummary.htm?webapp=%2fprobe&amp;size=">/probe</a>
							</td>
							<td>


								<a title="Stop /probe" href="/probe/app/stop.htm?webapp=/probe"
									class="okValue"
									onclick="return toggleContext('1', '/probe/app/toggle.ajax', '/probe');">
									<div id="rs_1">
										running
									</div> </a>



							</td>
							<td>
								<a href="/probe/app/reload.htm?webapp=/probe" class="imglink"
									onclick="return handleContextReload('1', '/probe');"> <img
										border="0" title="Reload /probe" alt="reload"
										src="/probe/css/classic/gifs/reset.gif" id="ri_1"> </a>
							</td>
							<td>
								LambdaProbe for Apache Tomcat&nbsp;
							</td>
							<td>
								<a href="/probe/appservlets.htm?webapp=/probe">479</a>
							</td>
							<td>
								<a href="/probe/sessions.htm?webapp=%2fprobe&amp;size="> 1 </a>
							</td>
							<td>
								4
							</td>
							<td>
								<a href="/probe/appattributes.htm?webapp=%2fprobe"> 11 </a>
							</td>
							<td>
								30
							</td>
							<td>
								<a href="/probe/app/jsp.htm?webapp=%2fprobe" class="imglink">
									<img border="0" alt="view"
										src="/probe/css/classic/gifs/magnifier.gif"> </a>
							</td>
							<td class="score_wrapper">
								<div class="score_wrapper">
									<a href="/probe/resources.htm?webapp=/probe" class="imglink"><img
											border="0" title="Max.conn.usage 0%" alt="+"
											src="/probe/css/classic/gifs/rb_a0.gif"> </a><a
										href="/probe/resources.htm?webapp=/probe" class="imglink"><img
											border="0" title="Max.conn.usage 0%" alt="+"
											src="/probe/css/classic/gifs/rb_0.gif"> </a><a
										href="/probe/resources.htm?webapp=/probe" class="imglink"><img
											border="0" title="Max.conn.usage 0%" alt="+"
											src="/probe/css/classic/gifs/rb_0.gif"> </a><a
										href="/probe/resources.htm?webapp=/probe" class="imglink"><img
											border="0" title="Max.conn.usage 0%" alt="+"
											src="/probe/css/classic/gifs/rb_0.gif"> </a><a
										href="/probe/resources.htm?webapp=/probe" class="imglink"><img
											border="0" title="Max.conn.usage 0%" alt="+"
											src="/probe/css/classic/gifs/rb_0.gif"> </a><a
										href="/probe/resources.htm?webapp=/probe" class="imglink"><img
											border="0" title="Max.conn.usage 0%" alt="+"
											src="/probe/css/classic/gifs/rb_0.gif"> </a><a
										href="/probe/resources.htm?webapp=/probe" class="imglink"><img
											border="0" title="Max.conn.usage 0%" alt="+"
											src="/probe/css/classic/gifs/rb_0.gif"> </a><a
										href="/probe/resources.htm?webapp=/probe" class="imglink"><img
											border="0" title="Max.conn.usage 0%" alt="+"
											src="/probe/css/classic/gifs/rb_0.gif"> </a><a
										href="/probe/resources.htm?webapp=/probe" class="imglink"><img
											border="0" title="Max.conn.usage 0%" alt="+"
											src="/probe/css/classic/gifs/rb_0.gif"> </a><a
										href="/probe/resources.htm?webapp=/probe" class="imglink"><img
											border="0" title="Max.conn.usage 0%" alt="+"
											src="/probe/css/classic/gifs/rb_0.gif"> </a><a
										href="/probe/resources.htm?webapp=/probe" class="imglink"><img
											border="0" title="Max.conn.usage 0%" alt="+"
											src="/probe/css/classic/gifs/rb_0.gif"> </a><a
										href="/probe/resources.htm?webapp=/probe" class="imglink"><img
											border="0" title="Max.conn.usage 0%" alt="+"
											src="/probe/css/classic/gifs/rb_b0.gif"> </a>
								</div>
							</td>
							<td>



								<span class="errorValue">no</span>


							</td>
							<td>


								<span class="okValue">yes</span>



							</td>
						</tr>
					</tbody>
				</table>


			</div>
		</div>
		
		
		----------------------------------------------
		<div id="mainBody">
			<ul class="options">


				<li id="showapps">
					<a href="?apps=false&amp;d-16474-s=0&amp;d-16474-o=2">show
						existing only</a>
				</li>



			</ul>

			<div class="blockContainer">

				<table cellspacing="0" id="log" class="genericTbl">
					<thead>
						<tr>
							<th class="order1 sortable sorted">
								<a href="?apps=true&amp;d-16474-s=0&amp;d-16474-o=1">App</a>
							</th>
							<th class="sortable">
								<a href="?apps=true&amp;d-16474-s=1&amp;d-16474-o=2">Class</a>
							</th>
							<th class="sortable">
								<a href="?apps=true&amp;d-16474-s=2&amp;d-16474-o=2">File
									name</a>
							</th>
							<th>
								&nbsp;
							</th>
							<th class="sortable">
								<a href="?apps=true&amp;d-16474-s=4&amp;d-16474-o=2">Size</a>
							</th>
							<th class="sortable">
								<a href="?apps=true&amp;d-16474-s=5&amp;d-16474-o=2">Modified</a>
							</th>
							<th class="sortable">
								<a href="?apps=true&amp;d-16474-s=6&amp;d-16474-o=2">Type</a>
							</th>
						</tr>
					</thead>
					<tbody>
						<tr class="odd">
							<td class="leftmost">
								/monitor
							</td>
							<td>
								log4j
							</td>
							<td>
								<a href="/probe/logs/follow.htm?id=1" class="logfile">
									common.log </a>

							</td>
							<td>
								<a href="/probe/logs/download?id=1" class="imglink"><img
										alt="download"
										src="/probe/css/classic/gifs/page_white_put.gif" class="lnk">
								</a>
							</td>
							<td>
								92Kb&nbsp;
							</td>
							<td>
								2011-03-05 23:34:13.156&nbsp;
							</td>
							<td>
								org.apache.log4j.RollingFileAppender
							</td>
						</tr>
						<tr class="even">
							<td class="leftmost">
								/probe
							</td>
							<td>
								log4j
							</td>
							<td>

								<a href="/probe/logs/follow.htm?id=2" class="logfile">
									E:\environment\apache-tomcat-6.0.20\logs\probe.log </a>

							</td>
							<td>

								<a href="/probe/logs/download?id=2" class="imglink"><img
										alt="download"
										src="/probe/css/classic/gifs/page_white_put.gif" class="lnk">
								</a>

							</td>
							<td>
								675Kb&nbsp;
							</td>
							<td>
								2010-10-10 22:18:51.25&nbsp;
							</td>
							<td>
								org.apache.log4j.RollingFileAppender
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>

--------------------------------------------------

		<div id="contentBody">



			<ul class="options">
				<li id="back">
					<a href="/probe/app/jsp.htm?webapp=%2fbboss-mvc"> back to JSP
						list</a>
				</li>


				<li id="viewservlet">
					<a
						href="/probe/app/viewservlet.htm?webapp=%2fbboss-mvc&amp;source=%2fjsp%2fvalidate%2fimagevalidate.jsp">view
						generated servlet</a>
				</li>

				<li id="compilesingle">
					<a
						href="/probe/app/recompile.htm?webapp=%2fbboss-mvc&amp;source=%2fjsp%2fvalidate%2fimagevalidate.jsp&amp;view=%2fapp%2fviewsource.htm">
						compile</a>
				</li>

			</ul>




			<div class="embeddedBlockContainer">
				<h3>
					JSP information
				</h3>

				<div class="shadow">
					<div class="info">
						<p>
							Application name:&nbsp;
							<span class="value">/bboss-mvc</span> File name:&nbsp;
							<span class="value">/jsp/validate/imagevalidate.jsp</span>
							Size:&nbsp;
							<span class="value">463b</span> Last modified:&nbsp;
							<span class="value">2011-01-23 23:30:20.0</span> Encoding:&nbsp;
							<span class="value">UTF-8</span> State:&nbsp;
							<span class="value"> Compiled </span>
						</p>
					</div>
				</div>



				<h3>
					JSP source code
				</h3>

				<table cellspacing="0" id="resultsTable">
					<tbody>
						<tr>
							<td class="scroller" id="left_scroller">
								&nbsp;
							</td>
							<td width="1%">
								&nbsp;
							</td>
							<td>
								<div class="scrollable_content" id="srccontent">
									<code>



										<span class="codeline"><span class="linenum">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;1</span><span
											class="xml_tag_symbols">&lt;</span><span class="xml_plain">%@&nbsp;</span><span
											class="xml_attribute_name">page</span><span class="xml_plain">&nbsp;</span><span
											class="xml_attribute_name">contentType</span><span
											class="xml_tag_symbols">=</span><span
											class="xml_attribute_value">"text/html;charset=UTF-8"</span><span
											class="xml_plain">&nbsp;&nbsp;%</span><span
											class="xml_tag_symbols">&gt;</span><span class="xml_plain"></span>
										<br>
										</span>
										<span class="codeline"><span class="linenum">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2</span><span
											class="xml_tag_symbols">&lt;</span><span class="xml_plain">%@&nbsp;</span><span
											class="xml_attribute_name">taglib</span><span
											class="xml_plain">&nbsp;</span><span
											class="xml_attribute_name">uri</span><span
											class="xml_tag_symbols">=</span><span
											class="xml_attribute_value">"/WEB-INF/pager-taglib.tld"</span><span
											class="xml_plain">&nbsp;</span><span
											class="xml_attribute_name">prefix</span><span
											class="xml_tag_symbols">=</span><span
											class="xml_attribute_value">"pg"</span><span
											class="xml_plain">%</span><span class="xml_tag_symbols">&gt;</span><span
											class="xml_plain"></span>
										<br>
										</span>
										<span class="codeline"><span class="linenum">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;3</span><span
											class="xml_tag_symbols">&lt;</span><span class="xml_tag_name">form</span><span
											class="xml_plain">&nbsp;</span><span
											class="xml_attribute_name">name</span><span
											class="xml_tag_symbols">=</span><span
											class="xml_attribute_value">"frm"</span><span
											class="xml_plain">&nbsp;</span><span
											class="xml_attribute_name">method</span><span
											class="xml_tag_symbols">=</span><span
											class="xml_attribute_value">"post"</span><span
											class="xml_plain">&nbsp;</span><span
											class="xml_attribute_name">action</span><span
											class="xml_tag_symbols">=</span><span
											class="xml_attribute_value">"/bboss-mvc/rest/imagevalidator"</span><span
											class="xml_tag_symbols">&gt;</span><span class="xml_plain"></span>
										<br>
										</span>
										<span class="codeline"><span class="linenum">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;4</span><span
											class="xml_plain">Hello&nbsp;Image&nbsp;Test</span><span
											class="xml_tag_symbols">&lt;</span><span class="xml_tag_name">br</span><span
											class="xml_tag_symbols">/&gt;</span><span class="xml_plain"></span>
										<br>
										</span>
										<span class="codeline"><span class="linenum">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;5</span><span
											class="xml_plain">checkCode:</span><span
											class="xml_tag_symbols">&lt;</span><span class="xml_tag_name">img</span><span
											class="xml_plain">&nbsp;</span><span
											class="xml_attribute_name">src</span><span
											class="xml_tag_symbols">=</span><span
											class="xml_attribute_value">"/bboss-mvc/rest/imagevalidator/abcdefghijk123456/5"</span><span
											class="xml_tag_symbols">&gt;&lt;</span><span
											class="xml_tag_name">br</span><span class="xml_tag_symbols">/&gt;</span><span
											class="xml_plain"></span>
										<br>
										</span>
										<span class="codeline"><span class="linenum">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;6</span><span
											class="xml_plain">please&nbsp;input&nbsp;the&nbsp;checkCode:</span><span
											class="xml_tag_symbols">&lt;</span><span class="xml_tag_name">input</span><span
											class="xml_plain">&nbsp;</span><span
											class="xml_attribute_name">type</span><span
											class="xml_tag_symbols">=</span><span
											class="xml_attribute_value">"text"</span><span
											class="xml_plain">&nbsp;</span><span
											class="xml_attribute_name">name</span><span
											class="xml_tag_symbols">=</span><span
											class="xml_attribute_value">"imagecode"</span><span
											class="xml_plain">&nbsp;</span><span
											class="xml_attribute_name">value</span><span
											class="xml_tag_symbols">=</span><span
											class="xml_attribute_value">""</span><span
											class="xml_tag_symbols">&gt;&lt;</span><span
											class="xml_tag_name">br</span><span class="xml_tag_symbols">/&gt;</span><span
											class="xml_plain"></span>
										<br>
										</span>
										<span class="codeline"><span class="linenum">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;7</span><span
											class="xml_tag_symbols">&lt;</span><span class="xml_tag_name">input</span><span
											class="xml_plain">&nbsp;</span><span
											class="xml_attribute_name">type</span><span
											class="xml_tag_symbols">=</span><span
											class="xml_attribute_value">"submit"</span><span
											class="xml_plain">&nbsp;</span><span
											class="xml_attribute_name">name</span><span
											class="xml_tag_symbols">=</span><span
											class="xml_attribute_value">"btn1"</span><span
											class="xml_plain">&nbsp;</span><span
											class="xml_attribute_name">value</span><span
											class="xml_tag_symbols">=</span><span
											class="xml_attribute_value">"check"</span><span
											class="xml_tag_symbols">&gt;</span><span class="xml_plain"></span>
										<br>
										</span>
										<span class="codeline"><span class="linenum">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;8</span><span
											class="xml_tag_symbols">&lt;/</span><span
											class="xml_tag_name">form</span><span class="xml_tag_symbols">&gt;</span><span
											class="xml_plain">&nbsp;</span>
										<br>
										</span>
										<span class="codeline"><span class="linenum">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;9</span><span
											class="xml_plain"></span>
										<br>
										</span>
										<span class="codeline"><span class="linenum">&nbsp;&nbsp;&nbsp;&nbsp;10</span><span
											class="xml_plain">&nbsp;</span><span class="xml_tag_symbols">&lt;</span><span
											class="xml_tag_name">pg:message</span><span class="xml_plain">&nbsp;</span><span
											class="xml_attribute_name">code</span><span
											class="xml_tag_symbols">=</span><span
											class="xml_attribute_value">"${message}"</span><span
											class="xml_tag_symbols">/&gt;</span><span class="xml_plain">&nbsp;&nbsp;</span>
										<br>
										</span>
										<span class="codeline"><span class="linenum">&nbsp;&nbsp;&nbsp;&nbsp;11</span><span
											class="xml_plain"></span>
										<br>
										</span>




									</code>
								</div>
							</td>
							<td class="scroller" id="right_scroller">
								&nbsp;
							</td>
						</tr>
					</tbody>
				</table>

			</div>
			<script type="text/javascript">
            setupScrollers('srccontent');
        </script>
		</div>

	</body>
</html>