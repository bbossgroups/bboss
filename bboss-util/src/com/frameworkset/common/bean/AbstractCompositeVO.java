/*
 * Created on 2004-5-29
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.frameworkset.common.bean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.frameworkset.util.CompareUtil;




/**
 * @author biaoping.yin
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public abstract class AbstractCompositeVO implements Serializable
{
	private static Logger log = Logger.getLogger(AbstractCompositeVO.class);
	/**
	 * binMap用来容纳一个HashMap对象序列，
	 * 每个HashMap对象的key为hashmap中所容纳对象的class，
	 * 序列中的Map对象只包含一种特定类型的值对象散列。
	 */
	private Map binMap = new HashMap();

	/**
	 * 获取存放clazz的对象实例的Map对象
	 * @param clazz 要获取值对象对应的Class对象，例如：xxVO.class
	 * @return Map
	 */

	protected Map getMap(Class clazz)
	{
		return getMap(clazz,true);
	}

	/**
	 * 获取对应class的binMap
	 * @param clazz
	 * @param createBin 控制获取的bin为null时，是否为对应的class创建一个新的bin
	 * @return Map
	 */
	protected Map getMap(Class clazz,boolean createBin)
	{
		Map voBin = (Map)binMap.get(clazz);
		if(voBin == null)
		{
			if(createBin)
			{
				voBin = new HashMap();
				binMap.put(clazz,voBin);
			}
			else
				return null;
		}
		return voBin;
	}
	/**
	 * 为特定类型的值对象产生一个未使用的key值
	 * Description:
	 * @param iterator
	 * @return
	 * long
	 */
	protected Object getId(Iterator iterator)
	{
		long maxId = 0;
		while(iterator.hasNext())
		{
			Object obj = iterator.next();
			long temp = ((Long)obj).longValue();
						maxId = Math.max(
								temp,
								maxId);

		}
		maxId ++;
		return new Long(maxId);
	}

	/**
	 * 获取值对象的key值
	 * Description:
	 * @param dcVO
	 * @return Object
	 * @throws IllegalArgumentException
	 *
	 */
	private Object getKey(DecoratorVO dcVO)
		throws IllegalArgumentException
	{
		if(dcVO == null)
			throw new IllegalArgumentException("DecoratorVO dcVO 为空!");
		if(dcVO.vo == null)
			throw new IllegalArgumentException("值对象为空!");
		return ((ValueObject)dcVO.vo).getKey();
	}

	/**
	 * 对特定类型的值对象进行排序
	 * Description:
	 * @param list
	 * void
	 */
	private void sort(List list)
	{
	    Collections.sort(list,new Comparator()
		  {
				public int compare(Object o1, Object o2)
				{
					DecoratorVO dcO1 = (DecoratorVO)o1;
					DecoratorVO dcO2 = (DecoratorVO)o2;
					try
					{
					    CompareUtil cUtil = new CompareUtil();
						return cUtil.compareValue(getKey(dcO1),getKey(dcO2));
					}
					catch(Exception e)
					{
						//e.printStackTrace();
						log.info(e.getMessage());
						return 0;
					}

				}
			  });
	}
	/**
	 * 获取类型为clazz，符合filter中的条件的值对象的迭代器
	 * 会根据每个值对象的key进行排序
	 * @param clazz
	 * @param operations
	 * @return Iterator
	 */
	protected Iterator getVO(Class clazz,int[] operations)
	{
		List list = new ArrayList(getMap(clazz).values());
		sort(list);
		return new UtilIterator(list.iterator(),operations);
	}

	/**
	 * 将值对象obj和操作信息封装成DecoratorVO对象存入到obj对应的散列中
	 * @param obj
	 * @param operation
	 */
	protected void setVO(ValueObject obj,int operation)
	{
	    //System.out.println("setvo obj.getKey():" + obj.getKey());
		getMap(obj.getClass()).put(obj.getKey(),new DecoratorVO(obj, operation));
	}

	/**
	 * 获取类型信息为clazz,关键字为key的对象
	 * @param clazz
	 * @param key
	 * @return ValueObject
	 */
	public ValueObject getSingleVO(Class clazz,long key)
	{
		Map map = this.getMap(clazz);
		if(map == null)
			return null;
		DecoratorVO dec = (DecoratorVO)map.get(new Long(key));
		if(dec != null)
			return (ValueObject)dec.vo;
		return null;
	}


	/**
	 * 消除delete和status的冲突
	 * @param obj
	 */
	protected boolean synchroDelete(ValueObject obj,int add)
	{
		if (obj == null)
			return false;
		Map newBin = getMap(obj.getClass());
		Object key = obj.getKey();
		if (newBin.containsKey(key))
		{
			DecoratorVO dec = (DecoratorVO) newBin.get(key);
			if (dec.status == add)
			{
				newBin.remove(key);
				return true;
			}
		}
		return false;
	}

	/**
	 * 消除update和status的冲突
	 * @param obj
	 */
	protected boolean synchroAdd(ValueObject obj,int delete)
	{
		if (obj == null)
			return false;
		Map newBin = getMap(obj.getClass());
		Object key = obj.getKey();
		if (newBin.containsKey(key))
		{
			DecoratorVO dec = (DecoratorVO) newBin.get(key);
			if (dec.status == delete)
			{
				dec.status = StatusConst.CACHE;
				return true;
			}
		}
		return false;
	}

	/**
	 * 消除update和status的冲突
	 * @param obj
	 */
	protected boolean synchroUpdate(ValueObject obj,int add)
	{
		if (obj == null)
			return false;
		Map newBin = getMap(obj.getClass());
		Object key = obj.getKey();
		if (newBin.containsKey(key))
		{
			DecoratorVO dec = (DecoratorVO) newBin.get(key);
			if (dec.status == add)
			{
				dec.vo = obj;
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取所有符合operations条件的clazz对象
	 * @param clazz :要获取值对象对应的Class对象，例如：xxVO.class
	 * @return 包含值对象的迭代器
	 */
	public Iterator getVOByStatus(Class clazz,int[] status)
	{
		return getVO(clazz, status);
	}



	/**
	 * 判断给定的值对象是否存在
	 * @param obj
	 */
	public boolean contain(ValueObject obj)
	{
		if (obj == null)
			return false;
		Map newBin = getMap(obj.getClass(),false);
		if(newBin == null)
			return false;
		Object key = obj.getKey();
		return newBin.containsKey(key);
		//return false;
	}

	/**
	 * 判断给定的值对象并且状态为status是否存在
	 * Description:
	 * @param obj
	 * @param status
	 * @return
	 * boolean
	 */
	public boolean contain(ValueObject obj,int status)
	{
		if (obj == null)
			return false;
		Map newBin = getMap(obj.getClass(),false);
		if(newBin == null)
			return false;
		Object key = obj.getKey();
		DecoratorVO dec = (DecoratorVO) newBin.get(key);

		if (dec != null && dec.status == status)
		{
			return true;
		}
		return false;
	}

	/**
	 * Description:判定是否存在类型为clazz并且包含属性field,
	 * 属性值为fieldValue的值对象，如果存在返回true，否则返回false
	 * @param clazz
	 * @param field
	 * @param fieldValue
	 * @return
	 * boolean
	 */

	public boolean contain(Class clazz,String field,Object fieldValue)
	{
		return contain(clazz,field,fieldValue,null);
	}

	/**
	 * Description:判定是否存在类型为clazz并且包含属性field,
	 * 属性值为fieldValue以及状态在数组status中的值对象，如果存在返回true，否则返回false
	 * @param clazz
	 * @param field
	 * @param fieldValue
	 * @param status
	 * @return
	 * boolean
	 */
	public boolean contain(Class clazz,String field,Object fieldValue,int[] status)
	{
		Map newBin = getMap(clazz,false);
		if(newBin == null)
			return false;
		Iterator iterator = new MapIterator(newBin,field,fieldValue,status);
		if(iterator.hasNext())
		{
			return true;
		}
		return false;
	}
	/**
	 * Description:判定是否存在类型为clazz并且包含属性field,
	 * 属性值为fieldValue以及状态为status的值对象，如果存在返回true，否则返回false
	 * @param clazz
	 * @param field
	 * @param fieldValue
	 * @param status
	 * @return
	 * boolean
	 */
	public boolean contain(Class clazz,String field,Object fieldValue,int status)
	{
		return contain(clazz,field,fieldValue,new int[] {status});
	}

	/**
	 * Description:获取类型为clazz，包含属性field，属性值为fieldValue的值对象迭代器
	 * @param clazz 值对象的类型
	 * @param field 作为条件的字段
	 * @param fieldValue 作为条件的字段的值
	 * @return Iterator 符合条件的clazz对象的迭代器
	 */
	public Iterator getVoBy(Class clazz,String field,Object fieldValue)
	{
		return getVoBy(clazz,field,fieldValue,null);
	}

	/**
	 * Description:获取类型为clazz，包含属性field，
	 * 属性值为fieldValue并且状态为数组status中之一的值对象迭代器
	 * @param clazz 值对象的类型
	 * @param field 作为条件的字段
	 * @param fieldValue 作为条件的字段的值
	 * @param status 值对象状态数组
	 * @return Iterator 符合条件的clazz对象的迭代器
	 */
	public Iterator getVoBy(Class clazz,String field,Object fieldValue,int[] status)
	{
		Map newBin = getMap(clazz,false);
		if(newBin == null)
			return null;
		return new MapIterator(newBin,field,fieldValue,status);

	}

	/**
	 * Description:获取类型为clazz，包含属性field，
	 * 属性值为fieldValue并且状态为status的值对象迭代器
	 * @param clazz 值对象的类型
	 * @param field 作为条件的字段
	 * @param fieldValue 作为条件的字段的值
	 * @param status 值对象状态
	 * @return Iterator 符合条件的clazz对象的迭代器
	 */
	public Iterator getVoBy(Class clazz,String field,Object fieldValue,int status)
	{
		return getVoBy(clazz,field,fieldValue,new int[] {status});
	}



	/**
	 * 获取类型信息为clazz,关键字为key的对象
	 * @param clazz
	 * @param key
	 * @return ValueObject
	 */
	public ValueObject getSingleVO(Class clazz,Object key,int status)
	{
		Map map = this.getMap(clazz);
		if(map == null)
			return null;
		DecoratorVO dec = (DecoratorVO)map.get(key);
		if(dec != null && dec.status == status)
			return (ValueObject)dec.vo;
		return null;
	}

	private int matchStatus(DecoratorVO decorator, int[] filter)
	{
		int status = decorator.status;
		for(int i = 0; i < filter.length; i ++)
		{
			if(status == filter[i])
			{
				return i;
			}
		}
		return -1;
	}

	/**
	 * 移除所有容器中缓存的类型为clazz的值对象
	 * @param clazz
	 */
	public void removeVO(Class clazz)
	{
	    Map bin = this.getMap(clazz);
	    if(bin == null)
	        return;
	    bin.clear();

	}

	/**
	 * 移除所有容器中缓存的类型为clazz的状态为status的值对象
	 * @param clazz
	 */
	public void removeVO(Class clazz,int[] status)
	{
	    Map bin = this.getMap(clazz);

	    if(bin == null)
	        return;
	    bin.clear();
	}

	/**
	 * 将clazz类型的所有对象的状态修改为status
	 * @param clazz
	 * @param status
	 */
	protected void updateStatus(Class clazz,int status)
	{
	    Map bin = this.getMap(clazz);

	    if(bin == null)
	        return;
	    Iterator keys = bin.keySet().iterator();
	    while(keys.hasNext())
	    {
	        DecoratorVO dec = (DecoratorVO)bin.get(keys.next());
	        dec.status = status;
	    }

	}


	/**
	 * inner class DecoratorVO implement decorator pattern to decorator an object with a status
	 * @author biaoping.yin
	 *
	 * To change the template for this generated type comment go to
	 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
	 */
	protected static class DecoratorVO implements Serializable
	{
		public DecoratorVO(Object vo, int status)
		{
			this.vo = vo;
			this.status = status;
		}

		public boolean equals(Object obj)
		{
			if(obj != null)
				return vo.equals(obj);
			return false;
		}

		public String toString()
		{
			return "[status = " + status + "]"  + "[vo = " + vo + "]";
		}
		public Object vo;
		public int status;
	}

	/**
	 *
	 * @author biaoping.yin
	 * created on 2004-4-19
	 * version 1.0
	 */
	protected class UtilIterator implements Iterator, Serializable
	{
		Iterator allData;
		DecoratorVO decorator;
		int[] filter;
		public UtilIterator(Iterator allData, int[] filter)
		{
			this.allData = allData;
			this.filter = filter;
			decorator = null;
		}
		/* (non-Javadoc)
		 * @see java.util.Iterator#hasNext()
		 */
		public boolean hasNext()
		{
			boolean found = false;
			while (allData.hasNext() && !found)
			{
				decorator = (DecoratorVO) allData.next();

				//int match = Arrays.binarySearch(filter, decorator.status);
				if(filter == null)
				{
					found = true;
				}
				else
				{
					int match = matchStatus(decorator, filter);
					found = (match != -1);
				}
			}
			if (!found)
				decorator = null;
			return found;
		}
		/* (non-Javadoc)
		 * @see java.util.Iterator#next()
		 */
		public Object next()
		{
			if (decorator != null)
				return decorator.vo;
			log.info("no such element!!!");
			return null;
		}
		/* (non-Javadoc)
		 * @see java.util.Iterator#remove()
		 */
		public void remove()
		{
			throw new UnsupportedOperationException("remove(),未支持的操作");
		}
	}

	/**
	 *
	 * @author biaoping.yin
	 * created on 2004-4-19
	 * version 1.0
	 */
	protected class MapIterator implements Iterator, Serializable
	{
		Iterator allData;
		DecoratorVO decorator;
		int[] filter;
		String field;
		Object fieldValue;
		public MapIterator(Map bin,String field,Object fieldValue)
		{
			this(bin,field,fieldValue,null);
		}
		public MapIterator(Map bin,String field,Object fieldValue,int[] filter)
		{
		    //构造list对各种对象进行排序
		    List list = new ArrayList(bin.values());
			sort(list);
			this.allData = list.iterator();
			this.filter = filter;
			this.field = field;
			this.fieldValue = fieldValue;
			decorator = null;
		}


		/* (non-Javadoc)
		 * @see java.util.Iterator#hasNext()
		 */
		public boolean hasNext()
		{

			boolean found = false;
			while(allData.hasNext() && !found )
			{
				decorator = (DecoratorVO) allData.next();
				found = found();
			}

			if (!found)
				decorator = null;
			return found;
		}

		private boolean found()
		{
			if(filter == null)
				return fieldMatch();
			//return (Arrays.binarySearch(filter, decorator.status) != -1) && fieldMatch();
			return (matchStatus(decorator,filter) != -1) && fieldMatch();
		}

		private boolean fieldMatch()
		{
			CompareUtil util = new CompareUtil();
			if(util.compareField(decorator.vo,field,fieldValue) == 0)
				return true;
			return false;

		}
		/* (non-Javadoc)
		 * @see java.util.Iterator#next()
		 */
		public Object next()
		{
			if (decorator != null)
				return decorator.vo;
			log.info("no such element!!!");
			return null;
		}
		/* (non-Javadoc)
		 * @see java.util.Iterator#remove()
		 */
		public void remove()
		{
			throw new UnsupportedOperationException("remove(),未支持的操作");
		}
	}
}
