package org.frameworkset.spi.assemble;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.frameworkset.spi.ProviderInterceptor;
import org.frameworkset.spi.UNmodify;
import org.frameworkset.spi.interceptor.DummyInterceptorFacttory;
import org.frameworkset.spi.interceptor.DumyInterceptor;
import org.frameworkset.spi.interceptor.InterceptorChain;
import org.frameworkset.spi.interceptor.InterceptorFacttory;
import org.frameworkset.spi.interceptor.InterceptorWrapper;

import com.frameworkset.proxy.Interceptor;

public abstract class BaseTXManager implements java.io.Serializable,UNmodify
{
    private static Logger log = Logger.getLogger(ProviderManagerInfo.class);
    private static InterceptorFacttory interceptorFacttory;
    static
    {
    	try {
			interceptorFacttory = (InterceptorFacttory) Class.forName("org.frameworkset.spi.interceptor.InterceptorFacttoryImpl").newInstance();
		} catch (Exception e) {
			log.warn("class org.frameworkset.spi.interceptor.InterceptorFacttoryImpl not found in classpath,use DummyInterceptorFacttory. ");
			interceptorFacttory = new DummyInterceptorFacttory();
			
		}
    }
    protected Transactions txs;
    
    
    protected List<InterceptorInfo> interceptors = new ArrayList<InterceptorInfo>();

    protected boolean callorder_sequence = false;
    protected AOPMethods asyncMethods;

    /**
     * 用户是否自己定义了拦截器
     */
    protected boolean usedCustomInterceptor = false;

    protected String transactionInterceptorClass;

    public void setTransactions(Transactions txs)
    {

        this.txs = txs;

    }
    public SynchronizedMethod isTransactionMethod(Method method,String muuid)
    {   
        this.loadTXAnnotation();
        if(txs == null)
                return null;
        return txs.isTransactionMethod(method,muuid);
    }
    
    
    
    public SynchronizedMethod isAsyncMethod(Method method,String muuid)
    {   
        this.loadAsyncAnnotation();
        if(this.asyncMethods == null)
                return null;
        return this.asyncMethods.isAsyncMethod(method,muuid);
    }
    
    
	
	private Object lock_ = new Object();
    protected void loadAsyncAnnotation()
    {
        if(this.loadAsyncAnnotation )
            return;
        synchronized(lock_)
        {
            if(this.loadAsyncAnnotation )
                return;
            try
            {
                this.initAsyncMethods();
            }
            catch(Exception e)
            {
                
            }
            this.loadAsyncAnnotation = true;
        }
    }
    private boolean loadAsyncAnnotation = false; 
	 public boolean enableAsyncCall()
	    {
	        this.loadAsyncAnnotation();
	        if (this.asyncMethods != null && asyncMethods.size() > 0)
	            return true;
	        return false;
	    }
	 
	 protected void setAsyncMethods(AOPMethods asyncMethods) {
			this.asyncMethods = asyncMethods;
		}

