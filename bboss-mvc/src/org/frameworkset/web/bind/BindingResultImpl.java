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
package org.frameworkset.web.bind;

import java.util.Collections;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.frameworkset.spi.support.bind.BindingErrorProcessor;
import org.frameworkset.spi.support.bind.DefaultBindingErrorProcessor;
import org.frameworkset.spi.support.validate.BindingResult;
import org.frameworkset.spi.support.validate.DefaultMessageCodesResolver;
import org.frameworkset.spi.support.validate.Errors;
import org.frameworkset.spi.support.validate.FieldError;
import org.frameworkset.spi.support.validate.MessageCodesResolver;
import org.frameworkset.spi.support.validate.ObjectError;

import com.frameworkset.util.StringUtil;

/**
 * <p>Title: BindingResultImpl.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-12-24
 * @author biaoping.yin
 * @version 1.0
 */
public class BindingResultImpl implements BindingResult {
//	private Map<String,FieldError> errors = new HashMap<String,FieldError>();
//	private Map model;
	private Object target;
	private MessageCodesResolver messageCodesResolver = new DefaultMessageCodesResolver();

	private final List errors = new LinkedList();
	private BindingErrorProcessor bindingErrorProcessor;
	private String objectName;
	private String nestedPath = "";

	private final Stack nestedPathStack = new Stack();
	
	public BindingResultImpl(String objectName,DefaultBindingErrorProcessor bindingErrorProcessor)
	{
		this.objectName = objectName;
		this.bindingErrorProcessor = bindingErrorProcessor;
	}
	/**
	 * Create a new AbstractBindingResult instance.
	 * @param objectName the name of the target object
	 * @see DefaultMessageCodesResolver
	 */
	public BindingResultImpl(String objectName) {
		this.objectName = objectName == null?"command":objectName;
		this.bindingErrorProcessor = new DefaultBindingErrorProcessor();
	}

	/**
	 * Set the strategy to use for resolving errors into message codes.
	 * Default is DefaultMessageCodesResolver.
	 * @see DefaultMessageCodesResolver
	 */
	public void setMessageCodesResolver(MessageCodesResolver messageCodesResolver) {
		this.messageCodesResolver = messageCodesResolver;
	}

	/**
	 * Return the strategy to use for resolving errors into message codes.
	 */
	public MessageCodesResolver getMessageCodesResolver() {
		return this.messageCodesResolver;
	}


	//---------------------------------------------------------------------
	// Implementation of the Errors interface
	//---------------------------------------------------------------------

	public String getObjectName() {
		return this.objectName;
	}


	
	
	



	public void setNestedPath(String nestedPath) {
		doSetNestedPath(nestedPath);
		this.nestedPathStack.clear();
	}

	public String getNestedPath() {
		return this.nestedPath;
	}

	public void pushNestedPath(String subPath) {
		this.nestedPathStack.push(getNestedPath());
		doSetNestedPath(getNestedPath() + subPath);
	}

	public void popNestedPath() throws IllegalArgumentException {
		try {
			String formerNestedPath = (String) this.nestedPathStack.pop();
			doSetNestedPath(formerNestedPath);
		}
		catch (EmptyStackException ex) {
			throw new IllegalStateException("Cannot pop nested path: no nested path on stack");
		}
	}

	/**
	 * Actually set the nested path.
	 * Delegated to by setNestedPath and pushNestedPath.
	 */
	protected void doSetNestedPath(String nestedPath) {
		if (nestedPath == null) {
			nestedPath = "";
		}
		nestedPath = canonicalFieldName(nestedPath);
		if (nestedPath.length() > 0 && !nestedPath.endsWith(Errors.NESTED_PATH_SEPARATOR)) {
			nestedPath += Errors.NESTED_PATH_SEPARATOR;
		}
		this.nestedPath = nestedPath;
	}

