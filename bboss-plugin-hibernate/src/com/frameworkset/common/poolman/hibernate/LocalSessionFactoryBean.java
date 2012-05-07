/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.frameworkset.common.poolman.hibernate;


import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.frameworkset.spi.DefaultApplicationContext;
import org.frameworkset.util.ClassUtils;
import org.frameworkset.util.ReflectionUtils;
import org.frameworkset.util.io.ClassPathResource;
import org.frameworkset.util.io.Resource;
import org.hibernate.HibernateException;
import org.hibernate.Interceptor;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.cfg.NamingStrategy;
import org.hibernate.event.EventListeners;

import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.util.BeanUtils;
import com.frameworkset.util.SimpleStringUtil;


/**
 * {@link org.springframework.beans.factory.FactoryBean} that creates a
 * Hibernate {@link org.hibernate.SessionFactory}. This is the usual way to
 * set up a shared Hibernate SessionFactory in a Spring application context;
 * the SessionFactory can then be passed to Hibernate-based DAOs via
 * dependency injection.
 *
 * <p>Configuration settings can either be read from a Hibernate XML file,
 * specified as "configLocation", or completely via this class. A typical
 * local configuration consists of one or more "mappingResources", various
 * "hibernateProperties" (not strictly necessary), and a "dataSource" that the
 * SessionFactory should use. The latter can also be specified via Hibernate
 * properties, but "dataSource" supports any Spring-configured DataSource,
 * instead of relying on Hibernate's own connection providers.
 *
 * <p>This SessionFactory handling strategy is appropriate for most types of
 * applications, from Hibernate-only single database apps to ones that need
 * distributed transactions. Either {@link HibernateTransactionManager} or
 * {@link org.springframework.transaction.jta.JtaTransactionManager} can be
 * used for transaction demarcation, with the latter only necessary for
 * transactions which span multiple databases.
 *
 * <p>This factory bean will by default expose a transaction-aware SessionFactory
 * proxy, letting data access code work with the plain Hibernate SessionFactory
 * and its <code>getCurrentSession()</code> method, while still being able to
 * participate in current Spring-managed transactions: with any transaction
 * management strategy, either local or JTA / EJB CMT, and any transaction
 * synchronization mechanism, either Spring or JTA. Furthermore,
 * <code>getCurrentSession()</code> will also seamlessly work with
 * a request-scoped Session managed by
 * {@link org.springframework.orm.hibernate3.support.OpenSessionInViewFilter} /
 * {@link org.springframework.orm.hibernate3.support.OpenSessionInViewInterceptor}.
 *
 * <p><b>Requires Hibernate 3.2 or later; tested with 3.3, 3.5 and 3.6.</b>
 * Note that this factory will use "on_close" as default Hibernate connection
 * release mode, unless in the case of a "jtaTransactionManager" specified,
 * for the reason that this is appropriate for most Spring-based applications
 * (in particular when using Spring's HibernateTransactionManager).
 *
 * @author Juergen Hoeller
 * @since 1.2
 * @see HibernateTemplate#setSessionFactory
 * @see HibernateTransactionManager#setSessionFactory
 * @see #setExposeTransactionAwareSessionFactory
 * @see #setJtaTransactionManager
 * @see org.hibernate.SessionFactory#getCurrentSession()
 * @see HibernateTransactionManager
 */
public class LocalSessionFactoryBean extends AbstractSessionFactoryBean {

	
	


	private static final ThreadLocal<DataSource> configTimeDataSourceHolder =
			new ThreadLocal<DataSource>();

	private Class<? extends Configuration> configurationClass = Configuration.class;

	private Resource[] configLocations;

	private String[] mappingResources;

	private Resource[] mappingLocations;

	private Resource[] cacheableMappingLocations;

	private Resource[] mappingJarLocations;

	private Resource[] mappingDirectoryLocations;

	private Map hibernateProperties;

	

//	private Object cacheRegionFactory;

//	private CacheProvider cacheProvider;

	

	private Interceptor entityInterceptor;

	private NamingStrategy namingStrategy;





	private Properties entityCacheStrategies;

	private Properties collectionCacheStrategies;

	private Map<String, Object> eventListeners;



	private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();

	private Configuration configuration;

