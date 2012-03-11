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
package org.frameworkset.web.servlet.mvc;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.frameworkset.http.converter.HttpMessageConverter;
import org.frameworkset.spi.BeanNameAware;
import org.frameworkset.spi.support.validate.Validator;
import org.frameworkset.util.AntPathMatcher;
import org.frameworkset.util.Assert;
import org.frameworkset.util.PathMatcher;
import org.frameworkset.web.servlet.DispatchServlet;
import org.frameworkset.web.servlet.ModelAndView;
import org.frameworkset.web.servlet.handler.HandlerMeta;
import org.frameworkset.web.servlet.handler.HandlerUtils;
import org.frameworkset.web.servlet.handler.HandlerUtils.ServletHandlerMethodResolver;
import org.frameworkset.web.servlet.mvc.mutiaction.AbstractUrlMethodNameResolver;
import org.frameworkset.web.servlet.mvc.mutiaction.InternalPathMethodNameResolver;

/**
 * <p>Title: MultiActionController.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-10-12
 * @author biaoping.yin
 * @version 1.0
 */
public class MultiActionController  extends AbstractController implements LastModified,BeanNameAware  {
	
	private ServletHandlerMethodResolver handlerMethodResolver =
		null;
	
	private Object locker = new Object();

	/** Suffix for last-modified methods */
	public static final String LAST_MODIFIED_METHOD_SUFFIX = "LastModified";

	/** Default command name used for binding command objects: "command" */
	public static final String DEFAULT_COMMAND_NAME = "command";
	private PathMatcher pathMatcher = new AntPathMatcher();


	/** HandlerMeta we'll invoke methods on. Defaults to this. */
	private HandlerMeta delegate;

	/** Delegate that knows how to determine method names from incoming requests */
	private MethodNameResolver methodNameResolver = new InternalPathMethodNameResolver();

	/** List of Validators to apply to commands */
	private Validator[] validators;

//	/** Optional strategy for pre-initializing data binding */
//	private WebBindingInitializer webBindingInitializer;

//	/** Handler methods, keyed by name */
//	private final Map<String,MethodInfo> handlerMethodMap = new HashMap<String,MethodInfo>();

	/** LastModified methods, keyed by handler method name (without LAST_MODIFIED_SUFFIX) */
	private final Map lastModifiedMethodMap = new HashMap();

	/** Methods, keyed by exception class */
	private final Map exceptionHandlerMap = new HashMap();


	/**
	 * Constructor for <code>MultiActionController</code> that looks for
	 * handler methods in the present subclass.
	 */
	public MultiActionController() {
//		this.delegate = this;
//		registerHandlerMethods(this.delegate);
		// We'll accept no handler methods found here - a delegate might be set later on.
	}

	/**
	 * Constructor for <code>MultiActionController</code> that looks for
	 * handler methods in delegate, rather than a subclass of this class.
	 * @param delegate handler object. This does not need to implement any
	 * particular interface, as everything is done using reflection.
	 */
	public MultiActionController(HandlerMeta delegate) {
		setDelegate(delegate);
	}


	/**
	 * Set the delegate used by this class; the default is <code>this</code>,
	 * assuming that handler methods have been added by a subclass.
	 * <p>This method does not get invoked once the class is configured.
	 * @param delegate an object containing handler methods
	 * @throws IllegalStateException if no handler methods are found
	 */
	public final void setDelegate(HandlerMeta delegate) {
		Assert.notNull(delegate, "Delegate must not be null");
		this.delegate = delegate;
//		registerHandlerMethods(this.delegate);
//		// There must be SOME handler methods.
//		if (this.handlerMethodMap.isEmpty()) {
//			throw new IllegalStateException("No handler methods in class [" + this.delegate.getClass() + "]");
//		}
	}

	/**
	 * Set the method name resolver that this class should use.
	 * <p>Allows parameterization of handler method mappings.
	 */
	public final void setMethodNameResolver(MethodNameResolver methodNameResolver) {
		this.methodNameResolver = methodNameResolver;
	}

	/**
	 * Return the MethodNameResolver used by this class.
	 */
	public final MethodNameResolver getMethodNameResolver() {
		return this.methodNameResolver;
	}

	/**
	 * Set the {@link Validator Validators} for this controller.
	 * <p>The <code>Validators</code> must support the specified command class.
	 */
	public final void setValidators(Validator[] validators) {
		this.validators = validators;
	}