	/**
	 * Transform the given field into its full path,
	 * regarding the nested path of this instance.
	 */
	protected String fixedField(String field) {
		if (StringUtil.hasLength(field)) {
			return getNestedPath() + canonicalFieldName(field);
		}
		else {
			String path = getNestedPath();
			return (path.endsWith(Errors.NESTED_PATH_SEPARATOR) ?
					path.substring(0, path.length() - NESTED_PATH_SEPARATOR.length()) : path);
		}
	}

	/**
	 * Determine the canonical field name for the given field.
	 * <p>The default implementation simply returns the field name as-is.
	 * @param field the original field name
	 * @return the canonical field name
	 */
	protected String canonicalFieldName(String field) {
		return field;
	}
	
	public void rejectValueWithErrorArgs(String field, String errorCode, Object[] errorArgs) {
		rejectValueWithErrorArgs( field, errorCode, errorArgs, null);
	}
	public void rejectValueWithErrorArgs(String field, String errorCode, Object[] errorArgs, String defaultMessage) {
		if ("".equals(getNestedPath()) && !StringUtil.hasLength(field)) {
			// We're at the top of the nested object hierarchy,
			// so the present level is not a field but rather the top object.
			// The best we can do is register a global error here...
			rejectWithErrorArgs(errorCode, errorArgs, defaultMessage);
			return;
		}
		String fixedField = fixedField(field);
		Object newVal = null;//getActualFieldValue(fixedField);
//		FieldError fe = new FieldError(
//				getObjectName(), fixedField, newVal, null,false,
//				resolveMessageCodes(errorCode, field,null), errorArgs, defaultMessage);
		FieldError fe = new FieldError(
				getObjectName(), fixedField, newVal, null,false,
				new String[]{errorCode}, errorArgs, defaultMessage);
		addError(fe);
	}
	
	public void rejectValueWithErrorArgs(String field, String errorCode, Object[] errorArgs,String value,Class type, String defaultMessage) {
		if ("".equals(getNestedPath()) && !StringUtil.hasLength(field)) {
			// We're at the top of the nested object hierarchy,
			// so the present level is not a field but rather the top object.
			// The best we can do is register a global error here...
			rejectWithErrorArgs(errorCode, errorArgs, defaultMessage);
			return;
		}
		String fixedField = fixedField(field);
		Object newVal = value;//getActualFieldValue(fixedField);
//		FieldError fe = new FieldError(
//				getObjectName(), fixedField, newVal,type, false,
//				resolveMessageCodes(errorCode, field, type), errorArgs, defaultMessage);
		FieldError fe = new FieldError(
				getObjectName(), fixedField, newVal,type, false,
				new String[]{errorCode}, errorArgs, defaultMessage);
		addError(fe);
	}
	
	public void rejectValueWithErrorArgs(String field, String errorCode, Object[] errorArgs,String[] value,Class type, String defaultMessage) {
		if ("".equals(getNestedPath()) && !StringUtil.hasLength(field)) {
			// We're at the top of the nested object hierarchy,
			// so the present level is not a field but rather the top object.
			// The best we can do is register a global error here...
			rejectWithErrorArgs(errorCode, errorArgs, defaultMessage);
			return;
		}
		String fixedField = fixedField(field);
		Object newVal = value;//getActualFieldValue(fixedField);
//		FieldError fe = new FieldError(
//				getObjectName(), fixedField, newVal,type, false,
//				resolveMessageCodes(errorCode, field,type), errorArgs, defaultMessage);
		FieldError fe = new FieldError(
				getObjectName(), fixedField, newVal,type, false,
				new String[]{errorCode}, errorArgs, defaultMessage);
		addError(fe);
	}
	
