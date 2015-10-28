package com.frameworkset.common.hibernate.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.type.Type;
import org.framework.orm.ObjectRetrievalFailureException;
import org.framework.orm.hibernate3.support.HibernateDaoSupport;

import com.frameworkset.util.ListInfo;

/**
 * <p>Title: BaseDAOHibernate</p>
 *
 * <p>Description: dao的公共基类，提供了一些公用的数据库查询</p>
 *

 *
 * <p>
 * bboss workgroup
 * </p>
 * <p>
 * Copyright (c) 2007
 * </p>
 * 
 * @Date 2009-6-1 下午08:58:51
 * @author biaoping.yin
 * @version 1.0
 */
public class BaseDAOHibernate
    extends HibernateDaoSupport 
	implements DAO,Serializable
{
	private static Logger log = Logger.getLogger(BaseDAOHibernate.class);
	public BaseDAOHibernate()
	{

	}

	public void removeObject(Object o)
		throws DataAccessException
	{
		getHibernateTemplate().delete(o);
	}

	public Object getObject(Class clazz, Serializable id)
		throws DataAccessException
	{
		Object o = getHibernateTemplate().get(clazz, id);
		if(o == null)
			throw new ObjectRetrievalFailureException(clazz, id);
		else
			return o;
	}

	public List getObjects(Class clazz)
		throws DataAccessException
	{
		return getHibernateTemplate().loadAll(clazz);
	}

	public void removeObject(Class clazz, Serializable id)
		throws DataAccessException
	{
		getHibernateTemplate().delete(getObject(clazz, id));
	}

	public void saveObject(Object o)
		throws DataAccessException
	{
		getHibernateTemplate().save(o);
	}

	public void updateObject(Object o)
		throws DataAccessException
	{
		getHibernateTemplate().update(o);
	}

	/**
	 * 查询类型为clazz的所有记录
	 * @param clazz Class
	 * @param start 分页数据起始位置
 	 * @param maxSize 获取记录最大条数
	 * @return ListInfo 封装结果列表和总记录数
	 * @throws DataAccessException
	 */
	public ListInfo getObjects(Class clazz,long start,int maxSize)
		throws DataAccessException
	{

		return find("from " + clazz.getName(), start,maxSize);
	}


	/**
	 * 执行预编译分页查询，并且返回查询结果和相关的分页信息（总记录数）
	 * @param sql String 预编译查询语句
	 * @param objs Object[] 预编译查询语句参数数组
	 * @param types Type[] 预编译查询语句参数类型数组
 	 * @param start 分页数据起始位置
 	 * @param maxSize 获取记录最大条数
	 * @return ListInfo 封装结果列表和总记录数
	 * @throws DataAccessException
	 * @deprecated 本方法是与hibernate 2.x兼容而写，替代的方法为protected List find(String sql, Object objs[], Pagination pagination)
	 */
	public ListInfo find(String sql, Object objs[], Type types[], long start,int maxSize)
		throws DataAccessException
	{
		return find(sql, objs, start,maxSize);
	}

	/**
	 * 执行预编译分页查询，并且返回查询结果和相关的分页信息（总记录数）
	 * @param sql String 预编译查询语句
	 * @param objs Object[] 预编译查询语句参数数组
	 * @param types Type[] 预编译查询语句参数类型数组
	 * @return ListInfo 封装结果列表和总记录数
	 * @throws DataAccessException
	 * @deprecated 本方法是与hibernate 2.x兼容而写，替代的方法为protected List find(String sql, Object objs[])
	 */
	public List find(String sql, Object objs[], Type types[])
		throws DataAccessException
	{
		return super.getHibernateTemplate().find(sql, objs);
	}

	/**
	 * 执行预编译分页查询，并且返回查询结果和相关的分页信息（总记录数）
	 * @param sql String 预编译查询语句
	 * @param objs Object[] 预编译查询语句参数数组
	 * @param types Type[] 预编译查询语句参数类型数组
	 *
	 * @return ListInfo 封装结果列表和总记录数
	 * @throws DataAccessException
	 * @deprecated 本方法是与hibernate 2.x兼容而写，替代的方法为protected List find(String sql, Object objs[])
	 */
	public List find(String sql, Object objs[])
		throws DataAccessException
	{
		return super.getHibernateTemplate().find(sql, objs);
	}


	/**
	 * 执行预编译分页查询，并且返回查询结果和相关的分页信息（总记录数）
	 * @param sql String 预编译查询语句
	 * @param objs Object[] 预编译查询语句参数数组
	 * @param start 分页数据起始位置
 	 * @param maxSize 获取记录最大条数
	 * @return ListInfo 封装结果列表和总记录数
	 * @throws DataAccessException
	 */
	public ListInfo find(String sql, Object objs[],long start,int maxSize)
		throws DataAccessException
	{
		ListInfo listInfo = new ListInfo();
		listInfo.setTotalSize(loadTotalSize(sql, objs));
		Session session = getSession();
		Query sqlQuery = null;
		List ls = null;
		try
		{
			sqlQuery = session.createQuery(sql);
			if(objs != null && objs.length > 0 )
			{
				for(int i = 0; i < objs.length; i++)
					sqlQuery.setParameter(i, objs[i]);

			}
			sqlQuery.setFirstResult((int)start).setMaxResults(maxSize);
			ls = sqlQuery.list();
			if(ls == null)
				ls = new ArrayList(0);
		}
		catch(HibernateException e)
		{
			log.error(e);
		}
		listInfo.setDatas(ls);
		return listInfo;
	}

	/**
	 * 执行分页查询
	 * @param sql String 查询语句
	 * @param start 分页数据起始位置
 	 * @param maxSize 获取记录最大条数
	 * @return ListInfo 封装结果列表和总记录数
	 * @throws DataAccessException
	 */
	public ListInfo find(String sql, long start,int maxSize)
		throws DataAccessException
	{
		return find(sql, new Object[0], start, maxSize);
	}

	/**
	 * 执行查询
	 * @param sql String 查询语句
	 * @return List 结果列表
	 * @throws DataAccessException
	 */
	public List find(String sql)
		throws DataAccessException
	{
		return getHibernateTemplate().find(sql);
	}


	/**
	 * 执行预编译查询语句，返回结果列表
	 * @param sql String 预编译语句
	 * @param obj Object 预编译查询参数值
	 * @param type Type 预编译查询参数类型
	 * @param start 分页数据起始位置
 	 * @param maxSize 获取记录最大条数
	 * @return ListInfo 封装结果列表和总记录数
	 * @throws DataAccessException
	 * @deprecated 本方法为与hibernate 2.x兼容而设计的,hibernate 3.x替代的方法为protected List find(String sql, Object obj, Pagination pagination)
	 */
	public ListInfo find(String sql, Object obj, Type type, long start, int maxSize)
		throws DataAccessException
	{
		return find(sql, obj, start,maxSize);
	}

	/**
	 * 执行预编译查询语句，返回结果列表
	 * @param sql String 预编译语句
	 * @param obj Object 预编译查询参数值
	 * @param start 分页数据起始位置
 	 * @param maxSize 获取记录最大条数
	 * @return ListInfo 封装结果列表和总记录数
	 * @throws DataAccessException
	 */
	public ListInfo find(String sql, Object obj,long start, int maxSize)
		throws DataAccessException
	{
			return find(sql, new Object[] {
				obj
			}, start,maxSize);
	}



	/**
	 * 执行预编译查询语句，返回结果列表
	 * @param sql String 预编译语句
	 * @param obj Object 预编译查询参数值
	 * @return List 结果列表
	 * @throws DataAccessException
	 */
	public List find(String sql, Object obj)
		throws DataAccessException
	{
		return getHibernateTemplate().find(sql, obj);
	}



	/**
	 * 执行预编译sql语句，获取查询结果的总记录数，objs[]数组存放查询参数
	 * @param sql String
	 * @param objs Object[]
	 * @return long
	 * @throws DataAccessException
	 */
	public long loadTotalSize(String sql, Object objs[])
		throws DataAccessException
	{
		long count = 0L;
		try
		{
			String midSql = getCountSql(sql);
			midSql = "select count(*) " + midSql;

			//hibernate 3.x 中对应的方法
			List ls = getHibernateTemplate().find(midSql, objs);
			if(ls != null && ls.size() > 0)
			{
				Object obj = ls.get(0);
				if(obj instanceof Integer)
					count = ((Integer)obj).longValue();
				else
				if(obj instanceof Long)
					count = ((Long)obj).longValue();
			}
		}
		catch(Exception he)
		{
			log.error(he.getMessage(), he);
		}
		return count;
	}

	/**
	 * 获取记录总数
	 * 与hibernate 2.x兼容的方法
	 * @param sql String
	 * @param objs Object[]
	 * @param types Type[]
	 * @return long
	 * @throws DataAccessException
	 * @deprecated 替代的方法为protected long loadTotalSize(String sql, Object objs[])
	 */
	public long loadTotalSize(String sql, Object objs[], Type types[])
		throws DataAccessException
	{
		return loadTotalSize(sql, objs);
	}

	/**
	 * 获取hql中的from子句
	 * @param sql String
	 * @return String
	 */
	public String getCountSql(String sql)
	{
		String midSql = sql;
		int count = StringUtils.indexOf(midSql.toLowerCase(), "from");
		midSql = StringUtils.substring(midSql, count);
		return midSql;
	}

	/**
	 * 批量插入
	 * @param objs Collection
	 * @return Object
	 */
	public void batchInsert(Collection objs)
	{
		getHibernateTemplate().saveOrUpdateAll(objs);
	}

	/**
	 * 批量更新
	 * @param objs Collection
	 */
	public void batchUpdate(Collection objs)
	{
		getHibernateTemplate().saveOrUpdateAll(objs);
	}

	/**
	 * 批量删除
	 * @param objs Collection
	 */
	public void batchDelete(Collection objs)
	{
		getHibernateTemplate().deleteAll(objs);
	}
}