	/**
	 * Return the Validators for this controller.
	 */
	public final Validator[] getValidators() {
		return this.validators;
	}

//	/**
//	 * Specify a WebBindingInitializer which will apply pre-configured
//	 * configuration to every DataBinder that this controller uses.
//	 * <p>Allows for factoring out the entire binder configuration
//	 * to separate objects, as an alternative to {@link #initBinder}.
//	 */
//	public final void setWebBindingInitializer(WebBindingInitializer webBindingInitializer) {
//		this.webBindingInitializer = webBindingInitializer;
//	}
//
//	/**
//	 * Return the WebBindingInitializer (if any) which will apply pre-configured
//	 * configuration to every DataBinder that this controller uses.
//	 */
//	public final WebBindingInitializer getWebBindingInitializer() {
//		return this.webBindingInitializer;
//	}


//	/**
//	 * Registers all handlers methods on the delegate object.
//	 */
//	private void registerHandlerMethods(Object delegate) {
//		this.handlerMethodMap.clear();
//		this.lastModifiedMethodMap.clear();
//		this.exceptionHandlerMap.clear();
//
//		// Look at all methods in the subclass, trying to find
//		// methods that are validators according to our criteria
//		Method[] methods = delegate.getClass().getMethods();
//		for (int i = 0; i < methods.length; i++) {
//			// We're looking for methods with given parameters.
//			Method method = methods[i];
//			if (isExceptionHandlerMethod(method)) {
//				registerExceptionHandlerMethod(method);
//			}
//			else if (HandlerUtils.isHandlerMethod(method)) {
//				registerHandlerMethod(method);
//				registerLastModifiedMethodIfExists(delegate, method);
//			}
//		}
//	}

	
	
	
	

//	/**
//	 * Is the supplied method a valid exception handler method?
//	 */
//	private boolean isExceptionHandlerMethod(Method method) {
//		return (HandlerUtils.isHandlerMethod(method) &&
//				method.getParameterTypes().length == 3 &&
//				Throwable.class.isAssignableFrom(method.getParameterTypes()[2]));
//	}

//	/**
//	 * Registers the supplied method as a request handler.
//	 */
//	private void registerHandlerMethod(Method method) {
//		if (logger.isDebugEnabled()) {
//			logger.debug("Found action method [" + method + "]");
//		}
//		MethodInfo methodInfo = new MethodInfo(method);
//		this.handlerMethodMap.put(method.getName(), methodInfo);
//	}

//	/**
//	 * Registers a last-modified handler method for the supplied handler method
//	 * if one exists.
//	 */
//	private void registerLastModifiedMethodIfExists(Object delegate, Method method) {
//		// Look for corresponding LastModified method.
//		try {
//			Method lastModifiedMethod = delegate.getClass().getMethod(
//					method.getName() + LAST_MODIFIED_METHOD_SUFFIX,
//					new Class[] {HttpServletRequest.class});
//			Class returnType = lastModifiedMethod.getReturnType();
//			if (!(long.class.equals(returnType) || Long.class.equals(returnType))) {
//				throw new IllegalStateException("last-modified method [" + lastModifiedMethod +
//						"] declares an invalid return type - needs to be 'long' or 'Long'");
//			}
//			// Put in cache, keyed by handler method name.
//			this.lastModifiedMethodMap.put(method.getName(), lastModifiedMethod);
//			if (logger.isDebugEnabled()) {
//				logger.debug("Found last-modified method for handler method [" + method + "]");
//			}
//		}
//		catch (NoSuchMethodException ex) {
//			// No last modified method. That's ok.
//		}
//	}

//	/**
//	 * Registers the supplied method as an exception handler.
//	 */
//	private void registerExceptionHandlerMethod(Method method) {
//		this.exceptionHandlerMap.put(method.getParameterTypes()[2], method);
//		if (logger.isDebugEnabled()) {
//			logger.debug("Found exception handler method [" + method + "]");
//		}
//	}


	//---------------------------------------------------------------------
	// Implementation of LastModified
	//---------------------------------------------------------------------

