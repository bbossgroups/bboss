<%@ page contentType="text/css" %>

<%
	//position:relative;??????? 
	//08.04.02 peng.yang 
	//????????tabPane??????????
	//?????????ditch-tab-wrap???????
	
	String userAgent = request.getHeader("User-Agent").toLowerCase(); 
	boolean  isMac = false;
	boolean  isMacIE = false;
	if(userAgent.indexOf("mac") > -1)
	{
		isMac = true;
		if(userAgent.indexOf("msie") > -1 && userAgent.indexOf("opera") == -1)
		{
			isMacIE = true;
		}
	}

	
		
%>



/******************************************************************************
********************************   wireframe   ********************************
******************************************************************************/

.ditch-tab-skin-wireframe .ditchnet-tab-container {
			margin:10px;
			border-bottom: 1px solid gray;
			border-left:   1px solid gray;
			}
	
.ditch-tab-skin-wireframe .ditch-tab-wrap {
			
			}
	
.ditch-tab-skin-wireframe .ditch-tab {
			float:left;
			padding:2px 10px 2px;
			border-top:  1px solid gray;
			border-right:1px solid gray;
			cursor:pointer;
			}
	
.ditch-tab-skin-wireframe .ditch-tab-wrap .ditch-unfocused {
			color:gray;
			background-color:white;
			}
	
.ditch-tab-skin-wireframe .ditch-tab-wrap .ditch-focused {
			color:black;
			background-color:silver;
			}

.ditch-tab-skin-wireframe .ditch-tab-pane-wrap {
			padding:8px;
			border-top:    1px solid gray;
			border-right:  1px solid gray;
			}

.ditch-tab-skin-wireframe .ditch-tab-pane {
			overflow:auto;
			}
	
.ditch-tab-skin-wireframe br.ditch-clear {
			clear:both;
			}
			
			
/******************************************************************************
******************************   INVISIBLE   **********************************
******************************************************************************/

.ditch-tab-skin-invisible .ditchnet-tab-container {
			margin:10px;
			border:0;
			}
	
.ditch-tab-skin-invisible .ditch-tab-wrap {
			display:none;
			border:0;
			}
	
.ditch-tab-skin-invisible .ditch-tab {
			display:none;
			border:0;
			}
	
.ditch-tab-skin-invisible .ditch-tab-wrap .ditch-unfocused {
			border:0;
			}
	
.ditch-tab-skin-invisible .ditch-tab-wrap .ditch-focused {
			border:0;
			}

.ditch-tab-skin-invisible .ditch-tab-pane-wrap {
			border:0;
			padding:8px;
			}

.ditch-tab-skin-invisible .ditch-tab-pane {
			border:0;
			overflow:auto;
			}
	
.ditch-tab-skin-invisible br.ditch-clear {
			clear:both;
			}
			
			
/******************************************************************************
********************************   default   **********************************
******************************************************************************/

.ditch-tab-skin-default .ditchnet-tab-container {
			margin:10px;
			}
	
.ditch-tab-skin-default .ditch-tab-wrap {
			z-index:10;
			font:12px "Lucida Grande",LucidaGrande,Verdana,sans-serif;
			}
	
.ditch-tab-skin-default .ditch-tab {
			position:relative;
			float:left;
			padding:2px 20px;
			margin:0 1px -1px 0;
			text-align:center;
			cursor:pointer;
			font:12px "Lucida Grande",LucidaGrande,Verdana,sans-serif;
			}

.ditch-tab-skin-default .ditch-tab-bg-left {
			position:absolute;
			left:0; top:0;
			width:10px; height:18px;
			}
html>body .ditch-tab-skin-default .ditch-tab-bg-left {
<%
	if(isMac)
	{
		out.print("height:19px;");
	}
	else
	{
		out.print("height:18px;");
	}
%>

			}

.ditch-tab-skin-default .ditch-unfocused {
			color:#999;
			border-bottom:1px solid silver;
<%
	if(isMacIE)
	{
		out.print("background-color:#eee;");
	}
	else
	{%>
		background:transparent url(<%=request.getContextPath()%>/include/default_tab_bg_right.gif) 100% 0 no-repeat;
	<%}
%>
	
			}
	
