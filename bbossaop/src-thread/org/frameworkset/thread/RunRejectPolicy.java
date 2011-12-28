package org.frameworkset.thread;

import org.frameworkset.thread.ThreadPoolExecutor.CallerRunsPolicy;





/**
 * 
 * <p>Title: RunRejectPolicy.java</p>
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
public class RunRejectPolicy extends CallerRunsPolicy
{
    public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
        if (r instanceof RejectTask)
        {
            RejectTask task = (RejectTask) r;
            if(task.isStopORInterrupted())
                return;
            task.setReject();
            task.increamentRejecttimes();
        }
        if (!e.isShutdown()) {
            long waitTime = 1000;
            if (r instanceof DelayThread)
            {
                DelayThread t_ = (DelayThread) r;
                try
                {       
                    waitTime = t_.getWaittime();
                }
                catch (ThreadException err)
                {
                    waitTime = 1000;
                }

            }
            if(waitTime < 0)
            {
                waitTime = 1000;
                
            }
            synchronized(r)
            {
                try
                {
                    r.wait(waitTime);
                }
                catch (InterruptedException e1)
                {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
            r.run();
        }
    }
}
