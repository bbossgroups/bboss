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

package org.frameworkset.spi.cglib;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import net.sf.cglib.proxy.NoOp;

import org.apache.log4j.Logger;
import org.frameworkset.spi.CallContext;
import org.frameworkset.spi.assemble.BaseTXManager;
import org.frameworkset.spi.assemble.Pro;
import org.frameworkset.spi.assemble.ProviderInfoQueue;
import org.frameworkset.spi.assemble.ProviderManagerInfo;
import org.frameworkset.spi.assemble.SecurityProviderInfo;
import org.frameworkset.spi.assemble.SynchronizedMethod;
import org.frameworkset.spi.async.AsyncCall;
import org.frameworkset.spi.async.CallBack;
import org.frameworkset.spi.async.CallBackService;
import org.frameworkset.spi.async.CallBackServiceImpl;
import org.frameworkset.spi.async.CallService;
import org.frameworkset.spi.interceptor.AfterThrowable;

import com.frameworkset.proxy.Interceptor;

/**
 * <p>Title: CGLibUtil.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-6-19 下午06:00:28
 * @author biaoping.yin
 * @version 1.0
 */
public class CGLibUtil {
	private static Logger log = Logger.getLogger(CGLibUtil.class);
	private static AsyncCall asyncCall;
	private static Object lock = new Object();
	
	private static void init()
	{
		if(asyncCall != null )
			return;
		synchronized(lock)
		{
			if(asyncCall == null )
			{
				asyncCall = new AsyncCall();
				asyncCall.start();
			}
		}
		
	}
	public static Object invoke(final Object delegate, final Method method, final Object[] args,
			final MethodProxy proxy,final CallContext callcontext,final BaseTXManager providerManagerInfo) throws Throwable
	{
		final SynchronizedMethod synmethod = providerManagerInfo.isAsyncMethod(method,null) ;
		if(synmethod == null )
		{
			return invoke_(delegate, method, args,
					proxy,callcontext,providerManagerInfo) ;
		}
		else
		{
//			if(synmethod.getAsyncResultMode() == Result.YES 
//					&& synmethod.getAsyncCallback() == null 
//					&& synmethod.getAsyncTimeout() <= 0 )
//			{
//				return invoke_(delegate, method, args,
//						proxy,callcontext,serviceID,providerManagerInfo) ; 
//			}
			init();

			
			return asyncCall.runCallService(new CallService(){
				public SynchronizedMethod getAsyncMethod() {
					return synmethod;
				}

				public CallBackService getCallBackService() {
					if(callcontext != null && callcontext.getApplicationContext() != null && synmethod.getAsyncCallback() != null)
					{
						CallBack callBack = (CallBack)callcontext.getApplicationContext().getBeanObject(synmethod.getAsyncCallback());
						return new CallBackServiceImpl(callBack);
					}
					return null;
				}
				public Object call() throws Exception {
					
					try {
						return invoke_(delegate, method, args,
								proxy,callcontext,providerManagerInfo);
					} catch (Exception e) {
						throw e;
					}
					 catch (Throwable e) {
						throw new Exception(e);
					}
				}
				
			});
		}
			
		
	}
	