	/**
	 * Return the DataSource for the currently configured Hibernate SessionFactory,
	 * to be used by LocalDataSourceConnectionProvoder.
	 * <p>This instance will be set before initialization of the corresponding
	 * SessionFactory, and reset immediately afterwards. It is thus only available
	 * during configuration.
	 * @see #setDataSource
	 * @see LocalDataSourceConnectionProvider
	 */
	public static DataSource getConfigTimeDataSource() {
		return configTimeDataSourceHolder.get();
	}
	/**
	 * Specify the Hibernate Configuration class to use.
	 * Default is "org.hibernate.cfg.Configuration"; any subclass of
	 * this default Hibernate Configuration class can be specified.
	 * <p>Can be set to "org.hibernate.cfg.AnnotationConfiguration" for
	 * using Hibernate3 annotation support (initially only available as
	 * alpha download separate from the main Hibernate3 distribution).
	 * <p>Annotated packages and annotated classes can be specified via the
	 * corresponding tags in "hibernate.cfg.xml" then, so this will usually
	 * be combined with a "configLocation" property that points at such a
	 * standard Hibernate configuration file.
	 * @see #setConfigLocation
	 * @see org.hibernate.cfg.Configuration
	 * @see org.hibernate.cfg.AnnotationConfiguration
	 */
	@SuppressWarnings("unchecked")
	public void setConfigurationClass(Class<?> configurationClass) {
		if (configurationClass == null || !Configuration.class.isAssignableFrom(configurationClass)) {
			throw new IllegalArgumentException(
					"'configurationClass' must be assignable to [org.hibernate.cfg.Configuration]");
		}
		this.configurationClass = (Class<? extends Configuration>) configurationClass;
	}

	/**
	 * Set the location of a single Hibernate XML config file, for example as
	 * classpath resource "classpath:hibernate.cfg.xml".
	 * <p>Note: Can be omitted when all necessary properties and mapping
	 * resources are specified locally via this bean.
	 * @see org.hibernate.cfg.Configuration#configure(java.net.URL)
	 */
	public void setConfigLocation(Resource configLocation) {
		this.configLocations = new Resource[] {configLocation};
	}

	/**
	 * Set the locations of multiple Hibernate XML config files, for example as
	 * classpath resources "classpath:hibernate.cfg.xml,classpath:extension.cfg.xml".
	 * <p>Note: Can be omitted when all necessary properties and mapping
	 * resources are specified locally via this bean.
	 * @see org.hibernate.cfg.Configuration#configure(java.net.URL)
	 */
	public void setConfigLocations(Resource[] configLocations) {
		this.configLocations = configLocations;
	}

	/**
	 * Set Hibernate mapping resources to be found in the class path,
	 * like "example.hbm.xml" or "mypackage/example.hbm.xml".
	 * Analogous to mapping entries in a Hibernate XML config file.
	 * Alternative to the more generic setMappingLocations method.
	 * <p>Can be used to add to mappings from a Hibernate XML config file,
	 * or to specify all mappings locally.
	 * @see #setMappingLocations
	 * @see org.hibernate.cfg.Configuration#addResource
	 */
	public void setMappingResources(String[] mappingResources) {
		this.mappingResources = mappingResources;
	}

	/**
	 * Set locations of Hibernate mapping files, for example as classpath
	 * resource "classpath:example.hbm.xml". Supports any resource location
	 * via Spring's resource abstraction, for example relative paths like
	 * "WEB-INF/mappings/example.hbm.xml" when running in an application context.
	 * <p>Can be used to add to mappings from a Hibernate XML config file,
	 * or to specify all mappings locally.
	 * @see org.hibernate.cfg.Configuration#addInputStream
	 */
	public void setMappingLocations(Resource[] mappingLocations) {
		this.mappingLocations = mappingLocations;
	}

	/**
	 * Set locations of cacheable Hibernate mapping files, for example as web app
	 * resource "/WEB-INF/mapping/example.hbm.xml". Supports any resource location
	 * via Spring's resource abstraction, as long as the resource can be resolved
	 * in the file system.
	 * <p>Can be used to add to mappings from a Hibernate XML config file,
	 * or to specify all mappings locally.
	 * @see org.hibernate.cfg.Configuration#addCacheableFile(java.io.File)
	 */
	public void setCacheableMappingLocations(Resource[] cacheableMappingLocations) {
		this.cacheableMappingLocations = cacheableMappingLocations;
	}

