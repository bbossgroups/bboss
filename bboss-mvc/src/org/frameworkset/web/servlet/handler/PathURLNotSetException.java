package org.frameworkset.web.servlet.handler;

import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.frameworkset.spi.support.StylerUtils;
import org.frameworkset.util.ClassUtil;

public class PathURLNotSetException  extends ServletException  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PathURLNotSetException(String path,String method,Object handler,HttpServletRequest request) {
		super("Alias [" + path + "] not setted real url mapping for handler["+ ClassUtil.getClassInfo(handler.getClass()).getClazz().getName() + "]: Request path '" + request.getRequestURI() +
				"', method '" + method + "', parameters " + StylerUtils.style(request.getParameterMap()));
	}
	
	public PathURLNotSetException(String path,String looppath,String method,Object handler,HttpServletRequest request) {
		super("Found Alias [" + path + "]  real url mapping for handler["+ ClassUtil.getClassInfo(handler.getClass()).getClazz().getName() + "] failed: Loop reference occour [" + looppath + "]'" + request.getRequestURI() +
				"', method '" + method + "', parameters " + StylerUtils.style(request.getParameterMap()));
	}
	
//	public PathURLNotSetException(String path,List<String> oldpaths,String method,Object handler,HttpServletRequest request) {
//		
//			
//		super("Found Alias [" + path + "]  real url mapping for handler["+ handler.getClass().getName() + "] failed: Loop reference occour [" + temp + "]'" + request.getRequestURI() +
//				"', method '" + method + "', parameters " + StylerUtils.style(request.getParameterMap()));
//	}
	
	public static String buildLooppath(List<String> looppaths)
	{
		StringBuffer temp = new StringBuffer();
		
		for(String oldpath:looppaths)
		{
			if(temp.length() > 0)
				temp.append("->").append(oldpath);
			else
				temp.append(oldpath);
		}
		return temp.toString();
	}

}
