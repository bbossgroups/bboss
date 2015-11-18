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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.apache.log4j.Logger;
import org.frameworkset.soa.SerialFactory;
import org.frameworkset.soa.SerialFactory.MagicClass;
import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.SPIException;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 * 
 * 
 * <p>
 * Title: ProviderParser.java
 * </p>
 * 
 * <p>
 * Description: 管理服务配置文件解析器,bean定义
 * 针对soa的优化节点和属性对照表
properties转换为ps
property转换为p
name转换为n
value转换为v
class转换为cs
list转换为l
array转换为a
map转换为m
set转换为s
soa:type_null_value转换为s:nvl
soa:type转换为s:t
componentType转换为cmt
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * 
 * <p>
 * bboss workgroup
 * </p>
 * 
 * @Date Sep 8, 2008 10:02:46 AM
 * @author biaoping.yin,尹标平
 * @version 1.0
 */
public class ProviderParser extends DefaultHandler
{
    private Stack traceStack;

    private StringBuffer currentValue;

    private Map managers;

    private List mangerimports;
    
    private boolean serial = false;

    public void setSerial(boolean serial) {
		this.serial = serial;
	}

	private Map<String,Pro> properties = new HashMap<String,Pro>();
    
    private BaseApplicationContext applicationContext;  
    
    

    public Map getProperties()
    {
        return properties;
    }

    LinkConfigFile parent;

    private String file;

    private static Logger log = Logger.getLogger(ProviderParser.class);

    public ProviderParser(BaseApplicationContext applicationContext,String file, LinkConfigFile parent)
    {
        traceStack = new Stack();
        managers = new HashMap();
        mangerimports = new ArrayList();
//        currentValue = new StringBuffer();
        this.file = file;
        this.parent = parent;
        this.applicationContext = applicationContext;
//        isSOAApplicationContext = applicationContext instanceof SOAApplicationContext;
    }
    
    public ProviderParser(BaseApplicationContext applicationContext)
    {
        
        if(applicationContext.isfile())
        {
	        managers = new HashMap();
	        mangerimports = new ArrayList();
//	        this.file = applicationContext.getConfigfile();
//	        this.parent = applicationContext.getp;
        }
//        currentValue = new StringBuffer();
        this.applicationContext = applicationContext;
//        isSOAApplicationContext = applicationContext instanceof SOAApplicationContext;
        traceStack = new Stack();
    }