	/* (non-Javadoc)
	 * @see org.frameworkset.spi.support.validate.Errors#rejectValue(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void rejectValue(String field, String errorCode, String rejectvalue,
			String defaultMessage) {
		rejectValueWithErrorArgs( field, errorCode, null,rejectvalue,null,  defaultMessage);
		
	}
	/* (non-Javadoc)
	 * @see org.frameworkset.spi.support.validate.Errors#rejectValue(java.lang.String, java.lang.String, java.lang.String[], java.lang.String)
	 */
	public void rejectValue(String field, String errorCode,
			String[] rejectvalue, String defaultMessage) {
		rejectValueWithErrorArgs( field, errorCode, null,rejectvalue,null,  defaultMessage);
		
	}
	/* (non-Javadoc)
	 * @see org.frameworkset.spi.support.validate.Errors#rejectValue(java.lang.String, java.lang.String, java.lang.String, java.lang.Class, java.lang.String)
	 */
	public void rejectValue(String field, String errorCode, String rejectvalue,
			Class fieldtype, String defaultMessage) {
		rejectValueWithErrorArgs( field, errorCode, null,rejectvalue,fieldtype,  defaultMessage);
		
	}
	/* (non-Javadoc)
	 * @see org.frameworkset.spi.support.validate.Errors#rejectValue(java.lang.String, java.lang.String, java.lang.String[], java.lang.Class, java.lang.String)
	 */
	public void rejectValue(String field, String errorCode,
			String[] rejectvalue, Class fieldtype, String defaultMessage) {
		rejectValueWithErrorArgs( field, errorCode, null,rejectvalue,fieldtype,  defaultMessage);
		
	}

	public void addError(ObjectError error) {
		this.errors.add(error);
	}
	
	public List getAllErrors() {
		return Collections.unmodifiableList(this.errors);
	}

	public void addAllErrors(Errors errors) {
		if (!errors.getObjectName().equals(getObjectName())) {
			throw new IllegalArgumentException("Errors object needs to have same object name");
		}
		this.errors.addAll(errors.getAllErrors());
	}

	/**
	 * Resolve the given error code into message codes.
	 * Calls the MessageCodesResolver with appropriate parameters.
	 * @param errorCode the error code to resolve into message codes
	 * @return the resolved message codes
	 * @see #setMessageCodesResolver
	 */
	public String[] resolveMessageCodes(String errorCode) {
		return getMessageCodesResolver().resolveMessageCodes(errorCode, getObjectName());
	}

	public String[] resolveMessageCodes(String errorCode, String field,Class fieldType) {
		String fixedField = fixedField(field);
//		Class fieldType = getFieldType(fixedField);
		return getMessageCodesResolver().resolveMessageCodes(errorCode, getObjectName(), fixedField, fieldType);
	}


	public boolean hasErrors() {
		return !this.errors.isEmpty();
	}

	public int getErrorCount() {
		return this.errors.size();
	}

	

	public List getGlobalErrors() {
		List result = new LinkedList();
		for (Iterator it = this.errors.iterator(); it.hasNext();) {
			Object error = it.next();
			if (!(error instanceof FieldError)) {
				result.add(error);
			}
		}
		return Collections.unmodifiableList(result);
	}

//	public ObjectError getGlobalError() {
//		for (Iterator it = this.errors.iterator(); it.hasNext();) {
//			ObjectError objectError = (ObjectError) it.next();
//			if (!(objectError instanceof FieldError)) {
//				return objectError;
//			}
//		}
//		return null;
//	}

	public List getFieldErrors() {
		List result = new LinkedList();
		for (Iterator it = this.errors.iterator(); it.hasNext();) {
			Object error = it.next();
			if (error instanceof FieldError) {
				result.add(error);
			}
		}
		return Collections.unmodifiableList(result);
	}

	public FieldError getFieldError() {
		for (Iterator it = this.errors.iterator(); it.hasNext();) {
			Object error = it.next();
			if (error instanceof FieldError) {
				return (FieldError) error;
			}
		}
		return null;
	}

	public List getFieldErrors(String field) {
		List result = new LinkedList();
		String fixedField = fixedField(field);
		for (Iterator it = this.errors.iterator(); it.hasNext();) {
			Object error = it.next();
			if (error instanceof FieldError && isMatchingFieldError(fixedField, (FieldError) error)) {
				result.add(error);
			}
		}
		return Collections.unmodifiableList(result);
	}

