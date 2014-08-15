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

package org.frameworkset.spi.support.bind;

import org.frameworkset.spi.support.DefaultMessageSourceResolvable;
import org.frameworkset.spi.support.validate.BindingResult;
import org.frameworkset.spi.support.validate.Errors;
import org.frameworkset.spi.support.validate.FieldError;
import org.frameworkset.util.beans.PropertyAccessException;



/**
 * <p>Title: DefaultBindingErrorProcessor.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-10-15 下午04:02:34
 * @author biaoping.yin
 * @version 1.0
 */
public class DefaultBindingErrorProcessor  implements BindingErrorProcessor {

	/**
	 * Error code that a missing field error (i.e. a required field not
	 * found in the list of property values) will be registered with:
	 * "required".
	 */
	public static final String MISSING_FIELD_ERROR_CODE = "required";


	public void processMissingFieldError(String missingField, BindingResult bindingResult) {
		// Create field error with code "required".
		String fixedField = bindingResult.getNestedPath() + missingField;
		String[] codes = bindingResult.resolveMessageCodes(MISSING_FIELD_ERROR_CODE, missingField,null);
		Object[] arguments = getArgumentsForBindError(bindingResult.getObjectName(), fixedField);
		bindingResult.addError(new FieldError(
				bindingResult.getObjectName(), fixedField, "", null,true,
				codes, arguments, "Field '" + fixedField + "' is required"));
	}

	public void processPropertyAccessException(PropertyAccessException ex, BindingResult bindingResult) {
		// Create field error with the exceptions's code, e.g. "typeMismatch".
		String field = ex.getPropertyChangeEvent().getPropertyName();
		Object value = ex.getPropertyChangeEvent().getNewValue();
		String[] codes = bindingResult.resolveMessageCodes(ex.getErrorCode(), field,null);
		Object[] arguments = getArgumentsForBindError(bindingResult.getObjectName(), field);
		bindingResult.addError(new FieldError(
				bindingResult.getObjectName(), field, value, null,true,
				codes, arguments, ex.getLocalizedMessage()));
	}

	/**
	 * Return FieldError arguments for a binding error on the given field.
	 * Invoked for each missing required fields and each type mismatch.
	 * <p>Default implementation returns a DefaultMessageSourceResolvable
	 * with "objectName.field" and "field" as codes.
	 * @param field the field that caused the binding error
	 * @return the Object array that represents the FieldError arguments

	 */
	protected Object[] getArgumentsForBindError(String objectName, String field) {
		String[] codes = new String[] {objectName + Errors.NESTED_PATH_SEPARATOR + field, field};
		String defaultMessage = field;
		return new Object[] {new DefaultMessageSourceResolvable(codes, defaultMessage)};
	}

}
