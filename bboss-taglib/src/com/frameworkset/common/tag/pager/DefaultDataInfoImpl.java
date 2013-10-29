/*****************************************************************************
 *  Created on 2005-3-25                                                     *
 *  This file is part of the tna framework distribution.                     *
 *  Documentation and updates may be get from  biaoping.yin the author of    *
 *  this framework							     							 *
 *                                                                           *
 *  Sun Public License Notice:                                               *
 *                                                                           *
 *  The contents of this file are subject to the Sun Public License Version  *
 *  1.0 (the "License"); you may not use this file except in compliance with *
 *  the License. A copy of the License is available at http://www.sun.com    *
 *                                                                           *
 *  The Original Code is tag. The Initial Developer of the Original          *
 *  Code is biaoping yin. Portions created by biaoping yin are Copyright     *
 *  (C) 2000.  All Rights Reserved.                                          *
 *                                                                           *
 *  GNU Public License Notice:                                               *
 *                                                                           *
 *  Alternatively, the contents of this file may be used under the terms of  *
 *  the GNU Lesser General Public License (the "LGPL"), in which case the    *
 *  provisions of LGPL are applicable instead of those above. If you wish to *
 *  allow use of your version of this file only under the  terms of the LGPL *
 *  and not to allow others to use your version of this file under the SPL,  *
 *  indicate your decision by deleting the provisions above and replace      *
 *  them with the notice and other provisions required by the LGPL.  If you  *
 *  do not delete the provisions above, a recipient may use your version of  *
 *  this file under either the SPL or the LGPL.                              *
 *                                                                           *
 *  biaoping.yin (yin-bp@163.com)                                            * 
 *                                                                           *
 *****************************************************************************/
package com.frameworkset.common.tag.pager;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.common.poolman.Record;
import com.frameworkset.common.poolman.SQLParams;
import com.frameworkset.util.ListInfo;

import java.sql.SQLException;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

/**
 * 提供DataInfo接口的缺省实现，从数据库中获取数据集
 * @author biaoping.yin
 * created on 2005-3-25
 * version 1.0
 */
public class DefaultDataInfoImpl implements DataInfo {

    /**封装页面数据变量*/
    private ListInfo listInfo = null;
	private int pageItemsize;
    private long offSet;
	protected HttpServletRequest request = null;
	protected HttpSession session = null;

	protected String sql = null;
	protected String dbName = null;
	protected boolean listMode;
	protected boolean first = true;
	private static final Logger log = Logger.getLogger(DefaultDataInfoImpl.class);
	/**
	 * 标识查询是否是more查询
	 */
	private boolean moreQuery;
	/**
	 * 预编译处理参数
	 */
    private SQLParams params;


    /**
     * 无需实现
     * @param sortKey
     * @param desc
     * @param offSet
     * @param pageItemsize
     * @param listMode
     * @param request
     */
    public void initial(String sortKey, boolean desc, long offSet,
            int pageItemsize, boolean listMode,HttpServletRequest request) {

    }
    
    

    /**
     * 初始化获取分页/列表数据的必要参数
     * @param sql 数据库查询语句
     * @param dbName 数据库连接池名称
     * @param offSet 获取分页数据时，用该参数设置获取数据的起点
     * @param pageItemsize 获取分页数据时，用该参数设置获取数据的条数
     */
    public void initial(String sql,
            			String dbName,
            			long offSet,
            			int pageItemsize,
            			boolean listMode,
//            			String sortKey,
//            			boolean desc,
            			HttpServletRequest request) {
        initial( sql,
                dbName,
                offSet,
                pageItemsize,
                listMode,
//              String sortKey,
//              boolean desc,
                request,(SQLParams)null);

    }
    
    
    /**
     * 初始化获取分页/列表数据的必要参数
     * @param sql 数据库查询语句
     * @param dbName 数据库连接池名称
     * @param offSet 获取分页数据时，用该参数设置获取数据的起点
     * @param pageItemsize 获取分页数据时，用该参数设置获取数据的条数
     */
    public void initial(String sql,
                        String dbName,
                        long offSet,
                        int pageItemsize,
                        boolean listMode,
//                      String sortKey,
//                      boolean desc,
                        HttpServletRequest request,SQLParams params) {
        this.sql = sql;
        this.dbName = dbName;
        this.offSet = offSet;
        this.pageItemsize =pageItemsize;
        this.request = request;
        this.params = params;

        if(request != null)
            session = request.getSession(false);
        this.listMode = listMode;
        listInfo = null;
        first = true;

    }
    
    


    /**
     * 从数据库中获取分页页面数据
     * @see com.frameworkset.common.tag.pager.DataInfo#getPageItemsFromDB()
     */
    public Object[] getPageItemsFromDB() {

        if(first)
        {
            listInfo = getDataFromDB(sql,dbName,offSet,pageItemsize);
            first = false;
        }
        if(listInfo == null)
            return null;
        return listInfo.getArrayDatas();
    }

    /**
     * 从数据库中获取列表数据
     * @see com.frameworkset.common.tag.pager.DataInfo#getListItemsFromDB()
     */
    public Object[] getListItemsFromDB() {
        if(first)
        {
            listInfo = getListItemsFromDB(sql,dbName);
            first = false;
        }
        if(listInfo == null)
            return null;
        return listInfo.getArrayDatas();
    }



