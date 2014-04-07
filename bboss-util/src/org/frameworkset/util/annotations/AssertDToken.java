/*
 *  Copyright 2008 bbossgroups
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
 * <p>Title: DToken.java</p> 
 * <p>Description:用来标注mvc控制器方法强制要求进行动态令牌校验，如果客户端请求没有附带令牌或者令牌已经作废，那么直接拒绝
 * 请求 
 * 
 * 防止跨站请求过滤器相关参数 bboss防止跨站请求过滤器的机制如下： 采用动态令牌和session相结合的方式产生客户端令牌，一次请求产生一个唯一令牌 
			令牌识别采用客户端令牌和服务端session标识混合的方式进行判别，如果客户端令牌和服务端令牌正确匹配，则允许访问，否则认为用户为非法用户并阻止用户访问并跳转到 
			redirectpath参数对应的地址，默认为/login.jsp。 令牌存储机制通过参数tokenstore指定，包括两种，内存存储和session存储，默认为session存储，当令牌失效（匹配后立即失效，或者超时失效）后，系统自动清除失效的令牌；采用session方式 
			存储令牌时，如果客户端页面没有启用session，那么令牌还是会存储在内存中。 令牌生命周期：客户端的令牌在服务器端留有存根，当令牌失效（匹配后立即失效，或者超时失效）后，系统自动清除失效的令牌； 
			当客户端并没有正确提交请求，会导致服务端令牌存根变为垃圾令牌，需要定时清除这些 垃圾令牌；如果令牌存储在session中，那么令牌的生命周期和session的生命周期保持一致，无需额外清除机制； 
			如果令牌存储在内存中，那么令牌的清除由令牌管理组件自己定时扫描清除，定时扫描时间间隔为由tokenscaninterval参数指定，单位为毫秒，默认为30分钟，存根保存时间由tokendualtime参数指定，默认为1个小时 
			可以通过enableToken参数配置指定是否启用令牌检测机制，true检测，false不检测，默认为false不检测 enableToken是否启用令牌检测机制，true 
			启用，false 不启用，默认为不启用</p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2012-8-24
 * @author biaoping.yin
 * @version 3.6
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AssertDToken {
//	String appid() default "appid";
//	String secret() default "secret";
//	String token() default "_dt_token_";
//	String tokentype() default "tt";
}
