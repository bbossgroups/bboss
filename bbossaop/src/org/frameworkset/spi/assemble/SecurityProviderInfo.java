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

import java.beans.IntrospectionException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.log4j.Logger;
import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.CallContext;

import com.frameworkset.common.util.NoSupportTypeCastException;
import com.frameworkset.spi.assemble.CurrentlyInCreationException;

/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company:bboss group
 * </p>
 * 
 * @author biaoping.yin
 * @version 1.0
 */
public class SecurityProviderInfo implements java.io.Serializable,BeanInf
{
    /**
	 * 
	 */
    private static final long serialVersionUID = 3932490475171037677L;

    private static Logger log = Logger.getLogger(SecurityProviderInfo.class);

    private boolean isdefault = false;

    private String type;

    private String providerClass;

    private boolean used;

    private ProviderManagerInfo providerManagerInfo;
    
    private BaseApplicationContext applicationContext;

    BeanAccembleHelper beanAccember = new BeanAccembleHelper();

    private Constructor constructor;

    /**
     * 设置多个服务提供者同步方法调用时的优先顺序，如果不指定该属性时按每个提供者的配置顺序来调用
     */
    private int prior = 0;

    /**
     * 单列模式下创建对象实例的同步锁
     */
    private Object lock = new Object();

    private Object provider;

    public static void main(String[] args)
    {
        SecurityProviderInfo securityprovider = new SecurityProviderInfo();
    }

    public boolean isIsdefault()
    {
        return isdefault;
    }

    public String getProviderClass()
    {
        return providerClass;
    }

    public String getType()
    {
        return type;
    }

    public boolean isUsed()
    {
        return used;
    }

    public ProviderManagerInfo getProviderManagerInfo()
    {
        return providerManagerInfo;
    }

//    public Object getSingleProvider() throws CurrentlyInCreationException
//    {
//        // TODO Auto-generated method stub
//        return getSingleProvider(null);
//    }

    public Object getProvider() throws CurrentlyInCreationException
    {
        // TODO Auto-generated method stub
        return getProvider(null);
    }

    // private Object getInstance(Context parent) throws InstantiationException,
    // IllegalAccessException,
    // ClassNotFoundException,
    // CurrentlyInCreationException
    // {
    // if(this.providerManagerInfo.getConstruction() != null)
    // {
    // List params = providerManagerInfo.getConstruction().getParams();
    // if(params.size() == 0)
    // return Class.forName(providerClass).newInstance();
    // else
    // {
    // try
    // {
    // if(parent == null)
    // {
    // return injectConstruction(new
    // Context(this.providerManagerInfo.getId()),params);
    // }
    // else
    // {
    // return injectConstruction(new
    // Context(parent,this.providerManagerInfo.getId()),params);
    // }
    // }
    // catch(CurrentlyInCreationException e)
    // {
    // throw e;
    // }
    // catch(InstantiationException e)
    // {
    // throw e;
    // }
    // catch(IllegalAccessException e)
    // {
    // throw e;
    // }
    // catch(ClassNotFoundException e)
    // {
    // throw e;
    // }
    // catch(Exception other)
    // {
    // throw new CurrentlyInCreationException(other);
    // }
    // }
    // }
    // else
    // {
    // return Class.forName(providerClass).newInstance();
    // }
    // }

