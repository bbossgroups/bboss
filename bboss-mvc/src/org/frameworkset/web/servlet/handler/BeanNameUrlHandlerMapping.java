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
package org.frameworkset.web.servlet.handler;




/**
 * <p>Title: BeanNameUrlHandlerMapping.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-9-24
 * @author biaoping.yin
 * @version 1.0
 */
public class BeanNameUrlHandlerMapping extends AbstractDetectingUrlHandlerMapping{
	private boolean useDefaultSuffixPattern = true;




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
	/**
	 * Checks name and aliases of the given bean for URLs, starting with "/".
	 */
	protected String[] determineUrlsForHandler(String beanName) {
		
//		List urls = new ArrayList();
//		if (beanName.startsWith("/")) {
//			urls.add(beanName);
//		}
//		String[] aliases = getApplicationContext().getAliases(beanName);
//		for (int i = 0; i < aliases.length; i++) {
//			if (aliases[i].startsWith("/")) {
//				urls.add(aliases[i]);
//			}
//		}
//		return StringUtil.toStringArray(urls);
//		boolean iscontroller = beanName != null && beanName.startsWith("/");
//		Pro pro = this.getApplicationContext().getProBean(beanName);
//		boolean restful = pro.getBooleanExtendAttribute("restful", false);
//		if(iscontroller)
//		{
//			
//				return beanName.split(",");
//			
//		}
		
//		return null;
//		return determineUrlsForHandler_(beanName);
		return HandlerUtils.determineUrlsForHandler(getApplicationContext(), beanName, null);
	}
	
	
//	/**
//	 * Checks name and aliases of the given bean for URLs, starting with "/".
//	 */
//	protected String[] determineUrlsForHandler(String beanName) {
//		
////		List urls = new ArrayList();
////		if (beanName.startsWith("/")) {
////			urls.add(beanName);
////		}
////		String[] aliases = getApplicationContext().getAliases(beanName);
////		for (int i = 0; i < aliases.length; i++) {
////			if (aliases[i].startsWith("/")) {
////				urls.add(aliases[i]);
////			}
////		}
////		return StringUtil.toStringArray(urls);
//		boolean iscontroller = beanName != null && beanName.startsWith("/");
//		Pro pro = this.getApplicationContext().getProBean(beanName);
//		boolean restful = pro.getBooleanExtendAttribute("restful", false);
//		if(iscontroller)
//		{
//			
//				return beanName.split(",");
//			
//		}
//		
//		return null;
////		return determineUrlsForHandler_(beanName);
//	}
	
//	protected String[] determineUrlsForHandler_(String beanName) {
//		ApplicationContext context = getApplicationContext();
//		Pro beaninfos = context.getProBean(beanName);
//		if(!beaninfos.isBean())
//			return null;
//		Class<?> handlerType = beaninfos.getBeanClass();
////		ListableBeanFactory bf = (context instanceof ConfigurableApplicationContext ?
////				((ConfigurableApplicationContext) context).getBeanFactory() : context);
////		GenericBeanFactoryAccessor bfa = new GenericBeanFactoryAccessor(bf);
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
//
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
//		urls.add(path);
//		if (this.useDefaultSuffixPattern && path.indexOf('.') == -1) {
//			urls.add(path + ".*");
//		}
//	}
//
//
//	/**
//	 * Validate the given annotated handler against the current request.
//	 * @see #validateMapping
//	 */
//	protected void validateHandler(Object handler, HttpServletRequest request) throws Exception {
//		HandlerMapping mapping = this.cachedMappings.get(handler.getClass());
//		if (mapping == null) {
//			mapping = AnnotationUtils.findAnnotation(handler.getClass(), HandlerMapping.class);
//		}
//		if (mapping != null) {
//			validateMapping(mapping, request);
//		}
//	}
//
//	/**
//	 * Validate the given type-level mapping metadata against the current request,
//	 * checking HTTP request method and parameter conditions.
//	 * @param mapping the mapping metadata to validate
//	 * @param request current HTTP request
//	 * @throws Exception if validation failed
//	 */
//	protected void validateMapping(HandlerMapping mapping, HttpServletRequest request) throws Exception {
//		RequestMethod[] mappedMethods = mapping.method();
//		if (!ServletAnnotationMappingUtils.checkRequestMethod(mappedMethods, request)) {
//			String[] supportedMethods = new String[mappedMethods.length];
//			for (int i = 0; i < mappedMethods.length; i++) {
//				supportedMethods[i] = mappedMethods[i].name();
//			}
//			throw new HttpRequestMethodNotSupportedException(request.getMethod(), supportedMethods);
//		}
//
//		String[] mappedParams = mapping.params();
//		if (!ServletAnnotationMappingUtils.checkParameters(mappedParams, request)) {
//			throw new ServletException("Parameter conditions {" +
//					StringUtil.arrayToDelimitedString(mappedParams, ", ") +
//					"} not met for request parameters: " + request.getParameterMap());
//		}
//	}

}
