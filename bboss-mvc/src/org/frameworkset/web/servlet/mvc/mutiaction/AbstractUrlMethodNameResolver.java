package org.frameworkset.web.servlet.mvc.mutiaction;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.frameworkset.util.Assert;
import org.frameworkset.web.servlet.handler.AbstractUrlHandlerMapping;
import org.frameworkset.web.servlet.mvc.MethodNameResolver;
import org.frameworkset.web.servlet.mvc.NoSuchRequestHandlingMethodException;
import org.frameworkset.web.util.UrlPathHelper;

public abstract class AbstractUrlMethodNameResolver  implements MethodNameResolver {

	protected final static Logger logger = Logger.getLogger(AbstractUrlMethodNameResolver.class);

	private UrlPathHelper urlPathHelper = new UrlPathHelper();


	/**
	 * Set if URL lookup should always use full path within current servlet
	 * context. Else, the path within the current servlet mapping is used
	 * if applicable (i.e. in the case of a ".../*" servlet mapping in web.xml).
	 * Default is "false".
	 * @see org.frameworkset.web.util.UrlPathHelper#setAlwaysUseFullPath
	 */
	public void setAlwaysUseFullPath(boolean alwaysUseFullPath) {
		this.urlPathHelper.setAlwaysUseFullPath(alwaysUseFullPath);
	}

	/**
	 * Set if context path and request URI should be URL-decoded.
	 * Both are returned <i>undecoded</i> by the Servlet API,
	 * in contrast to the servlet path.
	 * <p>Uses either the request encoding or the default encoding according
	 * to the Servlet spec (ISO-8859-1).
	 * @see org.frameworkset.web.util.UrlPathHelper#setUrlDecode
	 */
	public void setUrlDecode(boolean urlDecode) {
		this.urlPathHelper.setUrlDecode(urlDecode);
	}

	/**
	 * Set the UrlPathHelper to use for resolution of lookup paths.
	 * <p>Use this to override the default UrlPathHelper with a custom subclass,
	 * or to share common UrlPathHelper settings across multiple MethodNameResolvers
	 * and HandlerMappings.
	 * @see AbstractUrlHandlerMapping#setUrlPathHelper
	 */
	public void setUrlPathHelper(UrlPathHelper urlPathHelper) {
		Assert.notNull(urlPathHelper, "UrlPathHelper must not be null");
		this.urlPathHelper = urlPathHelper;
	}


	/**
	 * Retrieves the URL path to use for lookup and delegates to
	 * <code>getHandlerMethodNameForUrlPath</code>.
	 * Converts <code>null</code> values to NoSuchRequestHandlingMethodExceptions.
	 * @see #getHandlerMethodNameForUrlPath
	 */
	public final String getHandlerMethodName(HttpServletRequest request)
			throws NoSuchRequestHandlingMethodException {

		String urlPath = this.urlPathHelper.getLookupPathForRequest(request);
		String name = getHandlerMethodNameForUrlPath(urlPath);
		if (name == null) {
			throw new NoSuchRequestHandlingMethodException(urlPath, request.getMethod(), request.getParameterMap());
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Returning handler method name '" + name + "' for lookup path: " + urlPath);
		}
		return name;
	}

	/**
	 * Return a method name that can handle this request, based on the
	 * given lookup path. Called by <code>getHandlerMethodName</code>.
	 * @param urlPath the URL path to use for lookup,
	 * according to the settings in this class
	 * @return a method name that can handle this request.
	 * Should return null if no matching method found.
	 * @see #getHandlerMethodName
	 * @see #setAlwaysUseFullPath
	 * @see #setUrlDecode
	 */
	protected abstract String getHandlerMethodNameForUrlPath(String urlPath);

	public UrlPathHelper getUrlPathHelper() {
		return urlPathHelper;
	}

}
