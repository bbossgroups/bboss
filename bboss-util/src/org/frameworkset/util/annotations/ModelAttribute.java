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
package org.frameworkset.util.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Title: ModelAttribute.java</p> 
 * <p>Description:Annotation that binds a method parameter or method return value
 * to a named model attribute, exposed to a web view. Supported
 * for {@link HandlerMapping} annotated handler classes.
 *
 * <p>Can be used to expose command objects to a web view, using
 * specific attribute names, through annotating corresponding
 * parameters of a {@link HandlerMapping} annotated handler method).
 *
 * <p>Can also be used to expose reference data to a web view
 * through annotating accessor methods in a controller class which
 * is based on {@link HandlerMapping} annotated handler methods,
 * with such accessor methods allowed to have any arguments that
 * {@link HandlerMapping} supports for handler methods, returning
 * the model attribute value to expose. </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-10-24
 * @author biaoping.yin
 * @version 1.0
 */

@Target({ElementType.PARAMETER,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ModelAttribute {

	/**
	 * The name of the model attribute to bind to.
	 * <p>The default model attribute name is inferred from the declared
	 * attribute type (i.e. the method parameter type or method return type),
	 * based on the non-qualified class name:
	 * e.g. "orderAddress" for class "mypackage.OrderAddress",
	 * or "orderAddressList" for "List&lt;mypackage.OrderAddress&gt;".
	 */
	String name() default "";
	String editor() default "";
	boolean required() default false;
	

}
