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
import org.frameworkset.spi.async.annotation.Async;
import org.frameworkset.spi.async.annotation.Constants;
import org.frameworkset.spi.async.annotation.Result;
//import org.frameworkset.spi.remote.RPCMethodCall;

import com.frameworkset.orm.annotation.TransactionType;
import com.frameworkset.orm.transaction.TransactionException;
import com.frameworkset.util.RegexUtil;

/**
 * <p>Title: SynchronizedMethod</p>
 *
 * <p>Description: 包装同步方法，事务性方法信息</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 长沙科创计算机系统集成有限公司</p>
 *
 * @author biaoping.yin
 * @version 1.0
 */
public class SynchronizedMethod implements java.io.Serializable {
	private static Logger log = Logger.getLogger(SynchronizedMethod.class);
	
	/**
	 * 注解事务方法
	 */
//	private Method txMethod = null;
	/**
	 * 声明的方法名称，如果指定了pattern属性，方法名称就会无效
	 */
    private String methodName;
    
    /**
     * 匹配方法名称的正则表达式模式串
     */
    private String pattern;
    /**
     * 参数类型串列表
     * List<Pro>
     */
    private List<Pro> params;
    private Map<String,Pro> paramIndexs;
    
    /**
     * 方法事务回滚异常列表
     * List<RollbackException>
     */
    private List<RollbackException> rollbackExceptions;
    
    /**
     * 方法事务性异常信息索引表
     * 如果一个异常是事务性异常，则以异常的名字为索引键，Boolean(true)为索引值
     * 如果一个异常不是事务性异常，则以异常的名字为索引键，Boolean(false)为索引值
     * 下次系统再判别该异常时，直接以该索引为准，不在进行复杂的逻辑判断
     * Map<exceptionClass,Boolean>
     */
    private Map<String,Boolean> rollbackExceptionIndexs;
    
    private long asynctimeout ;
    private Result asyncResult ;
    
    
    /**
     * 事务类型
     */
    private TransactionType txtype = TransactionType.REQUIRED_TRANSACTION;
    
//    private Map 
    
    public SynchronizedMethod()
    {
    	params = new ArrayList<Pro>();
    	paramIndexs = new HashMap<String,Pro>();
    	rollbackExceptions = new ArrayList<RollbackException>();
    	rollbackExceptionIndexs = new HashMap<String,Boolean>();
    }
    
    public SynchronizedMethod(Method method,Async async)
    {
    	this.uuid = buildMethodUUID(method);
    	this.asynctimeout = async.timeout();
    	if(async.callback().equals(Constants.NULLCALLBACK))
    		this.asyncCallback = null;
    	else
    		this.asyncCallback = async.callback();
    	asyncResult = async.result();
    }
    
    public long getAsyncTimeout()
    {
    	return asynctimeout;
    }
    private String asyncCallback;
    public String getAsyncCallback()
    {
    	return asyncCallback;
    }
    
    
    public Result getAsyncResultMode()
    {
    	return asyncResult;
    }
    
    
   
    
    
//    public SynchronizedMethod(Method method, String txtype, String[] rollbacksexceptions)
//    {
//        rollbackExceptions = new ArrayList<RollbackException>();
//        rollbackExceptionIndexs = new HashMap<String,Boolean>();
//        this.uuid = buildMethodUUID(method);
//        this.setTxtype(txtype);
//        if(rollbacksexceptions != null && rollbacksexceptions.length > 0)
//            buildRollbackExceptios(rollbacksexceptions);
////        this.txtype_s = txtype;
//        
//    }
    
    public SynchronizedMethod(Method method, TransactionType txtype, String[] rollbacksexceptions)
    {
        rollbackExceptions = new ArrayList<RollbackException>();
        rollbackExceptionIndexs = new HashMap<String,Boolean>();
        this.uuid = buildMethodUUID(method);
        this.setTxtype(txtype);
        if(rollbacksexceptions != null && rollbacksexceptions.length > 0)
            buildRollbackExceptios(rollbacksexceptions);
//        this.txtype_s = txtype;
        
    }

    private void buildRollbackExceptios(String[] rollbacksexceptions)
    {
        if(rollbacksexceptions == null || rollbacksexceptions.length == 0)
            return ;
        for(String e:rollbacksexceptions)
        {
            if(e.equals(""))
                continue;
            int i = e.indexOf('@');
            if(i > 0)
            {
                String exceptionName = e.substring(0,i);
                String exceptionType = e.substring(i + 1);
                RollbackException RollbackException = new RollbackException();
                RollbackException.setExceptionName(exceptionName);
                RollbackException.setExceptionType(exceptionType); 
                this.rollbackExceptions.add(RollbackException);
            }
            else
            {
                RollbackException RollbackException = new RollbackException();
                RollbackException.setExceptionName(e);
                RollbackException.setExceptionType("INSTANCEOF"); 
                this.rollbackExceptions.add(RollbackException);
            }
                
        }
    }