.ditch-tab-skin-default .ditch-focused {
			border-bottom:1px solid white;
	<%
	if(isMacIE)
	{
		out.print("background-color:white;");
	}
	else
	{%>
		background:transparent url(<%=request.getContextPath()%>/include/default_tab_bg_white_right.gif) 100% 0 no-repeat;
		
	<%}
%>
	
			}

<%
	if(!isMacIE)
	{
%>
.ditch-tab-skin-default .ditch-unfocused .ditch-tab-bg-left {
			background:transparent url(<%=request.getContextPath()%>/include/default_tab_bg_left.gif) 0 0 no-repeat;
			}
.ditch-tab-skin-default .ditch-focused .ditch-tab-bg-left {
			background:transparent url(<%=request.getContextPath()%>/include/default_tab_bg_white_left.gif) 0 0 no-repeat;
			}
<%}%>


.ditch-tab-skin-default .ditch-tab a:link,
.ditch-tab-skin-default .ditch-tab a:visited {
			color:black;
			text-decoration:none;
			}
.ditch-tab-skin-default .ditch-tab-wrap .ditch-unfocused a:link,
.ditch-tab-skin-default .ditch-tab-wrap .ditch-unfocused a:visited {
			color:silver;
			}



.ditch-tab-skin-default .ditch-tab-pane-wrap {
			position:relative;
			z-index:9;
			border:1px solid silver;
			padding:8px;
			}

.ditch-tab-skin-default .ditch-tab-pane {
			overflow:auto;
			}
	
.ditch-tab-skin-default br.ditch-clear {
			clear:both;
			}



/******************************************************************************
********************************   bluesky   **********************************
******************************************************************************/

.ditch-tab-skin-bluesky .ditchnet-tab-container {
			margin:10px;
			}
	
.ditch-tab-skin-bluesky .ditch-tab-wrap {

			z-index:10;
			font:12px "Lucida Grande",LucidaGrande,Verdana,sans-serif;
			white-space:nowrap;
			}
	
.ditch-tab-skin-bluesky .ditch-tab {
			position:relative;
			float:left;
			padding:2px 20px;
			margin:0 1px -1px 0;
			text-align:center;
			cursor:pointer;
			font:12px "Lucida Grande",LucidaGrande,Verdana,sans-serif;
			}

.ditch-tab-skin-bluesky .ditch-tab-bg-left {
			position:absolute;
			left:0; top:0;
			width:10px; height:18px;
			}
html>body .ditch-tab-skin-bluesky .ditch-tab-bg-left {
<%
	if(isMac)
	{
		out.print("height:19px;");
	}
	else
	{
		out.print("height:18px;");
	}
%>

			}

.ditch-tab-skin-bluesky .ditch-unfocused {
			color:#999;
			border-bottom:1px solid #0B72B4;
			height:24px;
			padding-top:6px;
<%
	if(isMacIE)
	{
		out.print("background-color:#eee;");
	}
	else
	{%>
		background:transparent url(<%=request.getContextPath()%>/include/bluesky_tab_bg_right.gif) 100% 0 no-repeat;
	<%}
%>
	
			}
	
.ditch-tab-skin-bluesky .ditch-focused {
			border-bottom:1px solid #0B72B4;
	<%
	if(isMacIE)
	{
		out.print("background-color:white;");
	}
	else
	{%>
		background:transparent url(<%=request.getContextPath()%>/include/bluesky_tab_bg_white_right.gif) 100% 0 ;
		height:19px;
		padding-right:39px;
		margin-right:-5px;
		padding-top:7px;
		
	<%}
%>
	
			}

<%
	if(!isMacIE)
	{
%>
.ditch-tab-skin-bluesky .ditch-unfocused .ditch-tab-bg-left {
			background:transparent url(<%=request.getContextPath()%>/include/bluesky_tab_bg_left1.gif) 0 0 no-repeat;
			
			}
.ditch-tab-skin-bluesky .ditch-focused .ditch-tab-bg-left {
			background:transparent url(<%=request.getContextPath()%>/include/bluesky_tab_bg_white_left1.gif) 0 0 no-repeat;
			height:25px;
			}
<%}%>


