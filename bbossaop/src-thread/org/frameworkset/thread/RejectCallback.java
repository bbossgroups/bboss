package org.frameworkset.thread;
/** 
 * <p>类说明:</p>
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: bboss group</p>
 * @author  gao.tang ,biaoping.yin
 * @version V1.0  创建时间：Sep 11, 2009 6:02:25 PM 
 */
public abstract class RejectCallback {
    /**用于线程调度时延时采用*/
    int rejecttimes = 0;
	/**
     * 获取被拒绝的次数
     */
    public int getRejectTimes()
    {
        /**用于线程调度时延时采用*/
        return rejecttimes;
    }
    public int increamentRejecttimes()
    {
        rejecttimes ++ ;
        return rejecttimes;
    }

}
