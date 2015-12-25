package com.frameworkset.common.tag.pager;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.frameworkset.util.ListInfo;

/**
 * 
 * <p>Title: com.frameworkset.common.tag.pager.ObjectDataInfo.java</p>
 *
 * <p>Description: 封装对象类型的数据获取类</p>
 *
 * <p>Copyright (c) 2006.10 </p>
 *
 * <p>Company: chinacreator</p>
 * @Date 2007-2-4 15:11:55
 * @author biaoping.yin
 * @version 1.0
 */
public class ObjectDataInfoImpl extends DataInfoImpl {
	/**
	 * 存放对象类型数据的变量
	 */
	Object data;
	
	public ObjectDataInfoImpl(Object data)
	{
		this.data = data;
	}
	

	public void initial(String sortKey, boolean desc, long offSet,
			int pageItemsize, boolean isList, HttpServletRequest request) {
		throw new UnsupportedOperationException("public int getDataSize()");

	}

	public void initial(String sql, String dbName, long offSet,
			int pageItemsize, boolean isList, HttpServletRequest request) {
		throw new UnsupportedOperationException("public int getDataSize()");

	}

	public Object getListItems() {
		throw new UnsupportedOperationException("public int getDataSize()");
	}

	public Object[] getPageItemsFromDB() {
		throw new UnsupportedOperationException("public int getDataSize()");
	}

	public Object[] getListItemsFromDB() {
		throw new UnsupportedOperationException("public int getDataSize()");
	}

	public Object getPageItems() {
		throw new UnsupportedOperationException("public int getDataSize()");
	}

	public Class getVOClass() {
		throw new UnsupportedOperationException("public int getDataSize()");
	}

	public long getItemCount() {
//		throw new UnsupportedOperationException("public int getDataSize()");
		return this.getSize(data);
	}
	
	private long getSize(Object obj)
	{
		if(obj == null )
			return 0;
		if(obj instanceof List)
		{
			return ((List)obj).size();
		}
		else if(obj instanceof Map)
		{
			return ((Map)obj).size(); 
		}
		else if(obj instanceof Set)
		{
			return ((Set)obj).size(); 
		}
		else if(obj.getClass().isArray())
		{
			return ((Object[])obj).length; 
		}
		return 1;
	}

	public int getDataSize() {
//		throw new UnsupportedOperationException("public int getDataSize()");
		return (int)this.getSize(data);
	}
	
	
	public Object getObjectData()
	{
		return this.data;
	}


	protected ListInfo getDataList(String sortKey, boolean desc, long offSet, int pageItemsize) {
		throw new UnsupportedOperationException("public int getDataSize()");
	}


	protected ListInfo getDataList(String sortKey, boolean desc) {
		throw new UnsupportedOperationException("public int getDataSize()");
	}

}
