/*
 *  Copyright 2008 biaoping.yin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.frameworkset.util.annotations;


import com.frameworkset.util.BeanUtils;
import com.frameworkset.util.EditorInf;
import com.frameworkset.util.ValueObjectUtil;
import org.frameworkset.http.HttpMethodsContainer;
import org.frameworkset.http.MediaType;
import org.frameworkset.util.*;
import org.frameworkset.util.annotations.wraper.*;
import org.frameworkset.util.beans.BeansException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Title: MethodInfo.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-10-16 下午07:26:01
 * @author biaoping.yin
 * @version 1.0
 */
public class MethodInfo extends HttpMethodsContainer {
	private Method method;
    /**
	 * 是否是Flux异步响应式方法
	 */
    private boolean reactorFlux;



    private boolean reactorMono;
	/**
	 * 是否是分页方法
	 */
	private boolean pagerMethod = false;
	/**
	 * 是否指定页面大小参数
	 */
	private boolean definePageSize = false;
	
	private int pageSize = 10;
	
	private MethodParameter[] paramNames;
	private HandlerMapping mapping = null;
	private HandlerMapping typeLevelMapping;
	private AssertDToken assertDToken;
	private AssertTicket assertTicket;
	private PermissionToken permissionToken;
	private boolean requireTicket;
	private boolean responsebody = false;
	private ResponseBodyWraper responsebodyAnno ;
	
	private String[] paths;
	private String[] pathPattern;
	private PathVariableInfo[] pathVariables;
	private boolean[] databind;
	private String[] baseurls ;
    public static Class reactorTypeFlux;

    public static Class reactorTypeMono;
    static {
        try {
            reactorTypeFlux = Class.forName("reactor.core.publisher.Flux");
        } catch (ClassNotFoundException e) {
//            throw new RuntimeException(e);
        }
        try {
            reactorTypeMono = Class.forName("reactor.core.publisher.Mono");
        } catch (ClassNotFoundException e) {
//            throw new RuntimeException(e);
        }
    }
	/**
	 * 用来标注mvc控制器方法强制要求进行动态令牌校验，如果客户端请求没有附带令牌或者令牌已经作废，那么直接拒绝
	 * 请求 
	 * 
	 * 防止跨站请求过滤器相关参数 bboss防止跨站请求过滤器的机制如下： 采用动态令牌和session相结合的方式产生客户端令牌，一次请求产生一个唯一令牌 
				令牌识别采用客户端令牌和服务端session标识混合的方式进行判别，如果客户端令牌和服务端令牌正确匹配，则允许访问，否则认为用户为非法用户并阻止用户访问并跳转到 
				redirectpath参数对应的地址，默认为/login.jsp。 令牌存储机制通过参数tokenstore指定，包括两种，内存存储和session存储，默认为session存储，当令牌失效（匹配后立即失效，或者超时失效）后，系统自动清除失效的令牌；采用session方式 
				存储令牌时，如果客户端页面没有启用session，那么令牌还是会存储在内存中。 令牌生命周期：客户端的令牌在服务器端留有存根，当令牌失效（匹配后立即失效，或者超时失效）后，系统自动清除失效的令牌； 
				当客户端并没有正确提交请求，会导致服务端令牌存根变为垃圾令牌，需要定时清除这些 垃圾令牌；如果令牌存储在session中，那么令牌的生命周期和session的生命周期保持一致，无需额外清除机制； 
				如果令牌存储在内存中，那么令牌的清除由令牌管理组件自己定时扫描清除，定时扫描时间间隔为由tokenscaninterval参数指定，单位为毫秒，默认为30分钟，存根保存时间由tokendualtime参数指定，默认为1个小时 
				可以通过enableToken参数配置指定是否启用令牌检测机制，true检测，false不检测，默认为false不检测 enableToken是否启用令牌检测机制，true 
				启用，false 不启用，默认为不启用
	 */
	private boolean requiredDToken = false;
	
