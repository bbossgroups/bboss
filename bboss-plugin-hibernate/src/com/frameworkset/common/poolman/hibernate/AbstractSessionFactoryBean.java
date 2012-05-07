/*
 * Copyright 2002-2011 the original author or authors.
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

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.frameworkset.spi.DisposableBean;
import org.frameworkset.spi.InitializingBean;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;



/**
 * Abstract {@link org.springframework.beans.factory.FactoryBean} that creates
 * a Hibernate {@link org.hibernate.SessionFactory} within a Spring application
 * context, providing general infrastructure not related to Hibernate's
 * specific configuration API.
 *
 * <p>This class implements the
 * {@link org.springframework.dao.support.PersistenceExceptionTranslator}
 * interface, as autodetected by Spring's
 * {@link org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor},
 * for AOP-based translation of native exceptions to Spring DataAccessExceptions.
 * Hence, the presence of e.g. LocalSessionFactoryBean automatically enables
 * a PersistenceExceptionTranslationPostProcessor to translate Hibernate exceptions.
 *
 * <p>This class mainly serves as common base class for {@link LocalSessionFactoryBean}.
 * For details on typical SessionFactory setup, see the LocalSessionFactoryBean javadoc.
 *
 * @author Juergen Hoeller
 * @since 2.0
 * @see #setExposeTransactionAwareSessionFactory
 * @see org.hibernate.SessionFactory#getCurrentSession()
 * @see org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor
 */
public abstract class AbstractSessionFactoryBean implements InitializingBean,DisposableBean {

	/** Logger available to subclasses */
	protected final static Logger logger = Logger.getLogger(AbstractSessionFactoryBean.class);

	private DataSource dataSource;



	private SessionFactory sessionFactory;


	/**
	 * Set the DataSource to be used by the SessionFactory.
	 * If set, this will override corresponding settings in Hibernate properties.
	 * <p>If this is set, the Hibernate settings should not define
	 * a connection provider to avoid meaningless double configuration.
	 * <p>If using HibernateTransactionManager as transaction strategy, consider
	 * proxying your target DataSource with a LazyConnectionDataSourceProxy.
	 * This defers fetching of an actual JDBC Connection until the first JDBC
	 * Statement gets executed, even within JDBC transactions (as performed by
	 * HibernateTransactionManager). Such lazy fetching is particularly beneficial
	 * for read-only operations, in particular if the chances of resolving the
	 * result in the second-level cache are high.
	 * <p>As JTA and transactional JNDI DataSources already provide lazy enlistment
	 * of JDBC Connections, LazyConnectionDataSourceProxy does not add value with
	 * JTA (i.e. Spring's JtaTransactionManager) as transaction strategy.
	 * @see #setUseTransactionAwareDataSource
	 * @see HibernateTransactionManager
	 * @see org.springframework.transaction.jta.JtaTransactionManager
	 * @see org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * Return the DataSource to be used by the SessionFactory.
	 */
	public DataSource getDataSource() {
		return this.dataSource;
	}



	/**
	 * Build and expose the SessionFactory.
	 * @see #buildSessionFactory()
	 * @see #wrapSessionFactoryIfNecessary
	 */
	public void afterPropertiesSet() throws Exception {
		SessionFactory rawSf = buildSessionFactory();
		this.sessionFactory = wrapSessionFactoryIfNecessary(rawSf);
		afterSessionFactoryCreation();
	}

	/**
	 * Wrap the given SessionFactory with a proxy, if demanded.
	 * <p>The default implementation simply returns the given SessionFactory as-is.
	 * Subclasses may override this to implement transaction awareness through
	 * a SessionFactory proxy, for example.
	 * @param rawSf the raw SessionFactory as built by {@link #buildSessionFactory()}
	 * @return the SessionFactory reference to expose
	 * @see #buildSessionFactory()
	 */
	protected SessionFactory wrapSessionFactoryIfNecessary(SessionFactory rawSf) {
		return rawSf;
	}

	/**
	 * Return the exposed SessionFactory.
	 * Will throw an exception if not initialized yet.
	 * @return the SessionFactory (never <code>null</code>)
	 * @throws IllegalStateException if the SessionFactory has not been initialized yet
	 */
	protected final SessionFactory getSessionFactory() {
		if (this.sessionFactory == null) {
			throw new IllegalStateException("SessionFactory not initialized yet");
		}
		return this.sessionFactory;
	}

	/**
	 * Close the SessionFactory on bean factory shutdown.
	 */
	public void destroy() throws HibernateException {
		logger.info("Closing Hibernate SessionFactory");
		try {
			beforeSessionFactoryDestruction();
		}
		finally {
			this.sessionFactory.close();
		}
	}


	/**
	 * Return the singleton SessionFactory.
	 */
	public SessionFactory getObject() {
		return this.sessionFactory;
	}

	public Class<? extends SessionFactory> getObjectType() {
		return (this.sessionFactory != null ? this.sessionFactory.getClass() : SessionFactory.class);
	}

	public boolean isSingleton() {
		return true;
	}


	/**
	 * Build the underlying Hibernate SessionFactory.
	 * @return the raw SessionFactory (potentially to be wrapped with a
	 * transaction-aware proxy before it is exposed to the application)
	 * @throws Exception in case of initialization failure
	 */
	protected abstract SessionFactory buildSessionFactory() throws Exception;

	/**
	 * Hook that allows post-processing after the SessionFactory has been
	 * successfully created. The SessionFactory is already available through
	 * <code>getSessionFactory()</code> at this point.
	 * <p>This implementation is empty.
	 * @throws Exception in case of initialization failure
	 * @see #getSessionFactory()
	 */
	protected void afterSessionFactoryCreation() throws Exception {
	}

	/**
	 * Hook that allows shutdown processing before the SessionFactory
	 * will be closed. The SessionFactory is still available through
	 * <code>getSessionFactory()</code> at this point.
	 * <p>This implementation is empty.
	 * @see #getSessionFactory()
	 */
	protected void beforeSessionFactoryDestruction() {
	}

}