    public void characters(char[] ch, int start, int length)
    {    	
    	if(currentValue == null)
    		currentValue = new StringBuffer();
        currentValue.append(ch, start, length);
    }
    /**
     * 判断组件是否是bean组件，如果是返回true
     * 如果是普通属性，或者是引用那么返回false
     * @param p
     * @return
     */
    private boolean isbean(Pro p)
    {

		boolean isbean = p.getValue() == null && p.getClazz() != null && !p.getClazz().equals("") && (p.getRefid() == null || p.getRefid().equals("")) ;
    	if(isbean)
    		return true;
    	if(p.getMagicclass() != null)
    		return true;
    	if(p.getValue() != null)
    	{
    		isbean = p.getValue() instanceof ProList || p.getValue() instanceof ProMap || p.getValue() instanceof ProSet || p.getValue() instanceof ProArray; 
    	}
    	if(isbean)
    		return true;
    	String factory_bean = p.getFactory_bean();
    	
    	if(factory_bean != null && !factory_bean.equals(""))
    		return true;
    	String factory_class = p.getFactory_class();
    	if(factory_class != null && !factory_class.equals(""))
    		return true;
    	String iocplugin = p.getIocplugin();
		if(iocplugin != null && !iocplugin.equals(""))
    		return true;		
    	return false;
    	
    }
    private void endProperty(Pro p,String s1, String s2, String name)
    {
//    	Pro p = (Pro) traceStack.pop();
    	boolean isinterceptor = (p instanceof InterceptorInfo);
    	if(!isinterceptor)
    	{
	        if ( p.getValue() == null)
	        {		
	        	String _value = null;
//	        	if(this.serial)
//	        		_value = currentValue.toString();
//	        	else
//	        		_value = currentValue.toString().trim();
	        	if(this.currentValue != null)
	        	{
	        		if(this.serial)
	        		{
	        			
	        			_value = this.currentValue.toString();
	        			
	    	            {
	    	        		p.setValue(_value);
//	    	            	currentValue.delete(0, currentValue.length());
	    	        		this.currentValue = null;
	    	            }
	        		}
	        		else
	        		{
	        			_value = this.currentValue.toString().trim();
	        			if(_value.length() > 0)
	    	            {
	    	        		p.setValue(_value);
//	    	            	currentValue.delete(0, currentValue.length());
	    	        		this.currentValue = null;
	    	            }
	        		}
	        	}
	        	
	//            else if(p.getClazz() != null && !p.getClazz().equals("") 
	//                    && (p.getRefid() == null || p.getRefid().equals("")))
//	            else if(isbean(p))@setBean 注释开始
//	            {
//	                p.setBean(true);
//	                
//	            }@setBean 注释结束
	        }
    	}
    	

		if(isbean(p))
        {
            p.setBean(true);
            
        }
    	
        
        if(traceStack.size() > 0)
        {
            Object value = this.traceStack.peek();
            if(value instanceof Pro)
            {
            	if(!isinterceptor )
            	{

	            	Pro pro = (Pro)value;
	            	pro.addReferenceParam(p);
            	}
            	else
            	{
	            	BaseTXManager providerManagerInfo = (BaseTXManager)value;
                	providerManagerInfo.addInterceptor((InterceptorInfo)p);
            	}
            	
            }
            else if (value instanceof List)
            {
                ProList<Pro> list = (ProList<Pro>) value;
//                list.add(p.getValue());
                list.add(p);
            }
            else if (value instanceof Map)
            {
            	ProMap<String,Pro> map = (ProMap<String,Pro>)value;
//                map.put(p.getName(), p.value);
                map.put(p.getName(), p);
                
            }
            else if (value instanceof ProArray)
            {
            	ProArray set = (ProArray)value;
//                set.add(p.value);
                set.addPro(p);
            }
            
            else if(value instanceof Construction)
            {
            	Construction construction = (Construction)value;
            	construction.addParam(p);
            }
            else if (value instanceof Set)
            {
            	ProSet<Pro> set = (ProSet<Pro>)value;
//                set.add(p.value);
                set.add(p);
            }
            
            else if(value instanceof ProviderManagerInfo)
            {
            	ProviderManagerInfo providerManagerInfo = (ProviderManagerInfo) value;
            	if(!isinterceptor )
            	{
            		providerManagerInfo.addReference(p);
            	}
            	else
            	{
            		providerManagerInfo.addInterceptor((InterceptorInfo)p);
            	}
//            	if(p.getName() != null)
//            		p.setUuid(providerManagerInfo.getId() + Pro.id_mask + p.getName());
//            	else
//            	{
//            		p.setUuid(providerManagerInfo.getId());
//            	}
            }
            else
            {
                this.properties.put(p.getName(), p);
            }
        }
        else
        {
            this.properties.put(p.getName(), p);
        }
//        /**
//         * 初始化事务信息
//         */
//        p.initTransactions();
        p.freeze();
        p = null;

    }
    @SuppressWarnings("unchecked")
	public void endElement(String s1, String s2, String name)
    {
    	if (name.equals("p") || name.equals("property"))
        {
    		Pro p = (Pro) traceStack.pop();
    		if(p.getIocplugin()!= null)
    			System.out.println();
    		endProperty(p, s1, s2, name);

        }
    	 else if (name.equals("l")||name.equals("list"))
         {
             ProList list = (ProList) this.traceStack.pop();
             Pro pro = (Pro) this.traceStack.peek();
//             List l = java.util.Collections.unmodifiableList(list);
             list.freeze();
             pro.setValue(list);
             pro.setList(true);

         }
    	 else if (name.equals("m") || name.equals("map"))
         {
             ProMap map = (ProMap) this.traceStack.pop();
             Pro pro = (Pro) this.traceStack.peek();
//             Map t = java.util.Collections.unmodifiableMap(map);
             map.freeze();
             pro.setValue(map);
             pro.setMap(true);

         }
    	 else if (name.equals("construction"))
         {
             Construction construction = (Construction) this.traceStack.pop();

         }
         else if (name.equals("a") || name.equals("array"))
         {
             ProArray array = (ProArray) this.traceStack.pop();

             Pro pro = (Pro) this.traceStack.peek();
//             List l = java.util.Collections.unmodifiableList(list);
             array.freeze();
             pro.setValue(array);
             pro.setArray(true);

         }
         
         else if (name.equals("s") || name.equals("set"))
         {
             ProSet set = (ProSet) this.traceStack.pop();
             Pro pro = (Pro) this.traceStack.peek();
//             Set t = java.util.Collections.unmodifiableSet(set);
             set.freeze();
             pro.setValue(set);
             pro.setSet(true);

         }
    	else if (name.equals("manager"))
        {
            ProviderManagerInfo providerManger = (ProviderManagerInfo)traceStack.pop();
            providerManger.unmodify();
        }
        else if (name.equals("provider"))
        {
            traceStack.pop();
        }
        else if (name.equals("synchronize"))
        {

            // do nothing

        }
        else if (name.equals("managerimport"))
        {

            // do nothing;
        }
        else if (name.equals("transactions"))
        {
            Transactions txs = (Transactions)traceStack.pop();
            txs.unmodify();

        }
        else if (name.equals("reference"))
        {
            // do nothing

        }

        else if (name.equals("method"))
        {

            SynchronizedMethod synchronizedMethod = (SynchronizedMethod) this.traceStack.pop();
            Object parent = this.traceStack.peek();
            if (parent instanceof ProviderManagerInfo)
            {
                ProviderManagerInfo providerManagerInfo = (ProviderManagerInfo) this.traceStack.peek();

                providerManagerInfo.addSynchronizedMethod(synchronizedMethod);
            }
            else if(parent instanceof InterceptorInfo)
            {
            	InterceptorInfo txs = (InterceptorInfo) this.traceStack.peek();
                txs.addInterceptMethod(synchronizedMethod);
            }
            else
            {
                Transactions txs = (Transactions) this.traceStack.peek();
                txs.addTransactionMethod(synchronizedMethod);
            }
        }
        else if (name.equals("rollbackexceptions"))
        {
            // do nothing
        }
        else if (name.equals("exception"))
        {
            // do nothing

        }
       
        else if (name.equals("param")) // 添加方法参数信息
        {
            // do nothing
        }
        
        else if (name.equals("description"))
        {
            if(this.traceStack.size() <= 0)
            {
                
//                System.out.println(currentValue);
//                currentValue.delete(0, currentValue.length());
            	this.currentValue = null;
                return ;
            }
            Object p = this.traceStack.peek();
            if(p instanceof Pro)
            {
                Pro pro = (Pro)p;
                pro.setDescription(this.currentValue.toString());
            }
        }
       
        else if(name.equals("editor"))
        {
            
        }
        else if (name.equals("interceptor"))
        {
        	InterceptorInfo interceptor = (InterceptorInfo)this.traceStack.pop();
        	this.endProperty(interceptor,s1, s2, name);
        	
//        	Object obj = this.traceStack.peek();
//        	
//        	BaseTXManager providerManagerInfo = (BaseTXManager) obj;
//            InterceptorInfo interceptor = new InterceptorInfo();
//            String clazz = attributes.getValue("class");
//            interceptor.setClazz(clazz);
//
//            providerManagerInfo.addInterceptor(interceptor);

        }

//       currentValue.delete(0, currentValue.length());
       this.currentValue = null;
       

    }
    
