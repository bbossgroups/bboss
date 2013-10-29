/*
 * Created on 2004-6-3
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.frameworkset.common.bean;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * @author biaoping.yin
 * 组合值对象，可以自动将不同类型的对象（对象对应的类必须从ValueObject继承，
 * ValueObject为系统中所有值对相的基类）分发到特定的map中。
 * 
 * 适用于对多个值对象进行增、删、改等操作的情况后传递和缓冲这些对象
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CompositeVO extends AbstractCompositeVO 
{
	/****************************************************
	 *      组合对象可以缓存对业务数据进行增、删、改    *
	 * 		三种操作后的结果,以下定义四种操作对应的标志 *
	 ****************************************************/
	/**
	 * 删除状态标志
	 */
	protected final static int DELETE = 0;
	/**
	 * 更新状态标识
	 */
	protected final static int UPDATE = 1;
	/**
	 * 添加状态标识
	 */
	protected final static int ADD = 2;
	/**
	 * 缓冲状态标识
	 */
	protected final static int CACHE = 3;
	/**
	 * 缓冲一般的对象
	 * @param obj
	 */
	public void cacheVO(ValueObject obj)
	{
		setVO(obj, CACHE);
	}
	
	public void cacheVOS(List vos)
	{
	    for(int i = 0; vos != null && i < vos.size(); i ++)
	        setVO((ValueObject)vos.get(i), CACHE);
	}
	/**
	 * 获取缓冲数据
	 * @param clazz
	 * @return Iterator
	 */
	public Iterator getCacheVO(Class clazz)
	{
		return this.getVO(clazz, new int[] { CACHE });
	}
	/**
	 * 将更新业务信息时，将封装信息的值对象存放到该对象对应的散列中
	 * @param obj
	 */
	public void updateVO(ValueObject obj)
	{
		if (!synchroUpdate(obj))
			setVO(obj, UPDATE);
	}
	/**
	 * 从组合对象中获取单个值对象,
	 * 如果开发人员操作的是一个值对象而不是值对象的集合，
	 * 最后从组合对象中取对象时调用该方法
	 * @param clazz 要获取值对象对应的Class对象，例如：xxVO.class
	 * @return 如果改类型的对象存在则返回该对象否则返回null
	 */
	public ValueObject getSingleVO(Class clazz)
	{
		Iterator iterator = getAllVO(clazz);
		if (iterator.hasNext())
			return (ValueObject) iterator.next();
		return null;
	}	
	
	
	/**
	 * 获取散列中所有clazz的对象
	 * @param clazz
	 * @return Iterator
	 */
	public Iterator getAllVO(Class clazz)
	{
		return getVO(clazz, new int[] { ADD, UPDATE, DELETE, CACHE });
	}
	/**
	 * 新建业务信息时，将封装信息的值对象存放到该对象对应的散列中
	 * 注意:调用本方法时将要修改obj的主键
	 * @param obj
	 */
	public void addVO(ValueObject obj)
	{
		addVO(obj,true);
	}
	
	/**
	 * 添加obj到值对象中,根据keyGenerator判定是否产生主键true产生,false不产生
	 * @param obj
	 * @param keyGenerator
	 */
	public void addVO(ValueObject obj,boolean keyGenerator)
	{
	    addVO(obj,keyGenerator,false);
//		Map voBin = getMap(obj.getClass());
//		//如果需要产生主键,则产生并设置该主键到值对象中,否则采用值对象中的主键
//		if(keyGenerator)
//		{
//			Object id = getId(voBin.keySet().iterator());
//			obj.setKey(id);
//		}		
//		setVO(obj, ADD);
	}
	
	/**
	 * 添加obj到值对象中,根据keyGenerator判定是否产生主键true产生,false不产生
	 * @param obj
	 * @param keyGenerator
	 */
	public void addVO(ValueObject obj,boolean keyGenerator,boolean needSynchro)
	{
		Map voBin = getMap(obj.getClass());
		//如果需要产生主键,则产生并设置该主键到值对象中,否则采用值对象中的主键
		if(keyGenerator)
		{
			Object id = getId(voBin.keySet().iterator());
			obj.setKey(id);
			setVO(obj, ADD);
		}		
		else if(needSynchro)
		{
		    if(!synchroAdd(obj))
		        setVO(obj, ADD);
		}
		else
		    setVO(obj, ADD);
		
	}
	
	protected boolean synchroAdd(ValueObject obj)
	{
	    return this.synchroAdd(obj,StatusConst.DELETE);
	}
	
	/**
	 * 删除业务信息时，将封装信息的值对象存放到该对象的散列中
	 * @param obj
	 */
	public void deleteVO(ValueObject obj)
	{
		if (!synchroDelete(obj))
			setVO(obj, DELETE);
	}
	/**
	 * 获取缓存中新建的所有clazz对应的值对象
	 * @param clazz 要获取值对象对应的Class对象，例如：xxVO.class
	 * @return 包含值对象的迭代器
	 */
	public Iterator getNewVO(Class clazz)
	{
		return getVO(clazz, new int[] { ADD });
	}
	/**
	 * 获取缓存中更新的所有clazz对应的值对象
	 * @param clazz :要获取值对象对应的Class对象，例如：xxVO.class
	 * @return 包含值对象的迭代器
	 */
	public Iterator getUpdateVO(Class clazz)
	{
		return getVO(clazz, new int[] { UPDATE });
	}
	
	/**
	 * 获取clazz的新增，修改两种状态的对象
	 * @param clazz
	 * @return Iterator
	 */
	public Iterator getNew2ndUpdateVO(Class clazz)
	{
		return getVO(clazz, new int[] { UPDATE, ADD });
	}
	
	/**
	 * 获取clazz的新增，修改，缓冲三种状态的对象
	 * @param clazz
	 * @return Iterator
	 */
	public Iterator getNew2ndUpdate2ndCacheVO(Class clazz)
	{
		return getVO(clazz, new int[] { UPDATE, ADD, CACHE });
	}
	     
	/**
	 * 获取clazz的新增，修改，删除三种状态的对象
	 * @param clazz
	 * @return Iterator
	 */
	public Iterator getNew2ndUpdate2ndDeleteVO(Class clazz)
	{
		return getVO(clazz, new int[] { UPDATE, ADD, DELETE });
	}
	/**
	 * 获取缓存中删除的所有clazz对应的值对象
	 * @param clazz :要获取值对象对应的Class对象，例如：xxVO.class
	 * @return 包含值对象的迭代器
	 */
	public Iterator getDeleteVO(Class clazz)
	{
		return getVO(clazz, new int[] { DELETE });
	}	
	
	/**
	 * 消除delete和add的冲突
	 * @param obj
	 */
	protected boolean synchroDelete(ValueObject obj)
	{
		return synchroDelete(obj,ADD);
	}
	/**
	 * 消除update和add的冲突
	 * @param obj
	 */
	protected boolean synchroUpdate(ValueObject obj)
	{
		return synchroUpdate(obj,ADD);
	}	
	
	/**
	 * 移除所有容器中缓存的类型为clazz的状态为status的值对象
	 * @param clazz
	 */
	public void deleteAll(Class clazz)
	{
	    Map bin = getMap(clazz,false);
	    
	    if(bin == null)
	        return;
	    
	    Iterator keys = bin.values().iterator();
//	    for(int i = 0; i < keys.size(); i ++)
//	    {
//	        keys.removeAll(keys);
//	        //DecoratorVO dec = (DecoratorVO)keys.get(i);	        
//	    }
	    List removeVO = new ArrayList();
	    while(keys.hasNext())
	    {
	        Object dec = keys.next();	        
	        //System.out.println("first:"+((DecoratorVO)dec).status);
	        if(((DecoratorVO)dec).status == StatusConst.ADD)
	            removeVO.add(((DecoratorVO)dec).vo);
	        else
	            ((DecoratorVO)dec).status = StatusConst.DELETE;
	        //System.out.println(dec);
	        //deleteVO((ValueObject)((DecoratorVO)dec).vo);	        
	        
	    }
	    
	    for(int i = 0; i < removeVO.size(); i ++)
	    {
	        ValueObject vo = (ValueObject)removeVO.get(i);
	        bin.remove(vo.getKey());
	    }
	    
	    
	}
}
