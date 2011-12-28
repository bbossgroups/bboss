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
 * <p>Title: RequestHeader.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-11-21
 * @author biaoping.yin
 * @version 1.0
 */
@Target({ElementType.PARAMETER,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestHeader {

	/**
	 * The name of the request header to bind to.
	 */
	String name() default "";

	/**
	 * Whether the header is required.
	 * <p>Default is <code>true</code>, leading to an exception thrown in case
	 * of the header missing in the request. Switch this to <code>false</code>
	 * if you prefer a <code>null</value> in case of the header missing.
	 * <p>Alternatively, provide a {@link #defaultValue() defaultValue},
	 * which implicitely sets this flag to <code>false</code>.
	 */
	boolean required() default true;

	/**
	 * The default value to use as a fallback. Supplying a default value implicitely
	 * sets {@link #required()} to false.
	 */
	String defaultvalue() default ValueConstants.DEFAULT_NONE;

	String  editor() default "";

	String dateformat()default ValueConstants.DEFAULT_NONE;

}