    @SuppressWarnings("unchecked")
    private void setFAttr(Pro property,Attributes attributes)
    {
    	if(attributes == null || attributes.getLength() == 0)
    		return;
    	int length = attributes.getLength();
    	
    	Map<String,Object> extendsAttributes = new HashMap<String,Object>();
    	Map<String,String> pathAttributes = new HashMap<String,String>();
    	Map<String,String> WSAttributes = new HashMap<String,String>();
    	Map<String,String> SOAAttributes = new HashMap<String,String>();
    	Map<String,String> RMIAttributes = new HashMap<String,String>();
    	for(int i = 0; i < length; i ++)
    	{
    		String name = attributes.getQName(i);
    		if(name.equals("n") || name.equals("name"))
    		{
    			continue;
    		}
    		
    		else if(name.startsWith("s:"))//soa:
    		{
    			
    			SOAAttributes.put(name, attributes.getValue(i));
    		}
    		else if( name.equals("cs") || name.equals("v") || name.equals("class") || name.equals("value"))
    		{
    			continue;
    		}
    		
    		else if(name.startsWith("f:"))//通过property的属性来制定对象中field的值
    		{
    			Pro f = new Pro(applicationContext);
    			
    			f.setName(name.substring(2));
    			
    			String value = attributes.getValue(i);
    			if(value.startsWith(ServiceProviderManager.SERVICE_PREFIX) 
    					|| value.startsWith(ServiceProviderManager.ATTRIBUTE_PREFIX))
    			{
    				f.setRefid(value);
    			}
    			else
    			{
    				f.setValue(value);
    			}
    			//增加xpath信息
    			f.setXpath(property.getXpath() + Pro.REF_TOKEN + f.getName());
    			property.addReferenceParam(f);
    		}
    		else if(name.startsWith("path:"))
    		{
    			pathAttributes.put(name,attributes.getValue(i));
    		}
    		else if(name.startsWith("ws:"))
    		{
    			WSAttributes.put(name, attributes.getValue(i));
    		}
    		
    		else if(name.startsWith("rmi:"))
    		{
    			
    			RMIAttributes.put(name, attributes.getValue(i));
    		}
    			
    		else if(!Pro.isFixAttribute(name))
    		{
    		    extendsAttributes.put(name, attributes.getValue(i));
    		}
    		    
    	}
    	if(pathAttributes .size() > 0)
    	{
    		property.setMvcpaths(pathAttributes);
    	}
    	if(WSAttributes.size() > 0)
    		property.setWSAttributes(WSAttributes);
    	if(SOAAttributes.size() > 0)
    	{
    		property.setSOAAttributes(SOAAttributes);
    	}
    	if(RMIAttributes.size() > 0)
    	{
    		property.setRMIAttributes(RMIAttributes);
    	}
    	property.setExtendsAttributes(extendsAttributes);
//    	return null;
    }
    
