<%@ page contentType="text/html; charset=UTF-8" language="java" import="test.*,java.util.*"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>


<!-- 
	标签库case-then-other测试,if-else测试
-->
<html>
<head>
<title>case-then-other测试,if-else测试</title>
</head>
<body>
	<table>
	    <h3>if-else测试</h3>
		<pg:equal evalbody="true" actual="false" value="true">
		
			<pg:yes>
				yes,很好！
			</pg:yes>
			<pg:no>
				no,很坏！
			</pg:no>
		</pg:equal>
		
		
	</table>
	
	
	
	<table>
	    <h3>case-then-other测试</h3>
		<pg:case actual="1">
		
			<pg:equal value="1">
				yes,1！
			</pg:equal>
			<pg:equal value="2">
				yes,2！
			</pg:equal>
			<pg:other>
				yes,other！！
			</pg:other>
		</pg:case>
		
		<pg:case actual="2">
		
			<pg:equal value="1">
				yes,1！
			</pg:equal>
			<pg:equal value="2">
				yes,2！
			</pg:equal>
			<pg:other>
				yes,other！！
			</pg:other>
		</pg:case>
		
		<pg:case actual="3">
		
			<pg:equal value="1">
				yes,1！
			</pg:equal>
			<pg:equal value="2">
				yes,2！
			</pg:equal>
			<pg:other>
				yes,other！！
			</pg:other>
		</pg:case>
		
	</table>
	
	<table>
	    <h3>case-then-other嵌套测试</h3>
		<pg:case actual="1">
		
			<pg:equal evalbody="true" value="1">
				<pg:yes>
				yes 1,很好！
				</pg:yes>
				<pg:no>
					no 1,很坏！
				</pg:no>
			</pg:equal>
			<pg:equal value="2">
				yes,2！
			</pg:equal>
			<pg:other>
				yes,other！！
			</pg:other>
		</pg:case>
		
		<pg:case actual="2">
		
			<pg:equal evalbody="true" value="1">
				<pg:yes>
				yes 1,很好！
				</pg:yes>
				
			</pg:equal>
			<pg:equal value="2">
				yes,2！
			</pg:equal>
			<pg:other>
				yes,other！！
			</pg:other>
		</pg:case>
		
		<pg:case actual="3">
		
			<pg:equal value="1">
				yes,1！
			</pg:equal>
			<pg:equal value="2">
				yes,2！
			</pg:equal>
			<pg:other>
				yes,other！！
			</pg:other>
		</pg:case>
		
	</table>
</body>
</html>
