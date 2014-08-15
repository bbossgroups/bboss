package org.frameworkset.spi.assemble;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.frameworkset.spi.CallContext;

/**
 * 
 * <p>Title: ProMap.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2009-9-19 下午11:00:43
 * @author biaoping.yin
 * @version 1.0
 */
public class ProMap<K,V extends Pro> extends HashMap<K,V>
{        
	 /**
     * 容器组件类型，有以下三种类型：
     * bean:将可以直接将组装的ProList转换为List<po对象>集合
     * String：ProList转换为List<String>集合
     * Pro：默认类型ProList<V extends Pro>，不做转换，除非指定editor编辑器
     */
    private String componentType ;
    private boolean isfreeze = false;
    
    public void freeze()
    {
        this.isfreeze = true;
    }
    private boolean isFreeze()
    {
        
        return this.isfreeze;
    }
    
    private void modify() 
    {
        if(this.isFreeze())
            throw new CannotModifyException();
    }
    public int getInt(String key)
    {            
        Pro value = this.get(key);
        if(value == null)
            return 0;
//        int value_ = Integer.parseInt(value.toString());
        return value.getInt();
    }
    
    public long getLong(String key)
    {            
        Pro value = this.get(key);
        if(value == null)
            return 0;
//        int value_ = Integer.parseInt(value.toString());
        return value.getLong();
    }
    @Override
    public void clear()
    {
        modify();
        super.clear();
    }
    @Override
    public V put(K key, V value)
    {
        modify();
        return super.put(key, value);
    }
    @Override
    public void putAll(Map<? extends K, ? extends V> m)
    {
        modify();
        super.putAll(m);
    }
    @Override
    public V remove(Object key)
    {
        modify();
        return super.remove(key);
    }
    public int getInt(String key,int defaultValue)
    {
        Pro value = this.get(key);
        if(value == null)
            return defaultValue;
//        int value_ = Integer.parseInt(value.toString());
        return value.getInt();
    }
    
    
    public long getLong(String key,long defaultValue)
    {
        Pro value = this.get(key);
        if(value == null)
            return defaultValue;
//        int value_ = Integer.parseInt(value.toString());
        return value.getLong();
    }
    
    
    
    public boolean getBoolean(String key)
    {
        Pro value = this.get(key);
        if(value == null)
            return false;
//        boolean value_ = Boolean.parseBoolean(value.toString());
        return value.getBoolean();
    }
    public boolean getBoolean(String key,boolean defaultValue)
    {
        Pro value = this.get(key);
        if(value == null)
            return defaultValue;
        boolean value_ = value.getBoolean(defaultValue);
        return value_;
    }
    
    public String getString(String key)
    {
        Pro value = this.get(key);
        if(value == null)
            return null;
        
        return value.getString();
    }
    public String getString(String key,String defaultValue)
    {
        Pro value = this.get(key);
        
        if(value == null)
            return defaultValue;
        
        return value.getString(defaultValue);
    }
    public ProList getList(String key,ProList defaultValue)
    {   
        Pro value = this.get(key);
        
        if(value == null)
            return defaultValue;
        
        return value.getList(defaultValue);
    }
    public ProList getList(String key)
    {   
        Pro value = this.get(key);
        if(value == null)
            return null;
        
        return value.getList();
    }
    
    public ProMap getMap(String key,ProMap defaultValue)
    {   
        Pro value = this.get(key);
        
        if(value == null)
            return defaultValue;
        
        return value.getMap(defaultValue);
    }
    public ProMap getMap(String key)
    {   
        Pro value = this.get(key);
        if(value == null)
            return null;
        
        return value.getMap();
    }
    
    public ProSet getSet(String key,ProSet defaultValue)
    {   
        Pro value = this.get(key);
        
        if(value == null)
            return defaultValue;
        
        return value.getSet(defaultValue);
    }
    public ProSet getSet(String key)
    {   
        Pro value = this.get(key);
        if(value == null)
            return null;
        
        return value.getSet();
    }
    
    /**
     * 返回带ioc功能的对象
     * @param key
     * @param callcontext
     * @return
     */
    public Object getProxyObject(String key,CallContext callcontext)
    {
        Pro<V> value = this.get(key);
        if(value == null)
            return null;
        
        return value.getProxyTrueValue(callcontext);
    }
    public Object getObject(String key)
    {
        return getObject(key,null);
    }
    public Pro getPro(String name)
    {
        return this.get(name);
    }
    
