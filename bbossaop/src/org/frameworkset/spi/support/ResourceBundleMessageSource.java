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

package org.frameworkset.spi.support;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.frameworkset.spi.BeanClassLoaderAware;
import org.frameworkset.util.Assert;
import org.frameworkset.util.ClassUtils;
import org.frameworkset.util.PropertiesPersister;
import org.frameworkset.util.ResourceUtils;
import org.frameworkset.util.io.DefaultResourceLoader;
import org.frameworkset.util.io.Resource;
import org.frameworkset.util.io.ResourceLoader;

import com.frameworkset.util.DefaultPropertiesPersister;
import com.frameworkset.util.SimpleStringUtil;



/**
 * <p>Title: ResourceBundleMessageSource.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-9-24 下午06:52:31
 * @author biaoping.yin
 * @version 1.0
 */
public class ResourceBundleMessageSource  extends AbstractMessageSource implements BeanClassLoaderAware {

	private String[] basenames = new String[0];

	private ClassLoader bundleClassLoader;

	private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
	private ResourceLoader resourceLoader = new DefaultResourceLoader();
	private PropertiesPersister propertiesPersister = new DefaultPropertiesPersister();
	
	public ResourceBundleMessageSource()
	{
		super();
	}
	/**
	 * Cache to hold loaded ResourceBundles.
	 * This Map is keyed with the bundle basename, which holds a Map that is
	 * keyed with the Locale and in turn holds the ResourceBundle instances.
	 * This allows for very efficient hash lookups, significantly faster
	 * than the ResourceBundle class's own cache.
	 */
	private final Map cachedResourceBundles = new HashMap();

	/**
	 * Cache to hold already generated MessageFormats.
	 * This Map is keyed with the ResourceBundle, which holds a Map that is
	 * keyed with the message code, which in turn holds a Map that is keyed
	 * with the Locale and holds the MessageFormat values. This allows for
	 * very efficient hash lookups without concatenated keys.
	 * @see #getMessageFormat
	 */
	private final Map cachedBundleMessageFormats = new HashMap();


	/**
	 * Set a single basename, following {@link java.util.ResourceBundle} conventions:
	 * essentially, a fully-qualified classpath location. If it doesn't contain a
	 * package qualifier (such as <code>org.mypackage</code>), it will be resolved
	 * from the classpath root.
	 * <p>Messages will normally be held in the "/lib" or "/classes" directory of
	 * a web application's WAR structure. They can also be held in jar files on
	 * the class path.
	 * <p>Note that ResourceBundle names are effectively classpath locations: As a
	 * consequence, the JDK's standard ResourceBundle treats dots as package separators.
	 * This means that "test.theme" is effectively equivalent to "test/theme",
	 * just like it is for programmatic <code>java.util.ResourceBundle</code> usage.
	 * @see #setBasenames
	 * @see java.util.ResourceBundle#getBundle(String)
	 */
	public void setBasename(String basename) {
		setBasenames(new String[] {basename});
	}

	/**
	 * Set an array of basenames, each following {@link java.util.ResourceBundle}
	 * conventions: essentially, a fully-qualified classpath location. If it
	 * doesn't contain a package qualifier (such as <code>org.mypackage</code>),
	 * it will be resolved from the classpath root.
	 * <p>The associated resource bundles will be checked sequentially
	 * when resolving a message code. Note that message definitions in a
	 * <i>previous</i> resource bundle will override ones in a later bundle,
	 * due to the sequential lookup.
	 * <p>Note that ResourceBundle names are effectively classpath locations: As a
	 * consequence, the JDK's standard ResourceBundle treats dots as package separators.
	 * This means that "test.theme" is effectively equivalent to "test/theme",
	 * just like it is for programmatic <code>java.util.ResourceBundle</code> usage.
	 * @see #setBasename
	 * @see java.util.ResourceBundle#getBundle(String)
	 */
	public void setBasenames(String[] basenames)  {
		if (basenames != null) {
			this.basenames = new String[basenames.length];
			for (int i = 0; i < basenames.length; i++) {
				String basename = basenames[i];
				Assert.hasText(basename, "Basename must not be empty");
				this.basenames[i] = basename.trim();
			}
		}
		else {
			this.basenames = new String[0];
		}
	}

	/**
	 * Set the ClassLoader to load resource bundles with.
	 * <p>Default is the containing BeanFactory's

	 * or the default ClassLoader determined by
	 * {@link ClassUtils#getDefaultClassLoader()}
	 * if not running within a BeanFactory.
	 */
	public void setBundleClassLoader(ClassLoader classLoader) {
		this.bundleClassLoader = classLoader;
	}