    /**
     * 构建元素的xpath值
     * @param p
     * @return
     */
    private void buildXpath(Pro p)
    {
    	String xpath = null;
    	if(traceStack.size() > 0)
        {
            Object value = this.traceStack.peek();
            if(value instanceof Pro)
            {
            	Pro pro = (Pro)value;
            	xpath = pro.getXpath() + Pro.REF_TOKEN + p.getName();
            	p.setXpath(xpath);
            }
            else if (value instanceof List)
            {
                ProList<Pro> list = (ProList<Pro>) value;
                Pro pp = (Pro)traceStack.get(traceStack.size() - 2);
//                list.add(p.getValue());
                if(list.size() > 0)
                	xpath = pp.getXpath() + "[" + (list.size() - 1) + "]";
                else
                	xpath = pp.getXpath() + "[0]";
//                list.add(p);
                p.setXpath(xpath);
            }
            else if (value instanceof Map)
            {
//            	ProMap<String,Pro> map = (ProMap<String,Pro>)value;
//                map.put(p.getName(), p.value);
//                map.put(p.getName(), p);
                Pro pp = (Pro)traceStack.get(traceStack.size() - 2);
//              list.add(p.getValue());
//              if(list.size() > 0)
              	xpath = pp.getXpath() + "[" + p.getName() + "]";
//              else
//              	xpath = pp.getXpath() + "[0]";
//              list.add(p);
              	p.setXpath(xpath);
                
            }
            else if (value instanceof ProArray)
            {
            	ProArray set = (ProArray)value;
//                set.add(p.value);
//                set.addPro(p);
                Pro pp = (Pro)traceStack.get(traceStack.size() - 2);
//              list.add(p.getValue());
              if(set.size() > 0)
              	xpath = pp.getXpath() + "[" + (set.size() - 1) + "]";
              else
              	xpath = pp.getXpath() + "[0]";
//              list.add(p);
              p.setXpath(xpath);
            }
            else if(value instanceof Construction)
            {
            	Construction construction = (Construction)value;
//            	construction.addParam(p);
            	Pro pp = (Pro)traceStack.get(traceStack.size() - 2);
//              list.add(p.getValue());
              if(construction.getParams() != null && construction.getParams().size() > 0)
              	xpath = pp.getXpath() + "{" + (construction.getParams().size() - 1) + "}";
              else
              	xpath = pp.getXpath() + "{0}";
//              list.add(p);
              p.setXpath(xpath);
            }
            else if (value instanceof Set)
            {
            	ProSet<Pro> set = (ProSet<Pro>)value;
            	 Pro pp = (Pro)traceStack.get(traceStack.size() - 2);
//               list.add(p.getValue());
               if(set.size() > 0)
               	xpath = pp.getXpath() + "[" + (set.size() - 1) + "]";
               else
               	xpath = pp.getXpath() + "[0]";
//               list.add(p);
               p.setXpath(xpath);
            }
            
            else if(value instanceof ProviderManagerInfo)
            {
//            	ProviderManagerInfo providerManagerInfo = (ProviderManagerInfo) value;
//            	
//            	
//            	
//            	xpath = providerManagerInfo.getXpath() + Pro.REF_TOKEN + p.getName();
//            	p.setXpath(xpath);
            	xpath = p.getName();
           	 	p.setXpath(xpath);
//            	if(p.getName() != null)
//            		p.setUuid(providerManagerInfo.getId() + Pro.id_mask + p.getName());
//            	else
//            	{
//            		p.setUuid(providerManagerInfo.getId());
//            	}
            }
            else
            {
            	xpath = p.getName();
            	 p.setXpath(xpath);
            }
        }
        else
        {
        	xpath = p.getName();
        	 p.setXpath(xpath);
        }
//    	return xpath;
    }
    