	public MethodInfo(Method method, HandlerMapping typeLevelMapping) {
		super();
		this.method = method;
        if(reactorTypeFlux != null) {
            Class clazz = method.getReturnType();

            if (reactorTypeFlux.isAssignableFrom(clazz)) {
                this.reactorFlux = true;
            }
        }

        if(reactorTypeMono != null) {
            Class clazz = method.getReturnType();

            if (reactorTypeMono.isAssignableFrom(clazz)) {
                this.reactorMono = true;
            }
        }
		this.assertDToken = method.getAnnotation(AssertDToken.class);		
		this.requiredDToken = assertDToken != null;
		this.assertTicket = method.getAnnotation(AssertTicket.class);
		this.requireTicket = assertTicket != null;
		mapping = method.getAnnotation(HandlerMapping.class);
		permissionToken = method.getAnnotation(PermissionToken.class);
		
		ResponseBody body = method.getAnnotation(ResponseBody.class);
//		this.responsebodyAnno = method.getAnnotation(ResponseBody.class);
		if(body != null)
		{
			responsebodyAnno = new ResponseBodyWraper(body,method);
			responsebody = true;
//			this.responseMediaType = convertMediaType();
		}
        HttpMethod[] requestMethods = null;
        this.typeLevelMapping = typeLevelMapping;
        if(mapping != null) {
            requestMethods = mapping.method();
            if(requestMethods == null || requestMethods.length == 0) {
                if(typeLevelMapping != null ){
                    requestMethods = typeLevelMapping.method();
                }
               
            }
            
        }
        if(requestMethods != null && requestMethods.length > 0) {
            super.setHttpMethods(requestMethods);
        }
		
		this.baseurls = typeLevelMapping != null?typeLevelMapping.value():null;
		
		this.paths = mapping != null?mapping.value():null;
		this.pathPattern = buildPathPatterns();
		this.parserVariables();
		this.parserInfo();
//		genericParameterTypes();
		
	}
	
	public MethodInfo(Method method, String[] baseurls) {
		super();
		this.method = method;
        if(reactorTypeFlux != null) {
            Class clazz = method.getReturnType();

            if (clazz.isAssignableFrom(reactorTypeFlux)) {
                this.reactorFlux = true;
            }
        }

        if(reactorTypeMono != null) {
            Class clazz = method.getReturnType();

            if (reactorTypeMono.isAssignableFrom(clazz)) {
                this.reactorMono = true;
            }
        }
		mapping = method.getAnnotation(HandlerMapping.class);
		this.assertDToken = method.getAnnotation(AssertDToken.class);
		this.requiredDToken = assertDToken != null;
		this.assertTicket = method.getAnnotation(AssertTicket.class);
		this.requireTicket = assertTicket != null;
		permissionToken = method.getAnnotation(PermissionToken.class);
		ResponseBody body = method.getAnnotation(ResponseBody.class);
//		this.responsebodyAnno = method.getAnnotation(ResponseBody.class);
		if(body != null)
		{
			this.responsebodyAnno = new ResponseBodyWraper(body,method);
			responsebody = true;
//			this.responseMediaType = convertMediaType();
		}
		if(mapping != null)
            super.setHttpMethods(mapping.method());
		this.baseurls = baseurls;
		this.paths = mapping != null?mapping.value():null;
		this.pathPattern = buildPathPatterns();
		this.parserVariables();
		this.parserInfo();
//		genericParameterTypes();
		
	}

    public MethodInfo setRequestMethods(HttpMethodsContainer httpMethodsContainer) {
        if(httpMethodsContainer == null || !httpMethodsContainer.containMethods()){
            return this;
        }        
        if(this.httpMethods == null || this.httpMethods.length == 0) {
            setHttpMethods(httpMethodsContainer);
        }
        return this;
    }

    public boolean isResponseBody()
	{
		return this.responsebody;
	}
	public String toString()
	{
		if(method != null)
			return method.toString();
		else
			return super.toString();
	}
    
