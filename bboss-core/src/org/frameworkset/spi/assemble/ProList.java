package org.frameworkset.spi.assemble;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.frameworkset.spi.CallContext;

/**
 * 
 * <p>Title: ProList.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2009-9-19 下午11:01:26
 * @author biaoping.yin
 * @version 1.0
 */
public  class ProList<V extends Pro> extends ArrayList<V>
{        
    private boolean isfreeze = false;
    /**
     * 容器组件类型，有以下三种类型：
     * bean:将可以直接将组装的ProList转换为List<po对象>集合
     * String：ProList转换为List<String>集合
     * Pro：默认类型ProList<V extends Pro>，不做转换，除非指定editor编辑器
     */
    private String componentType ;
    
    
    
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
    public int getInt(int i)
    {
        Pro value = this.get(i);
        if(value == null)
            return 0;
//        int value_ = Integer.parseInt(value.toString());
        return value.getInt();
    }
    @Override
    public void add(int index, V element)
    {
        modify() ;
        super.add(index, element);
    }
    @Override
    public boolean add(V o)
    {
        modify() ;
        return super.add(o);
    }
    @Override
    public boolean addAll(Collection<? extends V> c)
    {
        modify() ;
        return super.addAll(c);
    }
    @Override
    public boolean addAll(int index, Collection<? extends V> c)
    {
        modify() ;
        return super.addAll(index, c);
    }
    @Override
    public void clear()
    {
        modify() ;
        super.clear();
    }
    @Override
    public V remove(int index)
    {
        modify() ;
        return super.remove(index);
    }
    @Override
    public boolean remove(Object o)
    {
        modify() ;
        return super.remove(o);
    }
    @Override
    protected void removeRange(int fromIndex, int toIndex)
    {
        modify() ;
        super.removeRange(fromIndex, toIndex);
    }
    @Override
    public V set(int index, V element)
    {
        modify() ;
        return super.set(index, element);
    }
    @Override
    public boolean removeAll(Collection<?> c)
    {
        modify() ;
        return super.removeAll(c);
    }
    @Override
    public boolean retainAll(Collection<?> c)
    {
        modify() ;
        return super.retainAll(c);
    }
    public int getInt(int i,int defaultValue)
    {
        Pro value = this.get(i);
        if(value == null)
            return defaultValue;
//        int value_ = Integer.parseInt(value.toString());
        return value.getInt();
    }
    public Pro<?> getPro(int i)
    {
        return this.get(i);
    }
    
    
    
    
    public boolean getBoolean(int i)
    {
        Pro value = this.get(i);
        if(value == null)
            return false;
//        boolean value_ = Boolean.parseBoolean(value.toString());
        return value.getBoolean();
    }
    public boolean getBoolean(int i,boolean defaultValue)
    {
        Pro value = this.get(i);
        if(value == null)
            return defaultValue;
        boolean value_ = value.getBoolean(defaultValue);
        return value_;
    }
    
    public String getString(int i)
    {
        Pro value = this.get(i);
        if(value == null)
            return null;
        
        return value.getString();
    }
    public String getString(int i,String defaultValue)
    {
        Pro value = this.get(i);
        
        if(value == null)
            return defaultValue;
        
        return value.getString(defaultValue);
    }
    public ProList getList(int i,ProList defaultValue)
    {   
        Pro value = this.get(i);
        
        if(value == null)
            return defaultValue;
        
        return value.getList(defaultValue);
    }
    public ProList getList(int i)
    {   
        Pro value = this.get(i);
        if(value == null)
            return null;
        
        return value.getList();
    }
    
    public ProSet getSet(int i,ProSet defaultValue)
    {   
        Pro value = this.get(i);
        
        if(value == null)
            return defaultValue;
        
        return value.getSet(defaultValue);
    }
    public ProSet getSet(int i)
    {   
        Pro value = this.get(i);
        if(value == null)
            return null;
        
        return value.getSet();
    }
    