    public Object getObject(String key,Object defaultValue)
    {
        Pro value = this.get(key);
        
        if(value == null)
            return defaultValue;
        
        return value.getObject(defaultValue);
    }
    
    public Object getBean(String key)
    {
        Pro value = this.get(key);
        
        
        
        return value.getBean();
    }
	/**
	 * @return the componentType
	 */
	public String getComponentType() {
		return componentType;
	}
	/**
	 * @param componentType the componentType to set
	 */
	public void setComponentType(String componentType) {
		this.componentType = componentType;
	}
	
	
	private Map componentMap;
    private Object lock = new Object();
    
    private Map _getMap(Class maptype)
    {
    	Map componentMap = null;
    	if(maptype != HashMap.class)
		{
    		
			try {
				if(maptype.getName().equals("java.util.Collections$SynchronizedMap"))
				{
					componentMap = Collections
							.synchronizedMap(new HashMap());
				}
				else
				{
					componentMap = (Map)maptype.newInstance();
				}
			} catch (InstantiationException e) {
				throw new BeanInstanceException(e);
			} catch (IllegalAccessException e) {
				throw new BeanInstanceException(e);
			}
		}
		else
		{
			componentMap = new HashMap(this.size());
		}
    	return componentMap;
    }
    public Map getComponentMap(Class maptype,CallContext callcontext)
    {
    	if(this.getComponentType() == null)
    		return this;
    	if(componentMap == null)
    	{
    		synchronized(lock)
    		{
    			if(componentMap == null)
    			{
    				if(this.size() > 0)
    				{
    					if(this.componentType.equalsIgnoreCase(Pro.COMPONENT_BEAN) 
    							|| this.componentType.equalsIgnoreCase(Pro.COMPONENT_OBJECT) || this.componentType.equalsIgnoreCase(Pro.COMPONENT_OBJECT_SHORTNAME))
    					{
    						
    						componentMap = _getMap(maptype);
    						Iterator<String> keys = (Iterator<String>)this.keySet().iterator();
    						Context currentLoopContext = callcontext != null?callcontext.getLoopContext():null;	    					
	    					while(keys.hasNext())
	    					{
	    						String key = keys.next();
	    						try
	    						{
	    							componentMap.put(key, getProxyObject(key,callcontext));	
	    						}
	    						finally
	    						{
	    							if(callcontext != null)
	    								callcontext.setLoopContext(currentLoopContext);
	    						}
	    					}
    						
    					}
    					else if(this.componentType.equalsIgnoreCase(Pro.COMPONENT_STRING_SHORTNAME) || this.componentType.equalsIgnoreCase(Pro.COMPONENT_STRING))
    					{
//    						componentMap = new HashMap(this.size());
    						componentMap = _getMap(maptype);
    						Iterator<String> keys = (Iterator<String>)this.keySet().iterator();
	    					while(keys.hasNext())
	    					{
	    						String key = keys.next();
	    						componentMap.put(key,this.getString(key));	
	    					}
    					}
    					else
    					{
//    						componentMap = new HashMap(this.size());
    						componentMap = _getMap(maptype);
    						Iterator<String> keys = (Iterator<String>)this.keySet().iterator();
//	    					while(keys.hasNext())
//	    					{
//	    						String key = keys.next();
//	    						componentMap.put(key, getObject(key));	
//	    					}
    						Context currentLoopContext = callcontext != null?callcontext.getLoopContext():null;	    					
	    					while(keys.hasNext())
	    					{
	    						String key = keys.next();
	    						try
	    						{
	    							componentMap.put(key, getProxyObject(key,callcontext));	
	    						}
	    						finally
	    						{
	    							if(callcontext != null)
	    								callcontext.setLoopContext(currentLoopContext);
	    						}
	    					}
    					}
    				}
    				else
    				{
//    					componentMap = new HashMap(this.size());
    					componentMap = _getMap(maptype);
    				}    				
    			}
    		}
    		
    	}
    	
    	return componentMap;
    }
    
    
    
    
    
}