    public static void main(String[] args) {
        
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
    
    public void addParam(Pro param)
    {
    	this.params.add(param);
    	this.paramIndexs.put(param.getClazz(), param);
    }
    public List getParams()
    {
    	return this.params;
    }
    
    public List getRollbackExceptions()
    {
    	return this.rollbackExceptions;
    }
    
    public void addRollbackException(RollbackException rollbackException)
    {
    	this.rollbackExceptions.add(rollbackException);
//    	this.rollbackExceptionIndexs.put(rollbackException.getExceptionName(), rollbackException);
    }
    
//    public RollbackException getRollbackException(String rollbackExceptionClass)
//    {
//    	return (RollbackException)this.rollbackExceptionIndexs.get(rollbackExceptionClass);
//    }
    
    public Param getParam(String paramtype)
    {
    	return (Param)this.paramIndexs.get(paramtype);
    }
    
    
    
    
    private String uuid ;
    /**
     * 获取方法的惟一标识
     * @return
     */
    public String getUUID()
    {
    	try
    	{
	    	if(uuid != null)
	    		return uuid;
	    	if(this.isPattern())
	    	{
	    		return this.uuid = this.pattern;
	    	}
	    		
	    	StringBuffer uuid_ = new StringBuffer();    	
	    	uuid_.append(this.methodName);
	    	if(this.params != null)
	    	{
	    		for(int i = 0; i < params.size(); i ++)
	    		{
	    			Pro param = (Pro)params.get(i);    			
	    			uuid_.append("_").append(param.getClazz());
	    		}
	    	}
	    	uuid = uuid_.toString();
	    	return uuid;
    	}
    	catch(Exception e)
    	{
    		log.error("",e);
    		return null;
    	}
    	
    }
    
    
//    /**
//     * 构建方法的惟一标识id，一个类中的一个方法只对应一个标识，标识生成的规则
//     * 是：方法名+'_' + 参数类型1 + ... + '_' +  参数类型n
//     * @param method 方法对象，存放所有的方法信息，包括方法名，方法参数类型数组，方法返回值类型，方法异常类型等等
//     * @param args 方法参数
//     * @return 方法标识
//     */
//    public static String buildMethodUUID(RPCMethodCall method_call)
//    {
//    	try
//    	{
//	    	StringBuffer uuid = new StringBuffer();
//	    	Class[] paramTypes = method_call.getTypes();
//	    	uuid.append(method_call.getMethodName());
//	    	if(paramTypes == null || paramTypes.length == 0)
//	    	{
//	    		return uuid.toString();
//	    	}
//	    	for(int i = 0; i < paramTypes.length; i ++)
//	    	{
//	    		uuid.append("_").append(paramTypes[i].getName());
//	    	}
//	    	return uuid.toString();
//    	}
//    	catch(Exception e)
//    	{
////    		e.printStackTrace();
//    		log.error(e.getMessage(),e);
//    		return null;
//    	}
//    }
    
    /**
     * 构建方法的惟一标识id，一个类中的一个方法只对应一个标识，标识生成的规则
     * 是：方法名+'_' + 参数类型1 + ... + '_' +  参数类型n
     * @param method 方法对象，存放所有的方法信息，包括方法名，方法参数类型数组，方法返回值类型，方法异常类型等等
     * @param args 方法参数
     * @return 方法标识
     */
    public static String buildMethodUUID(Method method, Object[] args)
    {
//        try
//        {
//            StringBuffer uuid = new StringBuffer();
//            Class[] paramTypes = method.getParameterTypes();
//            uuid.append(method.getName());
//            if(paramTypes == null || paramTypes.length == 0)
//            {
//                return uuid.toString();
//            }
//            for(int i = 0; i < paramTypes.length; i ++)
//            {
//                uuid.append("_").append(paramTypes[i].getName());
//            }
//            return uuid.toString();
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//            log.error(e);
//            return null;
//        }
        return buildMethodUUID(method.getName(), method.getParameterTypes());
    }
    
    /**
     * 构建方法的惟一标识id，一个类中的一个方法只对应一个标识，标识生成的规则
     * 是：方法名+'_' + 参数类型1 + ... + '_' +  参数类型n
     * @param method 方法对象，存放所有的方法信息，包括方法名，方法参数类型数组，方法返回值类型，方法异常类型等等
     * @param args 方法参数
     * @return 方法标识
     */
    public static String buildMethodUUID(String method, Class[] paramTypes)
    {
        try
        {
            StringBuffer uuid = new StringBuffer();
            
            uuid.append(method);
            if(paramTypes == null || paramTypes.length == 0)
            {
                return uuid.toString();
            }
            for(int i = 0; i < paramTypes.length; i ++)
            {
                uuid.append("_").append(paramTypes[i].getName());
            }
            return uuid.toString();
        }
        catch(Exception e)
        {
          
            log.error("",e);
            return null;
        }
    }
    
     
    
    public static MethodInfo refactorMehtodfromUUID(String methodUUID) throws ClassNotFoundException
    {
        int index = methodUUID.lastIndexOf("_");
        MethodInfo methodInfo = new MethodInfo();
        if(index <= 0)
        {
            methodInfo.setMethodName(methodUUID);
            return methodInfo;
        }
        
        else
        {
            String[] infos = methodUUID.split("_");
            methodInfo.setMethodName(infos[0]);
            Class[] classes = new Class[infos.length - 1];
            for(int i = 1; i < infos.length; i ++)
            {
                
                classes[i - 1] = BeanAccembleHelper.getClass(infos[i]);
            }
            methodInfo.setParamTypes(classes);
        }
        return methodInfo;
        
    }
    
    public static String buildMethodUUID(Method method)
    {
    	
    	return buildMethodUUID(method, null);
    }
	public String getPattern() {
		return pattern;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	
	/**
	 * 判断方法是不是一个模式匹配方法，如果是，则按照模式来匹配需要同步或需要进行事务控制的方法
	 * 
	 * @return
	 */
	public boolean isPattern()
	{
		return this.pattern != null && !this.pattern.equals("");
	}
	
	/**
	 * 判断参数传入的方法是否与同步方法匹配
	 * 首先判别所有的同步方法中是否有模式匹配方法
     * 没有则直接构建方法惟一id
	 * @return true 匹配
	 *         false 不匹配
	 */
	public boolean match(Method method)
	{
		return match(method, null);
    	
	}
	
	/**
	 * 判断方法是否与当前方法匹配
	 * 注意：本方法已经不考虑老版系统管理中直接通过方法名称指定同步方法的使用模式，因此如果
	 * 老版迁移到新版时，需要明确地指定方法的名称和方法的参数才能正常运行
	 * @param method 方法对象，如果method为null，将报空指针异常
	 * @param methodUUID 方法惟一标识
	 * @return true-匹配上
	 *         false-没有匹配上
	 *         
	 */
	public boolean match(Method method,String methodUUID)
	{
		try
		{			
	    	boolean match = false;
			if(isPattern())
			{
				if(pattern.equals("*")) //如果模式为*，表示匹配所有的方法
					return true;
				String methodname = method.getName();
				match = RegexUtil.isMatch(methodname, this.getPattern());	
				return match;
			}
			if(methodUUID == null)
				methodUUID = SynchronizedMethod.buildMethodUUID(method);
			if(this.getUUID().equals(methodUUID))
			{
				match = true;
			}
			return match;
		}
		catch(Exception e)
		{
//			e.printStackTrace();
			log.error(e.getMessage(),e);
			return false;
		}
    	
	}
	/**
	 * 
	 * @return
	 */
	public TransactionType getTxtype() {
		return txtype;
	}
	
//	private String txtype_s = "REQUIRED_TRANSACTION";
	/**
	 * 
	 * @param txtype
	 */
	public void setTxtype(String txtype) {
		if(txtype == null)
			return ;
//		txtype_s = txtype;
		if(txtype.equals("NEW_TRANSACTION"))
		{
			this.txtype = TransactionType.NEW_TRANSACTION;
		}
		else if(txtype.equals("REQUIRED_TRANSACTION"))
		{
			this.txtype = TransactionType.REQUIRED_TRANSACTION;
		}
		else if(txtype.equals("MAYBE_TRANSACTION"))
		{
			this.txtype = TransactionType.MAYBE_TRANSACTION;
		}
		else if(txtype.equals("NO_TRANSACTION"))
		{
			this.txtype = TransactionType.NO_TRANSACTION;
		}
		
		else if(txtype.equals("RW_TRANSACTION"))
        {
            this.txtype = TransactionType.RW_TRANSACTION;
        }
			
	
	}
	
	public void setTxtype(TransactionType txtype) {
        if(txtype == null)
            return ;
        this.txtype  = txtype;
//        if(txtype == TransactionType.NEW_TRANSACTION)
//        {            
//            txtype_s = "NEW_TRANSACTION";
//        }
//        else if(txtype == TransactionType.REQUIRED_TRANSACTION)
//        {
//            
//            txtype_s = "REQUIRED_TRANSACTION";
//        }
//        else if(txtype == TransactionType.MAYBE_TRANSACTION)
//        {
//            txtype_s = "MAYBE_TRANSACTION";
//
//        }
//        else if(txtype == TransactionType.NO_TRANSACTION)
//        {
//            txtype_s = "NO_TRANSACTION";
//
//        }
//        else if(txtype == TransactionType.RW_TRANSACTION)
//        {
//            txtype_s = "RW_TRANSACTION";
//        }
            
    
    }
	
	/**
	 * 判断异常是否是需要回滚事务的异常
	 * 以下异常必须回滚：
	 * com.frameworkset.orm.transaction.TransactionException
	 * javax.transaction.RollbackException
	 * java.sql.SQLException
	 * 
	 * 系统级别的异常也必须回滚：
	 * java.lang.NullpointException
	 * 
	 * 其他类型的异常要看看是否包含的rollbackExceptions中，如果在则需要回滚事务，
	 * 否则提交事务。
	 * 判断异常是否是回滚事务异常有两个规则：
	 * 规则1：异常只要是回滚异常列表中所定义的异常的本身或者子类，则为回滚异常
	 * 规则2：异常必须是
	 * @param throwable
	 * @return
	 */
	public boolean isRollbackException(Throwable throwable)
	{
		if(rollbackExceptions == null || rollbackExceptions.size() ==0)
			return true;
		String key = throwable.getClass().getName();
		Boolean t = (Boolean)rollbackExceptionIndexs.get(key);
		if(t != null)
		{
			return t.booleanValue();
		}
		else
		{
			try
			{				
				//系统级别的异常，需要回滚
				if((throwable instanceof TransactionException) 
						|| (throwable instanceof javax.transaction.RollbackException)
						|| throwable instanceof java.sql.SQLException
						|| throwable instanceof java.lang.RuntimeException
						|| throwable instanceof java.lang.Error)
				{
					t = new Boolean( true);
					rollbackExceptionIndexs.put(key, t);
					return true;
				}
				//如果声明了需要回滚事务的异常列表，则判断异常是否是列表中的异常
				else if(rollbackExceptions != null && rollbackExceptions.size() > 0) 
				{
					for(int i = 0;  i < this.rollbackExceptions.size(); i ++)
					{
						RollbackException re = (RollbackException)rollbackExceptions.get(i);
						if(re.getExceptionType() == RollbackException.TYPE_IMPLEMENTS)
						{
							if(throwable.getClass() == re.getExceptionClass())
							{
								t = new Boolean( true);
								rollbackExceptionIndexs.put(key, t);
								return true;
							}
						}
						else if(re.getExceptionType() == RollbackException.TYPE_INSTANCEOF)
						{
							if(re.getExceptionClass() != null && 
									re.getExceptionClass().isAssignableFrom(throwable.getClass()))
							{
								t = new Boolean( true);
								rollbackExceptionIndexs.put(key, t);
								return true;
							}
						}
							
					}
					t = new Boolean( false);
					rollbackExceptionIndexs.put(key, t);
					return false;
				}
				//如果不是系统异常，并且没有声明回滚事务的异常，则默认为回滚事务异常
				else
				{
					t = new Boolean( true);
					rollbackExceptionIndexs.put(key, t);
					return true;
				}
			}
			catch(Exception e)
			{
				
				
				t = new Boolean( true);
				rollbackExceptionIndexs.put(key, t);
				log.error(e.getMessage(),e);
				return true;
			}
		}
		
		
	}
	
	public static class MethodInfo  implements java.io.Serializable
	{
	    private String methodName;
	    private Class[] paramTypes;
        public String getMethodName()
        {
            return methodName;
        }
        public void setMethodName(String methodName)
        {
            this.methodName = methodName;
        }
        public Class[] getParamTypes()
        {
            return paramTypes;
        }
        public void setParamTypes(Class[] paramTypes)
        {
            this.paramTypes = paramTypes;
        }
	}
//	public String getTxtype_s() {
//		return txtype_s;
//	}
}