	/**
	 * Return the ClassLoader to load resource bundles with.
	 * <p>Default is the containing BeanFactory's bean ClassLoader.
	 * @see #setBundleClassLoader
	 */
	protected ClassLoader getBundleClassLoader() {
		return (this.bundleClassLoader != null ? this.bundleClassLoader : this.beanClassLoader);
	}

	public void setBeanClassLoader(ClassLoader classLoader) {
		this.beanClassLoader = (classLoader != null ? classLoader : ClassUtils.getDefaultClassLoader());
	}


	/**
	 * Resolves the given message code as key in the registered resource bundles,
	 * returning the value found in the bundle as-is (without MessageFormat parsing).
	 */
	protected String resolveCodeWithoutArguments(String code, Locale locale) {
		String result = null;
		for (int i = 0; result == null && i < this.basenames.length; i++) {
			ResourceBundle bundle = getResourceBundle(this.basenames[i], locale);
			if (bundle != null) {
				result = getStringOrNull(bundle, code);
			}
		}
		return result;
	}

	/**
	 * Resolves the given message code as key in the registered resource bundles,
	 * using a cached MessageFormat instance per message code.
	 */
	protected MessageFormat resolveCode(String code, Locale locale) {
		MessageFormat messageFormat = null;
		for (int i = 0; messageFormat == null && i < this.basenames.length; i++) {
			ResourceBundle bundle = getResourceBundle(this.basenames[i], locale);
			if (bundle != null) {
				messageFormat = getMessageFormat(bundle, code, locale);
			}
		}
		return messageFormat;
	}


	/**
	 * Return a ResourceBundle for the given basename and code,
	 * fetching already generated MessageFormats from the cache.
	 * @param basename the basename of the ResourceBundle
	 * @param locale the Locale to find the ResourceBundle for
	 * @return the resulting ResourceBundle, or <code>null</code> if none
	 * found for the given basename and Locale
	 */
	protected ResourceBundle getResourceBundle(String basename, Locale locale) {
		synchronized (this.cachedResourceBundles) {
			Map localeMap = (Map) this.cachedResourceBundles.get(basename);
			if (localeMap != null) {
				ResourceBundle bundle = (ResourceBundle) localeMap.get(locale);
				if (bundle != null) {
					return bundle;
				}
			}
			try {
				ResourceBundle bundle = doGetBundle(basename, locale);
				if (localeMap == null) {
					localeMap = new HashMap();
					this.cachedResourceBundles.put(basename, localeMap);
				}
				localeMap.put(locale, bundle);
				return bundle;
			}
			catch (MissingResourceException ex) {
				{
					logger.warn("ResourceBundle [" + basename + "] not found for MessageSource: " + ex.getMessage());
				}
				
//				// Assume bundle not found
//				// -> do NOT throw the exception to allow for checking parent message source.
//				return null;
				ResourceBundle bundle = doGetDefaultBundle(basename);
				if (localeMap == null) {
					localeMap = new HashMap();
					this.cachedResourceBundles.put(basename, localeMap);
				}
				localeMap.put(locale, bundle);
				return bundle;
			}
		}
	}

	/**
	 * Obtain the resource bundle for the given basename and Locale.
	 * @param basename the basename to look for
	 * @param locale the Locale to look for
	 * @return the corresponding ResourceBundle
	 * @throws MissingResourceException if no matching bundle could be found
	 * @see java.util.ResourceBundle#getBundle(String, java.util.Locale, ClassLoader)
	 * @see #getBundleClassLoader()
	 */
	protected ResourceBundle doGetBundle(String basename, Locale locale) throws MissingResourceException {
		return ResourceBundle.getBundle(basename, locale, getBundleClassLoader());
	}
	private static final String PROPERTIES_SUFFIX = ".properties";
	private static final String XML_SUFFIX = ".xml";
	