    public Object getObject(int i)
    {
        Pro value = this.get(i);
        if(value == null)
            return null;
        
        return value.getObject();
    }
    private List componentList;
    private Object lock = new Object();
    private List _getList(Class maptype)
    {
    	List componentMap = null;
    	if(maptype != ArrayList.class)
		{
    		
			try {
				if(maptype.getName().equals("java.util.Collections$SynchronizedRandomAccessList"))
				{
					componentMap = Collections
							.synchronizedList(new ArrayList());
				}
				else if(maptype.getName().equals("java.util.Collections$SynchronizedList"))
				{
					componentMap = Collections
							.synchronizedList(new ArrayList());
				}
				else
				{
					componentMap = (List)maptype.newInstance();
				}
			} catch (InstantiationException e) {
				throw new BeanInstanceException(e);
			} catch (IllegalAccessException e) {
				throw new BeanInstanceException(e);
			}
		}
		else
		{
			componentMap = new ArrayList(this.size());
		}
    	return componentMap;
    }
    public List getComponentList(Class listtype,CallContext callcontext)
    {
    	if(this.getComponentType() == null)
    		return this;
    	if(componentList == null)
    	{
    		synchronized(lock)
    		{
    			if(componentList == null)
    			{
//    				if(this.size() > 0)
    				{
    					if(this.componentType.equals(Pro.COMPONENT_BEAN))
    					{
    						componentList = _getList( listtype);
        					
        					Context currentLoopContext = callcontext != null?callcontext.getLoopContext():null;
	    					for(Pro pro:this)
	    					{
	    						
	    						try{
	    							componentList.add(pro.getProxyBean(callcontext));
	    						}
	    						finally
	    						{
	    							if(callcontext != null)
	    								callcontext.setLoopContext(currentLoopContext);
	    						}
	    						
	    					}
    					}
    					else if(this.componentType
								.equalsIgnoreCase(Pro.COMPONENT_STRING_SHORTNAME) || this.componentType
								.equalsIgnoreCase(Pro.COMPONENT_STRING))
    					{
//    						componentList = new ArrayList(this.size());
    						componentList = _getList( listtype);
    						for(Pro pro:this)
	    					{
    							componentList.add(pro.getString());	
	    					}
    					}
    					else if(this.componentType.equalsIgnoreCase(Pro.COMPONENT_OBJECT_SHORTNAME) || this.componentType.equalsIgnoreCase(Pro.COMPONENT_OBJECT))
    					{
//    						componentList = new ArrayList(this.size());
    						componentList = _getList( listtype);
    						Context currentLoopContext = callcontext != null?callcontext.getLoopContext():null;
	    					for(Pro pro:this)
	    					{
	    						try{
	    							componentList.add(pro.getProxyBean(callcontext));	
	    						}
	    						finally
	    						{
	    							if(callcontext != null)
	    								callcontext.setLoopContext(currentLoopContext);
	    						}
	    					}
    					}
    					else
    					{
//    						componentList = new ArrayList(this.size());
    						componentList = _getList( listtype);
//	    					for(Pro pro:this)
//	    					{
//	    						componentList.add(pro.getBean());	
//	    					}
    						Context currentLoopContext = callcontext != null?callcontext.getLoopContext():null;
	    					for(Pro pro:this)
	    					{
	    						try{
	    							componentList.add(pro.getProxyBean(callcontext));	
	    						}
	    						finally
	    						{
	    							if(callcontext != null)
	    								callcontext.setLoopContext(currentLoopContext);
	    						}
	    					}
    					}
    				}
//    				else
//    				{
//    					componentList = new ArrayList(this.size());;
//    				}    				
    			}
    		}
    		
    	}
    	
    	return componentList;
    }
    
    public Object getObject(int i,Object defaultValue)
    {
        Pro value = this.get(i);
        
        if(value == null)
            return defaultValue;
        
        return value.getObject(defaultValue);
    }
    public Iterator<V> iterator() {
        return super.iterator();
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
    
}