    private void startProperty(Pro p,String s1, String s2, String name, Attributes attributes)
    {
    	
        String name_ = null;
        String value = null;
        String clazz = null;
        if(this.serial)
        {
        	 name_ = attributes.getValue("n");
        	 if(name_ == null)
        	 {
        		 name_ = attributes.getValue("name");
        		 if(name_ == null || name_.equals(""))
                 {
                 	 String id = attributes.getValue("id");
                 	 name_ = id;
                 }
        	 }              
        	value = attributes.getValue("v");   
        	if(value == null)
        		value = attributes.getValue("value");
        	 clazz = attributes.getValue("cs");
//	          if(clazz == null) //@setBean 注释开始
//	          {
//	        	  clazz = attributes.getValue(Pro.soa_type_attribute);
//	          }//@setBean 注释结束
	          if(clazz == null)
	            clazz = attributes.getValue("class");
	            
        }
        else
        {
        	 name_ = attributes.getValue("name");                 
             if(name_ == null || name_.equals(""))
             {
             	 String id = attributes.getValue("id");
             	name_ = id;
             }
        	 value = attributes.getValue("value");
        	 clazz = attributes.getValue("class");
          
        }
        
        if (name_ != null && !name_.equals(""))
        {
            p.setName(name_.trim());                
        }

        if (value != null)
        {
            p.setValue(value);
        }
        if (clazz != null && !clazz.equals(""))
        {
            p.setClazz(clazz);
        }
        String refid = attributes.getValue("refid");
       
        String label = attributes.getValue("label");
        String factory_bean = attributes.getValue("factory-bean");
        String factory_class = attributes.getValue("factory-class");
        String factory_method = attributes.getValue("factory-method");
        String iocplugin = attributes.getValue("iocplugin");
        boolean singlable = getBoolean(attributes.getValue("singlable"), true);
        if(iocplugin != null)
        {
        	  p.setIocplugin(iocplugin);
        }
        String enablerpc = attributes.getValue("enablerpc");
        if(enablerpc != null && enablerpc.equals("true"))
        {
        	p.setEnablerpc(true);
        }
      
        p.setConfigFile(this.file);
        p.setSinglable(singlable);
        p.setFactory_bean(factory_bean);
        p.setFactory_class(factory_class);
        p.setFactory_method(factory_method);
        String destroyMethod = attributes.getValue("destroy-method");
        p.setDestroyMethod(destroyMethod);
        String initMethod = attributes.getValue("init-method");
        p.setInitMethod(initMethod);
        //callorder_sequence标识组件配置的拦截器以顺序方式执行还是以堆栈方式执行,
        //true:顺序方式执行，拦截器中的每个方法都是按照配置顺序执行，before、after、afterthrow、afterfinally都是按照先后顺序执行
        //false:链式方式执行，拦截器中的每个方法都是按照配置堆栈方式执行执行，
        //先按先后顺序执行所有拦截器的before方法，然后按逆序执行所有拦截器的after、afterthrow、afterfinally三个方法
        //默认值为false
        p
        .setCallorder_sequence(getBoolean(attributes.getValue("callorder_sequence"), false));
        this.buildXpath(p);
//        if(this.serial)
        {
	        String mg = attributes.getValue("mg");
	        if(mg != null)
	        {
		        p.setMagicNumber(mg);
//		        String serial = attributes.getValue("serial");
//		        String preserial = attributes.getValue("preserial");
//		        if(serial == null && preserial == null)
		        {
		        	MagicClass mgc = SerialFactory.getSerialFactory().getMagicClassByMagicNumber(mg);
		        	if(mgc == null)
		        		throw new java.lang.IllegalArgumentException("magic="+mg + "对应的MagicClass不存在,请检查org/frameworkset/soa/serialconf.xml文件中是否定义了magic='"+mg+"'的序列化插件.");
		        	if(mgc.getMagicclass() != null && p.getClazz() == null)
		        	{
		        		 p.setClazz(mgc.getMagicclass());
		        	}
		        	p.setMagicclass(mgc);
		        }
	        }
        }
        setFAttr(p, attributes);
        if(label != null && !label.equals(""))
            p.setLabel(label);
       
        if (refid != null && !refid.equals(""))
        {
            p.setRefid(refid);
        }
        
       
       
    }
    public void startElement(String s1, String s2, String name, Attributes attributes)
    {
    	    	
//        currentValue.delete(0, currentValue.length());
    	currentValue = null;
        if (name.equals("p") || name.equals("property"))
        {    

        	Pro p = new Pro(applicationContext);
        	startProperty(p,s1, s2, name, attributes);
        	this.traceStack.push(p);

        }
        else if (name.equals("l")||name.equals("list"))
        {
        	ProList list = new ProList();
        	String componentType = null;        	 
//        	if(this.serial)
        	{
        		componentType = attributes.getValue("cmt");
        		if(componentType == null)
        			componentType = attributes.getValue("componentType");
        	}
//        	else
//        	{
//        		componentType = attributes.getValue("componentType");
//        	}
        	 list.setComponentType(componentType);
            this.traceStack.push(list);

        }
        else if (name.equals("m") || name.equals("map"))
        {
        	ProMap map = new ProMap<String,Pro>();
        	String componentType = null;        	 
//        	if(this.serial)
        	{
        		componentType = attributes.getValue("cmt");
        		if(componentType == null)
        			componentType = attributes.getValue("componentType");
        	}
//        	else
//        	{
//        		componentType = attributes.getValue("componentType");
//        	}
            map.setComponentType(componentType);
            
            this.traceStack.push(map);

        }  
        else if (name.equals("a") || name.equals("array"))
        {
        	ProArray array = new ProArray();
        	String componentType = null;        	 
//        	if(this.serial)
        	{
        		componentType = attributes.getValue("cmt");
        		if(componentType == null)
        			componentType = attributes.getValue("componentType");
        	}
//        	else
//        	{
//        		componentType = attributes.getValue("componentType");
//        	}
        	 array.setComponentType(componentType);
            this.traceStack.push(array);

        }
        else if (name.equals("construction"))
        {
            Construction construction = new Construction();
            Object parent = this.traceStack.peek();
            if(parent instanceof ProviderManagerInfo)
            {
	            ProviderManagerInfo providerManagerInfo = (ProviderManagerInfo) parent;
	            providerManagerInfo.setConstruction(construction);
	            
            }
            else if(parent instanceof Pro)
            {
            	Pro p = (Pro)parent;
            	p.addConstructor(construction);
            }
            this.traceStack.push(construction);
            	
            

        }
        
        
        else if (name.equals("s") || name.equals("set"))
        {
        	ProSet set = new ProSet();
        	String componentType = null;        	 
//        	if(this.serial)
        	{
        		componentType = attributes.getValue("cmt");
        		if(componentType == null)
        			componentType = attributes.getValue("componentType");
        	}
//        	else
//        	{
//        		componentType = attributes.getValue("componentType");
//        	}
            set.setComponentType(componentType);
            this.traceStack.push(set);

        }
        
        else if (name.equals("manager"))
        {

            String id = attributes.getValue("id");
            ProviderManagerInfo providerManger = new ProviderManagerInfo();
            providerManger.setId(id);
            String enablerpc = attributes.getValue("enablerpc");
            if(enablerpc != null && enablerpc.equals("true"))
            {
            	providerManger.setEnablerpc(true);
            }
            providerManger.setJndiName(attributes.getValue("jndiname"));

            // providerManger.setTransactionInterceptorClass(attributes
            // .getValue("interceptor"));
            providerManger.setSinglable(getBoolean(attributes.getValue("singlable"), true));
            providerManger.setDefaultable(getBoolean(attributes.getValue("default"), false));
            providerManger
                    .setCallorder_sequence(getBoolean(attributes.getValue("callorder_sequence"), false));
            managers.put(id, providerManger);
            traceStack.push(providerManger);
        }
        else if (name.equals("managerimport"))
        {

            ManagerImport mi = new ManagerImport();
            mi.setFile(attributes.getValue("file"));
            mangerimports.add(mi);
        }
        else if (name.equals("provider"))
        {
            ProviderManagerInfo providerManager = (ProviderManagerInfo) traceStack.peek();

            SecurityProviderInfo provider = new SecurityProviderInfo();
            provider.setConfigFile(this.file);
            boolean isdefault = getBoolean(attributes.getValue("default"), false);
            provider.setIsdefault(isdefault);
            provider.setProviderClass(attributes.getValue("class"));
            provider.setType(attributes.getValue("type"));

            provider.setUsed(getBoolean(attributes.getValue("used"), false));
            String destroyMethod = attributes.getValue("destroy-method");
            provider.setDestroyMethod(destroyMethod);
            String initMethod = attributes.getValue("init-method");
            provider.setInitMethod(initMethod);
            provider.setApplicationContext(applicationContext);
            if (isdefault)
                providerManager.setDefaulProviderInfo(provider);
            providerManager.addSecurityProviderInfo(provider);
            
            traceStack.push(provider);
        }
        else if (name.equals("synchronize"))
        {

            ProviderManagerInfo providerManagerInfo = (ProviderManagerInfo) this.traceStack.peek();
            providerManagerInfo.setSynchronizedEnabled(getBoolean(attributes.getValue("enabled"), false));

        }
        else if (name.equals("transactions"))
        {
        	Object tobj = this.traceStack.peek();
        	if(tobj instanceof ProviderManagerInfo)
        	{
	            ProviderManagerInfo providerManagerInfo = (ProviderManagerInfo) tobj;
	            Transactions txs = new Transactions();
	            providerManagerInfo.setTransactions(txs);
	            traceStack.push(txs);
        	}
        	else
        	{
        		Pro pro = (Pro) tobj;
	            Transactions txs = new Transactions();
	            pro.setTransactions(txs);
	            traceStack.push(txs);
        	}

        }
        else if (name.equals("reference"))
        {
            ProviderManagerInfo providerManagerInfo = (ProviderManagerInfo) this.traceStack.peek();
            Pro ref = new Pro(applicationContext);
            String fieldname = attributes.getValue("fieldname");
            if(fieldname == null)
                fieldname = attributes.getValue("name");;
            
            String refid = attributes.getValue("refid");
            String reftype = attributes.getValue("reftype");
            String value = attributes.getValue("value");
            
            String clazz = attributes.getValue("class");
            if(value == null && refid == null )
            {
                if(clazz != null && !clazz.equals(""))
                {
                    ref.setBean(true);
                }
                else
                {
                    throw new SPIException("reference元素必须带class,value,refid三个属性中的任何一个。");
                }
            }
            ref.setName(fieldname);
            ref.setRefid(refid);
            ref.setReftype(reftype);
            ref.setValue(value);
            ref.setClazz(clazz);
            providerManagerInfo.addReference(ref);

        }

        else if (name.equals("interceptor"))
        {
        	InterceptorInfo interceptor = new InterceptorInfo(this.applicationContext);
        	this.startProperty(interceptor, s1, s2, name, attributes);
        	this.traceStack.push(interceptor);
//        	Object obj = this.traceStack.peek();
//        	
//        	BaseTXManager providerManagerInfo = (BaseTXManager) obj;
//            InterceptorInfo interceptor = new InterceptorInfo();
//            String clazz = attributes.getValue("class");
//            interceptor.setClazz(clazz);
//
//            providerManagerInfo.addInterceptor(interceptor);

        }

        else if (name.equals("method"))
        {

            SynchronizedMethod synchronizedMethod = new SynchronizedMethod();
            synchronizedMethod.setMethodName(attributes.getValue("name"));
            synchronizedMethod.setPattern(attributes.getValue("pattern"));
            synchronizedMethod.setTxtype(attributes.getValue("txtype"));
            traceStack.push(synchronizedMethod);
        }
        else if (name.equals("rollbackexceptions"))
        {
            // do nothing
        }
        else if (name.equals("exception"))
        {
            RollbackException e = new RollbackException();
            String exceptionName = attributes.getValue("class");
            String exceptionType = attributes.getValue("type");
            e.setExceptionName(exceptionName);
            e.setExceptionType(exceptionType);
            SynchronizedMethod method = (SynchronizedMethod) traceStack.peek();
            method.addRollbackException(e);

        }
        
        else if (name.equals("param")) // 添加方法参数信息
        {
            String paramType = attributes.getValue("type");
            if(paramType == null)
            	paramType = attributes.getValue("class");
            String value = attributes.getValue("value");
            String refid = attributes.getValue("refid");
            Param param = new Param(applicationContext);
            param.setClazz(paramType);
            param.setRefid(refid);
            param.setValue(value);
            Object o = traceStack.peek();
            if (o instanceof SynchronizedMethod)
            {
                SynchronizedMethod method = (SynchronizedMethod) o;
                method.addParam(param);
            }
            else if (o instanceof Construction)
            {
                Construction construction = (Construction) o;
                construction.addParam(param);
            }
        }
        

        else if (name.equals("description"))
        {
//            this.currentValue.delete(0, this.currentValue.length());
        	this.currentValue = null;
//            Object p = this.traceStack.peek();
//            if(p instanceof Pro)
//            {
//                Pro pro = (Pro)p;
//                pro.setDescription(description)
//            }
        }
              
        else if(name.equals("editor"))
        {
            Editor editor = new Editor();
            String clazz = attributes.getValue("class");
            if(clazz == null || clazz.equals(""))
                throwable("editor 节点没有指定class属性。");
            editor.setEditor(clazz);
            Pro currentElement = (Pro)this.traceStack.peek();
            currentElement.setEditor(editor);            
        }
        else
        {
//        	if(this.applicationContext.isfile())
//        		log.info("解析文件时[" + this.parent + "]遇到元素[" + name + "]，忽略处理。");
//        	else
//        		log.info("解析报文时遇到元素[" + name + "]，忽略处理。");
        }
    }

    public Map getManagers()
    {
        return managers;
    }

    public List getMangerimports()
    {
        return this.mangerimports;
    }
    
    public void throwable(String message)
    {
        if(parent == null)
            throw new IllegalArgumentException("editor 节点没有指定class属性。对应的配置文件为：" + this.file);
        else
        {
            throw new IllegalArgumentException("editor 节点没有指定class属性。对应的配置文件为：" + this.parent.toString(this.file) );
        }
    }
    
    public static boolean getBoolean(String value, boolean nullReplace) {
		boolean ret = false;
		if (value == null)
			ret = nullReplace;
		else if (value.trim().equalsIgnoreCase("true"))
			ret = true;
		else
			ret = false;
		return ret;

	}

    
    
    
    
   
}
