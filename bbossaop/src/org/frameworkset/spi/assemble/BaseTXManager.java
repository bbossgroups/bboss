package org.frameworkset.spi.assemble;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.frameworkset.spi.ProviderInterceptor;
import org.frameworkset.spi.UNmodify;
import org.frameworkset.spi.interceptor.InterceptorChain;
import org.frameworkset.spi.interceptor.InterceptorWrapper;
import org.frameworkset.spi.interceptor.TransactionInterceptor;

import com.frameworkset.proxy.Interceptor;

public abstract class BaseTXManager implements java.io.Serializable,UNmodify
{
    private static Logger log = Logger.getLogger(ProviderManagerInfo.class);

    protected Transactions txs;
    

    protected List<InterceptorInfo> interceptors = new ArrayList<InterceptorInfo>();

    protected boolean callorder_sequence = true;
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
    public SynchronizedMethod isTransactionMethod(Method method)
    {   
        this.loadTXAnnotation();
        if(txs == null)
                return null;
        return txs.isTransactionMethod(method);
    }
    
    public SynchronizedMethod isAsyncMethod(Method method)
    {   
        this.loadAsyncAnnotation();
        if(this.asyncMethods == null)
                return null;
        return this.asyncMethods.isAsyncMethod(method);
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
    public Interceptor getTransactionInterceptor()
    {

        if (this.interceptors != null && this.interceptors.size() > 0)
        {
            if (!callorder_sequence)
            {
                Interceptor intercptor = getChainInterceptor();

                if (this.enableTransaction())
                {
                    if (intercptor != null)
                    {
                        TransactionInterceptor wrapInterceptor = new TransactionInterceptor(this.txs);
                        InterceptorChain inteceptor = new InterceptorChain(wrapInterceptor, intercptor, true);
                        return inteceptor;
                    }
                    else
                    {
                        TransactionInterceptor wrapInterceptor = new TransactionInterceptor(this.txs);

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
                Interceptor intercptor = this.getSequenceInterceptor();
                return intercptor;

            }
        }
        else if (this.enableTransaction())
        {
            return new TransactionInterceptor(this.txs);
        }
        return null;
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

    private Interceptor getSequenceInterceptor()
    {
        loadTXAnnotation();
        if (this.interceptors != null && interceptors.size() > 0)
        {
            int size = interceptors.size();
            List _t = new ArrayList(size);
            for (int i = 0; i < size; i++)
            {

                try
                {

                    InterceptorInfo it = (InterceptorInfo) interceptors.get(i);
                    Interceptor transactionInterceptor = (Interceptor) Class.forName(it.getClazz()).newInstance();
                    if (transactionInterceptor instanceof ProviderInterceptor)
                        ((ProviderInterceptor) transactionInterceptor).setProviderManagerInfo(this);

                    _t.add(transactionInterceptor);
                }
                catch (ClassNotFoundException ex)
                {
                    // ex.printStackTrace();
                    log.error(ex);
                }
                catch (IllegalAccessException ex)
                {
                    log.error(ex);
                }
                catch (InstantiationException ex)
                {
                    log.error(ex);
                }
                catch (java.lang.NullPointerException ne)
                {
                    log.error(ne);
                }

            }
            if (this.enableTransaction())
            {
                TransactionInterceptor wrapInterceptor = new TransactionInterceptor(this.txs);
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
            TransactionInterceptor wrapInterceptor = new TransactionInterceptor(this.txs);
            return wrapInterceptor;
        }

    }

    /**
     * 构建拦截器链表，根据拦截器配置的顺序构造链接，配置在前的先调用。
     * 
     * @return
     */
    private Interceptor getChainInterceptor()
    {
        if (this.interceptors != null && interceptors.size() > 0)
        {
            int size = interceptors.size();
            Interceptor next = null;
            for (int i = size - 1; i >= 0; i--)
            {
                try
                {

                    InterceptorInfo it = (InterceptorInfo) interceptors.get(i);
                    Interceptor transactionInterceptor = (Interceptor) Class.forName(it.getClazz()).newInstance();
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
                catch (ClassNotFoundException ex)
                {
                    // ex.printStackTrace();
                    log.error(ex);
                }
                catch (IllegalAccessException ex)
                {
                    log.error(ex);
                }
                catch (InstantiationException ex)
                {
                    log.error(ex);
                }
                catch (java.lang.NullPointerException ne)
                {
                    log.error(ne);
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
