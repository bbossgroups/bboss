package org.frameworkset.thread;

/**
 * 
 * <p>Title: RejectTask.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright (c) 2009</p>
 *
 * <p>bboss workgroup</p>
 * @Date 2009-9-7
 * @author biaoping.yin
 * @version 1.0
 */
public interface RejectTask extends Runnable
{
    public void setReject();
    
    /**
     * 获取被拒绝的次数
     */
    public int getRejectTimes();
    public void increamentRejecttimes();
    public boolean isStopORInterrupted();
}
