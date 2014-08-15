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


import org.apache.log4j.Logger;
import org.frameworkset.spi.ApplicationContextException;
import org.frameworkset.spi.BaseApplicationContext;

import com.frameworkset.spi.assemble.BeanInstanceException;





/**
 * <p>Title: ApplicationObjectSupport.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-9-24 下午04:51:39
 * @author biaoping.yin
 * @version 1.0
 */
public class ApplicationObjectSupport {
	/** Logger that is available to subclasses */
	protected static final Logger logger = Logger.getLogger(ApplicationObjectSupport.class);
	
	/** ApplicationContext this object runs in */
	private BaseApplicationContext applicationContext;

	/** MessageSourceAccessor for easy message access */
	private MessageSourceAccessor messageSourceAccessor;


	public final void setApplicationContext(BaseApplicationContext context) throws BeanInstanceException {
		if (context == null && !isContextRequired()) {
			// Reset internal context state.
			this.applicationContext = null;
			this.messageSourceAccessor = null;
		}
		else if (this.applicationContext == null) {
			// Initialize with passed-in context.
			if (!requiredContextClass().isInstance(context)) {
				throw new ApplicationContextException(
						"Invalid application context: needs to be of type [" + requiredContextClass().getName() + "]");
			}
			this.applicationContext = context;
			this.messageSourceAccessor = new MessageSourceAccessor(context);
			initApplicationContext(context);
		}
		else {
			// Ignore reinitialization if same context passed in.
			if (this.applicationContext != context) {
				throw new ApplicationContextException(
						"Cannot reinitialize with different application context: current one is [" +
						this.applicationContext + "], passed-in one is [" + context + "]");
			}
		}
	}

	/**
	 * Determine whether this application object needs to run in an ApplicationContext.
	 * <p>Default is "false". Can be overridden to enforce running in a context
	 * (i.e. to throw IllegalStateException on accessors if outside a context).
	 * @see #getApplicationContext
	 * @see #getMessageSourceAccessor
	 */
	protected boolean isContextRequired() {
		return false;
	}

	/**
	 * Determine the context class that any context passed to
	 * <code>setApplicationContext</code> must be an instance of.
	 * Can be overridden in subclasses.
	 * @see #setApplicationContext
	 */
	protected Class requiredContextClass() {
		return BaseApplicationContext.class;
	}

	/**
	 * Subclasses can override this for custom initialization behavior.
	 * Gets called by <code>setApplicationContext</code> after setting the context instance.
	 * <p>Note: Does </i>not</i> get called on reinitialization of the context
	 * but rather just on first initialization of this object's context reference.
	 * <p>The default implementation calls the overloaded {@link #initApplicationContext()}
	 * method without ApplicationContext reference.
	 * @param context the containing ApplicationContext
	 * @throws ApplicationContextException in case of initialization errors
	 * @throws Exception if thrown by ApplicationContext methods
	 * @see #setApplicationContext
	 */
	protected void initApplicationContext(BaseApplicationContext context) throws RuntimeException {
		initApplicationContext();
	}

	/**
	 * Subclasses can override this for custom initialization behavior.
	 * <p>The default implementation is empty. Called by
	 * {@link #initApplicationContext(org.frameworkset.spi.ApplicationContext)}.
	 * @throws ApplicationContextException in case of initialization errors
	 * @throws Exception if thrown by ApplicationContext methods
	 * @see #setApplicationContext
	 */
	protected void initApplicationContext() throws RuntimeException {
	}


	/**
	 * Return the ApplicationContext that this object is associated with.
	 * @throws IllegalStateException if not running in an ApplicationContext
	 */
	public final BaseApplicationContext getApplicationContext() throws IllegalStateException {
		if (this.applicationContext == null && isContextRequired()) {
			throw new IllegalStateException(
					"ApplicationObjectSupport instance [" + this + "] does not run in an ApplicationContext");
		}
		return this.applicationContext;
	}

	/**
	 * Return a MessageSourceAccessor for the application context
	 * used by this object, for easy message access.
	 * @throws IllegalStateException if not running in an ApplicationContext
	 */
	protected final MessageSourceAccessor getMessageSourceAccessor() throws IllegalStateException {
		if (this.messageSourceAccessor == null && isContextRequired()) {
			throw new IllegalStateException(
					"ApplicationObjectSupport instance [" + this + "] does not run in an ApplicationContext");
		}
		return this.messageSourceAccessor;
	}
}