	/**
	 * Set locations of jar files that contain Hibernate mapping resources,
	 * like "WEB-INF/lib/example.hbm.jar".
	 * <p>Can be used to add to mappings from a Hibernate XML config file,
	 * or to specify all mappings locally.
	 * @see org.hibernate.cfg.Configuration#addJar(java.io.File)
	 */
	public void setMappingJarLocations(Resource[] mappingJarLocations) {
		this.mappingJarLocations = mappingJarLocations;
	}

	/**
	 * Set locations of directories that contain Hibernate mapping resources,
	 * like "WEB-INF/mappings".
	 * <p>Can be used to add to mappings from a Hibernate XML config file,
	 * or to specify all mappings locally.
	 * @see org.hibernate.cfg.Configuration#addDirectory(java.io.File)
	 */
	public void setMappingDirectoryLocations(Resource[] mappingDirectoryLocations) {
		this.mappingDirectoryLocations = mappingDirectoryLocations;
	}

	/**
	 * Set Hibernate properties, such as "hibernate.dialect".
	 * <p>Can be used to override values in a Hibernate XML config file,
	 * or to specify all necessary properties locally.
	 * <p>Note: Do not specify a transaction provider here when using
	 * Spring-driven transactions. It is also advisable to omit connection
	 * provider settings and use a Spring-set DataSource instead.
	 * @see #setDataSource
	 */
	public void setHibernateProperties(Map hibernateProperties) {
		this.hibernateProperties = hibernateProperties;
	}

	/**
	 * Return the Hibernate properties, if any. Mainly available for
	 * configuration through property paths that specify individual keys.
	 */
	public Map getHibernateProperties() {
		if (this.hibernateProperties == null) {
			this.hibernateProperties = new Properties();
		}
		Properties temp = new Properties();
		temp.putAll(this.hibernateProperties);
		return temp;
	}

	


	

	/**
	 * Set a Hibernate entity interceptor that allows to inspect and change
	 * property values before writing to and reading from the database.
	 * Will get applied to any new Session created by this factory.
	 * <p>Such an interceptor can either be set at the SessionFactory level, i.e. on
	 * LocalSessionFactoryBean, or at the Session level, i.e. on HibernateTemplate,
	 * HibernateInterceptor, and HibernateTransactionManager. It's preferable to set
	 * it on LocalSessionFactoryBean or HibernateTransactionManager to avoid repeated
	 * configuration and guarantee consistent behavior in transactions.
	 * @see HibernateTemplate#setEntityInterceptor
	 * @see HibernateInterceptor#setEntityInterceptor
	 * @see HibernateTransactionManager#setEntityInterceptor
	 * @see org.hibernate.cfg.Configuration#setInterceptor
	 */
	public void setEntityInterceptor(Interceptor entityInterceptor) {
		this.entityInterceptor = entityInterceptor;
	}

	/**
	 * Set a Hibernate NamingStrategy for the SessionFactory, determining the
	 * physical column and table names given the info in the mapping document.
	 * @see org.hibernate.cfg.Configuration#setNamingStrategy
	 */
	public void setNamingStrategy(NamingStrategy namingStrategy) {
		this.namingStrategy = namingStrategy;
	}

	
	
	/**
	 * Specify the cache strategies for entities (persistent classes or named entities).
	 * This configuration setting corresponds to the &lt;class-cache&gt; entry
	 * in the "hibernate.cfg.xml" configuration format.
	 * <p>For example:
	 * <pre>
	 * &lt;property name="entityCacheStrategies"&gt;
	 *   &lt;props&gt;
	 *     &lt;prop key="com.mycompany.Customer"&gt;read-write&lt;/prop&gt;
	 *     &lt;prop key="com.mycompany.Product"&gt;read-only,myRegion&lt;/prop&gt;
	 *   &lt;/props&gt;
	 * &lt;/property&gt;</pre>
	 * @param entityCacheStrategies properties that define entity cache strategies,
	 * with class names as keys and cache concurrency strategies as values
	 * @see org.hibernate.cfg.Configuration#setCacheConcurrencyStrategy(String, String)
	 */
	public void setEntityCacheStrategies(Properties entityCacheStrategies) {
		this.entityCacheStrategies = entityCacheStrategies;
	}

