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
package org.frameworkset.web.servlet.view;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.frameworkset.spi.InitializingBean;

import com.frameworkset.util.BeanUtils;
import com.frameworkset.util.PatternMatchUtils;
import com.frameworkset.util.StringUtil;


/**
 * <p>Title: UrlBasedViewResolver.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-9-28
 * @author biaoping.yin
 * @version 1.0
 */
public abstract class UrlBasedViewResolver  extends AbstractCachingViewResolver  {

	public static boolean isPathVariable(String path)
	{
		return path != null && path.startsWith(UrlBasedViewResolver.PATH_URL_PREFIX);
	}
	
	
	/**
	 * Prefix for special view names that specify a redirect URL (usually
	 * to a controller after a form has been submitted and processed).
	 * Such view names will not be resolved in the configured default
	 * way but rather be treated as special shortcut.
	 */
	public static final String REDIRECT_URL_PREFIX = "redirect:";

	/**
	 * Prefix for special view names that specify a forward URL (usually
	 * to a controller after a form has been submitted and processed).
	 * Such view names will not be resolved in the configured default
	 * way but rather be treated as special shortcut.
	 */
	public static final String FORWARD_URL_PREFIX = "forward:";
	
	
	public static final String PATH_URL_PREFIX = "path:";


	private Class viewClass;

	private String prefix = "";
	
	private boolean needprefix = false;
	private boolean prefixchecked = false;
	private boolean needsuffix = false;
	private boolean suffixchecked = false;

	private String suffix = "";

	private String[] viewNames = null;

	private String contentType;

	private boolean redirectContextRelative = true;

	private boolean redirectHttp10Compatible = true;

	private String requestContextAttribute;

	private int order = Integer.MAX_VALUE;

	/** Map of static attributes, keyed by attribute name (String) */
	private final Map	staticAttributes = new HashMap();


	/**
	 * Set the view class that should be used to create views.
	 * @param viewClass class that is assignable to the required view class
	 * (by default, AbstractUrlBasedView)
	 * @see AbstractUrlBasedView
	 */
	public void setViewClass(Class viewClass) {
		if (viewClass == null || !requiredViewClass().isAssignableFrom(viewClass)) {
			throw new IllegalArgumentException(
					"Given view class [" + (viewClass != null ? viewClass.getName() : null) +
					"] is not of type [" + requiredViewClass().getName() + "]");
		}
		this.viewClass = viewClass;
	}

	/**
	 * Return the view class to be used to create views.
	 */
	protected Class getViewClass() {
		return this.viewClass;
	}

	/**
	 * Return the required type of view for this resolver.
	 * This implementation returns AbstractUrlBasedView.
	 * @see AbstractUrlBasedView
	 */
	protected Class requiredViewClass() {
		return AbstractUrlBasedView.class;
	}

	/**
	 * Set the prefix that gets prepended to view names when building a URL.
	 */
	public void setPrefix(String prefix) {
		this.prefix = (prefix != null ? prefix : "");
	}

	/**
	 * Return the prefix that gets prepended to view names when building a URL.
	 */
	protected String getPrefix() {
		return this.prefix;
	}

	/**
	 * Set the suffix that gets appended to view names when building a URL.
	 */
	public void setSuffix(String suffix) {
		this.suffix = (suffix != null ? suffix : "");
	}

	/**
	 * Return the suffix that gets appended to view names when building a URL.
	 */
	protected String getSuffix() {
		return this.suffix;
	}

	/**
	 * Set the content type for all views.
	 * <p>May be ignored by view classes if the view itself is assumed
	 * to set the content type, e.g. in case of JSPs.
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * Return the content type for all views, if any.
	 */
	protected String getContentType() {
		return this.contentType;
	}

	/**
	 * Set whether to interpret a given redirect URL that starts with a
	 * slash ("/") as relative to the current ServletContext, i.e. as
	 * relative to the web application root.
	 * <p>Default is "true": A redirect URL that starts with a slash will be
	 * interpreted as relative to the web application root, i.e. the context
	 * path will be prepended to the URL.
	 * <p><b>Redirect URLs can be specified via the "redirect:" prefix.</b>
	 * E.g.: "redirect:myAction.do"
	 * @see RedirectView#setContextRelative
	 * @see #REDIRECT_URL_PREFIX
	 */
	public void setRedirectContextRelative(boolean redirectContextRelative) {
		this.redirectContextRelative = redirectContextRelative;
	}

	/**
	 * Return whether to interpret a given redirect URL that starts with a
	 * slash ("/") as relative to the current ServletContext, i.e. as
	 * relative to the web application root.
	 */
	protected boolean isRedirectContextRelative() {
		return this.redirectContextRelative;
	}

