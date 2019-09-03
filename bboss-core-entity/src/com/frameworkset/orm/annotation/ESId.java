package com.frameworkset.orm.annotation;/*
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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface  ESId {

	/**
	 * 主键名称-对于tableinfo表中的主键信息配置
	 * @return
	 */
	String name() default "id";
	String pkname() default "";

	boolean auto() default true;

//	/**
//	 * 如果auto为true，将强制生成主键，不过主键字段的值是否已经设置，如果设置
//	 * 设置autoIfNull则只有在值为空，或者数字为0时才生成主键
//	 * @return
//	 */
//	boolean autoIfNull() default false;

	/**
	 * sequence,string,int,long
	 * @return
	 */
	String type() default "long";

	/**
	 * 用于单机单节点环境主键生成，与id属性配合使用
	 * 表名称
	 * @return
	 */
	String tableName() default "";
	/**
	 * 用于单机单节点环境主键生成，与tableName属性配合使用
	 * 表的物理主键名称
	 * @return
	 */
	String id() default "";
	/**
	 * 标识是否保存注解对应的字段的值
	 * @return
	 */
	boolean persistent() default true;

	/**
	 * 查询/检索文档时，是否将文档id设置给对应被注解的属性
	 * @return
	 */
	boolean readSet() default true;
}
