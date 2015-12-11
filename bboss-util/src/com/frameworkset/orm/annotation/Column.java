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
package com.frameworkset.orm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.frameworkset.util.annotations.ValueConstants;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public  @interface Column {
	String dataformat() default ValueConstants.DEFAULT_NONE;
	String editorparams() default ValueConstants.DEFAULT_NONE;
	String name() default "";
	String type() default ValueConstants.DEFAULT_NONE;
	String charset() default ValueConstants.DEFAULT_NONE;
	String editor() default ValueConstants.DEFAULT_NONE;
	/**
	 * 忽略创建、修改、删除时属性值绑定
	 * @return
	 */
	boolean ignoreCUDbind() default false;
	/**
	 * 忽略增删改查o/r mapping机制，相当于@IgnoreORMapp 和@Column(ignoreCUDbind=true)组合
	 * @return
	 */
	boolean ignorebind() default false;

}
