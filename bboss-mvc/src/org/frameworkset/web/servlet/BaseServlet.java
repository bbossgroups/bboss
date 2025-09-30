package org.frameworkset.web.servlet;

import com.frameworkset.util.StringUtil;
import org.frameworkset.http.CorsUtils;
import org.frameworkset.spi.support.LocaleContext;
import org.frameworkset.spi.support.LocaleContextHolder;
import org.frameworkset.spi.support.SimpleLocaleContext;
import org.frameworkset.util.ClassUtils;
import org.frameworkset.util.annotations.HttpMethod;
import org.frameworkset.web.servlet.context.RequestAttributes;
import org.frameworkset.web.servlet.context.RequestContextHolder;
import org.frameworkset.web.servlet.context.ServletRequestAttributes;
import org.frameworkset.web.util.UrlPathHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.jsp.JspFactory;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import java.io.IOException;
import java.security.Principal;
import java.util.Locale;

public abstract class BaseServlet extends HttpServlet{
	/** Checking for Servlet 3.0+ HttpServletResponse.getStatus() */
	private static final boolean responseGetStatusAvailable =
			ClassUtils.hasMethod(HttpServletResponse.class, "getStatus");

	/** Logger available to subclasses */
	protected static final Logger logger = LoggerFactory.getLogger(BaseServlet.class);
	protected UrlPathHelper urlPathHelper = new UrlPathHelper();
	
	/** Should we dispatch an HTTP TRACE request to {@link #doService}? */
	private boolean dispatchTraceRequest = false;
	/** Expose LocaleContext and RequestAttributes as inheritable for child threads? */
	private static boolean threadContextInheritable = false;
	/**
	 * Close the WebApplicationContext of this servlet.
	 */
	@Override
	public void destroy() {
//		getServletContext().log("Destroying bboss FrameworkServlet '" + getServletName() + "'");
//		// Only call close() on WebApplicationContext if locally managed...
//		if (this.webApplicationContext instanceof ConfigurableApplicationContext && !this.webApplicationContextInjected) {
//			((ConfigurableApplicationContext) this.webApplicationContext).close();
//		}
	}


	/**
	 * Override the parent class implementation in order to intercept PATCH requests.
	 */
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if (HttpMethod.PATCH.matches(request.getMethod())) {
			processRequest(request, response);
		}
		else {
			super.service(request, response);
		}
	}

	/**
	 * Delegate GET requests to processRequest/doService.
	 * <p>Will also be invoked by HttpServlet's default implementation of {@code doHead},
	 * with a {@code NoBodyResponse} that just captures the content length.
	 * @see #doService
	 * @see #doHead
	 */
	@Override
	protected final void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		processRequest(request, response);
	}
    

	/**
	 * Delegate POST requests to {@link #processRequest}.
	 * @see #doService
	 */
	@Override
	protected final void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {        
		processRequest(request, response);
	}

	/**
	 * Delegate PUT requests to {@link #processRequest}.
	 * @see #doService
	 */
	@Override
	protected final void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		processRequest(request, response);
	}

	/**
	 * Delegate DELETE requests to {@link #processRequest}.
	 * @see #doService
	 */
	@Override
	protected final void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		processRequest(request, response);
	}
	/** Should we dispatch an HTTP OPTIONS request to {@link #doService}? */
	private boolean dispatchOptionsRequest = false;
	/**
	 * Delegate OPTIONS requests to {@link #processRequest}, if desired.
	 * <p>Applies HttpServlet's standard OPTIONS processing otherwise,
	 * and also if there is still no 'Allow' header set after dispatching.
	 * @see #doService
	 */
	@Override
	protected void doOptions(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if (this.dispatchOptionsRequest || CorsUtils.isPreFlightRequest(request)) {
			processRequest(request, response);
			if (response.containsHeader("Allow")) {
				// Proper OPTIONS response coming from a handler - we're done.
				return;
			}
		}

		// Use response wrapper for Servlet 2.5 compatibility where
		// the getHeader() method does not exist
		super.doOptions(request, new HttpServletResponseWrapper(response) {
			@Override
			public void setHeader(String name, String value) {
				if ("Allow".equals(name)) {
					value = (StringUtil.hasLength(value) ? value + ", " : "") + HttpMethod.PATCH.name();
				}
				super.setHeader(name, value);
			}
		});
	}

	/**
	 * Delegate TRACE requests to {@link #processRequest}, if desired.
	 * <p>Applies HttpServlet's standard TRACE processing otherwise.
	 * @see #doService
	 */
	@Override
	protected void doTrace(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if (this.dispatchTraceRequest) {
			processRequest(request, response);
			if ("message/http".equals(response.getContentType())) {
				// Proper TRACE response coming from a handler - we're done.
				return;
			}
		}
		super.doTrace(request, response);
	}
	/** LocaleResolver used by this servlet */
	protected static LocaleResolver localeResolver;
	/**
	 * Build a LocaleContext for the given request, exposing the request's
	 * primary locale as current locale.
	 * <p>The default implementation uses the dispatcher's LocaleResolver
	 * to obtain the current locale, which might change during a request.
	 * @param request current HTTP request
	 * @return the corresponding LocaleContext
	 */
	protected static LocaleContext buildLocaleContext(final HttpServletRequest request) {
		Locale locale = localeResolver.resolveLocale(request);
		return new SimpleLocaleContext(locale);
	}
