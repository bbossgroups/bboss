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
package org.frameworkset.spi.assemble;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.frameworkset.spi.UNmodify;

/**
 * <p>Title: </p>
 *
 * <p>Description: 服务管理者信息封装器
 * 封装每个服务组件的各种不同的提供者信息，提供者间的同步方法信息，方法的声明式事务管理信息
 * </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 长沙科创</p>
 *
 * @author biaoping.yin
 * @version 1.0
 */
public class ProviderManagerInfo extends BaseTXManager implements UNmodify {
    private static Logger log = Logger.getLogger(ProviderManagerInfo.class);
    private String id;
    private String jndiName;
    private ProviderInfoQueue providerQueue;
    private Map providerIndexByType;
//    private ApplicationInfo ApplicationInfo;
    private Map synchronizedMethods;
    private Map realysynchronizedMethods;   
    private List synchronizedMethodLists;
    private Construction construction;
	/**
	 * 是否允许将组建发布为远程服务
	 */
	private boolean enablerpc = false;
//    private boolean callorder_sequence = true;
//    
//    private List interceptors;
//    
//    /**
//     * 用户是否自己定义了拦截器
//     */
//    private boolean usedCustomInterceptor = false;
//    
//    
//
////    private Interceptor transactionInterceptor;
//    private String transactionInterceptorClass;
    /**标识服务提供者实例是否为单例模式*/

    private boolean singlable = true;
    private boolean defaultable = false;

    /**缺省的provider*/
    private SecurityProviderInfo defaulProvider;

    private boolean synchronizedEnabled = false;
    
//    private Transactions txs;
    
    private List<Pro> references;

    public static void main(String[] args) {
        
    }
    
//    public List getTransactionMethods(){
//    	if(txs != null){
//    		return txs.getTransactionMethods();
//    	}else{
//    		return null;
//    	}
//    }

    public ProviderManagerInfo()
    {
        providerIndexByType = java.util.Collections.synchronizedMap(new HashMap());
        synchronizedMethods = java.util.Collections.synchronizedMap(new HashMap());
        realysynchronizedMethods = java.util.Collections.synchronizedMap(new HashMap());
        this.providerQueue = new ProviderInfoQueue();
        this.synchronizedMethodLists = new ArrayList();
        references = new ArrayList<Pro>();
//        interceptors = new ArrayList();
    }
    public SecurityProviderInfo getDefaulProviderInfo() {
    	try
    	{
    		if(defaulProvider == null)
    		{
    			if(providerQueue != null && providerQueue.size() > 0)
    			{
    				defaulProvider = providerQueue.getSecurityProviderInfo(0);
    			}
    		}
    		return defaulProvider;   		
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    		return null;
    	}
    }

    public void addSynchronizedMethod(SynchronizedMethod synchronizedMethod)
    {
    	this.synchronizedMethodLists.add(synchronizedMethod);
        this.synchronizedMethods.put(synchronizedMethod.getUUID(),synchronizedMethod);
    }
    
    /**
     * 从以方法名或者模式匹配名称索引的同步方法表获取同步方法对象
     * @param methodUUID
     * @return
     */
    public SynchronizedMethod getSynchronizedMethod(String methodUUID)
    {
        return (SynchronizedMethod)synchronizedMethods.get(methodUUID);
    }
    
//    public void setTransactions(Transactions txs)
//    {
//    	this.txs = txs;
//    }
    public static final Boolean TRUE = new Boolean(true);
    public static final Boolean FALSE = new Boolean(false);
    
    /**
     * 判断方法是否包含同步方法，如果包含则返回true，否则返回false
     * @param method
     * @return
     */
    public boolean isSynchronizedMethod(Method method)
    {
    	
    	String methodUUID = SynchronizedMethod.buildMethodUUID(method);
    	Boolean match = (Boolean)this.realysynchronizedMethods.get(methodUUID) ;
    	
    	if(match == null)
    	{
	    	for(int i = 0; i < this.synchronizedMethodLists.size(); i ++)
	    	{
	    		SynchronizedMethod m = (SynchronizedMethod)synchronizedMethodLists.get(i);
	    		if(m.match(method,methodUUID))
	    		{
	    			realysynchronizedMethods.put(methodUUID, TRUE);
	    			return true;
	    		}
	    	}
	    	realysynchronizedMethods.put(methodUUID, FALSE);
	    	return false;
    	}
    	else
    	{
    		return match.booleanValue();
    	}
    	
      
    }
    
//    public SynchronizedMethod isTransactionMethod(Method method)
//    {
//    	if(txs == null)
//    		return null;
//    	return txs.isTransactionMethod(method);
//    }
    
    



