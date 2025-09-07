package org.frameworkset.web.filter;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.frameworkset.spi.BeanNameAware;
import org.frameworkset.spi.DisposableBean;
import org.frameworkset.spi.InitializingBean;
import org.frameworkset.util.Assert;
import org.frameworkset.util.beans.BeanWrapper;
import org.frameworkset.util.beans.BeansException;
import org.frameworkset.web.servlet.context.ServletContextAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple base implementation of {@link javax.servlet.Filter} which treats
 * its config parameters ({@code init-param} entries within the
 * {@code filter} tag in {@code web.xml}) as bean properties.
 *
 * <p>A handy superclass for any type of filter. Type conversion of config
 * parameters is automatic, with the corresponding setter method getting
 * invoked with the converted value. It is also possible for subclasses to
 * specify required properties. Parameters without matching bean property
 * setter will simply be ignored.
 *
 * <p>This filter leaves actual filtering to subclasses, which have to
 * implement the {@link javax.servlet.Filter#doFilter} method.
 *
 * <p>This generic filter base class has no dependency on the bboss
 * {@link org.frameworkset.context.ApplicationContext} concept.
 * Filters usually don't load their own context but rather access service
 * beans from the bboss root application context, accessible via the
 * filter's {@link #getServletContext() ServletContext} (see
 * {@link org.frameworkset.web.context.support.WebApplicationContextUtils}).
 *
 * @author Juergen Hoeller
 * @since 06.12.2003
 * @see #addRequiredProperty
 * @see #initFilterBean
 * @see #doFilter
 */
