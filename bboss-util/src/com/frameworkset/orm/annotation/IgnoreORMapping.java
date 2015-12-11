package com.frameworkset.orm.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
/**
 * 查询时，忽略bean属性参数绑定
 * 
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2014-12-02
 * @author biaoping.yin
 * @version 1.0
 */
public @interface IgnoreORMapping {

}