	private MediaType convertMediaType()
	{
		MediaType temp = null;
		if(this.responsebodyAnno != null)
		{
			String type = responsebodyAnno.datatype();
			String charset = this.responsebodyAnno.charset();
			if(type == null)
			{
				if(charset != null)
				{
					temp = new MediaType("text","html",Charset.forName(charset));
				}
				else
				{
					temp = new MediaType("text","html",Charset.forName("UTF-8"));
				}
			}
			else if(type.equals("json"))
			{
				if(charset != null)
				{
					temp = new MediaType("application","json",Charset.forName(charset));
				}
				else
					temp = new MediaType("application","json",Charset.forName("UTF-8"));
			}
			else if(type.equals("jsonp"))
			{
				if(charset != null)
				{
					temp = new MediaType("application","jsonp",Charset.forName(charset));
				}
				else
					temp = new MediaType("application","jsonp",Charset.forName("UTF-8"));
			}
			
			else if(type.equals("xml"))
			{
				if(charset != null)
				{
					temp = new MediaType("application","xml",Charset.forName(charset));
				}
				else
					temp = new MediaType("application","xml",Charset.forName("UTF-8"));
			}
			else if(type.equals("javascript"))
			{
				if(charset != null)
				{
					temp = new MediaType("application","javascript",Charset.forName(charset));
				}
				else
					temp = new MediaType("application","javascript",Charset.forName("UTF-8"));
			}
			//javascript
			
			
			
			
		}
		return temp;
	}
	public static String getRealPath(String contextPath, String path) {
		if (contextPath == null || contextPath.equals("")) {
//			System.out.println("SimpleStringUtil.getRealPath() contextPath:"
//					+ contextPath);
			return path == null?"":path;
		}
		if (path == null || path.equals("")) {
			
			return contextPath;
		}
		
		contextPath = contextPath.replace('\\', '/');
		path = path.replace('\\', '/');
		if (path.startsWith("/") ) {
			
			if (!contextPath.endsWith("/"))
				return contextPath + path;
			else {
				return contextPath.substring(0,contextPath.length() - 1) + path;
			}

		} else {
			if (!contextPath.endsWith("/"))
				return contextPath + "/" + path;
			else {
				return contextPath + path;
			}
		}

	}
	/**
	 * 解析路径中的变量，第一级目录不能设置为变量
	 * 地址模式：/rest/{a}/people/{b}/{c}
	 * 变量信息：
	 * a
	 * b
	 * c
	 * 输入路径:/rest/sunshine/people/2_6_204/yinbp
	 * 解析出来的变量值：
	 * a=sunshine
	 * b=2_6_204
	 * c=yinbp
	 * 
	 */
	private void parserVariables()
	{
		if( paths == null || paths.length == 0)
			return ;
		String baseurl = this.baseurls != null && this.baseurls.length > 0?baseurls[0]:"";
		String path = getRealPath(baseurl, paths[0]);
//		int len = path.length();
//		int index = path.indexOf('/');
//		List<Integer> poses = new ArrayList<Integer>();
//		List<PathVariableInfo> variables = new ArrayList<PathVariableInfo>();
//		int count = -1;
//		while(index != -1)
//		{		
//			if(index == len - 1)
//				break;
//			count ++;
//			if(path.charAt(index+1) == '{')
//			{
//				poses.add(count);
//				int endps = path.indexOf("}",index + 1);
//				variables.add(path.substring(index + 1 + 1, endps));
//			}
//			index = path.indexOf("/", index + 1);			
//		}
//		
//		if(poses.size() > 0)
//		{
//			
//			this.pathVariables = new PathVariableInfo[variables.size()];
//			for(int k = 0; k < variables.size(); k ++)
//			{
//				pathVariables[k] = variables.get(k);
//			}
//					
//					
//			this.pathVariablePositions = SimpleStringUtil.toIntArray(poses);
//		}
		
		parserPathdata(path);
		
			
		
		
	}
	public void parserPathdata(String path)
	{
//		if(path.startsWith("//"))
//			path = path.substring(2);
//		else if(path.startsWith("/"))
//			path = path.substring(1);
		List<String> datas = new ArrayList<String>();
//		List<Integer> poses = new ArrayList<Integer>();
		List<PathVariableInfo> variables = new ArrayList<PathVariableInfo>();
		int i = 0;
		char c = ' ';
		int end = path.length();
		StringBuilder bu = new StringBuilder();
		do
		{
			c = path.charAt(i);
			if(c == '/')
			{
				if(bu.length() > 0)
				{
					datas.add(bu.toString());
					bu.setLength(0);
					
				}
				else 
				{
					if(i == end -1)
					{
//						datas.add("");
					}
					else if(i == 0){
						
					}						
				}
			}
			else
			{
				bu.append(c);
			}
			i ++;
			
			
			
		}while(i < end);
		if(bu.length() > 0)
		{
			datas.add(bu.toString());
			bu = null;
		}
		for(int k = 0; k < datas.size(); k ++)
		{
			String data = datas.get(k);
			if(data.charAt(0) == '{')
			{
				
				int idx = data.lastIndexOf('}');
				if(idx > 0)
				{
//					poses.add(k);
					String temp = data.substring(1, idx);
					PathVariableInfo v = new PathVariableInfo();
					v.setVariable(temp);
					v.setPostion(k);
					if(idx == data.length() - 1)
					{
						
					}
					else
					{
						temp = data.substring(idx+1);
						v.setConstantstr(temp);
						//v.setLast(true);
					}
					variables.add(v);
				}
						
			}
		}
		if(variables.size() > 0)
		{
			
			this.pathVariables = new PathVariableInfo[variables.size()];
			for(int k = 0; k < variables.size(); k ++)
			{
				pathVariables[k] = variables.get(k);
			}
					
					
//			this.pathVariablePositions = SimpleStringUtil.toIntArray(poses);
		}
	}
	public static String buildPathPattern(String mappedPath)
	{
		return buildPathPattern(null,mappedPath);
	}
	public static String buildPathPattern(String baseurl,String mappedPath)
	{
		
		StringBuilder pathUrl = new StringBuilder();
		if(baseurl != null)
			pathUrl.append(baseurl);
		String[] tmp = mappedPath.split("/");
//		pathUrl.append(tmp[0]);
		for(int i = 1; i < tmp.length; i ++ )
		{
			
			String data = tmp[i];
			if(data.charAt(0) == '{')
			{
				
				int idx = data.lastIndexOf('}');
				if(idx > 0)
				{					
					String temp = data.substring(1, idx);					 
					if(idx == data.length() - 1)
					{
						pathUrl.append("/*");
					}
					else
					{
						temp = data.substring(idx+1);
						pathUrl.append("/*").append(temp);
					}					 
				}
				else
				{
					pathUrl.append("/").append(data);
				}
						
			}
			else
			{
				pathUrl.append("/").append(data);
			}
			
		}
		return pathUrl.toString();
	}
	private String[] buildPathPatterns()
	{
		if(paths == null || paths.length == 0)
			return null;
		String[] pathPatterns = null;
		if(this.baseurls == null || this.baseurls.length == 0)
		{
			pathPatterns = new String[paths.length];
			int k = 0;
			for(String mappedPath:paths)
			{
//				StringBuilder pathUrl = new StringBuilder();
//				String[] tmp = mappedPath.split("/");
////				pathUrl.append(tmp[0]);
//				for(int i = 1; i < tmp.length; i ++ )
//				{
//					if(tmp[i].startsWith("{"))
//						pathUrl.append("/*");
//					else
//						pathUrl.append("/").append(tmp[i]);
//				}
//				pathPatterns[k] = pathUrl.toString();
				pathPatterns[k] = buildPathPattern( null,mappedPath);
				k ++;
			}
		}
		else
		{
			pathPatterns = new String[baseurls.length * paths.length];
			int k = 0;
			for(String baseurl:baseurls)
			{				
				for(String mappedPath:paths)
				{
//					StringBuilder pathUrl = new StringBuilder();
//					pathUrl.append(baseurl);
//					String[] tmp = mappedPath.split("/");
////					pathUrl.append(tmp[0]);
//					for(int i = 1; i < tmp.length; i ++ )
//					{
//						if(tmp[i].startsWith("{"))
//							pathUrl.append("/*");
//						else
//							pathUrl.append("/").append(tmp[i]);
//					}
//					pathPatterns[k] = pathUrl.toString();
					pathPatterns[k] = buildPathPattern(baseurl, mappedPath);
					k ++;
				}
			}
		}
		return pathPatterns;
			
//		String baseurl;
//		String mappedPath = paths[0];
//		StringBuilder pathUrl = new StringBuilder();
//		pathUrl.append(baseurl);
//		for(int i = 0; i < mappedPath.length(); i ++ )
//		{
//			if(mappedPath.charAt(i) == '/')
//				pathUrl.append("/*");
//			
//		}
//		
//		return new String[] {pathUrl.toString() };
	}
	
//	public boolean restful()
//	{
//		return this.typeLevelMapping.restful();
//	}
	
	
	public MethodInfo(Method method) {
		super();
		this.method = method;
        if(reactorTypeFlux != null) {
            Class clazz = method.getReturnType();

            if (clazz.isAssignableFrom(reactorTypeFlux)) {
                this.reactorFlux = true;
            }
        }

        if(reactorTypeMono != null) {
            Class clazz = method.getReturnType();

            if (reactorTypeMono.isAssignableFrom(clazz)) {
                this.reactorMono = true;
            }
        }
		this.assertDToken = method.getAnnotation(AssertDToken.class);
		this.requiredDToken = assertDToken != null;
		
		this.assertTicket = method.getAnnotation(AssertTicket.class);
		this.requireTicket = assertTicket != null;
		permissionToken = method.getAnnotation(PermissionToken.class);
		ResponseBody body = method.getAnnotation(ResponseBody.class);
		
		if(body != null)
		{
			this.responsebodyAnno = new ResponseBodyWraper(body,method );
			responsebody = true;
//			this.responseMediaType = convertMediaType();
		}
		this.parserInfo();
//		genericParameterTypes();
	}
	
