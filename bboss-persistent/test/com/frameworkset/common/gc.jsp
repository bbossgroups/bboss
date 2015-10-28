<%@ page contentType="text/html; charset=UTF-8" session="false" language="java" %>
<%
System.out.println("run System.gc() begin.");
long start = System.currentTimeMillis();
	System.out.println("before freeMemory:" + Runtime.getRuntime().freeMemory() / 1024/1024);
		System.out.println("before maxMemory:" + Runtime.getRuntime().maxMemory() / 1024/1024);
		
		System.out.println("before totalMemory:" + Runtime.getRuntime().totalMemory()/ 1024/1024);
System.gc();
System.out.println("after freeMemory:" + Runtime.getRuntime().freeMemory() / 1024/1024);
		System.out.println("after  maxMemory:" + Runtime.getRuntime().maxMemory() / 1024/1024);
		request.getSession().getSessionContext().
		System.out.println("after  totalMemory:" + Runtime.getRuntime().totalMemory()/ 1024/1024);
long end = System.currentTimeMillis();
System.out.println("run System.gc() end. spend time:" + (end - start)/1000 + " seconds.");
 %>

 