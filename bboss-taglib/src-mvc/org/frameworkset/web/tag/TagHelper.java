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
import java.util.Collection;
import java.util.Locale;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;

import org.frameworkset.spi.support.MessageSource;
import org.frameworkset.spi.support.MessageSourceResolvable;
import org.frameworkset.spi.support.NoSuchMessageException;
import org.frameworkset.spi.support.validate.FieldError;
import org.frameworkset.spi.support.validate.ObjectError;
import org.frameworkset.util.ObjectUtils;
import org.frameworkset.web.util.ExpressionEvaluationUtils;
import org.frameworkset.web.util.JavaScriptUtils;

import com.frameworkset.util.HtmlUtils;
import com.frameworkset.util.StringUtil;

/**
 * <p>Title: TagHelper.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2011-1-11
 * @author biaoping.yin
 * @version 1.0
 */
public abstract class TagHelper {
	/**
	 * Default separator for splitting an arguments String: a comma (",")
	 */
	public static final String DEFAULT_ARGUMENT_SEPARATOR = ",";
	/**
	 * Resolve the specified message into a concrete message String.
	 * The returned message String should be unescaped.
	 */
	public static String resolveMessage(PageContext pageContext,
			MessageSource messageSource,Locale locale,
			String code,String text,Object arguments,String argumentSeparator) throws JspException, NoSuchMessageException {
		return resolveMessage( null,pageContext,
						messageSource,locale,
						code,text,arguments,argumentSeparator);
	}
	
	public static String resolveMessage(PageContext pageContext,
			MessageSource messageSource,Locale locale,
			String code,Object arguments,String argumentSeparator) throws JspException, NoSuchMessageException {
		return resolveMessage( null,pageContext,
						messageSource,locale,
						code,null,arguments,argumentSeparator);
	}
	
	/**
	 * Resolve the specified message into a concrete message String.
	 * The returned message String should be unescaped.
	 */
	public static String resolveMessage(Object message,PageContext pageContext,
			MessageSource messageSource,Locale locale,
			String code,String text,Object arguments,String argumentSeparator) throws JspException, NoSuchMessageException {
//		MessageSource messageSource = getMessageSource();
		if (messageSource == null) {
			throw new JspTagException("No corresponding MessageSource found");
		}

		// Evaluate the specified MessageSourceResolvable, if any.
		MessageSourceResolvable resolvedMessage = null;
		if (message instanceof MessageSourceResolvable) {
			resolvedMessage = (MessageSourceResolvable) message;
		}
		else if (message != null) {
			String expr = message.toString();
			resolvedMessage = (MessageSourceResolvable)
					ExpressionEvaluationUtils.evaluate("message", expr, MessageSourceResolvable.class, pageContext);
		}

		if (resolvedMessage != null) {
			// We have a given MessageSourceResolvable.
//			return messageSource.getMessage(resolvedMessage, getRequestContext().getLocale());
			return messageSource.getMessage(resolvedMessage, locale);
		}

		String resolvedCode = ExpressionEvaluationUtils.evaluateString("code", code, pageContext);
		String resolvedText = ExpressionEvaluationUtils.evaluateString("text", text, pageContext);

		if (resolvedCode != null || resolvedText != null) {
			// We have a code or default text that we need to resolve.
			//resolveArguments(Object arguments,String argumentSeparator,PageContext pageContext)
//			Object[] argumentsArray = resolveArguments(arguments);
			Object[] argumentsArray = resolveArguments(arguments,argumentSeparator,pageContext);
			if (resolvedText != null) {
				// We have a fallback text to consider.
				return messageSource.getMessage(
						resolvedCode, argumentsArray, resolvedText, locale);
			}
			else {
				// We have no fallback text to consider.
				return messageSource.getMessage(
						resolvedCode, argumentsArray,locale);
			}
		}

		// All we have is a specified literal text.
		return resolvedText;
	}


	/**
	 * Resolve the given arguments Object into an arguments array.
	 * @param arguments the specified arguments Object
	 * @return the resolved arguments as array
	 * @throws JspException if argument conversion failed
	 * @see #setArguments
	 */
	public static Object[] resolveArguments(Object arguments,String argumentSeparator,PageContext pageContext) throws JspException {
		if (arguments instanceof String) {
			String[] stringArray =
					StringUtil.delimitedListToStringArray((String) arguments, argumentSeparator);
			if (stringArray.length == 1) {
				Object argument = ExpressionEvaluationUtils.evaluate("argument", stringArray[0], pageContext);
				if (argument != null && argument.getClass().isArray()) {
					return ObjectUtils.toObjectArray(argument);
				}
				else {
					return new Object[] {argument};
				}
			}
			else {
				Object[] argumentsArray = new Object[stringArray.length];
				for (int i = 0; i < stringArray.length; i++) {
					argumentsArray[i] =
							ExpressionEvaluationUtils.evaluate("argument[" + i + "]", stringArray[i], pageContext);
				}
				return argumentsArray;
			}
		}
		else if (arguments instanceof Object[]) {
			return (Object[]) arguments;
		}
		else if (arguments instanceof Collection) {
			return ((Collection) arguments).toArray();
		}
		else if (arguments != null) {
			// Assume a single argument object.
			return new Object[] {arguments};
		}
		else {
			return null;
		}
	}
	
	public static String handlerError(ObjectError error,String defaultArguments,PageContext pageContext,
			MessageSource messageSource,Locale locale,String argumentSeparator,
			boolean isHtmlEscape,boolean isJavaScriptEscape) throws IOException, NoSuchMessageException, JspException
	{
		
		String code = null;
		
		Object arguments = null;
	
		if(error instanceof FieldError)
		{
			FieldError error_ = (FieldError)error;
				

			if(defaultArguments == null)
			{
				if(error_.getArguments() == null || error_.getArguments().length == 0)
				{
					arguments = new Object[]{error_.getField(),error_.getRejectedValue(),error_.getType()};
				}
				else
				{
					arguments = error_.getArguments();
				}
			}
			else
			{
				arguments = defaultArguments;
			}
			
			
		}
		code = error.getCode();
		String msg = null;

		msg = resolveMessage(pageContext,messageSource,
				locale,code,arguments,argumentSeparator);
		if(msg != null )
		 {
			 if( msg.equals(code))
			 {
				 if(error.getDefaultMessage() != null)
					 msg = error.getDefaultMessage();
			 }
		 }

		if(msg != null && !msg.equals(""))
		{
			msg = isHtmlEscape ? HtmlUtils.htmlEscape(msg) : msg;
			msg =  isJavaScriptEscape ? JavaScriptUtils.javaScriptEscape(msg) : msg;
//			writeMessage(msg);
			
		}
		return msg;
		
	}

}
