<%-- 
 * @Description 返回jsondata
 * @Date 20110402
 * @author 李峰高
 * @version 1.0
--%>

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%
//System.out.println("debug>>returnJson.jsp->jsoncode="+request.getAttribute("jsoncode"));
/* 例如返回：
{"total":28,"rows":[
	{"productid":"FI-SW-01","unitcost":10.00,"status":"P","listprice":36.50,"attr1":"Large","itemid":"EST-1"},
	{"productid":"K9-DL-01","unitcost":12.00,"status":"P","listprice":18.50,"attr1":"Spotted Adult Female","itemid":"EST-10"}	
]}
*/
%>
${jsoncode}