	/**
	 * Refresh the PropertiesHolder for the given bundle filename.
	 * The holder can be <code>null</code> if not cached before, or a timed-out cache entry
	 * (potentially getting re-validated against the current last-modified timestamp).
	 * @param filename the bundle filename (basename + Locale)
	 * @param propHolder the current PropertiesHolder for the bundle
	 * 
	 */
	protected ResourceBundle doGetDefaultBundle(String filename) {
		
		Resource resource = this.resourceLoader.getResource(filename + PROPERTIES_SUFFIX);
		if (!resource.exists()) {
			if(!filename.startsWith(ResourceUtils.CLASSPATH_URL_PREFIX))
			{
				resource = this.resourceLoader.getResource(ResourceUtils.CLASSPATH_URL_PREFIX+ filename + PROPERTIES_SUFFIX);
			}
			
		}
		if (!resource.exists()) {
			resource = this.resourceLoader.getResource(filename + XML_SUFFIX);
			
			
		}
		
		if (!resource.exists()) {
			
			if(!filename.startsWith(ResourceUtils.CLASSPATH_URL_PREFIX))
			{
				resource = this.resourceLoader.getResource(ResourceUtils.CLASSPATH_URL_PREFIX+ filename + XML_SUFFIX);
			}
			
		}
		ResourceBundle propHolder = null;
		if (resource.exists()) {
			
			try {
				propHolder = loadProperties(resource, filename);
				
			}
			catch (IOException ex) {
				 {
					logger.warn("Could not parse properties file [" + resource.getFilename() + "]", ex);
				}
				// Empty holder representing "not valid".
//				propHolder = new PropertyResourceBundle(null);
			}
		}

		else {
			// Resource does not exist.
			if (logger.isDebugEnabled()) {
				logger.debug("No properties file found for [" + filename + "] - neither plain properties nor XML");
			}
			// Empty holder representing "not found".
//			propHolder = new PropertiesHolder();
		}

		
		
		return propHolder;
	}
	
	/**
	 * Load the properties from the given resource.
	 * @param resource the resource to load from
	 * @param filename the original bundle filename (basename + Locale)
	 * @return the populated Properties instance
	 * @throws IOException if properties loading failed
	 */
	protected ResourceBundle loadProperties(Resource resource, String filename) throws IOException {
		InputStream is = resource.getInputStream();
		ResourceBundle ret = new PropertyResourceBundle(is); 
		return ret;
//		try {
//			if (resource.getFilename().endsWith(XML_SUFFIX)) {
//				if (logger.isDebugEnabled()) {
//					logger.debug("Loading properties [" + resource.getFilename() + "]");
//				}
//				this.propertiesPersister.loadFromXml(props, is);
//			}
//			else {
//				String encoding = null;
//				if (this.fileEncodings != null) {
//					encoding = this.fileEncodings.getProperty(filename);
//				}
//				if (encoding == null) {
//					encoding = this.defaultEncoding;
//				}
//				if (encoding != null) {
//					if (logger.isDebugEnabled()) {
//						logger.debug("Loading properties [" + resource.getFilename() + "] with encoding '" + encoding + "'");
//					}
//					this.propertiesPersister.load(props, new InputStreamReader(is, encoding));
//				}
//				else {
//					if (logger.isDebugEnabled()) {
//						logger.debug("Loading properties [" + resource.getFilename() + "]");
//					}
//					this.propertiesPersister.load(props, is);
//				}
//			}
//			return props;
//		}
//		finally {
//			is.close();
//		}
	}

	/**
	 * Return a MessageFormat for the given bundle and code,
	 * fetching already generated MessageFormats from the cache.
	 * @param bundle the ResourceBundle to work on
	 * @param code the message code to retrieve
	 * @param locale the Locale to use to build the MessageFormat
	 * @return the resulting MessageFormat, or <code>null</code> if no message
	 * defined for the given code
	 * @throws MissingResourceException if thrown by the ResourceBundle
	 */
	protected MessageFormat getMessageFormat(ResourceBundle bundle, String code, Locale locale)
			throws MissingResourceException {

		synchronized (this.cachedBundleMessageFormats) {
			Map codeMap = (Map) this.cachedBundleMessageFormats.get(bundle);
			Map localeMap = null;
			if (codeMap != null) {
				localeMap = (Map) codeMap.get(code);
				if (localeMap != null) {
					MessageFormat result = (MessageFormat) localeMap.get(locale);
					if (result != null) {
						return result;
					}
				}
			}

			String msg = getStringOrNull(bundle, code);
			if (msg != null) {
				if (codeMap == null) {
					codeMap = new HashMap();
					this.cachedBundleMessageFormats.put(bundle, codeMap);
				}
				if (localeMap == null) {
					localeMap = new HashMap();
					codeMap.put(code, localeMap);
				}
				MessageFormat result = createMessageFormat(msg, locale);
				localeMap.put(locale, result);
				return result;
			}

			return null;
		}
	}

	private String getStringOrNull(ResourceBundle bundle, String key) {
		try {
			return bundle.getString(key);
		}
		catch (MissingResourceException ex) {
			// Assume key not found
			// -> do NOT throw the exception to allow for checking parent message source.
			return null;
		}
	}


	/**
	 * Show the configuration of this MessageSource.
	 */
	public String toString() {
		return getClass().getName() + ": basenames=[" +
				SimpleStringUtil.arrayToCommaDelimitedString(this.basenames) + "]";
	}

}
