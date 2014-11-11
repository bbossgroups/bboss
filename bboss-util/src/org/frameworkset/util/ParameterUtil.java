package org.frameworkset.util;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.frameworkset.util.ClassUtil.PropertieDescription;
import org.frameworkset.util.ClassUtil.Var;


/**
 * <p>Title: ParameterUtil.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2013-08-15
 * @author biaoping.yin
 * @version 1.0
 */
public class ParameterUtil {
	private static Logger log = Logger.getLogger(ParameterUtil.class);
	public static String getParameterName(PropertieDescription property,String defaultName,HttpServletRequest request ,int curposition)
	{
		List<Var> vars = property.getRequestParamNameToken();
		if(property.getRequestParamName() != null)
			return property.getRequestParamName();
		else if(vars != null && vars.size() > 0)
		{
			StringBuffer name = new StringBuffer();
			for(Var var:vars)
			{
				if(!var.isIsvar())
					name.append(var.getName());
				else
				{
					String values[] = request.getParameterValues(var.getName());
					if(values != null )
					{
						if(curposition < values.length)
							name.append(values[curposition]);
						else
							name.append(values[values.length - 1]);
					}
					
				}
			}
			
			if(name.length() == 0)
			{
				name.append("转换参数名称表达式").append(property.getOrigineRequestParamName()).append("失败：");
				for(Var var:vars)
				{
					if(var.isIsvar())
					{
						name.append("没有名称为").append(var.getName()).append("request请求参数;");			
						
						
					}
				}
				log.warn(name.toString());
				throw new java.lang.RuntimeException(name.toString());
			}
			return name.toString();
		}
		else
		{
			return defaultName;
		}
	}
	
	/**
	 * 获取识别集合是否有记录的参数名称
	 * @param property
	 * @param defaultName
	 * @param request
	 * @param curposition
	 * @return
	 * 2014年11月11日
	 */
	public static String getAssertHasdataParameterName(PropertieDescription property,String defaultName,HttpServletRequest request ,int curposition)
	{
		List<Var> vars = property.getRequestParamNameToken();
		if(property.getRequestParamName() != null)
			return property.getRequestParamName();
		else if(vars != null && vars.size() > 0)
		{			
			for(Var var:vars)
			{
				if(!var.isIsvar())
					;
				else
				{
					return var.getName();
					
				}
			}			
			return defaultName;
		}
		else
		{
			return defaultName;
		}
	}
	
	
	public static String getParameterName(MethodParameter parameter,HttpServletRequest request ,int curposition)
	{
		if(parameter.isIsrequestbody())
		{
			return null;
		}
		
		if(parameter.getRequestParameterName() != null)
			return parameter.getRequestParameterName();
		else 
		{
			List<Var> vars = parameter.getRequestParamNameToken();
			StringBuffer name = new StringBuffer();
			for(Var var:vars)
			{
				if(!var.isIsvar())
					name.append(var.getName());
				else
				{
					String values[] = request.getParameterValues(var.getName());
					if(values != null )
					{
						if(curposition < values.length)
							name.append(values[curposition]);
						else
							name.append(values[values.length - 1]);
					}
					
				}
			}
			
			if(name.length() == 0)
			{
				name.append("转换参数名称表达式").append(parameter.getOrigineRequestParamName()).append("失败：");
				for(Var var:vars)
				{
					if(var.isIsvar())
					{
						name.append("没有名称为").append(var.getName()).append("request请求参数;");			
						
						
					}
				}
				log.warn(name.toString());
				throw new java.lang.RuntimeException(name.toString());
			}
			return name.toString();
		}
		
	}
	
	public static List<Var> evalVars(int vstart,String name)
	{
		List<Var> requestParamNameToken = new ArrayList<Var>();
		
		Var var = new Var();
		if(vstart == 0)
		{
			
			
			var.setIsvar(true);
			int end = name.indexOf("}");
			var.setName(name.substring(2,end));
			requestParamNameToken.add(var);
			if(end == name.length() - 1)
			{
				
			}
			else
			{
				var = new Var();
				
				var.setName(name.substring(end + 1));
				requestParamNameToken.add(var);
			}
			
		}
		else
		{
			
			var.setName(name.substring(0,vstart));
			requestParamNameToken.add(var);
			int end = name.indexOf("}");
			var = new Var();
			var.setIsvar(true);
			var.setName(name.substring(vstart + 2,end));
			requestParamNameToken.add(var);
			if(end == name.length() - 1)
			{
				
			}
			else
			{
				var = new Var();								
				var.setName(name.substring(end + 1));
				requestParamNameToken.add(var);
			}
		}
		return requestParamNameToken;
	}

}