    /**
     * 获取新的服务实例引用
     * 
     * @return
     */
    // public Object getProvider(Context parent) throws
    // CurrentlyInCreationException{
    // Object provider = null;
    // //if(provider == null)
    // {
    // try {
    // provider = getInstance(parent);
    // if(parent == null)
    // {
    // injectReferences(new Context(this.providerManagerInfo.getId()),provider);
    // }
    // else
    // {
    // injectReferences(new
    // Context(parent,this.providerManagerInfo.getId()),provider);
    // }
    // // provider.setProviderInfo(this);
    // } catch (ClassNotFoundException ex) {
    // log.error(ex);
    // ex.printStackTrace();
    // } catch (IllegalAccessException ex) {
    // log.error(ex);
    // ex.printStackTrace();
    // } catch (InstantiationException ex) {
    // log.error(ex);
    // ex.printStackTrace();
    // }
    // }
    // return provider;
    // }
    public Object getProvider(CallContext parent)
    {        
        if (this.getProviderManagerInfo().isSinglable())
        {
            if (provider == null)
            {
                synchronized (lock)
                {
                    if (provider != null)
                        return provider;
                    provider = beanAccember.getBean(this, parent);
                }
            }

            return provider;
        }
        else
        {
            return beanAccember.getBean(this, parent);
        }

    }

//    /**
//     * 获取服务的单实例引用
//     * 
//     * @return
//     * @throws CurrentlyInCreationException
//     */
//    public Object getSingleProvider(Context parent) throws CurrentlyInCreationException
//    {
//        if (provider == null)
//        {
//            synchronized (lock)
//            {
//                if (provider != null)
//                    return provider;
//                provider = getProvider(parent);
//            }
//        }
//        return provider;
//    }