	public String[] getPaths()
	{
		return mapping == null?null:mapping.value();
	}

	public PermissionToken getPermissionToken() {
		return permissionToken;
	}

	private MethodParameter buildMutilMethodParamAnnotations(Annotation[] annotations, int parampostion, String methodparamname, Class paramType)
	{
		MethodParameter ret = new MethodParameter(method,parampostion);
		List<MethodParameter> mutilMethodParamAnnotations = new ArrayList<MethodParameter>();
		MethodParameter paramAnno = null;
		Annotation annotation = null;
		boolean ismapkey = false;
		for(int k = 0; k < annotations.length; k ++)
		{
			 annotation = annotations[k];
			 if(annotation instanceof RequestBody)
			{
				 paramAnno = new MethodParameter(method,parampostion);
				RequestBodyWraper param = new RequestBodyWraper((RequestBody)annotation,paramType);
//					if(param.editor() != null && !param.editor().equals(""))
//						paramNames[i].setEditor((EditorInf)BeanUtils.instantiateClass(param.editor()));
//					paramNames[i].setParameterName(param.value());
				paramAnno.setDataBindScope(Scope.REQUEST_BODY);
				paramAnno.setRequestBody(param);
				mutilMethodParamAnnotations.add(paramAnno);
				paramAnno.setIsrequestbody(true);
				continue;
				
			}
			else if(annotation instanceof DataBind)
			{
				 paramAnno  = new MethodParameter(method,parampostion);
				 paramAnno.setDataBeanBind(true);
				 mutilMethodParamAnnotations.add(paramAnno);
					continue;
			}
			else if(annotation instanceof RequestParam)
			{
				paramAnno  = new MethodParameter(method,parampostion);
				RequestParamWraper param = new RequestParamWraper((RequestParam)annotation,paramType);
				paramAnno.setRequestParam(param);
				if(param.editor() != null && !param.editor().equals(""))
					paramAnno.setEditor((EditorInf)BeanUtils.instantiateClass(param.editor()));
				if(!param.name().equals(""))
				{
					String paramname = param.name();
					int vstart = paramname.indexOf("${");
					if(vstart < 0)
					{
						paramAnno.setParameterName(paramname);
					}
					else
					{
						paramAnno.setRequestParamNameToken(ParameterUtil.evalVars(vstart, paramname));
						paramAnno.setNamevariabled(true);
					}
				}
				else
					paramAnno.setParameterName(methodparamname);
				paramAnno.setDataBindScope(Scope.REQUEST_PARAM);
				paramAnno.setRequired(param.required());
				Object aa = param.defaultvalue();
				if(aa != null)
					paramAnno.setDefaultValue(aa);
				mutilMethodParamAnnotations.add(paramAnno);
				continue;
				
			}
			else if(annotation instanceof MapKey)
			{
				ret  = new MethodParameter(method,parampostion);
				MapKey param = (MapKey)annotation;
				ret.setMapKey(param);
				ret.setDataBindScope(Scope.MAP_PARAM);	
				ismapkey = true;
//				mutilMethodParamAnnotations.add(paramAnno);
				break;
			}
			else if(annotation instanceof PagerParam)//分页参数信息
			{
				this.setPagerMethod(true);
				paramAnno  = new MethodParameter(method,parampostion);
				PagerParamWraper param = new PagerParamWraper((PagerParam)annotation,paramType);
				paramAnno.setPagerParam((param));
				if(param.editor() != null && !param.editor().equals(""))
					paramAnno.setEditor((EditorInf)BeanUtils.instantiateClass(param.editor()));
				if(param.name().equals(PagerParam.PAGE_SIZE))
				{
					this.setDefinePageSize(true);
					String id = param.id();
					
					paramAnno.setParamNamePrefix(id);
					paramAnno.setParameterName(param.name());
					
				}
				else
				{
					String id = param.id();
					if(param.id() == null)
						id = PagerParam.DEFAULT_ID;	
					if(param.name().startsWith(id))
						paramAnno.setParameterName(param.name());
					else
					{
						paramAnno.setParameterName(id + "." + param.name());
					}					
				}
				paramAnno.setDataBindScope(Scope.PAGER_PARAM);
				paramAnno.setRequired(param.required());
				Object aa = param.defaultvalue();
				if(aa != null)
					paramAnno.setDefaultValue(aa);
				mutilMethodParamAnnotations.add(paramAnno);
				continue;
				
			} 
			else if(annotation instanceof PathVariable)
			{
				paramAnno  = new MethodParameter(method,parampostion);
				PathVariableWraper param = new PathVariableWraper((PathVariable)annotation,paramType);
				paramAnno.setPathVariable((param));
				if(param.editor() != null && !param.editor().equals(""))
					paramAnno.setEditor((EditorInf)BeanUtils.instantiateClass(param.editor()));
//				paramAnno.setParameterName(param.value());
				if(!param.value().equals(""))
					paramAnno.setParameterName(param.value());
				else
					paramAnno.setParameterName(methodparamname);
				paramAnno.setDataBindScope(Scope.PATHVARIABLE);
				Object aa = param.defaultvalue();
				if(aa != null)
					paramAnno.setDefaultValue(aa);
				mutilMethodParamAnnotations.add(paramAnno);
				continue;
				
			}
			else if(annotation instanceof CookieValue)
			{
				paramAnno  = new MethodParameter(method,parampostion);
				CookieValueWraper param = new CookieValueWraper((CookieValue)annotation,paramType);
				paramAnno.setCookieValue((param));
				if(param.editor() != null && !param.editor().equals(""))
					paramAnno.setEditor((EditorInf)BeanUtils.instantiateClass(param.editor()));
				if(!param.name().equals(""))
					paramAnno.setParameterName(param.name());
				else
					paramAnno.setParameterName(methodparamname);
//				paramAnno.setParameterName(param.name());
				paramAnno.setDataBindScope(Scope.COOKIE);
				
				Object aa = param.defaultvalue();
				if(aa != null)
					paramAnno.setDefaultValue(aa);
				mutilMethodParamAnnotations.add(paramAnno);
				continue;
			}
			else if(annotation instanceof RequestHeader)
			{
				paramAnno  = new MethodParameter(method,parampostion);
				RequestHeaderWraper param = new RequestHeaderWraper((RequestHeader)annotation,paramType);
				paramAnno.setRequestHeader(param);
				if(param.editor() != null && !param.editor().equals(""))
					paramAnno.setEditor((EditorInf)BeanUtils.instantiateClass(param.editor()));
				if(!param.name().equals(""))
					paramAnno.setParameterName(param.name());
				else
					paramAnno.setParameterName(methodparamname);
//				paramAnno.setParameterName(param.name());
				paramAnno.setDataBindScope(Scope.REQUEST_HEADER);
				Object aa = param.defaultvalue();
				if(aa != null)
					paramAnno.setDefaultValue(aa);
				mutilMethodParamAnnotations.add(paramAnno);
				continue;
				
			}
			
			else if(annotation instanceof Attribute)
			{
				paramAnno  = new MethodParameter(method,parampostion);
				AttributeWraper param = new AttributeWraper((Attribute)annotation,paramType);
				paramAnno.setAttribute(param);
				paramAnno.setRequired(param.required());
				if(param.editor() != null && !param.editor().equals(""))
					paramAnno.setEditor((EditorInf)BeanUtils.instantiateClass(param.editor()));
				if(!param.name().equals(""))
					paramAnno.setParameterName(param.name());
				else
					paramAnno.setParameterName(methodparamname);
//				paramAnno.setParameterName(param.name());
				if(param.scope() == AttributeScope.PAGECONTEXT_APPLICATION_SCOPE)
					paramAnno.setDataBindScope(Scope.PAGECONTEXT_APPLICATION_SCOPE);
				else if(param.scope() == AttributeScope.PAGECONTEXT_PAGE_SCOPE)
					paramAnno.setDataBindScope(Scope.PAGECONTEXT_PAGE_SCOPE);
				else if(param.scope() == AttributeScope.PAGECONTEXT_REQUEST_SCOPE)
					paramAnno.setDataBindScope(Scope.PAGECONTEXT_REQUEST_SCOPE);
				else if(param.scope() == AttributeScope.PAGECONTEXT_SESSION_SCOPE)
					paramAnno.setDataBindScope(Scope.PAGECONTEXT_SESSION_SCOPE);
				else if(param.scope() == AttributeScope.REQUEST_ATTRIBUTE)
					paramAnno.setDataBindScope(Scope.REQUEST_ATTRIBUTE);
				else if(param.scope() == AttributeScope.SESSION_ATTRIBUTE)
					paramAnno.setDataBindScope(Scope.SESSION_ATTRIBUTE);
				else if(param.scope() == AttributeScope.MODEL_ATTRIBUTE)
					paramAnno.setDataBindScope(Scope.MODEL_ATTRIBUTE);
				Object aa = param.defaultvalue();
				if(aa != null)
					paramAnno.setDefaultValue(aa);
				mutilMethodParamAnnotations.add(paramAnno);
				continue;
			}
			
		}
		
		if(mutilMethodParamAnnotations.size() == 0)
		{
			if(!ismapkey)
			{
				boolean isprimary = ValueObjectUtil.isPrimaryType(paramType);
				if(isprimary )
				{
					MethodParameter temp = new MethodParameter(method,parampostion);
					temp.setParameterName(methodparamname);
					temp.setPrimaryType(isprimary);
					return temp;
				}
				else
				{
					MethodParameter temp = new MethodParameter(method,parampostion);
					temp.setParameterName(methodparamname);
					temp.setPrimaryType(isprimary);
					return temp;
				}
				
			}
			return ret;
		}
		ret.setMultiAnnotationParams(mutilMethodParamAnnotations);
		return ret;
	}
//	private void genericParameterTypes()
//	{
////		Type[] types = method.getGenericParameterTypes();
////		
////		if(types == null || types.length == 0)
////		{
////			return;
////		}
////		genericParameterTypes = new Class[types.length];
////		for(int i = 0;  i < types.length; i ++)
////		{
////			Type type = types[i]; 
////			if(type instanceof ParameterizedType)
////			{
////				Class listClass = (Class)type;
////				if(List.class.isAssignableFrom(listClass) || Set.class.isAssignableFrom(listClass))
////				{
////					Type zzz = ((ParameterizedType)type).getActualTypeArguments()[0];
////					this.genericParameterTypes[i] = (Class)zzz;
////				}
////			}
////		}
//		genericParameterTypes = ClassUtils.genericParameterTypes(method);
//		
//		
//		
//	}
	
