<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="test.*"%>
<%
String value = (String)session.getAttribute("$a.b.c");
if(value == null)
{
	session.setAttribute("$a.b.c", "a");
}
value = (String)session.getAttribute("$a.b.c");
out.println("$a.b.c:"+value);
out.println("<br>");

session.setAttribute("local", java.util.Locale.CHINESE);
out.println("session.getServletContext():"+session.getServletContext());
out.println("<br>");
out.println("local:"+session.getAttribute("local"));

//下面的功能演示存储一个复杂对象（包含引用关系）到session中，然后读取出来验证对象关系是否正确还原
TestVO testVO = new TestVO();
testVO.setId("testvoid");
TestVO1 testVO1 = new TestVO1();
testVO1.setName("hello,test vo1");
testVO.setTestVO1(testVO1);
session.setAttribute("testVO", testVO);
testVO = (TestVO)session.getAttribute("testVO");
//修改testVO中属性的值
testVO.setId("testvoidaaaaa");
//需要将修改后的对象重新设置到session中否则无法存储最新的testVO到mongodb中
session.setAttribute("testVO", testVO);
session.setAttribute("userAccount","john");//跨应用共享属性
out.println("<br>");
String privateAttr = (String)session.getAttribute("privateAttr");//session应用设置的共享会话属性
String userAccount = (String)session.getAttribute("userAccount");//session应用设置的共享会话属性
out.println("sessionmonitor's private attribute:"+privateAttr+"<br>");
out.println("shared attribute userAccount:"+userAccount+"<br>");
testVO = (TestVO)session.getAttribute("testVO");
out.println("shared attribute testVO:"+testVO.getId()+"<br>");
 %>
 
 <a href="http://sub.bboss.com.cn:8080/sessionmonitor/sessiontest.jsp" target="_blank">sessionmonitor</a>