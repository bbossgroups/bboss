/*
 *  Copyright 2008 biaoping.yin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.  
 */
package org.frameworkset.thread;



import org.apache.log4j.Logger;

/**
 * 
 * <p>
 * Title: WaitPolicy.java
 * </p>
 * 
 * <p>
 * Description: 如果缓冲队列已经满，则允许系统等待1秒钟， 然后重新执行直到被重新执行为止
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2009
 * </p>
 * 
 * <p>
 * bboss workgroup
 * </p>
 * 
 * @Date May 17, 2009
 * @author biaoping.yin
 * @version 1.0
 */
public class WaitPolicy implements RejectedExecutionHandler
{
    private static Logger log = Logger.getLogger(WaitPolicy.class);
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor)
    {

        if (!executor.isShutdown())
        {
            if(r instanceof RejectTask)
            {
                RejectTask reject = (RejectTask)r;
                if(reject.isStopORInterrupted())
                    return;
                reject.setReject();
                reject.increamentRejecttimes();
            }
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
                    WaitFailHandler waitFailHandler = t_.getWaitFailHandler();
                    if(waitFailHandler != null )
                    {
                        try
                        {
                            waitFailHandler.failhandler(r);
                        }
                        catch(Exception e1)
                        {
                            log.debug("waitFailHandler failed:" + e.getMessage(),e);
                        }
                    }
                    
                    log.debug("RejectedExecutionHandler failed:Do nothing and discard this task.",e);
                    return;
                }

            }
            if(waitTime < 0)
            {
                waitTime = 1000;
                
            }
            synchronized (r)
            {
                try
                {
                    r.wait(waitTime);
                }
                catch (InterruptedException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
               
            }
            executor.execute(r);

            
        }

    }

}