	/**
	 * Set whether redirects should stay compatible with HTTP 1.0 clients.
	 * <p>In the default implementation, this will enforce HTTP status code 302
	 * in any case, i.e. delegate to <code>HttpServletResponse.sendRedirect</code>.
	 * Turning this off will send HTTP status code 303, which is the correct
	 * code for HTTP 1.1 clients, but not understood by HTTP 1.0 clients.
	 * <p>Many HTTP 1.1 clients treat 302 just like 303, not making any
	 * difference. However, some clients depend on 303 when redirecting
	 * after a POST request; turn this flag off in such a scenario.
	 * <p><b>Redirect URLs can be specified via the "redirect:" prefix.</b>
	 * E.g.: "redirect:myAction.do"
	 * @see RedirectView#setHttp10Compatible
	 * @see #REDIRECT_URL_PREFIX
	 */
	public void setRedirectHttp10Compatible(boolean redirectHttp10Compatible) {
		this.redirectHttp10Compatible = redirectHttp10Compatible;
	}

	/**
	 * Return whether redirects should stay compatible with HTTP 1.0 clients.
	 */
	protected boolean isRedirectHttp10Compatible() {
		return this.redirectHttp10Compatible;
	}

	/**
	 * Set the name of the RequestContext attribute for all views.
	 * @param requestContextAttribute name of the RequestContext attribute
	 * @see AbstractView#setRequestContextAttribute
	 */
	public void setRequestContextAttribute(String requestContextAttribute) {
		this.requestContextAttribute = requestContextAttribute;
	}

	/**
	 * Return the name of the RequestContext attribute for all views, if any.
	 */
	protected String getRequestContextAttribute() {
		return this.requestContextAttribute;
	}

	/**
	 * Set static attributes from a <code>java.util.Properties</code> object,
	 * for all views returned by this resolver.
	 * <p>This is the most convenient way to set static attributes. Note that
	 * static attributes can be overridden by dynamic attributes, if a value
	 * with the same name is included in the model.
	 * <p>Can be populated with a String "value" (parsed via PropertiesEditor)
	 * or a "props" element in XML bean definitions.

	 * @see AbstractView#setAttributes
	 */
	public void setAttributes(Properties props) {
		setAttributesMap(props);
	}

	/**
	 * Set static attributes from a Map, for all views returned by this resolver.
	 * This allows to set any kind of attribute values, for example bean references.
	 * <p>Can be populated with a "map" or "props" element in XML bean definitions.
	 * @param attributes Map with name Strings as keys and attribute objects as values
	 * @see AbstractView#setAttributesMap
	 */
	public void setAttributesMap(Map attributes) {
		if (attributes != null) {
			this.staticAttributes.putAll(attributes);
		}
	}

	/**
	 * Allow Map access to the static attributes for views returned by
	 * this resolver, with the option to add or override specific entries.
	 * <p>Useful for specifying entries directly, for example via
	 * "attributesMap[myKey]". This is particularly useful for
	 * adding or overriding entries in child view definitions.
	 */
	public Map getAttributesMap() {
		return this.staticAttributes;
	}

	/**
	 * Set the view names (or name patterns) that can be handled by this
	 * {@link ViewResolver}. View names can contain
	 * simple wildcards such that 'my*', '*Report' and '*Repo*' will all match the
	 * view name 'myReport'.
	 * @see #canHandle
	 */
	public void setViewNames(String[] viewNames) {
		this.viewNames = viewNames;
	}

	/**
	 * Return the view names (or name patterns) that can be handled by this
	 * {@link ViewResolver}.
	 */
	protected String[] getViewNames() {
		return this.viewNames;
	}

	/**
	 * Set the order in which this {@link ViewResolver}
	 * is evaluated.
	 */
	public void setOrder(int order) {
		this.order = order;
	}

	/**
	 * Return the order in which this {@link ViewResolver}
	 * is evaluated.
	 */
	public int getOrder() {
		return this.order;
	}

	protected void initApplicationContext() {
		super.initApplicationContext();
		if (getViewClass() == null) {
			throw new IllegalArgumentException("Property 'viewClass' is required");
		}
	}


	/**
	 * This implementation returns just the view name,
	 * as this ViewResolver doesn't support localized resolution.
	 */
	protected Object getCacheKey(String viewName, Locale locale) {
		return viewName;
	}

