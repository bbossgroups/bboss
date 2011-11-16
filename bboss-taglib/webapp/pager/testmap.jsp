<%@ page contentType="text/html; charset=GBK" language="java" import="test.*,java.util.*"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>

<%
	//构建map<String,PO>对象
	TestBean bean = null;
	Map<String,TestBean> mapbeans = new HashMap<String,TestBean>();
	bean = new TestBean();
	bean.setId("uuid");
	bean.setName("多多");
	mapbeans.put(bean.getId(),bean);
	
	bean = new TestBean();
	bean.setId("uuid1");
	bean.setName("多多1");
	mapbeans.put(bean.getId(),bean);
	bean = new TestBean();
	bean.setId("uuid2");
	bean.setName("多多2");
	mapbeans.put(bean.getId(),bean);
	request.setAttribute("mapbeans",mapbeans);
	
	Map<String,String> mapstrings = new HashMap<String,String>();
	mapstrings.put("id1","多多1");
	mapstrings.put("id2","多多2");
	mapstrings.put("id3","多多3");
	mapstrings.put("id4","多多4");
	request.setAttribute("mapstrings",mapstrings);
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
	
	
</body>
</html>