    // /**
    // * 为服务提供者注入其他服务提供者的引用实例
    // * 如果相应refid和reftype对应的服务提供者不存在，或者参数对应的服务提供者没有提供
    // * 引用的注入方法，则该引用将被忽略掉
    // * @param context 检测是否有循环注入的上下文变量
    // * @param provider
    // * @throws IntrospectionException
    // */
    // private void injectReferences(Context context,
    // Object provider) throws CurrentlyInCreationException
    // {
    // //没有指定引用信息，直接返回
    //    	
    // try {
    // BeanInfo beanInfo = Introspector.getBeanInfo(provider.getClass());
    // PropertyDescriptor[] attributes = beanInfo.getPropertyDescriptors();
    // // System.out.println("attributes.length:"+attributes.length);
    // List refs = providerManagerInfo.getReferences();
    // if(refs == null || refs.size() == 0)
    // return ;
    // for(int i = 0; i < refs.size(); i ++)
    // {
    // Pro ref = (Pro)refs.get(i);
    // boolean flag = false;
    // String filedName = ref.getName();
    // if(ref.getValue() != null)
    // {
    //	    			
    // for (int n = 0; n < attributes.length; n++) {
    //	
    // // get bean attribute name
    // PropertyDescriptor propertyDescriptor = attributes[n];
    // String attrName = propertyDescriptor.getName();
    //	
    // if (filedName.equals(attrName)) {
    // flag = true;
    //		                   
    // Class type = propertyDescriptor.getPropertyType();
    //		                  
    //		                 
    // // create attribute value of correct type
    // Object value = ValueObjectUtil.typeCast(ref.getValue(),
    // ref.getValue().getClass(), type);
    // // PropertyEditor editor = PropertyEditorManager.findEditor(type);
    // // editor.setAsText(ref.getValue());
    // // Object value = editor.getValue();
    // Method wm = propertyDescriptor.getWriteMethod();
    //		                    
    // try {
    // wm.invoke(provider, new Object[]{value});
    // } catch (IllegalArgumentException e) {
    // throw new CurrentlyInCreationException(e);
    // } catch (IllegalAccessException e) {
    // throw new CurrentlyInCreationException(e);
    // } catch (InvocationTargetException e) {
    // throw new CurrentlyInCreationException(e);
    // }
    // // Object value = editor.getValue();
    // // set attribute value on bean
    //		                    
    // }
    // }
    //		    		
    // }
    // else if(ref.getClazz() != null)
    // {
    // Object value = Class.forName(ref.getClazz()).newInstance();
    // for (int n = 0; n < attributes.length; n++) {
    //	    				
    // // get bean attribute name
    // PropertyDescriptor propertyDescriptor = attributes[n];
    // String attrName = propertyDescriptor.getName();
    //	
    // if (filedName.equals(attrName)) {
    // flag = true;
    //		                   
    // // Class type = propertyDescriptor.getPropertyType();
    //		                  
    //		                 
    // // create attribute value of correct type
    // // Object value = ValueObjectUtil.typeCast(ref.getValue(),
    // ref.getValue().getClass(), type);
    // // PropertyEditor editor = PropertyEditorManager.findEditor(type);
    // // editor.setAsText(ref.getValue());
    // // Object value = editor.getValue();
    // Method wm = propertyDescriptor.getWriteMethod();
    //		                    
    // try {
    // wm.invoke(provider, new Object[]{value});
    // } catch (IllegalArgumentException e) {
    // throw new CurrentlyInCreationException(e);
    // } catch (IllegalAccessException e) {
    // throw new CurrentlyInCreationException(e);
    // } catch (InvocationTargetException e) {
    // throw new CurrentlyInCreationException(e);
    // }
    // // Object value = editor.getValue();
    // // set attribute value on bean
    //		                    
    // }
    // }
    // }
    // else if(ref.getRefid() != null)
    // {
    //		    		
    // if(filedName == null) //如果没有指定引用字段对应的名称，则直接使用refid作为字段名称
    // filedName = ref.getRefid();
    // for (int n = 0; n < attributes.length; n++) {
    //	
    // // get bean attribute name
    // PropertyDescriptor propertyDescriptor = attributes[n];
    // String attrName = propertyDescriptor.getName();
    //	
    // if (filedName.equals(attrName)) {
    // flag = true;
    // if(context.isLoopIOC(ref.getRefid()))
    // {
    //		                		
    // throw new CurrentlyInCreationException(
    // "loop inject error the inject context path is [" +
    // context + ">" + ref.getRefid() + "]");
    // }
    // // get value in props
    // Object reference = null;
    // try {
    //								
    // reference =
    // BaseSPIManager.getProvider(context,ref.getRefid(),ref.getReftype());
    // Method wm = propertyDescriptor.getWriteMethod();
    // wm.invoke(provider, new Object[]{reference});
    // } catch (SPIException e) {
    // if(e.getCause() != null && e.getCause() instanceof
    // CurrentlyInCreationException)
    // {
    // throw (CurrentlyInCreationException)e.getCause();
    // }
    // else
    // throw new CurrentlyInCreationException(e);
    // } catch (IllegalArgumentException e) {
    // throw new CurrentlyInCreationException(e);
    // } catch (IllegalAccessException e) {
    // throw new CurrentlyInCreationException(e);
    // } catch (InvocationTargetException e) {
    // throw new CurrentlyInCreationException(e);
    // }
    //	
    // // Class type = propertyDescriptor.getPropertyType();
    // // System.out.println("props: " + attrName);
    // // System.out.println("propsVal: " + propsVal);
    // // create attribute value of correct type
    // // PropertyEditor editor = PropertyEditorManager.findEditor(type);
    // // editor.setValue(reference);
    // // Object value = editor.getValue();
    // // set attribute value on bean
    //		                    
    // }
    // }
    // }
    // if(!flag) //引用字段名称在provider中没有定义
    // {
    // System.out.println("引用字段[" + filedName + "]在provider[" +
    // provider.getClass() + "]中没有定义");
    // log.warn("引用字段[" + filedName + "]在provider[" + provider.getClass() +
    // "]中没有定义");
    // }
    //	    		
    // }
    // } catch (IntrospectionException e1) {
    // throw new CurrentlyInCreationException(e1);
    // } catch (NumberFormatException e) {
    // throw new CurrentlyInCreationException(e);
    // } catch (IllegalArgumentException e) {
    // throw new CurrentlyInCreationException(e);
    // } catch (NoSupportTypeCastException e) {
    // throw new CurrentlyInCreationException(e);
    // } catch (InstantiationException e) {
    // throw new CurrentlyInCreationException(e);
    // } catch (IllegalAccessException e) {
    // throw new CurrentlyInCreationException(e);
    // } catch (ClassNotFoundException e) {
    // throw new CurrentlyInCreationException(e);
    // }
    //    	
    //    	
    //    	
    // }

