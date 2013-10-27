package org.frameworkset.thread;


/**
 * 
 * <p>Title: WaitFailHandler.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright (c) 2009</p>
 *
 * <p>bboss workgroup</p>
 * @Date 2009-7-22
 * @author biaoping.yin
 * @version 1.0
 */
public interface WaitFailHandler<T>
{
    /**
     * 等待重试失败后，对被丢弃的任务执行特殊处理方法
     * @param r
     */
    public void failhandler(T r);
    
}
