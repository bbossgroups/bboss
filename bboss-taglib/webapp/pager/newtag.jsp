<%@ page contentType="text/html; charset=UTF-8" language="java" import="test.*,java.util.*"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>


<!-- 
	测试在list标签上直接执行数据库，获取列表信息实例
	statement:数据库查询语句
	dbname:查询的相应数据库名称，在poolman.xml文件中进行配置
-->
<html>
<head>
<title>测试</title>
</head>
<body>
	<table>
	    <h3>map<String,po>对象信息迭代功能</h3>
		<pg:nlogic result="false">
		
			<pg:yes>
				yes,很好！
			</pg:yes>
			<pg:no>
				no,很坏！
			</pg:no>
		</pg:nlogic>
		
		
	</table>
	
	
	
	
</body>
</html>