	/**
	 * Overridden to implement check for "redirect:" prefix.
	 * <p>Not possible in <code>loadView</code>, since overridden
	 * <code>loadView</code> versions in subclasses might rely on the
	 * superclass always creating instances of the required view class.
	 * @see #loadView
	 * @see #requiredViewClass
	 */
	protected View createView(String viewName, Locale locale) throws Exception {
		// If this resolver is not supposed to handle the given view,
		// return null to pass on to the next resolver in the chain.
		if (!canHandle(viewName, locale)) {
			return null;
		}
		// Check for special "redirect:" prefix.
		if (viewName.startsWith(REDIRECT_URL_PREFIX)) {
			String redirectUrl = viewName.substring(REDIRECT_URL_PREFIX.length());
			return new RedirectView(
							redirectUrl, isRedirectContextRelative(), isRedirectHttp10Compatible());
		}
		// Check for special "forward:" prefix.
		if (viewName.startsWith(FORWARD_URL_PREFIX)) {
			String forwardUrl = viewName.substring(FORWARD_URL_PREFIX.length());
			return new InternalResourceView(forwardUrl);
		}
		// Else fall back to superclass implementation: calling loadView.
		return super.createView(viewName, locale);
	}

	/**
	 * Indicates whether or not this {@link ViewResolver} can
	 * handle the supplied view name. If not, {@link #createView(String, java.util.Locale)} will
	 * return <code>null</code>. The default implementation checks against the configured
	 * {@link #setViewNames view names}.
	 * @param viewName the name of the view to retrieve
	 * @param locale the Locale to retrieve the view for
	 * @return whether this resolver applies to the specified view
	 * @see PatternMatchUtils#simpleMatch(String, String)
	 */
	protected boolean canHandle(String viewName, Locale locale) {
		String[] viewNames = getViewNames();
		return (viewNames == null || PatternMatchUtils.simpleMatch(viewNames, viewName));
	}

	/**
	 * Delegates to <code>buildView</code> for creating a new instance of the
	 * specified view class, and applies the following Bboss lifecycle methods
	 * (as supported by the generic Bboss bean factory):
	 * <ul>
	 * <li>ApplicationContextAware's <code>setApplicationContext</code>
	 * <li>InitializingBean's <code>afterPropertiesSet</code>
	 * </ul>
	 * @param viewName the name of the view to retrieve
	 * @return the View instance
	 * @throws Exception if the view couldn't be resolved
	 * @see #buildView(String)
	 * @see org.frameworkset.spi.ApplicationContextAware#setApplicationContext
	 * @see InitializingBean#afterPropertiesSet
	 */
	protected View loadView(String viewName, Locale locale) throws Exception {
		AbstractUrlBasedView view = buildView(viewName);
		return (View) getApplicationContext().initBean(view, viewName);
//		return view;
	}

	/**
	 * Creates a new View instance of the specified view class and configures it.
	 * Does <i>not</i> perform an.y lookup for pre-defined View instances.
	 * <p>Bboss lifecycle methods as defined by the bean container do not have to
	 * be called here; those will be applied by the <code>loadView</code> method
	 * after this method returns.
	 * <p>Subclasses will typically call <code>super.buildView(viewName)</code>
	 * first, before setting further properties themselves. <code>loadView</code>
	 * will then apply Bboss lifecycle methods at the end of this process.
	 * @param viewName the name of the view to build
	 * @return the View instance
	 * @throws Exception if the view couldn't be resolved
	 * @see #loadView(String, java.util.Locale)
	 */
	protected AbstractUrlBasedView buildView(String viewName) throws Exception {
		AbstractUrlBasedView view = (AbstractUrlBasedView) BeanUtils.instantiateClass(getViewClass());
		String url = StringUtil.getRealPath(getPrefix(),viewName ) + this.getSuffix();
//		String url = null;
//		if(this.needprefix)
//		{
//			url = StringUtil.getRealPath(getPrefix(),viewName );
//		}
//		else if(!this.prefixchecked)
//		{
//			if(this.getPrefix() == null || this.getPrefix().equals(""))
//				url = viewName;
//			else
//			{
//				needprefix = true;
//				url = StringUtil.getRealPath(getPrefix(),viewName );
//			}
//			this.prefixchecked = true;
//		}
//		
//		if(this.needsuffix)
//		{
//			url = url + this.getSuffix();
//		}
//		else if(!this.suffixchecked)
//		{
//			if(this.getSuffix() == null || this.getSuffix().equals(""))
//			{
//				
//			}
//			else
//			{
//				needsuffix = true;
//				url = url + this.getSuffix();
//			}
//			suffixchecked = true;
//		}
		
		view.setUrl(url);
		String contentType = getContentType();
		if (contentType != null) {
			view.setContentType(contentType);
		}
		view.setRequestContextAttribute(getRequestContextAttribute());
		view.setAttributesMap(getAttributesMap());
		return view;
	}

}