.ditch-tab-skin-bluesky .ditch-tab a:link,
.ditch-tab-skin-bluesky .ditch-tab a:visited {
			color:#ffffff;
			text-decoration:none;
			}
.ditch-tab-skin-bluesky .ditch-tab-wrap .ditch-unfocused a:link,
.ditch-tab-skin-bluesky .ditch-tab-wrap .ditch-unfocused a:visited {
        color:black;
		height:14px;
		padding-right:30px;
		margin-right:-5px;
			}



.ditch-tab-skin-bluesky .ditch-tab-pane-wrap {
			position:relative;
			z-index:9;
			border:1px solid #448BCD;
			padding:8px;
			background:#ffffff;
			}

.ditch-tab-skin-bluesky .ditch-tab-pane-wrap-sub1 {
			position:relative;
			z-index:9;
			border:1px solid #448BCD;
			padding:0px;
			
			}



.ditch-tab-skin-bluesky .ditch-tab-pane {

			overflow:auto;
			}
	
.ditch-tab-skin-bluesky br.ditch-clear {
			clear:both;
			}
			
/******************************************************************************
********************************   grassgreen   **********************************
******************************************************************************/

.ditch-tab-skin-grassgreen .ditchnet-tab-container {
			margin:10px;
			}
	
.ditch-tab-skin-grassgreen .ditch-tab-wrap {
			z-index:10;
			font:12px "Lucida Grande",LucidaGrande,Verdana,sans-serif;
			}
	
.ditch-tab-skin-grassgreen .ditch-tab {
			position:relative;
			float:left;
			padding:2px 20px;
			margin:0 1px -1px 0;
			text-align:center;
			cursor:pointer;
			font:12px "Lucida Grande",LucidaGrande,Verdana,sans-serif;
			}

.ditch-tab-skin-grassgreen .ditch-tab-bg-left {
			position:absolute;
			left:0; top:0;
			width:10px; height:18px;
			}
html>body .ditch-tab-skin-grassgreen .ditch-tab-bg-left {
<%
	if(isMac)
	{
		out.print("height:19px;");
	}
	else
	{
		out.print("height:18px;");
	}
%>

			}

.ditch-tab-skin-grassgreen .ditch-unfocused {
			color:#999;
			border-bottom:1px solid #0B72B4;
			height:24px;
			padding-top:6px;
			
<%
	if(isMacIE)
	{
		out.print("background-color:#eee;");
	}
	else
	{%>
		background:transparent url(<%=request.getContextPath()%>/include/grassgreen_tab_bg_right.gif) 100% 0 no-repeat;
	<%}
%>
	
			}
	
.ditch-tab-skin-grassgreen .ditch-focused {
			border-bottom:1px solid #0B72B4;
			
	<%
	if(isMacIE)
	{
		out.print("background-color:white;");
	}
	else
	{%>
		background:transparent url(<%=request.getContextPath()%>/include/grassgreen_tab_bg_white_right.gif) 100% 0 ;
		height:19px;
		padding-right:39px;
		margin-right:-5px;
		padding-top:7px;
		
	<%}
%>
	
			}

<%
	if(!isMacIE)
	{
%>
.ditch-tab-skin-grassgreen .ditch-unfocused .ditch-tab-bg-left {
			background:transparent url(<%=request.getContextPath()%>/include/grassgreen_tab_bg_left1.gif) 0 0 no-repeat;
			
			}
.ditch-tab-skin-grassgreen .ditch-focused .ditch-tab-bg-left {
			background:transparent url(<%=request.getContextPath()%>/include/grassgreen_tab_bg_white_left1.gif) 0 0 no-repeat;
			height:25px;
			}
<%}%>


.ditch-tab-skin-grassgreen .ditch-tab a:link,
.ditch-tab-skin-grassgreen .ditch-tab a:visited {
			color:#ffffff;
			text-decoration:none;
			}