    /**
     * 为服务提供者注入其他服务提供者的引用实例 如果相应refid和reftype对应的服务提供者不存在，或者参数对应的服务提供者没有提供
     * 引用的注入方法，则该引用将被忽略掉
     * 
     * @param context
     *            检测是否有循环注入的上下文变量
     * @param provider
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IntrospectionException
     * @throws IntrospectionException
     * @throws NoSupportTypeCastException
     */
    // private Object injectConstruction(Context context,
    // List params) throws CurrentlyInCreationException,
    // ClassNotFoundException, InstantiationException, IllegalAccessException,
    // SecurityException, NoSuchMethodException, IllegalArgumentException,
    // InvocationTargetException, IntrospectionException,
    // NoSupportTypeCastException
    // {
    // //没有指定引用信息，直接返回
    //    	
    // // try {
    //			
    // // BeanInfo beanInfo =
    // Introspector.getBeanInfo(Class.forName(providerClass));
    // // Class[] parameterTypes = new Class[params.size()];
    // Constructor constructor = getConstructor(providerClass,params);
    //    		
    // Class[] parameterTypes = constructor.getParameterTypes();
    // Object[] values = new Object[params.size()];
    //			
    // for(int i = 0; i < params.size(); i ++)
    // {
    // // Class.forName(providerClass)
    // Param param = (Param)params.get(i);
    // // param.getParamType();
    // // param.getRefid();
    // // param.getValue();
    //				
    // if(param.getRefid() != null) //引用其他的管理服务
    // {
    // // ProviderManagerInfo providerManagerInfo =
    // ServiceProviderManager.getInstance().
    // // getProviderManagerInfo(param.getRefid());
    // if(context.isLoopIOC(param.getRefid()))
    // {
    // throw new CurrentlyInCreationException(
    // "loop inject constructor error. the inject context path is [" +
    // context + ">" + param.getRefid() + "]");
    // }
    // // get value in props
    // Object reference = null;
    // try {
    //						
    // reference =
    // BaseSPIManager.getProvider(context,param.getRefid(),param.getReftype());
    // values[i] = reference;
    // } catch (SPIException e) {
    // Throwable cause = e.getCause();
    // if(cause != null && cause instanceof CurrentlyInCreationException)
    // // e.printStackTrace();
    // throw (CurrentlyInCreationException)cause;
    // else
    // throw new CurrentlyInCreationException(e);
    // }
    // // String clazz =
    // providerManagerInfo.getDefaulProviderInfo().getProviderClass();
    // // parameterTypes[i] = Class.forName(clazz);
    // }
    // else if(param.getValue() != null)
    // {
    // // parameterTypes[i] = Class.forName(param.getParamType());
    // Object value = ValueObjectUtil.typeCast(param.getValue(),
    // param.getValue().getClass(), parameterTypes[i]);
    // // PropertyEditor editor =
    // PropertyEditorManager.findEditor(parameterTypes[i]);
    // // editor.setAsText(param.getValue());
    // // Object value = editor.getValue();
    // values[i] = value;
    // }
    // else if(param.getClazz() != null)
    // {
    // // parameterTypes[i] = Class.forName(param.getParamType());
    // Class type = Class.forName(param.getClazz());
    // values[i] = type.newInstance();
    // }
    // else
    // {
    // throw new CurrentlyInCreationException(
    // "Inject constructor error: illegal parameter  the inject context path is "
    // + context);
    // }
    //				
    // // parameterTypes[i] = ((Param)params.get(i)).getParamType();
    //				
    // }
    // // Constructor constructor =
    // Class.forName(providerClass).getConstructor(parameterTypes);
    //			
    // // Constructor[] constructor =
    // Class.forName(providerClass).getConstructors();
    // return constructor.newInstance(values);
    //			
    //
    // // } catch (IntrospectionException e1) {
    // // throw e1;
    // // }
    //    	
    //    	
    //    	
    // }
    // public Constructor getConstructor(String clazz,List params) throws
    // SecurityException, ClassNotFoundException, CurrentlyInCreationException
    // {
    // if(this.constructor != null)
    // return constructor;
    // Constructor[] constructors = Class.forName(clazz).getConstructors();
    //    	
    // if(constructors == null || constructors.length == 0)
    // throw new CurrentlyInCreationException(
    // "Inject constructor error: no construction define in the " + clazz);
    // int l = constructors.length;
    //    	
    // if(l == 1 || params == null || params.size() == 0)
    // {
    // return constructor = constructors[0];
    // }
    //    	
    // else
    // {
    // int size = params.size();
    // Class[] types = null;
    // for(int i = 0; i < l; i ++)
    // {
    // Constructor temp = constructors[i];
    // types = temp.getParameterTypes();
    // if(types != null && types.length == size)
    // {
    // return constructor = temp;
    // }
    // }
    // }
    // throw new CurrentlyInCreationException(
    // "Inject constructor error: Parameters with construction defined in the "
    // + clazz + " is not matched with the config paramenters .");
    // }
    public void setIsdefault(boolean isdefault)
    {
        this.isdefault = isdefault;
    }

