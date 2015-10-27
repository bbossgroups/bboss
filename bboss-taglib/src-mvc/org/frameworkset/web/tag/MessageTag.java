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

import static org.frameworkset.web.tag.TagHelper.resolveMessage;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import org.frameworkset.spi.support.NoSuchMessageException;
import org.frameworkset.web.util.ExpressionEvaluationUtils;
import org.frameworkset.web.util.JavaScriptUtils;
import org.frameworkset.web.util.TagUtils;

import com.frameworkset.util.HtmlUtils;
import com.frameworkset.util.StringUtil;


/**
 * <p>Title: MessageTag.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-10-10
 * @author biaoping.yin
 * @version 1.0
 */
public class MessageTag extends HtmlEscapingAwareTag {

	


	private Object message;

	private String code;

	private Object arguments;

	private String argumentSeparator = TagHelper.DEFAULT_ARGUMENT_SEPARATOR;

	private String text;
	
	private String var;
	
	private String scope = TagUtils.SCOPE_PAGE;

	


	/**
	 * Set the MessageSourceResolvable for this tag.
	 * Accepts a direct MessageSourceResolvable instance as well as a JSP
	 * expression language String that points to a MessageSourceResolvable.
	 * <p>If a MessageSourceResolvable is specified, it effectively overrides
	 * any code, arguments or text specified on this tag.
	 */
	public void setMessage(Object message) {
		this.message = message;
	}

	/**
	 * Set the message code for this tag.
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * Set optional message arguments for this tag, as a comma-delimited
	 * String (each String argument can contain JSP EL), an Object array
	 * (used as argument array), or a single Object (used as single argument).
	 */
	public void setArguments(Object arguments) {
		this.arguments = arguments;
	}

	/**
	 * Set the separator to use for splitting an arguments String.
	 * Default is a comma (",").
	 * @see #setArguments
	 */
	public void setArgumentSeparator(String argumentSeparator) {
		this.argumentSeparator = argumentSeparator;
	}

	/**
	 * Set the message text for this tag.
	 */
	public void setText(String text) {
		this.text = text;
	}
	
	/**
	 * Set PageContext attribute name under which to expose
	 * a variable that contains the resolved message.
	 * @see #setScope
	 * @see javax.servlet.jsp.PageContext#setAttribute
	 */
	public void setVar(String var) {
		this.var = var;
	}
	
	/**
	 * Set the scope to export the variable to.
	 * Default is SCOPE_PAGE ("page").
	 * @see #setVar

	 * @see javax.servlet.jsp.PageContext#setAttribute
	 */
	public void setScope(String scope) {
		this.scope = scope;
	}

	


	/**
	 * Resolves the message, escapes it if demanded,
	 * and writes it to the page (or exposes it as variable).
	 * @see #resolveMessage()
	
	
	 * @see #writeMessage(String)
	 */
	protected int doStartTagInternal() throws JspException, IOException {
		try {
			// Resolve the unescaped message.
			//resolveMessage(Object message,PageContext pageContext,
//			MessageSource messageSource,Locale locale,
//			String code,String text,Object arguments,String argumentSeparator)
//			String msg = resolveMessage();
			String msg = resolveMessage(message,pageContext,this.getMessageSource(),
					this.getRequestContext().getLocale(),code,text,arguments,argumentSeparator);
			// HTML and/or JavaScript escape, if demanded.
			msg = isHtmlEscape() ? HtmlUtils.htmlEscape(msg) : msg;
			msg = this.isJavaScriptEscape() ? JavaScriptUtils.javaScriptEscape(msg) : msg;

			// Expose as variable, if demanded, else write to the page.
			String resolvedVar = ExpressionEvaluationUtils.evaluateString("var", this.var, pageContext);
			if (resolvedVar != null) {
				String resolvedScope = ExpressionEvaluationUtils.evaluateString("scope", this.scope, pageContext);
				pageContext.setAttribute(resolvedVar, msg, TagUtils.getScope(resolvedScope));
			}
			else {
				writeMessage(msg);
			}

			return EVAL_BODY_INCLUDE;
		}
		catch (NoSuchMessageException ex) {
			throw new JspTagException(getNoSuchMessageExceptionDescription(ex));
		}
	}

	

	

	

	/**
	 * Return default exception message.
	 */
	protected String getNoSuchMessageExceptionDescription(NoSuchMessageException ex) {
		return ex.getMessage();
	}

	

	protected String getCode() {
		return code;
	}

	protected String getText() {
		return text;
	}

	@Override
	public int doStartTag() throws JspException {
		 
		 if(StringUtil.isEmpty(code))
		 {
			 init();
			 this.code = super.getStringValue();
		 }
			
		return super.doStartTag();
	}
	public void doFinally() {
		this.code = null;
		super.doFinally();
	}
}