.ditch-tab-skin-grassgreen .ditch-tab-wrap .ditch-unfocused a:link,
.ditch-tab-skin-grassgreen .ditch-tab-wrap .ditch-unfocused a:visited {
        color:black;
		height:14px;
		padding-right:30px;
		margin-right:-5px;
			}



.ditch-tab-skin-grassgreen .ditch-tab-pane-wrap {
			position:relative;
			z-index:9;
			border:1px solid #448BCD;
			padding:8px;
			background:#ffffff;
			}

.ditch-tab-skin-grassgreen .ditch-tab-pane-wrap-sub1 {
			position:relative;
			z-index:9;
			border:1px solid #448BCD;
			padding:0px;
			
			}



.ditch-tab-skin-grassgreen .ditch-tab-pane {
overflow:auto;
			
			}
	
.ditch-tab-skin-grassgreen br.ditch-clear {
			clear:both;
			}
			
			
/******************************************************************************
********************************   amethyst   **********************************
******************************************************************************/

.ditch-tab-skin-amethyst .ditchnet-tab-container {
			margin:10px;
			}
	
.ditch-tab-skin-amethyst .ditch-tab-wrap {
			z-index:10;
			font:12px "Lucida Grande",LucidaGrande,Verdana,sans-serif;
			}
	
.ditch-tab-skin-amethyst .ditch-tab {
			position:relative;
			float:left;
			padding:2px 20px;
			margin:0 1px -1px 0;
			text-align:center;
			cursor:pointer;
			font:12px "Lucida Grande",LucidaGrande,Verdana,sans-serif;
			}

.ditch-tab-skin-amethyst .ditch-tab-bg-left {
			position:absolute;
			left:0; top:0;
			width:10px; height:18px;
			}
html>body .ditch-tab-skin-amethyst .ditch-tab-bg-left {
<%
	if(isMac)
	{
		out.print("height:19px;");
	}
	else
	{
		out.print("height:18px;");
	}
%>

			}

.ditch-tab-skin-amethyst .ditch-unfocused {
			color:#999;
			border-bottom:1px solid #0B72B4;
			height:24px;
			padding-top:6px;
			
<%
	if(isMacIE)
	{
		out.print("background-color:#eee;");
	}
	else
	{%>
		background:transparent url(<%=request.getContextPath()%>/include/amethyst_tab_bg_right.gif) 100% 0 no-repeat;
	<%}
%>
	
			}
	
.ditch-tab-skin-amethyst .ditch-focused {
			border-bottom:1px solid #0B72B4;
			
	<%
	if(isMacIE)
	{
		out.print("background-color:white;");
	}
	else
	{%>
		background:transparent url(<%=request.getContextPath()%>/include/amethyst_tab_bg_white_right.gif) 100% 0 ;
		height:19px;
		padding-right:39px;
		margin-right:-5px;
		padding-top:7px;
		
	<%}
%>
	
			}

<%
	if(!isMacIE)
	{
%>
.ditch-tab-skin-amethyst .ditch-unfocused .ditch-tab-bg-left {
			background:transparent url(<%=request.getContextPath()%>/include/amethyst_tab_bg_left1.gif) 0 0 no-repeat;
			
			}
.ditch-tab-skin-amethyst .ditch-focused .ditch-tab-bg-left {
			background:transparent url(<%=request.getContextPath()%>/include/amethyst_tab_bg_white_left1.gif) 0 0 no-repeat;
			height:25px;
			}
<%}%>


.ditch-tab-skin-amethyst .ditch-tab a:link,
.ditch-tab-skin-amethyst .ditch-tab a:visited {
			color:#ffffff;
			text-decoration:none;
			}
.ditch-tab-skin-amethyst .ditch-tab-wrap .ditch-unfocused a:link,
.ditch-tab-skin-amethyst .ditch-tab-wrap .ditch-unfocused a:visited {
        color:black;
		height:14px;
		padding-right:30px;
		margin-right:-5px;
			}



.ditch-tab-skin-amethyst .ditch-tab-pane-wrap {
			position:relative;
			z-index:9;
			border:1px solid #448BCD;
			padding:8px;
			background:#ffffff;
			height:100%;
			width:100%;
			}

