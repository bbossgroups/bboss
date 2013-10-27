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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.apache.log4j.Logger;
import org.frameworkset.http.converter.HttpMessageConverter;
import org.frameworkset.util.ReflectionUtils;
import org.frameworkset.util.annotations.MethodData;
import org.frameworkset.web.servlet.HandlerMapping;
import org.frameworkset.web.servlet.ModelMap;
import org.frameworkset.web.servlet.handler.AbstractUrlHandlerMapping;
import org.frameworkset.web.servlet.handler.HandlerMeta;
import org.frameworkset.web.servlet.handler.HandlerUtils;
import org.frameworkset.web.servlet.support.RequestContext;

import com.frameworkset.util.BeanUtils;

/**
 * <p>
 * Title: HandlerMethodInvoker.java
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * bboss workgroup
 * </p>
 * <p>
 * Copyright (c) 2008
 * </p>
 * 
 * @Date 2010-10-24
 * @author biaoping.yin
 * @version 1.0
 */
public class HandlerMethodInvoker {

	/**
	 * We'll create a lot of these objects, so we don't want a new logger every
	 * time.
	 */
	protected final static Logger logger = Logger.getLogger(HandlerMethodInvoker.class);

	private final HandlerMethodResolver methodResolver;
//
////	private final WebBindingInitializer bindingInitializer;
//
//	private final SessionAttributeStore sessionAttributeStore;
//
//	private final ParameterNameDiscoverer parameterNameDiscoverer;
//
//	private final WebArgumentResolver[] customArgumentResolvers;
	protected HttpMessageConverter[] messageConverters;

//	private final SimpleSessionStatus sessionStatus = new SimpleSessionStatus();

//	public HandlerMethodInvoker(HandlerMethodResolver methodResolver,HttpMessageConverter[] messageConverters) {
//		this(methodResolver, null,messageConverters);
//	}

//	public HandlerMethodInvoker(HandlerMethodResolver methodResolver,
//			WebBindingInitializer bindingInitializer,
//			HttpMessageConverter[] messageConverters) {
//		this(messageConverters,methodResolver, 
////				bindingInitializer,
//				new DefaultSessionAttributeStore(), null);
//	}

	public HandlerMethodInvoker(HttpMessageConverter[] messageConverters,
//			,
			HandlerMethodResolver methodResolver
//			,
//			WebBindingInitializer bindingInitializer,
//			SessionAttributeStore sessionAttributeStore,
//			ParameterNameDiscoverer parameterNameDiscoverer,
//			WebArgumentResolver... customArgumentResolvers
			) {

		this.methodResolver = methodResolver;
//		this.bindingInitializer = bindingInitializer;
//		this.sessionAttributeStore = sessionAttributeStore;
//		this.parameterNameDiscoverer = parameterNameDiscoverer;
//		this.customArgumentResolvers = customArgumentResolvers;
		this.messageConverters = messageConverters;
	}

