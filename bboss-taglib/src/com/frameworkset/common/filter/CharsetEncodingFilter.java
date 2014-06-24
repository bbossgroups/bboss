package com.frameworkset.common.filter;


/**
 *带会话共享功能的字符串过滤器
 * 配置示例：
 * <filter>
    	<filter-name>CharsetEncoding</filter-name>
    	<filter-class>com.frameworkset.platform.oa.meeting.util.CharsetEncodingFilter</filter-class>
    	<init-param>
      		<param-name>RequestEncoding</param-name>
      		<param-value>iso-8859-1</param-value>
    	</init-param>
    	<init-param>
      		<param-name>ResponseEncoding</param-name>
      		<param-value>UTF-8</param-value>
    	</init-param>
  	</filter>

  	<filter-mapping>
  		<filter-name>CharsetEncoding</filter-name>
  		<url-pattern>/*</url-pattern>
  	</filter-mapping>
 * @author biaoping.yin
 * created on 2005-6-14
 * version 1.0
 */

public class CharsetEncodingFilter extends SimpleCharsetEncodingFilter {

}
