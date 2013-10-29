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
 *  Author of Learning Java 						     					 *
 *                                                                           *
 *****************************************************************************/
package com.frameworkset.common.tag.pager;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.frameworkset.security.AccessControlInf;
import org.frameworkset.security.SecurityUtil;

import com.frameworkset.common.poolman.SQLParams;
import com.frameworkset.common.tag.BaseTag;
import com.frameworkset.util.ListInfo;

/**
 * @author biaoping.yin
 * tag开发人员需要继承DataInfoImpl类，实现以下抽象方法：
 * protected abstract ListInfo getDataList(String sortKey,
										 boolean desc,
										 int offSet,
										 int pageItemsize)，该方法分页调用
  protected abstract ListInfo getDataList(String sortKey,
									 boolean desc)，该方法列表调用

 *
 */
public abstract class DataInfoImpl implements DataInfo
{
    
	private ListInfo listInfo = null;
	String sortKey;
	boolean desc = true;
	int pageItemsize;
    long offSet;
	protected transient HttpServletRequest request = null;
	protected transient  HttpSession session = null;
    protected transient  AccessControlInf accessControl = null;
	protected boolean listMode = false;
	boolean first;

	/**
     * 初始化获取分页/列表数据的必要参数
     * @param sortKey 排序字段
     * @param desc 排序顺序，true表示降序，false表示升序
     * @param offSet 获取分页数据时，用该参数设置获取数据的起点
     * @param pageItemsize 获取分页数据时，用该参数设置获取数据的条数
     */
	public void initial(String sortKey,
						boolean desc,
						long offSet,
						int pageItemsize,
						boolean listMode,
						HttpServletRequest request)
	{
		this.sortKey = sortKey;
		this.desc = desc;
		this.offSet = offSet;
		this.pageItemsize =pageItemsize;
		this.request = request;
		session = request.getSession(false);
		/**
		 * 2009.07.02 注释，如果放入系统平台需要打开
		 */
		if(BaseTag.ENABLE_TAG_SECURITY)
		{
//                    accessControl = AccessControl.getAccessControl();
//                    if(accessControl == null)
//                    {
//                    	accessControl = AccessControl.getInstance();
//                    	accessControl.checkAccess(request,null,null,false);
//                    }
			accessControl = SecurityUtil.getAccessControl(request, null, null);
		}
		listInfo = null;
		this.listMode = listMode;
		first = true;

	}

	/**
     * 无需实现，初始化获取分页/列表数据的必要参数
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
						HttpServletRequest request)
	{
	}

	/**
	* 分页显示时获取每页的数据项
	* sortKey:排序字段
	* desc:排序的秩序，true为降序，false为升序
	* @param offSet - 从数据源获取数据的游标位置
	*
	* @param pageItemsize - 每页显示的数据条数
	* @return java.util.List
	*/
	protected abstract ListInfo getDataList(String sortKey,
										 boolean desc,
										 long offSet,
										 int pageItemsize);



	/**
	 * 如果分页tag只是作为列表来实现时，调用该方法
	 * sortKey:排序字段
	 * desc:排序的秩序，true为降序，false为升序
	 * @return java.util.List
	 */
	protected abstract ListInfo getDataList(String sortKey,
									 boolean desc);
	/**
	 * 识别查询是否是more分页查询
	 * @return
	 */
	public boolean isMore()
	{
		if(first)
		{
		    if(!listMode)
		        listInfo = getDataList(sortKey,desc,offSet,pageItemsize);
		    else
		        listInfo = getDataList(sortKey,desc);
		    first = false;
		}
		if(listInfo == null)
			return false;
		return listInfo.isMore();
	}
	/**
	 * tag中调用以下方法获取分页时的总的数据条数，以便计算页面总数
	 */
	public long getItemCount()
		//throws Exception
	{
		if(first)
		{
		    if(!listMode)
		        listInfo = getDataList(sortKey,desc,offSet,pageItemsize);
		    else
		        listInfo = getDataList(sortKey,desc);
		    first = false;
		}
		if(listInfo == null)
			return 0;
		return listInfo.getTotalSize();
	}

	/**
	 * tag中调用以下方法获取分页数据
	 */
	public Object getPageItems()
	{
		if(first)
		{
			listInfo = getDataList(sortKey,desc,offSet,pageItemsize);
			first = false;
		}
		if(listInfo == null)
			return new ArrayList();
		Object datas = listInfo.getObjectDatas();
		return datas == null?new ArrayList():datas;
	}

	/**
	 * tag中调用以下方法获取列表数据
	 */
	public Object getListItems()
	{
	    if(first)
	    {
	        listInfo = getDataList(sortKey,desc);
	        first = false;
	    }
	    if(listInfo == null)
			return new ArrayList();
	    Object datas = listInfo.getObjectDatas();
		return datas == null?new ArrayList():datas;
//		return listInfo.getDatas() == null?new ArrayList():listInfo.getDatas();
	}

	/**
	 * tag中调用以下方法获取返回的数据集中所包含的值对象的Class对象
	 */
	public Class getVOClass() {
//		if(listInfo == null
//		     || listInfo.getDatas() == null
//		     || listInfo.getDatas().size() == 0)
//			return null;
//		return listInfo.getDatas().get(0).getClass();
		return null;
	}

	 /**
     * 分页显示时从数据库获取每页的数据项
     * @return Hashtable[]
     */
	public Object[] getPageItemsFromDB()
	{
	    return null;
	}

	/**
     * 如果分页tag只是作为列表来实现时，调用该方法从数据库中获取
     * 要显示的列表数据
     * @return java.util.List
     */
	public Object[] getListItemsFromDB(){return null;}

	/**
	 * 获取当前页面的记录条数
	 */
	public int getDataSize()
	{
	    if(first)
		{
		    if(!listMode)
		        listInfo = getDataList(sortKey,desc,offSet,pageItemsize);
		    else
		        listInfo = getDataList(sortKey,desc);
		    first = false;
		}
		if(listInfo == null )
			return 0;
		return listInfo.getSize();
	}
	
	/**
	 * 获取当前页面的记录条数
	 */
	public int getDataResultSize()
	{
	    if(first)
		{
		    if(!listMode)
		        listInfo = getDataList(sortKey,desc,offSet,pageItemsize);
		    else
		        listInfo = getDataList(sortKey,desc);
		    first = false;
		}
		if(listInfo == null )
			return 0;
		return listInfo.getResultSize();
	}

	/**
	 * 对列表进行分页操作,数据源是一个列表
	 * @param datas 列表数据
	 * @param offset 获取数据的起始位置
	 * @param pageItems 获取数据的条数
	 * @return ListInfo 对分页数据和总记录条数的封装类
	 */

	public static ListInfo pagerList(List datas,int offset,int pageItems)
	{
     
		if(datas == null)
			return null;
		List list = new ArrayList();
		if(offset >= datas.size())
		{
		    int temp = datas.size() % pageItems;
		    offset = datas.size() - temp;
		}
		for(int i = offset; i < datas.size() && i < offset + pageItems; i ++)
		{
		    list.add(datas.get(i));
		}
		ListInfo listInfo = new ListInfo();
		listInfo.setDatas(list);
		listInfo.setTotalSize(datas.size());
		return listInfo;
	}
	
	public Object getObjectData()
	{
		throw new UnsupportedOperationException("getObjectData()");
	}
	
	public void initial(String sql,
            String dbName,
            long offSet,
            int pageItemsize,
            boolean listMode,
//          String sortKey,
//          boolean desc,
            HttpServletRequest request,SQLParams params)
	{
	    
	}
	
}
