/*****************************************************************************
 *                                                                           *
 *  This file is part of the tna framework distribution.                     *
 *  Documentation and updates may be get from  biaoping.yin the author of    *
 *  this framework							     *
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

import com.frameworkset.common.poolman.SQLParams;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

/**
 * 业务组件必须实现的3个接口用来获取具体业务的数据集合，为分页Tag提供数据
 * 获取值对象的Class句柄
 * @author biaoping.yin
 * 2005-3-25
 * version 1.0
 */


public interface DataInfo
{
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
							boolean isList,
							HttpServletRequest request);

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
//						String sortKey,
//						boolean desc,
						boolean isList,
						HttpServletRequest request);


    /**
     * 如果分页tag只是作为列表来实现时，调用该方法获取
     * 要显示的列表数据
     * @return Object
     */
	public Object getListItems();


    /**
     * 分页显示时从数据库获取每页的数据项
     * @return Hashtable[]
     */
	public Object[] getPageItemsFromDB();

	/**
     * 如果分页tag只是作为列表来实现时，调用该方法从数据库中获取
     * 要显示的列表数据
     * @return java.util.List
     */
	public Object[] getListItemsFromDB();


    /**
     * 分页显示时获取每页的数据项
     * @return Object
     */
	public Object getPageItems();

    /**
     * 获取值对象的Class句柄
     * @return java.lang.Class
     */
    public Class getVOClass();

    /**
     * 获取数据源中数据的总数,分页时调用
     * @return int
     */
    public long getItemCount();

    /**
     * 获取当前页面记录数
     * @return 当前页面记录数
     */
    public int getDataSize();
    
    public int getDataResultSize();
    
    /**
     * 获取对象类型的数据
     * @return
     */
    public Object getObjectData();
    
    public void initial(String sql,
            String dbName,
            long offSet,
            int pageItemsize,
            boolean listMode,
//          String sortKey,
//          boolean desc,
            HttpServletRequest request,SQLParams params);
    /**
     * 识别当前查询是否是more分页，如果是more分页则标签库无需设置总记录数信息 
     * @return
     */
    public boolean isMore();
    
//    public boolean isdbdata();
    
    

}