	/**
	 * Try to find an XXXXLastModified method, where XXXX is the name of a handler.
	 * Return -1 if there's no such handler, indicating that content must be updated.
	 * @see LastModified#getLastModified(HttpServletRequest)
	 */
	public long getLastModified(HttpServletRequest request) {
		try {
			String handlerMethodName = this.methodNameResolver.getHandlerMethodName(request);
			Method lastModifiedMethod = (Method) this.lastModifiedMethodMap.get(handlerMethodName);
			if (lastModifiedMethod != null) {
				try {
					// Invoke the last-modified method...
					Long wrappedLong = (Long) lastModifiedMethod.invoke(this.delegate, new Object[] {request});
					return (wrappedLong != null ? wrappedLong.longValue() : -1);
				}
				catch (Exception ex) {
					// We encountered an error invoking the last-modified method.
					// We can't do anything useful except log this, as we can't throw an exception.
					logger.error("Failed to invoke last-modified method", ex);
				}
			}
		}
		catch (NoSuchRequestHandlingMethodException ex) {
			// No handler method for this request. This shouldn't happen, as this
			// method shouldn't be called unless a previous invocation of this class
			// has generated content. Do nothing, that's OK: We'll return default.
		}
		return -1L;
	}


	//---------------------------------------------------------------------
	// Implementation of AbstractController
	//---------------------------------------------------------------------

//	/**
//	 * Determine a handler method and invoke it.
//	 * @see MethodNameResolver#getHandlerMethodName
//	 * @see #invokeNamedMethod
//	 * @see #handleNoSuchRequestHandlingMethod
//	 */
//	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response,PageContext pageContext)
//			throws Exception {
//		try {
//			String methodName = this.methodNameResolver.getHandlerMethodName(request);
//			return invokeNamedMethod(methodName, request, response,pageContext);
//		}
//		catch (NoSuchRequestHandlingMethodException ex) {
//			return handleNoSuchRequestHandlingMethod(ex, request, response);
//		}
//	}
	
	/**
	 * Determine a handler method and invoke it.
	 * @see MethodNameResolver#getHandlerMethodName
	 * @see #invokeNamedMethod
	 * @see #handleNoSuchRequestHandlingMethod
	 */
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response,PageContext pageContext)
			throws Exception {
//		try {
//			String methodName = this.methodNameResolver.getHandlerMethodName(request);
//			return invokeNamedMethod(methodName, request, response,pageContext);
//		}
//		catch (NoSuchRequestHandlingMethodException ex) {
//			return handleNoSuchRequestHandlingMethod(ex, request, response);
//		}
		HttpMessageConverter[] messageConverters = (HttpMessageConverter[])request.getAttribute(DispatchServlet.messageConverters_KEY);
		return HandlerUtils.invokeHandlerMethod(request, response, pageContext, this.delegate, getServletHandlerMethodResolver(request),messageConverters);
	}
	
	
	private  ServletHandlerMethodResolver getServletHandlerMethodResolver(HttpServletRequest request)
	{

		if(handlerMethodResolver == null)
		{
			synchronized(locker)
			{
				if(handlerMethodResolver == null)
					
					handlerMethodResolver = new ServletHandlerMethodResolver(this.getClass(),
									((AbstractUrlMethodNameResolver)methodNameResolver).getUrlPathHelper(),
									pathMatcher,methodNameResolver,baseurls);
			}
			
		}
		
		return handlerMethodResolver;
	}
	private String baseurls[];
	public void setBeanName(String name) {
		this.delegate = new HandlerMeta(this,getApplicationContext().getProBean(name).getMvcpaths());
		baseurls = name.split(",");		
	}
	
	
//	/**
//	 * Determine a handler method and invoke it.
//	 * @see MethodNameResolver#getHandlerMethodName
//	 * @see #invokeNamedMethod
//	 * @see #handleNoSuchRequestHandlingMethod
//	 */
//	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response,PageContext pageContext)
//			throws Exception {
//		try {
//			String methodName = this.methodNameResolver.getHandlerMethodName(request);
//			return invokeNamedMethod(methodName, request, response,pageContext);
//		}
//		catch (NoSuchRequestHandlingMethodException ex) {
//			return handleNoSuchRequestHandlingMethod(ex, request, response);
//		}
//	}