	private Map<Integer,Object> genericParameterTypes = new HashMap<Integer,Object>();
	public Class getGenericParameterType(int i)
	{
//		if(this.genericParameterTypes != null )
//		{
//			return this.genericParameterTypes[i];
//		}
//		else
//			return null;
		return ClassUtils.genericParameterType(method, i);
	}
	
	public Class[] getGenericParameterTypes(int i)
	{
//		if(this.genericParameterTypes != null )
//		{
//			return this.genericParameterTypes[i];
//		}
//		else
//			return null;
		return ClassUtils.genericParameterTypes(method, i);
	}
	
	private void parserInfo()
	{
		Annotation[][] annotations = method.getParameterAnnotations();
		Class[] paramTypes = method.getParameterTypes();
		ParameterNameDiscoverer parameterNameDiscoverer = ClassUtil.getParameterNameDiscoverer();
		String[] temp_paramNames = parameterNameDiscoverer.getParameterNames(getMethod());
		/**
		 * 如果方法没有指定任何注解，并且没有通过asm获取到方法参数名称
		 * 则直接返回
		 */
		if((temp_paramNames == null || temp_paramNames.length == 0 ) 
				&& (annotations == null || annotations.length ==0))
			return;
//		editors = new EditorInf[annotations.length];
		paramNames = new MethodParameter[annotations.length];
		databind = new boolean[annotations.length];
		for(int i = 0; i < annotations.length; i ++)
		{
			String methodparamName = temp_paramNames == null || temp_paramNames.length == 0?"": temp_paramNames[i];
			if(annotations[i].length == 0)
			{
//				editors[i] = null;
				boolean isprimary = ValueObjectUtil.isPrimaryType(paramTypes[i]);
				if(isprimary && !methodparamName.equals(""))
				{
					MethodParameter temp = new MethodParameter(method,i);
					temp.setParameterName(methodparamName);
					temp.setPrimaryType(isprimary);
					paramNames[i] = temp;
				}
				else if(ValueObjectUtil.isCollectionType(paramTypes[i])  && !methodparamName.equals(""))
				{
					MethodParameter temp = new MethodParameter(method,i);
					temp.setParameterName(methodparamName);
					temp.setPrimaryType(false);
					paramNames[i] = temp;
				}
				else
				{
					MethodParameter temp = new MethodParameter(method,i);
					temp.setParameterName(methodparamName);
					temp.setPrimaryType(false);
					paramNames[i] = temp;
				}
			}
			else
			{
				paramNames[i] = buildMutilMethodParamAnnotations(annotations[i],i,methodparamName,paramTypes[i]);
			}
		}
		
	}

