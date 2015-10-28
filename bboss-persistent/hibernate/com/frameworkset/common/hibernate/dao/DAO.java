package com.frameworkset.common.hibernate.dao;


import java.util.List;
import com.frameworkset.util.ListInfo;
import org.hibernate.type.Type;
import java.io.Serializable;
import java.util.Collection;

/**
 * <p>Title: DAO</p>
 *
 * <p>Description: dao的接口</p>
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
public interface DAO extends Serializable{
	public void removeObject(Object o)
		throws DataAccessException;


	public Object getObject(Class clazz, Serializable id)
		throws DataAccessException;

	public List getObjects(Class clazz)
		throws DataAccessException;

	public void removeObject(Class clazz, Serializable id)
		throws DataAccessException;


	public void saveObject(Object o)
		throws DataAccessException
	;

	public void updateObject(Object o)
		throws DataAccessException
	;

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
	;


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
	;
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
	;

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
	;


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
		throws DataAccessException;


	/**
	 * 执行分页查询
	 * @param sql String 查询语句
	 * @param start 分页数据起始位置
	 * @param maxSize 获取记录最大条数
	 * @return ListInfo 封装结果列表和总记录数
	 * @throws DataAccessException
	 */
	public ListInfo find(String sql, long start,int maxSize)
		throws DataAccessException;

	/**
	 * 执行查询
	 * @param sql String 查询语句
	 * @return List 结果列表
	 * @throws DataAccessException
	 */
	public List find(String sql)
		throws DataAccessException
	;


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
		throws DataAccessException;

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
		throws DataAccessException;




	/**
	 * 执行预编译查询语句，返回结果列表
	 * @param sql String 预编译语句
	 * @param obj Object 预编译查询参数值
	 * @return List 结果列表
	 * @throws DataAccessException
	 */
	public List find(String sql, Object obj)
		throws DataAccessException;
	/**
	 * 执行预编译sql语句，获取查询结果的总记录数，objs[]数组存放查询参数
	 * @param sql String
	 * @param objs Object[]
	 * @return long
	 * @throws DataAccessException
	 */
	public long loadTotalSize(String sql, Object objs[])
		throws DataAccessException;

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
		;

	/**
	 * 获取hql中的from子句
	 * @param sql String
	 * @return String
	 */
	public String getCountSql(String sql);

	/**
	 * 批量插入
	 * @param objs Collection
	 * @return Object
	 */
	public void batchInsert(Collection objs);

	/**
	 * 批量更新
	 * @param objs Collection
	 */
	public void batchUpdate(Collection objs);

	/**
	 * 批量删除
	 * @param objs Collection
	 */
	public void batchDelete(Collection objs);




}