	/**
	 * Specify the cache strategies for persistent collections (with specific roles).
	 * This configuration setting corresponds to the &lt;collection-cache&gt; entry
	 * in the "hibernate.cfg.xml" configuration format.
	 * <p>For example:
	 * <pre>
	 * &lt;property name="collectionCacheStrategies"&gt;
	 *   &lt;props&gt;
	 *     &lt;prop key="com.mycompany.Order.items">read-write&lt;/prop&gt;
	 *     &lt;prop key="com.mycompany.Product.categories"&gt;read-only,myRegion&lt;/prop&gt;
	 *   &lt;/props&gt;
	 * &lt;/property&gt;</pre>
	 * @param collectionCacheStrategies properties that define collection cache strategies,
	 * with collection roles as keys and cache concurrency strategies as values
	 * @see org.hibernate.cfg.Configuration#setCollectionCacheConcurrencyStrategy(String, String)
	 */
	public void setCollectionCacheStrategies(Properties collectionCacheStrategies) {
		this.collectionCacheStrategies = collectionCacheStrategies;
	}

	/**
	 * Specify the Hibernate event listeners to register, with listener types
	 * as keys and listener objects as values. Instead of a single listener object,
	 * you can also pass in a list or set of listeners objects as value.
	 * <p>See the Hibernate documentation for further details on listener types
	 * and associated listener interfaces.
	 * @param eventListeners Map with listener type Strings as keys and
	 * listener objects as values
	 * @see org.hibernate.cfg.Configuration#setListener(String, Object)
	 */
	public void setEventListeners(Map<String, Object> eventListeners) {
		this.eventListeners = eventListeners;
	}


	public void setBeanClassLoader(ClassLoader beanClassLoader) {
		this.beanClassLoader = beanClassLoader;
	}


