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
 * <p>Description: es批量处理、文档添加时指定对应的索引名称和类型，对应于多type的处理，也用于动态index名称的批量处理</p>
 * <p></p>
 * <p>Copyright (c) 2018</p>
 * @Date 2019/5/5 22:09
 * @author biaoping.yin
 * @version 1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ESIndex {
	String name() ;
	String type() default "";
	boolean useBatchContextIndexName() default false;
	boolean useBatchContextIndexType() default false;
}
