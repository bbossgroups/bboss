package org.frameworkset.web.servlet.mvc.mutiaction;

import java.util.Map;

import org.frameworkset.web.util.WebUtils;

public class InternalPathMethodNameResolver  extends AbstractUrlMethodNameResolver {

	private String prefix = "";

	private String suffix = "";

	/** Request URL path String --> method name String */
	private  Map methodNameCache = new java.util.concurrent.ConcurrentHashMap();


	/**
	 * Specify a common prefix for handler method names.
	 * Will be prepended to the internal path found in the URL:
	 * e.g. internal path "baz", prefix "my" -> method name "mybaz".
	 */
	public void setPrefix(String prefix) {
		this.prefix = (prefix != null ? prefix : "");
	}

	/**
	 * Return the common prefix for handler method names.
	 */
	protected String getPrefix() {
		return this.prefix;
	}

	/**
	 * Specify a common suffix for handler method names.
	 * Will be appended to the internal path found in the URL:
	 * e.g. internal path "baz", suffix "Handler" -> method name "bazHandler".
	 */
	public void setSuffix(String suffix) {
		this.suffix = (suffix != null ? suffix : "");
	}

	/**
	 * Return the common suffix for handler method names.
	 */
	protected String getSuffix() {
		return this.suffix;
	}


	/**
	 * Extracts the method name indicated by the URL path.
	 * @see #extractHandlerMethodNameFromUrlPath
	 * @see #postProcessHandlerMethodName
	 */
	protected String getHandlerMethodNameForUrlPath(String urlPath) {
		String methodName = (String) this.methodNameCache.get(urlPath);
		if (methodName == null) {
			methodName = extractHandlerMethodNameFromUrlPath(urlPath);
			methodName = postProcessHandlerMethodName(methodName);
			this.methodNameCache.put(urlPath, methodName);
		}
		return methodName;
	}

	/**
	 * Extract the handler method name from the given request URI.
	 * Delegates to <code>WebUtils.extractViewNameFromUrlPath(String)</code>.
	 * @param uri the request URI (e.g. "/index.html")
	 * @return the extracted URI filename (e.g. "index")
	 * @see WebUtils#extractFilenameFromUrlPath
	 */
	protected String extractHandlerMethodNameFromUrlPath(String uri) {
		return WebUtils.extractFilenameFromUrlPath(uri);
	}

	/**
	 * Build the full handler method name based on the given method name
	 * as indicated by the URL path.
	 * <p>The default implementation simply applies prefix and suffix.
	 * This can be overridden, for example, to manipulate upper case
	 * / lower case, etc.
	 * @param methodName the original method name, as indicated by the URL path
	 * @return the full method name to use
	 * @see #getPrefix()
	 * @see #getSuffix()
	 */
	protected String postProcessHandlerMethodName(String methodName) {
		return getPrefix() + methodName + getSuffix();
	}

	@Override
	public void destroy() {
		if(methodNameCache != null)
		{
			methodNameCache.clear();
			methodNameCache = null;
		}
		
	}

}