    public Interceptor getSynTransactionInterceptor(Method method,String muuid)

    
    {
    	
        if (this.enableInterceptor())
        {
        	if(muuid == null)
        		muuid = SynchronizedMethod.buildMethodUUID(method);
            if (!callorder_sequence)
            {
                Interceptor intercptor = getChainInterceptor( method, muuid);

                if (this.enableTransaction())
                {
                    if (intercptor != null)
                    {
                    	SynchronizedMethod synmethod = txs.isTransactionMethod(method,muuid);
                    	if(synmethod == null)
                    		return intercptor;
                        Interceptor wrapInterceptor = interceptorFacttory.getInterceptor(synmethod);

                        InterceptorChain inteceptor = new InterceptorChain(wrapInterceptor, intercptor, true);
                        return inteceptor;
                    }
                    else
                    {
                    	SynchronizedMethod synmethod = txs.isTransactionMethod(method,muuid);
                    	if(synmethod == null)
                    		return null;
                        Interceptor wrapInterceptor = interceptorFacttory.getInterceptor(synmethod);


                        return wrapInterceptor;
                    }
                }
                else
                {
                    return intercptor;
                }
            }
            else
            {

                Interceptor intercptor = this.getSequenceInterceptor(method, muuid);

                return intercptor;

            }
        }
        else if (this.enableTransaction())
        {
        	SynchronizedMethod synmethod = txs.isTransactionMethod(method,muuid);
        	if(synmethod == null)
        		return null;
            return interceptorFacttory.getInterceptor(synmethod);//new TransactionInterceptor(synmethod);

        }
        return null;
    }
//	 public Interceptor getTransactionInterceptor(Method method)
//    {
//
//        if (this.enableTransaction())
//        {  
//            TransactionInterceptor wrapInterceptor = new TransactionInterceptor(this.txs,method);
//            return wrapInterceptor;
//        }
//        return null;
//    }
    /**
     * 
     * @return
     */
//    public Interceptor getInterceptorChain(Method method,String methoduuid)
//    {
//
//        if (this.interceptors != null && this.interceptors.size() > 0)
//        {
//            Interceptor intercptor = getChainInterceptor( method, methoduuid);
//            return intercptor;
//        }
//        
//        return null;
//    }
	 public Interceptor getTransactionInterceptor(Method method,String muuid)
    {

        if (this.enableTransaction())
        {  
        	SynchronizedMethod synmethod = txs.isTransactionMethod(method,muuid);
        	if(synmethod == null)
        		return null;
            Interceptor wrapInterceptor = interceptorFacttory.getInterceptor(synmethod);//new TransactionInterceptor(synmethod);
            return wrapInterceptor;
        }
        return null;
    }
	private Object InterceptorChainLock = new Object();
	private Interceptor nullintercptor = new DumyInterceptor();
	private Map<String,Interceptor> methodInterceptorMap = new HashMap<String,Interceptor>();
	
    /**
     * 
     * @return
     */
    public Interceptor getChainInterceptor(Method method,String methoduuid)
    {
    	return _getInterceptor( method, methoduuid, false);
        
        
        
    }
    private Interceptor getSequenceInterceptor(Method method,String methoduuid)
    {
    	return _getInterceptor( method, methoduuid, true);
    }
    private Object lock = new Object();
    protected void loadTXAnnotation()
    {
        if(this.loadTXAnnotation )
            return;
        synchronized(lock)
        {
            if(this.loadTXAnnotation )
                return;
            try
            {
                this.initTransactions();
            }
            catch(Exception e)
            {
                
            }
            this.loadTXAnnotation = true;
        }
    }
    private boolean loadTXAnnotation = false; 
    public boolean enableTransaction()
    {
        loadTXAnnotation();
        if (this.txs != null && txs.size() > 0)
            return true;
        return false;
    }
    
    
    protected abstract void initAsyncMethods() ;
    protected abstract void initTransactions();
    public boolean enableInterceptor()
    {
    	if(this.interceptors == null || this.interceptors.size() == 0)
    		return false;
    	return true;
    }
    private Interceptor _getInterceptor(Method method,String methoduuid,boolean sequence)
    {
    	if(!enableInterceptor())
    		return null;
    	if(methoduuid == null)
    		methoduuid = SynchronizedMethod.buildMethodUUID(method);
    	Interceptor intercptorChain = methodInterceptorMap.get(methoduuid);
    	if(intercptorChain != null)
    	{
    		if(intercptorChain == nullintercptor)
    			return null;
    		else
    		{
    			return intercptorChain;
    		}
    	}
    	    	
        
		synchronized(InterceptorChainLock)
		{
			intercptorChain = methodInterceptorMap.get(methoduuid);
	    	if(intercptorChain != null)
	    	{
	    		if(intercptorChain == nullintercptor)
	    			return null;
	    		else
	    		{
	    			return intercptorChain;
	    		}
	    	}
	    	if(!sequence)
	    		intercptorChain = _getChainInterceptor( method,methoduuid);
	    	else
	    		intercptorChain = _getSequenceInterceptor( method,methoduuid);
	    	if(intercptorChain == null)
	    		methodInterceptorMap.put(methoduuid, nullintercptor);
	    	else
	    		methodInterceptorMap.put(methoduuid, intercptorChain);
	    	
            return intercptorChain;
		}
    }
    
    private Interceptor _getSequenceInterceptor(Method method,String muuid)