    private Class providerClass_ = null;

    public Class getProviderClass_()
    {
        if(providerClass_ == null)
            try
              {
                  providerClass_ = BeanAccembleHelper.getClass(providerClass);
              }
              catch (ClassNotFoundException e)
              {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
              }
        return providerClass_;
    }

    public void setProviderClass(String providerClass)
    {

        this.providerClass = providerClass;
//        if (providerClass != null && !providerClass.equals(""))
//        {
//            try
//            {
//                providerClass_ = BeanAccembleHelper.getClass(providerClass);
//            }
//            catch (ClassNotFoundException e)
//            {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public void setUsed(boolean used)
    {
        this.used = used;
    }

    public void setProviderManagerInfo(ProviderManagerInfo providerManager)
    {
        this.providerManagerInfo = providerManager;
    }

    public int getPrior()
    {
        return prior;
    }

    public void setPrior(int prior)
    {
        this.prior = prior;
    }

    public String toString()
    {
        StringBuffer ret = new StringBuffer();
        ret.append("type=").append(this.type).append(",").append("class=").append(this.getProviderClass()).append(",")
                .append("used=").append(this.isUsed()).append(",").append("default=").append(this.isIsdefault())
                .append(",").append("prior=").append(this.getPrior() + "");
        return ret.toString();
    }

    public Class getBeanClass()
    {
        // TODO Auto-generated method stub
        return getProviderClass_();
    }

    public Construction getConstruction()
    {
        // TODO Auto-generated method stub
        return getProviderManagerInfo().getConstruction();
    }

    public List<Pro> getConstructorParams()
    {
        // TODO Auto-generated method stub
        if(getProviderManagerInfo().getConstruction() != null)
            return getProviderManagerInfo().getConstruction().getParams();
        else
            return null;
    }

    public String getName()
    {        
        return getProviderManagerInfo().getId();
    }

    public List<Pro> getReferences()
    {
        
        return getProviderManagerInfo().getReferences();
    }
    
    private String configFile; 
    public String getConfigFile()
    {
        // TODO Auto-generated method stub
        return configFile;
    }

    public void setConfigFile(String configFile)
    {
       this.configFile = configFile;
        
    }
    
    /**
     * init-method，destroy-method两个属性分别对应aop框架提供的两个InitializingBean和DisposableBean
		实现的方法，如果组件已经实现了InitializingBean就不需要指定init-method属性
		如果组件实现了DisposableBean接口就不需要指定destroy-method属性
     */
    
    /**
     * bean销毁方法，单列模式时使用
     */
    private String destroyMethod = null;
    
    /**
     * bean初始化方法
     */
    private String initMethod = null;
    
    
	
	public String getInitMethod()
	{
	
		return initMethod;
	}

	
	public void setInitMethod(String initMethod)
	{
	
		this.initMethod = initMethod;
	}

	public String getDestroyMethod()
	{
	
		return destroyMethod;
	}
	
	public void setDestroyMethod(String destroyMethod)
	{
	
		this.destroyMethod = destroyMethod;
	}

    public BaseApplicationContext getApplicationContext()
    {
       return applicationContext;
        
    }

    public boolean isSinglable()
    {
        // TODO Auto-generated method stub        
        return this.providerManagerInfo.isSinglable();
    }

	/**
	 * @param applicationContext the applicationContext to set
	 */
	public void setApplicationContext(BaseApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public String getFactory_bean() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getFactory_class() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getFactory_method() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getXpath() {
		// TODO Auto-generated method stub
		return null;
	}

	public Class getFactoryClass() {
		// TODO Auto-generated method stub
		return null;
	}

}