	public FieldError getFieldError(String field) {
		String fixedField = fixedField(field);
		for (Iterator it = this.errors.iterator(); it.hasNext();) {
			Object error = it.next();
			if (error instanceof FieldError) {
				FieldError fe = (FieldError) error;
				if (isMatchingFieldError(fixedField, fe)) {
					return fe;
				}
			}
		}
		return null;
	}
	
	/**
	 * Check whether the given FieldError matches the given field.
	 * @param field the field that we are looking up FieldErrors for
	 * @param fieldError the candidate FieldError
	 * @return whether the FieldError matches the given field
	 */
	protected boolean isMatchingFieldError(String field, FieldError fieldError) {
		return (field.equals(fieldError.getField()) ||
				(field.endsWith("*") && fieldError.getField().startsWith(field.substring(0, field.length() - 1))));
	}


	public String toString() {
		StringBuffer sb = new StringBuffer(getClass().getName());
		sb.append(": ").append(getErrorCount()).append(" errors");
		Iterator it = getAllErrors().iterator();
		while (it.hasNext()) {
			sb.append('\n').append(it.next());
		}
		return sb.toString();
	}

	public Object getFieldValue(String field) {
		FieldError fe = getFieldError(field);
		// Use rejected value in case of error, current bean property value else.
		Object value = null;
		if (fe != null) {
			value = fe.getRejectedValue();
		}
		
		// Apply formatting, but not on binding failures like type mismatches.
		if (fe == null || !fe.isBindingFailure()) {
			value = formatFieldValue(field, value);
		}
		return value;
	}

	


	//---------------------------------------------------------------------
	// Implementation of BindingResult interface
	//---------------------------------------------------------------------

	/**
	 * Return a model Map for the obtained state, exposing an Errors
	 * instance as '{@link #MODEL_KEY_PREFIX MODEL_KEY_PREFIX} + objectName'
	 * and the object itself.
	 * <p>Note that the Map is constructed every time you're calling this method.
	 * Adding things to the map and then re-calling this method will not work.
	 * <p>The attributes in the model Map returned by this method are usually
	 * included in the ModelAndView for a form view that uses 's bind tag,
	 * which needs access to the Errors instance. 's SimpleFormController
	 * will do this for you when rendering its form or success view. When
	 * building the ModelAndView yourself, you need to include the attributes
	 * from the model Map returned by this method yourself.
	 * @see #getObjectName
	 * @see #MODEL_KEY_PREFIX

	 */
	public Map getModel() {
		Map model = new HashMap(2);
		// Errors instance, even if no errors.
		model.put(MODEL_KEY_PREFIX + getObjectName(), this);
		// Mapping from name to target object.
		model.put(getObjectName(), getTarget());
		return model;
	}

//	public Object getRawFieldValue(String field) {
//		return getActualFieldValue(fixedField(field));
//	}

//	/**
//	 * This implementation delegates to the
//	 * {@link #getPropertyEditorRegistry() PropertyEditorRegistry}'s
//	 * editor lookup facility, if available.
//	 */
//	public EditorInf findEditor(String field, Class valueType) {
//		PropertyEditorRegistry editorRegistry = getPropertyEditorRegistry();
//		if (editorRegistry != null) {
//			Class valueTypeToUse = valueType;
////			if (valueTypeToUse == null) {
////				valueTypeToUse = getFieldType(field);
////			}
//			return editorRegistry.findCustomEditor(valueTypeToUse, fixedField(field));
//		}
//		else {
//			return null;
//		}
//	}
//
//	/**
//	 * This implementation returns <code>null</code>.
//	 */
//	public PropertyEditorRegistry getPropertyEditorRegistry() {
//		return null;
//	}

//	/**
//	 * Mark the specified disallowed field as suppressed.
//	 * <p>The data binder invokes this for each field value that was
//	 * detected to target a disallowed field.
//	 * @see DataBinder#setAllowedFields
//	 */
//	public void recordSuppressedField(String field) {
//		this.suppressedFields.add(field);
//	}

//	/**
//	 * Return the list of fields that were suppressed during the bind process.
//	 * <p>Can be used to determine whether any field values were targetting
//	 * disallowed fields.
//	 * @see DataBinder#setAllowedFields
//	 */
//	public String[] getSuppressedFields() {
//		return StringUtil.toStringArray(this.suppressedFields);
//	}


	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof BindingResult)) {
			return false;
		}
		BindingResult otherResult = (BindingResult) other;
		return (getObjectName().equals(otherResult.getObjectName()) &&
				getTarget().equals(otherResult.getTarget()) &&
				getAllErrors().equals(otherResult.getAllErrors()));
	}

	public int hashCode() {
		return getObjectName().hashCode() * 29 + getTarget().hashCode();
	}


	//---------------------------------------------------------------------
	// Template methods to be implemented/overridden by subclasses
	//---------------------------------------------------------------------

	/**
	 * Return the wrapped target object.
	 */
	public  Object getTarget()
	{
		return this.target;
	}

	
	/**
	 * Format the given value for the specified field.
	 * <p>The default implementation simply returns the field value as-is.
	 * @param field the field to check
	 * @param value the value of the field (either a rejected value
	 * other than from a binding error, or an actual field value)
	 * @return the formatted value
	 */
	protected Object formatFieldValue(String field, Object value) {
		return value;
	}
	public void addError(FieldError error) {
		this.addError((ObjectError)error);
		
	}
	
	
	public boolean hasGlobalErrors() {
		return (getGlobalErrorCount() > 0);
	}

	public int getGlobalErrorCount() {
		return getGlobalErrors().size();
	}

	public ObjectError getGlobalError() {
		List globalErrors = getGlobalErrors();
		return (!globalErrors.isEmpty() ? (ObjectError) globalErrors.get(0) : null);
	}

	public boolean hasFieldErrors() {
		return (getFieldErrorCount() > 0);
	}

	public int getFieldErrorCount() {
		return getFieldErrors().size();
	}