    /**
     * 获取数据封装类的Class句柄
     */
    public Class getVOClass() {

        return Hashtable.class;
    }

    /* (non-Javadoc)
     * @see com.frameworkset.common.tag.pager.DataInfo#getItemCount()
     */
    public long getItemCount() {
        if(first)
        {
            if(!listMode)
                listInfo = getDataFromDB(sql,dbName,offSet,pageItemsize);
            else
                listInfo = getListItemsFromDB(sql,dbName);
            first = false;
        }
        if(listInfo == null)
            return 0;
        return listInfo.getTotalSize();
    }
    
    public int getDataResultSize()
    {
    	if(first)
        {
            if(!listMode)
                listInfo = getDataFromDB(sql,dbName,offSet,pageItemsize);
            else
                listInfo = getListItemsFromDB(sql,dbName);
            first = false;
        }
        if(listInfo == null)
            return 0;
        return listInfo.getResultSize();
    }

    /**
	* 分页显示时从数据库获取每页的数据项，完成实际访问数据库的操作
	* sql:查询语句
	* dbName:数据库连接池的名称
	* desc:排序的秩序，true为降序，false为升序
	* @param offSet - 从数据源获取数据的游标位置
	*
	* @param pageItemsize - 每页显示的数据条数
	* @return ListInfo
	*/
	protected ListInfo getDataFromDB(String sql,
									 String dbName,
									 long offSet,
									 int pageItemsize)
	{
	    //定义数据库访问对象
	    
	    try {
	        ListInfo listInfo = new ListInfo();
	        if(this.params == null)
	        {
	            DBUtil dbUtil = new DBUtil();
	            Hashtable[] tables = (Hashtable[])dbUtil.executeSelectForObjectArray(dbName,sql,offSet,pageItemsize,Record.class);            
                listInfo.setArrayDatas(tables);
                listInfo.setTotalSize(dbUtil.getLongTotalSize());
                listInfo.setResultSize(dbUtil.size());
                return listInfo;
	        }
	        else
	        {
	            PreparedDBUtil dbUtil = new PreparedDBUtil();
	            dbUtil.preparedSelect(params.copy(), dbName, sql, offSet, pageItemsize);
	            Hashtable[] tables = (Hashtable[])dbUtil.executePreparedForObjectArray(Record.class);            
                listInfo.setArrayDatas(tables);
                listInfo.setTotalSize(dbUtil.getLongTotalSize());
                listInfo.setResultSize(dbUtil.size());
                return listInfo;
	        }
        } catch (SQLException e) {
            log.error(e);
	        return null;
        }

	}
	
	

	/**
	 * 完成从数据库获取列表显示数据的实际操作
	 */
	public ListInfo getListItemsFromDB(String sql,
			  						   String dbName)
	{
	   
	    
	    try {
	        ListInfo listInfo = new ListInfo();
	        if(this.params == null)
            {
	            //定义数据库访问对象
	            DBUtil dbUtil = new DBUtil();
	            
                Hashtable[] tables = (Hashtable[])dbUtil.executeSelectForObjectArray(dbName,sql,Record.class);
                listInfo.setArrayDatas(tables);
                listInfo.setResultSize(dbUtil.size());
                return listInfo;
            }
	        else
	        {
	          //定义数据库访问对象
                PreparedDBUtil dbUtil = new PreparedDBUtil();
                dbUtil.setMore(this.moreQuery);
                dbUtil.preparedSelect(params.copy(), dbName, sql);
                Hashtable[] tables = (Hashtable[])dbUtil.executePreparedForObjectArray(Record.class);  
                listInfo.setArrayDatas(tables);
                listInfo.setMore(this.moreQuery);
                listInfo.setResultSize(dbUtil.size());
                return listInfo;
	        }
        } catch (SQLException e) {
            log.error(e);
	        return null;
        }
	}

	/**
     * 未实现
     * @see com.frameworkset.common.tag.pager.DataInfo#getPageItems()
     */
    public Object getPageItems() {

        return null;
    }

    /**
     * 未实现
     */
    public Object getListItems() {
        return null;
    }

    /* (non-Javadoc)
     * @see com.frameworkset.common.tag.pager.DataInfo#getDataSize()
     */
    public int getDataSize() {
        if(first)
        {
            if(!listMode)
                listInfo = getDataFromDB(sql,dbName,offSet,pageItemsize);
            else
                listInfo = getListItemsFromDB(sql,dbName);
            first = false;
        }
        return listInfo == null
        				|| listInfo.getArrayDatas() == null
        					?0:listInfo.getArrayDatas().length;
    }
    
    

	public Object getObjectData() {
		throw new UnsupportedOperationException("getObjectData()");
	}

	public boolean isMoreQuery() {
		return moreQuery;
	}

	public void setMoreQuery(boolean moreQuery) {
		this.moreQuery = moreQuery;
	}

	@Override
	public boolean isMore() {
		if(first)
        {
            if(!listMode)
                listInfo = getDataFromDB(sql,dbName,offSet,pageItemsize);
            else
                listInfo = getListItemsFromDB(sql,dbName);
            first = false;
        }
        if(listInfo == null)
            return moreQuery;
        return listInfo.isMore();
	}
}
