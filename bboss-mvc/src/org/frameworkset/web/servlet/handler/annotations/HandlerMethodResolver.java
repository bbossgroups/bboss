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

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.apache.log4j.Logger;
import org.frameworkset.util.ClassUtils;
import org.frameworkset.util.annotations.HandlerMapping;
import org.frameworkset.util.annotations.MethodInfo;
import org.frameworkset.util.annotations.ModelAttribute;
import org.frameworkset.util.annotations.SessionAttributes;
import org.frameworkset.web.servlet.ModelAndView;
import org.frameworkset.web.servlet.handler.HandlerUtils;



/**
 * <p>Title: HandlerMethodResolver.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-10-24
 * @author biaoping.yin
 * @version 1.0
 */
public class HandlerMethodResolver {
	
	private  Set<MethodInfo> handlerMethods = new LinkedHashSet<MethodInfo>();
	/** Methods, keyed by exception class */
	private  Map<Class, Method> exceptionHandlerMap = new HashMap<Class, Method>();
	private final static Logger log = Logger.getLogger(HandlerMethodResolver.class);

	private final Set<Method> initBinderMethods = new LinkedHashSet<Method>();

	private final Set<Method> modelAttributeMethods = new LinkedHashSet<Method>();

	private final HandlerMapping typeLevelMapping;

	private final boolean sessionAttributesFound;

	private final Set<String> sessionAttributeNames = new HashSet<String>();

	private final Set<Class> sessionAttributeTypes = new HashSet<Class>();

	private final Set<String> actualSessionAttributeNames = Collections.synchronizedSet(new HashSet<String>(4));
	/**
	 * Is the supplied method a valid exception handler method?
	 */
	private boolean isExceptionHandlerMethod(Method method) {
		Class returnType = method.getReturnType();
		return ((ModelAndView.class.equals(returnType)
				|| Map.class.equals(returnType)
				|| String.class.equals(returnType)
				|| void.class.equals(returnType)) &&
				method.getParameterTypes().length >= 4 && HttpServletRequest.class
						.isAssignableFrom(method.getParameterTypes()[0])
						&& HttpServletResponse.class
								.isAssignableFrom(method.getParameterTypes()[1])
						&& PageContext.class
								.isAssignableFrom(method.getParameterTypes()[2]) && 
				Throwable.class.isAssignableFrom(method.getParameterTypes()[3]));
	}
	/**
	 * Registers the supplied method as an exception handler.
	 */
	private void registerExceptionHandlerMethod(Method method) {
		this.exceptionHandlerMap.put(method.getParameterTypes()[3], method);
		
		log.debug("Found exception handler method [" + method + "]");
		
	}
	
	/**
	 * Determine the exception handler method for the given exception.
	 * <p>Can return <code>null</code> if not found.
	 * @return a handler for the given exception type, or <code>null</code>
	 * @param exception the exception to handle
	 */
	protected Method getExceptionHandler(Throwable exception) {
		Class exceptionClass = exception.getClass();
		
		log.debug("Trying to find handler for exception class [" + exceptionClass.getName() + "]");
		
		Method handler = this.exceptionHandlerMap.get(exceptionClass);
		while (handler == null && !exceptionClass.equals(Throwable.class)) {
			
			log.debug("Trying to find handler for exception superclass [" + exceptionClass.getName() + "]");
			
			exceptionClass = exceptionClass.getSuperclass();
			handler = this.exceptionHandlerMap.get(exceptionClass);
		}
		return handler;
	}
	
	
	/**
	 * Create a new HandlerMethodResolver for the specified handler type.
	 * @param handlerType the handler class to introspect
	 */
	public HandlerMethodResolver(final Class<?> handlerType) {
		this.typeLevelMapping = handlerType.getAnnotation(HandlerMapping.class);
		Method[] methods = handlerType.getMethods();
		
		for(Method method:methods) {
			if (isExceptionHandlerMethod(method)) {
				registerExceptionHandlerMethod(method);
			}
			else	if (HandlerUtils.isHandlerMethod(handlerType,method)) {
					handlerMethods.add(new MethodInfo(ClassUtils.getMostSpecificMethod(method, handlerType),typeLevelMapping ));
				}
//				else if (method.isAnnotationPresent(InitBinder.class)) {
//					initBinderMethods.add(ClassUtils.getMostSpecificMethod(method, handlerType));
//				}
				else if (method.isAnnotationPresent(ModelAttribute.class)) {
					modelAttributeMethods.add(ClassUtils.getMostSpecificMethod(method, handlerType));
				}
			}
		
		
		SessionAttributes sessionAttributes = handlerType.getAnnotation(SessionAttributes.class);
		this.sessionAttributesFound = (sessionAttributes != null);
		if (this.sessionAttributesFound) {
			this.sessionAttributeNames.addAll(Arrays.asList(sessionAttributes.value()));
			this.sessionAttributeTypes.addAll(Arrays.asList(sessionAttributes.types()));
		}
	}
	
