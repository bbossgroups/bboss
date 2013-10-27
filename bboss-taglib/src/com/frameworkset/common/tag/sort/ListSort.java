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


package com.frameworkset.common.tag.sort;

import java.util.List;

import com.frameworkset.common.tag.pager.DataComparatorImpl;
/**
 * 负责排序，实现对各种集合的排序  
 * @author biaoping.yin
 * 2005-3-25
 * version 1.0
 */
 
public class ListSort extends BaseSort 
{
	/**
	 * ClassData集合排序比较器
	 */
    public DataComparatorImpl theDataComparatorImpl = DataComparatorImpl.newInstance();
    
    /**
     * 构建器
     */
    public ListSort() 
    {
     
    }
    
    /**
     * 实现对List中ClassData的排序功能
     * desc参数决定排序的秩序：
     * true :降序
     * false:升序
     * 
     * sortKey参数：排序字段
     * @param classDatas
     * @param sortKey
     * @param desc
     */
    public void sort(List classDatas, String sortKey, boolean desc) 
    {    	
		theDataComparatorImpl.reset();
    	theDataComparatorImpl.setDesc(desc);
    	theDataComparatorImpl.setSortKey(sortKey);    	 
    	sortCollection(classDatas,theDataComparatorImpl);
    }
}

