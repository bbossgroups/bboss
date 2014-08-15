package org.frameworkset.spi;

/**
 * 
 * <p>Title: DisposableBean.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-4-21 下午11:34:34
 * @author biaoping.yin
 * @version 1.0
 * 
 */
public interface DisposableBean 
{
    /**
     * Invoked by a BeanFactory on destruction of a singleton.
     * @throws Exception in case of shutdown errors.
     * Exceptions will get logged but not rethrown to allow
     * other beans to release their resources too.
     */
    public void destroy() throws Exception;
}