	@Override
	@SuppressWarnings("unchecked")
	protected SessionFactory buildSessionFactory() throws Exception {
		// Create Configuration instance.
		Configuration config = newConfiguration();

		DataSource dataSource = getDataSource();
		
		if(dataSource != null)
		{
			this.configTimeDataSourceHolder.set(dataSource);
		}
		// Analogous to Hibernate EntityManager's Ejb3Configuration:
		// Hibernate doesn't allow setting the bean ClassLoader explicitly,
		// so we need to expose it as thread context ClassLoader accordingly.
		Thread currentThread = Thread.currentThread();
		ClassLoader threadContextClassLoader = currentThread.getContextClassLoader();
		boolean overrideClassLoader =
				(this.beanClassLoader != null && !this.beanClassLoader.equals(threadContextClassLoader));
		try
		{
			if (overrideClassLoader) {
				currentThread.setContextClassLoader(this.beanClassLoader);
			}

		

			if (this.entityInterceptor != null) {
				// Set given entity interceptor at SessionFactory level.
				config.setInterceptor(this.entityInterceptor);
			}

			if (this.namingStrategy != null) {
				// Pass given naming strategy to Hibernate Configuration.
				config.setNamingStrategy(this.namingStrategy);
			}

			


			if (this.configLocations != null) {
				for (Resource resource : this.configLocations) {
					// Load Hibernate configuration from given location.
					config.configure(resource.getURL());
				}
			}

			if (this.hibernateProperties != null) {
				// Add given Hibernate properties to Configuration.
				Properties temp = new Properties();
				temp.putAll(this.hibernateProperties);
				config.addProperties(temp);
			}

			

			if (dataSource != null) {
				Class providerClass = LocalDataSourceConnectionProvider.class;
				
				config.setProperty(Environment.CONNECTION_PROVIDER, providerClass.getName());
			}

			if (this.mappingResources != null) {
				// Register given Hibernate mapping definitions, contained in resource files.
				for (String mapping : this.mappingResources) {
					Resource resource = new ClassPathResource(mapping.trim(), this.beanClassLoader);
					config.addInputStream(resource.getInputStream());
				}
			}

			if (this.mappingLocations != null) {
				// Register given Hibernate mapping definitions, contained in resource files.
				for (Resource resource : this.mappingLocations) {
					config.addInputStream(resource.getInputStream());
				}
			}

			if (this.cacheableMappingLocations != null) {
				// Register given cacheable Hibernate mapping definitions, read from the file system.
				for (Resource resource : this.cacheableMappingLocations) {
					config.addCacheableFile(resource.getFile());
				}
			}

			if (this.mappingJarLocations != null) {
				// Register given Hibernate mapping definitions, contained in jar files.
				for (Resource resource : this.mappingJarLocations) {
					config.addJar(resource.getFile());
				}
			}

			if (this.mappingDirectoryLocations != null) {
				// Register all Hibernate mapping definitions in the given directories.
				for (Resource resource : this.mappingDirectoryLocations) {
					File file = resource.getFile();
					if (!file.isDirectory()) {
						throw new IllegalArgumentException(
								"Mapping directory location [" + resource + "] does not denote a directory");
					}
					config.addDirectory(file);
				}
			}

			// Tell Hibernate to eagerly compile the mappings that we registered,
			// for availability of the mapping information in further processing.
			postProcessMappings(config);
			config.buildMappings();

			if (this.entityCacheStrategies != null) {
				// Register cache strategies for mapped entities.
				for (Enumeration classNames = this.entityCacheStrategies.propertyNames(); classNames.hasMoreElements();) {
					String className = (String) classNames.nextElement();
					String[] strategyAndRegion =
							SimpleStringUtil.commaDelimitedListToStringArray(this.entityCacheStrategies.getProperty(className));
					if (strategyAndRegion.length > 1) {
						// method signature declares return type as Configuration on Hibernate 3.6
						// but as void on Hibernate 3.3 and 3.5
						Method setCacheConcurrencyStrategy = Configuration.class.getMethod(
								"setCacheConcurrencyStrategy", String.class, String.class, String.class);
						ReflectionUtils.invokeMethod(setCacheConcurrencyStrategy, config,
								new Object[]{className, strategyAndRegion[0], strategyAndRegion[1]});
					}
					else if (strategyAndRegion.length > 0) {
						config.setCacheConcurrencyStrategy(className, strategyAndRegion[0]);
					}
				}
			}

			if (this.collectionCacheStrategies != null) {
				// Register cache strategies for mapped collections.
				for (Enumeration collRoles = this.collectionCacheStrategies.propertyNames(); collRoles.hasMoreElements();) {
					String collRole = (String) collRoles.nextElement();
					String[] strategyAndRegion =
							SimpleStringUtil.commaDelimitedListToStringArray(this.collectionCacheStrategies.getProperty(collRole));
					if (strategyAndRegion.length > 1) {
						config.setCollectionCacheConcurrencyStrategy(collRole, strategyAndRegion[0], strategyAndRegion[1]);
					}
					else if (strategyAndRegion.length > 0) {
						config.setCollectionCacheConcurrencyStrategy(collRole, strategyAndRegion[0]);
					}
				}
			}

			if (this.eventListeners != null) {
				// Register specified Hibernate event listeners.
				for (Map.Entry<String, Object> entry : this.eventListeners.entrySet()) {
					String listenerType = entry.getKey();
					Object listenerObject = entry.getValue();
					if (listenerObject instanceof Collection) {
						Collection<Object> listeners = (Collection<Object>) listenerObject;
						EventListeners listenerRegistry = config.getEventListeners();
						Object[] listenerArray =
								(Object[]) Array.newInstance(listenerRegistry.getListenerClassFor(listenerType), listeners.size());
						listenerArray = listeners.toArray(listenerArray);
						config.setListeners(listenerType, listenerArray);
					}
					else {
						config.setListener(listenerType, listenerObject);
					}
				}
			}

			// Perform custom post-processing in subclasses.
			postProcessConfiguration(config);

			// Build SessionFactory instance.
			logger.info("Building new Hibernate SessionFactory");
			this.configuration = config;
			return newSessionFactory(config);
		}

		finally {
			
			if (overrideClassLoader) {
				// Reset original thread context ClassLoader.
				currentThread.setContextClassLoader(threadContextClassLoader);
			}
			configTimeDataSourceHolder.set(null);
		}
	}