	public Method getMethod() {
		return method;
	}

	public MethodParameter[] getParamNames() {
		return paramNames;
	}
	
	public EditorInf getEditor(int index) {
		if(paramNames != null)
		{
			 if(this.paramNames.length < index + 1 )
				 throw new BeansException("非法的param index ["+index+"]：paramNames length is " + paramNames.length);
			return paramNames[index].getEditor();
		}
		return null;
	}
	public String getParamName(int index) {
		if(paramNames != null )
		{
			if(this.paramNames.length < index + 1 )
				 throw new BeansException("非法的paramNames index ["+index+"]：paramNames length is " + paramNames.length);
			return paramNames[index].getRequestParameterName();
		}
		return null;
		
	}
	
	public MethodParameter getMethodParameter(int index)
	{
		return paramNames[index];
	}
	public boolean isDataBind(int index)
	{
		if(databind != null)
		{
			 if(this.databind.length < index + 1 )
				 throw new BeansException("非法的databind index ["+index+"]：databind length is " + databind.length);
		}
		return this.databind[index];
	}
	
	
 

	public HandlerMapping getMethodMapping() {
		return mapping;
	}

	public HandlerMapping getTypeLevelMapping() {
		return typeLevelMapping;
	}

	public String[] getPathPattern() {
		return pathPattern;
	}

