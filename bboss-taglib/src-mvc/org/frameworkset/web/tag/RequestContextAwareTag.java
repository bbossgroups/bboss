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
package org.frameworkset.web.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TryCatchFinally;

import org.apache.log4j.Logger;
import org.frameworkset.spi.support.MessageSource;
import org.frameworkset.web.servlet.support.RequestContext;
import org.frameworkset.web.servlet.support.RequestContextUtils;

import com.frameworkset.common.tag.pager.tags.CellTag;

/**
 * <p>Title: RequestContextAwareTag.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-10-10
 * @author biaoping.yin
 * @version 1.0
 */
public abstract class RequestContextAwareTag extends CellTag implements TryCatchFinally {



	
	/** Logger available to subclasses */
	protected final static Logger logger = Logger.getLogger(RequestContextAwareTag.class);


	private RequestContext requestContext;


	/**
	 * Create and expose the current RequestContext.
	 * Delegates to {@link #doStartTagInternal()} for actual work.
	 * @see #REQUEST_CONTEXT_PAGE_ATTRIBUTE
	 
	 */
	public  int doStartTag() throws JspException {
		this.requestContext = RequestContextUtils.getRequestContext(pageContext);
		try {
//			if (this.requestContext == null) {
//				this.requestContext = new JspAwareRequestContext(this.pageContext);
//				this.pageContext.setAttribute(REQUEST_CONTEXT_PAGE_ATTRIBUTE, this.requestContext);
//			}
			return doStartTagInternal();
		}
		catch (JspException ex) {
			logger.error(ex.getMessage(), ex);
			throw ex;
		}
		catch (RuntimeException ex) {
			logger.error(ex.getMessage(), ex);
			throw ex;
		}
		catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new JspTagException(ex.getMessage());
		}
	}

	/**
	 * Return the current RequestContext.
	 */
	protected final RequestContext getRequestContext() {
		return this.requestContext;
	}

	/**
	 * Called by doStartTag to perform the actual work.
	 * @return same as TagSupport.doStartTag
	 * @throws Exception any exception, any checked one other than
	 * a JspException gets wrapped in a JspException by doStartTag
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag
	 */
	protected abstract int doStartTagInternal() throws Exception;


	public void doCatch(Throwable throwable) throws Throwable {
		throw throwable;
	}

	public void doFinally() {
		this.requestContext = null;
		super.doFinally();
	}
	/**
	 * Write the message to the page.
	 * <p>Can be overridden in subclasses, e.g. for testing purposes.
	 * @param msg the message to write
	 * @throws IOException if writing failed
	 */
	protected void writeMessage(Object msg) throws IOException {
		pageContext.getOut().write(String.valueOf(msg));
	}
	
	/**
	 * Use the current RequestContext's application context as MessageSource.
	 */
	protected MessageSource getMessageSource() {
		return getRequestContext().getMessageSource();
	}

}