    public String getId() {
        return id;
    }

    public String getJndiName() {
        return jndiName;
    }

    public ProviderInfoQueue getProviderInfoQueue() {
        return providerQueue;
    }



//    public ApplicationInfo getApplicationInfo() {
//        return ApplicationInfo;
//    }

    public boolean isSynchronizedEnabled() {
        return synchronizedEnabled;
    }

    public boolean isSinglable() {
        return singlable;
    }

    public boolean isDefaultable() {
        return defaultable;
    }

//    public Interceptor getTransactionInterceptor() {
//
//    	if(this.interceptors != null && this.interceptors.size() > 0)
//    	{
//    		if(!callorder_sequence)
//    		{
//	    		Interceptor intercptor = getChainInterceptor();
//	    		
//	    		if(this.enableTransaction())
//	    		{	
//	    			if(intercptor != null)
//	        		{
//						TransactionInterceptor wrapInterceptor = new TransactionInterceptor(this.txs);
//						InterceptorChain inteceptor = new InterceptorChain(wrapInterceptor,intercptor,true);
//						return inteceptor;    			
//	        		}
//	    			else
//	    			{
//	    				TransactionInterceptor wrapInterceptor = new TransactionInterceptor(this.txs);
//						
//						return wrapInterceptor;  
//	    			}
//	    		}
//	    		else
//	    		{
//	    			return intercptor;
//	    		}
//    		}
//    		else
//    		{
//    			Interceptor intercptor = this.getSequenceInterceptor();
//	    		return intercptor;
//	    		
//    		}
//    	}
//    	else if(this.enableTransaction())
//    	{
//    		return new TransactionInterceptor(this.txs);
//    	}
//    	return null;
//    }
//    
//    public boolean enableTransaction()
//    {
//    	if(this.txs != null && txs.size() > 0)
//    		return true;
//    	return false;
//    }
    
//    private Interceptor getSequenceInterceptor()
//    {
//    	
//    	if(this.interceptors != null && interceptors.size() > 0)
//    	{
//    		int size = interceptors.size();    		
//			List _t = new ArrayList(size);
//			for(int i = 0; i < size; i ++)
//			{
//				
//				try {
//					
//    				InterceptorInfo it = (InterceptorInfo)interceptors.get(i);
//    	    		Interceptor transactionInterceptor = (Interceptor) Class.forName(
//    	    				it.getClazz()).newInstance();
//    	            if(transactionInterceptor instanceof ProviderInterceptor)
//    	                ((ProviderInterceptor)transactionInterceptor).setProviderManagerInfo(this);
//
//    	            _t.add(transactionInterceptor);
//    	        } catch (ClassNotFoundException ex) {
////    	        	ex.printStackTrace();
//    	            log.error(ex);
//    	        } catch (IllegalAccessException ex) {
//    	            log.error(ex);
//    	        } catch (InstantiationException ex) {
//    	            log.error(ex);
//    	        }
//    	        catch(java.lang.NullPointerException ne)
//    	        {
//    	            log.error(ne);
//    	        }
//    	        
//			}
//			if(this.enableTransaction())
//    		{	
//    			TransactionInterceptor wrapInterceptor = new TransactionInterceptor(this.txs);
//    			InterceptorWrapper wraper = new InterceptorWrapper(wrapInterceptor,_t);
//		        return wraper;
//    			
//    		}
//			else
//			{
//    		
//				InterceptorWrapper wraper = new InterceptorWrapper(_t);
//		        return wraper;
//			}
//	    	
//    	}
//    	else
//    	{
//    		TransactionInterceptor wrapInterceptor = new TransactionInterceptor(this.txs);
//    		return wrapInterceptor;
//    	}
//        
//    }
    
//    /**
//     * 构建拦截器链表，根据拦截器配置的顺序构造链接，配置在前的先调用。
//     * 
//     * @return
//     */
//    private Interceptor getChainInterceptor()
//    {
//    	if(this.interceptors != null && interceptors.size() > 0)
//    	{
//    		int size = interceptors.size();    		
//			Interceptor next = null;
//			for(int i = size-1; i >= 0; i --)
//			{
//				try {
//					
//    				InterceptorInfo it = (InterceptorInfo)interceptors.get(i);
//    	    		Interceptor transactionInterceptor = (Interceptor) Class.forName(
//    	    				it.getClazz()).newInstance();
//    	            if(transactionInterceptor instanceof ProviderInterceptor)
//    	                ((ProviderInterceptor)transactionInterceptor).setProviderManagerInfo(this);
//    	            if(next != null)
//    	            {
//    	            	InterceptorChain chain = new InterceptorChain(transactionInterceptor,next);
//    	            	next = chain;    	            	
//    	            }
//    	            else
//    	            {
//    	            	next = transactionInterceptor;
//    	            }
//    	        } catch (ClassNotFoundException ex) {
////    	        	ex.printStackTrace();
//    	            log.error(ex);
//    	        } catch (IllegalAccessException ex) {
//    	            log.error(ex);
//    	        } catch (InstantiationException ex) {
//    	            log.error(ex);
//    	        }
//    	        catch(java.lang.NullPointerException ne)
//    	        {
//    	            log.error(ne);
//    	        }
//			}
//			return next;
//    		
//	    	
//    	}
//        return null;
//    }

//    public String getTransactionInterceptorClass() {
//        return transactionInterceptorClass;
//    }

