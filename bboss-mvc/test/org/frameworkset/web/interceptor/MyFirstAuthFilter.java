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
		if(name != null && name.equals("test"))
			return false;
		return true;
	}

}
