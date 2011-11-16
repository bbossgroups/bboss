<%--

 *  Copyright 2008-2011 biaoping.yin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.

 --%>

<%@ page contentType="text/html;charset=UTF-8" language="java"
	session="false"%>
<%@ page import="java.io.ByteArrayOutputStream"%>
<%@ page import="java.io.PrintStream"%>
<%@ page import="java.io.File,org.frameworkset.util.CodeUtils"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
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






</head>


<body>

	<div id="contentBody">
		<link type="text/css"
			href="${pageContext.request.contextPath}/css/classic/java_syntax.css"
			rel="stylesheet">

		<link type="text/css"
			href="${pageContext.request.contextPath}/css/classic/syntax.css"
			rel="stylesheet">
		<link type="text/css"
			href="${pageContext.request.contextPath}/css/classic/scroller.css"
			rel="stylesheet">

		<script src="${pageContext.request.contextPath}/jsp/js/prototype.js"
			language="javascript" type="text/javascript"></script>
		<script src="${pageContext.request.contextPath}/jsp/js/behaviour.js"
			language="javascript" type="text/javascript"></script>
		<script
			src="${pageContext.request.contextPath}/jsp/js/scriptaculous.js"
			language="javascript" type="text/javascript"></script>
		<script src="${pageContext.request.contextPath}/jsp/js/builder.js"
			type="text/javascript"></script>
		<script src="${pageContext.request.contextPath}/jsp/js/effects.js"
			type="text/javascript"></script>
		<script src="${pageContext.request.contextPath}/jsp/js/dragdrop.js"
			type="text/javascript"></script>
		<script src="${pageContext.request.contextPath}/jsp/js/controls.js"
			type="text/javascript"></script>
		<script src="${pageContext.request.contextPath}/jsp/js/slider.js"
			type="text/javascript"></script>
		<script
			src="${pageContext.request.contextPath}/jsp/js/areascroller.js"
			language="javascript" type="text/javascript"></script>
		<div class="embeddedBlockContainer">
			<h3>
				控制器实现类
			</h3>
			<div class="shadow">
				<div class="info">
					<p>
						类路径：
						<span class="value">/bboss-mvc/test/org/frameworkset/spi/mvc/ListBeanBindController.java</span>
						Size:&nbsp;
						<span class="value">463b</span> Last modified:&nbsp;
						<span class="value">2011-01-23 23:30:20.0</span> Encoding:&nbsp;
						<span class="value">UTF-8</span> State:&nbsp;
						<span class="value"> Compiled </span>
					</p>
				</div>
			</div>
			<h3>
				Java source code
			</h3>
			<table cellspacing="0" id="resultsTable">
				<tbody>
					<tr>
						<td class="scroller" id="left_scroller">
							&nbsp;
						</td>
						<td id="separator" width="1%" style="display: none;">
							&nbsp;
						</td>

						<td>
							<div class="scrollable_content" id="srccontent">
								<code><%=CodeUtils.highlightStream("ListBeanBindController.java", "D:/workspace/bbossgroup-2.0-RC2/bboss-mvc/test/org/frameworkset/spi/mvc/ListBeanBindController.java",
                                "java", "UTF-8")
                               %></code>
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