//	public FieldError getFieldError() {
//		List fieldErrors = getFieldErrors();
//		return (!fieldErrors.isEmpty() ? (FieldError) fieldErrors.get(0) : null);
//	}

	public boolean hasFieldErrors(String field) {
		return (getFieldErrorCount(field) > 0);
	}

	public int getFieldErrorCount(String field) {
		return getFieldErrors(field).size();
	}

//	public List getFieldErrors(String field) {
//		List fieldErrors = getFieldErrors();
//		List result = new LinkedList();
//		String fixedField = fixedField(field);
//		for (Iterator it = fieldErrors.iterator(); it.hasNext();) {
//			Object error = it.next();
//			if (isMatchingFieldError(fixedField, (FieldError) error)) {
//				result.add(error);
//			}
//		}
//		return Collections.unmodifiableList(result);
//	}

//	public FieldError getFieldError(String field) {
//		List fieldErrors = getFieldErrors(field);
//		return (!fieldErrors.isEmpty() ? (FieldError) fieldErrors.get(0) : null);
//	}

	public void rejectValue(String field, String errorCode) {
		rejectValue( field, errorCode,
				null);
		
	}
	public void rejectValue(String field, String errorCode, String defaultMessage) {
		rejectValueWithErrorArgs(field, errorCode, (Object[])null, defaultMessage);
	}
	public void rejectWithErrorArgs(String errorCode, Object[] errorArgs, String defaultMessage) {
//		addError(new ObjectError(getObjectName(), resolveMessageCodes(errorCode), errorArgs, defaultMessage));
		addError(new ObjectError(getObjectName(),new String[]{errorCode}, errorArgs, defaultMessage));
	}
	public void reject(String errorCode) {
		reject( errorCode,  null);
		
	}
	
	public void rejectWithErrorArgs(String errorCode, Object[] errorArgs) {
//		addError(new ObjectError(getObjectName(), resolveMessageCodes(errorCode), errorArgs, defaultMessage));
		addError(new ObjectError(getObjectName(),new String[]{errorCode}, errorArgs, null));
	}
	public void reject(String errorCode, String defaultMessage) {
		rejectWithErrorArgs( errorCode, (Object[] )null, defaultMessage);
		
	}
	

}