	public final Object invokeHandlerMethod(MethodData handlerMethod,
			HandlerMeta handler, HttpServletRequest request,
			HttpServletResponse response, PageContext pageContext,
			ModelMap implicitModel) throws Exception {

		// Method handlerMethodToInvoke =
		// BridgeMethodResolver.findBridgedMethod(handlerMethod);
		try {
			boolean debug = logger.isDebugEnabled();
			// for (Method attributeMethod :
			// this.methodResolver.getModelAttributeMethods()) {
			// Method attributeMethodToInvoke =
			// BridgeMethodResolver.findBridgedMethod(attributeMethod);
			// Object[] args = resolveHandlerArguments(attributeMethodToInvoke,
			// handler, webRequest,pageContext, implicitModel);
			// if (debug) {
			// logger.debug("Invoking model attribute method: " +
			// attributeMethodToInvoke);
			// }
			// Object attrValue = doInvokeMethod(attributeMethodToInvoke,
			// handler, args);
			// String attrName =
			// AnnotationUtils.findAnnotation(attributeMethodToInvoke,
			// org.frameworkset.util.annotations.ModelAttribute.class).name();
			// if ("".equals(attrName)) {
			// Class resolvedType =
			// GenericTypeResolver.resolveReturnType(attributeMethodToInvoke,
			// handler.getClass());
			// attrName =
			// Conventions.getVariableNameForReturnType(attributeMethodToInvoke,
			// resolvedType, attrValue);
			// }
			// implicitModel.addAttribute(attrName, attrValue);
			// }
			// System.out.println("_________________"+request.getMethod());
			
			/**
			 * HttpServletRequest request, HttpServletResponse
			 * response,PageContext pageContext, MethodData handlerMethod,
			 * ModelMap model,Validator[] validators
			 */
			if(handlerMethod.getMethodInfo().isPagerMethod())//
			{
				AbstractUrlHandlerMapping.exposeAttribute(HandlerMapping.PAGER_METHOD_FLAG_ATTRIBUTE, new Boolean(true), request);
			}
			if(!handlerMethod.getMethodInfo().isDefinePageSize())
			{
				String cookieid = RequestContext.getPagerSizeCookieID(request, null);
				int defaultSize = RequestContext.getPagerSize(request,  HandlerMapping.DEFAULT_PAGE_SIZE,cookieid);
				AbstractUrlHandlerMapping.exposeAttribute(HandlerMapping.PAGER_PAGESIZE_FLAG_ATTRIBUTE, new Integer(defaultSize), request);
				AbstractUrlHandlerMapping.exposeAttribute(org.frameworkset.web.servlet.HandlerMapping.PAGER_COOKIEID_ATTRIBUTE, 
						cookieid, request);
				AbstractUrlHandlerMapping.exposeAttribute(org.frameworkset.web.servlet.HandlerMapping.PAGER_CUSTOM_PAGESIZE_ATTRIBUTE, 
						HandlerMapping.DEFAULT_PAGE_SIZE, request);
				
			}
			
			Object[] args = HandlerUtils.buildMethodCallArgs(request, response,
					pageContext, handlerMethod, implicitModel, null,messageConverters);
			// Object[] args = resolveHandlerArguments(handlerMethodToInvoke,
			// handler, webRequest, pageContext,implicitModel);
			if (debug) {
				logger.debug("Invoking request handler method: "
						+ handlerMethod.getMethodInfo().getMethod());
			}
			if(!implicitModel.hasErrors())//没有数据绑定错误
			{
				return doInvokeMethod(handlerMethod.getMethodInfo().getMethod(),
						handler, args);
			}
			else//有绑定数据错误
			{
				return doInvokeMethod(handlerMethod.getMethodInfo().getMethod(),
						handler, args);
			}
		} catch (IllegalStateException ex) {
			// Throw exception with full handler method context...
			throw new HandlerMethodInvocationException(handlerMethod
					.getMethodInfo().getMethod(), ex);
		}
		catch (InvocationTargetException ex) {
			// The handler method threw an exception.
			return handleException(handler,request, response,  pageContext, ex.getTargetException(),implicitModel);
		}
		catch (Exception ex) {
			// The binding process threw an exception.
			return handleException(handler,request, response,  pageContext, ex,implicitModel);
		}
		finally
		{
			if(handlerMethod.getMethodInfo().isPagerMethod())
			{
//				AbstractUrlHandlerMapping.removeAttribute(HandlerMapping.PAGER_METHOD_FLAG_ATTRIBUTE, request);
//				AbstractUrlHandlerMapping.removeAttribute(HandlerMapping.PAGER_PAGESIZE_FLAG_ATTRIBUTE, request);
//				AbstractUrlHandlerMapping.removeAttribute(HandlerMapping.PAGER_COOKIEID_ATTRIBUTE, request);
			}
		}
		
	}
	