//	/**
//	 * Build a LocaleContext for the given request, exposing the request's
//	 * primary locale as current locale.
//	 * @param request current HTTP request
//	 * @return the corresponding LocaleContext, or {@code null} if none to bind
//	 * @see LocaleContextHolder#setLocaleContext
//	 */
//	protected LocaleContext buildLocaleContext(HttpServletRequest request) {
//		return new SimpleLocaleContext(request.getLocale());
//	}
	/**
	 * Build ServletRequestAttributes for the given request (potentially also
	 * holding a reference to the response), taking pre-bound attributes
	 * (and their type) into consideration.
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @param previousAttributes pre-bound RequestAttributes instance, if any
	 * @return the ServletRequestAttributes to bind, or {@code null} to preserve
	 * the previously bound instance (or not binding any, if none bound before)
	 * @see RequestContextHolder#setRequestAttributes
	 */
	protected ServletRequestAttributes buildRequestAttributes(
			HttpServletRequest request, HttpServletResponse response, PageContext pageContext,RequestAttributes previousAttributes) {

		if (previousAttributes == null || previousAttributes instanceof ServletRequestAttributes) {
			return new ServletRequestAttributes(request, response,pageContext);
		}
		else {
			return null;  // preserve the pre-bound RequestAttributes instance
		}
	}
	private void initContextHolders(
			HttpServletRequest request, LocaleContext localeContext, RequestAttributes requestAttributes) {

		if (localeContext != null) {
			LocaleContextHolder.setLocaleContext(localeContext, this.threadContextInheritable);
		}
		if (requestAttributes != null) {
			RequestContextHolder.setRequestAttributes(requestAttributes, this.threadContextInheritable);
		}
		if (logger.isTraceEnabled()) {
			logger.trace("Bound request context to thread: " + request);
		}
	}

	private void resetContextHolders(HttpServletRequest request,
			LocaleContext prevLocaleContext, RequestAttributes previousAttributes) {

		LocaleContextHolder.setLocaleContext(prevLocaleContext, this.threadContextInheritable);
		RequestContextHolder.setRequestAttributes(previousAttributes, this.threadContextInheritable);
		if (logger.isTraceEnabled()) {
			logger.trace("Cleared thread-bound request context: " + request);
		}
	}
	/**
	 * Process this request, publishing an event regardless of the outcome.
	 * <p>The actual event handling is performed by the abstract
	 * {@link #doService} template method.
	 */
	protected final void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Throwable failureCause = null;
//		LocaleContextHolder.setLocaleContext(buildLocaleContext(request), this.threadContextInheritable);
//		setLocaleContext(  request);

		// Expose current RequestAttributes to current thread.
//		previousRequestAttributes = RequestContextHolder.getRequestAttributes();
		PageContext pageContext = null;
		JspFactory fac= JspFactory.getDefaultFactory();

		if(fac != null)
			pageContext=fac.getPageContext(this, request,response, null, false, JspWriter.DEFAULT_BUFFER <= 0?8192:JspWriter.DEFAULT_BUFFER, true);
		else
			logger.info("JspFactory is null：JspFactory.getDefaultFactory() ");
//		requestAttributes = new ServletRequestAttributes(request, response,pageContext);
//		RequestContextHolder.setRequestAttributes(requestAttributes, this.threadContextInheritable);
		
		LocaleContext previousLocaleContext = LocaleContextHolder.getLocaleContext();
		LocaleContext localeContext = buildLocaleContext(request);

		RequestAttributes previousAttributes = RequestContextHolder.getRequestAttributes();
		ServletRequestAttributes requestAttributes = buildRequestAttributes(request, response, pageContext,previousAttributes);

