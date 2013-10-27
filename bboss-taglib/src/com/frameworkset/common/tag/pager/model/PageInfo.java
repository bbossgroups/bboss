/*****************************************************************************
 *                                                                           *
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
package com.frameworkset.common.tag.pager.model;

import com.frameworkset.common.tag.pager.DataInfo;



/**
 * 保存所有的页面参数
 * @author biaoping.yin
 * created on 2005-5-18
 * version 1.0
 */
public class PageInfo implements ModelObject{
    /**每页显示的记录条数*/
    protected int maxPageItems = 0;
    /**记录总数*/
    protected long totalSize = 0;
    /**标识列表模式*/
    protected boolean isList = false;
    /**数据库查询语句*/
    protected String statement ;
    /**数据库链接池名称*/
    protected String dbName;
    /**排序字段*/
    protected String sortKey;
    /**页面查询参数*/
    protected String queryString;
    /**
     * 分页时，控制是否导出全部数据，
     */
    private boolean eportAll = false;

    /**
     * 定义页面上要显示的所有字段域
     */
    protected MetaDatas metaDatas = null;
    /**页号*/
	private Object pageNumberInteger;
	/**页码*/
	private long pageNumber;

	private DataInfo dataInfo ;

    /**
     * @return Returns the maxPageItem.
     */
    public int getMaxPageItems() {
        return maxPageItems;
    }
    /**
     * @param maxPageItems The maxPageItem to set.
     */
    public void setMaxPageItems(int maxPageItems) {
    }
    /**
     * @return Returns the totalSize.
     */
    public long getTotalSize() {
        return totalSize;
    }
    /**
     * @param totalSize The totalSize to set.
     */
    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }
    /**
     * @return Returns the dbName.
     */
    public String getDbName() {
        return dbName;
    }
    /**
     * @param dbName The dbName to set.
     */
    public void setDbName(String dbName) {
        this.dbName = dbName;
    }
    /**
     * @return Returns the isList.
     */
    public boolean isList() {
        return isList;
    }
    /**
     * @param isList The isList to set.
     */
    public void setList(boolean isList) {
        this.isList = isList;
    }
    /**
     * @return Returns the sortKey.
     */
    public String getSortKey() {
        return sortKey;
    }
    /**
     * @param sortKey The sortKey to set.
     */
    public void setSortKey(String sortKey) {
        this.sortKey = sortKey;
    }
    /**
     * @return Returns the statement.
     */
    public String getStatement() {
        return statement;
    }
    /**
     * @param statement The statement to set.
     */
    public void setStatement(String statement) {
        this.statement = statement;
    }
    /**
     * @return Returns the eportAll.
     */
    public boolean isEportAll() {
        return eportAll;
    }
    /**
     * @param eportAll The eportAll to set.
     */
    public void setEportAll(boolean eportAll) {
        this.eportAll = eportAll;
    }
    /**
	 * 功能说明：计算当前页面编号
	 * @param offset
	 * @return long
	 */
	private final long pageNumber(long offset) {
		return (offset / maxPageItems) + (offset % maxPageItems == 0 ? 0 : 1);
	}

	/**
	 * 获取页面总数量
	 *
	 * @return int
	 */
	final long getPageCount() {

		return pageNumber(getTotalSize());
	}
	/**
	 * 获取最后一页的页码，比如共有5页，返回4。如果只有0页则返回0；
	 * @return int
	 */
	public long getLastPageNumber()
	{
		return Math.max(0,getPageCount() - 1);
	}

    public void initContext(long offset,long totalsize)
	{
    	this.totalSize = totalsize;
		try {

			offset = Math.max(0, offset);
			long newPageCount = pageNumber(offset);
			long lastPagerNumber = getLastPageNumber();
			if(newPageCount > lastPagerNumber)
			{
				offset =  lastPagerNumber * getMaxPageItems();
				/**
				 * 重新获取数据，如果数据库底层能够处理这个问题，将不需要重新获取数据
				 */
				//setDataInfo(getData());
			}

			//if (isOffset) 于2004/4/30注释
			//itemCount = offset;

		}
		catch (NumberFormatException ignore)
		{
			ignore.printStackTrace();
		}
		/**
		 * 当前第几页,从零开始
		 */
		pageNumber = pageNumber(offset);
		/**
		 * 当前页码，从1开始
		 */
		pageNumberInteger = new Long(1+pageNumber);
	}
	/**
	 * @return Returns the pageNumber.
	 */
	public long getPageNumber() {
		return pageNumber;
	}
	/**
	 * @param pageNumber The pageNumber to set.
	 */
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	/**
	 * @return Returns the pageNumberInteger.
	 */
	public Object getPageNumberInteger() {
		return pageNumberInteger;
	}
	/**
	 * @param pageNumberInteger The pageNumberInteger to set.
	 */
	public void setPageNumberInteger(Object pageNumberInteger) {
		this.pageNumberInteger = pageNumberInteger;
	}
	/**
	 * @return Returns the queryString.
	 */
	public String getQueryString() {
		return queryString;
	}
	/**
	 * @param queryString The queryString to set.
	 */
	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}
    /**
     * @return Returns the dataInfo.
     */
    public DataInfo getDataInfo() {
        return dataInfo;
    }
    /**
     * @param dataInfo The dataInfo to set.
     */
    public void setDataInfo(DataInfo dataInfo) {
        this.dataInfo = dataInfo;
    }
    /**
     * @return Returns the metaDatas.
     */
    public MetaDatas getMetaDatas() {
        return metaDatas;
    }
    /**
     * @param metaDatas The metaDatas to set.
     */
    public void setMetaDatas(MetaDatas metaDatas) {
        this.metaDatas = metaDatas;
    }
}