//	/**
//	 * Handle the case where no request handler method was found.
//	 * <p>The default implementation logs a warning and sends an HTTP 404 error.
//	 * Alternatively, a fallback view could be chosen, or the
//	 * NoSuchRequestHandlingMethodException could be rethrown as-is.
//	 * @param ex the NoSuchRequestHandlingMethodException to be handled
//	 * @param request current HTTP request
//	 * @param response current HTTP response
//	 * @return a ModelAndView to render, or <code>null</code> if handled directly
//	 * @throws Exception an Exception that should be thrown as result of the servlet request
//	 */
//	protected ModelAndView handleNoSuchRequestHandlingMethod(
//			NoSuchRequestHandlingMethodException ex, HttpServletRequest request, HttpServletResponse response)
//			throws Exception {
//
//		pageNotFoundLogger.warn(ex.getMessage());
//		response.sendError(HttpServletResponse.SC_NOT_FOUND);
//		return null;
//	}
	
	
	

//	/**
//	 * Invokes the named method.
//	 * <p>Uses a custom exception handler if possible; otherwise, throw an
//	 * unchecked exception; wrap a checked exception or Throwable.
//	 */
//	protected final ModelAndView invokeNamedMethod(
//			String methodName, HttpServletRequest request, HttpServletResponse response,PageContext pageContext) throws Exception {
//
//		MethodInfo methodInfo = (MethodInfo) this.handlerMethodMap.get(methodName);
//		if (methodInfo == null) {
//			throw new NoSuchRequestHandlingMethodException(methodName, getClass());
//		}
//
//		try {
////			Class[] paramTypes = method.getParameterTypes();
////			List params = new ArrayList(4);
////			params.add(request);
////			params.add(response);
////
////			if (paramTypes.length >= 3 && paramTypes[2].equals(HttpSession.class)) {
////				HttpSession session = request.getSession(false);
////				if (session == null) {
////					throw new HttpSessionRequiredException(
////							"Pre-existing session required for handler method '" + methodName + "'");
////				}
////				params.add(session);
////			}
////
////			// If last parameter isn't of HttpSession type, it's a command.
////			if (paramTypes.length >= 3 &&
////					!paramTypes[paramTypes.length - 1].equals(HttpSession.class)) {
////				Object command = newCommandObject(paramTypes[paramTypes.length - 1]);
////				params.add(command);
////				bind(request, command);
////			}
//			ModelMap model = new ModelMap();
//			Object[] params = HandlerUtils.buildMethodCallArgs(request, response,pageContext, methodInfo,model,null); 
//
//			Object returnValue = methodInfo.getMethod().invoke(this.delegate, params);
//			return massageReturnValueIfNecessary(returnValue);
//			
//		}
//		catch (InvocationTargetException ex) {
//			// The handler method threw an exception.
//			return handleException(request, response, ex.getTargetException());
//		}
//		catch (Exception ex) {
//			// The binding process threw an exception.
//			return handleException(request, response, ex);
//		}
//	}

//	/**
//	 * Processes the return value of a handler method to ensure that it either returns
//	 * <code>null</code> or an instance of {@link ModelAndView}. When returning a {@link Map},
//	 * the {@link Map} instance is wrapped in a new {@link ModelAndView} instance.
//	 */
//	private ModelAndView massageReturnValueIfNecessary(Object returnValue) {
//		if (returnValue instanceof ModelAndView) {
//			return (ModelAndView) returnValue;
//		}
//		else if (returnValue instanceof Map) {
//			return new ModelAndView().addAllObjects((Map) returnValue);
//		}
//		else if (returnValue instanceof String) {
//			return new ModelAndView((String) returnValue);
//		}
//		else {
//			// Either returned null or was 'void' return.
//			// We'll assume that the handle method already wrote the response.
//			return null;
//		}
//	}


	

//	/**
//	 * Bind request parameters onto the given command bean
//	 * @param request request from which parameters will be bound
//	 * @param command command object, that must be a JavaBean
//	 * @throws Exception in case of invalid state or arguments
//	 */
//	protected void bind(HttpServletRequest request, Object command) throws Exception {
//		logger.debug("Binding request parameters onto MultiActionController command");
//		ServletRequestDataBinder binder = createBinder(request, command);
//		binder.bind(request);
//		if (this.validators != null) {
//			for (int i = 0; i < this.validators.length; i++) {
//				if (this.validators[i].supports(command.getClass())) {
//					ValidationUtils.invokeValidator(this.validators[i], command, binder.getBindingResult());
//				}
//			}
//		}
//		binder.closeNoCatch();
//	}