	public static Object invoke(final Object delegate, final Method method, final Object[] args,
			final MethodProxy proxy,final Pro providerManagerInfo) throws Throwable
	{
		final SynchronizedMethod synmethod = providerManagerInfo.isAsyncMethod(method,null) ;
		if(synmethod == null )
		{
			return invoke_(delegate, method, args,
					proxy,providerManagerInfo) ;
		}
		else
		{
//			if(synmethod.getAsyncResultMode() == Result.YES 
//					&& synmethod.getAsyncCallback() == null 
//					&& synmethod.getAsyncTimeout() <= 0 )
//			{
//				return invoke_(delegate, method, args,
//						proxy,callcontext,serviceID,providerManagerInfo) ; 
//			}
			init();

			
			return asyncCall.runCallService(new CallService(){
				public SynchronizedMethod getAsyncMethod() {
					return synmethod;
				}

				public CallBackService getCallBackService() {
					if(providerManagerInfo.getApplicationContext() != null && synmethod.getAsyncCallback() != null)
					{
						CallBack callBack = (CallBack)providerManagerInfo.getApplicationContext().getBeanObject(synmethod.getAsyncCallback());
						return new CallBackServiceImpl(callBack);
					}
					return null;
				}
				public Object call() throws Exception {
					
					try {
						return invoke_(delegate, method, args,
								proxy,providerManagerInfo);
					} catch (Exception e) {
						throw e;
					}
					 catch (Throwable e) {
						throw new Exception(e);
					}
				}
				
			});
		}
			
		
	}
	
	private static Object invoke_(Object delegate, Method method, Object[] args,
			MethodProxy proxy,CallContext callcontext,BaseTXManager providerManagerInfo) throws Throwable
    {
//        if (!serviceID.isRemote())
        {
        	
            Interceptor txinterceptor = null;
            String muuid = null;
            if(providerManagerInfo.enableTransaction())
            {
            	muuid = SynchronizedMethod.buildMethodUUID(method);
            	txinterceptor = providerManagerInfo.getTransactionInterceptor(method,muuid);
            }
            Interceptor interceptor = null;
            if(providerManagerInfo.enableInterceptor())
            {
            	if(muuid == null)
            		muuid = SynchronizedMethod.buildMethodUUID(method);
            	interceptor = providerManagerInfo.getChainInterceptor(method,muuid);
            }
            Object obj = null;// 只返回delegate中方法返回的值
            try
            {
            	if (interceptor != null)
                    interceptor.before(method, args);
            }
            catch(Exception e)
            {
            	throw e;
            }
//            catch(Exception e)
//            {
//            	log.info("",e);
//            }
            try
            {

              
                // 如果不是同步方法，则无需同步调用其他接口方法
                // log.debug("method.getName():" + method.getName());
                //log.debug("providerManagerInfo.getSynchronizedMethod("
                // +
                // method.getName() + "):" +
                // providerManagerInfo.getSynchronizedMethod
                // (method.getName()));
                
                if(txinterceptor == null)
                {

                	obj = untxinvoke_(delegate, method, args,

                			proxy,providerManagerInfo);
                }
                else
                {

                	obj = txinvoke_(delegate, method, args,

                			proxy,providerManagerInfo,txinterceptor );
                }
                if (interceptor != null)
					try {
						interceptor.after(method, args);
					} catch (Throwable e) {
						throw new AfterThrowable(e);
					}
                return obj;

            }
            catch(AfterThrowable e)
            {
            	throw e.getCause();
            }
            catch (InvocationTargetException e)
            {               
                if (interceptor != null)
                {                   
                    interceptor.afterThrowing(method, args, e.getTargetException());
                }

                // 将方法抛出的异常直接抛出方法异常
                throw e.getTargetException();
            }
            catch (Throwable t)
            {
               
               
                    // t.printStackTrace();
                if (interceptor != null)
                    interceptor.afterThrowing(method, args, t);
                
                throw t;
            }
            finally
            {
                
                if (interceptor != null)
                {
                    interceptor.afterFinally(method, args);
                    interceptor = null;
                }
            }
        }
//        else
//        {
//
//            return RPCHelper.getRPCHelper().rpcService((RemoteServiceID)serviceID, method, args,callcontext);
//
//        }
    }
	
