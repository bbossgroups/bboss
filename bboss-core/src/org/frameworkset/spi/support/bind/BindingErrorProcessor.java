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

import org.frameworkset.spi.support.validate.BindingResult;
import org.frameworkset.spi.support.validate.Errors;
import org.frameworkset.spi.support.validate.FieldError;
import org.frameworkset.spi.support.validate.MessageCodesResolver;
import org.frameworkset.spi.support.validate.ObjectError;
import org.frameworkset.util.beans.PropertyAccessException;




/**
 * <p>Title: BindingErrorProcessor.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-10-15 下午03:25:22
 * @author biaoping.yin
 * @version 1.0
 */
public interface BindingErrorProcessor {
	/**
	 * Apply the missing field error to the given BindException.
	 * <p>Usually, a field error is created for a missing required field.
	 * @param missingField the field that was missing during binding
	 * @param bindingResult the errors object to add the error(s) to.
	 * You can add more than just one error or maybe even ignore it.
	 * The <code>BindingResult</code> object features convenience utils such as
	 * a <code>resolveMessageCodes</code> method to resolve an error code.
	 * @see BeanPropertyBindingResult#addError
	 * @see BeanPropertyBindingResult#resolveMessageCodes
	 */
	void processMissingFieldError(String missingField, BindingResult bindingResult);

	/**
	 * Translate the given <code>PropertyAccessException</code> to an appropriate
	 * error registered on the given <code>Errors</code> instance.
	 * <p>Note that two error types are available: <code>FieldError</code> and
	 * <code>ObjectError</code>. Usually, field errors are created, but in certain
	 * situations one might want to create a global <code>ObjectError</code> instead.
	 * @param ex the <code>PropertyAccessException</code> to translate
	 * @param bindingResult the errors object to add the error(s) to.
	 * You can add more than just one error or maybe even ignore it.
	 * The <code>BindingResult</code> object features convenience utils such as
	 * a <code>resolveMessageCodes</code> method to resolve an error code.
	 * @see Errors
	 * @see FieldError
	 * @see ObjectError
	 * @see MessageCodesResolver
	 * @see BeanPropertyBindingResult#addError
	 * @see BeanPropertyBindingResult#resolveMessageCodes
	 */
	void processPropertyAccessException(PropertyAccessException ex, BindingResult bindingResult);

}