	/**
	 * Create a new HandlerMethodResolver for the specified handler type.
	 * @param handlerType the handler class to introspect
	 */
	public HandlerMethodResolver(final Class<?> handlerType,final String baseurls[]) {
		this.typeLevelMapping = handlerType.getAnnotation(HandlerMapping.class);
		Method[] methods = handlerType.getMethods();
		for(Method method:methods)
		{
				
				if (HandlerUtils.isHandlerMethod(handlerType,method)) {
					//System.out.println(method);
					handlerMethods.add(new MethodInfo(ClassUtils.getMostSpecificMethod(method, handlerType),baseurls));
				}
//				else if (method.isAnnotationPresent(InitBinder.class)) {
//					initBinderMethods.add(ClassUtils.getMostSpecificMethod(method, handlerType));
//				}
				else if (method.isAnnotationPresent(ModelAttribute.class)) {
					modelAttributeMethods.add(ClassUtils.getMostSpecificMethod(method, handlerType));
				}
			}
		
		SessionAttributes sessionAttributes = handlerType.getAnnotation(SessionAttributes.class);
		this.sessionAttributesFound = (sessionAttributes != null);
		if (this.sessionAttributesFound) {
			this.sessionAttributeNames.addAll(Arrays.asList(sessionAttributes.value()));
			this.sessionAttributeTypes.addAll(Arrays.asList(sessionAttributes.types()));
		}
	}


	public final boolean hasHandlerMethods() {
		return !this.handlerMethods.isEmpty();
	}

	public final Set<MethodInfo> getHandlerMethods() {
		return this.handlerMethods;
	}

	public final Set<Method> getInitBinderMethods() {
		return this.initBinderMethods;
	}

	public final Set<Method> getModelAttributeMethods() {
		return this.modelAttributeMethods;
	}

	public boolean hasTypeLevelMapping() {
		return (this.typeLevelMapping != null);
	}

	public HandlerMapping getTypeLevelMapping() {
		return this.typeLevelMapping;
	}

	public boolean hasSessionAttributes() {
		return this.sessionAttributesFound;
	}

	public boolean isSessionAttribute(String attrName, Class attrType) {
		if (this.sessionAttributeNames.contains(attrName) || this.sessionAttributeTypes.contains(attrType)) {
			this.actualSessionAttributeNames.add(attrName);
			return true;
		}
		else {
			return false;
		}
	}

	public Set<String> getActualSessionAttributeNames() {
		return this.actualSessionAttributeNames;
	}
	public void destroy()
	{
		if(handlerMethods != null)
		{
			handlerMethods.clear();
			handlerMethods = null;
		}
		
		if(exceptionHandlerMap != null)
		{
			exceptionHandlerMap.clear();
			exceptionHandlerMap = null;
		}
	}

}
