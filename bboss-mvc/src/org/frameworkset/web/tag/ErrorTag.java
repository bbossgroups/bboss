/**
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
import java.util.List;
import java.util.Locale;

import javax.servlet.jsp.JspException;

import org.frameworkset.spi.support.MessageSource;
import org.frameworkset.spi.support.validate.Errors;
import org.frameworkset.spi.support.validate.FieldError;
import org.frameworkset.spi.support.validate.ObjectError;

/**
 * <p>
 * Title: ErrorTag.java
 * </p>
 * <p>
 * Description: 绑定参数异常信息标签
 * </p>
 * <p>
 * bboss workgroup
 * </p>
 * <p>
 * Copyright (c) 2008-2010
 * </p>
 * 
 * @Date 2011-1-9
 * @author biaoping.yin
 * @version 1.0
 */
public class ErrorTag extends HtmlEscapingAwareTag  {
	private String colName;
	private String arguments;
	private String text;

	
	

	private String argumentSeparator = TagHelper.DEFAULT_ARGUMENT_SEPARATOR;

	/**
	 * @return the colName
	 */
	public String getColName() {
		return colName;
	}



	/**
	 * @param colName the colName to set
	 */
	public void setColName(String colName) {
		this.colName = colName;
	}



//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see org.frameworkset.web.tag.RequestContextAwareTaartTagInternal()
//	 */
//	@Override
//	protected int doStartTagInternal() throws JspException, IOException {
//		
//		ErrorsTag parent = (ErrorsTag)findAncestorWithClass(this, ErrorsTag.class);
//		if(parent != null)
//		{
//			ObjectError error = parent.getCurrent_error();
//			
//			handlerError( error);
//			
//		}
//		else
//		{
//			Errors errors = (Errors) this.getRequestContext().getErrors("default");
//			boolean flag = false;
//			String code = null;
//			FieldError error_ = null;
//			Object arguments = null;
//			if (errors != null) {
//				List errors_ = errors.getFieldErrors(colName);
//				for (int i = 0; errors_ != null && i < errors_.size(); i++) {
//					 error_ = (FieldError) errors_.get(i);
////					 handlerError( error_);
//	//				if(colName.equals(error_.getField()))
//					{
//						code = error_.getCode();
//						if(error_.getArguments() == null || error_.getArguments().length == 0)
//						{
//							arguments = new Object[]{error_.getField(),error_.getRejectedValue(),error_.getType()};
//						}
//						else
//						{
//							arguments = error_.getArguments();
//						}
//	//					this.setMessage(error_.getDefaultMessage()+":use default message.");
//						
//						flag = true;
//						break;
//					}
//					
//	
//				}
//			}
//			
//			String msg = null;
//			if(flag)
//			{
//	//			msg = this.resolveMessage();
//				msg = resolveMessage(pageContext,this.getMessageSource(),
//						this.getRequestContext().getLocale(),code,arguments,argumentSeparator);
//				 if(msg != null )
//				 {
//					 if( msg.equals(error_.getCode()))
//					 {
//						 if(error_.getDefaultMessage() != null)
//							 msg = error_.getDefaultMessage();
//						 
//					 }
//					
//				 }
//				
//	//			
//			}
//			if(msg != null && !msg.equals(""))
//			{
//			// HTML and/or JavaScript escape, if demanded.
//				msg = isHtmlEscape() ? HtmlUtils.htmlEscape(msg) : msg;
//				msg =  this.isJavaScriptEscape() ? JavaScriptUtils.javaScriptEscape(msg) : msg;
//				this.writeMessage(msg);
//				
//			}
//		}
//		return this.EVAL_BODY_INCLUDE;
//	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.frameworkset.web.tag.RequestContextAwareTaartTagInternal()
	 */
	@Override
	protected int doStartTagInternal() throws JspException, IOException {
		
		ErrorsTag parent = (ErrorsTag)findAncestorWithClass(this, ErrorsTag.class);
		if(parent != null)
		{
			ObjectError error = parent.getCurrent_error();
			parent.setContainErrorTag(true);
			
//			handlerError( error);
			String msg = TagHelper.handlerError( error,this.arguments,pageContext,
					this.getMessageSource(),this.getRequestContext().getLocale(),argumentSeparator,
					this.isHtmlEscape(),isJavaScriptEscape());
			this.writeMessage(msg);
			
		}
		else
		{
			Errors errors = (Errors) this.getRequestContext().getErrors("default");
			boolean flag = false;
			String code = null;
			FieldError error_ = null;
			Object arguments = null;
			if (errors != null) {
				List errors_ = errors.getFieldErrors(colName);
				String msg = null;
				MessageSource msr = this.getMessageSource();
				Locale locale = this.getRequestContext().getLocale();
//				FieldError error_ = null;
				StringBuffer buffer = new StringBuffer();
				for (int i = 0; errors_ != null && i < errors_.size(); i++) {
					 error_ = (FieldError) errors_.get(i);
//					 handlerError( error_);
					 msg = TagHelper.handlerError( error_,this.arguments,pageContext,
							 msr,locale,argumentSeparator,
								this.isHtmlEscape(),isJavaScriptEscape());
					 buffer.append(msg);
					 if(i > 0 && i < errors_.size() - 1)
						 buffer.append("<br>");
						 
					
				}
				if(buffer.length() > 0)
					this.writeMessage(buffer.toString());
			}

		}
		return this.EVAL_BODY_INCLUDE;
	}
	
//	private void handlerError(ObjectError error) throws IOException, NoSuchMessageException, JspException
//	{
//		
//		String code = null;
//		
//		Object arguments = null;
//	
//		if(error instanceof FieldError)
//		{
//			FieldError error_ = (FieldError)error;
//				
//
//			if(this.arguments == null)
//			{
//				if(error_.getArguments() == null || error_.getArguments().length == 0)
//				{
//					arguments = new Object[]{error_.getField(),error_.getRejectedValue(),error_.getType()};
//				}
//				else
//				{
//					arguments = error_.getArguments();
//				}
//			}
//			else
//			{
//				arguments = this.arguments;
//			}
//			
//			
//		}
//		code = error.getCode();
//		String msg = null;
//
//		msg = resolveMessage(pageContext,this.getMessageSource(),
//				this.getRequestContext().getLocale(),code,arguments,argumentSeparator);
//		if(msg != null )
//		 {
//			 if( msg.equals(code))
//			 {
//				 if(error.getDefaultMessage() != null)
//					 msg = error.getDefaultMessage();
//			 }
//		 }
//
//		if(msg != null && !msg.equals(""))
//		{
//			msg = isHtmlEscape() ? HtmlUtils.htmlEscape(msg) : msg;
//			msg =  this.isJavaScriptEscape() ? JavaScriptUtils.javaScriptEscape(msg) : msg;
//			this.writeMessage(msg);
//			
//		}
//	}



	



	public void setArguments(String arguments) {
		this.arguments = arguments;
	}



	public String getText() {
		return text;
	}



	public void setText(String text) {
		this.text = text;
	}



	



	public void doFinally() {
		colName = null;
		arguments = null;
		text = null;
		argumentSeparator = TagHelper.DEFAULT_ARGUMENT_SEPARATOR;
		super.doFinally();
	}



	public String getArgumentSeparator() {
		return argumentSeparator;
	}



	public void setArgumentSeparator(String argumentSeparator) {
		if(argumentSeparator != null && !argumentSeparator.equals(""))
		{
			this.argumentSeparator = argumentSeparator;
		}
		
	}
	
	

}
