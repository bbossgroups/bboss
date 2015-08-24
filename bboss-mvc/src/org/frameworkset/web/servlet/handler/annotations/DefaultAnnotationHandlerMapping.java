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
package org.frameworkset.web.servlet.handler.annotations;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.frameworkset.util.ClassUtil;
import org.frameworkset.util.annotations.AnnotationUtils;
import org.frameworkset.util.annotations.HandlerMapping;
import org.frameworkset.util.annotations.HttpMethod;
import org.frameworkset.web.HttpRequestMethodNotSupportedException;
import org.frameworkset.web.servlet.handler.AbstractDetectingUrlHandlerMapping;
import org.frameworkset.web.servlet.handler.HandlerUtils;

import com.frameworkset.util.StringUtil;

/**
 * <p>Title: DefaultAnnotationHandlerMapping.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-10-23
 * @author biaoping.yin
 * @version 1.0
 */
public class DefaultAnnotationHandlerMapping  extends AbstractDetectingUrlHandlerMapping {

	private boolean useDefaultSuffixPattern = true;

	private Map<Class, HandlerMapping> cachedMappings = new HashMap<Class, HandlerMapping>();

	
	/**
	 * Set whether to register paths using the default suffix pattern as well:
	 * i.e. whether "/users" should be registered as "/users.*" too.
	 * <p>Default is "true". Turn this convention off if you intend to interpret
	 * your <code>@HandlerMapping</code> paths strictly.
	 * <p>Note that paths which include a ".xxx" suffix already will not be
	 * transformed using the default suffix pattern in any case.
	 */
	public void setUseDefaultSuffixPattern(boolean useDefaultSuffixPattern) {
		this.useDefaultSuffixPattern = useDefaultSuffixPattern;
	}


//	/**
//	 * Checks for presence of the {@link org.frameworkset.util.annotations.HandlerMapping}
//	 * annotation on the handler class and on any of its methods.
//	 */
//	protected String[] determineUrlsForHandler(String beanName) {
//		ApplicationContext context = getApplicationContext();
//		Pro beaninfos = context.getProBean(beanName);
//		if(!beaninfos.isBean())
//			return null;
//		Class<?> handlerType = beaninfos.getBeanClass();
////		ListableBeanFactory bf = (context instanceof ConfigurableApplicationContext ?
////				((ConfigurableApplicationContext) context).getBeanFactory() : context);
////		GenericBeanFactoryAccessor bfa = new GenericBeanFactoryAccessor(bf);
//		
//		//获取类级url和controller映射关系
//		HandlerMapping mapping = AnnotationUtils.findAnnotation(handlerType, HandlerMapping.class);
//
//		if (mapping != null) {
//			// @HandlerMapping found at type level
//			this.cachedMappings.put(handlerType, mapping);
//			Set<String> urls = new LinkedHashSet<String>();
//			String[] paths = mapping.value();
//			if (paths.length > 0) {
//				// @HandlerMapping specifies paths at type level
//				for (String path : paths) {
//					addUrlsForPath(urls, path);
//				}
//				
//				return StringUtil.toStringArray(urls);
//			}
//			else {
//				// actual paths specified by @HandlerMapping at method level
//				return determineUrlsForHandlerMethods(handlerType);
//			}
//		}
//		else if (AnnotationUtils.findAnnotation(handlerType, Controller.class) != null) {
//			// @HandlerMapping to be introspected at method level
//			return determineUrlsForHandlerMethods(handlerType);
//		}
//		else {
//			return null;
//		}
//	}
	