	private static Object txinvoke_(Object delegate, Method method, Object[] args,
			MethodProxy proxy,BaseTXManager providerManagerInfo,Interceptor tx ) throws Throwable
	{
		
		Object obj = null;
		try
		{
			tx.before(method, args);
			if(proxy == null)
	        	obj = method.invoke(delegate, args);
	        else
	        	obj = method.invoke(delegate, args);
			tx.after(method, args);
			return obj;
		}
		 catch (InvocationTargetException e)
		 {
			 tx.afterThrowing(method, args, e.getTargetException());
			 throw e;
		 }
		catch(Throwable e)
		{
			tx.afterThrowing(method, args, e);
			throw e;
		}
		finally
		{
			tx.afterFinally(method, args);
		}
		
	}
	private static Object untxinvoke_(Object delegate, Method method, Object[] args,
			MethodProxy proxy,BaseTXManager providerManagerInfo) throws Throwable
	{
		
		Object obj = null;
//		Interceptor tx = providerManagerInfo.getTransactionInterceptor(method);
		try
		{
			
			if(proxy == null)
	        	obj = method.invoke(delegate, args);
	        else
	        	obj = method.invoke(delegate, args);
			return obj;
		}
		catch(Throwable e)
		{
			throw e;
		}
		finally
		{
		}
	}
	private static Object invoke_(Object delegate, Method method, Object[] args,
			MethodProxy proxy,BaseTXManager providerManagerInfo) throws Throwable
    {
//        if (!serviceID.isRemote())
//		try		
        {
//            Interceptor txinterceptor = providerManagerInfo.getTransactionInterceptor(method);
//            Interceptor interceptor = providerManagerInfo.getInterceptorChain();
        	Interceptor txinterceptor = null;
            String muuid = null;
            if(providerManagerInfo.enableTransaction())
            {
            	muuid = SynchronizedMethod.buildMethodUUID(method);
            	txinterceptor = providerManagerInfo.getTransactionInterceptor(method,muuid);
            }
            Interceptor interceptor = null;
            if(providerManagerInfo.enableInterceptor())
            {
            	if(muuid == null)
            		muuid = SynchronizedMethod.buildMethodUUID(method);
            	interceptor = providerManagerInfo.getChainInterceptor(method,muuid);
            }
            
            Object obj = null;// 只返回delegate中方法返回的值
            try
            {
            	if (interceptor != null)
                    interceptor.before(method, args);
            }
            catch(Exception e)
            {
            	throw e;
            }
//            catch(Exception e)
//            {
//            	log.info("",e);
//            }
            try
            {

               
                // 如果不是同步方法，则无需同步调用其他接口方法
                // log.debug("method.getName():" + method.getName());
                //log.debug("providerManagerInfo.getSynchronizedMethod("
                // +
                // method.getName() + "):" +
                // providerManagerInfo.getSynchronizedMethod
                // (method.getName()));
                
                
            		if(txinterceptor != null)
            		{

            			obj = txinvoke_( delegate,  method,  args,
            					 proxy, providerManagerInfo,txinterceptor );
            		}
            		else
            		{
            			obj = untxinvoke_( delegate,  method,  args,

           					 proxy, providerManagerInfo);
            		}

            	
	                if (interceptor != null)
	                	try {
							interceptor.after(method, args);
						} catch (Throwable e) {
							throw new AfterThrowable(e);
						}
                return obj;

            }
            catch(AfterThrowable e)
            {
            	throw e.getCause();
            }
            catch (InvocationTargetException e)
            {
                //log.error(e);
                if (interceptor != null)
                {
                    interceptor.afterThrowing(method, args, e.getTargetException());
                    
                }

                // 将方法抛出的异常直接抛出方法异常
                throw e.getTargetException();
            }
            catch (Throwable t)
            {
                
                if (interceptor != null)
                	interceptor.afterThrowing(method, args, t);
                
                throw t;
            }
            finally
            {
                if (interceptor != null)
                {
                       interceptor.afterFinally(method, args);
                       interceptor = null;
                }

               
                
            }
        }
//		finally
//		{
//			
//		}
      
    }
	public static Object invokeSynTX(final Object delegate, final Method method, final Object[] args,
			final MethodProxy proxy,final CallContext callcontext,final ProviderManagerInfo providerManagerInfo) throws Throwable {
		final SynchronizedMethod synmethod = providerManagerInfo.isAsyncMethod(method,null) ;
		if(synmethod == null )
		{
			
			return invokeSynTX_(delegate, method, args,
					proxy,callcontext,providerManagerInfo) ;
		}
		else
		{
//			if(synmethod.getAsyncResultMode() == Result.YES 
//					&& synmethod.getAsyncCallback() == null 
//					&& synmethod.getAsyncTimeout() <= 0 )
//			{
//				return invokeSynTX_(delegate, method, args,
//						proxy,callcontext,serviceID,providerManagerInfo) ;
//			}
			init();			
			return asyncCall.runCallService(new CallService(){

				public SynchronizedMethod getAsyncMethod() {
					return synmethod;
				}

				public CallBackService getCallBackService() {
					if(callcontext != null && callcontext.getApplicationContext() != null)
					{
						CallBack callBack = (CallBack)callcontext.getApplicationContext().getBeanObject(synmethod.getAsyncCallback());
						return new CallBackServiceImpl(callBack);
					}
					return null;
				}
				public Object call() throws Exception {					
					try {
						return invokeSynTX_(delegate, method, args,
								proxy,callcontext,providerManagerInfo) ;
					} catch (Exception e) {
						throw e;
					}
					 catch (Throwable e) {
						throw new Exception(e);
					}
				}				
			});
		}
			
		
	}
	private static Object invokeSynTX_(Object delegate, Method method, Object[] args,
			MethodProxy proxy,CallContext callcontext,ProviderManagerInfo providerManagerInfo) throws Throwable {
//		if (!serviceID.isRemote())
        {
			String uuid = SynchronizedMethod.buildMethodUUID(method);
            Interceptor interceptor = providerManagerInfo.getSynTransactionInterceptor(method,uuid);
            try
            {

                Object obj = null;// 只返回delegate中方法返回的值
                // 如果不是同步方法，则无需同步调用其他接口方法
                // log.debug("method.getName():" + method.getName());
                //log.debug("providerManagerInfo.getSynchronizedMethod("
                // +
                // method.getName() + "):" +
                // providerManagerInfo.getSynchronizedMethod
                // (method.getName()));
                if (!providerManagerInfo.isSynchronizedEnabled()
                        || !providerManagerInfo.isSynchronizedMethod(method))
                {// 如果没有配置以方法及相应的方法参数的为标识的同步方法
                    // ，
                    // 如果有则执行所有provider的同步方法

                    if (interceptor != null)
                        interceptor.before(method, args);
                    if(proxy == null)
                    	obj = method.invoke(delegate, args);
                    else
                    	obj = method.invoke(delegate, args);
                    if (interceptor != null)
                        interceptor.after(method, args);
                    return obj;

                }
                else
                {
                    // 获取服务提供者队列，该队列已经按照指定的优先级顺序进行排序
                    ProviderInfoQueue providerInfoQueue = providerManagerInfo.getProviderInfoQueue();

                    //同步调用所有的接口方法，如果同步方法是是事务性方法，则需要进行事务处理，及要么全部成功，要么全部失败
                    // 否则在处理的过程中,一些服务提供者方法被执行时抛出异常，将继续执行后续的同步方法

                    SynchronizedMethod synm = providerManagerInfo.isTransactionMethod(method,uuid);
                    boolean isTXMethod = synm != null;
                    Throwable throwable = null;
                    if (interceptor != null)
                        interceptor.before(method, args);
                    for (int i = 0; i < providerInfoQueue.size(); i++)
                    {
                        SecurityProviderInfo securityProviderInfo = providerInfoQueue
                                .getSecurityProviderInfo(i);
                        if (!securityProviderInfo.isUsed() && !securityProviderInfo.isIsdefault())
                        {
                            continue;
                        }
                        Object provider = securityProviderInfo.getProvider(callcontext);
                        // Object provider =
                        // !providerManagerInfo.isSinglable() ?
                        // securityProviderInfo
                        // .getProvider(parent) :
                        // securityProviderInfo.getSingleProvider
                        // (parent);
                        boolean isdelegate = provider.getClass() == delegate.getClass();
                        // 缺省的provider和被标识为使用的provider都需要进行同步调用
                        if (isdelegate || securityProviderInfo.isUsed() || securityProviderInfo.isIsdefault())
                        {
                            if (isdelegate && !securityProviderInfo.isUsed())
                            {
                                log.warn("调用的Provider[" + delegate.getClass() + "]，[" + securityProviderInfo
                                        + "]被设置为未使用的服务提供者。");
                            }

                            if (!isTXMethod) // 不是事务方法时，
                                             // 执行所有的provider的同一个方法，
                            // 忽略失败的方法调用，
                            {
                                /**
                                 * 缺省的提供者，也就是默认的提供者，不需要进行事务处理时，
                                 * 只保留服务提供者的返回值
                                 */
                                if (isdelegate)
                                {
                                    try
                                    {
                                    	method = provider.getClass().getMethod(method.getName(), method.getParameterTypes());
                                    	if(proxy == null)
                                    		obj = method.invoke(provider, args);
                                    	else
                                    		obj = method.invoke(provider, args);
                                    }
                                    catch (InvocationTargetException e)
                                    {
                                        throwable = e.getTargetException();
                                        log.error("调用服务提供者[" + securityProviderInfo + "]中方法[" + method + "]异常："
                                                + e.getMessage(), e);
                                    }
                                    catch (Exception e)
                                    {
                                        throwable = e;
                                        log.error("调用服务提供者[" + securityProviderInfo + "]中方法[" + method + "]异常："
                                                + e.getMessage(), e);
                                    }
                                }
                                else
                                {
                                    try
                                    {
                                    	
                                    	method = provider.getClass().getMethod(method.getName(), method.getParameterTypes());
                                        method.invoke(provider, args);
                                    }
                                    catch (Exception e)// 抛出异常时不记录该异常，
                                    // 继续执行后续的provider的方法
                                    {
                                        // 不是事务方法调用时，忽略其他服务提供者方法执行时的异常
                                        log.error("同步调用服务提供者[" + securityProviderInfo + "]中方法[" + method
                                                + "]异常：" + e.getMessage(), e);
                                    }
                                }
                                // 抛出缺省服务提供者方法异常，同时终止其他provider同步执行逻辑

                            }
                            else
                            // 事务性方法执行逻辑，不处理事务性异常，但是需要处理非事务性异常
                            {
                                if (isdelegate)
                                {
                                    try
                                    {
                                    	method = provider.getClass().getMethod(method.getName(), method.getParameterTypes());
                                    	if(proxy == null)
                                        	obj = method.invoke(provider, args);
                                    	else
                                    		obj = method.invoke(provider, args);
                                    }
                                    catch (InvocationTargetException e)
                                    {
                                        if (synm.isRollbackException(e.getTargetException())) // 事务性异常
                                        // ，
                                        // 继续抛出异常
                                        // ，
                                        // 中断其他服务提供者方法执行
                                        {
                                            throw e.getTargetException();
                                        }
                                        else
                                        {
                                            throwable = e.getTargetException();// 抛出异常时记录该异常
                                            // ，
                                            // 继续执行后续的provider的同步方法
                                        }
                                    }

                                    catch (Exception e)
                                    {
                                        if (synm.isRollbackException(e)) // 事务性异常
                                        // ，
                                        // 继续抛出异常
                                        // ，
                                        // 中断其他服务提供者方法执行
                                        {
                                            throw e;
                                        }
                                        else
                                        {
                                            throwable = e;// 抛出异常时记录该异常，
                                            // 继续执行后续的provider的同步方法
                                        }
                                    }
                                }
                                else
                                {
                                    try
                                    {
                                    	method = provider.getClass().getMethod(method.getName(), method.getParameterTypes());
                                        method.invoke(provider, args);
                                    }
                                    catch (InvocationTargetException e)
                                    {
                                        if (synm.isRollbackException(e.getTargetException())) // 事务性异常
                                        // ，
                                        // 继续抛出异常
                                        // ，
                                        // 中断其他服务提供者方法执行
                                        {
                                            throw e;
                                        }
                                        else
                                        {
                                            log.error("同步调用服务提供者[" + securityProviderInfo + "]中方法[" + method
                                                    + "]异常：" + e.getMessage(), e);
                                        }
                                    }
                                    catch (Exception e)
                                    {
                                        if (synm.isRollbackException(e)) // 事务性异常
                                        // ，
                                        // 继续抛出异常
                                        // ，
                                        // 中断其他服务提供者方法执行
                                        {
                                            throw e;
                                        }
                                        else
                                        {
                                            log.error("同步调用服务提供者[" + securityProviderInfo + "]中方法[" + method
                                                    + "]异常：" + e.getMessage(), e);
                                        }
                                    }
                                }
                            }
                        }
                        // log.debug(
                        // "providerInfoQueue.getSecurityProviderInfo("+
                        // i
                        // +").getProvider()" +
                        // providerInfoQueue.getSecurityProviderInfo(i)
                        // .getProvider());
                    }
                    if (throwable != null)// 如果在方法的执行过程中发生异常，抛出该异常
                        throw throwable;
                    if (interceptor != null)
                        interceptor.after(method, args);
                    return obj;
                }
            }
            catch (InvocationTargetException e)
            {
                if (interceptor != null)
                {
                    try
                    {
                        interceptor.afterThrowing(method, args, e.getTargetException());
                    }
                    catch (Exception ei)
                    {
                        ei.printStackTrace();
                    }
                }
                throw e.getTargetException();
            }
            catch (Throwable t)
            {
                try
                {
                    t.printStackTrace();
                    if (interceptor != null)
                        interceptor.afterThrowing(method, args, t);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                throw t;
            }
            finally
            {
                try
                {
                    if (interceptor != null)
                        interceptor.afterFinally(method, args);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                interceptor = null;
            }
        }
//        else
//        {
//            return RPCHelper.getRPCHelper().rpcService((RemoteServiceID)serviceID, method, args,callcontext);
//        }
	}
	public static Object invokeSyn(final Object delegate, final Method method, final Object[] args,
			final MethodProxy proxy,final CallContext callcontext,final ProviderManagerInfo providerManagerInfo) throws Throwable{
		
		final SynchronizedMethod synmethod = providerManagerInfo.isAsyncMethod(method,null) ;
		if(synmethod == null )
		{
			
			return invokeSyn_(delegate, method, args,
					proxy,callcontext,providerManagerInfo);
		}
		else
		{
//			if(synmethod.getAsyncResultMode() == Result.YES 
//					&& synmethod.getAsyncCallback() == null 
//					&& synmethod.getAsyncTimeout() <= 0 )
//			{
//				return invokeSyn_(delegate, method, args,
//						proxy,callcontext,serviceID,providerManagerInfo);
//			}
			init();			
			return asyncCall.runCallService(new CallService(){

				public SynchronizedMethod getAsyncMethod() {
					return synmethod;
				}

				public CallBackService getCallBackService() {
					if(callcontext != null && callcontext.getApplicationContext() != null)
					{
						CallBack callBack = (CallBack)callcontext.getApplicationContext().getBeanObject(synmethod.getAsyncCallback());
						return new CallBackServiceImpl(callBack);
					}
					return null;
				}
				public Object call() throws Exception {					
					try {
						return invokeSyn_(delegate, method, args,
								proxy,callcontext,providerManagerInfo);
					} catch (Exception e) {
						throw e;
					}
					 catch (Throwable e) {
						throw new Exception(e);
					}
				}				
			});
		}
	}
	private static Object invokeSyn_(Object delegate, Method method, Object[] args,
			MethodProxy proxy,CallContext callcontext,ProviderManagerInfo providerManagerInfo) throws Throwable
	
    {
//        if (!serviceID.isRemote())
        {
            Interceptor interceptor = providerManagerInfo.getSynTransactionInterceptor(method,null);
            try
            {

                Object obj = null;// 只返回delegate中方法返回的值
                // 如果不是同步方法，则无需同步调用其他接口方法
                // log.debug("method.getName():" + method.getName());
                //log.debug("providerManagerInfo.getSynchronizedMethod("
                // +
                // method.getName() + "):" +
                // providerManagerInfo.getSynchronizedMethod
                // (method.getName()));
                if (!providerManagerInfo.isSynchronizedEnabled()
                        || !providerManagerInfo.isSynchronizedMethod(method))
                {// 如果没有配置以方法及相应的方法参数的为标识的同步方法
                    // ，
                    // 如果有则执行所有provider的同步方法
//                	method = delegate.getClass().getMethod(method.getName(), method.getParameterTypes());
                    if (interceptor != null)
                        interceptor.before(method, args);
                    if(proxy == null)
                    	obj = method.invoke(delegate, args);
                    else
                    	obj = method.invoke(delegate, args);
                    if (interceptor != null)
                        interceptor.after(method, args);
                    return obj;

                }
                else
                {
                    // 获取服务提供者队列，该队列已经按照指定的优先级顺序进行排序
                    ProviderInfoQueue providerInfoQueue = providerManagerInfo.getProviderInfoQueue();

                    //同步调用所有的接口方法，如果同步方法是是事务性方法，则需要进行事务处理，及要么全部成功，要么全部失败
                    // 否则在处理的过程中,一些服务提供者方法被执行时抛出异常，将继续执行后续的同步方法

                    Throwable throwable = null;
                    if (interceptor != null)
                        interceptor.before(method, args);
                    for (int i = 0; i < providerInfoQueue.size(); i++)
                    {
                        SecurityProviderInfo securityProviderInfo = providerInfoQueue
                                .getSecurityProviderInfo(i);
                        if (!securityProviderInfo.isUsed() && !securityProviderInfo.isIsdefault())
                        {
                            continue;
                        }
                        // Object provider =
                        // !providerManagerInfo.isSinglable() ?
                        // securityProviderInfo
                        // .getProvider(parent) :
                        // securityProviderInfo.getSingleProvider
                        // (parent);
                        Object provider = securityProviderInfo.getProvider(callcontext);
                        boolean isdelegate = provider.getClass() == delegate.getClass();
                        // 缺省的provider和被标识为使用的provider都需要进行同步调用
                        if (isdelegate || securityProviderInfo.isUsed() || securityProviderInfo.isIsdefault())
                        {
                            if (isdelegate && !securityProviderInfo.isUsed())
                            {
                                log.warn("调用的Provider[" + delegate.getClass() + "]，[" + securityProviderInfo
                                        + "]被设置为未使用的服务提供者。");
                            }

                            /**
                             * 缺省的提供者，也就是默认的提供者，不需要进行事务处理时，只保留服务提供者的返回值
                             */
                            if (isdelegate)
                            {
                                try
                                {
                                	method = provider.getClass().getMethod(method.getName(), method.getParameterTypes());
                                	if(proxy == null)
                                    	obj = method.invoke(provider, args);
                                	else
                                		obj = method.invoke(provider, args);
                                }
                                catch (InvocationTargetException e)
                                {
                                    throwable = e.getTargetException();
                                    log.error("调用服务提供者[" + securityProviderInfo + "]中方法[" + method + "]异常："
                                            + e.getMessage(), e);
                                }
                                catch (Exception e)
                                {
                                    throwable = e;
                                    log.error("调用服务提供者[" + securityProviderInfo + "]中方法[" + method + "]异常："
                                            + e.getMessage(), e);
                                }

                            }
                            else
                            {
                                try
                                {
                                	method = provider.getClass().getMethod(method.getName(), method.getParameterTypes());
                                    method.invoke(provider, args);
                                }
                                catch (Exception e)
                                {
                                    // 不是事务方法调用时，忽略其他服务提供者方法执行时的异常
                                    log.error("同步调用服务提供者[" + securityProviderInfo + "]中方法[" + method + "]异常："
                                            + e.getMessage(), e);
                                }
                            }

                        }
                        // log.debug(
                        // "providerInfoQueue.getSecurityProviderInfo("+
                        // i
                        // +").getProvider()" +
                        // providerInfoQueue.getSecurityProviderInfo(i)
                        // .getProvider());
                    }
                    // 抛出缺省服务提供者方法异常
                    if (throwable != null)
                        throw throwable;
                    if (interceptor != null)
                        interceptor.after(method, args);
                    return obj;
                }
            }
            catch (InvocationTargetException e)
            {
                if (interceptor != null)
                {
                    try
                    {
                        interceptor.afterThrowing(method, args, e.getTargetException());
                    }
                    catch (Exception ei)
                    {
                        ei.printStackTrace();
                    }
                }
                throw e.getTargetException();
            }
            catch (Throwable t)
            {
                try
                {
                    t.printStackTrace();
                    if (interceptor != null)
                        interceptor.afterThrowing(method, args, t);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                throw t;
            }
            finally
            {
                try
                {
                    if (interceptor != null)
                        interceptor.afterFinally(method, args);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                interceptor = null;
            }

        }
//        else
//        {
//            return RPCHelper.getRPCHelper().rpcService((RemoteServiceID)serviceID, method, args,callcontext);
//        }
    }
	
	/**
	 * 获取组件实例
	 * @param <T>
	 * @param rettype
	 * @param beanType
	 * @param proxy
	 * @return
	 */
	public static <T> T getBeanInstance(Class<T> rettype,Class beanType,MethodInterceptor proxy)
	{
		 Enhancer enhancer = new Enhancer();
		 
	        enhancer.setSuperclass(beanType);
	        enhancer.setCallbacks(new Callback[] { proxy, NoOp.INSTANCE });
	        enhancer.setCallbackFilter(filter);
	        return (T) enhancer.create();
	}
	
	/**
	 * 获取组件实例
	 * @param <T>
	 * @param rettype
	 * @param beanType
	 * @param proxy
	 * @return
	 */
	public static <T> T getBeanInstance(Class<T> rettype,MethodInterceptor proxy)
	{
		 Enhancer enhancer = new Enhancer();
		 
	        enhancer.setSuperclass(rettype);
	        enhancer.setCallbacks(new Callback[] { proxy, NoOp.INSTANCE });
	        enhancer.setCallbackFilter(filter);
	        return (T) enhancer.create();
	}
	/**
	 * 获取组件实例
	 * @param <T>
	 * @param rettype
	 * @param beanType
	 * @param proxy
	 * @return
	 */
	public static <T> T getBeanInstance(String rettype,MethodInterceptor proxy)
	{
		 Enhancer enhancer = new Enhancer();
		 	
	        try {
				enhancer.setSuperclass(Class.forName(rettype));
			} catch (ClassNotFoundException e) {
				throw new java.lang.RuntimeException(e);
			}
	        enhancer.setCallbacks(new Callback[] { proxy, NoOp.INSTANCE });
	        enhancer.setCallbackFilter(filter);
	        return (T) enhancer.create();
	}
	
	private static AopProxyFilter filter = new AopProxyFilter();
}
