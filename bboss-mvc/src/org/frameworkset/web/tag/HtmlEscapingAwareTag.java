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

import javax.servlet.jsp.JspException;

import org.frameworkset.web.util.ExpressionEvaluationUtils;

/**
 * <p>Title: HtmlEscapingAwareTag.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-10-10
 * @author biaoping.yin
 * @version 1.0
 */
public abstract class HtmlEscapingAwareTag  extends RequestContextAwareTag {

	private Boolean htmlEscape;
	private boolean javaScriptEscape = false;

	protected boolean isJavaScriptEscape() {
		return javaScriptEscape;
	}
	/**
	 * Set JavaScript escaping for this tag, as boolean value.
	 * Default is "false".
	 */
	public void setJavaScriptEscape(String javaScriptEscape) throws JspException {
		this.javaScriptEscape =
				ExpressionEvaluationUtils.evaluateBoolean("javaScriptEscape", javaScriptEscape, pageContext);
	}
	/**
	 * Set HTML escaping for this tag, as boolean value.
	 * Overrides the default HTML escaping setting for the current page.
	 * @see HtmlEscapeTag#setDefaultHtmlEscape
	 */
	public void setHtmlEscape(String htmlEscape) throws JspException {
		this.htmlEscape =
				new Boolean(ExpressionEvaluationUtils.evaluateBoolean("htmlEscape", htmlEscape, pageContext));
	}

	/**
	 * Return the HTML escaping setting for this tag,
	 * or the default setting if not overridden.
	 * @see #isDefaultHtmlEscape()
	 */
	protected boolean isHtmlEscape() {
		if (this.htmlEscape != null) {
			return this.htmlEscape.booleanValue();
		}
		else {
			return isDefaultHtmlEscape();
		}
	}

	/**
	 * Return the applicable default HTML escape setting for this tag.
	 * <p>The default implementation checks the RequestContext's setting,
	 * falling back to <code>false</code> in case of no explicit default given.
	 * @see #getRequestContext()
	 */
	protected boolean isDefaultHtmlEscape() {
		return getRequestContext().isDefaultHtmlEscape();
	}
	
	public void doFinally() {
		htmlEscape = false;
		javaScriptEscape = false;
		super.doFinally();
	}

}
