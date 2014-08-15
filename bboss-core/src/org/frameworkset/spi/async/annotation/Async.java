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

package org.frameworkset.spi.async.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Title: Async.java</p> 
 * <p>Description: 用来支持服务组件方法的异步调用机制
 * 1.采用异步调用，但是结果通过回调的方式返回给调用端
   2、不需要等待结果的的异步调用
   3、需要等待结果，但是指定等待超时时间，一旦timeout就报超时异常
 * </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2011-4-20 下午02:21:50
 * @author biaoping.yin
 * @version 1.0
 */
@Target({ ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Async {
	/**
	 * 指定异步调用的超时时间，默认为-1及永久等待，直到调用方返回
	 * 大于0时，等待结果，超过指定的时间就抛出超时异常
	 * @return
	 */
	public long timeout() default -1;
	/**
	 * 采用异步调用，但是结果通过回调的方式返回给调用端
	 * @return
	 */
	public String callback() default Constants.NULLCALLBACK;
	/**
	 * 是否需要返回调用结果，默认不返回，主线程继续往前走
	 * 如果需要返回则，根据timeout和callback两个参数来决定
	 * 返回结果的等待处理模式：
	 * 
	 * 当timeout > 0 则等待特定的时间来来获取结果，超过指定的时间后就抛超时异常，等待超时的模式又分为两种情况：
	 * 如果指定了回调函数，不阻塞主程序，将结果交给回调函数来处理
	 * 如果没有指定回调函数则阻塞主程序，将结果交给主程序来处理
	 * 
	 * 当timeout <= 0 时，则永久等待结果，直到结果返回，这种模式也分两种情况：
	 * 如果指定了回调函数 则不阻塞主程序，
	 * 如果没有指定回调函数，则阻塞主程序，直到结果返回来
	 * @return
	 */
	public Result result() default Result.NO;	
}
