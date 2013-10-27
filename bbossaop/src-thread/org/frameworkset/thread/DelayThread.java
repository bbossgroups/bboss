package org.frameworkset.thread;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.frameworkset.thread.ThreadPoolManagerFactory.WaitParam;

/**
 * 
 * <p>Title: DelayThread.java</p>
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
public abstract class DelayThread implements RejectTask
{
    private static final Logger log = Logger.getLogger(DelayThread.class);
//    protected long initdelayTime = 1 * 1000;
//    protected String delayTime_s ;
//   
//    
//    protected int maxWaits = -1;
//    private boolean isPercent = false;
//    private long delayTime = 10;
//    protected int current = 0;
//    protected long maxdelayTime = 20;
//    protected boolean needdelay;
//    
//    protected WaitFailHandler waitFailHandler;
    WaitParam waitParam;
    public WaitFailHandler getWaitFailHandler()
    {
        return waitParam.getWaitFailHandler();
    }

    public DelayThread()
    {
        
    }
    
    public void increamentRejecttimes()
    {
        
    }
    
//    public void init(ProMap poolparams,TimeUnit timeunit)
//    {
//        if(this.inited)
//            return ;
////        needdelay = poolparams.getBoolean("needdelay", false);
//        if(timeunit == null)
//            timeunit = TimeUnit.SECONDS;
//        delayTime_s = poolparams.getString("delayTime");
//        needdelay = delayTime_s != null && !delayTime_s.equals("");  
//        if(needdelay)
//        {
//            
//            try
//            {
//                if(this.delayTime_s.endsWith("%"))
//                {
//                    this.isPercent = true;        
//                    this.delayTime  = Integer.parseInt(this.delayTime_s.substring(0,delayTime_s.length() - 1));
//                }
//                else
//                {
//                    this.delayTime  = Integer.parseInt(this.delayTime_s);
//                    delayTime = TimeUnit.MILLISECONDS.convert(delayTime, timeunit);
//                }
//            }
//            catch(Exception e)
//            {
//                throw new ThreadException("build delay thread failed: delayTime=" + delayTime,e);
//            }
//        }
//        initdelayTime = poolparams.getInt("waitTime",1);
//        initdelayTime = TimeUnit.MILLISECONDS.convert(initdelayTime, timeunit);
//        maxdelayTime = poolparams.getInt("maxdelayTime",4);
//        maxdelayTime = TimeUnit.MILLISECONDS.convert(maxdelayTime, timeunit);
//        
//         
//        maxWaits = poolparams.getInt("maxWaits",-1);
////        if(maxWaits > 0)
//        {
//            waitFailHandlerClass = poolparams.getString("waitFailHandler");
//            if(waitFailHandlerClass != null)
//                try
//                {
//                    waitFailHandler = (WaitFailHandler) Class.forName(waitFailHandlerClass).newInstance();
//                }
//                catch (InstantiationException e)
//                {
//                    log.error(e);
////                    e.printStackTrace();
//                }
//                catch (IllegalAccessException e)
//                {
//                    log.error(e);
//                }
//                catch (ClassNotFoundException e)
//                {
//                    log.error(e);
//                }
//        }
//        System.out.println("initdelayTime:" +initdelayTime);
//        System.out.println("delayTime:" +delayTime_s);
//        System.out.println("current:" +current);
//        System.out.println("maxdelayTime:" +this.maxdelayTime);
//        System.out.println("maxWaits:" +maxWaits);
//        this.inited  = true;
//        
//    }
//    public DelayThread(String delayTime_, long initdelayTime, int maxWaits,TimeUnit timeunit,String waitFailHandlerClass_)
//    {
//        super();
//        delayTime_s = delayTime_;
//        needdelay = delayTime_s != null && !delayTime_s.equals("");
//        if(needdelay)
//        {
//            
//            try
//            {
//                if(this.delayTime_s.endsWith("%"))
//                {
//                    this.isPercent = true;        
//                    this.delayTime  = Integer.parseInt(this.delayTime_s.substring(0,delayTime_s.length() - 1));
//                }
//                else
//                {
//                    this.delayTime  = Integer.parseInt(this.delayTime_s);
//                    delayTime = TimeUnit.MILLISECONDS.convert(delayTime, timeunit);
//                }
//            }
//            catch(Exception e)
//            {
//                throw new ThreadException("build delay thread failed: delayTime=" + delayTime,e);
//            }
//        }
//        this.initdelayTime = initdelayTime;
//        this.maxWaits = maxWaits;
////        if(maxWaits > 0)
//        {
//            waitFailHandlerClass = waitFailHandlerClass_;
//            if(waitFailHandlerClass != null)
//                try
//                {
//                    waitFailHandler = (WaitFailHandler) Class.forName(waitFailHandlerClass).newInstance();
//                }
//                catch (InstantiationException e)
//                {
//                    log.error(e);
////                    e.printStackTrace();
//                }
//                catch (IllegalAccessException e)
//                {
//                    log.error(e);
//                }
//                catch (ClassNotFoundException e)
//                {
//                    log.error(e);
//                }
//        }
//         
//    } 
    
    
    
    public long getWaittime() throws ThreadException
    {
        int rejecttimes = this.getRejectTimes();
        if(waitParam.maxWaits > 0)
        {
            
            if(rejecttimes >= waitParam.maxWaits )
                throw new ThreadException("Exceed maxtimes[maxTimes=" + waitParam.maxWaits + "]");
        }
//        else
//        {
//            return -1;
//        }
        if(rejecttimes > 0 
                && waitParam.initdelayTime < waitParam.maxdelayTime && 
                waitParam.initdelayTime < waitParam.maxdelayTime)
        {
            if(waitParam.needdelay)
            {
                if(waitParam.isPercent)
                {
                    
                    waitParam.initdelayTime += java.lang.Math.round(waitParam.initdelayTime * waitParam.delayTime * 0.01);  
                }
                else
                {
                    waitParam.initdelayTime += waitParam.delayTime;
                }
            }
            
        }        
        if(waitParam.initdelayTime < waitParam.maxdelayTime)
            return waitParam.initdelayTime;
        else
            return waitParam.maxdelayTime;
        
    }
    
    public static void main(String[] args)
    {
        long o = 10l;
//        System.out.println("TimeUnit.MICROSECONDS:"+TimeUnit.SECONDS.convert(o, TimeUnit.MICROSECONDS));
//        System.out.println("TimeUnit.MILLISECONDS:"+TimeUnit.MILLISECONDS.convert(o, TimeUnit.MILLISECONDS));
//        System.out.println("TimeUnit.NANOSECONDS:"+TimeUnit.SECONDS.convert(o, TimeUnit.NANOSECONDS));
        System.out.println("TimeUnit.MILLISECONDS:"+TimeUnit.MILLISECONDS.convert(o, TimeUnit.SECONDS));
        long initdelayTime = 1000;
        long t = java.lang.Math.round(initdelayTime * 10 * 0.01);
        System.out.println(t);
        initdelayTime =initdelayTime + t;
        System.out.println(initdelayTime);
    }

    public void init(WaitParam waitParam)
    {
        this.waitParam = waitParam;
        
    }
    /**
     * 获取被拒绝的次数
     */
    public int getRejectTimes()
    {
        return 0;
    }
    
    public boolean isStopORInterrupted()
    {
        return false;
    }

}