.ditch-tab-skin-amethyst .ditch-tab-pane-wrap-sub1 {
			position:relative;
			z-index:9;
			border:1px solid #448BCD;
			padding:0px;
			
			}



.ditch-tab-skin-amethyst .ditch-tab-pane {
height:100%;
width:100%;
overflow:auto;
			
			}
	
.ditch-tab-skin-amethyst br.ditch-clear {
			clear:both;
			}
			
			
/******************************************************************************
********************************   bluegreen   **********************************
******************************************************************************/

.ditch-tab-skin-bluegreen .ditchnet-tab-container {
			margin:10px;
			}
	
.ditch-tab-skin-bluegreen .ditch-tab-wrap {
			z-index:10;
			font:12px "Lucida Grande",LucidaGrande,Verdana,sans-serif;
			}
	
.ditch-tab-skin-bluegreen .ditch-tab {
			position:relative;
			float:left;
			padding:2px 20px;
			margin:0 1px -1px 0;
			text-align:center;
			cursor:pointer;
			font:12px "Lucida Grande",LucidaGrande,Verdana,sans-serif;
			}

.ditch-tab-skin-bluegreen .ditch-tab-bg-left {
			position:absolute;
			left:0; top:0;
			width:10px; height:18px;
			}
html>body .ditch-tab-skin-bluegreen .ditch-tab-bg-left {
<%
	if(isMac)
	{
		out.print("height:19px;");
	}
	else
	{
		out.print("height:18px;");
	}
%>

			}

.ditch-tab-skin-bluegreen .ditch-unfocused {
			color:#999;
			border-bottom:1px solid #0B72B4;
			height:24px;
			padding-top:6px;
			
<%
	if(isMacIE)
	{
		out.print("background-color:#eee;");
	}
	else
	{%>
		background:transparent url(<%=request.getContextPath()%>/include/bluegreen_tab_bg_right.gif) 100% 0 no-repeat;
	<%}
%>
	
			}
	
.ditch-tab-skin-bluegreen .ditch-focused {
			border-bottom:1px solid #0B72B4;
			
	<%
	if(isMacIE)
	{
		out.print("background-color:white;");
	}
	else
	{%>
		background:transparent url(<%=request.getContextPath()%>/include/bluegreen_tab_bg_white_right.gif) 100% 0 ;
		height:19px;
		padding-right:39px;
		margin-right:-5px;
		padding-top:7px;
		
	<%}
%>
	
			}

<%
	if(!isMacIE)
	{
%>
.ditch-tab-skin-bluegreen .ditch-unfocused .ditch-tab-bg-left {
			background:transparent url(<%=request.getContextPath()%>/include/bluegreen_tab_bg_left1.gif) 0 0 no-repeat;
			
			}
.ditch-tab-skin-bluegreen .ditch-focused .ditch-tab-bg-left {
			background:transparent url(<%=request.getContextPath()%>/include/bluegreen_tab_bg_white_left1.gif) 0 0 no-repeat;
			height:25px;
			}
<%}%>


.ditch-tab-skin-bluegreen .ditch-tab a:link,
.ditch-tab-skin-bluegreen .ditch-tab a:visited {
			color:#ffffff;
			text-decoration:none;
			}
.ditch-tab-skin-bluegreen .ditch-tab-wrap .ditch-unfocused a:link,
.ditch-tab-skin-bluegreen .ditch-tab-wrap .ditch-unfocused a:visited {
        color:black;
		height:14px;
		padding-right:30px;
		margin-right:-5px;
			}



.ditch-tab-skin-bluegreen .ditch-tab-pane-wrap {
			position:relative;
			z-index:9;
			border:1px solid #448BCD;
			padding:8px;
			background:#ffffff;
			}

.ditch-tab-skin-bluegreen .ditch-tab-pane-wrap-sub1 {
			position:relative;
			z-index:9;
			border:1px solid #448BCD;
			padding:0px;
			
			}



.ditch-tab-skin-bluegreen .ditch-tab-pane {
			overflow:auto;
			}
	
.ditch-tab-skin-bluegreen br.ditch-clear {
			clear:both;
			}