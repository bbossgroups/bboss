<%@ page contentType="text/html; charset=UTF-8" language="java" import="test.*,java.util.*"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>

<%
	//构建map<String,PO>对象
	
%>
<!-- 
	测试在list标签上直接执行数据库，获取列表信息实例
	statement:数据库查询语句
	dbname:查询的相应数据库名称，在poolman.xml文件中进行配置
-->
<html>
<head>
<title>测试获取map信息实例</title>
</head>
<body>
	<table>
	    <h3>map<String,po>对象信息迭代功能</h3>
		<pg:map requestKey="mapbeans">
		
			<tr class="cms_data_tr">
				<td>
					mapkey:<pg:mapkey/>
				</td> 
				<td>
					id:<pg:cell colName="id" />
				</td> 
				<td>
					name:<pg:cell colName="name" />
				</td> 
			</tr>
		</pg:map>
		
		
	</table>
	
	<table>
	    <h3>map<String,String>字符串信息迭代功能</h3>
		<pg:map requestKey="mapstrings">
		
			<tr class="cms_data_tr">
				<td>
					mapkey:<pg:mapkey/>
				</td> 
				<td>
					value:<pg:cell/>
				</td> 
				
				
				
			</tr>
		</pg:map>
		
		
	</table>
	
	<table>
	    <h3>convert标签</h3>
	    <pg:beaninfo requestKey="beandata">
	    <tr><td>ff:<pg:convert convertData="beanmapdata" colName="userName"/></td></tr>
		
		</pg:beaninfo>
		
	</table>
	
	<table>
		<tr>
		<td>
	    <h3>逻辑true和false标签,展示true标签和false标签功能：
	    	true标签：如果指定的字段属性或者变量对应的值为true或者不为空对象，则输出标签体中的内容
	    	false标签：如果指定的字段属性或者变量对应的值为false或者为空对象，则输出标签体中的内容
	    </h3></td>
	    </tr>
		<pg:true actual="${mapstrings}">
		
			<tr>
				<td colspan="3">
					is true.
				</td> 
				 
				
				
				
			</tr>
		</pg:true>
		<pg:false actual="${mapstrings1}">
		
			<tr>
				<td colspan="3">
					is false.
				</td> 
			</tr>
		</pg:false>
		
	</table>
</body>
</html>
