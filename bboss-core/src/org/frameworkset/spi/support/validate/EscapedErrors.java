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
package org.frameworkset.spi.support.validate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.frameworkset.util.HtmlUtils;

/**
 * <p>Title: EscapedErrors.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-9-28
 * @author biaoping.yin
 * @version 1.0
 */
public class EscapedErrors  implements Errors {

	private final Errors source;


	/**
	 * Create a new EscapedErrors instance for the given source instance.
	 */
	public EscapedErrors(Errors source) {
		if (source == null) {
			throw new IllegalArgumentException("Cannot wrap a null instance");
		}
		this.source = source;
	}

	public Errors getSource() {
		return this.source;
	}


	public String getObjectName() {
		return this.source.getObjectName();
	}

	public void setNestedPath(String nestedPath) {
		this.source.setNestedPath(nestedPath);
	}

	public String getNestedPath() {
		return this.source.getNestedPath();
	}

	public void pushNestedPath(String subPath) {
		this.source.pushNestedPath(subPath);
	}

	public void popNestedPath() throws IllegalStateException {
		this.source.popNestedPath();
	}


	public void reject(String errorCode) {
		this.source.reject(errorCode);
	}

	public void reject(String errorCode, String defaultMessage) {
		this.source.reject(errorCode, defaultMessage);
	}

	public void rejectWithErrorArgs(String errorCode, Object[] errorArgs, String defaultMessage) {
		this.source.rejectWithErrorArgs(errorCode, errorArgs, defaultMessage);
	}

	public void rejectValue(String field, String errorCode) {
		this.source.rejectValue(field, errorCode);
	}

	public void rejectValue(String field, String errorCode, String defaultMessage) {
		this.source.rejectValue(field, errorCode, defaultMessage);
	}

	public void rejectValueWithErrorArgs(String field, String errorCode, Object[] errorArgs, String defaultMessage) {
		this.source.rejectValueWithErrorArgs(field, errorCode, errorArgs, defaultMessage);
	}

	public void addAllErrors(Errors errors) {
		this.source.addAllErrors(errors);
	}


	public boolean hasErrors() {
		return this.source.hasErrors();
	}

	public int getErrorCount() {
		return this.source.getErrorCount();
	}

	public List getAllErrors() {
		return escapeObjectErrors(this.source.getAllErrors());
	}

	public boolean hasGlobalErrors() {
		return this.source.hasGlobalErrors();
	}

	public int getGlobalErrorCount() {
		return this.source.getGlobalErrorCount();
	}

	public List getGlobalErrors() {
		return escapeObjectErrors(this.source.getGlobalErrors());
	}

	public ObjectError getGlobalError() {
		return escapeObjectError(this.source.getGlobalError());
	}

	public boolean hasFieldErrors() {
		return this.source.hasFieldErrors();
	}

	public int getFieldErrorCount() {
		return this.source.getFieldErrorCount();
	}

	public List getFieldErrors() {
		return this.source.getFieldErrors();
	}

//	public FieldError getFieldError() {
//		return this.source.getFieldError();
//	}

	public boolean hasFieldErrors(String field) {
		return this.source.hasFieldErrors(field);
	}

//	public int getFieldErrorCount(String field) {
//		return this.source.getFieldErrorCount(field);
//	}

//	public List getFieldErrors(String field) {
//		return escapeObjectErrors(this.source.getFieldErrors(field));
//	}

	public FieldError getFieldError(String field) {
		return (FieldError) escapeObjectError(this.source.getFieldError(field));
	}

	public Object getFieldValue(String field) {
		Object value = this.source.getFieldValue(field);
		return (value instanceof String ? HtmlUtils.htmlEscape((String) value) : value);
	}

//	public Class getFieldType(String field) {
//		return this.source.getFieldType(field);
//	}

	private ObjectError escapeObjectError(ObjectError source) {
		if (source == null) {
			return null;
		}
		if (source instanceof FieldError) {
			FieldError fieldError = (FieldError) source;
			Object value = fieldError.getRejectedValue();
			if (value instanceof String) {
				value = HtmlUtils.htmlEscape((String) value);
			}
			return new FieldError(
					fieldError.getObjectName(), fieldError.getField(), value,fieldError.getType(),
					fieldError.isBindingFailure(), fieldError.getCodes(),
					fieldError.getArguments(), HtmlUtils.htmlEscape(fieldError.getDefaultMessage()));
		}
		return new ObjectError(
				source.getObjectName(), source.getCodes(), source.getArguments(),
				HtmlUtils.htmlEscape(source.getDefaultMessage()));
	}

	private List escapeObjectErrors(List source) {
		List escaped = new ArrayList(source.size());
		for (Iterator it = source.iterator(); it.hasNext();) {
			ObjectError objectError = (ObjectError)it.next();
			escaped.add(escapeObjectError(objectError));
		}
		return escaped;
	}

	public Object getTarget() {
		
		return this.source.getTarget();
	}

	
	public void rejectValue(String field, String errorCode, String rejectvalue,
			String defaultMessage) {
		this.source.rejectValue( field,  errorCode,  rejectvalue,
				   defaultMessage);
		
	}

	
	public void rejectValue(String field, String errorCode,
			String[] rejectvalue, String defaultMessage) {
		this.source.rejectValue( field,  errorCode,  rejectvalue,
				   defaultMessage);
		
	}

	
	public void rejectValue(String field, String errorCode, String rejectvalue,
			Class fieldtype, String defaultMessage) {
		this.source.rejectValue( field,  errorCode,  rejectvalue,
				 fieldtype,  defaultMessage);
		
	}

	
	public void rejectValue(String field, String errorCode,
			String[] rejectvalue, Class fieldtype, String defaultMessage) {
		this.source.rejectValue( field,  errorCode,
				 rejectvalue,  fieldtype,  defaultMessage);
		
	}

	public void rejectValueWithErrorArgs(String field, String errorCode,
			Object[] errorArgs) {
		this.source.rejectValueWithErrorArgs( field,  errorCode,
				 errorArgs);
		
	}

	public void rejectWithErrorArgs(String errorCode, Object[] errorArgs) {
		// TODO Auto-generated method stub
		rejectWithErrorArgs(errorCode, errorArgs,null);
		
	}

	public int getFieldErrorCount(String field) {
		// TODO Auto-generated method stub
		return source.getFieldErrorCount(field);
	}

	public List getFieldErrors(String field) {
		// TODO Auto-generated method stub
		return source.getFieldErrors(field);
	}

}
