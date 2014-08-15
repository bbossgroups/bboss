package org.frameworkset.spi.assemble;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.frameworkset.spi.CallContext;


/**
 * 
 * <p>Title: ProSet.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2009-9-19 下午11:01:35
 * @author biaoping.yin
 * @version 1.0
 */
public  class ProSet<V extends Pro> extends TreeSet<V> 
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
    @Override
    public boolean add(V o)
    {
        modify(); 
        return super.add(o);
    }
    @Override
    public boolean addAll(Collection<? extends V> c)
    {
        modify();
        return super.addAll(c);
    }
    @Override
    public void clear()
    {
        modify();
        super.clear();
    }
    @Override
    public boolean remove(Object o)
    {
        modify();
        return super.remove(o);
    }
    @Override
    public boolean removeAll(Collection<?> c)
    {
        modify();
        return super.removeAll(c);
    }
    @Override
    public boolean retainAll(Collection<?> c)
    {
        modify();
        return super.retainAll(c);
    }
    public Iterator<V> iterator() {
        return super.iterator();
    }
    /**
     * 不是线程安全的方法
     * @param i
     * @return
     */
    public V get(int i)
    {
        Iterator<V> it = this.iterator();
        int t = 0;
        int size = this.size();
        while(t < size && it.hasNext())
        {
            if(i == t)
            {
                return it.next();
            }
            else
            {
                t ++;
            }
                
        }
        throw new java.lang.IllegalArgumentException("行号 i=" + i + "越界， 大于或者小于容器中数据的总个数size=" + size);
    }
    public Pro getPro(int i)
    {
        return this.get(i);
    }
    public int getInt(int i)
    {
        
        Pro value = this.get(i);
        if(value == null)
            return 0;

        return value.getInt();
    }
    public int getInt(int i,int defaultValue)
    {
        Pro value = this.get(i);
        if(value == null)
            return defaultValue;
//        int value_ = Integer.parseInt(value.toString());
        return value.getInt();
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
    
    public Object getObject(int i,Object defaultValue)
    {
        Pro value = this.get(i);
        
        if(value == null)
            return defaultValue;
        
        return value.getObject(defaultValue);
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
	
	private Set componentSet;
    private Object lock = new Object();
    private Set _getSet(Class maptype)
    {
    	Set componentMap = null;
    	if(maptype != ArrayList.class)
		{
    		
			try {
				if(maptype.getName().equals("java.util.Collections$SynchronizedSet"))
				{
					componentMap = Collections
							.synchronizedSet(new TreeSet());
				}
				else
				{
					componentMap = (Set)maptype.newInstance();
				}
			} catch (InstantiationException e) {
				throw new BeanInstanceException(e);
			} catch (IllegalAccessException e) {
				throw new BeanInstanceException(e);
			}
		}
		else
		{
			componentMap = new TreeSet();
		}
    	return componentMap;
    }
    public Set getComponentSet(Class settype,CallContext callcontext)
    {
    	if(this.getComponentType() == null)
    		return this;
    	if(componentSet == null)
    	{
    		synchronized(lock)
    		{
    			if(componentSet == null)
    			{
    				if(this.size() > 0)
    				{
    					if(this.componentType.equalsIgnoreCase(Pro.COMPONENT_BEAN) || this.componentType.equalsIgnoreCase(Pro.COMPONENT_OBJECT_SHORTNAME) || this.componentType.equalsIgnoreCase(Pro.COMPONENT_OBJECT))
    					{
    						componentSet =  _getSet(settype);
//    						if(settype != TreeSet.class)
//    						{
//        						try {
//        							if(!settype.getName().equals("java.util.Collections$SynchronizedSet"))
//        							{
//        								componentSet = (Set)settype.newInstance();
//        							}
//        							else
//        							{
//        								ClassInfo beaninfo = ClassUtil.getClassInfo(settype);
//        								Constructor c = beaninfo.getConstructor(Set.class);
//        								componentSet = (Set)c.newInstance(new TreeSet());
//        							}
//    							} catch (InstantiationException e) {
//    								throw new BeanInstanceException(e);
//    							} catch (IllegalAccessException e) {
//    								throw new BeanInstanceException(e);
//    							} catch (IllegalArgumentException e) {
//    								throw new BeanInstanceException(e);
//								} catch (InvocationTargetException e) {
//									throw new BeanInstanceException(e);
//								}
//    						}
//    						else
//    						{
//    							componentSet = new TreeSet();
//    						}
    						Iterator keys = this.iterator();
    						Context currentLoopContext = callcontext != null?callcontext.getLoopContext():null;
	    					while(keys.hasNext())
	    					{
	    						Pro pro = (Pro)keys.next();
	    						try{
	    							componentSet.add(pro.getBean(callcontext,true));	
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
//    						if(settype != TreeSet.class)
//    						{
//        						try {
//        							componentSet = (Set)settype.newInstance();
//    							} catch (InstantiationException e) {
//    								throw new BeanInstanceException(e);
//    							} catch (IllegalAccessException e) {
//    								throw new BeanInstanceException(e);
//    							}
//    						}
//    						else
//    						{
//    							componentSet = new TreeSet();
//    						}
    						componentSet =  _getSet(settype);
    						Iterator keys = this.iterator();
	    					while(keys.hasNext())
	    					{
	    						Pro pro = (Pro)keys.next();
	    						componentSet.add(pro.getString());	
	    					}
    					}
    					else
    					{
//    						if(settype != TreeSet.class)
//    						{
//        						try {
//        							componentSet = (Set)settype.newInstance();
//    							} catch (InstantiationException e) {
//    								throw new BeanInstanceException(e);
//    							} catch (IllegalAccessException e) {
//    								throw new BeanInstanceException(e);
//    							}
//    						}
//    						else
//    						{
//    							componentSet = new TreeSet();
//    						}
    						componentSet =  _getSet(settype);
    						Iterator keys = this.iterator();
    						Context currentLoopContext = callcontext != null?callcontext.getLoopContext():null;
	    					while(keys.hasNext())
	    					{
	    						Pro pro = (Pro)keys.next();
	    						try
	    						{
	    							componentSet.add(pro.getBean(callcontext,true));	
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
//    					if(settype != TreeSet.class)
//						{
//    						try {
//    							componentSet = (Set)settype.newInstance();
//							} catch (InstantiationException e) {
//								throw new BeanInstanceException(e);
//							} catch (IllegalAccessException e) {
//								throw new BeanInstanceException(e);
//							}
//						}
//						else
//						{
//							componentSet = new TreeSet();
//						}
    					componentSet =  _getSet(settype);
    				}    				
    			}
    		}
    	}
    	return componentSet;
    }
    
   
}