	/**
	 * We've encountered an exception thrown from a handler method.
	 * Invoke an appropriate exception handler method, if any.
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @param ex the exception that got thrown
	 * @return a ModelAndView to render the response
	 */
	private Object handleException(HandlerMeta delegate,HttpServletRequest request, HttpServletResponse response, PageContext pageContext, Throwable ex,ModelMap implicitModel)
			throws Exception {

		Method handler = methodResolver.getExceptionHandler(ex);
		if (handler != null) {
			 {
				 logger.debug("Invoking exception handler [" + handler + "] for exception: " + ex);
			}
			try {
				Object returnValue = handler.invoke(delegate.getHandler(), request, response,pageContext, ex,implicitModel);
				return returnValue;
//				return massageReturnValueIfNecessary(returnValue);
			}
			catch (InvocationTargetException ex2) {
				logger.error("Original exception overridden by exception handling failure", ex);
				ReflectionUtils.rethrowException(ex2.getTargetException());
			}
			catch (Exception ex2) {
				logger.error("Failed to invoke exception handler method", ex2);
			}
		}
		else {
			// If we get here, there was no custom handler or we couldn't invoke it.
			ReflectionUtils.rethrowException(ex);
		}
		throw new IllegalStateException("Should never get here");
	}

	

	/**
	 * Determine whether the given value qualifies as a "binding candidate",
	 * i.e. might potentially be subject to bean-style data binding later on.
	 */
	protected boolean isBindingCandidate(Object value) {
		return (value != null && !value.getClass().isArray()
				&& !(value instanceof Collection) && !(value instanceof Map) && !BeanUtils
				.isSimpleValueType(value.getClass()));
	}

	private Object doInvokeMethod(Method method, HandlerMeta target, Object[] args)
			throws Exception {
		ReflectionUtils.makeAccessible(method);
		try {
			return method.invoke(target.getHandler(), args);
		} catch (InvocationTargetException ex) {
			ReflectionUtils.rethrowException(ex.getTargetException());
		}
		throw new IllegalStateException("Should never get here");
	}

	protected void raiseMissingParameterException(String paramName,
			Class paramType) throws Exception {
		throw new IllegalStateException("Missing parameter '" + paramName
				+ "' of type [" + paramType.getName() + "]");
	}

	protected void raiseSessionRequiredException(String message)
			throws Exception {
		throw new IllegalStateException(message);
	}

//	protected WebDataBinder createBinder(NativeWebRequest webRequest,
//			Object target, String objectName) throws Exception {
//
//		return new ServletRequestDataBinder(target, objectName);
//	}

	protected HttpMessageConverter[] getMessageConverters() {
		return messageConverters;
	}

	// /**
	// * Create a new binder instance for the given command and request.
	// * <p>Called by <code>bind</code>. Can be overridden to plug in custom
	// * ServletRequestDataBinder subclasses.
	// * <p>The default implementation creates a standard
	// ServletRequestDataBinder,
	// * and invokes <code>initBinder</code>. Note that <code>initBinder</code>
	// * will not be invoked if you override this method!
	// * @param request current HTTP request
	// * @param command the command to bind onto
	// * @return the new binder instance
	// * @throws Exception in case of invalid state or arguments
	// * @see #bind
	// * @see #initBinder
	// */
	// protected ServletRequestDataBinder createBinder(HttpServletRequest
	// request, Object command) throws Exception {
	// ServletRequestDataBinder binder = new ServletRequestDataBinder(command,
	// getCommandName(command));
	// initBinder(request, binder);
	// return binder;
	// }

	// /**
	// * Return the command name to use for the given command object.
	// * <p>Default is "command".
	// * @param command the command object
	// * @return the command name to use
	// * @see #DEFAULT_COMMAND_NAME
	// */
	// protected String getCommandName(Object command) {
	// return DEFAULT_COMMAND_NAME;
	// }
	//
	// /** Default command name used for binding command objects: "command" */
	// public static final String DEFAULT_COMMAND_NAME = "command";
	

	
}
