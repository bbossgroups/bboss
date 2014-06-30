<%@ page contentType="text/html; charset=UTF-8"%>
<%
String value = (String)session.getAttribute("$a.b.c");
if(value == null)
{
	session.setAttribute("$a.b.c", "a");
}
value = (String)session.getAttribute("$a.b.c");
out.println("before remove:"+value);
out.println("<br>");
session.removeAttribute("$a.b.c");
value = (String)session.getAttribute("$a.b.c");
out.println("after remove:"+value);
out.println("<br>");
session.setAttribute("local", java.util.Locale.CHINESE);
out.println("SS:"+session.getServletContext());
out.println("<br>");
out.println(session.getAttribute("local"));
 %>