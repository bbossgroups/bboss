<%@page import="org.frameworkset.util.I18NUtil"%><%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%
String language = request.getParameter("language");
if(language==null){//获取默认语言
	language = I18NUtil.getRequestContextLocalCode(request);
}
else//设置用户选择语言  ,通常情况下，我们会在mvc 控制器方法中处理设置本地语言的逻辑
{
	try {
		I18NUtil.setLocale(request, response, language);
	} catch (Exception e) {
		e.printStackTrace();
	}
}

%>
<script language="JavaScript">
<!--
function changeLan(){
	var lan = document.getElementById("language").value;
	window.location.href="i18n.jsp?language="+lan;
}
//-->
</script>
<!-- 
国际化机制：默认从浏览器的Locale环境中识别语言，可以通过select切换中英文环境
 -->
 <div>
<pg:message code="select.language.label"/>:
<select name="language" id="language" onchange="changeLan()">
				<%if(language.equals("zh_CN")){ %>
					<option value="zh_CN" selected>
						<pg:message code="language.chinese"/>
					</option>
					<option value="en_US">
						<pg:message code="language.english"/>
					</option>
					<%}else{ %>
					<option value="zh_CN" >
						<pg:message code="language.chinese"/>
					</option>
					<option value="en_US" selected>
						<pg:message code="language.english"/>
					</option>
					<%} %>
</select>
</div>
 
 <!-- 通过标签获取输出数据 -->
<div>
<pg:message code="language.frommessagetag"/> :
<pg:message code="user.national"/>
</div>
 <!-- 通过java代码获取数据获取输出数据 -->
<div>
<pg:message code="language.fromjava"/>:
<%
//从messagesource中获取特点语言串
org.frameworkset.spi.support.MessageSource messageSource = org.frameworkset.web.servlet.support.WebApplicationContextUtils.getWebApplicationContext();
out.print("<br>从messagesource中获取特点语言串:"+messageSource.getMessage("user.national",  
		I18NUtil.getRequestContextLocal(request) ));

//如果localeResolver指定为org.frameworkset.web.servlet.i18n.LanguageLocaleResolver时可以通过以下方式在java代码中直接获取code对应的特定语言串

out.print("<br>如果localeResolver指定为org.frameworkset.web.servlet.i18n.LanguageLocaleResolver时可以通过以下方式在java代码中直接获取code对应的特定语言串:"+I18NUtil.getI18nMessage("user.national"));
%>
</div>