//		WebAsyncManager asyncManager = WebAsyncUtils.getAsyncManager(request);
//		asyncManager.registerCallableInterceptor(BaseServlet.class.getName(), new RequestBindingInterceptor(pageContext));

		initContextHolders(request, localeContext, requestAttributes);

		try {
			doService(request, response);
		}
		catch (ServletException ex) {
			failureCause = ex;
			throw ex;
		}
		catch (IOException ex) {
			failureCause = ex;
			throw ex;
		}
		catch (Exception ex) {
			failureCause = ex;
			throw new NestedServletException("Request processing failed", ex);
		}
		catch (Throwable ex) {
			failureCause = ex;
			throw new NestedServletException("Request processing failed", ex);
		}

		finally {
//            if(!request.isAsyncStarted()) {
                resetContextHolders(request, previousLocaleContext, previousAttributes);
                if (requestAttributes != null) {
                    requestAttributes.requestCompleted();
                }


                if (failureCause != null) {
                    if (logger.isErrorEnabled()) {
                        this.logger.error("Could not complete request", failureCause);
                    }
                } else {
//				if(logger.isDebugEnabled()) {
//					if (asyncManager.isConcurrentHandlingStarted()) {
//						logger.debug("Leaving response open for concurrent processing");
//					} else {
//						this.logger.debug("Successfully completed request");
//					}
//				}
                }

                if (fac != null && pageContext != null) {
                    fac.releasePageContext(pageContext);
                }
//            }

//			publishRequestHandledEvent(request, response, startTime, failureCause);
		}
	}
	
//	private void publishRequestHandledEvent(
//			HttpServletRequest request, HttpServletResponse response, long startTime, Throwable failureCause) {
//
//		if (this.publishEvents) {
//			// Whether or not we succeeded, publish an event.
//			long processingTime = System.currentTimeMillis() - startTime;
//			int statusCode = (responseGetStatusAvailable ? response.getStatus() : -1);
//			this.webApplicationContext.publishEvent(
//					new ServletRequestHandledEvent(this,
//							request.getRequestURI(), request.getRemoteAddr(),
//							request.getMethod(), getServletConfig().getServletName(),
//							WebUtils.getSessionId(request), getUsernameForRequest(request),
//							processingTime, failureCause, statusCode));
//		}
//	}

	/**
	 * Determine the username for the given request.
	 * <p>The default implementation takes the name of the UserPrincipal, if any.
	 * Can be overridden in subclasses.
	 * @param request current HTTP request
	 * @return the username, or {@code null} if none found
	 * @see javax.servlet.http.HttpServletRequest#getUserPrincipal()
	 */
	protected String getUsernameForRequest(HttpServletRequest request) {
		Principal userPrincipal = request.getUserPrincipal();
		return (userPrincipal != null ? userPrincipal.getName() : null);
	}


	/**
	 * Subclasses must implement this method to do the work of request handling,
	 * receiving a centralized callback for GET, POST, PUT and DELETE.
	 * <p>The contract is essentially the same as that for the commonly overridden
	 * {@code doGet} or {@code doPost} methods of HttpServlet.
	 * <p>This class intercepts calls to ensure that exception handling and
	 * event publication takes place.
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @throws Exception in case of any kind of processing failure
	 * @see javax.servlet.http.HttpServlet#doGet
	 * @see javax.servlet.http.HttpServlet#doPost
	 */
	protected abstract void doService(HttpServletRequest request, HttpServletResponse response)
			throws Exception;


	

//	/**
//	 * CallableProcessingInterceptor implementation that initializes and resets
//	 * FrameworkServlet's context holders, i.e. LocaleContextHolder and RequestContextHolder.
//	 */
//	private class RequestBindingInterceptor extends CallableProcessingInterceptorAdapter {
//		private PageContext pageContext;
//		public RequestBindingInterceptor(PageContext pageContext)
//		{
//			this.pageContext =  pageContext;
//		}
//		@Override
//		public <T> void preProcess(NativeWebRequest webRequest, Callable<T> task) {
//			HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
//			if (request != null) {
//				HttpServletResponse response = webRequest.getNativeRequest(HttpServletResponse.class);
//				initContextHolders(request, buildLocaleContext(request), buildRequestAttributes(request, response,pageContext, null));
//			}
//		}
//		@Override
//		public <T> void postProcess(NativeWebRequest webRequest, Callable<T> task, Object concurrentResult) {
//			HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
//			if (request != null) {
//				resetContextHolders(request, null, null);
//			}
//		}
//	}

}