//	/**
//	 * Create a new binder instance for the given command and request.
//	 * <p>Called by <code>bind</code>. Can be overridden to plug in custom
//	 * ServletRequestDataBinder subclasses.
//	 * <p>The default implementation creates a standard ServletRequestDataBinder,
//	 * and invokes <code>initBinder</code>. Note that <code>initBinder</code>
//	 * will not be invoked if you override this method!
//	 * @param request current HTTP request
//	 * @param command the command to bind onto
//	 * @return the new binder instance
//	 * @throws Exception in case of invalid state or arguments
//	 * @see #bind
//	 * @see #initBinder
//	 */
//	protected ServletRequestDataBinder createBinder(HttpServletRequest request, Object command) throws Exception {
//		ServletRequestDataBinder binder = new ServletRequestDataBinder(command, getCommandName(command));
//		initBinder(request, binder);
//		return binder;
//	}

//	/**
//	 * Return the command name to use for the given command object.
//	 * <p>Default is "command".
//	 * @param command the command object
//	 * @return the command name to use
//	 * @see #DEFAULT_COMMAND_NAME
//	 */
//	protected String getCommandName(Object command) {
//		return DEFAULT_COMMAND_NAME;
//	}

//	/**
//	 * Initialize the given binder instance, for example with custom editors.
//	 * Called by <code>createBinder</code>.
//	 * <p>This method allows you to register custom editors for certain fields of your
//	 * command class. For instance, you will be able to transform Date objects into a
//	 * String pattern and back, in order to allow your JavaBeans to have Date properties
//	 * and still be able to set and display them in an HTML interface.
//	 * <p>The default implementation is empty.
//	 * <p>Note: the command object is not directly passed to this method, but it's available

//	 * @param request current HTTP request
//	 * @param binder new binder instance
//	 * @throws Exception in case of invalid state or arguments
//	 * @see #createBinder

//	 */
//	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
//		if (this.webBindingInitializer != null) {
//			this.webBindingInitializer.initBinder(binder, new ServletWebRequest(request));
//		}
//		
//	}

	


//	/**
//	 * Determine the exception handler method for the given exception.
//	 * <p>Can return <code>null</code> if not found.
//	 * @return a handler for the given exception type, or <code>null</code>
//	 * @param exception the exception to handle
//	 */
//	protected Method getExceptionHandler(Throwable exception) {
//		Class exceptionClass = exception.getClass();
//		if (logger.isDebugEnabled()) {
//			logger.debug("Trying to find handler for exception class [" + exceptionClass.getName() + "]");
//		}
//		Method handler = (Method) this.exceptionHandlerMap.get(exceptionClass);
//		while (handler == null && !exceptionClass.equals(Throwable.class)) {
//			if (logger.isDebugEnabled()) {
//				logger.debug("Trying to find handler for exception superclass [" + exceptionClass.getName() + "]");
//			}
//			exceptionClass = exceptionClass.getSuperclass();
//			handler = (Method) this.exceptionHandlerMap.get(exceptionClass);
//		}
//		return handler;
//	}

//	/**
//	 * We've encountered an exception thrown from a handler method.
//	 * Invoke an appropriate exception handler method, if any.
//	 * @param request current HTTP request
//	 * @param response current HTTP response
//	 * @param ex the exception that got thrown
//	 * @return a ModelAndView to render the response
//	 */
//	private ModelAndView handleException(HttpServletRequest request, HttpServletResponse response, Throwable ex)
//			throws Exception {
//
//		Method handler = getExceptionHandler(ex);
//		if (handler != null) {
//			if (logger.isDebugEnabled()) {
//				logger.debug("Invoking exception handler [" + handler + "] for exception: " + ex);
//			}
//			try {
//				Object returnValue = handler.invoke(this.delegate, new Object[] {request, response, ex});
//				return massageReturnValueIfNecessary(returnValue);
//			}
//			catch (InvocationTargetException ex2) {
//				logger.error("Original exception overridden by exception handling failure", ex);
//				ReflectionUtils.rethrowException(ex2.getTargetException());
//			}
//			catch (Exception ex2) {
//				logger.error("Failed to invoke exception handler method", ex2);
//			}
//		}
//		else {
//			// If we get here, there was no custom handler or we couldn't invoke it.
//			ReflectionUtils.rethrowException(ex);
//		}
//		throw new IllegalStateException("Should never get here");
//	}
//
}