	public PathVariableInfo[] getPathVariables() {
		return pathVariables;
	}

//	public Integer[] getPathVariablePositions() {
//		return pathVariablePositions;
//	}

	/**
	 * @return the pagerMethod
	 */
	public boolean isPagerMethod() {
		return pagerMethod;
	}

	/**
	 * @param pagerMethod the pagerMethod to set
	 */
	public void setPagerMethod(boolean pagerMethod) {
		this.pagerMethod = pagerMethod;
	}

	/**
	 * @return the definePageSize
	 */
	public boolean isDefinePageSize() {
		return definePageSize;
	}

	/**
	 * @param definePageSize the definePageSize to set
	 */
	public void setDefinePageSize(boolean definePageSize) {
		this.definePageSize = definePageSize;
	}

	/**
	 * @return the pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	
	public ResponseBodyWraper getResponsebodyAnno()
	{
	
		return responsebodyAnno;
	}

	
	public void setResponsebodyAnno(ResponseBodyWraper responsebodyAnno)
	{
	
		this.responsebodyAnno = responsebodyAnno;
	}

	
//	public MediaType getResponseMediaType()
//	{
//	
//		return responseMediaType;
//	}

	
//	public void setResponseMediaType(MediaType responseMediaType)
//	{
//	
//		this.responseMediaType = responseMediaType;
//	}

	public boolean isRequiredDToken() {
		return requiredDToken;
	}

	public AssertDToken getAssertDToken() {
		return assertDToken;
	}

	public boolean isRequireTicket() {
		return requireTicket;
	}

	public AssertTicket getAssertTicket() {
		return assertTicket;
	}
	
	public static void main(String[] args)
	{
		String path = "/ap/bc/{v1}/{v2}.page";
		
		String path1 = "/ap/bc/{v1}/{v2}";
		
		String data = "/ap/bc/ss/dd.page";
		
	}

    /**
     * 判断控制器方法是否是reactor Flux方法
     * @return
     */
    public boolean isReactorFlux() {
        return reactorFlux;
    }

    /**
     * 判断控制器方法是否是reactor Mono方法
     * @return
     */
    public boolean isReactorMono() {
        return reactorMono;
    }

}
