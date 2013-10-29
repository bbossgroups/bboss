package org.frameworkset.util.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.frameworkset.util.annotations.ValueConstants;

/**
 * 分页注解，标注参数用来设置分页数据
 * 本注解只能用于注解控制器方法参数
 * @author biaoping.yin
 *
 */
@Target( ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PagerParam {
	
	public static final String DEFAULT_ID = "pager";
	public static final String OFFSET = "offset";
	public static final String DESC = "desc";
	public static final String SORT = "sortKey";
	public static final String PAGE_SIZE = "PAGE_SIZE";
	
	String id() default DEFAULT_ID;
	/**
	 * 分页参数名称
	 * @return
	 */
	String name() ;	
	boolean required() default false;
	String editor() default "";
	String defaultvalue() default ValueConstants.DEFAULT_NONE;
}
