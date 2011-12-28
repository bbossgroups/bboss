package org.frameworkset.thread;



import org.apache.log4j.Logger;

/**
 * 
 * <p>Title: RejectRequeuePoliecy.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright (c) 2009</p>
 *
 * <p>bboss workgroup</p>
 * @Date 2009-9-14
 * @author biaoping.yin
 * @version 1.0
 */
public class RejectRequeuePoliecy implements RejectedExecutionHandler
{
    private static Logger log = Logger.getLogger(WaitPolicy.class);
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor)
    {
        System.out.println("RejectRequeuePoliecy.rejectedExecution:" + r);
        if (r instanceof RejectTask)
        {
            RejectTask task = (RejectTask) r;
//            if(task.isStopORInterrupted())
//            {
//                System.out.println("task.isStopORInterrupted() == " + task.isStopORInterrupted()+":" + r);
//                return;
//            }
            task.setReject();
            task.increamentRejecttimes();
        }
        
        if (!executor.isShutdown()) {
            long waitTime = 1000;
            if (r instanceof DelayThread)
            {
                DelayThread t_ = (DelayThread) r;
                try
                {       
                    waitTime = t_.getWaittime();
                }
                catch (ThreadException e)
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
            executor.execute(r);
        }
        else
        {
            System.out.println("executor.isShutdown() == "  + r);
        }
        
    }

}