    public void setDefaulProviderInfo(SecurityProviderInfo defaulProvider) {
        this.defaulProvider = defaulProvider;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setJndiName(String jndiName) {
        this.jndiName = jndiName;
    }

    public void setProviderInfoQueue(ProviderInfoQueue providerQueue) {
        this.providerQueue = providerQueue;
    }

//    /**
//     * setSecuityInfo
//     *
//     * @param ApplicationInfo ApplicationInfo
//     */
//    public void setApplicationInfo(ApplicationInfo ApplicationInfo) {
//        this.ApplicationInfo = ApplicationInfo;
//    }

    public void setSynchronizedEnabled(boolean synchronizedEnabled) {
        this.synchronizedEnabled = synchronizedEnabled;
    }

    public void setSinglable(boolean singlable) {
        this.singlable = singlable;
    }

    public void setDefaultable(boolean defaultable) {
        this.defaultable = defaultable;
    }

//    public void setTransactionInterceptorClass(String
//                                               transactionInterceptorClass) {
//        this.transactionInterceptorClass = transactionInterceptorClass;
//        
//    }

    /**
     * addSecurityProvider
     *
     * @param provider SecurityProvider
     */
    public void addSecurityProviderInfo(SecurityProviderInfo provider) {
        provider.setProviderManagerInfo(this);
        this.providerIndexByType.put(provider.getType(),provider);
        this.getProviderInfoQueue().addSecurityProviderInfo(provider);
    }

    public SecurityProviderInfo getSecurityProviderInfoByType(String type)
    {
        return (SecurityProviderInfo)providerIndexByType.get(type);
    }
    
	public List<Pro> getReferences() {
		return references;
	}
	
	public void addReference(Pro ref)
	{
		this.references.add(ref);
	}
	
//	public boolean usedCustomInterceptor()
//	{
//		return this.usedCustomInterceptor;
//	}
//	
//	public void addInterceptor(InterceptorInfo interceptorinfo)
//	{
//		this.interceptors.add(interceptorinfo);
//		this.usedCustomInterceptor = true;
//	}

	public List getSynchronizedMethodLists() {
		return synchronizedMethodLists;
	}

//	public List getInterceptors() {
//		return interceptors;
//	}
//
//	public boolean isCallorder_sequence() {
//		return callorder_sequence;
//	}
//
//	public void setCallorder_sequence(boolean callorder_sequence) {
//		this.callorder_sequence = callorder_sequence;
//	}

	public void setConstruction(Construction construction) {
		this.construction = construction;
		
	}

	public Construction getConstruction() {
		
		return this.construction;
	}

    public void unmodify()
    {
        super.unmodify();
        this.synchronizedMethodLists = java.util.Collections.unmodifiableList(synchronizedMethodLists);
        this.synchronizedMethods = java.util.Collections.unmodifiableMap(synchronizedMethods);
        this.references = java.util.Collections.unmodifiableList(references);
        
    }

    @Override
    public void initTransactions()
    {
        // TODO Auto-generated method stub
        
    }

	@Override
	protected void initAsyncMethods() {
		// TODO Auto-generated method stub
		
	}

	public boolean isEnablerpc() {
		return enablerpc;
	}

	public void setEnablerpc(boolean enablerpc) {
		this.enablerpc = enablerpc;
	}

   

    
	
	
}
