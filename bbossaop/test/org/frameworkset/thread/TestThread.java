package org.frameworkset.thread;



public class TestThread
{
    static int i = 0;
    public static void testWaitPolicy()
    {
        ThreadPoolExecutor executer = ThreadPoolManagerFactory.getThreadPoolExecutor("event.threadpool");
        for(int i = 0; i < 50 ; i ++)
            executer.execute(new WaitRun());
        
    }
    
    public static void testCallerPolicy()
    {
        ThreadPoolExecutor executer = ThreadPoolManagerFactory.getThreadPoolExecutor("event.threadpool-caller");
        for(int i = 0; i < 50 ; i ++)
            executer.execute(new Run());
        
    }
    
    public static void testDiscardOldestPolicy()
    {
        ThreadPoolExecutor executer = ThreadPoolManagerFactory.getThreadPoolExecutor("event.threadpool-DiscardOldestPolicy");
        for(int i = 0; i < 50 ; i ++)
            executer.execute(new Run());
        
    }
    
    public static void testDiscardPolicy()
    {
        ThreadPoolExecutor executer = ThreadPoolManagerFactory.getThreadPoolExecutor("event.threadpool-DiscardPolicy");
        for(int i = 0; i < 50 ; i ++)
            executer.execute(new Run());
        
    }
    public static void testDiscardAbortPolicy()
    {
        ThreadPoolExecutor executer = ThreadPoolManagerFactory.getThreadPoolExecutor("event.threadpool-AbortPolicy");
        for(int i = 0; i < 50 ; i ++)
            executer.execute(new Run());
        
    }
    
    
    
    
    public static void testWaitPolicynodelay()
    {
        ThreadPoolExecutor executer = ThreadPoolManagerFactory.getThreadPoolExecutor("event.threadpool-nodelay");
        for(int i = 0; i < 50 ; i ++)
            executer.execute(new WaitRun());
        
        
    }
    public static void testWaitPolicynomaxwait()
    {
        ThreadPoolExecutor executer = ThreadPoolManagerFactory.getThreadPoolExecutor("event.threadpool-nomaxwait");
        for(int i = 0; i < 50 ; i ++)
            executer.execute(new WaitRun());
        
    }
    
    public static void testDefaultPool()
    {
        ThreadPoolExecutor executer = ThreadPoolManagerFactory.getThreadPoolExecutor("defaultpool");
        
        for(int i = 0; i < 2 ; i ++)
            executer.execute(new WaitRun());
        
    }

    public static void main(String args[])
    {
//        System.out.println(ThreadPoolManagerFactory.getThreadPoolExecutor("event.threadpool"));
//        testWaitPolicy();
        testDefaultPool();
        System.exit(1);
//        System.out.println(Math.cos(0));
//        System.out.println(Math.sin(a)cos(0));
//        testWaitPolicynodelay();
//        testCallerPolicy();
//        testDiscardPolicy();
//        testDiscardOldestPolicy();
//        java.util.concurrent.ThreadPoolExecutor.DiscardOldestPolicy s;
//        java.util.concurrent.ThreadPoolExecutor.DiscardOldestPolicy s1;
    }

    public static class WaitRun extends DelayThread
    {

        public void run()
        {
            if (true)
            {
                i ++;
                System.out.println("run:"+ i);
                try
                {
                    synchronized(this)
                    {
                        this.wait(40000);
                    }
                }
                catch (InterruptedException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }

        public void setReject()
        {
            // TODO Auto-generated method stub
            
        }

    }
    
    
    public static class Run implements Runnable
    {

        public void run()
        {
            if (true)
            {
                i ++;
                System.out.println("run:"+ i);
                try
                {
                    synchronized(this)
                    {
                        this.wait(4000);
                    }
                }
                catch (InterruptedException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }

    }
    
    public static class WaitFailHandlerTest implements WaitFailHandler<WaitRun>
    {

        public void failhandler(WaitRun r)
        {
            System.out.println("r:" + r);
            
        }
        
    }

}