public abstract class GenericFilterBean implements
		Filter, BeanNameAware,  ServletContextAware, InitializingBean, DisposableBean {

	/** Logger available to subclasses */
	protected static final Logger logger = LoggerFactory.getLogger(GenericFilterBean.class);


	/**
	 * Set of required properties (Strings) that must be supplied as
	 * config parameters to this filter.
	 */
	private final Set<String> requiredProperties = new HashSet<String>();

	private FilterConfig filterConfig;

	private String beanName;


	private ServletContext servletContext;


	/**
	 * Stores the bean name as defined in the bboss bean factory.
	 * <p>Only relevant in case of initialization as bean, to have a name as
	 * fallback to the filter name usually provided by a FilterConfig instance.
	 * @see org.frameworkset.beans.factory.BeanNameAware
	 * @see #getFilterName()
	 */
	@Override
	public final void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	 

	/**
	 * Stores the ServletContext that the bean factory runs in.
	 * <p>Only relevant in case of initialization as bean, to have a ServletContext
	 * as fallback to the context usually provided by a FilterConfig instance.
	 * @see org.frameworkset.web.context.ServletContextAware
	 * @see #getServletContext()
	 */
	@Override
	public final void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	/**
	 * Calls the {@code initFilterBean()} method that might
	 * contain custom initialization of a subclass.
	 * <p>Only relevant in case of initialization as bean, where the
	 * standard {@code init(FilterConfig)} method won't be called.
	 * @see #initFilterBean()
	 * @see #init(javax.servlet.FilterConfig)
	 */
	@Override
	public void afterPropertiesSet() throws ServletException {
		initFilterBean();
	}


	/**
	 * Subclasses can invoke this method to specify that this property
	 * (which must match a JavaBean property they expose) is mandatory,
	 * and must be supplied as a config parameter. This should be called
	 * from the constructor of a subclass.
	 * <p>This method is only relevant in case of traditional initialization
	 * driven by a FilterConfig instance.
	 * @param property name of the required property
	 */
	protected final void addRequiredProperty(String property) {
		this.requiredProperties.add(property);
	}

	/**
	 * Standard way of initializing this filter.
	 * Map config parameters onto bean properties of this filter, and
	 * invoke subclass initialization.
	 * @param filterConfig the configuration for this filter
	 * @throws ServletException if bean properties are invalid (or required
	 * properties are missing), or if subclass initialization fails.
	 * @see #initFilterBean
	 */
	@Override
	public final void init(FilterConfig filterConfig) throws ServletException {
		Assert.notNull(filterConfig, "FilterConfig must not be null");
		if (logger.isDebugEnabled()) {
			logger.debug("Initializing filter '" + filterConfig.getFilterName() + "'");
		}

		this.filterConfig = filterConfig;

//		// Set bean properties from init parameters.
//		try {
//			PropertyValues pvs = new FilterConfigPropertyValues(filterConfig, this.requiredProperties);
//			BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(this);
//			ResourceLoader resourceLoader = new ServletContextResourceLoader(filterConfig.getServletContext());
//			bw.registerCustomEditor(Resource.class, new ResourceEditor(resourceLoader, this.environment));
//			initBeanWrapper(bw);
//			bw.setPropertyValues(pvs, true);
//		}
//		catch (BeansException ex) {
//			String msg = "Failed to set bean properties on filter '" +
//				filterConfig.getFilterName() + "': " + ex.getMessage();
//			logger.error(msg, ex);
//			throw new NestedServletException(msg, ex);
//		}

		// Let subclasses do whatever initialization they like.
		initFilterBean();

		if (logger.isDebugEnabled()) {
			logger.debug("Filter '" + filterConfig.getFilterName() + "' configured successfully");
		}
	}

	/**
	 * Initialize the BeanWrapper for this GenericFilterBean,
	 * possibly with custom editors.
	 * <p>This default implementation is empty.
	 * @param bw the BeanWrapper to initialize
	 * @throws BeansException if thrown by BeanWrapper methods
	 * @see org.frameworkset.beans.BeanWrapper#registerCustomEditor
	 */
	protected void initBeanWrapper(BeanWrapper bw) throws BeansException {
	}


	/**
	 * Make the FilterConfig of this filter available, if any.
	 * Analogous to GenericServlet's {@code getServletConfig()}.
	 * <p>Public to resemble the {@code getFilterConfig()} method
	 * of the Servlet Filter version that shipped with WebLogic 6.1.
	 * @return the FilterConfig instance, or {@code null} if none available
	 * @see javax.servlet.GenericServlet#getServletConfig()
	 */
	public final FilterConfig getFilterConfig() {
		return this.filterConfig;
	}

	/**
	 * Make the name of this filter available to subclasses.
	 * Analogous to GenericServlet's {@code getServletName()}.
	 * <p>Takes the FilterConfig's filter name by default.
	 * If initialized as bean in a bboss application context,
	 * it falls back to the bean name as defined in the bean factory.
	 * @return the filter name, or {@code null} if none available
	 * @see javax.servlet.GenericServlet#getServletName()
	 * @see javax.servlet.FilterConfig#getFilterName()
	 * @see #setBeanName
	 */
	protected final String getFilterName() {
		return (this.filterConfig != null ? this.filterConfig.getFilterName() : this.beanName);
	}

	/**
	 * Make the ServletContext of this filter available to subclasses.
	 * Analogous to GenericServlet's {@code getServletContext()}.
	 * <p>Takes the FilterConfig's ServletContext by default.
	 * If initialized as bean in a bboss application context,
	 * it falls back to the ServletContext that the bean factory runs in.
	 * @return the ServletContext instance, or {@code null} if none available
	 * @see javax.servlet.GenericServlet#getServletContext()
	 * @see javax.servlet.FilterConfig#getServletContext()
	 * @see #setServletContext
	 */
	protected final ServletContext getServletContext() {
		return (this.filterConfig != null ? this.filterConfig.getServletContext() : this.servletContext);
	}


	/**
	 * Subclasses may override this to perform custom initialization.
	 * All bean properties of this filter will have been set before this
	 * method is invoked.
	 * <p>Note: This method will be called from standard filter initialization
	 * as well as filter bean initialization in a bboss application context.
	 * Filter name and ServletContext will be available in both cases.
	 * <p>This default implementation is empty.
	 * @throws ServletException if subclass initialization fails
	 * @see #getFilterName()
	 * @see #getServletContext()
	 */
	protected void initFilterBean() throws ServletException {
	}

	/**
	 * Subclasses may override this to perform custom filter shutdown.
	 * <p>Note: This method will be called from standard filter destruction
	 * as well as filter bean destruction in a bboss application context.
	 * <p>This default implementation is empty.
	 */
	@Override
	public void destroy() {
	}


//	/**
//	 * PropertyValues implementation created from FilterConfig init parameters.
//	 */
//	@SuppressWarnings("serial")
//	private static class FilterConfigPropertyValues extends MutablePropertyValues {
//
//		/**
//		 * Create new FilterConfigPropertyValues.
//		 * @param config FilterConfig we'll use to take PropertyValues from
//		 * @param requiredProperties set of property names we need, where
//		 * we can't accept default values
//		 * @throws ServletException if any required properties are missing
//		 */
//		public FilterConfigPropertyValues(FilterConfig config, Set<String> requiredProperties)
//			throws ServletException {
//
//			Set<String> missingProps = (requiredProperties != null && !requiredProperties.isEmpty()) ?
//					new HashSet<String>(requiredProperties) : null;
//
//			Enumeration<?> en = config.getInitParameterNames();
//			while (en.hasMoreElements()) {
//				String property = (String) en.nextElement();
//				Object value = config.getInitParameter(property);
//				addPropertyValue(new PropertyValue(property, value));
//				if (missingProps != null) {
//					missingProps.remove(property);
//				}
//			}
//
//			// Fail if we are still missing properties.
//			if (missingProps != null && missingProps.size() > 0) {
//				throw new ServletException(
//					"Initialization from FilterConfig for filter '" + config.getFilterName() +
//					"' failed; the following required properties were missing: " +
//					StringUtil.collectionToDelimitedString(missingProps, ", "));
//			}
//		}
//	}

}
