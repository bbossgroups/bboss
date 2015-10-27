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
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import org.frameworkset.spi.support.NoSuchMessageException;
import org.frameworkset.spi.support.validate.Errors;
import org.frameworkset.spi.support.validate.FieldError;
import org.frameworkset.spi.support.validate.ObjectError;

/**
 * <p>Title: ErrorTag.java</p> 
 * <p>Description: 输出全局错误和参数绑定错误信息标签
 * 
 * <div>error:
	<pg:errors >
			<pg:error/>
		</pg:errors >
	</div>
	<div>errors:<pg:errors /></div>
	<div>globalerrors:<pg:globalerrors /></div>
	<div>message:<pg:error colName="message"/></div>
		<div>${error_message}</div>
 * 
 * </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2011-1-4
 * @author biaoping.yin
 * @version 1.0
 */
public class ErrorsTag extends HtmlEscapingAwareTag {
	
	private ObjectError current_error;
	private List errors_;
	private int curindex;
	private boolean containErrorTag = false;
	private boolean needevalbodyAtEndTag = false;
	private String arguments;
	
	private String argumentSeparator = TagHelper.DEFAULT_ARGUMENT_SEPARATOR;
	
	
	protected FieldError getFileError(){
		return (FieldError)current_error;
	}
	
	protected boolean suppport(ObjectError current_error)
	{
		return true;
	}
	
	

	@Override
	protected int doStartTagInternal() throws Exception {
		
		Errors errors = null;
		{
			errors = this.getRequestContext().getErrors("default");
		}
		
		
		if(errors != null)
		{
			

			errors_ = errors.getAllErrors();
			curindex = 0;
//			if(errors_ != null && index <errors_.size())
//			{
//				ObjectError temp = (ObjectError)errors_.get(index);
//				index ++;
//				if(this.suppport(temp))
//				{
//					current_error = temp;
//				}
//				else
//				{
//					while(index < errors_.size())
//					{
//						temp = (ObjectError)errors_.get(index);
//						index ++;
//						if(this.suppport(temp))
//						{
//							current_error = temp;
//							break;
//						}
//					}
//				}
//				
//				
//				if(current_error != null)
//					return EVAL_BODY_INCLUDE;
//			}
			evaluateNextError();
			if(current_error != null)
			{
				needevalbodyAtEndTag = true;
				return EVAL_BODY_INCLUDE;
			}
		}
		return SKIP_BODY;
		
	}
	private void evaluateNextError()
	{
		current_error = null;
		if(errors_ != null && curindex <errors_.size())
		{
			ObjectError temp = (ObjectError)errors_.get(curindex);
			curindex ++;
			if(this.suppport(temp))
			{
				current_error = temp;
			}
			else
			{
				while(curindex < errors_.size())
				{
					temp = (ObjectError)errors_.get(curindex);
					curindex ++;
					if(this.suppport(temp))
					{
						current_error = temp;
						break;
					}
				}
			}
		}
	}
	private void evalbody() throws JspException
	{
		do
		{
			try {
				String msg = TagHelper.handlerError(current_error, this.arguments, 
						pageContext, getMessageSource(), 
						this.getRequestContext().getLocale(),
						argumentSeparator, 
						this.isHtmlEscape(), isJavaScriptEscape());
				this.writeMessage(msg + "<br>");
//					return SKIP_BODY;
			} catch (NoSuchMessageException e) {
				
				logger.error(e.getMessage(), e);
				throw new JspTagException(e.getMessage());
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
				throw new JspTagException(e.getMessage());
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
			evaluateNextError();		
		}while(current_error != null);
	}
	public int doAfterBody() throws JspException {
		needevalbodyAtEndTag = false;
		if(errors_ == null || errors_.size() == 0)
			return SKIP_BODY;
		
		if(curindex >= errors_.size())
		{
			if(this.isContainErrorTag())
				return SKIP_BODY;
			else
			{
				try {
					String msg = TagHelper.handlerError(current_error, this.arguments, 
							pageContext, getMessageSource(), 
							this.getRequestContext().getLocale(),
							argumentSeparator, 
							this.isHtmlEscape(), isJavaScriptEscape());
					this.writeMessage(msg);
					return SKIP_BODY;
				} catch (NoSuchMessageException e) {
					
					logger.error(e.getMessage(), e);
					throw new JspTagException(e.getMessage());
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
					throw new JspTagException(e.getMessage());
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
		}
		else
		{
			if(!this.isContainErrorTag())
			{
				 evalbody();
				return SKIP_BODY;
			}
			else
			{
				evaluateNextError();			
				if(current_error != null)
				{	
					return EVAL_BODY_AGAIN;
				}
				else
					return SKIP_BODY;
			}
		}
	}

	@Override
	public int doEndTag() throws JspException {
		if(needevalbodyAtEndTag)//没有指定标签体，则在标签结束时输出错误信息
		{
			needevalbodyAtEndTag = false;
			 evalbody();
		}
		
		return super.doEndTag();
	}
	
	public void doFinally()
	{
		curindex = 0;
		current_error = null;
		errors_ = null;
		needevalbodyAtEndTag = false;
		
		containErrorTag = false;
		arguments = null;
		
		argumentSeparator = TagHelper.DEFAULT_ARGUMENT_SEPARATOR;
		super.doFinally();
	}

	protected ObjectError getCurrent_error() {
		return current_error;
	}

	 

	private boolean isContainErrorTag() {
		return containErrorTag;
	}

	public void setContainErrorTag(boolean containErrorTag) {
		this.containErrorTag = containErrorTag;
	}

	public String getArguments() {
		return arguments;
	}

	public void setArguments(String arguments) {
		this.arguments = arguments;
	}

	public String getArgumentSeparator() {
		return argumentSeparator;
	}

	public void setArgumentSeparator(String argumentSeparator) {
		this.argumentSeparator = argumentSeparator;
	}
	

}
