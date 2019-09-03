package com.frameworkset.orm.annotation;
/**
 * Copyright 2008 biaoping.yin
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Description: 存放检索文档对应的index field meta信息
 *  Map<String,List<Object>>类型
 * </p>
 * <p></p>
 * <p>Copyright (c) 2018</p>
 * @Date 2019/8/8 22:27
 * @author biaoping.yin
 * @version 1.0
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ESMetaFields {
	String name() default "fields";
	/**
	 * 查询/检索文档时，是否将文档index fileds设置给对应被注解的属性
	 * @return
	 */
	boolean readSet() default true;
}