    {
        loadTXAnnotation();
        if (enableInterceptor())
        {
            int size = interceptors.size();
            List _t = new ArrayList(size);
            for (int i = 0; i < size; i++)
            {

                try
                {

                    InterceptorInfo it = (InterceptorInfo) interceptors.get(i);
                    if(!it.allWillBeIntercept() || it.isInterceptMethod(method, muuid )== null)//如果不是一个需要被本拦截器拦截的方法，则忽略该拦截器
                    	continue;    
//                    Interceptor transactionInterceptor = (Interceptor) Class.forName(it.getClazz()).newInstance();
                    Interceptor transactionInterceptor = (Interceptor)it.getBean();
                    if (transactionInterceptor instanceof ProviderInterceptor)
                        ((ProviderInterceptor) transactionInterceptor).setProviderManagerInfo(this);

                    _t.add(transactionInterceptor);
                }

                catch (Exception ne)
                {


                    log.error("",ne);
                }

            }
            if (this.enableTransaction())
            {
            	SynchronizedMethod synmethod = txs.isTransactionMethod(method,muuid);
            	if(synmethod == null)
            		return new InterceptorWrapper(_t);
                Interceptor wrapInterceptor = interceptorFacttory.getInterceptor(synmethod);//new TransactionInterceptor(synmethod);

                InterceptorWrapper wraper = new InterceptorWrapper(wrapInterceptor, _t);
                return wraper;

            }
            else
            {

                InterceptorWrapper wraper = new InterceptorWrapper(_t);
                return wraper;
            }

        }
        else
        {
        	SynchronizedMethod synmethod = txs.isTransactionMethod(method,muuid);
        	if(synmethod == null)
        		return null;
            Interceptor wrapInterceptor = interceptorFacttory.getInterceptor(synmethod);//new TransactionInterceptor(synmethod);
            return wrapInterceptor;
        }

    }

    /**
     * 构建拦截器链表，根据拦截器配置的顺序构造链接，配置在前的先调用。
     * 
     * @return
     */
    private Interceptor _getChainInterceptor(Method method,String methoduuid)
    {
        if (enableInterceptor())
        {
            int size = interceptors.size();
            Interceptor next = null;
            for (int i = size - 1; i >= 0; i--)
            {
                try
                {

                    InterceptorInfo it = (InterceptorInfo) interceptors.get(i);
                    if(!it.allWillBeIntercept() )//如果不是一个需要被本拦截器拦截的方法，则忽略该拦截器
                    {
                    	if( it.isInterceptMethod(method, methoduuid )== null)
                    		continue;
                    }
                    
//                    Interceptor transactionInterceptor = (Interceptor) Class.forName(it.getClazz()).newInstance();
                    Interceptor transactionInterceptor = (Interceptor)it.getBean();
                    if (transactionInterceptor instanceof ProviderInterceptor)
                        ((ProviderInterceptor) transactionInterceptor).setProviderManagerInfo(this);
                    if (next != null)
                    {
                        InterceptorChain chain = new InterceptorChain(transactionInterceptor, next);
                        next = chain;
                    }
                    else
                    {
                        next = transactionInterceptor;
                    }
                }

                catch (Exception ne)
                {
                    log.info("",ne);
                }
            }
            return next;

        }
        return null;
    }

    public void setTransactionInterceptorClass(String transactionInterceptorClass)
    {
        this.transactionInterceptorClass = transactionInterceptorClass;

    }

    public boolean usedCustomInterceptor()
    {
        return this.usedCustomInterceptor;
    }

    public void addInterceptor(InterceptorInfo interceptorinfo)
    {
        this.interceptors.add(interceptorinfo);
        this.usedCustomInterceptor = true;
    }

    public List getInterceptors()
    {
        return interceptors;
    }

    public boolean isCallorder_sequence()
    {
        return callorder_sequence;
    }

    public void setCallorder_sequence(boolean callorder_sequence)
    {
        this.callorder_sequence = callorder_sequence;
    }
    public String getTransactionInterceptorClass() {
        return transactionInterceptorClass;
    }
    
    public List getTransactionMethods(){
        this.loadTXAnnotation();
        if(txs != null){
                return txs.getTransactionMethods();
        }else{
                return null;
        }
    }
    
    public void unmodify()
    {
        interceptors = java.util.Collections.unmodifiableList(interceptors);
    }



}
