package org.frameworkset.spi;

/**
 * 
 * <p>Title: InitializingBean.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2009-9-28 ионГ11:20:11
 * @author biaoping.yin
 * @version 1.0
 */
public interface InitializingBean {
    
    void afterPropertiesSet() throws Exception;

}