package org.frameworkset.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.frameworkset.web.servlet.handler.HandlerMeta;

public class MyFirstAuthFilter extends AuthenticateFilter{

	@Override
	protected boolean check(HttpServletRequest request,
			HttpServletResponse response, HandlerMeta handlerMeta)
	{
		
		String name = request.getParameter("name");
		//如果页面带有name参数，并且参数值为test，则认为是一个非安全页面，系统将自动跳转到redirecturl属性对应的页面
		if(name != null && name.equals("test"))
			return false;
		return true;
	}

	@Override
	protected boolean checkPermission(HttpServletRequest request,
			HttpServletResponse response, HandlerMeta handlerMeta, String uri) {
		//如果页面地址为authorfailed.htm，则认为是一个非安全页面，系统将自动跳转到authorfailedurl属性对应的页面
		if(uri.equals("/authorfailed.htm"))
			return false;
		return true;
	}

}
