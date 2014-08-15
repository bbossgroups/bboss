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


/**
 * <p>Title: Validator.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-10-12
 * @author biaoping.yin
 * @version 1.0
 */
public interface Validator {
	/**
	 * Can this {@link Validator} {@link #validate(Object, Errors) validate}
	 * instances of the supplied <code>clazz</code>?
	 * <p>This method is <i>typically</i> implemented like so:
	 * <pre class="code">return Foo.class.isAssignableFrom(clazz);</pre>
	 * (Where <code>Foo</code> is the class (or superclass) of the actual
	 * object instance that is to be {@link #validate(Object, Errors) validated}.)
	 * @param clazz the {@link Class} that this {@link Validator} is
	 * being asked if it can {@link #validate(Object, Errors) validate}
	 * @return <code>true</code> if this {@link Validator} can indeed
	 * {@link #validate(Object, Errors) validate} instances of the
	 * supplied <code>clazz</code> 
	 */
	boolean supports(Class clazz);
	
	/**
	 * Validate the supplied <code>target</code> object, which must be
	 * of a {@link Class} for which the {@link #supports(Class)} method
	 * typically has (or would) return <code>true</code>.
	 * <p>The supplied {@link Errors errors} instance can be used to report
	 * any resulting validation errors.
	 * @param target the object that is to be validated (can be <code>null</code>) 
	 * @param errors contextual state about the validation process (never <code>null</code>) 
	 * @see ValidationUtils
	 */
	void validate(Object target, Errors errors);

}
