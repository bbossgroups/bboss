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


import java.util.Stack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import com.frameworkset.common.tag.pager.DataInfo;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.common.tag.pager.DefaultDataInfoImpl;
import com.frameworkset.common.tag.pager.tags.LoadDataException;
import com.frameworkset.common.tag.pager.tags.PagerTag;

/**
 * 输出数据到word，pdf，excel，cvs，xml文件，
 * 包括详细信息的输出，分页数据的输出，列表数据的输出，
 * 分页数据输出又包括只输出当前页数据和全部数据的两种模式
 * @author biaoping.yin
 * created on 2005-5-18
 * version 1.0
 */
public class PageModel extends PagerTag implements ModelObject
{
    /**存储分页信息*/
    private PageInfo pageInfo;

    /**数据获取接口*/
    private DataInfo dataInfo;

    /**如果不倒出全部记录，则直接从页面dataSet中获取数据*/
    private Stack dataSet;

    /**
     * 控制dataSet的初始化，当要求导出全部记录时调用initial方法时修改first的值
     */
    private boolean first = true;

    /**
     * 初始化所有的生成文件的必要参数
     * @param pageInfo
     * @param dataInfo
     * @param dataSet
     * @param pageContext
     */
    public void setParameter(PageInfo pageInfo,
                             DataInfo dataInfo,
                             Stack dataSet,
                             PageContext pageContext
                             )
    {
        this.pageInfo = pageInfo;
        this.setPageContext(pageContext);

        this.dataSet = dataSet;
        this.dataInfo = dataInfo;
    }

    /**
     * 初始化，分别获取每一页的数据，页面显示模式为分页时调用该方法
     * @param offset int
     * @param maxPageItem int
     */
    public void initial(long offset, int maxPageItem)
    {
    	HttpServletRequest request = getHttpServletRequest();
		HttpSession session = request.getSession(false) ;
        //如果不是列表
        //if(pageInfo.isEportAll())
        {
            if (dataInfo instanceof DefaultDataInfoImpl) {

                DataInfo defaultDataInfo = (DefaultDataInfoImpl) dataInfo;
                //分页列表的形式pagerTag不会为空，但是详细信息显示时就会为空

                defaultDataInfo.initial(pageInfo.getStatement(),
                                        pageInfo.getDbName(),
                                        offset,
                                        maxPageItem,
                                        false,
                                        request);

                //loadClassData(defaultDataInfo.getListItemsFromDB(), null);
            }
            else if (dataInfo instanceof DataInfoImpl) {
                dataInfo.initial(pageInfo.getSortKey(), pageInfo.isList(),
                                 offset,
                                 maxPageItem, false, request);
//                try {
//                    //loadClassData(dataInfo, pageInfo.isList());
//                }
//                catch (LoadDataException ex) {
//                    ex.printStackTrace();
//                }
                //初始化页面的参数
                pageInfo.initContext(offset,dataInfo.getItemCount());
            }
            //设置当前页面数据
            if(first)
            {
	            this.dataSet = new Stack();
	            dataSet.push(this);
            }
            else
            {
                dataSet.push(this);
            }
        }

    }

    /**
     * @return Returns the dataInfo.
     */
    public DataInfo getDataInfo()
    {
        return dataInfo;
    }

    /**
     * @param dataInfo The dataInfo to set.
     */
    public void setDataInfo(DataInfo dataInfo)
    {
        this.dataInfo = dataInfo;
    }

    /**
     * @return Returns the dataSet.
     */
    public Stack getDataSet()
    {
        return dataSet;
    }

    /**
     * @param dataSet The dataSet to set.
     */
    public void setDataSet(Stack dataSet)
    {
        this.dataSet = dataSet;
    }


    /**
     * @return Returns the pageInfo.
     */
    public PageInfo getPageInfo()
    {
        return pageInfo;
    }

    /**
     * @param pageInfo The pageInfo to set.
     */
    public void setPageInfo(PageInfo pageInfo)
    {
        this.pageInfo = pageInfo;
    }
}