	protected String[] determineUrlsForHandler(String beanName) {
		return HandlerUtils.determineUrlsForHandler(getApplicationContext(), beanName, cachedMappings);
	}
	
//	/**
//	 * Checks for presence of the {@link org.frameworkset.util.annotations.HandlerMapping}
//	 * annotation on the handler class and on any of its methods.
//	 */
//	protected String[] determineUrlsForHandler(String beanName) {
//		ApplicationContext context = getApplicationContext();
//		Pro beaninfos = context.getProBean(beanName);
//		if(!beaninfos.isBean())
//			return null;
//		final Class<?> handlerType = beaninfos.getBeanClass();
////		ListableBeanFactory bf = (context instanceof ConfigurableApplicationContext ?
////				((ConfigurableApplicationContext) context).getBeanFactory() : context);
////		GenericBeanFactoryAccessor bfa = new GenericBeanFactoryAccessor(bf);
//		
//		//获取类级url和controller映射关系
//		HandlerMapping mapping = AnnotationUtils.findAnnotation(handlerType, HandlerMapping.class);
//
//		if (mapping != null) {
//			// @HandlerMapping found at type level
//			this.cachedMappings.put(handlerType, mapping);
//			
//			Set<String> urls = new LinkedHashSet<String>();
//			final Set<Method> handlerMethods = new LinkedHashSet<Method>();
//			ReflectionUtils.doWithMethods(handlerType, new ReflectionUtils.MethodCallback() {
//				public void doWith(Method method) {
//					if (method.isAnnotationPresent(HandlerMapping.class)) {
//						handlerMethods.add(ClassUtils.getMostSpecificMethod(method, handlerType));
//					}					
//				}
//			});
//			String[] paths = mapping.value();
//			if (paths.length > 0) {
//				// @HandlerMapping specifies paths at type level
//				for (String path : paths) {
//					
//					
//					if(mapping.restful())
//					{
//						this.addUrlsForRestfulPath(urls, path, handlerMethods);
//					}
//					else
//					{
//						addUrlsForPath(urls, path);
//					}
//				}
//				
//				return StringUtil.toStringArray(urls);
//			}
//			else {
//				// actual paths specified by @HandlerMapping at method level
//				return determineUrlsForHandlerMethods(handlerType);
//			}
//		}
//		else if (AnnotationUtils.findAnnotation(handlerType, Controller.class) != null) {
//			// @HandlerMapping to be introspected at method level
//			return determineUrlsForHandlerMethods(handlerType);
//		}
//		else {
//			return null;
//		}
//	}

//	/**
//	 * Derive URL mappings from the handler's method-level mappings.
//	 * @param handlerType the handler type to introspect
//	 * @return the array of mapped URLs
//	 */
//	protected String[] determineUrlsForHandlerMethods(Class<?> handlerType) {
//		final Set<String> urls = new LinkedHashSet<String>();
//		ReflectionUtils.doWithMethods(handlerType, new ReflectionUtils.MethodCallback() {
//			public void doWith(Method method) {
//				HandlerMapping mapping = method.getAnnotation(HandlerMapping.class);
//				if (mapping != null) {
//					String[] mappedPaths = mapping.value();
//					for (int i = 0; i < mappedPaths.length; i++) {
//						addUrlsForPath(urls, mappedPaths[i]);
//					}
//				}
//			}
//		});
//		return StringUtil.toStringArray(urls);
//	}
//
//	/**
//	 * Add URLs and/or URL patterns for the given path.
//	 * @param urls the Set of URLs for the current bean
//	 * @param path the currently introspected path
//	 */
//	protected void addUrlsForPath(Set<String> urls, String path) {
////		Iterator<Method> methods = handlermethods.iterator();
////		while(methods.hasNext())
////		{
////			urls.addAll(this.getUrl(path, restful, methods.next()));
//			urls.add(path);
//			if (this.useDefaultSuffixPattern && path.indexOf('.') == -1) {
//				urls.add(path + ".*");
//			}
////		}
//	}
//	
//	/**
//	 * Add URLs and/or URL patterns for the given path.
//	 * @param urls the Set of URLs for the current bean
//	 * @param path the currently introspected path
//	 */
//	protected void addUrlsForRestfulPath(Set<String> urls, String path,Set<Method> handlermethods) {
//		Iterator<Method> methods = handlermethods.iterator();
//		while(methods.hasNext())
//		{
//			urls.addAll(this.getRestfulUrl(path, methods.next()));
//
//		}
//	}
//	
////	protected int convert(HttpMethod requestMethod)
////	{
////		
////	}
//	protected Set<String> getRestfulUrl(String path, Method method)
//	{
//		
//		String url = path;
//		Set<String> urls = new LinkedHashSet<String>();
//		HandlerMapping mapping = method.getAnnotation(HandlerMapping.class);
////		MethodInfo methodInfo = new MethodInfo(method);
//		String[] mappedPaths = mapping.value();
//		if (mappedPaths != null && mappedPaths.length > 0) {	
//			String mappedPath = mappedPaths[0];
//			StringBuffer pathUrl = new StringBuffer();
//			pathUrl.append(url);
//			for(int i = 0; i < mappedPath.length(); i ++ )
//			{
//				if(mappedPath.charAt(i) == '/')
//					pathUrl.append("/*");
//				
//			}
//			urls.add(pathUrl.toString());
//			pathUrl = null;
//		}
//		else
//		{
//			urls.add(url);
//		}
//		return urls;
//		
//	}
	
	


	/**
	 * Validate the given annotated handler against the current request.
	 * @see #validateMapping
	 */
	protected void validateHandler(Object handler, HttpServletRequest request) throws Exception {
		HandlerMapping mapping = this.cachedMappings.get(ClassUtil.getClassInfo(handler.getClass()).getClazz());
		if (mapping == null) {
			mapping = AnnotationUtils.findAnnotation(ClassUtil.getClassInfo(handler.getClass()).getClazz(), HandlerMapping.class);
		}
		if (mapping != null) {
			validateMapping(mapping, request);
		}
	}

	/**
	 * Validate the given type-level mapping metadata against the current request,
	 * checking HTTP request method and parameter conditions.
	 * @param mapping the mapping metadata to validate
	 * @param request current HTTP request
	 * @throws Exception if validation failed
	 */
	protected void validateMapping(HandlerMapping mapping, HttpServletRequest request) throws Exception {
		HttpMethod[] mappedMethods = mapping.method();
		if (!ServletAnnotationMappingUtils.checkRequestMethod(mappedMethods, request)) {
			String[] supportedMethods = new String[mappedMethods.length];
			for (int i = 0; i < mappedMethods.length; i++) {
				supportedMethods[i] = mappedMethods[i].name();
			}
			throw new HttpRequestMethodNotSupportedException(request.getMethod(), supportedMethods);
		}

		String[] mappedParams = mapping.params();
		if (!ServletAnnotationMappingUtils.checkParameters(mappedParams, request)) {
			throw new ServletException("Parameter conditions {" +
					StringUtil.arrayToDelimitedString(mappedParams, ", ") +
					"} not met for request parameters: " + request.getParameterMap());
		}
	}


	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		super.destroy();
		if(cachedMappings != null)
		{
			cachedMappings.clear();
			cachedMappings = null;
		}
		
	}

}