	/**
	 * Subclasses can override this method to perform custom initialization
	 * of the Configuration instance used for SessionFactory creation.
	 * The properties of this LocalSessionFactoryBean will be applied to
	 * the Configuration object that gets returned here.
	 * <p>The default implementation creates a new Configuration instance.
	 * A custom implementation could prepare the instance in a specific way,
	 * or use a custom Configuration subclass.
	 * @return the Configuration instance
	 * @throws HibernateException in case of Hibernate initialization errors
	 * @see org.hibernate.cfg.Configuration#Configuration()
	 */
	protected Configuration newConfiguration() throws HibernateException {
		return BeanUtils.instantiateClass(this.configurationClass);
	}

	/**
	 * To be implemented by subclasses that want to to register further mappings
	 * on the Configuration object after this FactoryBean registered its specified
	 * mappings.
	 * <p>Invoked <i>before</i> the <code>Configuration.buildMappings()</code> call,
	 * so that it can still extend and modify the mapping information.
	 * @param config the current Configuration object
	 * @throws HibernateException in case of Hibernate initialization errors
	 * @see org.hibernate.cfg.Configuration#buildMappings()
	 */
	protected void postProcessMappings(Configuration config) throws HibernateException {
	}

	/**
	 * To be implemented by subclasses that want to to perform custom
	 * post-processing of the Configuration object after this FactoryBean
	 * performed its default initialization.
	 * <p>Invoked <i>after</i> the <code>Configuration.buildMappings()</code> call,
	 * so that it can operate on the completed and fully parsed mapping information.
	 * @param config the current Configuration object
	 * @throws HibernateException in case of Hibernate initialization errors
	 * @see org.hibernate.cfg.Configuration#buildMappings()
	 */
	protected void postProcessConfiguration(Configuration config) throws HibernateException {
	}

	/**
	 * Subclasses can override this method to perform custom initialization
	 * of the SessionFactory instance, creating it via the given Configuration
	 * object that got prepared by this LocalSessionFactoryBean.
	 * <p>The default implementation invokes Configuration's buildSessionFactory.
	 * A custom implementation could prepare the instance in a specific way,
	 * or use a custom SessionFactoryImpl subclass.
	 * @param config Configuration prepared by this LocalSessionFactoryBean
	 * @return the SessionFactory instance
	 * @throws HibernateException in case of Hibernate initialization errors
	 * @see org.hibernate.cfg.Configuration#buildSessionFactory
	 */
	protected SessionFactory newSessionFactory(Configuration config) throws HibernateException {
		return config.buildSessionFactory();
	}

	/**
	 * Return the Configuration object used to build the SessionFactory.
	 * Allows for access to configuration metadata stored there (rarely needed).
	 * @throws IllegalStateException if the Configuration object has not been initialized yet
	 */
	public final Configuration getConfiguration() {
		if (this.configuration == null) {
			throw new IllegalStateException("Configuration not initialized yet");
		}
		return this.configuration;
	}

	

	/**
	 * Allows for schema export on shutdown.
	 */
	@Override
	public void destroy() throws HibernateException {
		DataSource dataSource = getDataSource();
		
		try {
			super.destroy();
		}
		finally {
			
		}
	}
	
	public static void main(String[] args) throws Exception
	{
		 
//		TransactionManager tm = new TransactionManager();
//		tm.begin();
//		LocalSessionFactoryBean localSessionFactoryBean = new LocalSessionFactoryBean();
//		localSessionFactoryBean.setDataSource(SQLManager.getDatasourceByDBName("bspf"));
//		SessionFactory factory = localSessionFactoryBean.buildSessionFactory();
//		Connection con = factory.openSession().connection();
//		con.createStatement();
//		tm.commit();
		
		
		DefaultApplicationContext context =  DefaultApplicationContext.getApplicationContext("hibernate.cfg.xml"); 
		SessionFactory factory = context.getTBeanObject("sessionFactory", SessionFactory.class);
		TransactionManager tm = new TransactionManager();
		tm.begin();

	
		Connection con = factory.openSession().connection();
		con.createStatement();
		tm.commit();
		System.out.println();
		
	}



}
